package com.example.yasmeen.teacherassistant;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.example.yasmeen.teacherassistant.Data.AttendanceCustomCursorAdapter;
import com.example.yasmeen.teacherassistant.Data.ClassContract;
import com.example.yasmeen.teacherassistant.Data.Student;
import com.example.yasmeen.teacherassistant.Data.StudentCustomCursorAdapter;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AttendanceActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TASK_LOADER_ID = 0 , STUDENT_LOADER_ID = 1 , ATTENDANCE_LOADER_ID = 2;
    MaterialBetterSpinner materialSpinner;
    ArrayList<String> arrayList = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter ;
    private AttendanceCustomCursorAdapter mAdapter ;
    RecyclerView mRecyclerView ;
    Bundle b = new Bundle() ;
    Button saveButton ;
    private ArrayList<Student> studentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        materialSpinner = (MaterialBetterSpinner)findViewById(R.id.android_material_design_spinner) ;

        //getSupportLoaderManager().initLoader(STUDENT_LOADER_ID, null , AttendanceActivity.this);
        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null , AttendanceActivity.this);


        arrayAdapter = new ArrayAdapter<String>(this , android.R.layout.simple_dropdown_item_1line , arrayList) ;
        materialSpinner.setAdapter(arrayAdapter);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        saveButton = (Button)findViewById(R.id.saveButton);

        saveButton.setClickable(false);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String data = "" ;
                ArrayList<Student> stList = ((AttendanceCustomCursorAdapter)mAdapter).getStudentList() ;
               // Calendar c = Calendar.getInstance();
               // int date = c.get(Calendar.DATE);
               // Calendar.mo

                String date = SimpleDateFormat.getDateInstance().format(new Date());

                for(int i = 0 ; i < stList.size() ; i++)
                {
                    Student singleStudent = stList.get(i) ;
                    if(singleStudent.isSelected() == true) {
                        data = data + "\n" + singleStudent.getName().toString();
                        ContentValues contentValues = new ContentValues() ;
                        contentValues.put(ClassContract.AttendanceEntry.COLUMN_COURSEID, materialSpinner.getText().toString());
                        contentValues.put(ClassContract.AttendanceEntry.COLUMN_STUDENTID, singleStudent.getName().toString());
                        contentValues.put(ClassContract.AttendanceEntry.COLUMN_Date, date);
                        Uri uri = getContentResolver().insert(ClassContract.AttendanceEntry.CONTENT_URI, contentValues);

                       /* if (uri != null) {
                            Toast.makeText(getApplicationContext(), uri.toString(), Toast.LENGTH_SHORT).show();
                        }*/

                    }
                }

                //Toast.makeText(AttendanceActivity.this, "Selected Student: \n" + data, Toast.LENGTH_SHORT).show();
                getSupportLoaderManager().initLoader(ATTENDANCE_LOADER_ID, null , AttendanceActivity.this);
                /*ArrayList<String> attList = mAdapter.getList();
                Calendar c = Calendar.getInstance();
                int date = c.get(Calendar.DATE);


                for(Object s : attList) {

                    ContentValues contentValues = new ContentValues() ;
                    contentValues.put(ClassContract.AttendanceEntry.COLUMN_COURSEID, materialSpinner.getText().toString());
                    contentValues.put(ClassContract.AttendanceEntry.COLUMN_STUDENTID, s.toString());
                    contentValues.put(ClassContract.AttendanceEntry.COLUMN_Date, String.valueOf(date));

                    Toast.makeText(getApplicationContext(), s.toString(), Toast.LENGTH_SHORT).show();

                    Uri uri = getContentResolver().insert(ClassContract.AttendanceEntry.CONTENT_URI, contentValues);

                    if (uri != null) {
                        Toast.makeText(getApplicationContext(), uri.toString(), Toast.LENGTH_SHORT).show();
                    }

                }

                getSupportLoaderManager().initLoader(ATTENDANCE_LOADER_ID, null , AttendanceActivity.this);*/


            }
        });

        


    }

    @Override
    public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {


        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mTaskData = null;

            @Override
            public Cursor loadInBackground() {
                try
                {
                    if(id == TASK_LOADER_ID) {
                        return getContentResolver().query(ClassContract.CoursesEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                ClassContract.CoursesEntry._ID);
                    }
                    else
                    if(id == STUDENT_LOADER_ID)
                    {
                        return getContentResolver().query(ClassContract.StudentsEntry.CONTENT_URI,
                                null,
                                ClassContract.StudentsEntry.COLUMN_COURSE+ "=?",
                                new String[]{args.getString("course")},
                                ClassContract.StudentsEntry._ID);
                    }
                    else
                    if(id == ATTENDANCE_LOADER_ID)
                    {
                        return getContentResolver().query(ClassContract.AttendanceEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                ClassContract.AttendanceEntry._ID);
                    }
                    else
                        return null ;
                }
                catch (Exception e)
                {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }


            }

            @Override
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }

            @Override
            protected void onStartLoading() {
                if(mTaskData != null)
                {
                    deliverResult(mTaskData);
                }
                else
                {
                    forceLoad();
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        switch (loader.getId()) {
            case TASK_LOADER_ID:
            {
                int courseIndex = data.getColumnIndex(ClassContract.CoursesEntry.COLUMN_CNAME);

                data.moveToFirst();
                while(data.isAfterLast() == false)
                {
                    arrayList.add(data.getString(courseIndex));
                    data.moveToNext();
                }
                arrayAdapter = new ArrayAdapter<String>(this , android.R.layout.simple_dropdown_item_1line , arrayList) ;
                materialSpinner.setAdapter(arrayAdapter);

                materialSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Toast.makeText(getApplicationContext(), materialSpinner.getText().toString() , Toast.LENGTH_LONG).show();

                        b.putString("course", materialSpinner.getText().toString());
                       // Toast.makeText(getApplicationContext(), b.getString("course") , Toast.LENGTH_LONG).show();
                        getSupportLoaderManager().restartLoader(STUDENT_LOADER_ID, b , AttendanceActivity.this);
                        saveButton.setClickable(true);
                    }
                });
                break;
            }
            case STUDENT_LOADER_ID:
            {
                int studentIndex = data.getColumnIndex(ClassContract.StudentsEntry.COLUMN_SNAME);

                studentList = new ArrayList<Student>();
                data.moveToFirst();
                while(data.isAfterLast() == false)
                {
                    Student st = new Student(data.getString(studentIndex) , false) ;
                    studentList.add(st);
                    data.moveToNext();
                }
                mAdapter = new AttendanceCustomCursorAdapter(studentList) ;

                mRecyclerView.setAdapter(mAdapter);

                break ;
            }

            case ATTENDANCE_LOADER_ID:
            {
                int courseIndex = data.getColumnIndex(ClassContract.AttendanceEntry.COLUMN_COURSEID);
                int studentIndex = data.getColumnIndex(ClassContract.AttendanceEntry.COLUMN_STUDENTID);
                int dateIndex = data.getColumnIndex(ClassContract.AttendanceEntry.COLUMN_Date);

                data.moveToFirst() ;

                while(data.isAfterLast() == false)
                {
                    Toast.makeText(getApplicationContext() ,data.getString(courseIndex) + " " +
                         data.getString(studentIndex) + "  " +
                            data.getString(dateIndex), Toast.LENGTH_SHORT ).show();
                    data.moveToNext();
                }

            }
        }



    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //mAdapter.swapCursor(null) ;
    }
}
