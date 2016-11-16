package com.eightunity.unitypicker.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.eightunity.unitypicker.model.dao.ESearchWord;
import com.eightunity.unitypicker.utility.DateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chokechaic on 9/6/2016.
 */
public class ESearchWordDAO {

    private static final String TAG = "ESearchWordDAO";

    public static final String TABLE_E_SEARCH_WORD = "E_SEARCH_WORD";
    public static final String SEARCH_ID_FIELD = "search_id";
    public static final String USER_ID_FIELD = "username";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String SEARCH_TYPE_FIELD = "SEARCH_TYPE";
    public static final String MODIFIED_DATE_FIELD = "modified_date";

    public static String createTable() {
        return "CREATE TABLE " + TABLE_E_SEARCH_WORD +
                " (" +
                    SEARCH_ID_FIELD     +   " INTEGER PRIMARY KEY," +
                    USER_ID_FIELD       +   " TEXT,"+
                    DESCRIPTION_FIELD   +   " TEXT,"+
                    SEARCH_TYPE_FIELD   +   " INTEGER," +
                    MODIFIED_DATE_FIELD +   " DATETIME DEFAULT CURRENT_TIMESTAMP" +
                " )";
    }

    public void add(ESearchWord data) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(SEARCH_ID_FIELD, data.getSearch_id());
        values.put(USER_ID_FIELD, data.getUser_id());
        values.put(DESCRIPTION_FIELD, data.getDescription());
        values.put(SEARCH_TYPE_FIELD, data.getSearch_type());

        db.insert(TABLE_E_SEARCH_WORD, null, values);
        DatabaseManager.getInstance().closeDatabase();
    }

    public void delete(int search_id) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(TABLE_E_SEARCH_WORD,
                SEARCH_ID_FIELD + " = ?",
                new String[] { String.valueOf(search_id) });
        DatabaseManager.getInstance().closeDatabase();
    }

    public ESearchWord getByKey(int search_id, String user_id) {
        String query =
                "SELECT *" +
                        " FROM " + TABLE_E_SEARCH_WORD +
                        " WHERE " + SEARCH_ID_FIELD + " = " + search_id +
                        USER_ID_FIELD + " = '" + user_id +"'";

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            cursor.moveToFirst();

            ESearchWord data = getData(cursor);

            cursor.close();
            DatabaseManager.getInstance().closeDatabase();
            return data;
        } else {
            cursor.close();
            DatabaseManager.getInstance().closeDatabase();
            return null;
        }
    }

    public List<ESearchWord> getAllData(String user_id) {
        String query =
                "SELECT *" +
                        " FROM " + TABLE_E_SEARCH_WORD+
                        " WHERE " + USER_ID_FIELD + "='" + user_id + "'" +
                        " ORDER BY " + MODIFIED_DATE_FIELD + " DESC";
        List<ESearchWord> datas = new ArrayList<>();

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

    public List<Integer> getAllId(String user_id) {
        String query =
                "SELECT " + SEARCH_ID_FIELD +
                        " FROM " + TABLE_E_SEARCH_WORD+
                        " WHERE " + USER_ID_FIELD + "='" + user_id + "'" +
                        " ORDER BY " + MODIFIED_DATE_FIELD + " DESC";
        List<Integer> datas = new ArrayList<>();

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                datas.add(cursor.getInt(cursor.getColumnIndex(SEARCH_ID_FIELD)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        DatabaseManager.getInstance().closeDatabase();

        return datas;
    }

    private ESearchWord getData(Cursor cursor) {
        ESearchWord data= new ESearchWord();
        data.setSearch_id(cursor.getInt(cursor.getColumnIndex(SEARCH_ID_FIELD)));
        data.setUser_id(cursor.getString(cursor.getColumnIndex(USER_ID_FIELD)));
        data.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION_FIELD)));
        data.setSearch_type(cursor.getInt(cursor.getColumnIndex(SEARCH_TYPE_FIELD)));
        data.setModified_date(DateUtil.stringToDate(cursor.getString(cursor.getColumnIndex(MODIFIED_DATE_FIELD))));

        return data;
    }

}
