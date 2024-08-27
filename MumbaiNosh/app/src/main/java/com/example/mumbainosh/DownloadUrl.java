package com.example.mumbainosh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadUrl {

    public String ReadTheUrl(String placeUrl) throws IOException {
        String data = "";

        if (placeUrl == null || placeUrl.isEmpty()) {
            throw new IllegalArgumentException("URL cannot be null or empty");
        }

        // Use try-with-resources for automatic resource management
        try (InputStream inputStream = new URL(placeUrl).openStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {

            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(placeUrl).openConnection();
            httpURLConnection.setConnectTimeout(15000); // 15 seconds
            httpURLConnection.setReadTimeout(15000);    // 15 seconds
            httpURLConnection.connect();

            // Check for successful response code
            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + httpURLConnection.getResponseCode());
            }

            StringBuilder stringBuffer = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            data = stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace(); // Log the exception
            throw e; // Re-throw the exception to propagate it further
        }

        return data;
    }
}
