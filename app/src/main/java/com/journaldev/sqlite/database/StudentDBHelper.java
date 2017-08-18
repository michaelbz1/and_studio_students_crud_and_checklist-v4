package com.journaldev.sqlite.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StudentDBHelper extends SQLiteOpenHelper {

    ///////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////
    //This helper class' function is only to create (upgrade) the database//
    ///////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////

    //public static final String DB_FILE_NAME = "nadias.db";
    public static final String DB_FILE_NAME = "STUDENTDB.DB";
    public static final int DB_VERSION = 6;

    //Create the database.
    public StudentDBHelper(Context context)
    {
        super(context, DB_FILE_NAME, null, DB_VERSION);
    }

    //Create the table.  SQL_CREATE is in the DataItem Class.
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(ItemsTable.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ItemsTable.SQL_DELETE);
        onCreate(db);
    }
}
