package org.example;

public class App {
    public static void main( String[] args )  {
        DBConnectData credentials = new DBConnectData(args);
        JSONManager jsonManager = new JSONManager("output/tables.json");
        PostgresToJSONConverter converter = new PostgresToJSONConverter(credentials, jsonManager);
        converter.writeDBtoJSON();
    }
}
