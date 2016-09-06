package com.eightunity.unitypicker.watch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.eightunity.unitypicker.R;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class WatchViewHolder extends RecyclerView.ViewHolder {

    public TextView searchWordView;
    public TextView searchType;
    public TextView timeDescView;
    public ImageButton optionBtn;

    public WatchViewHolder(View itemView) {
        super(itemView);

        searchWordView = (TextView) itemView.findViewById(R.id.searchWordView);
        searchType = (TextView) itemView.findViewById(R.id.searchType);
        timeDescView = (TextView) itemView.findViewById(R.id.timeDescView);
        optionBtn = (ImageButton) itemView.findViewById(R.id.optionBtn);
    }
}
