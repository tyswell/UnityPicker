package com.eightunity.unitypicker.match.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.eightunity.unitypicker.R;

/**
 * Created by chokechaic on 1/4/2017.
 */

public class MatchCountHolder extends RecyclerView.ViewHolder {

    public TextView countFoundView;

    public MatchCountHolder(View item) {
        super(item);

        countFoundView = (TextView) item.findViewById(R.id.countFoundView);
    }
}
