package com.eightunity.unitypicker.match.adapter.utility;

import android.content.res.Resources;
import android.util.Log;

import com.eightunity.unitypicker.match.adapter.model.MatchCountItem;
import com.eightunity.unitypicker.match.adapter.model.MatchHeaderItem;
import com.eightunity.unitypicker.match.adapter.model.MatchItem;
import com.eightunity.unitypicker.match.adapter.model.MatchNoItem;
import com.eightunity.unitypicker.model.Notificaiton.Notification;
import com.eightunity.unitypicker.model.match.Match;
import com.eightunity.unitypicker.model.match.MatchDetail;
import com.eightunity.unitypicker.search.SearchUtility;
import com.eightunity.unitypicker.ui.recyclerview.BaseRecyclerViewType;
import com.eightunity.unitypicker.watch.adapter.utility.WatchAdapterConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chokechaic on 12/29/2016.
 */

public class MatchAdapterConverter {

    public static MatchHeaderItem getMatchHeaderItem(Match match, Resources resources) {
        MatchHeaderItem item = new MatchHeaderItem();
        item.setTimeDesc(match.getTimeDesc());

        item.setImageSearchType(SearchUtility.searchTypeLogo(match.getSearchType()));
        item.setImageWatchStatus(WatchAdapterConverter.formatWatchStatusImage(match.getWatchingStatus()));
        item.setSearchId(match.getSearchId());
        item.setSearchType(match.getSearchType());
        item.setSearchTypeCode(SearchUtility.searhTypeDescToCode(match.getSearchType()));
        item.setSearchWord(match.getSearchWord());
        item.setWatchStatus(match.getWatchingStatus());
        item.setWatchStatusDesc(WatchAdapterConverter.formatWatchStatusDesc(match.getWatchingStatus(), resources));
        return item;
    }

    public static MatchCountItem getMatchCountItem(Match match, Resources resources) {
        MatchCountItem item = new MatchCountItem();
        item.setCountFound(WatchAdapterConverter.formatCountFound(match.getCountFound(), resources));
        return item;
    }

    public static List<MatchItem> getMatchItem(List<MatchDetail> details) {
        List<MatchItem> results = new ArrayList<>();
        for (MatchDetail detail : details) {
            results.add(getMatchItem(detail));
        }
        return results;
    }

    public static MatchItem getMatchItem(MatchDetail detail) {
        MatchItem item = new MatchItem();
        item.setUrl(detail.getUrl());
        item.setWebName(detail.getWebName());
        item.setWatchingStatus(detail.getWatchingStatus());
        item.setMatchID(detail.getMatchID());
        item.setSearchId(detail.getSearchId());
        item.setTimeDesc(detail.getTimeDesc());
        item.setTitleContent(detail.getTitleContent());
        return item;
    }

    public static MatchNoItem getMatchNoItem() {
        MatchNoItem item = new MatchNoItem();
        return item;
    }
}
