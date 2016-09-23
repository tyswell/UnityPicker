package com.eightunity.unitypicker.model.match;

import android.os.Parcelable;

import org.parceler.Parcel;

/**
 * Created by chokechaic on 9/14/2016.
 */
@Parcel
public class MatchDetail implements Parcelable{

    private int matchID;
    private String titleContent;
    private String webName;
    private String timeDesc;
    private String url;

    public MatchDetail(){

    }

    protected MatchDetail(android.os.Parcel in) {
        matchID = in.readInt();
        titleContent = in.readString();
        webName = in.readString();
        timeDesc = in.readString();
        url = in.readString();
    }

    public static final Creator<MatchDetail> CREATOR = new Creator<MatchDetail>() {
        @Override
        public MatchDetail createFromParcel(android.os.Parcel in) {
            return new MatchDetail(in);
        }

        @Override
        public MatchDetail[] newArray(int size) {
            return new MatchDetail[size];
        }
    };

    public int getMatchID() {
        return matchID;
    }

    public void setMatchID(int matchID) {
        this.matchID = matchID;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(matchID);
        dest.writeString(titleContent);
        dest.writeString(webName);
        dest.writeString(timeDesc);
        dest.writeString(url);
    }
}
