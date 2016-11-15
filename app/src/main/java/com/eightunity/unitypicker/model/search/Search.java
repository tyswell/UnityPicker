package com.eightunity.unitypicker.model.search;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class Search {

    private Integer searchId;
    private String searchWord;
    private String searchType;

    public Integer getSearchId() {
        return searchId;
    }

    public void setSearchId(Integer searchId) {
        this.searchId = searchId;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }
}
