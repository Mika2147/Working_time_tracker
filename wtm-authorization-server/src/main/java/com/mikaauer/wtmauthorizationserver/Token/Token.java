package com.mikaauer.wtmauthorizationserver.Token;

public class Token {
    private String username;
    private String tokenString;
    private Long createdTimestamp;

    private boolean isAdmin;

    public Token(String username, String tokenString, Long createdTimestamp, boolean isAdmin) {
        this.username = username;
        this.tokenString = tokenString;
        this.createdTimestamp = createdTimestamp;
        this.isAdmin = isAdmin;
    }

    public String getUsername() {
        return username;
    }

    public String getTokenString() {
        return tokenString;
    }

    public Long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}
