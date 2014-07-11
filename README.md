# url-to-db

A Clojure library designed to get data from API endpoints and store it in databases.

### Usage

1. Create a edn file with your database config
`$ echo "{:host host :port port :user user :pw pw}" >> db-config.edn`

2. Compile the uberjar
`lein uberjar`

3. Run be supplying needed options
`java -jar target/url-to-db.jar -u someurl -t sometable -c db-config.edn`

### Limitations

The API endpoint you call has to return JSON.
Only PostgreSQL is supported.

### License

Copyright © 2014 Leon du Toit

MIT.
