package com.example.mumbainosh;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class nearbyplaces extends AsyncTask<Object, String, String> {

    public MapsActivity context;
    private String googlePlaceData;
    private String url;
    private GoogleMap nMap;

    @Override
    protected String doInBackground(Object... objects) {
        nMap = (GoogleMap) objects[0];
        url = (String) objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();

        try {
            googlePlaceData = downloadUrl.ReadTheUrl(url);
        } catch (IOException e) {
            Log.e("nearbyplaces", "Error reading the URL", e);
            return null;  // Return null to indicate failure
        }

        return googlePlaceData;
    }

    @Override
    protected void onPostExecute(String s) {
        if (s == null) {
            Log.e("nearbyplaces", "No data received");
            return;
        }

        List<HashMap<String, String>> nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        try {
            nearbyPlacesList = dataParser.parse(s);
        } catch (JSONException e) {
            Log.e("nearbyplaces", "Error parsing JSON data", e);
        }

        if (nearbyPlacesList != null && !nearbyPlacesList.isEmpty()) {
            Intent intent = new Intent(Context, RestaurantsListActivity.class);
            intent.putExtra("restaurantsList", (ArrayList<HashMap<String, String>>) nearbyPlacesList);
            context.startActivity(intent);
        }
    }

    private void DisplayNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googleNearbyPlaces = nearbyPlacesList.get(i);
            String nameOfPlace = googleNearbyPlaces.get("place_name");
            String vicinity = googleNearbyPlaces.get("vicinity");
            double lat = Double.parseDouble(googleNearbyPlaces.get("lat"));
            double lng = Double.parseDouble(googleNearbyPlaces.get("lng"));

            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(nameOfPlace + " : " + vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            nMap.addMarker(markerOptions);
            boundsBuilder.include(latLng);
        }

        LatLngBounds bounds = boundsBuilder.build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100);
        nMap.animateCamera(cameraUpdate);
    }
}
