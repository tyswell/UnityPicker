package com.eightunity.unitypicker.search;

import com.eightunity.unitypicker.R;
import com.eightunity.unitypicker.UnityPicker;

/**
 * Created by chokechaic on 9/6/2016.
 */
public class SearchUtility {

    public static final int descToCode(String desc) {
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

    public static final String codeToDesc(int code) {
        String[] searchTypeDatas = UnityPicker.getContext().getResources().getStringArray(R.array.search_type);

        return searchTypeDatas[code - 1];
    }

}
