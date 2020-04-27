package org.example;

public class App {
    public static void main( String[] args )  {
        DBConnectData credentials = new DBConnectData(args);
        PostgresToJSONConverter converter = new PostgresToJSONConverter(credentials);
        converter.writeDBtoJSON();
    }
}
