package com.eightunity.unitypicker.model.account;

import android.os.Build;

/**
 * Created by deksen on 9/2/16 AD.
 */
public class Device {

    private int osType;
    private String tokenNotification;
    private String deviceModel;

    public int getOsType() {
        return osType;
    }

    public void setOsType(int osType) {
        this.osType = osType;
    }

    public String getTokenNotification() {
        return tokenNotification;
    }

    public void setTokenNotification(String tokenNotification) {
        this.tokenNotification = tokenNotification;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }
}
