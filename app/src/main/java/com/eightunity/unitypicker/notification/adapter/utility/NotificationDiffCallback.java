package com.eightunity.unitypicker.notification.adapter.utility;

import android.support.v7.util.DiffUtil;

import com.eightunity.unitypicker.notification.adapter.model.NotificationItem;
import com.eightunity.unitypicker.ui.recyclerview.BaseRecyclerViewType;
import com.eightunity.unitypicker.watch.adapter.model.WatchSectionItem;

import java.util.List;

/**
 * Created by chokechaic on 12/28/2016.
 */

public class NotificationDiffCallback extends DiffUtil.Callback {

    private List<BaseRecyclerViewType> oldItemList;
    private List<BaseRecyclerViewType> newItemList;

    public NotificationDiffCallback(List<BaseRecyclerViewType> oldItemList,  List<BaseRecyclerViewType> newItemList) {
        this.oldItemList = oldItemList;
        this.newItemList = newItemList;
    }

    @Override
    public int getOldListSize() {
        return oldItemList != null ? oldItemList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newItemList != null ? newItemList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        BaseRecyclerViewType newItem = newItemList.get(newItemPosition);
        BaseRecyclerViewType oldItem = oldItemList.get(oldItemPosition);
        if (newItem.getType() == oldItem.getType()) {
            if (newItem instanceof NotificationItem) {
                NotificationItem newSectionItem = (NotificationItem)newItem;
                NotificationItem oldSectionItem = (NotificationItem)oldItem;

                return newSectionItem.getMatchId() == oldSectionItem.getMatchId();
            }
        }
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        BaseRecyclerViewType newItem = newItemList.get(newItemPosition);
        BaseRecyclerViewType oldItem = oldItemList.get(oldItemPosition);
        if (newItem.getType() == oldItem.getType()) {
            if (newItem instanceof NotificationItem) {
                NotificationItem newSectionItem = (NotificationItem)newItem;
                NotificationItem oldSectionItem = (NotificationItem)oldItem;

                return newSectionItem.getMatchId() == oldSectionItem.getMatchId() &&
                        newSectionItem.getSearchId() == oldSectionItem.getSearchId() &&
                        newSectionItem.getSearchWord().equals(oldSectionItem.getSearchWord()) &&
                        newSectionItem.getTitleContent().equals(oldSectionItem.getTitleContent()) &&
                        newSectionItem.getWebName().equals(oldSectionItem.getWebName()) &&
                        newSectionItem.getTimeDesc().equals(oldSectionItem.getTimeDesc()) &&
                        newSectionItem.getWatchingStatus() == oldSectionItem.getWatchingStatus() &&
                        newSectionItem.getUrl().equals(oldSectionItem.getUrl());
            }
        }
        return false;
    }
}
