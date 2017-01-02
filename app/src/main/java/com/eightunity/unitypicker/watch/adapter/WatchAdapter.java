package com.eightunity.unitypicker.watch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.model.watch.Watch;
import com.eightunity.unitypicker.ui.recyclerview.BaseRecyclerViewType;
import com.eightunity.unitypicker.ui.recyclerview.RecycleClickListener;
import com.eightunity.unitypicker.watch.adapter.holder.WatchNoHoler;
import com.eightunity.unitypicker.watch.adapter.holder.WatchSectionHolder;
import com.eightunity.unitypicker.watch.adapter.holder.WatchViewHolder;
import com.eightunity.unitypicker.watch.adapter.model.WatchItem;
import com.eightunity.unitypicker.watch.adapter.model.WatchNoItem;
import com.eightunity.unitypicker.watch.adapter.model.WatchSectionItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class WatchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BaseRecyclerViewType> watchesDetails;

    private OnItemClickListener onItemClickListener;

    public WatchAdapter() {
        watchesDetails = new ArrayList<>();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == WatchDetailType.TYPE_SECTION) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_section_watch, parent, false);
            return new WatchSectionHolder(view);
        } else if (viewType == WatchDetailType.TYPE_DETAIL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_watch, parent, false);
            return new WatchViewHolder(view);
        } else if (viewType == WatchDetailType.TYPE_NOWATCH) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_no_watch, parent, false);
            return new WatchNoHoler(view);
        }

        throw new NullPointerException("View Type " + viewType + " doesn't match with any existing order detail type");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseRecyclerViewType viewType = watchesDetails.get(position);
        if (holder instanceof WatchSectionHolder) {
            WatchSectionHolder sectionHolder = (WatchSectionHolder)holder;
            WatchSectionItem secItem = (WatchSectionItem)viewType;
            setUpSection(sectionHolder, secItem);
        } else if (holder instanceof WatchViewHolder) {
            WatchViewHolder viewHolder = (WatchViewHolder)holder;
            WatchItem item = (WatchItem)viewType;
            setUpView(viewHolder, item, position);
        } else if (holder instanceof WatchNoHoler) {
            WatchNoHoler viewHolder = (WatchNoHoler)holder;
            WatchNoItem item = (WatchNoItem)viewType;
            setUpNoWatch(viewHolder, item);
        }
    }

    private void setUpNoWatch(WatchNoHoler viewHolder, WatchNoItem item) {
        // Nothing to do ...
    }

    private void setUpSection(WatchSectionHolder viewHolder, WatchSectionItem item) {
        viewHolder.sectionImage.setImageResource(item.getImageSection());
        viewHolder.sectionDesc.setText(item.getDescSection());
    }

    private void setUpView(WatchViewHolder viewHolder, final WatchItem item, final int position) {
        viewHolder.watchStatusImage.setImageResource(item.getWatchStatusImage());
        viewHolder.watchStatusView.setText(item.getWatchStatusDesc());
        viewHolder.searchWordView.setText(item.getSearchWord());
        viewHolder.countFoundView.setText(item.getCountFound());
        viewHolder.timeDescView.setText(item.getTimeDesc());

        viewHolder.watchStatusImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onRowItemClickListener(item);
            }
        });
        viewHolder.watchStatusView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onRowItemClickListener(item);
            }
        });
        viewHolder.searchWordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onRowItemClickListener(item);
            }
        });
        viewHolder.countFoundView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onRowItemClickListener(item);
            }
        });
        viewHolder.timeDescView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onRowItemClickListener(item);
            }
        });


        viewHolder. optionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onOptionItemClickListener(item);
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return watchesDetails.size();
    }

    @Override
    public int getItemViewType(int position) {
        return watchesDetails.get(position).getType();
    }

    public interface OnItemClickListener {
        void onRowItemClickListener(WatchItem item);

        void onOptionItemClickListener(WatchItem item);
    }

    public void setWatchDetails(List<BaseRecyclerViewType> watchesDetails) {
        this.watchesDetails = watchesDetails;
    }
}
