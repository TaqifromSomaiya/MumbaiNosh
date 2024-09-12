package com.example.mumbainosh;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.mumbainosh.databinding.ActivityMapsBinding;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        nearbyplaces.OnTaskCompleted {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private GoogleApiClient googleApiClient;
    public LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private static final int Request_User_Location_Code = 99;
    private double latitude, longitude;
    private int proximityRadius = 10000;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (CheckUserLocationPermission()) {
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);
            }
        } else {
            // Directly load the map for older versions
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId()== R.id.nav_avail_food)
            {
                startActivity(new Intent(this, AavailFoodList.class));
            } else if (item.getItemId() == R.id.nav_first_screen) {
                startActivity(new Intent(this, FirstScreen.class));

            } else if (item.getItemId() == R.id.nav_about) {
                startActivity(new Intent(this, About.class));
            }

            return false;
        });
        bottomNavigationView.setSelectedItemId(0);
    }

    private String getUrl(double latitude, double longitude, String NearbyPlace) {
        StringBuilder googleUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleUrl.append("location=").append(latitude).append(",").append(longitude);
        googleUrl.append("&radius=").append(proximityRadius);
        googleUrl.append("&type=").append(NearbyPlace);
        googleUrl.append("&sensor=true");
        googleUrl.append("&key=").append("AIzaSyAiMNBhbcu4WBJNZcWf7uf4XFSCzJCEuYc");

        Log.d("MapsActivity", "url = " + googleUrl.toString());

        return googleUrl.toString();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (CheckUserLocationPermission()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
    }

    public boolean CheckUserLocationPermission() {
        // Check if permission is not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Show rationale dialog if needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app requires location permission to show your position on the map.")
                        .setPositiveButton("OK", (dialog, which) -> {
                            // Request permission
                            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .create()
                        .show();
            } else {
                // Request permission directly
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
            }
            return false;
        } else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Request_User_Location_Code) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with enabling location
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    protected synchronized void buildGoogleApiClient()
    {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }
         @Override
         public void onLocationChanged(Location location)
         {
             latitude = location.getLatitude();
             longitude = location.getLongitude();

             lastLocation = location;
             if (currentUserLocationMarker != null)
             {
                 currentUserLocationMarker.remove();
             }
             LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

             MarkerOptions markerOptions = new MarkerOptions();
             markerOptions.position(latLng);
             markerOptions.title("User current location");
             markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

             currentUserLocationMarker = mMap.addMarker(markerOptions);

             mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
             mMap.animateCamera(CameraUpdateFactory.zoomBy(12));

             if (googleApiClient !=null)
             {
                 LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this::onLocationChanged);
             }

         }



    @Override
         public void onConnected(@Nullable Bundle bundle) {

         locationRequest = new LocationRequest();
         locationRequest.setInterval(1100);
         locationRequest.setFastestInterval(1100);
         locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
         if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
         {
             LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this::onLocationChanged);
         }

         }

         @Override
         public void onConnectionSuspended(int i) {

         }

         @Override
         public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

         }

    @Override
    public void onTaskCompleted(List<HashMap<String, String>> nearbyPlacesList) {

    }
}