package com.eightunity.unitypicker.watch.adapter.utility;

import android.content.res.Resources;

import com.bumptech.glide.load.engine.Resource;
import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.model.watch.Watch;
import com.eightunity.unitypicker.search.SearchUtility;
import com.eightunity.unitypicker.ui.recyclerview.BaseRecyclerViewType;
import com.eightunity.unitypicker.watch.adapter.model.WatchItem;
import com.eightunity.unitypicker.watch.adapter.model.WatchNoItem;
import com.eightunity.unitypicker.watch.adapter.model.WatchSectionItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chokechaic on 12/27/2016.
 */

public class WatchAdapterConverter {

    public static List<BaseRecyclerViewType> getWatchDetail(List<Watch> watches, Resources resources) {
        List<BaseRecyclerViewType> results = new ArrayList<>();
        if (watches != null && watches.size() > 0) {
            String searchType = "";
            for (Watch watch : watches) {
                if (!searchType.equals(watch.getSearchType())) {
                    searchType = watch.getSearchType();
                    WatchSectionItem sectionItem = new WatchSectionItem();
                    sectionItem.setDescSection(watch.getSearchType());
                    sectionItem.setImageSection(SearchUtility.searchTypeLogo(watch.getSearchType()));
                    results.add(sectionItem);
                }

                results.add(convert(watch, resources));
            }
        } else {
            WatchNoItem item = new WatchNoItem();
            results.add(item);
        }

        return results;
    }

    public static WatchItem convert(Watch watch, Resources resources) {
        if (watch == null) {
            return null;
        }

        WatchItem item = new WatchItem();
        item.setWatchingStatus(watch.getwatchingStatus());
        item.setCountFound(formatCountFound(watch.getCountFound(), resources));
        item.setSearchId(watch.getSearchId());
        item.setSearchType(watch.getSearchType());
        item.setSearchWord(watch.getSearchWord());
        item.setTimeDesc(watch.getTimeDesc());
        item.setWatchStatusDesc(formatWatchStatusDesc(watch.getwatchingStatus(), resources));
        item.setWatchStatusImage(formatWatchStatusImage(watch.getwatchingStatus()));

        return item;
    }

    public static String formatCountFound(int countFound, Resources resources) {
        return countFound + " " + resources.getString(R.string.found_desc);
    }

    public static String formatWatchStatusDesc(boolean watchStatus, Resources resources) {
        if (watchStatus) {
            return resources.getString(R.string.follow_desc);
        } else {
            return resources.getString(R.string.unfollow_desc);
        }
    }

    public static int formatWatchStatusImage(boolean watchStatus) {
        if (watchStatus) {
            return R.drawable.ic_notifications;
        } else {
            return R.drawable.ic_close;
        }
    }

}
