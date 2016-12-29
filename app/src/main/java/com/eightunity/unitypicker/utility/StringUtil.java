package com.eightunity.unitypicker.utility;

/**
 * Created by chokechaic on 12/27/2016.
 */

public class StringUtil {

    public static final String ACTIVE_DESC = "Y";
    public static final String INACTIVE_DESC = "N";

    public static boolean activeConvert(String desc) {
        if (ACTIVE_DESC.equals(desc)) {
            return true;
        }
        return false;
    }

    public static String activeCode(boolean code) {
        if (code) {
            return ACTIVE_DESC;
        }
        return INACTIVE_DESC;
    }

}
