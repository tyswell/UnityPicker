package com.eightunity.unitypicker.match.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.eightunity.unitypicker.R;

/**
 * Created by chokechaic on 12/29/2016.
 */

public class MatchHeaderHolder extends RecyclerView.ViewHolder {

    public TextView searchWordView;
    public ImageView imageSearchType;
    public TextView descSearchType;
    public ImageView watchStatusImage;
    public TextView watchStatusView;
    public TextView countFoundView;
    public TextView timeDescView;
    public Button stopWatchBtn;
    public Button deleteWatchingBtn;

    public MatchHeaderHolder(View item) {
        super(item);

        searchWordView = (TextView) item.findViewById(R.id.searchWordView);
        imageSearchType = (ImageView) item.findViewById(R.id.imageSearchType);
        descSearchType = (TextView) item.findViewById(R.id.descSearchType);
        watchStatusImage = (ImageView) item.findViewById(R.id.watchStatusImage);
        watchStatusView = (TextView) item.findViewById(R.id.watchStatusView);
        countFoundView = (TextView) item.findViewById(R.id.countFoundView);
        timeDescView = (TextView) item.findViewById(R.id.timeDescView);
        stopWatchBtn = (Button) item.findViewById(R.id.stopWatchBtn);
        deleteWatchingBtn = (Button) item.findViewById(R.id.deleteWatchingBtn);
    }
}
