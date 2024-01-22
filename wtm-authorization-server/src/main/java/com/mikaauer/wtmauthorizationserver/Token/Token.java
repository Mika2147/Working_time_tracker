package com.mikaauer.wtmauthorizationserver.Token;

public class Token {
    private String username;
    private String tokenString;
    private Long createdTimestamp;

    public Token(String username, String tokenString, Long createdTimestamp) {
        this.username = username;
        this.tokenString = tokenString;
        this.createdTimestamp = createdTimestamp;
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
}
