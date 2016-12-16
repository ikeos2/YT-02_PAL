package edu.iastate.pal;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.iastate.pal.network.VolleyResponseListener;
import edu.iastate.pal.network.VolleyUtils;

/**
 * ----------
 * File: CourseLocatorMapActivity.java
 * ----------
 * XML:
 *  + activity_courselocatormap.xml
 * ----------
 * Nav:
 *  + MapDashboardActivity.java --> Back to main maps module screen
 * ----------
 * Func:
 *  + View courses, as markers, on Google Maps
 *  + Tap markers to see more details about a course (info from course manager entry)
 *  + Toggle between four map types
 *  + 'Distance mode': touch markers to begin counting the distance between course buildings
 *  + Clear/reset the map
 * ----------
 * Dev:
 * @author Nathan Cool
 * ----------
 */
public class CourseLocatorMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    /* Interface elements */
    private Button clearMarkersButton;
    private FloatingActionButton mapToggleFab;
    private FloatingActionButton toggleDistanceModeFab;
    private GoogleMap map;
    private TextView distanceView;
    private TextView mapModeView;
    private Toolbar actionBar;

    /* Variables and miscellaneous objects */
    private ArrayList<String> buildingNames;
    private ArrayList<String> courseNames;
    private ArrayList<Marker> distanceSet;
    private ArrayList<Marker> markers;
    private ArrayList<String> roomNumbers;
    private ArrayList<String> times;

    private boolean distanceModeOn = false;

    private double totalDistance = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initUI();
        initMapTypeFabListener();
        initToggleDistanceModeFabListener();
        initClearMarkersButtonOnClickListener();
        populateCourseNames();
    }

    /**
     * Initialize interface elements
     */
    private void initUI() {
        setContentView(R.layout.activity_courselocatormap);

        actionBar = (Toolbar) findViewById(R.id.activity_courseLocatorMap_actionbar);
        setSupportActionBar(actionBar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Course Locator");

        mapToggleFab = (FloatingActionButton) findViewById(R.id.activity_courseLocatorMap_fab_toggleMapType);
        assert mapToggleFab != null;

        toggleDistanceModeFab = (FloatingActionButton) findViewById(R.id.activity_courseLocatorMap_fab_toggleDistanceMode);
        assert toggleDistanceModeFab != null;

        distanceView = (TextView) findViewById(R.id.activity_courseLocatorMap_TextView_markerDistanceView);
        assert distanceView != null;

        mapModeView = (TextView) findViewById(R.id.activity_courseLocatorMap_TextView_mapMode);
        assert mapModeView != null;
        mapModeView.setText("Mode: Info");

        clearMarkersButton = (Button) findViewById(R.id.activity_courseLocatorMap_Button_clearMarkers);
        assert clearMarkersButton != null;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        setupMap();
    }

    private void setupMap() {
        markers = new ArrayList<>();
        distanceSet = new ArrayList<>();

        for(int i = 0; i < buildingNames.size(); i++) {
            LatLng temp = getLocationFromAddress(this, buildingNames.get(i));
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(temp)
                    .title(buildingNames.get(i) + ", Rm " + roomNumbers.get(i))
                    .snippet(courseNames.get(i) + ", " + times.get(i)));
            markers.add(marker);
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for(int i = 0; i < markers.size(); i++) {
            builder.include(markers.get(i).getPosition());
        }

        if(markers.size() != 0) {
            LatLngBounds bounds = builder.build();

            int padding = 100;
            CameraUpdate moveMapUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);

            map.animateCamera(moveMapUpdate);
        }

        initOnMarkerClickListener();
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder= new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName("Ames, IA " + strAddress, 5);
            if(address == null) {
                return null;
            }

            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return p1;
    }

    private void populateCourseNames() {
        buildingNames = new ArrayList<>();
        roomNumbers = new ArrayList<>();
        courseNames = new ArrayList<>();
        times = new ArrayList<>();

        HashMap<String, String> map = new HashMap<>();
        map.put("path", "getCourses");
        map.put("owner", LoginActivity.activeUser.getUsername());
        map.put("token", LoginActivity.activeUser.getToken());

        VolleyUtils.volleyCall(getApplicationContext(), map, new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                handleResult((String) response);
            }

            @Override
            public void onError(String message) {
                Toast.makeText(CourseLocatorMapActivity.this, "Error reaching server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleResult(String result) {
        try {
            JSONArray data = new JSONArray(result);
            for(int i= 0; i < data.length(); i++) {
                JSONObject temp = data.getJSONObject(i);
                buildingNames.add(temp.getString("location"));
                roomNumbers.add(temp.getString("room"));
                courseNames.add(temp.getString("courseName"));
                times.add(temp.getString("startTime") + " - " + temp.getString("endTime"));

            }

            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(CourseLocatorMapActivity.this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handler for toggling between various Google Map types
     */
    private void initMapTypeFabListener() {
        mapToggleFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (map.getMapType()) {
                    case GoogleMap.MAP_TYPE_NORMAL:
                        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        break;
                    case GoogleMap.MAP_TYPE_HYBRID:
                        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        break;
                    case GoogleMap.MAP_TYPE_SATELLITE:
                        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        break;
                    case GoogleMap.MAP_TYPE_TERRAIN:
                        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                    default:
                        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        break;
                }
            }
        });
    }

    private void initOnMarkerClickListener() {
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(!distanceModeOn) {
                    marker.showInfoWindow();
                } else {
                    distanceSet.add(marker);
                    marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    updateDistanceView(marker);
                }

                return true;
            }
        });
    }

    private void initClearMarkersButtonOnClickListener() {
        clearMarkersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext())
                        .setTitle("Reset Map")
                        .setMessage("Your marker route will be cleared. Continue?")
                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(CourseLocatorMapActivity.this, "Resetting map...", Toast.LENGTH_SHORT).show();
                                clearDistanceView();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    private void updateDistanceView(Marker marker) {
        if(distanceSet.size() <= 1) {
            totalDistance = 0.0;
            distanceView.setText("Distance: " + distanceInMiles() + " (miles)");
        } else {
            float[] results = new float[2];

            PolylineOptions line = new PolylineOptions().add(distanceSet.get(distanceSet.size() - 2).getPosition(), distanceSet.get(distanceSet.size() - 1).getPosition()).width(15).color(Color.GREEN);

            map.addPolyline(line);

            Location.distanceBetween(distanceSet.get(distanceSet.size() - 2).getPosition().latitude,
                    distanceSet.get(distanceSet.size() - 2).getPosition().longitude,
                    distanceSet.get(distanceSet.size() - 1).getPosition().latitude,
                    distanceSet.get(distanceSet.size() - 1).getPosition().longitude, results);

            totalDistance += results[0];
            distanceView.setText(String.format("Distance: %.3f (miles)", distanceInMiles()));
        }
    }

    private void clearDistanceView() {
        distanceSet.clear();
        distanceView.setText("Distance: ");

        for(int i = 0; i < markers.size(); i++) {
            markers.get(i).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }

        map.clear();
        setupMap();
    }

    private double distanceInMiles() {
        return ((this.totalDistance * 3.28084) / 5280);
    }

    private void initToggleDistanceModeFabListener() {
        toggleDistanceModeFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!distanceModeOn) {
                    distanceModeOn = true;
                    toggleDistanceModeFab.setBackgroundTintList(ColorStateList.valueOf(0xff00ff00));
//                    Toast.makeText(CourseLocatorMapActivity.this, "Distance Mode is ON.", Toast.LENGTH_SHORT).show();
                    mapModeView.setText("Mode: Distance");
                } else {
                    distanceModeOn = false;
                    toggleDistanceModeFab.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.colorAccent)));
//                    Toast.makeText(CourseLocatorMapActivity.this, "Distance Mode is OFF.", Toast.LENGTH_SHORT).show();
                    mapModeView.setText("Mode: Info");
                }
            }
        });
    }
}
