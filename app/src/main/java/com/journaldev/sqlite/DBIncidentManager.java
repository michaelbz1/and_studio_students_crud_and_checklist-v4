package com.journaldev.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBIncidentManager {

    private static final String TAG = "IncidentDbAdapter";
    private databaseIncidentHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBIncidentManager(Context c) {
        context = c;
    }

    public DBIncidentManager open() throws SQLException {
        dbHelper = new databaseIncidentHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String student_id, String student_name, String student_per, String student_cons, String student_parent, String student_comments) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(databaseIncidentHelper.BEHAVIORID, student_id);
        contentValue.put(databaseIncidentHelper.BEHAVIORNAME, student_name);
        contentValue.put(databaseIncidentHelper.BEHAVIORDATE, student_per);
        contentValue.put(databaseIncidentHelper.BEHAVIORCONSEQUENCE, student_cons);
        contentValue.put(databaseIncidentHelper.BEHAVIORPARENTCONTACT, student_parent);
        contentValue.put(databaseIncidentHelper.BEHAVIORCOMMENTS, student_comments);
        database.insert(databaseIncidentHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {

        String[] columns = new String[] { databaseIncidentHelper._ID, databaseIncidentHelper.BEHAVIORID, databaseIncidentHelper.BEHAVIORNAME, databaseIncidentHelper.BEHAVIORDATE, databaseIncidentHelper.BEHAVIORCONSEQUENCE, databaseIncidentHelper.BEHAVIORPARENTCONTACT, databaseIncidentHelper.BEHAVIORCOMMENTS };
        Cursor cursor = database.query(databaseIncidentHelper.TABLE_NAME, columns, null, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor fetchDistictBEHAVIORDATEiod() {

        String[] columns = new String[] { databaseIncidentHelper._ID, databaseIncidentHelper.BEHAVIORID, databaseIncidentHelper.BEHAVIORNAME, databaseIncidentHelper.BEHAVIORDATE, databaseIncidentHelper.BEHAVIORCONSEQUENCE, databaseIncidentHelper.BEHAVIORPARENTCONTACT, databaseIncidentHelper.BEHAVIORCOMMENTS };
        Cursor cursor = database.query(databaseIncidentHelper.TABLE_NAME, columns, databaseIncidentHelper.BEHAVIORDATE + " like '%" + "" + "%'", null, databaseIncidentHelper.BEHAVIORDATE, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String student_id, String student_name, String student_per, String student_cons, String student_parent, String student_comments) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(databaseIncidentHelper.BEHAVIORID, student_id);
        contentValues.put(databaseIncidentHelper.BEHAVIORNAME, student_name);
        contentValues.put(databaseIncidentHelper.BEHAVIORDATE, student_per);
        contentValues.put(databaseIncidentHelper.BEHAVIORCONSEQUENCE, student_cons);
        contentValues.put(databaseIncidentHelper.BEHAVIORPARENTCONTACT, student_parent);
        contentValues.put(databaseIncidentHelper.BEHAVIORCOMMENTS, student_comments);
        int i = database.update(databaseIncidentHelper.TABLE_NAME, contentValues, databaseIncidentHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(databaseIncidentHelper.TABLE_NAME, databaseIncidentHelper._ID + "=" + _id, null);
    }

    public Cursor fetchCountriesByName(String inputText) throws SQLException {
        //Log.w(TAG, inputText);
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
                    databaseIncidentHelper.BEHAVIORID + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
}
