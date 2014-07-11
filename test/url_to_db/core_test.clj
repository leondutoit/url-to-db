(ns url-to-db.core-test
  (:require
    [url-to-db.core :refer :all]
    [cheshire.core :as json])
  (:use [midje.sweet]
        [midje.util :only [testable-privates]]))

(testable-privates url-to-db.core create-exists-query add-hash)

(def ^:const correct-exists-query
  (str "select exists( select * from information_schema.tables "
        "where table_schema = 'public' and table_name = 'test_table');"))

(fact "correct query is generated to check if table exists"
  (create-exists-query "test_table") => correct-exists-query)

;; TODO (Leon) moar
