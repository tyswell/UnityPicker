package com.eightunity.unitypicker.watch.adapter.model;

import com.eightunity.unitypicker.ui.recyclerview.BaseRecyclerViewType;
import com.eightunity.unitypicker.watch.adapter.WatchDetailType;

/**
 * Created by chokechaic on 12/28/2016.
 */

public class WatchItem extends BaseRecyclerViewType {

    private int searchId;
    private int watchStatusImage;
    private String watchStatusDesc;
    private String searchWord;
    private String searchType;
    private String countFound;
    private String timeDesc;
    private boolean watchingStatus;

    public WatchItem() {
        super(WatchDetailType.TYPE_DETAIL);
    }

    public int getSearchId() {
        return searchId;
    }

    public void setSearchId(int searchId) {
        this.searchId = searchId;
    }

    public int getWatchStatusImage() {
        return watchStatusImage;
    }

    public void setWatchStatusImage(int watchStatusImage) {
        this.watchStatusImage = watchStatusImage;
    }

    public String getWatchStatusDesc() {
        return watchStatusDesc;
    }

    public void setWatchStatusDesc(String watchStatusDesc) {
        this.watchStatusDesc = watchStatusDesc;
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

    public boolean getWatchingStatus() {
        return watchingStatus;
    }

    public void setWatchingStatus(boolean watchingStatus) {
        this.watchingStatus = watchingStatus;
    }
}
