package org.example;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

final public class PostgresToJSONConverter {

    private DBConnectData connectData;
    private Set<String> tablesToIgnore;
    private JSONManager jsonManager;

    public PostgresToJSONConverter(DBConnectData connectData, JSONManager jsonManager) {
        this.connectData = connectData;
        this.jsonManager = jsonManager;
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
            jsonManager.writeToFile(fullDatabase);
        } catch (SQLException e) {
            System.out.println("PostgreSQL connection failure");
            e.printStackTrace();
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
        String query = new StringBuilder()
                .append("SELECT * FROM ")
                .append(connectData.getSchemeName())
                .append(".")
                .append(tableName)
                .toString();
        ResultSet resultSet = statement.executeQuery(query);

        return jsonManager.getTable(resultSet);
    }

}
