package com.eightunity.unitypicker.search;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.UnityPicker;

/**
 * Created by chokechaic on 9/6/2016.
 */
public class SearchUtility {

    public static int searhTypeDescToCode(String desc) {
        String[] searchTypeDatas = UnityPicker.getContext().getResources().getStringArray(R.array.search_type);
        int code = 1;
        for (int i = 0; i < searchTypeDatas.length; i++) {
            if (searchTypeDatas[i].equals(desc)) {
                return code;
            }
            code++;
        }

        return 0;
    }

    public static String searchTypeCodeToDesc(int code) {
        String[] searchTypeDatas = UnityPicker.getContext().getResources().getStringArray(R.array.search_type);

        return searchTypeDatas[code - 1];
    }

    public static int searchTypeLogo(String desc) {
        String[] searchTypeDatas = UnityPicker.getContext().getResources().getStringArray(R.array.search_type);
        for (int i = 0; i < searchTypeDatas.length; i++) {
            if (searchTypeDatas[i].equals(desc)) {
                switch (i) {
                    case 0:
                        return R.drawable.ic_action_bike;
                    default:
                        return android.R.drawable.ic_menu_zoom;
                }
            }
        }

        return android.R.drawable.ic_menu_zoom;
    }

}
