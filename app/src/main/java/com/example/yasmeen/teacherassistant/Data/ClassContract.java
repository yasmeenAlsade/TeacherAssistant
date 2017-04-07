package com.example.yasmeen.teacherassistant.Data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by yasmeen on 1/13/2017.
 */

public class ClassContract {

    public static final String AUTHORITY = "com.example.android.teacherassistant" ;
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);


    public static final String PATH_STUDENTS = "students";
    public static final String PATH_ATTENDANCE= "attendance";
    public static final String PATH_COURSES= "courses";

    public static final class StudentsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_STUDENTS).build();

        public static final String TABLE_NAME = "students";

        public static final String COLUMN_SNAME = "sname";
        public static final String COLUMN_UNID = "unid";
        public static final String COLUMN_COUNTRY = "country" ;
        public static final String COLUMN_COURSE = "courseName" ;
    }

    public static final class AttendanceEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ATTENDANCE).build();

        public static final String TABLE_NAME = "attendance";

        public static final String COLUMN_COURSEID = "courseid";
        public static final String COLUMN_STUDENTID = "studentid";
        public static final String COLUMN_Date = "date" ;
    }

    public static final class CoursesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_COURSES).build();

        public static final String TABLE_NAME = "courses";

        public static final String COLUMN_CNAME = "cname";
    }
}
