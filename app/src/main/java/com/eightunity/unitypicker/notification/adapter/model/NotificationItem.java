package com.eightunity.unitypicker.notification.adapter.model;

import com.eightunity.unitypicker.notification.adapter.NotificationDetailType;
import com.eightunity.unitypicker.ui.recyclerview.BaseRecyclerViewType;

/**
 * Created by chokechaic on 12/28/2016.
 */

public class NotificationItem extends BaseRecyclerViewType {

    private int matchId;
    private int searchId;
    private String searchWord;
    private String titleContent;
    private String webName;
    private String timeDesc;
    private boolean watchingStatus;
    private String url;

    public NotificationItem() {
        super(NotificationDetailType.TYPE_DETAIL);
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getSearchId() {
        return searchId;
    }

    public void setSearchId(int searchId) {
        this.searchId = searchId;
    }

    public String getSearchWord() {
        return searchWord;
    }

    public void setSearchWord(String searchWord) {
        this.searchWord = searchWord;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean getWatchingStatus() {
        return watchingStatus;
    }

    public void setWatchingStatus(boolean watchingStatus) {
        this.watchingStatus = watchingStatus;
    }


}
