package com.eightunity.unitypicker.match.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.ui.recyclerview.RecycleClickListener;

/**
 * Created by chokechaic on 12/29/2016.
 */

public class MatchViewHolder extends RecyclerView.ViewHolder {

    public TextView titleContentView;
    public TextView webNameView;
    public TextView timeDescView;
    public ImageButton optionBtn;

    public MatchViewHolder(View itemView) {
        super(itemView);

        titleContentView = (TextView) itemView.findViewById(R.id.titleContentView);
        webNameView = (TextView) itemView.findViewById(R.id.webNameView);
        timeDescView = (TextView) itemView.findViewById(R.id.timeDescView);
        optionBtn = (ImageButton) itemView.findViewById(R.id.optionBtn);
    }
}
