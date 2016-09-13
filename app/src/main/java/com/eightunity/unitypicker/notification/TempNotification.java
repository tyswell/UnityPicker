package com.eightunity.unitypicker.notification;

import com.eightunity.unitypicker.model.Notificaiton.Notification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deksen on 9/5/16 AD.
 */
public class TempNotification {


    public static List<Notification> getModel() {
        List<Notification> list = new ArrayList<>();

        Notification not = new Notification();
        not.setSearchWord("โช๊ค");
        not.setTitleContent("ขายโข๊ค Fox สภาพดีไม่รั่วไม่ซึม ล้อ 26");
        not.setWebName("THAIMTB");
        not.setTimeDesc("5 hours ago");
        list.add(not);

        Notification not2 = new Notification();
        not2.setSearchWord("โช๊ค");
        not2.setTitleContent("โช๊ค ล้อ 26 ยุบ 160 talas แกน 15 ทท");
        not2.setWebName("THAIMTB");
        not2.setTimeDesc("2 hours ago");
        list.add(not2);

        return list;
    }

}
