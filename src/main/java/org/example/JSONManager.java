package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.*;

final public class JSONManager {

    private String filePath;

    public JSONManager(String filePath) {
        this.filePath = filePath;
    }

    void writeToFile(JsonObject fullDatabase) {
        System.out.println("Writing JSON to disk...");
        Gson gson = new Gson();

        try {
            Writer writer = new FileWriter(filePath);
            gson.toJson(fullDatabase, writer);
            writer.flush();
            writer.close();
            System.out.println("JSON written to ".concat(this.filePath));
        } catch (IOException e) {
            System.out.println("Error on writing JSON to disk");
        }
    }

    JsonArray getTable(ResultSet tableResultSet) throws SQLException {
        ResultSetMetaData metadata = tableResultSet.getMetaData();
        int columnCount = metadata.getColumnCount();
        JsonArray table = new JsonArray();

        while (tableResultSet.next()) {
            JsonObject entry = new JsonObject();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metadata.getColumnName(i);
                String value = tableResultSet.getString(i);
                if (value != null) {
                    entry.addProperty(columnName, value);
                }
            }
            table.add(entry);
        }
        return table;
    }

}
