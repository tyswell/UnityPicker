package com.eightunity.unitypicker.model.account;

import android.os.Build;

/**
 * Created by deksen on 9/2/16 AD.
 */
public class Device {

    private int osTypeCode;
    private String tokenNotification;
    private String deviceModel;

    public int getOsTypeCode() {
        return osTypeCode;
    }

    public void setOsTypeCode(int osTypeCode) {
        this.osTypeCode = osTypeCode;
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
