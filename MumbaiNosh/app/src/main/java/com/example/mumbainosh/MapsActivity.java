package com.example.mumbainosh;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import com.example.mumbainosh.R;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.server.response.FastJsonResponse;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.example.mumbainosh.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private GoogleApiClient googleApiClient;
    public LocationRequest locationRequest;
    private  Location lastLocation;
    private Marker currentUserLocationMarker;
    private  static final int Request_User_Location_Code = 99;
    private  double latitude,longitude;
    private int proximtiyRadius = 10000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_list);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (Build.VERSION.SDK_INT >- Build.VERSION_CODES.M)
        {
            CheckUserLocationPermission();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    public void onClick(View v) {
        String restro = "restaurant";
        Object[] transferData = new Object[2];
        nearbyplaces getNearbyPlaces = new nearbyplaces();

        if (v.getId() == R.id.restro) {
            if (mMap != null) {
                mMap.clear();
                String url = getUrl(latitude, longitude, restro);
                transferData[0] = mMap;
                transferData[1] = url;

                getNearbyPlaces.context = this;  // Pass the current context
                try {
                    getNearbyPlaces.execute(transferData);

                    // Start the RestaurantsListActivity after the data is fetched
                    Intent intent = new Intent(this, RestaurantsListActivity.class);

                    // Pass the list of restaurants to the new activity
                    intent.putExtra("url", url);
                    startActivity(intent);

                    Toast.makeText(this, "Searching and showing nearby restaurants", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, "Error fetching nearby restaurants", Toast.LENGTH_SHORT).show();
                    Log.e("MapsActivity", "Error executing nearby places task", e);
                }
            } else {
                Toast.makeText(this, "Map not ready", Toast.LENGTH_SHORT).show();
                Log.e("MapsActivity", "Google Map is null");
            }
        }
    }


    private String getUrl(double latitude, double longitude, String restroType) {
        StringBuilder googleUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googleUrl.append("location=").append(latitude).append(",").append(longitude);
        googleUrl.append("&radius=").append(proximtiyRadius);
        googleUrl.append("&type=").append(restroType);
        googleUrl.append("&key=").append("AIzaSyBNfu3e1CliUKM6PtIHF0Nl_Q3lQleAqcM");  // Use BuildConfig or another secure method for API key

        Log.d("MapsActivity", "URL: " + googleUrl.toString());
        return googleUrl.toString();
    }





    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }


    }

    private void CheckUserLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_User_Location_Code);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Request_User_Location_Code) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // Initialize location updates
                    buildGoogleApiClient();
                    mMap.setMyLocationEnabled(true);
                }
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
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
         public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();

             if (location != null) {
                 lastLocation = location;

                 // Log location coordinates
                 Log.d("Location", "Latitude: " + location.getLatitude() + " Longitude: " + location.getLongitude());

                 if (currentUserLocationMarker != null) {
                     currentUserLocationMarker.remove();
                 }

                 LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                 MarkerOptions markerOptions = new MarkerOptions();
                 markerOptions.position(latLng);
                 markerOptions.title("User Current Location");
                 markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

                 currentUserLocationMarker = mMap.addMarker(markerOptions);

                 mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                 mMap.animateCamera(CameraUpdateFactory.zoomTo(12)); // Use zoomTo for a fixed zoom level

                 if (googleApiClient != null) {
                     LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this::onLocationChanged);
                 }
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
     }