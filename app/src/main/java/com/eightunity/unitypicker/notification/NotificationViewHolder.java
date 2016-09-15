package com.eightunity.unitypicker.notification;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.database.EMatchingDAO;
import com.eightunity.unitypicker.ui.recyclerview.RecycleClickListener;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class NotificationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private static final String TAG = "NotificationViewHolder";

    public TextView searchWordView;
    public TextView titleContentView;
    public TextView webNameView;
    public TextView timeDescView;
    public ImageButton optionBtn;
    private RecycleClickListener recycleClick;
    private RecycleClickListener optionClick;

    public NotificationViewHolder(View itemView, RecycleClickListener recycleClick, RecycleClickListener optionClick) {
        super(itemView);

        searchWordView = (TextView) itemView.findViewById(R.id.searchWordView);
        titleContentView = (TextView) itemView.findViewById(R.id.titleContentView);
        webNameView = (TextView) itemView.findViewById(R.id.webNameView);
        timeDescView = (TextView) itemView.findViewById(R.id.timeDescView);
        optionBtn = (ImageButton) itemView.findViewById(R.id.optionBtn);

        searchWordView.setOnClickListener(this);
        titleContentView.setOnClickListener(this);
        webNameView.setOnClickListener(this);
        timeDescView.setOnClickListener(this);
        searchWordView.setOnClickListener(this);
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
