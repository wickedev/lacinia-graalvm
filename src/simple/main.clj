(ns simple.main
  (:require [com.walmartlabs.lacinia :as lacinia]
            [criterium.core :as c]
            [simple.schema :as s])
  (:gen-class))

(def query-tmpl
  "query {
     %s {
       title
      authors {
        first_name
        last_name
      }
      subject
      published
    }
  }")

(defn execute-query []
  (let [schema (s/load-schema)
        query "books"
        q-str (format query-tmpl query)]
    (lacinia/execute schema q-str nil nil)))

(defn execute-benchmark-query []
  (let [results (c/benchmark
                 (execute-query)
                 {:verbose true})]
    (c/report-point-estimate
     "Execution time sample mean" (:sample-mean results))
    (c/report-point-estimate "Execution time mean" (:mean results))
    (c/report-point-estimate-sqrt
     "Execution time sample std-deviation" (:sample-variance results))
    (c/report-point-estimate-sqrt
     "Execution time std-deviation" (:variance results))
    (c/report-point-estimate
     "Execution time lower quantile"
     (:lower-q results) (:tail-quantile results))
    (c/report-point-estimate
     "Execution time upper quantile"
     (:upper-q results) (- 1.0 (:tail-quantile results)))
    (when-let [overhead (:overhead results)]
      (when (pos? overhead)
        (c/report-point-estimate "Overhead used" [overhead])))
    (c/report-outliers results)))

(defn -main []
  #_(benchmark-query-execute)
  (prn (execute-query)))

(comment
  (-main))