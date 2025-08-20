package com.fabricio.onlinelibrary;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

  
import java.sql.SQLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;

import com.fabricio.onlinelibrary.models.User;
import java.sql.Statement;
import java.sql.ResultSet;

import org.zkoss.zk.ui.util.Clients;


public class UserDAO {

        private static final Path DB_DIR = Paths.get("/var/lib/tomcat9/webapps/onlineLibrary/data");
    private static final Path DB_PATH = DB_DIR.resolve("library.db");
    private static final String URL = "jdbc:sqlite:" + DB_PATH.toString(); 

    public boolean createUser(String name, String username, String password) throws Exception {
        try (Connection conn = DriverManager.getConnection(URL)) {
            String sql = "INSERT INTO users (name, username, password) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setString(2, username);
                stmt.setString(3, password);  
                stmt.executeUpdate();
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, name, username, password FROM users";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id")); 
                u.setUsername(rs.getString("username"));
                u.setName(rs.getString("name"));
                u.setPassword(rs.getString("password"));
                users.add(u);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean updateUser(User user) throws Exception {
          try (Connection conn = DriverManager.getConnection(URL)) {
            String sql = "UPDATE users SET username=?, name=?, password=? WHERE id=?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, user.getUsername());
                stmt.setString(2, user.getName());
                stmt.setString(3, user.getPassword());   
                stmt.setInt(4, user.getId()); 


                stmt.executeUpdate();
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

        private void printToConsole(String msg) {
        msg = msg.replace("\\", "\\\\")
                .replace("'", "\\'")
                .replace("\n", "\\n")
                .replace("\r", "");
        Clients.evalJavaScript("console.log('" + msg + "');");
    }

    public void delete(User user) {
    try (Connection conn = DriverManager.getConnection(URL);
         PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {
        ps.setInt(1, user.getId());
        ps.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

}
