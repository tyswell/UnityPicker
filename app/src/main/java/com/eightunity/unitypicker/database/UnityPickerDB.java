package com.eightunity.unitypicker.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chokechaic on 9/6/2016.
 */
public class UnityPickerDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 17;

    private static final String DATABASE_NAME = "UNITY_PICKER_DB";

    public UnityPickerDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ESearchWordDAO.createTable());
        db.execSQL(EMatchingDAO.createTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + ESearchWordDAO.TABLE_E_SEARCH_WORD);
        db.execSQL("DROP TABLE IF EXISTS " + EMatchingDAO.TABLE_E_MATCHING);

        // Create tables again
        onCreate(db);
    }
}
