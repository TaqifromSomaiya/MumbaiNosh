package com.example.mumbainosh;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataParser {

    private HashMap<String, String> getPlaces(JSONObject googlePacesJSON) {
        HashMap<String, String> googlePlacesMap = new HashMap<>();
        String NameOfPlaces = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";

        try {
            if (!googlePacesJSON.isNull("name")) {
                NameOfPlaces = googlePacesJSON.getString("name");
            }
            if (!googlePacesJSON.isNull("vicinity")) {
                vicinity = googlePacesJSON.getString("vicinity");
            }
            if (!googlePacesJSON.isNull("geometry")) {
                JSONObject geometry = googlePacesJSON.getJSONObject("geometry");
                if (!geometry.isNull("location")) {
                    JSONObject location = geometry.getJSONObject("location");
                    latitude = location.getString("lat");
                    longitude = location.getString("lng");
                }
            }
            if (!googlePacesJSON.isNull("reference")) {
                reference = googlePacesJSON.getString("reference");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        googlePlacesMap.put("place_name", NameOfPlaces);
        googlePlacesMap.put("vicinity", vicinity);
        googlePlacesMap.put("lat", latitude);
        googlePlacesMap.put("lng", longitude);
        googlePlacesMap.put("reference", reference);

        return googlePlacesMap;
    }

    private List<HashMap<String, String>> getAllNearbyPlaces(JSONArray jsonArray) throws JSONException {
        int counter = jsonArray.length();
        List<HashMap<String, String>> NearbyPlacesList = new ArrayList<>();
        HashMap<String, String> NearbyPlacesMap;

        for (int i = 0; i < counter; i++) {
            NearbyPlacesMap = getPlaces(jsonArray.getJSONObject(i));
            NearbyPlacesList.add(NearbyPlacesMap);
        }
        return NearbyPlacesList;
    }

    public List<HashMap<String, String>> parse(String jsonData) throws JSONException {
        if (jsonData == null || jsonData.isEmpty()) {
            return new ArrayList<>();
        }

        JSONObject jsonObject = new JSONObject(jsonData);
        JSONArray jsonArray = jsonObject.getJSONArray("results");

        return getAllNearbyPlaces(jsonArray);
    }
}
