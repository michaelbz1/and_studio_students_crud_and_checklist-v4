package com.journaldev.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBBehaviorsManager {

    private static final String TAG = "BehaviorsDbAdapter";
    private DatabaseBehaviorsHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBBehaviorsManager(Context c) {
        context = c;
    }

    public DBBehaviorsManager open() throws SQLException {
        dbHelper = new DatabaseBehaviorsHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String consequence_name, String consequence_sort_field) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseBehaviorsHelper.CONSEQUENCENAME, consequence_name);
        contentValue.put(DatabaseBehaviorsHelper.CONSEQUENCESORT, consequence_sort_field);
        database.insert(DatabaseBehaviorsHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {

        String[] columns = new String[] { DatabaseBehaviorsHelper._ID, DatabaseBehaviorsHelper.CONSEQUENCENAME, DatabaseBehaviorsHelper.CONSEQUENCESORT };
        Cursor cursor = database.query(DatabaseBehaviorsHelper.TABLE_NAME, columns, null, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String consequence_name, String consequence_sort_field) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseBehaviorsHelper.CONSEQUENCENAME, consequence_name);
        contentValue.put(DatabaseBehaviorsHelper.CONSEQUENCESORT, consequence_sort_field);
        int i = database.update(DatabaseBehaviorsHelper.TABLE_NAME, contentValue, DatabaseBehaviorsHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseBehaviorsHelper.TABLE_NAME, DatabaseBehaviorsHelper._ID + "=" + _id, null);
    }

    public Cursor fetchConsquencesByName(String inputText) throws SQLException {
        //Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = database.query(DatabaseBehaviorsHelper.TABLE_NAME, new String[] {DatabaseBehaviorsHelper._ID,
                            DatabaseBehaviorsHelper.CONSEQUENCENAME, DatabaseBehaviorsHelper.CONSEQUENCESORT},
                    null, null, null, null, null);

        }
        else {
            mCursor = database.query(true, DatabaseBehaviorsHelper.TABLE_NAME, new String[] {DatabaseBehaviorsHelper._ID,
                            DatabaseBehaviorsHelper.CONSEQUENCENAME, DatabaseBehaviorsHelper.CONSEQUENCESORT},
                    DatabaseBehaviorsHelper.CONSEQUENCENAME + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
}
