package com.eightunity.unitypicker.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.eightunity.unitypicker.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chokechaic on 8/26/2016.
 */
public class SearchTypeAdapter extends BaseAdapter {

    private Context mContext;
    private List<String> mDatas;
    private LayoutInflater mInflater;

    public SearchTypeAdapter(Context context, List<String> datas) {
        mContext = context;
        mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.adapter_seach_type, parent, false);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.logo = (ImageView) convertView.findViewById(R.id.logo);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int imgResourceId;
        switch (position) {
            case 0:
                imgResourceId = R.drawable.ic_action_bike;
                break;
            default:
                imgResourceId = android.R.drawable.ic_menu_zoom;
                break;
        }

        holder.name.setText(mDatas.get(position));
        holder.logo.setImageResource(imgResourceId);

        convertView.setTag(holder);

        return convertView;
    }

    public class ViewHolder {
        TextView name;
        ImageView logo;
    }
}
