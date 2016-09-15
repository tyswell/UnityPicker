package com.eightunity.unitypicker.watch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.model.watch.Watch;
import com.eightunity.unitypicker.ui.recyclerview.RecycleClickListener;

import java.util.List;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class WatchAdapter extends RecyclerView.Adapter<WatchViewHolder> {

    private List<Watch> watches;
    private Context context;
    private RecycleClickListener recycleClick;
    private RecycleClickListener optionClick;

    public WatchAdapter(Context context, List<Watch> watches, RecycleClickListener recycleClick, RecycleClickListener optionClick) {
        this.watches = watches;
        this.context = context;
        this.recycleClick = recycleClick;
        this.optionClick = optionClick;
    }

    @Override
    public WatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recycler_row_watch,
                parent,
                false);
        return new WatchViewHolder(itemView, recycleClick, optionClick);
    }

    @Override
    public void onBindViewHolder(WatchViewHolder holder, int position) {
        Watch watch = watches.get(position);

        holder.searchWordView.setText(watch.getSearchWord());
        holder.searchType.setText(watch.getSearchType());
        holder.timeDescView.setText(watch.getTimeDesc());
    }

    @Override
    public int getItemCount() {
        return watches.size();
    }

    public void removeAt(int position) {
        watches.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, watches.size());
    }
}
