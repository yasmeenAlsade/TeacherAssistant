package com.example.yasmeen.teacherassistant;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.yasmeen.teacherassistant.Data.ClassContract;
import com.example.yasmeen.teacherassistant.Data.CustomCursorAdapter;
import com.example.yasmeen.teacherassistant.Data.StudentCustomCursorAdapter;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;


import java.util.ArrayList;

public class StudentsActivity extends AppCompatActivity  implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TASK_LOADER_ID = 0 , STUDENT_LOADER_ID = 1 ;
    MaterialBetterSpinner materialSpinner;
    ArrayList<String> arrayList = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter ;
    private StudentCustomCursorAdapter mAdapter ;
    RecyclerView mRecyclerView ;
    Bundle b = new Bundle() ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);
        materialSpinner = (MaterialBetterSpinner)findViewById(R.id.android_material_design_spinner) ;

        getSupportLoaderManager().initLoader(STUDENT_LOADER_ID, null , StudentsActivity.this);
        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null , StudentsActivity.this);


        arrayAdapter = new ArrayAdapter<String>(this , android.R.layout.simple_dropdown_item_1line , arrayList) ;
        materialSpinner.setAdapter(arrayAdapter);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new StudentCustomCursorAdapter(this) ;

        mRecyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                int id = (int)viewHolder.itemView.getTag() ;

                String stringId = Integer.toString(id);
                Uri uri = ClassContract.StudentsEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();
                b.putString("course" , materialSpinner.getText().toString());

                getContentResolver().delete(uri, null, null) ;
                getSupportLoaderManager().restartLoader(STUDENT_LOADER_ID, b ,StudentsActivity.this);

            }
        }).attachToRecyclerView(mRecyclerView);

        FloatingActionButton fabButton = (FloatingActionButton)findViewById(R.id.fab) ;


        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog() ;
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
                        //Toast.makeText(getApplicationContext(), b.getString("course") , Toast.LENGTH_LONG).show();
                        getSupportLoaderManager().restartLoader(STUDENT_LOADER_ID, b , StudentsActivity.this);
                    }
                });
                break;
            }
            case STUDENT_LOADER_ID:
            {
                mAdapter.swapCursor(data) ;
            }
        }



    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null) ;
    }


    void dialog()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        alert.setTitle("Add Student") ;
        LayoutInflater inflater = getLayoutInflater();
        final View v = inflater.inflate(R.layout.students_dialog, null);
        final String text = materialSpinner.getText().toString();
        alert.setView(v);


        alert.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                EditText editText = (EditText)v.findViewById(R.id.studentEditText) ;
                String courseName = editText.getText().toString() ;

                String studentID = ((EditText)v.findViewById(R.id.studentIDEditText)).getText().toString();
                String studentCountry = ((EditText)v.findViewById(R.id.studentCountryEditText)).getText().toString();


                if(courseName.length() == 0)
                    return;

                ContentValues contentValues = new ContentValues() ;

               /* Toast.makeText(getBaseContext(), courseName, Toast.LENGTH_LONG).show();
                Toast.makeText(getBaseContext(), studentID, Toast.LENGTH_LONG).show();
                Toast.makeText(getBaseContext(), studentCountry, Toast.LENGTH_LONG).show();
                Toast.makeText(getBaseContext(), text, Toast.LENGTH_LONG).show();*/

                contentValues.put(ClassContract.StudentsEntry.COLUMN_SNAME, courseName);
                contentValues.put(ClassContract.StudentsEntry.COLUMN_UNID, studentID);
                contentValues.put(ClassContract.StudentsEntry.COLUMN_COUNTRY,studentCountry) ;
                contentValues.put(ClassContract.StudentsEntry.COLUMN_COURSE, text);

                Uri uri = getContentResolver().insert(ClassContract.StudentsEntry.CONTENT_URI, contentValues) ;

                b.putString("course" , text);

               /* if(uri != null)
                {
                    Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
                }*/
                getSupportLoaderManager().restartLoader(STUDENT_LOADER_ID, b, StudentsActivity.this);

            }
        });


        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();

    }
}
