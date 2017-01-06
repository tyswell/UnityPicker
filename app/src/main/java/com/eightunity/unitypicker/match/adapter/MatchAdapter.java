package com.eightunity.unitypicker.match.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.match.adapter.holder.MatchCountHolder;
import com.eightunity.unitypicker.match.adapter.holder.MatchHeaderHolder;
import com.eightunity.unitypicker.match.adapter.holder.MatchNoHolder;
import com.eightunity.unitypicker.match.adapter.holder.MatchViewHolder;
import com.eightunity.unitypicker.match.adapter.model.MatchCountItem;
import com.eightunity.unitypicker.match.adapter.model.MatchHeaderItem;
import com.eightunity.unitypicker.match.adapter.model.MatchItem;
import com.eightunity.unitypicker.match.adapter.model.MatchNoItem;
import com.eightunity.unitypicker.ui.recyclerview.BaseRecyclerViewType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class MatchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BaseRecyclerViewType> matchDetails;
    private OnItemClickListener onItemClickListener;

    public MatchAdapter() {
        matchDetails = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MatchDetailType.TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_header_match, parent, false);
            return new MatchHeaderHolder(view);
        } else if (viewType == MatchDetailType.TYPE_DETAIL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_match, parent, false);
            return new MatchViewHolder(view);
        } else if (viewType == MatchDetailType.TYPE_COUNT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_count_match, parent, false);
            return new MatchCountHolder(view);
        } else if (viewType == MatchDetailType.TYPE_NO_ROW) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_no_match, parent, false);
            return new MatchNoHolder(view);
        }

        throw new NullPointerException("View Type " + viewType + " doesn't match with any existing order detail type");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseRecyclerViewType viewType = matchDetails.get(position);
        if (holder instanceof MatchHeaderHolder) {
            MatchHeaderHolder viewHolder = (MatchHeaderHolder)holder;
            MatchHeaderItem viewItem = (MatchHeaderItem)viewType;
            setUpHeader(viewHolder, viewItem);
        } else if (holder instanceof MatchViewHolder) {
            MatchViewHolder viewHolder = (MatchViewHolder)holder;
            MatchItem viewItem = (MatchItem)viewType;
            setUpView(viewHolder, viewItem);
        } else if (holder instanceof MatchCountHolder) {
            MatchCountHolder viewHolder = (MatchCountHolder)holder;
            MatchCountItem viewItem = (MatchCountItem)viewType;
            setUpCount(viewHolder, viewItem);
        } else if (holder instanceof MatchNoHolder) {
            MatchNoHolder viewHolder = (MatchNoHolder)holder;
            MatchNoItem viewItem = (MatchNoItem)viewType;
            setUpNoRow(viewHolder, viewItem);
        }
    }

    private void setUpCount(MatchCountHolder viewHolder, MatchCountItem item) {
        viewHolder.countFoundView.setText(item.getCountFound());
    }

    private void setUpHeader(MatchHeaderHolder viewHolder, final MatchHeaderItem item) {
        viewHolder.searchWordView.setText(item.getSearchWord());
        viewHolder.imageSearchType.setImageResource(item.getImageSearchType());
        viewHolder.descSearchType.setText(item.getSearchType());
        viewHolder.watchStatusImage.setImageResource(item.getImageWatchStatus());
        viewHolder.watchStatusView.setText(item.getWatchStatusDesc());
        viewHolder.timeDescView.setText(item.getTimeDesc());

        if (!item.getWatchStatus()) {
            viewHolder.stopWatchBtn.setVisibility(View.GONE);
        } else {
            viewHolder.stopWatchBtn.setVisibility(View.VISIBLE);
        }

        viewHolder.stopWatchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onStopWatchClickListener(item);
            }
        });
        viewHolder.deleteWatchingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onDeleteClickListener(item);
            }
        });
    }

    private void setUpView(MatchViewHolder viewHolder, final MatchItem item) {
        viewHolder.titleContentView.setText(item.getTitleContent());
        viewHolder.webNameView.setText(item.getWebName());
        viewHolder.timeDescView.setText(item.getTimeDesc());

        viewHolder.titleContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onRowItemClickListener(item);
            }
        });
        viewHolder.webNameView.setOnClickListener(new View.OnClickListener() {
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

        viewHolder.optionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onOptionItemClickListener(item);
            }
        });
    }

    private void setUpNoRow(MatchNoHolder viewHolder, MatchNoItem viewItem) {
        // Nothing to do ...
    }

    @Override
    public int getItemCount() {
        return matchDetails.size();
    }

    @Override
    public int getItemViewType(int position) {
        return matchDetails.get(position).getType();
    }


    public interface OnItemClickListener {
        void onStopWatchClickListener(MatchHeaderItem item);

        void onDeleteClickListener(MatchHeaderItem item);

        void onRowItemClickListener(MatchItem item);

        void onOptionItemClickListener(MatchItem item);
    }

    public void setMatchDetails(List<BaseRecyclerViewType> matchDetails) {
        this.matchDetails = matchDetails;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
