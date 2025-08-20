package com.fabricio.onlinelibrary;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.util.Clients;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.fabricio.onlinelibrary.database.DBHelper;
import com.fabricio.onlinelibrary.models.User;

import com.fabricio.onlinelibrary.UserDAO;


public class UsersVM {

    private List<User> userList = new ArrayList<>();
    private User editUser = new User();
    private boolean dialogVisible = false;

    public List<User> getUserList() { return userList; }
    public User getEditUser() { return editUser; }
    public boolean isDialogVisible() { return dialogVisible; }

    public UsersVM() {
        loadUsers();
    }

    private void loadUsers() {
        userList.clear();
 
        try {
 
        UserDAO dao = new UserDAO();
        userList = dao.getAllUsers();
        

        } catch (Exception e) { 
            Clients.showNotification("Erro ao carregar usuários", "error", null, "top_right", 3000);
        }
    }

    @Command
    @NotifyChange({"editUser", "dialogVisible", "dialogTitle"})
    public void openUserDialog(@BindingParam("user") User user) {
        if (user == null) {
            editUser = new User();
        } else {
            editUser = user;
        }
        dialogVisible = true;
    }

    @Command
    @NotifyChange({"userList", "dialogVisible"})
    public void saveUser() {
        
        try {
        UserDAO dao = new UserDAO();

        if(editUser.getId() == null) {  
           dao.createUser(editUser.getName(), editUser.getUsername(), editUser.getPassword());
        } else { 
           dao.updateUser(editUser);
        }
            loadUsers();
            dialogVisible = false;
        } catch (Exception e) {
            printToConsole(e.toString());
            e.printStackTrace();
            Clients.showNotification("Erro ao salvar usuário", "error", null, "top_right", 3000);
        }
    }

    @Command
    @NotifyChange("dialogVisible")
    public void closeDialog() {
        dialogVisible = false;
    }

    public String getDialogTitle() {
    if (editUser.getId() == null) {
        return "Novo usuário";
    }
    if (editUser.getId() != null && editUser.getName() != null && !editUser.getName().isEmpty()) {
        return editUser.getName();
    }
    return "Usuário";
}
    private void printToConsole(String msg) {
        msg = msg.replace("\\", "\\\\")
                .replace("'", "\\'")
                .replace("\n", "\\n")
                .replace("\r", "");
        Clients.evalJavaScript("console.log('" + msg + "');");
    }

    @Command
    @NotifyChange("userList")
    public void deleteUser(@BindingParam("user") User u) {
        if (u != null) { 
            UserDAO dao = new UserDAO();
            dao.delete(u);
 
            userList.remove(u);
        }
    }
}
