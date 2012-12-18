(defproject datibernate "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
 :java-source-paths ["java"]
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :aot [datibernate.query-demo]
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [com.datomic/datomic-free "0.8.3546"]
                 [org.clojure/clojure-contrib "1.2.0"]]
  
  
  )
