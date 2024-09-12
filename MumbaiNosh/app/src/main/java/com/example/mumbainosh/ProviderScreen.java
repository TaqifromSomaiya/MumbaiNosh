package com.example.mumbainosh;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProviderScreen extends AppCompatActivity {

    private EditText inputName, inputAddress, inputPinCode,inputTiming;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_screen);
        databaseReference = FirebaseDatabase.getInstance().getReference("FoodProviders");
        inputName = findViewById(R.id.etOrganizationName);
        inputAddress = findViewById(R.id.etOrganizationAddress);
        inputPinCode = findViewById(R.id.etOrganizationPinCode);
        inputTiming = findViewById(R.id.etOrganizationTiming);

    }
    public void OnProviderSubmit(View v) {
        String name = inputName.getText().toString().trim();
        String address = inputAddress.getText().toString().trim();
        String pinCode = inputPinCode.getText().toString().trim();
        String Timing = inputTiming.getText().toString().trim();

        if (name.isEmpty() || address.isEmpty() || pinCode.isEmpty() || Timing.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        Provider provider = new Provider(name, address, pinCode,Timing);
        databaseReference.push().setValue(provider)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ProviderScreen.this, "Data added successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProviderScreen.this, "Failed to add data", Toast.LENGTH_SHORT).show();
                    }
                });
        Intent intent = new Intent(ProviderScreen.this,ProviderFinalScreen.class);
        startActivity(intent);
    }
}