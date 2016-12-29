package com.eightunity.unitypicker.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

/**
 * Created by chokechaic on 12/29/2016.
 */

public class WrapContentLinearLayoutManager extends LinearLayoutManager {

    public WrapContentLinearLayoutManager(Context context) {
        super(context);
    }

    public WrapContentLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public WrapContentLinearLayoutManager(RecyclerView view) {
        super(view);
    }

    public WrapContentLinearLayoutManager(RecyclerView view, int orientation, boolean reverseLayout) {
        super(view, orientation, reverseLayout);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        try {
            super.onLayoutChildren(recycler, state);
        } catch (IndexOutOfBoundsException e) {
            Log.e("probe", "meet a IOOBE in RecyclerView");
        }
    }
}
