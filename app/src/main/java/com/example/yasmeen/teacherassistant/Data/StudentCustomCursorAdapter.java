package com.example.yasmeen.teacherassistant.Data;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yasmeen.teacherassistant.R;

/**
 * Created by yasmeen on 1/13/2017.
 */

public class StudentCustomCursorAdapter extends RecyclerView.Adapter<StudentCustomCursorAdapter.CourseViewHolder> implements
View.OnClickListener{

    private Context mContext;
    private Cursor mCursor; ;


    @Override
    public void onClick(View v) {
       // Toast.makeText(mContext, "onClick " + v.getTag() + " ", Toast.LENGTH_SHORT).show();
        Cursor cursor2 =  mContext.getContentResolver().query(ClassContract.StudentsEntry.CONTENT_URI,
                null,
                ClassContract.StudentsEntry._ID+ "=?",
                new String[]{v.getTag().toString()},
                ClassContract.AttendanceEntry._ID);
        cursor2.moveToFirst() ;
        int nameIndex = cursor2.getColumnIndex(ClassContract.StudentsEntry.COLUMN_SNAME) ;
        String nameStudent = cursor2.getString(nameIndex) ;

       Cursor cursor =  mContext.getContentResolver().query(ClassContract.AttendanceEntry.CONTENT_URI,
                null,
                ClassContract.AttendanceEntry.COLUMN_STUDENTID+ "=?",
                new String[]{nameStudent},
                ClassContract.AttendanceEntry._ID);
        if(cursor != null)
        {
            int count = 0 ;
            String s = "" ;
            int courseIndex = cursor.getColumnIndex(ClassContract.AttendanceEntry.COLUMN_COURSEID);
            int dateIndex = cursor.getColumnIndex(ClassContract.AttendanceEntry.COLUMN_Date);
            cursor.moveToFirst() ;

            while(cursor.isAfterLast() == false)
            {
                count++ ;
                s= s + "\n" + cursor.getString(dateIndex)  ;
                cursor.moveToNext();
            }
            if(count != 0)
            Toast.makeText(mContext ,"Absent " + count + " Days on" + s, Toast.LENGTH_SHORT ).show();
            else
                Toast.makeText(mContext, "There Is No Absence ", Toast.LENGTH_SHORT).show();


        }

    }

    public StudentCustomCursorAdapter(Context mContext) {
        this.mContext = mContext ;

    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.course_layout, parent, false) ;
        return new CourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {

        int idIndex = mCursor.getColumnIndex(ClassContract.StudentsEntry._ID) ;
        int courseIndex = mCursor.getColumnIndex(ClassContract.StudentsEntry.COLUMN_SNAME);

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
            StudentCustomCursorAdapter customAD = new StudentCustomCursorAdapter(mContext) ;
            itemView.setOnClickListener(customAD);
        }
    }
}
