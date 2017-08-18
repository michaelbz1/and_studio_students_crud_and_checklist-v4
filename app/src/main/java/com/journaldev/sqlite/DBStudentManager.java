package com.journaldev.sqlite;

/**
 * Created by anupamchugh on 19/10/15.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBStudentManager {

    private static final String TAG = "CountriesDbAdapter";
    private DatabaseStudentHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBStudentManager(Context c) {
        context = c;
    }

    public DBStudentManager open() throws SQLException {
        dbHelper = new DatabaseStudentHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String itemId, String student_id, String student_name, String student_per) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseStudentHelper._ID, itemId);
        contentValue.put(DatabaseStudentHelper.STUDENTID, student_id);
        contentValue.put(DatabaseStudentHelper.STUDENTNAME, student_name);
        contentValue.put(DatabaseStudentHelper.STUDENTPER, student_per);
        database.insert(DatabaseStudentHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {

        String[] columns = new String[] { DatabaseStudentHelper._ID, DatabaseStudentHelper.STUDENTID, DatabaseStudentHelper.STUDENTNAME, DatabaseStudentHelper.STUDENTPER };
        Cursor cursor = database.query(DatabaseStudentHelper.TABLE_NAME, columns, null, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public Cursor fetchDistictStudentPeriod() {

        String[] columns = new String[] { DatabaseStudentHelper._ID, DatabaseStudentHelper.STUDENTID, DatabaseStudentHelper.STUDENTNAME, DatabaseStudentHelper.STUDENTPER };
        Cursor cursor = database.query(DatabaseStudentHelper.TABLE_NAME, columns, DatabaseStudentHelper.STUDENTPER + " like '%" + "" + "%'", null, DatabaseStudentHelper.STUDENTPER, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    public int update(String _id, String student_id, String student_name, String student_per) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseStudentHelper.STUDENTID, student_id);
        contentValues.put(DatabaseStudentHelper.STUDENTNAME, student_name);
        contentValues.put(DatabaseStudentHelper.STUDENTPER, student_per);
        int i = database.update(DatabaseStudentHelper.TABLE_NAME, contentValues, DatabaseStudentHelper._ID + " = '" + _id + "'", null);
        return i;
    }

    public void delete(String _id) {
        database.delete(DatabaseStudentHelper.TABLE_NAME, DatabaseStudentHelper._ID + "='" + _id + "'", null);
    }

    public Cursor fetchCountriesByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = database.query(DatabaseStudentHelper.TABLE_NAME, new String[] {DatabaseStudentHelper._ID,
                            DatabaseStudentHelper.STUDENTID, DatabaseStudentHelper.STUDENTNAME, DatabaseStudentHelper.STUDENTPER},
                    null, null, null, null, null);

        }
        else {
            mCursor = database.query(true, DatabaseStudentHelper.TABLE_NAME, new String[] {DatabaseStudentHelper._ID,
                            DatabaseStudentHelper.STUDENTID, DatabaseStudentHelper.STUDENTNAME, DatabaseStudentHelper.STUDENTPER},
                    DatabaseStudentHelper.STUDENTPER + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
}
