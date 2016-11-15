package com.eightunity.unitypicker.model.server.user;

import java.util.List;

public class LoginReceive {
	
	private String tokenId;
	private List<Integer> searchingIds;
	private Integer userLoginType; 
	private Device device;
	private FacebookUser facebookUser;
		
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public List<Integer> getSearchingIds() {
		return searchingIds;
	}
	public void setSearchingIds(List<Integer> searchingIds) {
		this.searchingIds = searchingIds;
	}
	public Device getDevice() {
		return device;
	}
	public void setDevice(Device device) {
		this.device = device;
	}
	public FacebookUser getFacebookUser() {
		return facebookUser;
	}
	public void setFacebookUser(FacebookUser facebookUser) {
		this.facebookUser = facebookUser;
	}
	public Integer getUserLoginType() {
		return userLoginType;
	}
	public void setUserLoginType(Integer userLoginType) {
		this.userLoginType = userLoginType;
	}
	
}
