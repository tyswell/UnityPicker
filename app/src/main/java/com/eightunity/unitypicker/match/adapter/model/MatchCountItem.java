package com.eightunity.unitypicker.match.adapter.model;

import com.eightunity.unitypicker.match.adapter.MatchDetailType;
import com.eightunity.unitypicker.ui.recyclerview.BaseRecyclerViewType;

/**
 * Created by chokechaic on 1/4/2017.
 */

public class MatchCountItem extends BaseRecyclerViewType {

    private String countFound;

    public MatchCountItem() {
        super(MatchDetailType.TYPE_COUNT);
    }

    public String getCountFound() {
        return countFound;
    }

    public void setCountFound(String countFound) {
        this.countFound = countFound;
    }
}
