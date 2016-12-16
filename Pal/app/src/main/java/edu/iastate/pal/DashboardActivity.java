package edu.iastate.pal;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Objects;

import edu.iastate.pal.network.VolleyResponseListener;
import edu.iastate.pal.network.VolleyUtils;

/**
 * ----------
 * File: DashboardActivity.java
 * ----------
 * XML:
 *  + activity_dashboard.xml
 * ----------
 * Nav:
 *  + LoginActivityActivity.java          --> If user logs out
 *  + NotesDashboardActivity.java         --> Main module page for notes
 *  + CourseManagerDashboardActivity.java --> Main module page for courses
 *  + ChatSessionActivity.java          --> Main module page for chat
 *  + MapsDashboardActivity.java          --> Main module page for maps
 * ----------
 * Func:
 *  + First Activity visible when user logs in
 *  + Navigate between four main modules
 *  + User logout
 * ----------
 * Dev:
 * @author Nathan Cool
 * ----------
 */
public class DashboardActivity extends AppCompatActivity {

    /* Interface elements */
    private ImageView chatModuleImageView;
    private ImageView courseManagerModuleImageView;
    private ImageView mapsModuleImageView;
    private ImageView notesModuleImageView;
    private TextView activeUserTextView;
    private Toolbar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initModuleButtonListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_dashboard_overflowmenu, menu);
        return true;
    }

    private void initUI() {
        setContentView(R.layout.activity_dashboard);

        actionBar = (Toolbar) findViewById(R.id.activity_dashboard_actionBar);
        setSupportActionBar(actionBar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle(getString(R.string.s_generic_word_dashboard));

        activeUserTextView = (TextView) findViewById(R.id.activity_dashboard_textView_activeUser);
        assert activeUserTextView != null;
        String activeUserString = LoginActivity.activeUser.getUsername() + " is logged in.";
        activeUserTextView.setText(activeUserString);

        notesModuleImageView = (ImageView) findViewById(R.id.activity_dashboard_imageView_notesModule);
        assert notesModuleImageView != null;
        courseManagerModuleImageView = (ImageView) findViewById(R.id.activity_dashboard_imageView_courseManagerModule);
        assert courseManagerModuleImageView != null;
        chatModuleImageView = (ImageView) findViewById(R.id.activity_dashboard_imageView_chatModule);
        assert chatModuleImageView != null;
        mapsModuleImageView = (ImageView) findViewById(R.id.activity_dashboard_imageView_mapsModule);
        assert mapsModuleImageView != null;
    }

    private void initModuleButtonListeners() {
        notesModuleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, NotesDashboardActivity.class));
            }
        });

        courseManagerModuleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, CourseManagerDashboardActivity.class));
            }
        });

        chatModuleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, ChatSessionActivity.class));
            }
        });

        mapsModuleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DashboardActivity.this, MapsDashboardActivity.class));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.activity_dashboard_overflow_logout:
                new AlertDialog.Builder(this)
                        .setTitle("Log Out")
                        .setMessage("Are you sure you want to log out? Your content will be synced.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                HashMap<String, String> data = new HashMap<>();
                                data.put("path", "logout");
                                data.put("token", LoginActivity.activeUser.getToken());

                                VolleyUtils.volleyCall(getApplicationContext(), data, new VolleyResponseListener() {
                                    @Override
                                    public void onResponse(Object response) {
                                        if(Objects.equals(response, "true")) {
                                            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                                            finish();
                                        } else {
                                            Toast.makeText(DashboardActivity.this, "Logout error. Contact dev.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onError(String message) {
                                        Log.e("DashboardActivity.java", message);
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Cancel note deletion
                            }
                        })
                        .setIcon(R.drawable.ic_logout_black)
                        .show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out? Your content will be synced.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        HashMap<String, String> data = new HashMap<>();
                        data.put("path", "logout");
                        data.put("token", LoginActivity.activeUser.getToken());

                        VolleyUtils.volleyCall(getApplicationContext(), data, new VolleyResponseListener() {
                            @Override
                            public void onResponse(Object response) {
                                if(Objects.equals(response, "true")) {
                                    startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(DashboardActivity.this, "Logout error. Contact dev.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onError(String message) {
                                Log.e("DashboardActivity", message);
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel note deletion
                    }
                })
                .setIcon(R.drawable.ic_logout_black)
                .show();
    }
}
