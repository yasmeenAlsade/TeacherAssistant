package com.example.yasmeen.teacherassistant.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by yasmeen on 1/13/2017.
 */

public class ClassContentProvider extends ContentProvider {

    public static final int COURSES = 100;
    public static final int COURSE_WITH_ID = 101;

    public static final int STUDENTS = 200;
    public static final int STUDENT_WITH_ID = 201;

    public static final int ATTENDANCE = 300;
    public static final int ATTENDANCE_WITH_ID = 301;

    private static final UriMatcher sUriMatcher = buildUriMatcher() ;

    public static UriMatcher buildUriMatcher()
    {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH) ;

        uriMatcher.addURI(ClassContract.AUTHORITY, ClassContract.PATH_COURSES, COURSES);
        uriMatcher.addURI(ClassContract.AUTHORITY, ClassContract.PATH_COURSES+"/#", COURSE_WITH_ID);

        uriMatcher.addURI(ClassContract.AUTHORITY, ClassContract.PATH_STUDENTS, STUDENTS);
        uriMatcher.addURI(ClassContract.AUTHORITY, ClassContract.PATH_STUDENTS+"/#", STUDENT_WITH_ID);

        uriMatcher.addURI(ClassContract.AUTHORITY, ClassContract.PATH_ATTENDANCE, ATTENDANCE);
        uriMatcher.addURI(ClassContract.AUTHORITY, ClassContract.PATH_ATTENDANCE+"/#", ATTENDANCE_WITH_ID);

        return uriMatcher ;
    }

    private ClassDBHelper mClassDbHelper;

    @Override
    public boolean onCreate() {

        Context context = getContext() ;
        mClassDbHelper = new ClassDBHelper(context) ;
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mClassDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri) ;
        Cursor retCursor ;

        switch(match)
        {
            case COURSES:
                retCursor = db.query(ClassContract.CoursesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break ;

            case STUDENTS:
                retCursor = db.query(ClassContract.StudentsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break ;

            case ATTENDANCE:
                retCursor = db.query(ClassContract.AttendanceEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break ;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = mClassDbHelper.getWritableDatabase() ;

        int match = sUriMatcher.match(uri) ;
        long id;

        Uri returnUri ;

        switch(match)
        {
            case COURSES:
                id = db.insert(ClassContract.CoursesEntry.TABLE_NAME, null, values);
                if(id > 0)
                {
                    returnUri = ContentUris.withAppendedId(ClassContract.CoursesEntry.CONTENT_URI, id) ;
                }
                else
                {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            case STUDENTS:
                id = db.insert(ClassContract.StudentsEntry.TABLE_NAME, null, values);
                if(id < 0)
                {
                    throw new android.database.SQLException("Failed to insert row into " + uri);

                }
                else
                {
                    returnUri = ContentUris.withAppendedId(ClassContract.StudentsEntry.CONTENT_URI, id) ;
                }
                break;

            case ATTENDANCE:
                id = db.insert(ClassContract.AttendanceEntry.TABLE_NAME, null, values);
                if(id < 0)
                {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                else
                {
                    returnUri = ContentUris.withAppendedId(ClassContract.AttendanceEntry.CONTENT_URI, id) ;
                }
                break;


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mClassDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int tasksDeleted;
        String id;

        switch (match)
        {
            case COURSE_WITH_ID:
                if(selection == null) {
                    id = uri.getPathSegments().get(1);
                    tasksDeleted = db.delete(ClassContract.CoursesEntry.TABLE_NAME, "_id=?", new String[]{id});
                }
                else
                {
                    tasksDeleted = db.delete(ClassContract.StudentsEntry.TABLE_NAME, selection, selectionArgs) ;
                }
                break ;

            case STUDENT_WITH_ID:
                id = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(ClassContract.StudentsEntry.TABLE_NAME, "_id=?", new String[]{id});
                break ;

            case ATTENDANCE_WITH_ID:
                id = uri.getPathSegments().get(1);
                tasksDeleted = db.delete(ClassContract.AttendanceEntry.TABLE_NAME, "_id=?", new String[]{id});
                break ;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if(tasksDeleted != 0)
        {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return tasksDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
