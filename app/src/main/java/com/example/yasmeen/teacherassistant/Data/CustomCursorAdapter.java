package com.example.yasmeen.teacherassistant.Data;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yasmeen.teacherassistant.R;

/**
 * Created by yasmeen on 1/13/2017.
 */

public class CustomCursorAdapter extends RecyclerView.Adapter<CustomCursorAdapter.CourseViewHolder> {

    private Context mContext;
    private Cursor mCursor;


    public CustomCursorAdapter(Context mContext) {

        this.mContext = mContext ;

    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.course_layout, parent, false) ;
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {

        int idIndex = mCursor.getColumnIndex(ClassContract.CoursesEntry._ID) ;
        int courseIndex = mCursor.getColumnIndex(ClassContract.CoursesEntry.COLUMN_CNAME);

        mCursor.moveToPosition(position);

        final int id = mCursor.getInt(idIndex);
        String course = mCursor.getString(courseIndex);


        holder.itemView.setTag(id);
        holder.courseName.setText(course);


    }

    @Override
    public int getItemCount() {
        if(mCursor == null)
            return 0 ;
        return mCursor.getCount() ;
    }

    public Cursor swapCursor(Cursor c)
    {
        if(mCursor == c)
            return null ;

        Cursor temp = mCursor ;
        this.mCursor = c ;

        if(c != null)
            this.notifyDataSetChanged();

        return temp ;
    }

    class CourseViewHolder extends RecyclerView.ViewHolder
    {
        TextView courseName ;

        public CourseViewHolder(View itemView) {
            super(itemView);
            courseName = (TextView)itemView.findViewById(R.id.textViewCourse);
        }
    }
}
