package com.eightunity.unitypicker.model.watch;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by chokechaic on 9/6/2016.
 */
public class Watch implements Parcelable{

    private int id;
    private String searchWord;
    private String searchType;
    private String timeDesc;

    public Watch(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(searchWord);
        dest.writeString(searchType);
        dest.writeString(timeDesc);
    }

    protected Watch(Parcel in) {
        id = in.readInt();
        searchWord = in.readString();
        searchType = in.readString();
        timeDesc = in.readString();
    }

    public static final Creator<Watch> CREATOR = new Creator<Watch>() {
        @Override
        public Watch createFromParcel(Parcel in) {
            return new Watch(in);
        }

        @Override
        public Watch[] newArray(int size) {
            return new Watch[size];
        }
    };

}
