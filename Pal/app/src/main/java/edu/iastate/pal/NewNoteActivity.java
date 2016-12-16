package edu.iastate.pal;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

import edu.iastate.pal.network.VolleyResponseListener;
import edu.iastate.pal.network.VolleyUtils;

/**
 * ----------
 * File: NewNoteActivity.java
 * ----------
 * Assets:
 *  + activity_newnote.xml
 * ----------
 * Nav:
 *  + NotesDashboardActivity.java --> Return to module dashboard once note is created/updated
 * ----------
 * Func:
 *  + Compose a new note (add title and content)
 *  + Update the title and/or content of an existing note
 *  + Save new or existing note to the database
 *  + Return to notes list
 * ----------
 * Dev:
 * @author Nathan Cool
 * ----------
 */
public class NewNoteActivity extends AppCompatActivity {

    /* Interface elements */
    private Button boldButton;
    private Button italicsButton;
    private Button underlineButton;
    private EditText contentField;
    private EditText titleField;
    private Toolbar actionBar;

    /* Variables and miscellaneous objects */
    private String isExistingNote;
    private String noteContentEdited;
    private String noteContentOriginal;
    private String noteID;
    private String noteNameEdited;
    private String noteNameOriginal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();

        initNoteData();
        initFormattingButtonListeners();
    }

    /**
     * Initialize data for the note selected in NotesDashboardActivity.java
     */
    private void initNoteData() {
        Intent receivedIntent = getIntent();
        noteNameOriginal = (String) receivedIntent.getSerializableExtra("noteName");
        isExistingNote = getIntent().getStringExtra("isExistingNote");

        if(Objects.equals(isExistingNote, "false")) {
            noteNameOriginal = "";
            noteContentOriginal = "";
            titleField.setText(noteNameOriginal);
            contentField.setText(noteContentOriginal);
            noteNameEdited = "";
            noteContentEdited = "";
        } else {
            HashMap<String, String> map = new HashMap<>();
            map.put("path", "getNote");
            map.put("title", noteNameOriginal);
            map.put("token", LoginActivity.activeUser.getToken());

            VolleyUtils.volleyCall(getApplicationContext(), map, new VolleyResponseListener() {
                @Override
                public void onResponse(Object response) {
                    try {
                        JSONObject data = new JSONObject((String) response);
                        noteContentOriginal = data.getString("data");
                        noteID = data.getString("uid");
                        titleField.setText(noteNameOriginal);
                        contentField.setText(noteContentOriginal);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(NewNoteActivity.this, "Error reaching server", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * Initialize interface elements
     */
    private void initUI() {
        setContentView(R.layout.activity_newnote);

        actionBar = (Toolbar) findViewById(R.id.activity_newNote_actionbar);
        setSupportActionBar(actionBar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        boldButton = (Button) findViewById(R.id.activity_newNote_button_bold);
        assert boldButton != null;

        italicsButton = (Button) findViewById(R.id.activity_newNote_button_italics);
        assert italicsButton != null;

        underlineButton = (Button) findViewById(R.id.activity_newNote_button_underline);
        assert underlineButton != null;

        titleField = (EditText) findViewById(R.id.activity_newNote_editText_title);
        assert titleField != null;

        contentField = (EditText) findViewById(R.id.activity_newNote_editText_content);
        assert contentField != null;
    }

    private void initFormattingButtonListeners() {
        boldButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewNoteActivity.this, "TODO: add bold function.", Toast.LENGTH_SHORT).show();
            }
        });

        italicsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewNoteActivity.this, "TODO: add italics function.", Toast.LENGTH_SHORT).show();
            }
        });

        underlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewNoteActivity.this, "TODO: add underline function.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Prompt user as to whether content should be saved or deleted before returning.
    // Use an AlertDialogBox.
    // The save functionality will eventually be extended to a Save button, storing content.
    // while maintaining its visibility on the screen.
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Proceed?")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        closeNote();
                        finish();
                    }
                })
                .setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(NewNoteActivity.this, NotesDashboardActivity.class));
                        finish();
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Resume working on note
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                new AlertDialog.Builder(this)
                        .setMessage("Proceed?")
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                closeNote();
                                finish();
                            }
                        })
                        .setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(NewNoteActivity.this, NotesDashboardActivity.class));
                                finish();
                            }
                        })
                        .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Resume working on note
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
        }

        return true;
    }

    private void closeNote() {
        noteNameEdited = titleField.getText().toString();
        noteContentEdited = contentField.getText().toString();

        if(Objects.equals(isExistingNote, "false")) {
            // add new note based
            HashMap<String, String> map = new HashMap<>();
            map.put("path", "addNote");
            map.put("owner", LoginActivity.activeUser.getUsername());
            map.put("token", LoginActivity.activeUser.getToken());
            map.put("title", noteNameEdited);
            map.put("data", noteContentEdited);

            VolleyUtils.volleyCall(getApplicationContext(), map, new VolleyResponseListener() {
                @Override
                public void onResponse(Object response) {
                    if(Objects.equals(response, "true")) {
                        Intent notesDashboardActivityReturnIntent = new Intent(NewNoteActivity.this, NotesDashboardActivity.class);
                        startActivity(notesDashboardActivityReturnIntent);
//                        finish();
                    } else {
                        Toast.makeText(NewNoteActivity.this, "Error saving new note.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(String message) {
                    Toast.makeText(NewNoteActivity.this, "Error reaching server", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            if((Objects.equals(noteNameOriginal, noteNameEdited)) && (Objects.equals(noteContentOriginal, noteContentEdited))) {
                // title same, content same
                startActivity(new Intent(NewNoteActivity.this, NotesDashboardActivity.class));
                finish();
            } else if((!Objects.equals(noteNameOriginal, noteNameEdited)) && (Objects.equals(noteContentOriginal, noteContentEdited))) {
                // title changed, content same
                HashMap<String, String> map = new HashMap<>();
                map.put("path", "updateTitle");
                map.put("owner", LoginActivity.activeUser.getUsername());
                map.put("token", LoginActivity.activeUser.getToken());
                map.put("title", noteNameEdited);
                map.put("data", noteContentEdited);

                VolleyUtils.volleyCall(getApplicationContext(), map, new VolleyResponseListener() {
                    @Override
                    public void onResponse(Object response) {
                        if(Objects.equals(response, "true")) {
                            startActivity(new Intent(NewNoteActivity.this, NotesDashboardActivity.class));
                        } else {
                            Toast.makeText(NewNoteActivity.this, "Error saving existing note.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(NewNoteActivity.this, "Error reaching server", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if((Objects.equals(noteNameOriginal, noteNameEdited)) && (!Objects.equals(noteContentOriginal, noteContentEdited))) {
                // title same, content changed
                HashMap<String, String> map = new HashMap<>();
                map.put("path", "updateNote");
                map.put("owner", LoginActivity.activeUser.getUsername());
                map.put("token", LoginActivity.activeUser.getToken());
                map.put("title", noteNameEdited);
                map.put("data", noteContentEdited);

                VolleyUtils.volleyCall(getApplicationContext(), map, new VolleyResponseListener() {
                    @Override
                    public void onResponse(Object response) {
                        if(Objects.equals(response, "true")) {
                            startActivity(new Intent(NewNoteActivity.this, NotesDashboardActivity.class));
                        } else {
                            Toast.makeText(NewNoteActivity.this, "Error saving existing note.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(NewNoteActivity.this, "Error reaching server", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // title changed, content changed
                HashMap<String, String> map = new HashMap<>();
                map.put("path", "updateDataTitle");
                map.put("owner", LoginActivity.activeUser.getUsername());
                map.put("token", LoginActivity.activeUser.getToken());
                map.put("title", noteNameEdited);
                map.put("data", noteContentEdited);
                map.put("id", String.valueOf(noteID));

                VolleyUtils.volleyCall(getApplicationContext(), map, new VolleyResponseListener() {
                    @Override
                    public void onResponse(Object response) {
                        if(Objects.equals(response, "true")) {
                            startActivity(new Intent(NewNoteActivity.this, NotesDashboardActivity.class));
                        } else {
                            Toast.makeText(NewNoteActivity.this, "Error saving existing note.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(NewNoteActivity.this, "Error reaching server", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }
}