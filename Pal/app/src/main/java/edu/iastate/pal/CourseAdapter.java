package edu.iastate.pal;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import edu.iastate.pal.templates.Course;

/**
 * Created by evanl on 10/30/2016.
 */

public class CourseAdapter extends ArrayAdapter<Course> {
    private Context context;
    private int layoutResourceId;
    private List<Course> courseList = null;

    private static class ViewHolder{
        TextView courseName;
        TextView buildingName;
        TextView roomNumber;
        TextView meetingDays;
        TextView meetingTime;
    };

    public CourseAdapter(Context context, int resourceId, List<Course> courseList){
        super(context, resourceId, courseList);
        this.context = context;
        layoutResourceId = resourceId;
        this.courseList = courseList;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder;

        if (row == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            viewHolder.courseName = (TextView)row.findViewById(R.id.activity_courseview_courseNameTextView);
            viewHolder.buildingName = (TextView)row.findViewById(R.id.activity_courseview_buildingNameTextView);
            viewHolder.roomNumber = (TextView) row.findViewById(R.id.activity_courseview_roomNumberTextView);
            viewHolder.meetingDays = (TextView)row.findViewById(R.id.activity_courseview_daysTextView);
            viewHolder.meetingTime = (TextView)row.findViewById(R.id.activity_courseview_classTimeTextView);

            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }

        Course course = getItem(position);
        viewHolder.courseName.setText(course.getCourseName());
        viewHolder.buildingName.setText(course.getBuildingName());
        viewHolder.roomNumber.setText(course.getRoomNumber());
        viewHolder.meetingDays.setText(course.getMeetingDays());
        viewHolder.meetingTime.setText(course.getStartTime() + " - " + course.getEndTime());

        return row;
    }
}
