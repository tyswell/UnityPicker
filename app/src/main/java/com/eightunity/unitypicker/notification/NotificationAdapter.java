package com.eightunity.unitypicker.notification;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.model.Notificaiton.Notification;
import com.eightunity.unitypicker.ui.recyclerview.RecycleClickListener;

import java.util.List;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationViewHolder> {

    private List<Notification> notifications;
    private Context context;
    private RecycleClickListener recycleClick;
    private RecycleClickListener optionClick;

    public NotificationAdapter(Context context, List<Notification> notifications, RecycleClickListener recycleClick, RecycleClickListener optionClick) {
        this.notifications = notifications;
        this.context = context;
        this.recycleClick = recycleClick;
        this.optionClick = optionClick;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recycler_row_notification,
                parent,
                false);
        return new NotificationViewHolder(itemView, recycleClick, optionClick);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);

        holder.searchWordView.setText(notification.getSearchWord());
        holder.titleContentView.setText(notification.getTitleContent());
        holder.webNameView.setText(notification.getWebName());
        holder.timeDescView.setText(notification.getTimeDesc());

    }

    public void removeAt(int position) {
        notifications.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, notifications.size());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

}
