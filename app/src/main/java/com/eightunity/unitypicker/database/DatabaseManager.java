package com.eightunity.unitypicker.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by chokechaic on 9/6/2016.
 */
public class DatabaseManager {

    private Integer mOpenCounter = 0;
    private static SQLiteOpenHelper mDatabaseHelper;
    private static DatabaseManager dbm;
    private SQLiteDatabase database;

    public static synchronized void initializeInstance(SQLiteOpenHelper helper) {
        if (dbm == null) {
            dbm = new DatabaseManager();
            mDatabaseHelper = helper;
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (dbm == null) {
            throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }

        return dbm;
    }

    public synchronized SQLiteDatabase openDatabase() {
        mOpenCounter+=1;
        if(mOpenCounter == 1) {
            // Opening new database
            database = mDatabaseHelper.getWritableDatabase();
        }
        return database;
    }

    public synchronized void closeDatabase() {
        mOpenCounter-=1;
        if(mOpenCounter == 0) {
            // Closing database
            database.close();
        }
    }

}
