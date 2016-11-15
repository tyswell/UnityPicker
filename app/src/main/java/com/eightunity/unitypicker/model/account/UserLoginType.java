package com.eightunity.unitypicker.model.account;

/**
 * Created by chokechaic on 10/11/2016.
 */
public class UserLoginType {

    public static final int FACEBOOK_LOGIN_TYPE_CODE = 1;
    public static final int GOOGLE_LOGIN_TYPE_CODE = 2;

    public static int getCode(String providerID) {
        if ("FACEBOOK".equals(providerID)) {
            return FACEBOOK_LOGIN_TYPE_CODE;
        } else if ("GOOGLE".equals(providerID)) {
            return GOOGLE_LOGIN_TYPE_CODE;
        }
        return 0;
    }
}
