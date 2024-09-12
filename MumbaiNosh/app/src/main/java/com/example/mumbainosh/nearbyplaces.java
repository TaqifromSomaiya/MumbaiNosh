package com.example.mumbainosh;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class nearbyplaces extends AsyncTask<Object, String, String> {

    public interface OnTaskCompleted {
        void onTaskCompleted(List<HashMap<String, String>> nearbyPlacesList);
    }

    private String googlePlaceData, url;
    private GoogleMap nMap;
    private OnTaskCompleted callback;

    public nearbyplaces() {
        this.callback = callback;
    }

    @Override
    protected String doInBackground(Object... objects) {
        nMap = (GoogleMap) objects[0];
        url = (String) objects[1];

        DownloadUrl downloadUrl = new DownloadUrl();

        try {
            googlePlaceData = downloadUrl.ReadTheUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlaceData;
    }

    @Override
    protected void onPostExecute(String s)   {
        List<HashMap<String, String>> nearbyPlacesList = null;


            DataParser dataParser = new DataParser();
        try {
            nearbyPlacesList = dataParser.parse(String.valueOf(s));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    private void DisplayNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        if (nMap == null) {
            Log.e("DisplayNearbyPlaces", "GoogleMap instance is null.");
            return; // Exit the method if GoogleMap is not initialized
        }

        if (nearbyPlacesList == null || nearbyPlacesList.isEmpty()) {
            Log.w("DisplayNearbyPlaces", "Nearby places list is empty or null.");
            return;
        }

        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googleNearbyPlaces = nearbyPlacesList.get(i);

            String nameOfPlace = googleNearbyPlaces.get("place_name");
            String vicinity = googleNearbyPlaces.get("vicinity");
            String latString = googleNearbyPlaces.get("lat");
            String lngString = googleNearbyPlaces.get("lng");

            double lat = 0, lng = 0;

            // Check if latString and lngString are not null and not empty
            if (latString != null && !latString.isEmpty() && lngString != null && !lngString.isEmpty()) {
                try {
                    lat = Double.parseDouble(latString);
                    lng = Double.parseDouble(lngString);
                } catch (NumberFormatException e) {
                    Log.e("DisplayNearbyPlaces", "Error parsing coordinates: " + e.getMessage());
                    continue; // Skip this place if coordinates are invalid
                }
            } else {
                Log.e("DisplayNearbyPlaces", "Latitude or Longitude is null or empty.");
                continue; // Skip this place if lat or lng is null or empty
            }

            LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(nameOfPlace + " : " + vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            nMap.addMarker(markerOptions);
            nMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            nMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }
    }


}
