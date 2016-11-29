package com.eightunity.unitypicker.model.server.search;

import java.sql.Timestamp;

public class AddSearchingResponse {
	
	private Integer searchingId;
	private Timestamp createDate;
	
	public Integer getSearchingId() {
		return searchingId;
	}
	public void setSearchingId(Integer searchingId) {
		this.searchingId = searchingId;
	}
	public Timestamp getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}
	
	

}
