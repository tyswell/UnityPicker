package com.eightunity.unitypicker.match;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.model.Notificaiton.Notification;
import com.eightunity.unitypicker.model.match.MatchDetail;
import com.eightunity.unitypicker.notification.NotificationViewHolder;
import com.eightunity.unitypicker.ui.recyclerview.RecycleClickListener;

import java.util.List;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class MatchAdapter extends RecyclerView.Adapter<MatchViewHolder> {

    private List<MatchDetail> matchDetails;
    private Context context;
    private RecycleClickListener recycleClick;
    private RecycleClickListener optionClick;

    public MatchAdapter(Context context, List<MatchDetail> matchDetails, RecycleClickListener recycleClick, RecycleClickListener optionClick) {
        this.matchDetails = matchDetails;
        this.context = context;
        this.recycleClick = recycleClick;
        this.optionClick = optionClick;
    }

    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recycler_row_match,
                parent,
                false);
        return new MatchViewHolder(itemView, recycleClick, optionClick);
    }

    @Override
    public void onBindViewHolder(MatchViewHolder holder, int position) {
        MatchDetail matchDetail = matchDetails.get(position);

        holder.titleContentView.setText(matchDetail.getTitleContent());
        holder.webNameView.setText(matchDetail.getWebName());
        holder.timeDescView.setText(matchDetail.getTimeDesc());
    }

    @Override
    public int getItemCount() {
        return matchDetails.size();
    }

    public void removeAt(int position) {
        matchDetails.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, matchDetails.size());
    }

}
