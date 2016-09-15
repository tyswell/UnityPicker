package com.eightunity.unitypicker.watch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.ui.recyclerview.RecycleClickListener;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class WatchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView searchWordView;
    public TextView searchType;
    public TextView timeDescView;
    public ImageButton optionBtn;
    private RecycleClickListener recycleClick;
    private RecycleClickListener optionClick;

    public WatchViewHolder(View itemView, RecycleClickListener recycleClick, RecycleClickListener optionClick) {
        super(itemView);

        searchWordView = (TextView) itemView.findViewById(R.id.searchWordView);
        searchType = (TextView) itemView.findViewById(R.id.searchType);
        timeDescView = (TextView) itemView.findViewById(R.id.timeDescView);
        optionBtn = (ImageButton) itemView.findViewById(R.id.optionBtn);

        searchWordView.setOnClickListener(this);
        searchType.setOnClickListener(this);
        timeDescView.setOnClickListener(this);
        optionBtn.setOnClickListener(this);

        this.recycleClick = recycleClick;
        this.optionClick = optionClick;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == optionBtn.getId()) {
            optionClick.onClick(v, getAdapterPosition());
        } else {
            recycleClick.onClick(v, getAdapterPosition());
        }
    }
}
