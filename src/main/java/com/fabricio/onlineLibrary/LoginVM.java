package com.fabricio.onlinelibrary;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.util.Clients; 
import org.zkoss.zk.ui.Executions; 

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.zkoss.zk.ui.util.Clients;

import com.fabricio.onlinelibrary.database.DBHelper;


public class LoginVM {
    private String username;
    private String password;
 
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
     
      
    @Command
    @NotifyChange("message")
    public void login() {
        if (checkLogin(username, password)) {
            Clients.showNotification("Sucesso!", "info", null, "top_right", 2000, true); 
            Executions.sendRedirect("/dashboard");
        } else {  
              Clients.showNotification("Usuário e/ou senha não encontrados", "error", null, "top_right", 2000, true);
        }
    }

 private boolean checkLogin(String username, String password) {
    String sql = "SELECT name FROM users WHERE username = ? AND password = ?";
    try (Connection conn = DBHelper.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, username);
        pstmt.setString(2, password);

        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) { 
            String user = rs.getString("name");
            Clients.evalJavaScript("localStorage.setItem('authenticatedUser', '" + user + "');"); 
            return true;
        }

    } catch (Exception e) {
        printToConsole(e.toString());
        e.printStackTrace();
    }
    return false;
}

    private void printToConsole(String msg) {
    msg = msg.replace("\\", "\\\\")
             .replace("'", "\\'")
             .replace("\n", "\\n")
             .replace("\r", "");
    Clients.evalJavaScript("console.log('" + msg + "');");
}
}
