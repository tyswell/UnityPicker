package com.eightunity.unitypicker.model.match;

import android.os.Parcelable;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by chokechaic on 9/14/2016.
 */
@Parcel
public class Match implements Parcelable{

    private String searchWord;
    private String searchType;

    private List<MatchDetail> matchDetails;

    public Match() {

    }

    protected Match(android.os.Parcel in) {
        searchWord = in.readString();
        searchType = in.readString();
        matchDetails = in.createTypedArrayList(MatchDetail.CREATOR);
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(searchWord);
        dest.writeString(searchType);
        dest.writeTypedList(matchDetails);
    }
}
