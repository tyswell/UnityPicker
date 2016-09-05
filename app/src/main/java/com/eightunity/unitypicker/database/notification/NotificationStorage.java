package com.eightunity.unitypicker.database.notification;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by deksen on 9/5/16 AD.
 */
public class NotificationStorage extends SQLiteOpenHelper {

    private static final String DB_NAME = "UNITY_PICKER_DB";
    private static final int DB_VERSION = 1;


    public NotificationStorage(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
