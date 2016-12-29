package com.eightunity.unitypicker.watch.adapter.model;

import com.eightunity.unitypicker.ui.recyclerview.BaseRecyclerViewType;
import com.eightunity.unitypicker.watch.adapter.WatchDetailType;

/**
 * Created by chokechaic on 12/27/2016.
 */

public class WatchSectionItem extends BaseRecyclerViewType {

    private Integer imageSection;
    private String descSection;

    public WatchSectionItem() {
        super(WatchDetailType.TYPE_SECTION);
    }

    public Integer getImageSection() {
        return imageSection;
    }

    public void setImageSection(Integer imageSection) {
        this.imageSection = imageSection;
    }

    public String getDescSection() {
        return descSection;
    }

    public void setDescSection(String descSection) {
        this.descSection = descSection;
    }
}
