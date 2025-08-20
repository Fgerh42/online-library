package com.fabricio.onlinelibrary;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;

public class RegisterVM {

    private String name;
    private String username;
    private String password;
    private String message; // for success or error feedback

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    @Command
    @NotifyChange("message")
    public void register() { 
        if (name == null || name.trim().isEmpty() ||
            username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty()) { 
                Clients.showNotification("Por favor, preencha todos os campos", "warning", null, "top_right", 2000, true);
            return;
        }

        try { 
            UserDAO userDAO = new UserDAO();
            boolean success = userDAO.createUser(name, username, password);

            if (success) {
                Clients.showNotification("Sucesso!", "success", null, "top_right", 2000, true); 
                Executions.sendRedirect("/login");
            } else {
                Clients.showNotification("Usuário já existe", "error", null, "top_right", 2000, true); 
            }
        } catch (Exception e) {
                          Clients.showNotification(e.getMessage(), "error", null, "top_right", 2000, true);

        }
    }
}
