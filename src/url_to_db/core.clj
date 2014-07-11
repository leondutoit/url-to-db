(ns url-to-db.core
  (:gen-class)
  (:require
    [clj-http.client :as client]
    [clojure.tools.cli :refer [parse-opts]]
    [clojure.edn :as edn]
    [clojure.java.jdbc :as jdbc]
    [pandect.core :refer :all]
    [cheshire.core :as json])
  (:import
    [org.postgresql.util PGobject]))

(def cli-options
  [["-u" "--url URL" "API URL endpoint"]
   ["-t" "--table TABLENAME" "table to write data to"]
   ["-c" "--config FILENAME" "edn file with port, host, user, pw"]])

(defn- read-db-config! [file-name]
  "this must be an extensible data notation (edn) formatted file"
  (edn/read-string (slurp file-name)))

(defn- get-data [url]
  (let [response (client/get url)]
    (json/parse-string (:body response))))

(defn- create-postgres-db [config-file]
  "spec used to connect to the db"
  (let [{:keys [host port user pw db]} (read-db-config! config-file)]
    {:classname "org.postgresql.Driver"
     :subprotocol "postgresql"
     :subname (str "//" host ":" port "/" db)
     :user user
     :password (str pw)}))

(defn- create-exists-query [table-name]
  (str "select exists( select * from information_schema.tables "
        "where table_schema = 'public' and table_name = '" table-name "');"))

(defn- table-created? [db-spec table-name]
  "checks only in public schema"
  (let [query (create-exists-query table-name)]
    (:exists (first (jdbc/query db-spec [query])))))

(defn- create-table [table-name]
    (jdbc/create-table-ddl
      table-name
        [:data "json"]
        [:data_hash "text" "primary key"]))

(defn- add-hash [raw-data]
  (map #(assoc {} :data_hash (sha1 (str %)) :data %) raw-data))

(defn- make-postgresql-object [json-entry]
  (doto (PGobject.)
    (.setType "json")
    (.setValue (json/generate-string json-entry))))

(defn- write-entry! [db-spec table-name json-data]
  (let [data (make-postgresql-object (:data json-data))
        hashed (:data_hash json-data)]
    (try
      (jdbc/insert! db-spec table-name {:data data :data_hash hashed})
      (catch org.postgresql.util.PSQLException e
        (println (str "INFO: ignoring duplicate entry - " e))))))

(defn- write-data-to-db! [db-spec table-name json-data]
  (doseq [data json-data]
    (write-entry! db-spec table-name data)))

(defn -main [& args]
  (let [{:keys [options arguments errors summary]} (parse-opts args cli-options)
        {:keys [url table config]} options
        json-data (add-hash (get-data url))
        postgres (create-postgres-db config)]
    (cond
      (true? (table-created? postgres table))
        (println "table already created: going to insert new data now...")
      :else
        (do
          (println "creating a table for you...")
          (jdbc/db-do-commands postgres (create-table table))))
    (write-data-to-db! postgres table json-data)))
