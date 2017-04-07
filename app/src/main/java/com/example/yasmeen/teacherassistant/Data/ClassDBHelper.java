package com.example.yasmeen.teacherassistant.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by yasmeen on 1/13/2017.
 */

public class ClassDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "classDb.db";
    private static final int VERSION = 1;

    public ClassDBHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TABLE2 = "CREATE TABLE " + ClassContract.StudentsEntry.TABLE_NAME + " (" +
                ClassContract.StudentsEntry._ID          + " INTEGER PRIMARY KEY, " +
                ClassContract.StudentsEntry.COLUMN_SNAME + " TEXT NOT NULL, " +
                ClassContract.StudentsEntry.COLUMN_UNID + " TEXT NOT NULL, " +
                ClassContract.StudentsEntry.COLUMN_COUNTRY + " TEXT NOT NULL, " +
                ClassContract.StudentsEntry.COLUMN_COURSE + " TEXT NOT NULL);" ;

        db.execSQL(CREATE_TABLE2);

        final String CREATE_TABLE1 = "CREATE TABLE " + ClassContract.CoursesEntry.TABLE_NAME + " (" +
                ClassContract.CoursesEntry._ID          + " INTEGER PRIMARY KEY, " +
                ClassContract.CoursesEntry.COLUMN_CNAME + " TEXT NOT NULL);" ;

        db.execSQL(CREATE_TABLE1);



        final String CREATE_TABLE3 = "CREATE TABLE " + ClassContract.AttendanceEntry.TABLE_NAME + " (" +
                ClassContract.AttendanceEntry._ID          + " INTEGER PRIMARY KEY, " +
                ClassContract.AttendanceEntry.COLUMN_COURSEID + " TEXT NOT NULL, " +
                ClassContract.AttendanceEntry.COLUMN_STUDENTID + " TEXT NOT NULL, " +
                ClassContract.AttendanceEntry.COLUMN_Date + " TEXT NOT NULL);" ;

        db.execSQL(CREATE_TABLE3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + ClassContract.CoursesEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ClassContract.StudentsEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ClassContract.AttendanceEntry.TABLE_NAME);

        onCreate(db);

    }
}
