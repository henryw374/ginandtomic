(ns datibernate.reflect
  ( :require [clojure.string :as str])
  (:import [datibernate.ReturnType] [ datibernate.Attribute]))

(defn- typesym
  "Given a typeref, create a legal Clojure symbol version of the
   type's name."
  [t] t
  )

(comment (-> (typename t)
      (str/replace "[]" "<>")
      (symbol)))

(defn get-annotation-value [method annotation-class]
  (and (.isAnnotationPresent method annotation-class) (.value (.getAnnotation method annotation-class))))

(defrecord Method
  [name return-type declaring-class parameter-types exception-types return-type-val attribute-val])

(defn- method->map
  [^java.lang.reflect.Method method]
  (Method.
   (symbol (.getName method))
   (typesym (.getReturnType method))
   (typesym (.getDeclaringClass method))
   (vec (map typesym (.getParameterTypes method)))
   (vec (map typesym (.getExceptionTypes method)))
   (get-annotation-value method datibernate.ReturnType)
      (get-annotation-value method datibernate.Attribute)
   ))

(defn- declared-methods
  "Return a set of the declared constructors of class as a Clojure map."
  [^Class cls]
  (set (map
        method->map
        (.getDeclaredMethods cls))))


(defn reflect-methods [cls]
  {
   :members 
   (declared-methods cls)
   })

