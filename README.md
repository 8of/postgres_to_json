# PostgreSQL to JSON

Export whole PostgreSQL database as a JSON file

Java project with Maven.

## How to use

Compile & run with 6 parameters:

`db_host db_port db_name db_sceheme user_name user_password`

Example (replace with your own params):

`192.168.30.1 5432 superdatabase superdatabase admin secret`

Alternatively you can replace `DBConnectData credentials = new DBConnectData(args);` line with different constructor `new DBConnectData(host: ..., port: ..., ...)` and compile.

File `tables.json` will appear in `output` project folder.
