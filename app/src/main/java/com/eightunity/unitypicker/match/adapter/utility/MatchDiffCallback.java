package com.eightunity.unitypicker.match.adapter.utility;

import android.support.v7.util.DiffUtil;

import com.eightunity.unitypicker.match.adapter.model.MatchCountItem;
import com.eightunity.unitypicker.match.adapter.model.MatchHeaderItem;
import com.eightunity.unitypicker.match.adapter.model.MatchItem;
import com.eightunity.unitypicker.ui.recyclerview.BaseRecyclerViewType;
import com.eightunity.unitypicker.watch.adapter.model.WatchSectionItem;

import java.util.List;

/**
 * Created by chokechaic on 12/29/2016.
 */

public class MatchDiffCallback extends DiffUtil.Callback {

    private List<BaseRecyclerViewType> oldItemList;
    private List<BaseRecyclerViewType> newItemList;

    public MatchDiffCallback(List<BaseRecyclerViewType> oldItemList,  List<BaseRecyclerViewType> newItemList) {
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
            if (newItem instanceof MatchHeaderItem) {
                MatchHeaderItem newSectionItem = (MatchHeaderItem)newItem;
                MatchHeaderItem oldSectionItem = (MatchHeaderItem)oldItem;

                return newSectionItem.getSearchId() == oldSectionItem.getSearchId();
            } else if (newItem instanceof MatchItem) {
                MatchItem newSectionItem = (MatchItem)newItem;
                MatchItem oldSectionItem = (MatchItem)oldItem;

                return newSectionItem.getMatchID() == oldSectionItem.getMatchID();
            } else if (newItem instanceof MatchCountItem) {
                MatchCountItem newSectionItem = (MatchCountItem)newItem;
                MatchCountItem oldSectionItem = (MatchCountItem)oldItem;

                return newSectionItem.getCountFound() == oldSectionItem.getCountFound();
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
            if (newItem instanceof MatchHeaderItem) {
                MatchHeaderItem newSectionItem = (MatchHeaderItem)newItem;
                MatchHeaderItem oldSectionItem = (MatchHeaderItem)oldItem;

                return newSectionItem.getSearchId()== oldSectionItem.getSearchId() &&
                        newSectionItem.getSearchWord().equals(oldSectionItem.getSearchWord()) &&
                        newSectionItem.getSearchTypeCode()== oldSectionItem.getSearchTypeCode() &&
                        newSectionItem.getImageSearchType()== oldSectionItem.getImageSearchType() &&
                        newSectionItem.getSearchType().equals(oldSectionItem.getSearchType()) &&
                        newSectionItem.getWatchStatus()== oldSectionItem.getWatchStatus() &&
                        newSectionItem.getImageWatchStatus()== oldSectionItem.getImageWatchStatus() &&
                        newSectionItem.getWatchStatusDesc().equals(oldSectionItem.getWatchStatusDesc()) &&
                        newSectionItem.getTimeDesc().equals( oldSectionItem.getTimeDesc());
            } else if (newItem instanceof MatchItem) {
                MatchItem newSectionItem = (MatchItem)newItem;
                MatchItem oldSectionItem = (MatchItem)oldItem;

                return newSectionItem.getMatchID()== oldSectionItem.getMatchID() &&
                        newSectionItem.getSearchId()== oldSectionItem.getSearchId() &&
                        newSectionItem.getTitleContent().equals(oldSectionItem.getTitleContent()) &&
                        newSectionItem.getWebName().equals(oldSectionItem.getWebName()) &&
                        newSectionItem.getTimeDesc().equals(oldSectionItem.getTimeDesc()) &&
                        newSectionItem.getWatchingStatus()== oldSectionItem.getWatchingStatus() &&
                        newSectionItem.getUrl().equals(oldSectionItem.getUrl());
            } else if (newItem instanceof MatchCountItem) {
                MatchCountItem newSectionItem = (MatchCountItem)newItem;
                MatchCountItem oldSectionItem = (MatchCountItem)oldItem;

                return newSectionItem.getCountFound() == oldSectionItem.getCountFound();
            } else {
                return false;
            }
        }

        return false;
    }
}
