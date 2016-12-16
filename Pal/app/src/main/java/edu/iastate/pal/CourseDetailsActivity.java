package edu.iastate.pal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import edu.iastate.pal.network.VolleyResponseListener;
import edu.iastate.pal.network.VolleyUtils;
import edu.iastate.pal.templates.Course;

public class CourseDetailsActivity extends AppCompatActivity {
    private TextView courseName;
    private TextView instructor;
    private TextView location;
    private TextView days;
    private TextView sessionTime;

    private ArrayAdapter<String> filesAdapater;
    private ListView filesListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);

        initUI();

        ArrayList<String> mockData = new ArrayList<>();
        mockData.add("Reflection Essay");
        mockData.add("Lab8");
        mockData.add("Lab7");
        mockData.add("Stuff");
        mockData.add("MoreStuff");
        mockData.add("Even More Stuff");
        mockData.add("Design Interfaces");

        filesAdapater = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mockData);
        filesListView.setAdapter(filesAdapater);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        if(LoginActivity.activeUser.getUserType().equals("Faculty") || LoginActivity.activeUser.getUserType().equals("Admin")){
            inflater.inflate(R.menu.activity_course_details_overflow, menu);
        }else{
            //inflate the student menu
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.activity_course_details_uploadFile:
                Toast.makeText(this, "Upload this file", Toast.LENGTH_SHORT).show();
                break;
            case R.id.activity_course_details_delete_course:
                Toast.makeText(this, "Delete course", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

    private void initUI(){
        courseName = (TextView)findViewById(R.id.activity_courseDetails_courseTitleTextView);
        instructor = (TextView)findViewById(R.id.activity_courseDetails_courseIntructorTextView);
        location = (TextView)findViewById(R.id.activity_courseDetails_courseLocationTextView);
        days = (TextView)findViewById(R.id.activity_courseDetails_courseMeetingDaysTextView);
        sessionTime = (TextView)findViewById(R.id.activity_courseDetails_courseTimeTextView);
        filesListView = (ListView)findViewById(R.id.activity_course_details_files_listview);

        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.activity_courseDetails_courseName_linearLayout);
        linearLayout.setElevation(8.f);

        Intent courseDetailsIntent = getIntent();
        Course course = (Course) courseDetailsIntent.getSerializableExtra("course");

        courseName.setText(course.getCourseName());
        instructor.setText(course.getProfessorName());
        location.setText(course.getBuildingName() + " " + course.getRoomNumber());
        days.setText(course.getMeetingDays());
        sessionTime.setText(course.getStartTime() + " - " + course.getEndTime());


        Toolbar actionBar = (Toolbar) findViewById(R.id.activity_courseDetails_actionbar);
        setSupportActionBar(actionBar);
        assert getSupportActionBar() != null;
        actionBar.setElevation(0.0f);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }
}
