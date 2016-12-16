package edu.iastate.pal;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import edu.iastate.pal.network.VolleyResponseListener;
import edu.iastate.pal.network.VolleyUtils;
import edu.iastate.pal.templates.Course;

public class CourseManagerDashboardActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private ListView courseList;
    private CourseAdapter courseAdapter;
    private FloatingActionButton courseFab;
    private ArrayList<Course> courses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coursemanager_dashboard);
        courses = new ArrayList<>();

        courseList = (ListView) findViewById(R.id.activity_courseManagerDashboard_listView_notes);
        courseFab = (FloatingActionButton) findViewById(R.id.activity_courseManagerDashboard_fab_newCourse);

        courseAdapter = new CourseAdapter(this, R.layout.activity_courseview, courses);
        courseList.setAdapter(courseAdapter);

        Toolbar actionBar = (Toolbar) findViewById(R.id.activity_courseManagerDashboard_actionbar);
        setSupportActionBar(actionBar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Course Manager");
    }

    @Override
    protected void onResume() {
        super.onResume();
        courses.clear();

        HashMap<String, String> data = new HashMap<>();
        data.put("path", "getCourses");
        data.put("owner", LoginActivity.activeUser.getUsername());
        data.put("token", LoginActivity.activeUser.getToken());

        VolleyUtils.volleyCall(getApplicationContext(), data, new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                grabCourses((String) response);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(CourseManagerDashboardActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        });

        courseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course course = (Course) courseList.getItemAtPosition(position);
                Intent intent = new Intent(CourseManagerDashboardActivity.this, CourseDetailsActivity.class);
                intent.putExtra("course", course);
                startActivity(intent);
            }
        });

        courseList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                Course course = (Course) courseList.getItemAtPosition(position);
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("path", "deleteCourse");
                data.put("id", String.valueOf(course.getuID()));
                data.put("token", LoginActivity.activeUser.getToken());

                VolleyUtils.volleyCall(getApplicationContext(), data, new VolleyResponseListener() {
                    @Override
                    public void onResponse(Object response) {
                        if(Objects.equals(response, "true")){
                            courses.remove(position);
                            courseAdapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(CourseManagerDashboardActivity.this, "Failed to delete course", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
                return true;
            }
        });


        courseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseManagerDashboardActivity.this, NewCourseActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

    }

    private void grabCourses(String response) {
        try {
            JSONArray dataArray = new JSONArray((String) response);
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject temp = dataArray.getJSONObject(i);
                String courseName = temp.getString("courseName");
                String instructor = temp.getString("instructor");
                String location = temp.getString("location");
                String roomNumber = temp.getString("room");
                String days = temp.getString("days");
                String startTime = temp.getString("startTime");
                String endTime = temp.getString("endTime");
                Course newCourse = new Course(courseName, instructor, location, roomNumber, days, startTime, endTime);
                newCourse.setuID(temp.getString("uid"));
                courses.add(newCourse);
            }

            if (courses.size() > 0) {
                TextView noCourses = (TextView) findViewById(R.id.activity_courseManagerDashboard_noCourseTextView);
                noCourses.setVisibility(View.GONE);
            }
            courseAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
