(ns datibernate.test.query
  (:require [datibernate.query :as totest])
  (:use [clojure.test])
  (:import test.User2))

(deftest reify-a-java-class-with-a-map-return-itself
  (is (= "henry"
          (let [user-adaptor (totest/create-dict-getter-adapta test.User2)]
            (.getName (user-adaptor {:name "henry" :address {} :toy []}))))))

(deftest reify-class-with-map-returning-map-to-be-reifyd
  (is (= "s6" (let [user-adaptor (totest/create-dict-getter-adapta test.User2)]
                (.getZip (.getAddress (user-adaptor {:address {:zip "s6"} :toy []})))))))

(deftest reify-class-with-map-returning-coll-of-entities
  (is (= "buzz" (let [user-adaptor (totest/create-dict-getter-adapta test.User2)]
                  (.getName (first (.getToy (user-adaptor {:toy #{{:name "buzz"}} :address {} }))))))))
                                        

(deftest reify-class-with-an-enum
  (is ( = "MALE" (let [user-adaptor (totest/create-dict-getter-adapta test.User2)]
                   (.toString  (.getGender (user-adaptor {:gender :MALE  :toy #{} :address {} }))))) ))