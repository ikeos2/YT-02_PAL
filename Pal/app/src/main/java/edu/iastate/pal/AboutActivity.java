package edu.iastate.pal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * ----------
 * File: AboutActivity.java
 * ----------
 * XML:
 *  + activity_about.xml
 * ----------
 * Nav:
 *  + LoginActivity.java --> Back to login screen
 * ----------
 * Func:
 *  + View information about the project developers
 * ----------
 * Dev:
 * @author Nathan Cool
 * ----------
 */
public class AboutActivity extends AppCompatActivity {

    private Toolbar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
    }

    private void initUI() {
        setContentView(R.layout.activity_about);

        actionBar = (Toolbar) findViewById(R.id.activity_about_actionBar);
        setSupportActionBar(actionBar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(AboutActivity.this, LoginActivity.class));
                finish();
                return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AboutActivity.this, LoginActivity.class));
        finish();
    }
}
