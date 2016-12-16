package edu.iastate.pal;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;

import edu.iastate.pal.network.VolleyResponseListener;
import edu.iastate.pal.network.VolleyUtils;

/**
 * ----------
 * File: NotesDashboardActivity.java
 * ----------
 * XML:
 *  + activity_notesdashboard.xml
 * ----------
 * Nav:
 *  +   NewNoteActivity.java --> Create a new note OR update an existing note
 *  + DashboardActivity.java --> Back to main user dashboard
 * ----------
 * Func:
 *  + Displays in a ListView all notes associated with the logged-in user.
 *  + Add a new note by pressing the '+' FloatingActionButton
 *  + Open, update, and save an existing note by selecting a ListView item
 *  + Delete a note by long-pressing its ListView item and accepting a warning prompt
 *  + Sort notes by A-Z or Z-A via the associated ActionBar button
 * ----------
 * Dev:
 * @author Nathan Cool
 * ----------
 */
public class NotesDashboardActivity extends AppCompatActivity {

    /* Interface elements */
    private FloatingActionButton newNoteFab;
    private FloatingActionButton newDrawingNoteFab;
    private ListView listView;
    private Toolbar actionBar;

    /* Variables and miscellaneous objects */
    private ArrayAdapter<String> adapter;
    private ArrayList<String> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();

        notes = new ArrayList<>();
        initNotes();

        /* Connect content ArrayAdapter to the interface element and a listener */
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(adapter);

        initNoteListViewClickListener();
        initNoteListViewLongClickListener();
        initNewDrawingNoteFabListener();
        initNewNoteFabListener();
    }

    /**
     * Initialize user interface elements for this Activity's View.
     */
    private void initUI() {
        setContentView(R.layout.activity_notesdashboard);

        actionBar = (Toolbar) findViewById(R.id.activity_notesDashboard_actionbar);
        setSupportActionBar(actionBar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.s_global_module_name_notes));

        newDrawingNoteFab = (FloatingActionButton) findViewById(R.id.activity_notesDashboard_fab_newDrawingNote);
        assert newDrawingNoteFab != null;

        newNoteFab = (FloatingActionButton) findViewById(R.id.activity_notesDashboard_fab_newNote);
        assert newNoteFab != null;

        listView = (ListView) findViewById(R.id.activity_notesDashboard_listView_notes);
        assert listView != null;
    }

    private void initNotes() {
        HashMap<String, String> map = new HashMap<>();
        map.put("path", "getNotes");
        map.put("user", LoginActivity.activeUser.getUsername());
        map.put("token", LoginActivity.activeUser.getToken());

        VolleyUtils.volleyCall(getApplicationContext(), map, new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                handleResult((String) response);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(NotesDashboardActivity.this, "Error reaching server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleResult(String result) {
        try {
            JSONArray data = new JSONArray(result);
            for(int i= 0; i < data.length(); i++) {
                JSONObject temp = data.getJSONObject(i);
                notes.add(temp.getString("title"));
            }

            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_notesdashboard_overflowmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.home:
                startActivity(new Intent(NotesDashboardActivity.this, DashboardActivity.class));
                finish();
            case R.id.activity_notes_dashboard_overflow_sort_AZ:
                Collections.sort(notes);
                Toast.makeText(this, "Notes sorted A-Z", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                return true;
            case R.id.activity_notes_dashboard_overflow_sort_ZA:
                Collections.sort(notes, Collections.reverseOrder());
                Toast.makeText(this, "Notes sorted Z-A", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                return true;
            case R.id.activity_notes_dashboard_overflow_sync:
                notes.clear();
                initNotes();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(NotesDashboardActivity.this, DashboardActivity.class));
        finish();
    }

    private void initNoteListViewClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String noteName = (String) listView.getItemAtPosition(position);
                String isExistingNote = "true";
                startActivity(new Intent(NotesDashboardActivity.this, NewNoteActivity.class).putExtra("noteName", noteName)
                                                                                            .putExtra("isExistingNote", isExistingNote));
            }
        });
    }

    private void initNoteListViewLongClickListener() {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle("Proceed?")
                        .setMessage("This note will be deleted.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String noteName = (String) listView.getItemAtPosition(position);

                                HashMap<String, String> map = new HashMap<>();
                                map.put("path", "deleteNote");
                                map.put("title", noteName);
                                map.put("token", LoginActivity.activeUser.getToken());

                                VolleyUtils.volleyCall(getApplicationContext(), map, new VolleyResponseListener() {
                                    @Override
                                    public void onResponse(Object response) {
                                        if(Objects.equals(response, "true")) {
                                            Toast.makeText(NotesDashboardActivity.this, "Note deleted.", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(NotesDashboardActivity.this, "Error deleting note.", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onError(String message) {
                                        Toast.makeText(NotesDashboardActivity.this, "Error reaching server.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                notes.remove(notes.indexOf(noteName));
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Cancel note deletion
                            }
                        })
                        .setIcon(android.R.drawable.ic_menu_delete)
                        .show();
                return true;
            }
        });
    }

    private void initNewNoteFabListener() {
        newNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String isExistingNote = "false";
                startActivity(new Intent(NotesDashboardActivity.this, NewNoteActivity.class).putExtra("isExistingNote", isExistingNote));
            }
        });
    }

    private void initNewDrawingNoteFabListener() {
        newDrawingNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotesDashboardActivity.this, NewCanvasNoteActivity.class));
            }
        });
    }
}
