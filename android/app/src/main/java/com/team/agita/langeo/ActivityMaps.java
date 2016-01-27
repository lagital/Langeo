package com.team.agita.langeo;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.location.Location;

import com.androidmapsextensions.OnMapReadyCallback;
import com.google.android.gms.location.LocationListener;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.androidmapsextensions.GoogleMap;
import com.androidmapsextensions.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.androidmapsextensions.Marker;
import com.androidmapsextensions.MarkerOptions;
import com.team.agita.langeo.achievements.ActivityAchievements;
import com.team.agita.langeo.contacts.ActivityContacts;

import java.text.DateFormat;
import java.util.Date;

public class ActivityMaps extends AppCompatActivity implements
        GoogleMap.OnInfoWindowClickListener,
        LocationListener, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    protected static final String TAG = "location-updates-sample";
    private static final String ACHIEVEMENTS_START = "com.team.agita.langeo.RUN_ACHIEVEMENTS";
    private static final String CONTACTS_START = "com.team.agita.langeo.RUN_CONTACTS";
    private static final String PROFILE_START = "com.team.agita.langeo.RUN_PROFILE";
    private static final String SIGNIN_START = "com.team.agita.langeo.RUN_SIGNIN";

    //The desired interval for location updates. Inexact. Updates may be more or less frequent.
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    //Map camera parameters
    public static int CAMERA_BEARING = 90;
    public static int CAMERA_TILT = 40;
    public static int CAMERA_ZOOM = 16;


    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";

    //Provides the entry point to Google Play services.
    protected GoogleApiClient mGoogleApiClient;


    //Stores parameters for requests to the FusedLocationProviderApi.
    protected LocationRequest mLocationRequest;

    protected SupportMapFragment mMapFragment;
    protected Marker mMarkerMe;
    protected GoogleMap mMap;

    //Represents a geographical location.
    protected Location mCurrentLocation;

    // UI Widgets.
    protected FloatingActionButton mFAB;

    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    protected Boolean mRequestingLocationUpdates;

    //Time when the location was updated represented as a String.
    protected String mLastUpdateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();

        mFAB = (FloatingActionButton) this.findViewById(R.id.fab);
        mFAB.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor
                (ActivityMaps.this, R.color.fab_disabled)));
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mRequestingLocationUpdates) {
                    startLocationUpdates();
                    mRequestingLocationUpdates = true;
                    mFAB.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor
                            (ActivityMaps.this, R.color.fab_enabled)));
                } else {
                    stopLocationUpdates();
                    mRequestingLocationUpdates = false;
                    mFAB.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor
                            (ActivityMaps.this, R.color.fab_disabled)));
                }
            }
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        mMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mMapFragment.getExtendedMapAsync(this);

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        // Kick off the process of building a GoogleApiClient and requesting the LocationServices
        // API.
        buildGoogleApiClient();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        // Add a marker on current location, and move the camera
        mMap = map;
        mMap.setOnInfoWindowClickListener(this);
        mMarkerMe = map.addMarker(new MarkerOptions().position(new LatLng(0.0, 0.0))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .visible(true));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        //TODO: normal action on click
        Toast.makeText(this, "Info window clicked",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar, menu);
        return true;
    }

    //Updates fields based on data stored in the bundle.
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        Log.i(TAG, "Updating values from bundle");
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
                setButtonsEnabledState();
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                // Since LOCATION_KEY was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                mLastUpdateTime = savedInstanceState.getString(LAST_UPDATED_TIME_STRING_KEY);
            }
            updateUI(true);
        }
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    protected synchronized void buildGoogleApiClient() {
        Log.i(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        createLocationRequest();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    //Requests location updates from the FusedLocationApi.
    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "startLocationUpdates");
    }

    /**
     * Ensures that only one button is enabled at any time. The Start Updates button is enabled
     * if the user is not requesting location updates. The Stop Updates button is enabled if the
     * user is requesting location updates.
     */
    private void setButtonsEnabledState() {
        if (mRequestingLocationUpdates) {
            mFAB.setEnabled(false);
        } else {
            mFAB.setEnabled(true);
        }
    }

    //Updates the latitude, the longitude, and the last location time in the UI.
    private void updateUI(Boolean initial) {
        if (initial) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()), 13));
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(mCurrentLocation.getLatitude(),
                            mCurrentLocation.getLongitude())) // Sets the center of the map to location user
                    .zoom(CAMERA_ZOOM)          // Sets the zoom
                    .bearing(CAMERA_BEARING)    // Sets the orientation of the camera to east
                    .tilt(CAMERA_TILT)          // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

        mMarkerMe.setPosition(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
        Log.d(TAG, "updateUI");
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        Log.d(TAG, "stopLocationUpdates");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.

        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            mFAB.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor
                    (ActivityMaps.this, R.color.fab_enabled)));
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();

        super.onStop();
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");

        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
            updateUI(true);
        }
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    //Callback that fires when the location changes.
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI(false);
        Log.d(TAG, "Location Changed!");
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    //Stores activity data in the Bundle.
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdateTime);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_achievements:
                // Go to achievements.
                Intent aintent = new Intent(this, ActivityAchievements.class);
                aintent.putExtra(ACHIEVEMENTS_START, true);
                startActivity(aintent);
                return true;
            case R.id.action_contacts:
                // Go to contacts.
                Intent cintent = new Intent(this, ActivityContacts.class);
                cintent.putExtra(CONTACTS_START, true);
                startActivity(cintent);
                return true;
            case R.id.action_profile:
                // Go to profile.
                Intent pintent = new Intent(this, ActivityProfile.class);
                pintent.putExtra(PROFILE_START, true);
                startActivity(pintent);
                return true;
            case R.id.action_signout:
                // Go to signin.
                Intent sintent = new Intent(this, ActivitySignin.class);
                sintent.putExtra(SIGNIN_START, true);
                startActivity(sintent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}