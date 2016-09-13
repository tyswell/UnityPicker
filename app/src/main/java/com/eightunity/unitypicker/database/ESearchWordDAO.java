package com.eightunity.unitypicker.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.text.format.DateUtils;
import android.util.Log;

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
    public static final String ID_FIELD = "id";
    public static final String USERNAME_FIELD = "username";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String SEARCH_TYPE_FIELD = "SEARCH_TYPE";
    public static final String MODIFIED_DATE_FIELD = "modified_date";



    public static String createTable() {
        return "CREATE TABLE " + TABLE_E_SEARCH_WORD +
                " (" +
                    ID_FIELD            +   " INTEGER PRIMARY KEY," +
                    USERNAME_FIELD      +   " TEXT,"+
                    DESCRIPTION_FIELD   +   " TEXT,"+
                    SEARCH_TYPE_FIELD   +   " INTEGER," +
                    MODIFIED_DATE_FIELD +   " DATETIME DEFAULT CURRENT_TIMESTAMP" +
                " )";
    }

    public int add(ESearchWord data) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        ContentValues values = new ContentValues();
        values.put(USERNAME_FIELD, data.getUsername());
        values.put(DESCRIPTION_FIELD, data.getDescription());
        values.put(SEARCH_TYPE_FIELD, data.getSearch_type());

        int id;
        id = (int) db.insert(TABLE_E_SEARCH_WORD, null, values);
        DatabaseManager.getInstance().closeDatabase();

        return id;
    }

    public void delete(int id) {
        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        db.delete(TABLE_E_SEARCH_WORD,
                ID_FIELD + " = ?",
                new String[] { String.valueOf(id) });
        DatabaseManager.getInstance().closeDatabase();
    }

    public ESearchWord getByKey(int id, String username) {
        String query =
                "SELECT *" +
                        " FROM " + TABLE_E_SEARCH_WORD +
                        " WHERE " + ID_FIELD + " = " + id +
                                    USERNAME_FIELD + " = '" + username +"'";

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

    public List<ESearchWord> getAllData(String username) {
        String query =
                "SELECT *" +
                        " FROM " + TABLE_E_SEARCH_WORD+
                        " WHERE " + USERNAME_FIELD + "='" + username + "'" +
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

    private ESearchWord getData(Cursor cursor) {
        ESearchWord data= new ESearchWord();
        data.setId(cursor.getInt(cursor.getColumnIndex(ID_FIELD)));
        data.setUsername(cursor.getString(cursor.getColumnIndex(USERNAME_FIELD)));
        data.setDescription(cursor.getString(cursor.getColumnIndex(DESCRIPTION_FIELD)));
        data.setSearch_type(cursor.getInt(cursor.getColumnIndex(SEARCH_TYPE_FIELD)));
        data.setModified_date(DateUtil.stringToDate(cursor.getString(cursor.getColumnIndex(MODIFIED_DATE_FIELD))));

        return data;
    }

}
