package edu.iastate.pal;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Objects;

import edu.iastate.pal.network.VolleyResponseListener;
import edu.iastate.pal.network.VolleyUtils;

/**
 * ----------
 * File: CreateAccountActivity.java
 * ----------
 * XML:
 *  + activity_createaccount.xml
 * ----------
 * Nav:
 *  + LoginActivity.java --> Back to login once new user is created
 * ----------
 * Func:
 *  + User account creation
 *     - Student user OR
 *     - Faculty user (for professors, lecturers, lab staff, etc.)
 * ----------
 * Dev:
 * @author Nathan Cool
 * ----------
 */
public class CreateAccountActivity extends AppCompatActivity {

    /* Interface elements */
    private Button createUserAccountButton;
    private Spinner userTypeSpinner;
    private TextInputEditText newEmailEditText;
    private TextInputEditText newPasswordEditText;
    private TextInputEditText newUsernameEditText;
    private Toolbar actionBar;

    /* Variables and miscellaneous objects */
    private ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initUserTypeSpinnerListener();
        initCreateUserAccountButtonListener();
    }

    /**
     * Initialize interface elements
     */
    private void initUI() {
        setContentView(R.layout.activity_createaccount);

        actionBar = (Toolbar) findViewById(R.id.activity_createAccount_actionBar);
        setSupportActionBar(actionBar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        userTypeSpinner = (Spinner) findViewById(R.id.activity_createAccount_spinner_userType);
        assert userTypeSpinner != null;

        newUsernameEditText = (TextInputEditText) findViewById(R.id.activity_createAccount_editText_newUsername);
        assert newUsernameEditText != null;
        newPasswordEditText = (TextInputEditText) findViewById(R.id.activity_createAccount_editText_newPassword);
        assert newPasswordEditText != null;
        newEmailEditText = (TextInputEditText) findViewById(R.id.activity_createAccount_editText_newEmail);
        assert newEmailEditText != null;

        createUserAccountButton = (Button) findViewById(R.id.activity_createAccount_button_createUserAccount);
        assert createUserAccountButton != null;
    }

    private void initUserTypeSpinnerListener() {
        adapter = ArrayAdapter.createFromResource(this, R.array.s_activity_createAccount_spinnerOptions_userTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSpinner.setAdapter(adapter);
    }

    private void initCreateUserAccountButtonListener() {
        createUserAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> data = new HashMap<>();
                data.put("path", "createUser");
                data.put("name", newUsernameEditText.getText().toString());
                data.put("password", newPasswordEditText.getText().toString());
                data.put("email", newEmailEditText.getText().toString());
                data.put("type", userTypeSpinner.getSelectedItem().toString());

                VolleyUtils.volleyCall(getApplicationContext(), data, new VolleyResponseListener() {
                    @Override
                    public void onResponse(Object response) {
                        if(Objects.equals(response, "User created successfully")) {
                            startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
                            finish();
                        } else if(Objects.equals(response, "Username already exists")){
                            Toast.makeText(CreateAccountActivity.this, getString(R.string.s_activity_createAccount_errorMessage_usernameExists), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CreateAccountActivity.this, getString(R.string.s_activity_createAccount_toastMessage_createUserGeneralError), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String message) {
                        Log.e("CreateAccountActivity", message);
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if(newUsernameEditText.getText().toString().equals("") || newPasswordEditText.getText().toString().equals("") || newEmailEditText.getText().toString().equals("")) {
                    new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.s_activity_createAccount_alertDialog_returnToLoginWithEmptyFields_title))
                            .setMessage(getString(R.string.s_activity_createAccount_alertDialog_returnToLoginWithEmptyFields_message))
                            .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(CreateAccountActivity.this, getString(R.string.s_activity_createAccount_toastMessage_userDiscarded), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
                                    finish();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue creating new user account
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.s_activity_createAccount_alertDialog_returnToLoginWithCompletedFields_title))
                            .setMessage(getString(R.string.s_activity_createAccount_alertDialog_returnToLoginWithCompletedFields_message))
                            .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    HashMap<String, String> data = new HashMap<>();
                                    data.put("path", "createUser");
                                    data.put("name", newUsernameEditText.getText().toString());
                                    data.put("password", newPasswordEditText.getText().toString());
                                    data.put("email", newEmailEditText.getText().toString());
                                    data.put("type", userTypeSpinner.getSelectedItem().toString());

                                    VolleyUtils.volleyCall(getApplicationContext(), data, new VolleyResponseListener() {
                                        @Override
                                        public void onResponse(Object response) {
                                            if(Objects.equals(response, "User created successfully")) {
                                                startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
                                                finish();
                                            } else if(Objects.equals(response, "Username already exists")){
                                                Toast.makeText(CreateAccountActivity.this, getString(R.string.s_activity_createAccount_errorMessage_usernameExists), Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(CreateAccountActivity.this, getString(R.string.s_activity_createAccount_toastMessage_createUserGeneralError), Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void onError(String message) {
                                            Log.e("CreateAccountActivity", message);
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(CreateAccountActivity.this, getString(R.string.s_activity_createAccount_toastMessage_userDiscarded), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
                                    finish();
                                }
                            })
                            .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Continue creating new user account
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(newUsernameEditText.getText().toString().equals("") || newPasswordEditText.getText().toString().equals("") || newEmailEditText.getText().toString().equals("")) {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.s_activity_createAccount_alertDialog_returnToLoginWithEmptyFields_title))
                    .setMessage(getString(R.string.s_activity_createAccount_alertDialog_returnToLoginWithEmptyFields_message))
                    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(CreateAccountActivity.this, getString(R.string.s_activity_createAccount_toastMessage_userDiscarded), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue creating new user account
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.s_activity_createAccount_alertDialog_returnToLoginWithCompletedFields_title))
                    .setMessage(getString(R.string.s_activity_createAccount_alertDialog_returnToLoginWithCompletedFields_message))
                    .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            HashMap<String, String> data = new HashMap<>();
                            data.put("path", "createUser");
                            data.put("name", newUsernameEditText.getText().toString());
                            data.put("password", newPasswordEditText.getText().toString());
                            data.put("email", newEmailEditText.getText().toString());
                            data.put("type", userTypeSpinner.getSelectedItem().toString());

                            VolleyUtils.volleyCall(getApplicationContext(), data, new VolleyResponseListener() {
                                @Override
                                public void onResponse(Object response) {
                                    if(Objects.equals(response, "User created successfully")) {
                                        startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
                                        finish();
                                    } else if(Objects.equals(response, "Username already exists")){
                                        Toast.makeText(CreateAccountActivity.this, getString(R.string.s_activity_createAccount_errorMessage_usernameExists), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(CreateAccountActivity.this, getString(R.string.s_activity_createAccount_toastMessage_createUserGeneralError), Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onError(String message) {
                                    Log.e("CreateAccountActivity", message);
                                }
                            });
                        }
                    })
                    .setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(CreateAccountActivity.this, getString(R.string.s_activity_createAccount_toastMessage_userDiscarded), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CreateAccountActivity.this, LoginActivity.class));
                            finish();
                        }
                    })
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue creating new user account
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }
}
