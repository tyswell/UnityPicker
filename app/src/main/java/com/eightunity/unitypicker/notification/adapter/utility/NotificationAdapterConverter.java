package com.eightunity.unitypicker.notification.adapter.utility;

import com.eightunity.unitypicker.model.Notificaiton.Notification;
import com.eightunity.unitypicker.model.watch.Watch;
import com.eightunity.unitypicker.notification.adapter.model.NotificationItem;
import com.eightunity.unitypicker.notification.adapter.model.NotificationNoItem;
import com.eightunity.unitypicker.ui.recyclerview.BaseRecyclerViewType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chokechaic on 12/28/2016.
 */

public class NotificationAdapterConverter {

    public static List<BaseRecyclerViewType> getNotificationDetail(List<Notification> notifications) {
        List<BaseRecyclerViewType> results = new ArrayList<>();
        if (notifications != null && notifications.size() > 0) {
            for (Notification notification : notifications) {
                NotificationItem item = new NotificationItem();
                item.setMatchId(notification.getMatchId());
                item.setSearchId(notification.getSearchId());
                item.setSearchWord(notification.getSearchWord());
                item.setTitleContent(notification.getTitleContent());
                item.setTimeDesc(notification.getTimeDesc());
                item.setUrl(notification.getUrl());
                item.setWatchingStatus(notification.getWatchingStatus());
                item.setWebName(notification.getWebName());
                results.add(item);
            }
        } else {
            NotificationNoItem item = new NotificationNoItem();
            results.add(item);
        }

        return results;
    }

}
