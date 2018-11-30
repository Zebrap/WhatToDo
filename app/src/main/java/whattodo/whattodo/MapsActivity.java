package whattodo.whattodo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.services.android.navigation.ui.v5.NavigationView;
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions;
import com.mapbox.services.android.navigation.ui.v5.OnNavigationReadyCallback;
import com.mapbox.services.android.navigation.ui.v5.instruction.InstructionView;
import com.mapbox.services.android.navigation.ui.v5.listeners.NavigationListener;

import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnNavigationReadyCallback,
        NavigationListener, LocationEngineListener, PermissionsListener {

    private Point origin = Point.fromLngLat( 18.618783,54.372496);
    private Point destination = Point.fromLngLat( 18.616432,54.380217);

    //  private Point destination = Point.fromLngLat(18.619377,54.372016);

    private NavigationView navigationView;
    private InstructionView instructionView;
    private PermissionsManager permissionsManager;
    private Location originLocation;
    private LocationEngine locationEngine;
    double Long, Lat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_AppCompat_NoActionBar);
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,
                "pk.eyJ1IjoiemVicmFwdyIsImEiOiJjam9kNTZkMzkwc3RiM3ZvMTNlNHIya2s2In0.DyQaOsbV8AL0kmYR4TF5KQ");
        setContentView(R.layout.activity_maps);

        enableLocation();
        origin = Point.fromLngLat(originLocation.getLongitude(),originLocation.getLatitude());

        Intent intent = getIntent();
        Long = Double.parseDouble(intent.getStringExtra("Longitude"));
        Lat = Double.parseDouble(intent.getStringExtra("Latitude"));

        instructionView = findViewById(R.id.instructionView);

        navigationView = findViewById(R.id.navigationView);

        navigationView.onCreate(savedInstanceState);
        navigationView.initialize(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        navigationView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        navigationView.onResume();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        navigationView.onLowMemory();
    }

    @Override
    public void onBackPressed() {
        // If the navigation view didn't need to do anything, call super
        if (!navigationView.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        navigationView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        navigationView.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
        navigationView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        navigationView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        navigationView.onDestroy();
    }

    @Override
    public void onNavigationReady() {
        navigationView.findViewById(R.id.feedbackFab).setVisibility(View.GONE);
        //   navigationView.findViewById(R.id.soundFab).setVisibility(View.GONE);
        navigationView.findViewById(R.id.instructionLayout).setEnabled(false);

        destination = Point.fromLngLat(Long,Lat);

        NavigationViewOptions options = NavigationViewOptions.builder()
                .origin(origin)
                .destination(destination)
                .shouldSimulateRoute(false)
                .navigationListener(this)
                .build();

        navigationView.startNavigation(options);
    }


    @Override
    public void onCancelNavigation() {
        navigationView.onDestroy();
   //     Toast.makeText(MapsActivity.this, "long: "+originLocation.getLongitude()+" lat:"+originLocation.getLatitude(), Toast.LENGTH_SHORT).show();
   //     Log.d("loc","long: "+originLocation.getLongitude()+" lat:"+originLocation.getLatitude());
        Intent intent = new Intent(MapsActivity.this,Main2Activity.class);
        MapsActivity.this.startActivity(intent);
    }

    @Override
    public void onNavigationFinished() {
    }

    @Override
    public void onNavigationRunning() {

    }

    @SuppressWarnings("MissingPermission")
    private void enableLocation(){
        if(permissionsManager.areLocationPermissionsGranted(this)){
            initializedLocationEngine();
        }else{
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    @SuppressWarnings("MissingPermission")
    private void initializedLocationEngine(){
        locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        Location lastLocation = locationEngine.getLastLocation();
        if(lastLocation!=null){
            originLocation = lastLocation;
        }else{
            locationEngine.addLocationEngineListener(this);
        }
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onConnected() {
        locationEngine.requestLocationUpdates();

    }

    @Override
    public void onLocationChanged(Location location) {
        if(location!=null){
            originLocation = location;
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if(granted){
            enableLocation();
        }
    }

}




