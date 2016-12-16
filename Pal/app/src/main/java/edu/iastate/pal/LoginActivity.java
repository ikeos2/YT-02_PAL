package edu.iastate.pal;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import edu.iastate.pal.network.ActiveUser;
import edu.iastate.pal.network.VolleyResponseListener;
import edu.iastate.pal.network.VolleyUtils;

/**
 * ----------
 * File: LoginActivity.java
 * ----------
 * XML:
 *  + activity_login.xml
 * ----------
 * Nav:
 *  +         AboutActivity.java --> View developer information
 *  + CreateAccountActivity.java --> User account creation
 *  +     DashboardActivity.java --> Logged-in user dashboard
 * ----------
 * Func:
 *  + Application entrance point
 *  + User login
 *  + Nav to create new user account
 *  + Facilitate user account cred recovery
 *  + Nav to about developers page
 * ----------
 * Dev:
 *
 * @author Nathan Cool
 * @author Evan Lambert
 *         ----------
 */
public class LoginActivity extends AppCompatActivity {

    /* Interface elements */
    private Button loginButton;
    private ImageView aboutDevelopersImageView;
    private TextInputEditText passwordEditText;
    private TextInputEditText usernameEditText;
    private TextView createAccountTextView;
    private TextView forgotCredentialsTextView;

    /* Variables and miscellaneous objects */
    public static ActiveUser activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();

    }

    @Override
    protected void onResume() {
        super.onResume();

        activeUser = new ActiveUser();

        usernameEditText.setText("");
        passwordEditText.setText("");

        initLoginButtonListener();
        initCreateUserAccountListener();
        initForgotCredentialsListener();
        initAboutDevelopersListener();
    }

    /**
     * Initialize interface elements
     */
    private void initUI() {
        setContentView(R.layout.activity_login);

        usernameEditText = (TextInputEditText) findViewById(R.id.activity_login_editText_username);
        assert usernameEditText != null;
        passwordEditText = (TextInputEditText) findViewById(R.id.activity_login_editText_password);
        assert passwordEditText != null;

        loginButton = (Button) findViewById(R.id.activity_login_button_login);
        assert loginButton != null;

        createAccountTextView = (TextView) findViewById(R.id.activity_login_textView_createAccount);
        assert createAccountTextView != null;
        forgotCredentialsTextView = (TextView) findViewById(R.id.activity_login_textView_forgotCredentials);
        assert forgotCredentialsTextView != null;

        aboutDevelopersImageView = (ImageView) findViewById(R.id.activity_login_button_aboutDevelopers);
        assert aboutDevelopersImageView != null;
    }

    private void initLoginButtonListener() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // TODO: handle other potential invalid credentials cases

                if ((Objects.equals(username, "") || Objects.equals(password, ""))) {
                    toastAndClear(R.string.s_activity_login_errorMessage_emptyCredentialsFields);
                } else {
                    String path = "login";

                    HashMap<String, String> map = new HashMap<>();
                    map.put("path", path);
                    map.put("name", username);
                    map.put("password", password);

                    VolleyUtils.volleyCall(getApplicationContext(), map, new VolleyResponseListener() {
                        @Override
                        public void onResponse(Object response) {
                            handleResult((String) response);
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(LoginActivity.this, R.string.s_volley_errorMessage_server, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    /**
     * User is logged in if Volley successfully authenticates input credentials.
     *
     * @param result login if true, else display error message
     */
    private void handleResult(String result) {
        if (result.length() == 64) {
            // Set up ActiveUser instance for the logged-in user
            activeUser.setToken(result);

            String inputUsername = usernameEditText.getText().toString();
            activeUser.setUsername(inputUsername);

            HashMap<String, String> map = new HashMap<>();
            map.put("path", "user");
            map.put("name", inputUsername);

            VolleyUtils.volleyCall(getApplicationContext(), map, new VolleyResponseListener() {
                @Override
                public void onResponse(Object response) {
                    try {
                        String userType = parseUserType((String) response);
                        activeUser.setUserType(userType);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onError(String message) {
                    Toast.makeText(LoginActivity.this, R.string.s_volley_errorMessage_server, Toast.LENGTH_SHORT).show();
                }
            });

            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
        } else {
            Toast.makeText(this, R.string.s_volley_errorMessage_generic, Toast.LENGTH_SHORT).show();
        }
    }

    private String parseUserType(String response) throws JSONException {
        JSONObject jsonObject = new JSONObject(response);
        String userType = jsonObject.getString("userType");
        return userType;
    }

    private void initCreateUserAccountListener() {
        createAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
            }
        });
    }

    private void initForgotCredentialsListener() {
        forgotCredentialsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: password recovery
                Toast.makeText(LoginActivity.this, "TODO: password recovery", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initAboutDevelopersListener() {
        aboutDevelopersImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, AboutActivity.class));
            }
        });
    }

    /**
     * Close application when the hardware back button is pressed.
     */
    @Override
    public void onBackPressed() {
        startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
        System.exit(0);
    }

    /**
     * Use when catching various cases.
     * Prints a toast with the associated error message ID and clears both credentials fields.
     *
     * @param resID error message string resource ID
     */
    private void toastAndClear(int resID) {
        Toast.makeText(LoginActivity.this, resID, Toast.LENGTH_SHORT).show();
        usernameEditText.setText("");
        passwordEditText.setText("");
    }
}
