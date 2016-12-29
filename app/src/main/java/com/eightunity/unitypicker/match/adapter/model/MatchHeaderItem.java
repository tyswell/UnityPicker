package com.eightunity.unitypicker.match.adapter.model;

import com.eightunity.unitypicker.match.adapter.MatchDetailType;
import com.eightunity.unitypicker.ui.recyclerview.BaseRecyclerViewType;

/**
 * Created by chokechaic on 12/29/2016.
 */

public class MatchHeaderItem extends BaseRecyclerViewType {

    private Integer searchId;
    private String searchWord;
    private Integer searchTypeCode;
    private Integer imageSearchType;
    private String searchType;
    private Boolean watchStatus;
    private Integer imageWatchStatus;
    private String watchStatusDesc;
    private String countFound;
    private String timeDesc;

    public MatchHeaderItem() {
        super(MatchDetailType.TYPE_HEADER);
    }

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

    public Integer getSearchTypeCode() {
        return searchTypeCode;
    }

    public void setSearchTypeCode(Integer searchTypeCode) {
        this.searchTypeCode = searchTypeCode;
    }

    public Integer getImageSearchType() {
        return imageSearchType;
    }

    public void setImageSearchType(Integer imageSearchType) {
        this.imageSearchType = imageSearchType;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public Boolean getWatchStatus() {
        return watchStatus;
    }

    public void setWatchStatus(Boolean watchStatus) {
        this.watchStatus = watchStatus;
    }

    public Integer getImageWatchStatus() {
        return imageWatchStatus;
    }

    public void setImageWatchStatus(Integer imageWatchStatus) {
        this.imageWatchStatus = imageWatchStatus;
    }

    public String getWatchStatusDesc() {
        return watchStatusDesc;
    }

    public void setWatchStatusDesc(String watchStatusDesc) {
        this.watchStatusDesc = watchStatusDesc;
    }

    public String getCountFound() {
        return countFound;
    }

    public void setCountFound(String countFound) {
        this.countFound = countFound;
    }

    public String getTimeDesc() {
        return timeDesc;
    }

    public void setTimeDesc(String timeDesc) {
        this.timeDesc = timeDesc;
    }
}
