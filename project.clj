(defproject url-to-db "0.1.0-SNAPSHOT"
  :description "GET data from a URL and store it in a db table"
  :url "TODO (Leon) once it exists on github"
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [
    [org.clojure/clojure "1.5.1"] ; TODO (Leon) update to 1.6.0
    [com.fasterxml.jackson.core/jackson-core "2.4.1"]
    [com.fasterxml.jackson.dataformat/jackson-dataformat-smile "2.4.1"]
    [com.fasterxml.jackson.dataformat/jackson-dataformat-cbor "2.4.1"]
    [tigris "0.1.1"]
    [cheshire "5.3.1"]
    [clj-http "0.3.4"]
    [org.clojure/tools.cli "0.3.1"]
    [org.clojure/java.jdbc "0.3.2"]
    [org.postgresql/postgresql "9.2-1002-jdbc4"]
    [pandect "0.3.4"]]
  :main url-to-db.core
  :uberjar-name "url-to-db.jar"
  :repl-options {:init-ns init}
  :profiles {:dev {:dependencies [[midje "1.6.3"]]
                   :plugins [[lein-midje "3.1.1"]]}}
  :uberjar {:aot :all})
