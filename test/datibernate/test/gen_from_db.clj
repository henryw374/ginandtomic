(ns datibernate.test.gen-from-db
  (:require [datibernate.gen-from-db :as totest]
            )
  (:use [clojure.test]
        ))

(comment  
  (deftest test-splitting-ident-into-name-and-entity-happy
    (is
     (= true (let [result (totest/add-name-and-entity-to-attribute {:db/ident :user/username})]
               (prn result)
               (and (= (:name result) "username") (= "user" (:entity result))) ))))

  (deftest test-finding-and-grouping-attributes
    (is (= 2
           (let [attributes [{:db/ident :user/username} {:db/ident :user/email}]]
             (.size ( (totest/find-and-group-entities attributes) "user"))))))

  (deftest create-simple-class-is-valid-java
    "rubbish assertion - need to include janino or some such compiler"
    (is (=  "User.java" (let [filename-and-contents (totest/create-class {} ["user" [{:db/ident :user/password, :db/valueType :db.type/string,
                                                                                      :db/cardinality :db.cardinality/one, :db/doc "A persons password",
                                                                                      :name "password", :entity "user"}]] )]
                          (prn filename-and-contents)
                          (filename-and-contents :filename)))))


  (deftest convert-value-type-returns
    (is (= "Ref" (totest/convert-value-type :db.type/ref))))

  (deftest test-create-enum-happy
    (is (= "User" (:entity (totest/create-enum [ "user.activeStatus" [{:db/ident :user.activeStatus/INACTIVE, :name "INACTIVE", :entity "user.activeStatus"} {:db/ident :user.activeStatus/ACTIVE, :name "ACTIVE", :entity "user.activeStatus"}]]))))))

;(run-tests)

