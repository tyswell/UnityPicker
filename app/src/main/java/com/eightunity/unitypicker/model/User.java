package com.eightunity.unitypicker.model;

/**
 * Created by deksen on 5/15/16 AD.
 */
public class User {

    private String nativeUserId;
    private String facebookUserId;
    private Boolean isFacebookLogin;
    private String token;

    public String getNativeUserId() {
        return nativeUserId;
    }

    public void setNativeUserId(String nativeUserId) {
        this.nativeUserId = nativeUserId;
    }

    public String getFacebookUserId() {
        return facebookUserId;
    }

    public void setFacebookUserId(String facebookUserId) {
        this.facebookUserId = facebookUserId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getFacebookLogin() {
        return isFacebookLogin;
    }

    public void setFacebookLogin(Boolean facebookLogin) {
        isFacebookLogin = facebookLogin;
    }
}
