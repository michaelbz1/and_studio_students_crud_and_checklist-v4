package com.journaldev.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBConsequenceManager {

    private static final String TAG = "ConsequenceDbAdapter";
    private DatabaseConsequenceHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBConsequenceManager(Context c) {
        context = c;
    }

    public DBConsequenceManager open() throws SQLException {
        dbHelper = new DatabaseConsequenceHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String consequence_name, String consequence_sort_field) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseConsequenceHelper.CONSEQUENCENAME, consequence_name);
        contentValue.put(DatabaseConsequenceHelper.CONSEQUENCESORT, consequence_sort_field);
        database.insert(DatabaseConsequenceHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {

        String[] columns = new String[] { DatabaseConsequenceHelper._ID, DatabaseConsequenceHelper.CONSEQUENCENAME, DatabaseConsequenceHelper.CONSEQUENCESORT };
        Cursor cursor = database.query(DatabaseConsequenceHelper.TABLE_NAME, columns, null, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String consequence_name, String consequence_sort_field) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseConsequenceHelper.CONSEQUENCENAME, consequence_name);
        contentValue.put(DatabaseConsequenceHelper.CONSEQUENCESORT, consequence_sort_field);
        int i = database.update(DatabaseConsequenceHelper.TABLE_NAME, contentValue, DatabaseConsequenceHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseConsequenceHelper.TABLE_NAME, DatabaseConsequenceHelper._ID + "=" + _id, null);
    }

    public Cursor fetchConsquencesByName(String inputText) throws SQLException {
        //Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = database.query(DatabaseConsequenceHelper.TABLE_NAME, new String[] {DatabaseConsequenceHelper._ID,
                            DatabaseConsequenceHelper.CONSEQUENCENAME, DatabaseConsequenceHelper.CONSEQUENCESORT},
                    null, null, null, null, null);

        }
        else {
            mCursor = database.query(true, DatabaseConsequenceHelper.TABLE_NAME, new String[] {DatabaseConsequenceHelper._ID,
                            DatabaseConsequenceHelper.CONSEQUENCENAME, DatabaseConsequenceHelper.CONSEQUENCESORT},
                    DatabaseConsequenceHelper.CONSEQUENCENAME + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
}
