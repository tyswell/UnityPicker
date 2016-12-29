package com.eightunity.unitypicker.model.match;

import android.os.Parcelable;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by chokechaic on 9/14/2016.
 */
@Parcel
public class Match implements Parcelable{

    private Integer searchId;
    private String searchWord;
    private String searchType;
    private int countFound;
    private boolean watchingStatus;
    private String timeDesc;

    private List<MatchDetail> matchDetails;

    public Match() {

    }

    protected Match(android.os.Parcel in) {
        searchId = in.readInt();
        searchWord = in.readString();
        searchType = in.readString();
        matchDetails = in.createTypedArrayList(MatchDetail.CREATOR);
        timeDesc = in.readString();
        countFound = in.readInt();
        watchingStatus = in.readByte() != 0;
    }

    public static final Creator<Match> CREATOR = new Creator<Match>() {
        @Override
        public Match createFromParcel(android.os.Parcel in) {
            return new Match(in);
        }

        @Override
        public Match[] newArray(int size) {
            return new Match[size];
        }
    };

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

    public List<MatchDetail> getMatchDetails() {
        return matchDetails;
    }

    public void setMatchDetails(List<MatchDetail> matchDetails) {
        this.matchDetails = matchDetails;
    }

    public Integer getSearchId() {
        return searchId;
    }

    public void setSearchId(Integer searchId) {
        this.searchId = searchId;
    }

    public int getCountFound() {
        return countFound;
    }

    public void setCountFound(int countFound) {
        this.countFound = countFound;
    }

    public boolean getWatchingStatus() {
        return watchingStatus;
    }

    public void setWatchingStatus(boolean watchingStatus) {
        this.watchingStatus = watchingStatus;
    }

    public String getTimeDesc() {
        return timeDesc;
    }

    public void setTimeDesc(String timeDesc) {
        this.timeDesc = timeDesc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(searchId);
        dest.writeString(searchWord);
        dest.writeString(searchType);
        dest.writeTypedList(matchDetails);
        dest.writeString(timeDesc);
        dest.writeInt(countFound);
        dest.writeByte((byte) (watchingStatus ? 1 : 0));
    }
}
