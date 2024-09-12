package com.example.mumbainosh;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RestroDashboard extends AppCompatActivity {

    private TextView timingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restro_dashboard);

        timingTextView = findViewById(R.id.timingTextView);

        String timing = getIntent().getStringExtra("TIMING");
        timingTextView.setText(timing);
    }
}