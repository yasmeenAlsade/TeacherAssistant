package com.example.yasmeen.teacherassistant;

import android.app.Activity;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> basicFields;
    gridAdapter adapter;
    GridView gridView;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        basicFields = new ArrayList<String>() ;
        gridView = (GridView)findViewById(R.id.grid);
        basicFields.add("ATTENDANCE");
        basicFields.add("NOTES");
        basicFields.add("COURSES");
        basicFields.add("STUDENTS");
        adapter = new gridAdapter(this,basicFields);
        gridView.setAdapter(adapter);

        int screenSize = getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;

        String toastMsg;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                toastMsg = "Large screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                toastMsg = "Normal screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                toastMsg = "Small screen";
                break;
            default:
                toastMsg = "Screen size is neither large, normal or small";
        }
        //Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();


    }
}