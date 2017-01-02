package com.eightunity.unitypicker.watch.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eightunity.unitypicker.R;

import org.w3c.dom.Text;

/**
 * Created by chokechaic on 12/23/2016.
 */

public class WatchSectionHolder extends RecyclerView.ViewHolder {

    public ImageView sectionImage;
    public TextView sectionDesc;

    public WatchSectionHolder(View itemView) {
        super(itemView);
        sectionImage = (ImageView) itemView.findViewById(R.id.image_section);
        sectionDesc = (TextView) itemView.findViewById(R.id.desc_section);
    }
}
