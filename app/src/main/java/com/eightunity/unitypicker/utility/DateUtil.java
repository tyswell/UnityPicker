package com.eightunity.unitypicker.utility;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by chokechaic on 9/6/2016.
 */
public class DateUtil {

    public static Date stringToDate(String value) {
        try {
            Calendar t = new GregorianCalendar();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dt = sdf.parse(value);
            return dt;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Timestamp stringToTimeStamp(String value) {
        try {
            Calendar t = new GregorianCalendar();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dt = sdf.parse(value);
            Timestamp tm = new Timestamp(dt.getTime());
            return tm;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String dateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    public static String timeSpent(String value) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date past = null;
        try {
            past = format.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeSpent(past);
    }

    public static String timeSpent(Date past)  {
        Date now = new Date();
        long seconds= TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
        long minutes=TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
        long hours=TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
        long days=TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());


        if(seconds<60)
        {
            return seconds+" seconds ago";
        }
        else if(minutes<60)
        {
            return minutes+" minutes ago";
        }
        else if(hours<24)
        {
            return hours+" hours ago";
        }
        else
        {
            return days+" days ago";
        }
    }

}
