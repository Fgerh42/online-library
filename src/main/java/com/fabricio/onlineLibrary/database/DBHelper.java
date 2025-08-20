package com.fabricio.onlinelibrary.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class DBHelper {
 
    private static final Path DB_DIR = Paths.get("/var/lib/tomcat9/webapps/onlineLibrary/data");
    private static final Path DB_PATH = DB_DIR.resolve("library.db");
    private static final String URL = "jdbc:sqlite:" + DB_PATH.toString();

    static {
        try { 

            Files.createDirectories(DB_DIR);
 
            Class.forName("org.sqlite.JDBC");

            initializeDatabase();
            System.out.println("SQLite DB path: " + DB_PATH.toAbsolutePath());
        } catch (Exception e) { 
            e.printStackTrace();
        }
    }

    private DBHelper() {}  

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initializeDatabase() {
    String sql = "CREATE TABLE IF NOT EXISTS users (" +
                 "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                 "username TEXT NOT NULL UNIQUE," +
                 "name TEXT NOT NULL," +
                 "password TEXT NOT NULL" +
                 ");";
    try (Connection conn = getConnection();
         java.sql.Statement stmt = conn.createStatement()) {
        stmt.execute(sql);
        System.out.println("Users table ready");
    } catch (SQLException e) {
        e.printStackTrace();
    }
} 

}
