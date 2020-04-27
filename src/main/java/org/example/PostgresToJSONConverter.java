package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

final public class PostgresToJSONConverter {

    private DBConnectData connectData;
    private Set<String> tablesToIgnore;

    public PostgresToJSONConverter(DBConnectData connectData) {
        this.connectData = connectData;
        this.tablesToIgnore = new HashSet<>(Arrays.asList("django_migrations"));
    }

    public void writeDBtoJSON() {
        String url = new StringBuilder()
                .append("jdbc:postgresql://")
                .append(connectData.getHost())
                .append(":")
                .append(connectData.getPort())
                .append("/")
                .append(connectData.getDbName())
                .toString();

        try (Connection connection = DriverManager.getConnection(url, connectData.getUser(), connectData.getPassword())) {

            System.out.println("Java JDBC PostgreSQL Example");

            System.out.println("Connected to PostgreSQL database");
            Statement statement = connection.createStatement();
            JsonObject fullDatabase = getDatabase(connection, statement);
            writeToFile(fullDatabase);
        } catch (SQLException e) {
            System.out.println("PostgreSQL connection failure");
            e.printStackTrace();
        }
    }

    private void writeToFile(JsonObject fullDatabase) {
        System.out.println("Writing JSON to disk...");
        Gson gson = new Gson();
        String filePath = "output/tables.json";

        try {
            Writer writer = new FileWriter(filePath);
            gson.toJson(fullDatabase, writer);
            writer.flush();
            writer.close();
            System.out.println("JSON written to ".concat(filePath));
        } catch (IOException e) {
            System.out.println("Error on writing JSON to disk");
        }
    }

    private JsonObject getDatabase(Connection connection, Statement statement) throws SQLException {
        System.out.println("Reading records...");
        DatabaseMetaData dbmd = connection.getMetaData();
        JsonObject database = new JsonObject();
        try (ResultSet tables = dbmd.getTables(null, null, "%", new String[] { "TABLE" })) {
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                if (this.tablesToIgnore.contains(tableName)) {
                    System.out.println("TABLE IGNORED: ".concat(tableName));
                } else {
                    database.add(tableName, getTable(tableName, statement));
                }
            }
        } finally {
            return database;
        }
    }

    private JsonArray getTable(String tableName, Statement statement) throws SQLException {
        JsonArray table = new JsonArray();
        String query = new StringBuilder()
                .append("SELECT * FROM ")
                .append(connectData.getSchemeName())
                .append(".")
                .append(tableName)
                .toString();
        ResultSet resultSet = statement.executeQuery(query);

        ResultSetMetaData metadata = resultSet.getMetaData();
        int columnCount = metadata.getColumnCount();

        while (resultSet.next()) {
            JsonObject entry = new JsonObject();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metadata.getColumnName(i);
                String value = resultSet.getString(i);
                if (value != null) {
                    entry.addProperty(columnName, value);
                }
            }
            table.add(entry);
        }
        return table;
    }

}
