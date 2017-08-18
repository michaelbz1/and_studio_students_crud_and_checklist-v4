package com.journaldev.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBClassPeriodsManager {

    private static final String TAG = "ClassPeriodsDbAdapter";
    private DatabaseClassPeriodsHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBClassPeriodsManager(Context c) {
        context = c;
    }

    public DBClassPeriodsManager open() throws SQLException {
        dbHelper = new DatabaseClassPeriodsHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String ClassPeriods_name) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseClassPeriodsHelper.PERIODSNAME, ClassPeriods_name);
        database.insert(DatabaseClassPeriodsHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {

        String[] columns = new String[] { DatabaseClassPeriodsHelper._ID, DatabaseClassPeriodsHelper.PERIODSNAME};
        Cursor cursor = database.query(DatabaseClassPeriodsHelper.TABLE_NAME, columns, null, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String ClassPeriods_name) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseClassPeriodsHelper.PERIODSNAME, ClassPeriods_name);
        int i = database.update(DatabaseClassPeriodsHelper.TABLE_NAME, contentValue, DatabaseClassPeriodsHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseClassPeriodsHelper.TABLE_NAME, DatabaseClassPeriodsHelper._ID + "=" + _id, null);
    }

    public Cursor fetchClassPeriodsByName(String inputText) throws SQLException {
        //Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = database.query(DatabaseClassPeriodsHelper.TABLE_NAME, new String[] {DatabaseClassPeriodsHelper._ID,
                            DatabaseClassPeriodsHelper.PERIODSNAME},
                    null, null, null, null, null);

        }
        else {
            mCursor = database.query(true, DatabaseClassPeriodsHelper.TABLE_NAME, new String[] {DatabaseClassPeriodsHelper._ID,
                            DatabaseClassPeriodsHelper.PERIODSNAME},
                    DatabaseClassPeriodsHelper.PERIODSNAME + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
}
