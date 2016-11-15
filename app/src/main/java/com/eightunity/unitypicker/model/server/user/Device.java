package com.eightunity.unitypicker.model.server.user;

/**
 * Created by deksen on 9/2/16 AD.
 */
public class Device {

	private Integer osTypeCode;
	private String tokenNotification;
	private String deviceModel;

	public Integer getOsTypeCode() {
		return osTypeCode;
	}

	public void setOsTypeCode(Integer osTypeCode) {
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
