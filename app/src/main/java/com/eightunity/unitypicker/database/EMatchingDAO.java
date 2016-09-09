package com.eightunity.unitypicker.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.eightunity.unitypicker.model.dao.EMatching;
import com.eightunity.unitypicker.utility.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chokechaic on 9/6/2016.
 */
public class EMatchingDAO {

    public static final String TABLE_E_MATCHING = "E_MATCHING";
    public static final String ID_FIELD = "id";
    public static final String USERNAME_FIELD = "username";
    public static final String SEARCH_WORD_ID_FIELD = "seacrh_word_id";
    public static final String SEARCH_WORD_DESC_FIELD = "search_word_desc";
    public static final String TITLE_CONTENT_FIELD = "title_content";
    public static final String WEB_NAME_FIELD = "web_name";
    public static final String URL_FIELD = "url";
    public static final String MATCHING_DATE_FIELD = "matching_date";

    public static String createTable() {
        return "CREATE TABLE " + TABLE_E_MATCHING +
                " (" +
                    ID_FIELD              + " INTEGER PRIMARY KEY," +
                    USERNAME_FIELD        + " TEXT," +
                    SEARCH_WORD_ID_FIELD  + " INTEGER," +
                    SEARCH_WORD_DESC_FIELD+ " TEXT," +
                    TITLE_CONTENT_FIELD   + " TEXT," +
                    WEB_NAME_FIELD        + " TEXT," +
                    URL_FIELD             + " TEXT," +
                    MATCHING_DATE_FIELD   + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
                " )";
    }

    public int add(EMatching data) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(USERNAME_FIELD, data.getUsername());
        values.put(SEARCH_WORD_ID_FIELD, data.getSeacrh_word_id());
        values.put(SEARCH_WORD_DESC_FIELD, data.getSearch_word_desc());
        values.put(TITLE_CONTENT_FIELD, data.getTitle_content());
        values.put(WEB_NAME_FIELD, data.getWeb_name());
        values.put(URL_FIELD, data.getUrl());

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

    public EMatching getByKey(int id, String username) {
        String query =
                "SELECT *" +
                        " FROM " + TABLE_E_MATCHING +
                        " WHERE " + ID_FIELD + " = " + id +
                                    USERNAME_FIELD + " = '" + username + "'";

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

    public List<EMatching> getAllData(String username) {
        String query =
                "SELECT *" +
                        " FROM " + TABLE_E_MATCHING +
                        " WHERE " + USERNAME_FIELD + "='" + username + "'";
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

    private EMatching getData(Cursor cursor) {
        EMatching data= new EMatching();
        data.setId(cursor.getInt(cursor.getColumnIndex(ID_FIELD)));
        data.setUsername(cursor.getString(cursor.getColumnIndex(USERNAME_FIELD)));
        data.setSeacrh_word_id(cursor.getInt(cursor.getColumnIndex(SEARCH_WORD_ID_FIELD)));
        data.setSearch_word_desc(cursor.getString(cursor.getColumnIndex(SEARCH_WORD_DESC_FIELD)));
        data.setTitle_content(cursor.getString(cursor.getColumnIndex(TITLE_CONTENT_FIELD)));
        data.setUrl(cursor.getString(cursor.getColumnIndex(URL_FIELD)));
        data.setWeb_name(cursor.getString(cursor.getColumnIndex(WEB_NAME_FIELD)));
        data.setMatching_date(DateUtil.stringToDate(cursor.getString(cursor.getColumnIndex(MATCHING_DATE_FIELD))));

        return data;
    }

}
