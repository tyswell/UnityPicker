package com.eightunity.unitypicker.watch.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.ui.recyclerview.RecycleClickListener;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class WatchViewHolder extends RecyclerView.ViewHolder {

    public ImageView watchStatusImage;
    public TextView watchStatusView;
    public TextView searchWordView;
    public TextView countFoundView;
    public TextView timeDescView;
    public ImageButton optionBtn;

    public WatchViewHolder(View itemView) {
        super(itemView);

        watchStatusImage = (ImageView) itemView.findViewById(R.id.watchStatusImage);
        watchStatusView = (TextView) itemView.findViewById(R.id.watchStatusView);
        searchWordView = (TextView) itemView.findViewById(R.id.searchWordView);
        countFoundView = (TextView) itemView.findViewById(R.id.countFoundView);
        timeDescView = (TextView) itemView.findViewById(R.id.timeDescView);
        optionBtn = (ImageButton) itemView.findViewById(R.id.optionBtn);


    }
}
