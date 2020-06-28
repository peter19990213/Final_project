package com.example.final_project.data;

import com.example.final_project.data.FinalContract.FinalEntry;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "final.db";

    private static final int DATABASE_VERSION = 5;

    public MyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FINAL_TABLE = "CREATE TABLE " + FinalEntry.TABLE_NAME + " (" +
                FinalEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FinalEntry.COLUMN_longitude + " REAL NOT NULL, " +
                FinalEntry.COLUMN_latitude + " REAL NOT NULL, " +
                FinalEntry.COLUMN_name + " Text" +
                "); ";
        sqLiteDatabase.execSQL(SQL_CREATE_FINAL_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FinalEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
