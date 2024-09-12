package com.example.mumbainosh;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WithBottomNavActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_bottom_nav);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragmentManager = getSupportFragmentManager();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                Intent intent = null;

                if (item.getItemId()== R.id.nav_avail_food)
                {
                    intent = new Intent(WithBottomNavActivity.this, AavailFoodList.class);

                } else if (item.getItemId() == R.id.nav_first_screen) {
                    intent = new Intent(WithBottomNavActivity.this, FirstScreen.class);

                } else if (item.getItemId() == R.id.nav_about) {
                    intent = new Intent(WithBottomNavActivity.this, About.class);

                }

                if (intent != null) {
                    startActivity(intent);
                    return true;
                }

                return false;
            }
        });

        // Set default selection if needed
        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_first_screen);
        }
    }
}
