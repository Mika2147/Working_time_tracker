package com.mikaauer.wtmauthorizationserver;

public class Credentials {
    private String username;
    private String password;
     private boolean isAdmin;

    public Credentials(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
