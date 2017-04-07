package com.example.yasmeen.teacherassistant.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yasmeen.teacherassistant.R;

import java.util.ArrayList;

/**
 * Created by yasmeen on 2/8/2017.
 */

public class AttendanceCustomCursorAdapter extends RecyclerView.Adapter<AttendanceCustomCursorAdapter.AttendanceViewHolder> {

    //private Context mContext;
    private Cursor mCursor;
    ArrayList attendanceList  = new ArrayList<String>();
    Boolean[] boolList  = new Boolean[30];
    String course;
    private ArrayList<Student> stList ;



    public AttendanceCustomCursorAdapter(ArrayList<Student> students) {

       /* this.mContext = mContext ;
        for(int i =0 ; i <30 ; i++)
        {
            boolList[i] = false ;
        }*/
        this.stList = students ;



    }

    @Override
    public AttendanceCustomCursorAdapter.AttendanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_row, parent, false) ;
        return new AttendanceCustomCursorAdapter.AttendanceViewHolder(view);


    }

    @Override
    public void onBindViewHolder(final AttendanceCustomCursorAdapter.AttendanceViewHolder holder,int position) {


        final int pos = position ;
        /*int idIndex = mCursor.getColumnIndex(ClassContract.StudentsEntry._ID) ;
        int courseIndex = mCursor.getColumnIndex(ClassContract.StudentsEntry.COLUMN_SNAME);

        mCursor.moveToPosition(position);

        final int id = mCursor.getInt(idIndex);
        course = mCursor.getString(courseIndex);*/

        //holder.itemView.setTag(id);
        holder.studentName.setText(stList.get(position).getName());
        holder.attCheck.setChecked(stList.get(position).isSelected());
        holder.attCheck.setTag(stList.get(position));

        holder.attCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v ;
                Student contact = (Student) cb.getTag();

                contact.setSelected(cb.isChecked());
                stList.get(pos).setSelected(cb.isChecked());

               // Toast.makeText(v.getContext(), "Clicked on CheckBox: " + cb.getText() + " is " + cb.isChecked() , Toast.LENGTH_SHORT ).show();
            }
        });

        //in some cases, it will prevent unwanted situations
        //holder.attCheck.setOnCheckedChangeListener(null);

        //if true, your checkbox will be selected, else unselected
       // holder.attCheck.setChecked(boolList[position]);

      /* holder.attCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                boolList[position] = isChecked;

                Toast.makeText(mContext, course, Toast.LENGTH_SHORT).show();
                Toast.makeText(mContext, String.valueOf(position), Toast.LENGTH_SHORT).show();
                Toast.makeText(mContext, String.valueOf(isChecked), Toast.LENGTH_SHORT).show();
                if(isChecked == true)
                {
                    attendanceList.add(course);
                }
            }
        });*/



    }

    @Override
    public int getItemCount() {

        return stList.size() ;
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

    public ArrayList<Student> getStudentList()
    {

        return stList ;

    }

    class AttendanceViewHolder extends RecyclerView.ViewHolder
    {
        TextView studentName ;
        CheckBox attCheck ;



        public AttendanceViewHolder(View itemView) {
            super(itemView);
            studentName = (TextView)itemView.findViewById(R.id.textViewStudent);
            attCheck = (CheckBox)itemView.findViewById(R.id.attCheckBoxID);
        }
    }

}
