package com.eightunity.unitypicker.model.server.user;

/**
 * Created by chokechaic on 10/11/2016.
 */
public class FacebookUser {

    private String facebookId;
    private String tokenFacebook;

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookID(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getTokenFacebook() {
        return tokenFacebook;
    }

    public void setTokenFacebook(String tokenFacebook) {
        this.tokenFacebook = tokenFacebook;
    }
}
