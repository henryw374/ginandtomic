(ns datibernate.query-demo  
  ( :require [datomic.api :only [q db] :as d]
             [datibernate.query :as query]
             [datibernate.gen-from-db :as gen]))

(defn create-db-with-schema
  [^java.lang.String uri ^java.lang.String schema ]
  (do
    (d/create-database uri)
    (let [ conn (d/connect uri)]    
      (do @(d/transact conn (read-string (slurp schema)))
          conn))))

(defn transact-data [conn  ^java.lang.String data]
  (do @(d/transact conn (read-string (slurp data)))
      conn))

(defn adapt-datomic-maps-to-java [results clazz conn]
  (let [adaptor (query/create-dict-getter-adapta clazz)]
    (->> results (map #(d/entity (d/db conn) (first %)) ,,,) (map adaptor ,,,))))

(defn setup-test-environment [f]
  (let [conn (create-db-with-schema "datomic:mem://seattle" "demo-java-client/seattle-schema.dtm")]     
    (f conn)))

(defn gen-java []
  (setup-test-environment (fn [conn] (gen/gen conn "java"))))

(defn populate-test-db []
  (setup-test-environment (fn [conn] (transact-data conn "demo-java-client/seattle-data0.dtm"))))
