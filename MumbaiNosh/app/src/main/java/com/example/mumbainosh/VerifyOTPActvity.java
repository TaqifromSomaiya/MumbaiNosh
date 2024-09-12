package com.example.mumbainosh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class VerifyOTPActvity extends AppCompatActivity {

    private String receivedOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_verify_otpactvity);

        receivedOTP = getIntent().getStringExtra("otp_key");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnVerify = findViewById(R.id.btnVerifyOTP);
        EditText etOTP = findViewById(R.id.etOTP);

        btnVerify.setOnClickListener(v -> {
            String enteredOTP = etOTP.getText().toString().trim();

            if (enteredOTP.equals(receivedOTP)) {
                Toast.makeText(VerifyOTPActvity.this, "OTP Verified Successfully", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("is_otp_verified", true);
                editor.apply();

                Intent intent = new Intent(VerifyOTPActvity.this, MapsActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(VerifyOTPActvity.this, "Invalid OTP. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}