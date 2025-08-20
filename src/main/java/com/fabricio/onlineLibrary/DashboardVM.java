package com.fabricio.onlinelibrary;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;

public class DashboardVM { 

    private String authenticatedUser = "";

    public String getAuthenticatedUser() {
        return authenticatedUser;
    }

    public void setAuthenticatedUser(String authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    @Command 
    public void goToPeople() {
        Executions.sendRedirect("/people");
    }

    @Command 
    public void goToBooks() {
        Executions.sendRedirect("/books");
    }
}
