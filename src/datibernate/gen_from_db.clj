(ns datibernate.gen-from-db
  (:require [datomic.api :only [q db] :as d]
            [clojure.string :as str]))

(comment
  (in-ns 'datibernate.gen-from-db)

  (use '[datomic.api :only [q db] :as d])
(use 'clojure.pprint)

;; store database uri
(def uri "datomic:mem://seattle")

;; create database
(d/create-database uri)

;; connect to database
(def conn (d/connect uri))

(def schema-tx (read-string (slurp "demo-java-client/seattle-schema.dtm")))
@(d/transact conn schema-tx)
(def data-tx (read-string (slurp "demo-java-client/seattle-data0.dtm")))
@(d/transact conn data-tx)

(use 'datibernate.query)

 (def x (->> (d/q '[:find ?c :where [?c :community/name ?x]] (d/db conn)) ffirst (d/entity (d/db conn)))
   )

(gen conn "java")
((create-dict-getter-adapta generated.Community) x)

)



(defn query-for-attributes [conn]
  (map #(d/entity (d/db conn) (first %)) (d/q '[:find  ?c :where
                                                [?c :db/ident ?ident]] (d/db conn))))

(defn split-keyword-based-on-char [char-str a-keyword]
  (let [as-str (str a-keyword)]
    (seq (.split (.substring as-str 1 (.length as-str)) char-str))))

(def split-on-slash (partial split-keyword-based-on-char "/"))
(def split-on-dot (partial split-keyword-based-on-char "\\."))

(defn keyword-to-string [kw]
  (apply str (rest (str kw))))


(defn split-key-and-reinsert-as-values [split-fn key-name zip-keys orig-map]
  (let [split-str (split-fn (key-name orig-map))
        dict-of-attr-name-and-value (zipmap zip-keys split-str)]
    (merge-with #(%1) dict-of-attr-name-and-value orig-map)))

; revisit this - need dates etc etc
(def value-types {:db.type/uuid "String"
                  :db.type/bigint "BigInteger"
                  :db.type/uri "String"                  
                  :db.type/keyword "String"
                  :db.type/bytes "String"
                  :db.type/string "String"
                  :db.type/instant "java.util.Date"
                  :db.type/fn "String"
                  :db.type/long "Long"
                  :db.type/bigdec "BigDecimal"
                  :db.type/boolean "Boolean"
                  :db.type/double "Double"
                  :db.type/float "Float" })

(defn convert-cardinality [cardinality converted-value-type]
  (if (= :db.cardinality/one cardinality)
    converted-value-type
    (if converted-value-type
      (str  "Collection<" converted-value-type ">")
      (str "Collection"))))

(defn toupper-first-char [a-str]
  (apply str (str/upper-case  (first a-str)) (rest a-str)))

(defn create-java-method-from-attribute [attribute]
  (let [value-type (:db/valueType attribute)
        str-version-of-value-type  (or (and (= :db.type/ref value-type ) (toupper-first-char (attribute :name))) (value-types value-type))
        str-version-of-cardinality (convert-cardinality (:db/cardinality attribute) str-version-of-value-type)]    
    (str
     "@Attribute(value=\""  (keyword-to-string (:db/ident attribute)) "\")\n"
     (or (and (= :db.type/ref value-type )
              (str "@ReturnType(value=" str-version-of-value-type ".class)\n" ))
         "")
         str-version-of-cardinality " get" (toupper-first-char (:name attribute )) "();\n")))

(defn create-class [enums-grouped-by-entity [entity methods]]
  (let [method-strings (doall (map create-java-method-from-attribute methods))
        upper-entity (toupper-first-char entity)]
    {:filename (str upper-entity ".java")
     :contents (str
                (apply str
                       (apply str "package generated;\nimport datomic.db.*;\nimport java.util.Collection;\nimport datibernate.ReturnType;\nimport datibernate.Attribute;\npublic interface " upper-entity "{\n" method-strings )
                       (or  (enums-grouped-by-entity entity) ""))
                "\n}")}))

(defn extract-and-groupby-entity [attributes]
  (group-by :entity
            (map (partial split-key-and-reinsert-as-values split-on-slash  :db/ident [:entity :name])
                 attributes)))

(defn enum? [[name _]]
  (.contains name "."))

(defn create-enum [[name enum-values]]
  (let [[entity-name enum-name]  (split-on-dot (keyword name))
        enum-vals-with-commas (map (comp #(str % ",\n") :name) (butlast enum-values))]
    {:entity entity-name
     :enum-str (str
                (apply str "enum " (toupper-first-char enum-name) " {\n" enum-vals-with-commas)
                (:name  (last enum-values))
                "};\n")}))

(defn gen [conn dest]
  (let [attributes (query-for-attributes conn)
        entities-and-enums (extract-and-groupby-entity attributes)
        enums-to-be (filter enum? entities-and-enums)
        entities (filter (comp not enum?) entities-and-enums)
        enums-grouped-by-entity (group-by :entity (map create-enum enums-to-be))
        enums-grouped-with-vals-ready-to-print (reduce (fn [results [k v]] (assoc results k (map :enum-str v))) {} enums-grouped-by-entity)]
    (doseq [{:keys  [filename contents]}
            (filter #(not (#{"Db.java" "Fressian.java"} (:filename %))) (keep (partial create-class enums-grouped-with-vals-ready-to-print)
                          entities))]
      (spit (str dest "/generated/" filename) contents))))

