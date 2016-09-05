package com.eightunity.unitypicker.model.account;

import android.os.Build;

/**
 * Created by deksen on 9/2/16 AD.
 */
public class Device {

    private String tokenNotification;
    private String deviceModel;

    public String getDeviceModel() {
        return getDeviceName();
    }

    private String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

}
