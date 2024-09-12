package com.example.mumbainosh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;



    public class FirstScreen extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_first_screen);
        }

        public void onProviderClick(View view) {
            Intent intent = new Intent(FirstScreen.this, ProviderScreen.class);
            startActivity(intent);
        }

        public void onUserClick(View view) {
            // Access SharedPreferences to check if OTP has already been verified
            SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
            boolean isOtpVerified = sharedPreferences.getBoolean("is_otp_verified", false);

            Intent intent;

            if (isOtpVerified) {
                // If OTP is already verified, skip OTP screen and go to the MainScreen
                intent = new Intent(FirstScreen.this, MapsActivity.class);
            } else {
                // If OTP is not verified, go to the Send OTP screen
                intent = new Intent(FirstScreen.this, SendOTPActivity.class);
            }

            startActivity(intent);
        }

    }