package com.eightunity.unitypicker.notification;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.eightunity.unitypicker.R;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class NotificationViewHolder extends RecyclerView.ViewHolder {

    public TextView searchWordView;
    public TextView titleContentView;
    public TextView webNameView;
    public TextView timeDescView;
    public ImageButton optionBtn;

    public NotificationViewHolder(View itemView) {
        super(itemView);

        searchWordView = (TextView) itemView.findViewById(R.id.searchWordView);
        titleContentView = (TextView) itemView.findViewById(R.id.titleContentView);
        webNameView = (TextView) itemView.findViewById(R.id.webNameView);
        timeDescView = (TextView) itemView.findViewById(R.id.timeDescView);
        optionBtn = (ImageButton) itemView.findViewById(R.id.optionBtn);
    }
}
