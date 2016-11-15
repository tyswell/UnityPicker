package com.eightunity.unitypicker.model.server.user;

import com.eightunity.unitypicker.model.server.search.Searching;

import java.util.List;


public class LoginResponse {
	
	private List<Searching> searching;
	
	public List<Searching> getSearching() {
		return searching;
	}
	public void setSearching(List<Searching> searching) {
		this.searching = searching;
	}

	
}
