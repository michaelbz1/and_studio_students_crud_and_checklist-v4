package com.journaldev.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class databaseIncidentHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "INCIDENTS";

    // Table columns
    public static final String _ID = "_id";
    public static final String BEHAVIORID = "BEHAVIORID";
    public static final String BEHAVIORNAME = "BEHAVIORNAME";
    public static final String BEHAVIORDATE = "BEHAVIORDATE";
    public static final String BEHAVIORCONSEQUENCE = "BEHAVIORCONSEQUENCE";
    public static final String BEHAVIORPARENTCONTACT = "BEHAVIORPARENTCONTACT";
    public static final String BEHAVIORCOMMENTS = "BEHAVIORCOMMENTS";

    // Database Information
    static final String DB_NAME = "IncidentsDB.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + BEHAVIORID + " TEXT NOT NULL, " + BEHAVIORNAME + " TEXT NOT NULL, " + BEHAVIORDATE
            + " TEXT NOT NULL, " + BEHAVIORCONSEQUENCE + " TEXT NOT NULL, " + BEHAVIORPARENTCONTACT + " TEXT NOT NULL, " + BEHAVIORCOMMENTS + " TEXT NOT NULL);";

    public databaseIncidentHelper(Context context) {
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
        String selectQuery = "SELECT  * FROM INCIDENTS";
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                //Id,Name,Date
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("a", cursor.getString(0));
                map.put("b", cursor.getString(1));
                map.put("c", cursor.getString(2));
                map.put("d", cursor.getString(3)); //new
                map.put("e", cursor.getString(4)); //new
                map.put("f", cursor.getString(5)); //new
                journalList.add(map);
                Log.e("dataofList", cursor.getString(0) + "," + cursor.getString(1) + "," + cursor.getString(2));
            } while (cursor.moveToNext());
        }
        return journalList;

    }
    public Cursor fetchCountriesByName(String inputText) throws SQLException {
        //Log.w(TAG, inputText);
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = database.query(databaseIncidentHelper.TABLE_NAME, new String[] {databaseIncidentHelper._ID,
                            databaseIncidentHelper.BEHAVIORID, databaseIncidentHelper.BEHAVIORNAME, databaseIncidentHelper.BEHAVIORDATE,
                            databaseIncidentHelper.BEHAVIORCONSEQUENCE, databaseIncidentHelper.BEHAVIORPARENTCONTACT, databaseIncidentHelper.BEHAVIORCOMMENTS},
                    null, null, null, null, null);

        }
        else {
            mCursor = database.query(true, databaseIncidentHelper.TABLE_NAME, new String[] {databaseIncidentHelper._ID,
                            databaseIncidentHelper.BEHAVIORID, databaseIncidentHelper.BEHAVIORNAME, databaseIncidentHelper.BEHAVIORDATE,
                            databaseIncidentHelper.BEHAVIORCONSEQUENCE, databaseIncidentHelper.BEHAVIORPARENTCONTACT, databaseIncidentHelper.BEHAVIORCOMMENTS},
                    databaseIncidentHelper.BEHAVIORDATE + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
}
