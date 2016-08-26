package com.eightunity.unitypicker.model;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class ModelService<T> {

    private String username;
    private String token;

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
}
