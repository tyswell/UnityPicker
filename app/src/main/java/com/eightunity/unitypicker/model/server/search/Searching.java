package com.eightunity.unitypicker.model.server.search;

import java.sql.Timestamp;
import java.util.Date;

public class Searching {

	private String tokenId;
	private Integer searchingId;
	private String description;
	private Integer searchTypeCode;
	private Timestamp createDate;

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getSearchTypeCode() {
		return searchTypeCode;
	}

	public void setSearchTypeCode(Integer searchTypeCode) {
		this.searchTypeCode = searchTypeCode;
	}

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
