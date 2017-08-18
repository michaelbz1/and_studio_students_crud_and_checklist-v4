package com.journaldev.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseClassPeriodsHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "PERIODS";

    // Table columns
    public static final String _ID = "_id";
    public static final String PERIODSNAME = "PERIODSNAME";

    // Database Information
    static final String DB_NAME = "PeriodsDB.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PERIODSNAME + " TEXT NOT NULL);";

    public DatabaseClassPeriodsHelper(Context context) {
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
/*
    public ArrayList<HashMap<String, String>> getAllPeriods() {

        ArrayList<HashMap<String, String>> periodsList;
        periodsList = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM PERIODS";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                //Id,Name,Date
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("a", cursor.getString(0));
                map.put("b", cursor.getString(1));
                periodsList.add(map);
                Log.e("dataofList", cursor.getString(0) + "," + cursor.getString(1));
            } while (cursor.moveToNext());
        }
        return periodsList;

    }
*/

    public List<String> getAllPeriods(){
        List<String> periods = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " order by PERIODSNAME" ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                periods.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return periods;
    }
}
