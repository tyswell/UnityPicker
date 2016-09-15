package com.eightunity.unitypicker.match;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.ui.recyclerview.RecycleClickListener;

/**
 * Created by chokechaic on 9/14/2016.
 */
public class MatchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String TAG = "MatchViewHolder";

    public TextView titleContentView;
    public TextView webNameView;
    public TextView timeDescView;
    public ImageButton optionBtn;
    private RecycleClickListener recycleClick;
    private RecycleClickListener optionClick;

    public MatchViewHolder(View itemView, RecycleClickListener recycleClick, RecycleClickListener optionClick) {
        super(itemView);

        titleContentView = (TextView) itemView.findViewById(R.id.titleContentView);
        webNameView = (TextView) itemView.findViewById(R.id.webNameView);
        timeDescView = (TextView) itemView.findViewById(R.id.timeDescView);
        optionBtn = (ImageButton) itemView.findViewById(R.id.optionBtn);

        titleContentView.setOnClickListener(this);
        webNameView.setOnClickListener(this);
        timeDescView.setOnClickListener(this);
        optionBtn.setOnClickListener(this);

        this.recycleClick = recycleClick;
        this.optionClick = optionClick;

    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "v.getId()="+v.getId());
        if (v.getId() == optionBtn.getId()) {
            optionClick.onClick(v, getAdapterPosition());
        } else {
            recycleClick.onClick(v, getAdapterPosition());
        }
    }
}