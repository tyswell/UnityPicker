package com.eightunity.unitypicker.model.server.user;

import java.util.List;

public class LoginReceive {
	
	private String tokenId;
	private List<Integer> searchingIds;
	private Device device;
		
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
	
}
