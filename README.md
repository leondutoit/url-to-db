# url-to-db

A Clojure library designed to get data from API endpoints and store it in databases. For those times when you need data to be in your database. Why? Because you might want to join it with other data already there, or maybe keeping it in a file is not good enough or whatever.

You give the utility a URL, table name and DB config. If the table does not exist it will be created for you. Each JSON entry is hashed and that hash is the primary key of the table, so duplicate entries are ignored. This is _very_ useful in cases where you do not want duplicate rows, but obviously not if duplicates are allowed. In the future I will probably add the option to enable or disable this feature.

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
