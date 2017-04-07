package com.example.yasmeen.teacherassistant;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yasmeen.teacherassistant.Data.ClassContract;
import com.example.yasmeen.teacherassistant.Data.CustomCursorAdapter;

import java.io.IOException;

public class CoursesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TASK_LOADER_ID = 0;
    private CustomCursorAdapter mAdapter ;
    RecyclerView mRecyclerView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new CustomCursorAdapter(this) ;

        mRecyclerView.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                int id = (int)viewHolder.itemView.getTag() ;
                String course = ((TextView)viewHolder.itemView.findViewById(R.id.textViewCourse)).getText().toString();
               // Toast.makeText(getApplicationContext(), course , Toast.LENGTH_LONG).show();

                String stringId = Integer.toString(id);
                Uri uri = ClassContract.CoursesEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(stringId).build();

                getContentResolver().delete(uri, null, null) ;
                getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null , CoursesActivity.this);
                getContentResolver().delete(uri,  ClassContract.StudentsEntry.COLUMN_COURSE+ "=?", new String[]{course});

            }
        }).attachToRecyclerView(mRecyclerView);

        FloatingActionButton fabButton = (FloatingActionButton)findViewById(R.id.fab) ;

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               dialog() ;
            }
        });

        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this) ;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor mTaskData = null;

            @Override
            public Cursor loadInBackground() {
                try
                {
                    return getContentResolver().query(ClassContract.CoursesEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            ClassContract.CoursesEntry._ID);
                }
                catch (Exception e)
                {
                    Log.e(TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
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

            @Override
            public void deliverResult(Cursor data) {
                mTaskData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data) ;

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }


    void dialog()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        alert.setTitle("Add Course") ;
        LayoutInflater inflater = getLayoutInflater();
        final View v = inflater.inflate(R.layout.courses_dialog, null);
        alert.setView(v);


        alert.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                EditText editText = (EditText)v.findViewById(R.id.courseEditText) ;
                String courseName = editText.getText().toString() ;
                if(courseName.length() == 0)
                    return;

                ContentValues contentValues = new ContentValues() ;

                contentValues.put(ClassContract.CoursesEntry.COLUMN_CNAME, courseName);

                Uri uri = getContentResolver().insert(ClassContract.CoursesEntry.CONTENT_URI, contentValues) ;

               /* if(uri != null)
                {
                    Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
                }*/
                getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null , CoursesActivity.this);

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
