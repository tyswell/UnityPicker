package com.eightunity.unitypicker.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.eightunity.unitypicker.model.dao.EMatching;
import com.eightunity.unitypicker.utility.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chokechaic on 9/6/2016.
 */
public class EMatchingDAO {

    public static final String TABLE_E_MATCHING = "E_MATCHING";
    public static final String ID_FIELD = "id";
    public static final String USERID_FIELD = "user_id";
    public static final String SEARCH_WORD_ID_FIELD = "seacrh_word_id";
    public static final String SEARCH_WORD_DESC_FIELD = "search_word_desc";
    public static final String CONTENT_ID_FIELD = "content_id";
    public static final String TITLE_CONTENT_FIELD = "title_content";
    public static final String WEB_NAME_FIELD = "web_name";
    public static final String URL_FIELD = "url";
    public static final String MATCHING_DATE_FIELD = "matching_date";

    public static String createTable() {
        return "CREATE TABLE " + TABLE_E_MATCHING +
                " (" +
                    ID_FIELD              + " INTEGER PRIMARY KEY," +
                    USERID_FIELD          + " TEXT," +
                    SEARCH_WORD_ID_FIELD  + " INTEGER," +
                    SEARCH_WORD_DESC_FIELD+ " TEXT," +
                    CONTENT_ID_FIELD      + " INTEGER," +
                    TITLE_CONTENT_FIELD   + " TEXT," +
                    WEB_NAME_FIELD        + " TEXT," +
                    URL_FIELD             + " TEXT," +
                    MATCHING_DATE_FIELD   + " DATETIME " +
                " )";
    }

    public int add(EMatching data) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(USERID_FIELD, data.getUser_id());
        values.put(SEARCH_WORD_ID_FIELD, data.getSeacrh_word_id());
        values.put(SEARCH_WORD_DESC_FIELD, data.getSearch_word_desc());
        values.put(CONTENT_ID_FIELD, data.getContent_id());
        values.put(TITLE_CONTENT_FIELD, data.getTitle_content());
        values.put(WEB_NAME_FIELD, data.getWeb_name());
        values.put(URL_FIELD, data.getUrl());
        values.put(MATCHING_DATE_FIELD, DateUtil.dateToString(data.getMatching_date()));

        int id;
        id = (int) db.insert(TABLE_E_MATCHING, null, values);
        DatabaseManager.getInstance().closeDatabase();

        return id;
    }

    public void delete(int id) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(TABLE_E_MATCHING,
                ID_FIELD + " = ?",
                new String[] { String.valueOf(id) });
        DatabaseManager.getInstance().closeDatabase();
    }

    public void deleteBySearchId(int searchId) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(TABLE_E_MATCHING,
                SEARCH_WORD_ID_FIELD + " = ?",
                new String[] { String.valueOf(searchId) });
        DatabaseManager.getInstance().closeDatabase();
    }

    public EMatching getByKey(int id, String user_id) {
        String query =
                "SELECT *" +
                        " FROM " + TABLE_E_MATCHING +
                        " WHERE " + ID_FIELD + " = " + id +
                        USERID_FIELD + " = '" + user_id + "'";

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            cursor.moveToFirst();

            EMatching data = getData(cursor);

            cursor.close();
            DatabaseManager.getInstance().closeDatabase();
            return data;
        } else {
            cursor.close();
            DatabaseManager.getInstance().closeDatabase();
            return null;
        }
    }

    public List<EMatching> getAllData(String user_id) {
        String query =
                "SELECT *" +
                        " FROM " + TABLE_E_MATCHING +
                        " WHERE " + USERID_FIELD + "='" + user_id + "'" +
                        " ORDER BY " + MATCHING_DATE_FIELD + " DESC";
        List<EMatching> datas = new ArrayList<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                datas.add(getData(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return datas;
    }

    public List<EMatching> getBySearchWord(String user_id, int searchWordId) {
        String query =
                "SELECT *" +
                        " FROM " + TABLE_E_MATCHING +
                        " WHERE " + USERID_FIELD + "='" + user_id + "'" +
                            " AND " +  SEARCH_WORD_ID_FIELD + "=" + searchWordId +
                        " ORDER BY " + MATCHING_DATE_FIELD + " DESC";
        List<EMatching> datas = new ArrayList<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                datas.add(getData(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return datas;
    }

    public int getCountBySearchWord(String user_id, int searchWordId) {
        String query =
                "SELECT count(*)" +
                        " FROM " + TABLE_E_MATCHING +
                        " WHERE " + USERID_FIELD + "='" + user_id + "'" +
                        " AND " +  SEARCH_WORD_ID_FIELD + "=" + searchWordId;
        List<EMatching> datas = new ArrayList<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        int count = cursor.getInt(0);


        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return count;
    }

    private EMatching getData(Cursor cursor) {
        EMatching data= new EMatching();
        data.setId(cursor.getInt(cursor.getColumnIndex(ID_FIELD)));
        data.setUser_id(cursor.getString(cursor.getColumnIndex(USERID_FIELD)));
        data.setSeacrh_word_id(cursor.getInt(cursor.getColumnIndex(SEARCH_WORD_ID_FIELD)));
        data.setSearch_word_desc(cursor.getString(cursor.getColumnIndex(SEARCH_WORD_DESC_FIELD)));
        data.setContent_id(cursor.getInt(cursor.getColumnIndex(CONTENT_ID_FIELD)));
        data.setTitle_content(cursor.getString(cursor.getColumnIndex(TITLE_CONTENT_FIELD)));
        data.setUrl(cursor.getString(cursor.getColumnIndex(URL_FIELD)));
        data.setWeb_name(cursor.getString(cursor.getColumnIndex(WEB_NAME_FIELD)));
        data.setMatching_date(DateUtil.stringToDate(cursor.getString(cursor.getColumnIndex(MATCHING_DATE_FIELD))));

        return data;
    }

}
