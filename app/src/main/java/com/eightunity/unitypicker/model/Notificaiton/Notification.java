package com.eightunity.unitypicker.model.Notificaiton;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class Notification implements Parcelable{

    private int matchId;
    private int searchId;
    private String searchWord;
    private String titleContent;
    private String webName;
    private String timeDesc;
    private boolean watchingStatus;
    private String url;

    public Notification(){}

    public int getSearchId() {
        return searchId;
    }

    public void setSearchId(int searchId) {
        this.searchId = searchId;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public String getTimeDesc() {
        return timeDesc;
    }

    public void setTimeDesc(String timeDesc) {
        this.timeDesc = timeDesc;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(matchId);
        dest.writeString(searchWord);
        dest.writeString(titleContent);
        dest.writeString(webName);
        dest.writeString(timeDesc);
        dest.writeString(url);
        dest.writeByte((byte) (watchingStatus ? 1 : 0));
    }

    protected Notification(Parcel in) {
        matchId = in.readInt();
        searchWord = in.readString();
        titleContent = in.readString();
        webName = in.readString();
        timeDesc = in.readString();
        url = in.readString();
        watchingStatus = in.readByte() != 0;
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>() {
        @Override
        public Notification createFromParcel(Parcel in) {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size) {
            return new Notification[size];
        }
    };
}
