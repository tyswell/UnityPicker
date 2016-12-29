package com.eightunity.unitypicker.watch.adapter.utility;

import android.support.v7.util.DiffUtil;

import com.eightunity.unitypicker.model.watch.Watch;
import com.eightunity.unitypicker.ui.recyclerview.BaseRecyclerViewType;
import com.eightunity.unitypicker.watch.adapter.model.WatchItem;
import com.eightunity.unitypicker.watch.adapter.model.WatchSectionItem;

import java.util.List;

/**
 * Created by chokechaic on 12/27/2016.
 */

public class WatchDiffCallback extends DiffUtil.Callback {

    private List<BaseRecyclerViewType> oldItemList;
    private List<BaseRecyclerViewType> newItemList;

    public WatchDiffCallback(List<BaseRecyclerViewType> oldItemList,  List<BaseRecyclerViewType> newItemList) {
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
            if (newItem instanceof WatchSectionItem) {
                WatchSectionItem newSectionItem = (WatchSectionItem)newItem;
                WatchSectionItem oldSectionItem = (WatchSectionItem)oldItem;

                return newSectionItem.getDescSection().equals(oldSectionItem.getDescSection());
            } else if (newItem instanceof WatchItem) {
                WatchItem newDetailItem = (WatchItem)newItem;
                WatchItem oldDetailItem = (WatchItem)oldItem;

                return newDetailItem.getSearchId() == oldDetailItem.getSearchId();
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        BaseRecyclerViewType newItem = newItemList.get(newItemPosition);
        BaseRecyclerViewType oldItem = oldItemList.get(oldItemPosition);
        if (newItem.getType() == oldItem.getType()) {
            if (newItem instanceof WatchSectionItem) {
                WatchSectionItem newSectionItem = (WatchSectionItem)newItem;
                WatchSectionItem oldSectionItem = (WatchSectionItem)oldItem;

                return newSectionItem.getDescSection().equals(oldSectionItem.getDescSection()) &&
                        newSectionItem.getImageSection() == oldSectionItem.getImageSection();
            } else if (newItem instanceof WatchItem) {
                WatchItem newDetailItem = (WatchItem)newItem;
                WatchItem oldDetailItem = (WatchItem)oldItem;

                return newDetailItem.getSearchId() == oldDetailItem.getSearchId() &&
                        newDetailItem.getWatchStatusImage() == oldDetailItem.getWatchStatusImage() &&
                        newDetailItem.getWatchStatusDesc().equals(oldDetailItem.getWatchStatusDesc()) &&
                        newDetailItem.getSearchType().equals(oldDetailItem.getSearchType()) &&
                        newDetailItem.getSearchWord().equals(oldDetailItem.getSearchWord()) &&
                        newDetailItem.getCountFound().equals(oldDetailItem.getCountFound()) &&
                        newDetailItem.getWatchingStatus() == oldDetailItem.getWatchingStatus() &&
                        newDetailItem.getTimeDesc().equals(oldDetailItem.getTimeDesc());
            } else {
                return false;
            }
        }

        return false;
    }
}
