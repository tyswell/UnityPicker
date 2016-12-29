package com.eightunity.unitypicker.notification.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.model.Notificaiton.Notification;
import com.eightunity.unitypicker.notification.adapter.holder.NotificationNoHolder;
import com.eightunity.unitypicker.notification.adapter.holder.NotificationViewHolder;
import com.eightunity.unitypicker.notification.adapter.model.NotificationItem;
import com.eightunity.unitypicker.notification.adapter.model.NotificationNoItem;
import com.eightunity.unitypicker.ui.recyclerview.BaseRecyclerViewType;
import com.eightunity.unitypicker.ui.recyclerview.RecycleClickListener;
import com.eightunity.unitypicker.watch.adapter.WatchAdapter;
import com.eightunity.unitypicker.watch.adapter.WatchDetailType;
import com.eightunity.unitypicker.watch.adapter.holder.WatchNoHoler;
import com.eightunity.unitypicker.watch.adapter.holder.WatchViewHolder;
import com.eightunity.unitypicker.watch.adapter.model.WatchItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BaseRecyclerViewType> notifications;
    private OnItemClickListener onItemClickListener;

    public NotificationAdapter() {
        notifications = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == NotificationDetailType.TYPE_DETAIL) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_notification, parent, false);
            return new NotificationViewHolder(view);
        } else if (viewType == NotificationDetailType.TYPE_NO_ROW) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_row_no_notificaiton, parent, false);
            return new NotificationNoHolder(view);
        }

        throw new NullPointerException("View Type " + viewType + " doesn't match with any existing order detail type");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseRecyclerViewType viewType = notifications.get(position);
        if (holder instanceof NotificationViewHolder) {
            NotificationViewHolder viewHolder = (NotificationViewHolder)holder;
            NotificationItem viewItem = (NotificationItem)viewType;
            setUpView(viewHolder, viewItem);
        } else if (holder instanceof NotificationNoHolder) {
            NotificationNoHolder viewHolder = (NotificationNoHolder)holder;
            NotificationNoItem viewItem = (NotificationNoItem)viewType;
            setUpNoRow(viewHolder, viewItem);
        }
    }

    private void setUpNoRow(NotificationNoHolder viewHolder, NotificationNoItem viewItem) {
        // Nothing to do ...
    }

    private void setUpView(NotificationViewHolder viewHolder, final NotificationItem viewItem) {
        viewHolder.searchWordView.setText(viewItem.getSearchWord());
        viewHolder.titleContentView.setText(viewItem.getTitleContent());
        viewHolder.webNameView.setText(viewItem.getWebName());
        viewHolder.timeDescView.setText(viewItem.getTimeDesc());

        viewHolder.searchWordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onRowItemClickListener(viewItem);
            }
        });
        viewHolder.searchWordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onRowItemClickListener(viewItem);
            }
        });
        viewHolder.searchWordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onRowItemClickListener(viewItem);
            }
        });
        viewHolder.searchWordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onRowItemClickListener(viewItem);
            }
        });

        viewHolder.optionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onOptionItemClickListener(viewItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    @Override
    public int getItemViewType(int position) {
        return notifications.get(position).getType();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onRowItemClickListener(NotificationItem item);

        void onOptionItemClickListener(NotificationItem item);
    }

    public void setNotifications(List<BaseRecyclerViewType> notifications) {
        this.notifications = notifications;
    }

}
