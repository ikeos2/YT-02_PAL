package edu.iastate.pal;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import edu.iastate.pal.NewCourseDialogFragment.SelectDaysDialogListener;
import edu.iastate.pal.network.VolleyResponseListener;
import edu.iastate.pal.network.VolleyUtils;

public class NewCourseActivity extends AppCompatActivity implements SelectDaysDialogListener{
    private final String[] daysArray = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
    private String startTime, endTime;

    private TextInputLayout courseName;
    private TextInputLayout professorName;
    private TextInputLayout buildingName;
    private TextInputLayout roomNumber;
    private Button daysButton;

    private boolean isStartTimeButtonClicked;
    private boolean isEndTimeButtonClicked;

    private ArrayList<Integer> mSelectedDays;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newcourse);
        initUI();
        isEndTimeButtonClicked = false;
        isStartTimeButtonClicked = false;
    }

    @Override
    public void onBackPressed() {
        final String className = courseName.getEditText().getText().toString();
        String teacher = professorName.getEditText().getText().toString();
        String building = buildingName.getEditText().getText().toString();
        String room = roomNumber.getEditText().getText().toString();
        String meetingDays = daysButton.getText().toString();

        boolean isACourseFieldEmpty =  checkForEmptyFields(className, teacher, building, room, meetingDays);

        if(isACourseFieldEmpty){
            Toast.makeText(this, "One or more course fields are empty", Toast.LENGTH_SHORT).show();
        }else{
            HashMap<String, String> data = new HashMap<>();
            data.put("path", "addCourse");
            data.put("name", className);
            data.put("instructor", teacher);
            data.put("location", building);
            data.put("owner", LoginActivity.activeUser.getUsername());
            data.put("token", LoginActivity.activeUser.getToken());
            data.put("startTime", startTime);
            data.put("endTime", endTime);
            data.put("days", meetingDays);
            data.put("room", room);

            VolleyUtils.volleyCall(getApplicationContext(), data, new VolleyResponseListener() {
                @Override
                public void onResponse(Object response) {
                    if(Objects.equals(response, "true")){
                        Toast.makeText(NewCourseActivity.this, "Added " + className, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(NewCourseActivity.this, "Error Adding Course", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(NewCourseActivity.this, "Error with call", Toast.LENGTH_SHORT).show();
                }
            });
            finish();

            super.onBackPressed();
        }
    }

    private boolean checkForEmptyFields(String className, String teacher, String building, String room, String meetingDays) {
        if(className.equals("") || teacher.equals("") || building.equals("") || room.equals("")|| meetingDays.equals("")) {
            return true;
        }else{
            return false;
        }
    }

    private void initUI(){
        courseName = (TextInputLayout) findViewById(R.id.activity_newcourse_courseNameWrapper);
        professorName = (TextInputLayout) findViewById(R.id.activity_newcourse_professorNameWrapper);
        buildingName = (TextInputLayout) findViewById(R.id.activity_newcourse_buildingNameWrapper);
        roomNumber = (TextInputLayout) findViewById(R.id.activity_newcourse_roomNumberWrapper);
        daysButton = (Button)findViewById(R.id.activity_newcourse_daysButton);

        startTime = getString(R.string.s_activity_newcourse_startTimeField);
        endTime = getString(R.string.s_activity_newcourse_endTimeField);

        courseName.setHint(getString(R.string.s_activity_newcourse_courseNameField));
        professorName.setHint(getString(R.string.s_activity_newcourse_professorNameField));
        buildingName.setHint(getString(R.string.s_activity_newcourse_buildingNameField));
        roomNumber.setHint(getString(R.string.s_activity_newcourse_roomNumberField));

        Toolbar actionBar = (Toolbar) findViewById(R.id.activity_newCourse_actionbar);
        setSupportActionBar(actionBar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Course");
    }

    /**
     * Used to create a dialog when button is pressed
     * @param view
     *  view of the button that was pressed
     */
    public void setMeetingDays(View view){
        NewCourseDialogFragment daysFragment = NewCourseDialogFragment.newInstance(mSelectedDays);
        daysFragment.show(getFragmentManager(), "NewCourseDialogFragment");
    }

    public void setTime(View view){
        setTimeDialog(view);
    }


    /**
     * Listener for DialogFragment
     * @param arrayList
     *      The arrayList of specific indices for days of the week
     */
    @Override
    public void onDialogPositiveClick(ArrayList<Integer> arrayList) {
        mSelectedDays = arrayList;
        if(arrayList.size() == 0){
            daysButton.setTextSize(18.f);
            daysButton.setTextColor(Color.GRAY);
            daysButton.setText(R.string.s_activity_newcourse_courseDaysField);
            return;
        }

        daysButton.setText("");
        String daysString = "";
        for (int i = 0; i < arrayList.size(); i++) {
            if(i == arrayList.size() - 1){
                daysString += daysArray[arrayList.get(i)];
            }else{
                daysString += daysArray[arrayList.get(i)] + ", ";
            }
        }

        daysButton.setTextSize(14.f);
        daysButton.setTextColor(Color.BLACK);
        daysButton.setText(daysString);
    }

    private void setTimeDialog(View view){
        final Button classTime = (Button) findViewById(view.getId());

        final String timeString = classTime.getText().toString();
        String[] splitTime = timeString.split(":| ");

        int hour = Integer.parseInt(splitTime[0]);
        final int min = Integer.parseInt(splitTime[1]);

        if(splitTime[2].equals("AM") && splitTime[0].equals("12")){
            hour = 0;
        }

        TimePickerDialog.OnTimeSetListener timeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (classTime.getId() == R.id.activity_newcourse_startTimeButton) {
                    startTime = timeChecker(hourOfDay, minute);
                    classTime.setText(startTime);

                    if (hourOfDay == 0) {
                        startTime = classTime.getText().toString();
                    }
                } else {
                    endTime = timeChecker(hourOfDay, minute);
                    classTime.setText(endTime);

                    if (hourOfDay == 0) {
                        endTime = classTime.getText().toString();
                    }
                }

                if (classTime.getId() == R.id.activity_newcourse_startTimeButton) {
                    isStartTimeButtonClicked = true;
                    if (!isEndTimeButtonClicked) {
                        Button endTime = (Button) findViewById(R.id.activity_newcourse_endTimeButton);
                        endTime.setText(timeChecker(hourOfDay + 1, minute));
                    }
                } else {
                    isEndTimeButtonClicked = true;
                    if (!isStartTimeButtonClicked) {
                        Button startTime = (Button) findViewById(R.id.activity_newcourse_startTimeButton);
                        startTime.setText(timeChecker(hourOfDay - 1, minute));
                    }
                }

                if(isEndTimeButtonClicked && isStartTimeButtonClicked){
                    isEndTimeButtonClicked = false;
                    isStartTimeButtonClicked = false;
                }
            }
        };

        //24 hour format determined later
        TimePickerDialog timePicker = new TimePickerDialog(this, timeListener, hour, min, false);
        timePicker.show();
    }

    private String timeChecker(int hour, int min){
        String minute;
        String hourOfDay;
        String timeOfDay = " AM";
        String start;

        if(hour < 0) {
            hourOfDay = "11";
            timeOfDay = " PM";
        } else if(hour == 0){
            hourOfDay = "12";
            timeOfDay = " AM";
        } else if(hour >= 12){
            if (hour == 12) {
                hourOfDay = "12";
                timeOfDay = " PM";
            }else{
                hourOfDay = "" + (hour - 12);
                timeOfDay = " PM";
            }

        }else{
            hourOfDay = "" + hour;
        }

        if(min < 10){
            minute = "0" + min;
        }else{
            minute = "" + min;
        }

        start = hourOfDay + ":" + minute + timeOfDay;
        return start;
    }

}
