package org.example;

final public class DBConnectData {

    private String host;
    private int port;
    private String dbName;
    private String schemeName;
    private String user;
    private String password;

    public DBConnectData(String host, int port, String dbName, String schemeName, String user, String password) {
        this.host = host;
        this.port = port;
        this.dbName = dbName;
        this.schemeName = schemeName;
        this.user = user;
        this.password = password;
    }

    public DBConnectData(String[] args) throws IllegalArgumentException, NumberFormatException {
        if (args.length != 6) {
            throw new IllegalArgumentException("Exactly 6 params required: host, port, database name, scheme name, user, password.");
        }

        try {
            this.host = args[0];
            this.port = Integer.parseInt(args[1]);
            this.dbName = args[2];
            this.schemeName = args[3];
            this.user = args[4];
            this.password = args[5];
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Port argument should be integer.");
        }
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDbName() {
        return dbName;
    }

    public String getSchemeName() {
        return schemeName;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

}
