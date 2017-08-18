package com.journaldev.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class ImportDBController extends SQLiteOpenHelper {
    private static final String LOGCAT = null;
    // Table Name
    public static final String TABLE_NAME = "STUDENTS";

    // Table columns
    public static final String _ID = "_id";
    public static final String STUDENTID = "StudentID";
    public static final String STUDENTNAME = "StudentName";
    public static final String STUDENTPER = "StudentPeriod";

    // Database Information
    static final String DB_NAME = "STUDENTDB.DB";

    // database version
    static final int DB_VERSION = 6;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + STUDENTID + " TEXT NOT NULL, " + STUDENTNAME + " TEXT NOT NULL, " + STUDENTPER + " TEXT NOT NULL);";

    public ImportDBController(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }




    public ArrayList<HashMap<String, String>> getAllProducts() {

        ArrayList<HashMap<String, String>> journalList;
        journalList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM students";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                //Id, Company,Name,Price
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("a", cursor.getString(0));
                map.put("b", cursor.getString(1));
                map.put("c", cursor.getString(2));
                journalList.add(map);
                Log.e("dataofList",cursor.getString(0)+","+cursor.getString(1)+","+cursor.getString(2));
                } while (cursor.moveToNext());
            }
        return journalList;

    }

}
