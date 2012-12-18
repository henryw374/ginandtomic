# ginandtomic

A Clojure library which can adapt java objects onto datomic entities. This is done through two capabilities:
1) From a datomic database, generate java interfaces representing the entities in the database.
2) If a datomic query returns an entity id, then the library offers a function which given the id and a java interface, will make the entity appear to be an object implementing the interface. 
 
See 'Usage' below for how to use this with the Communities dataset from the Datomic samples (with a couple of name changes to help with java compatibility).

This library is not packaged for general usage yet. 

Future work may include the generation of builder classes that can faciliate raw creation of new, and derivation from existing, Datomic attributes. These could then be persisted to storage. It may also include a java-like way of creating queries.

## Usage - to run the demo
lein deps
lein repl
(use 'datibernate.query-demo)
(gen-java)
exit repl
;; now that the java code has been generated, you can have a play at using it...
lein repl ; generated java code is compiled on repl start
(require 'datibernate.query-demo)
(in-ns 'datibernate.query-demo)
(def conn (datibernate.query-demo/populate-test-db))
;; do a query to get back community entity ids
(def communities  (d/q '[:find ?c :where [?c :community/name ?x]] (d/db conn)))
;; turn the results into instances of recently generated Community class
(def adapted-communities (adapt-datomic-maps-to-java communities generated.Community conn))
;; find the name of the first community
(->> adapted-communities first .getName)
;; get the org type of the second community - its a Java Enum
(->> adapted-communities first .getOrgtype)
;; get the name of the district reachable from the first community
(->> adapted-communities first .getNeighborhood .getDistrict .getName)
;; get a category associated with the first community
(->> adapted-communities first .getCategory first)


## todo's
reverse relationships on java objects?
check mapping from all datomic types to java equivalents
write capability
query capability

## problem areas
; reference to entity maps is maintained... no detached objects as yet
; doing a reify per entity.. dont know if that is a good idea... perm gen fills up?

## License

Distributed under the Eclipse Public License, the same as Clojure.
