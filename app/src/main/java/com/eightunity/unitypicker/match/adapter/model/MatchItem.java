package com.eightunity.unitypicker.match.adapter.model;

import com.eightunity.unitypicker.match.adapter.MatchDetailType;
import com.eightunity.unitypicker.ui.recyclerview.BaseRecyclerViewType;

/**
 * Created by chokechaic on 12/29/2016.
 */

public class MatchItem extends BaseRecyclerViewType{

    private int matchID;
    private int searchId;
    private String titleContent;
    private String webName;
    private String timeDesc;
    private boolean watchingStatus;
    private String url;

    public MatchItem() {
        super(MatchDetailType.TYPE_DETAIL);
    }

    public int getMatchID() {
        return matchID;
    }

    public void setMatchID(int matchID) {
        this.matchID = matchID;
    }

    public int getSearchId() {
        return searchId;
    }

    public void setSearchId(int searchId) {
        this.searchId = searchId;
    }

    public String getTitleContent() {
        return titleContent;
    }

    public void setTitleContent(String titleContent) {
        this.titleContent = titleContent;
    }

    public String getWebName() {
        return webName;
    }

    public void setWebName(String webName) {
        this.webName = webName;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
