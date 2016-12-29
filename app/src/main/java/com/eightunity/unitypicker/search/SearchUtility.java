package com.eightunity.unitypicker.search;

import android.util.Log;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.UnityPicker;

/**
 * Created by chokechaic on 9/6/2016.
 */
public class SearchUtility {

    public static int searhTypeDescToCode(String desc) {
        String[] searchTypeDatas = UnityPicker.getContext().getResources().getStringArray(R.array.search_type);
        int code = 0;
        for (int i = 0; i < searchTypeDatas.length; i++) {
            if (searchTypeDatas[i].equals(desc)) {
                return code;
            }
            code++;
        }

        return -1;
    }

    public static String searchTypeCodeToDesc(int code) {
        String[] searchTypeDatas = UnityPicker.getContext().getResources().getStringArray(R.array.search_type);

        return searchTypeDatas[code];
    }

    public static int searchTypeLogo(String desc) {
        int code = searhTypeDescToCode(desc);
        return searchTypeLogo(code);
    }

    public static int searchTypeLogo(int code) {
        switch (code) {
            case 0:
                return R.drawable.ic_action_bike;
            case 1:
                return R.drawable.ic_tag_faces;
            default:
                return android.R.drawable.ic_menu_zoom;
        }
    }

}
