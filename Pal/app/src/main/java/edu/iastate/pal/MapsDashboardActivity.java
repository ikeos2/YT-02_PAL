package edu.iastate.pal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

/**
 * ----------
 * File: MapsDashboardActivity.java
 * ----------
 * XML:
 *  + activity_mapsdashboard.xml
 * ----------
 * Nav:
 *  + CourseLocatorMapActivity.java --> Start map to view/analyze courses
 *  +        DashboardActivity.java --> Navigate back to the main dashboard
 * ----------
 * Func:
 *  + Display information about the Course Locator map.
 *  + Navigate to the Course Locator map.
 * ----------
 * Dev:
 * @author Nathan Cool
 * ----------
 */
public class MapsDashboardActivity extends AppCompatActivity {

    /* Interface elements */
    private Button courseLocatorMapButton;
    private Toolbar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
        initOnClickListeners();
    }

    /**
     * Initialize interface elements
     */
    private void initUI() {
        setContentView(R.layout.activity_mapsdashboard);

        actionBar = (Toolbar) findViewById(R.id.activity_mapsDashboard_actionbar);
        setSupportActionBar(actionBar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.s_activity_mapsDashboard_title));

        courseLocatorMapButton = (Button) findViewById(R.id.activity_maps_dashboard_button_launch_courseLocator);
        assert courseLocatorMapButton != null;
    }

    private void initOnClickListeners() {
        courseLocatorMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsDashboardActivity.this, CourseLocatorMapActivity.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MapsDashboardActivity.this, DashboardActivity.class));
        finish();
    }
}
