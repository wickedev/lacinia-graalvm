(ns simple.schema
  "Adapted from this tutorial:
  https://lacinia.readthedocs.io/en/latest/tutorial/designer-data.html#id4"
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [com.walmartlabs.lacinia.schema :as schema]
            [com.walmartlabs.lacinia.util :as util]))

(defn- ->entity
  [data entity-name]
  (->> (get data entity-name)
       (reduce #(assoc %1 (:id %2) %2) {})))

(defn- resolve-book-authors
  [authors-map _context _args book]
  (->> (:authors book) (map authors-map)))

(defn- resolve-book-subject
  [_ _context _args book]
  (:subject book))

(defn- resolve-book-by-id
  [books _context args _value]
  (let [{:keys [id]} args]
    (get books id)))

(defn- resolve-books
  [books _context _args _value]
  books)

(defn- resolver-map []
  (let [data (-> (io/resource "data.edn")
                 slurp
                 edn/read-string)
        books-map (->entity data :books)
        authors-map (->entity data :authors)]
    {:Book/authors (partial resolve-book-authors authors-map)
     :Book/subject (partial resolve-book-subject nil)
     :query/book-by-id (partial resolve-book-by-id books-map)
     :query/books (partial resolve-books (:books data))}))

(defn load-schema []
  (-> (io/resource "schema.edn")
      slurp
      edn/read-string
      (util/attach-resolvers (resolver-map))
      schema/compile))
