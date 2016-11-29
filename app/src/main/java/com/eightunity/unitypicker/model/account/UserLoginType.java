package com.eightunity.unitypicker.model.account;

import java.util.List;

/**
 * Created by chokechaic on 10/11/2016.
 */
public class UserLoginType {

    public static final int NO_CODE_LOGIN_TYPE_CODE = 0;
    public static final int FACEBOOK_LOGIN_TYPE_CODE = 1;
    public static final int GOOGLE_LOGIN_TYPE_CODE = 2;

    public static int getCode(List<String> providers) {
        for (String provider : providers) {
            if (provider.contains("facebook")) {
                return FACEBOOK_LOGIN_TYPE_CODE;
            } else if (provider.contains("google")) {
                return UserLoginType.GOOGLE_LOGIN_TYPE_CODE;
            }
        }
        return NO_CODE_LOGIN_TYPE_CODE;
    }
}
