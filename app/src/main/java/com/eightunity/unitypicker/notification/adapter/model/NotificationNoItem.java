package com.eightunity.unitypicker.notification.adapter.model;

import com.eightunity.unitypicker.notification.adapter.NotificationDetailType;
import com.eightunity.unitypicker.ui.recyclerview.BaseRecyclerViewType;

/**
 * Created by chokechaic on 12/28/2016.
 */

public class NotificationNoItem extends BaseRecyclerViewType {

    public NotificationNoItem() {
        super(NotificationDetailType.TYPE_NO_ROW);
    }
}
