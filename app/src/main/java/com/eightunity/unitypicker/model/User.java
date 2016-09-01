package com.eightunity.unitypicker.model;

/**
 * Created by deksen on 5/15/16 AD.
 */
public class User {

    private String username;
    private String token;
    private String tokenType;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
}
