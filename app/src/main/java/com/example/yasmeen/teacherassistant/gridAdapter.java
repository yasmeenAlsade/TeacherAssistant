package com.example.yasmeen.teacherassistant;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yasmeen.teacherassistant.Data.ClassContract;

import java.util.ArrayList;

/**
 * Created by yasmeen on 1/7/2017.
 */
public class gridAdapter extends BaseAdapter {

    ArrayList names;
    public static Activity activity;

    public gridAdapter(Activity activity, ArrayList names) {

        this.activity = activity;
        this.names = names;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater vi = LayoutInflater.from(activity);
            view = vi.inflate(R.layout.grid_single, null);
        }
        TextView textView = (TextView) view.findViewById(R.id.namePlacer);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageHolder);

        if (names.get(position).toString().equals("ATTENDANCE")) {
            imageView.setImageResource(R.drawable.ic_attendance);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(activity.getBaseContext(), "attendance", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent( v.getContext() , AttendanceActivity.class) ;
                    v.getContext().startActivity(intent);
                }
            });
        } else if (names.get(position).toString().equals("COURSES")) {
            imageView.setImageResource(R.drawable.ic_courses);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(activity.getBaseContext(), "courses", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent( v.getContext() , CoursesActivity.class) ;
                    v.getContext().startActivity(intent);
                }
            });
        } else if (names.get(position).toString().equals("NOTES")) {
            imageView.setImageResource(R.drawable.ic_notes);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   Toast.makeText(activity.getBaseContext(), "notes", Toast.LENGTH_LONG).show();
                }
            });
        } else if (names.get(position).toString().equals("STUDENTS")) {
            imageView.setImageResource(R.drawable.ic_students);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   Toast.makeText(activity.getBaseContext(), "students", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent( v.getContext() , StudentsActivity.class) ;
                    v.getContext().startActivity(intent);
                }
            });
        }

        textView.setText(names.get(position).toString());
        return view;
    }
}
