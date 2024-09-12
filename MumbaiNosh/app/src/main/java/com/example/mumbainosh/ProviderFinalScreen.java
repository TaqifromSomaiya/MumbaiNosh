package com.example.mumbainosh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;

import android.view.View;
import android.widget.ImageView;

public class ProviderFinalScreen extends AppCompatActivity {
    private static final String PREFS_NAME = "MyPrefsFile";
    private static final String KEY_ANIMATION_PLAYED = "animation_played";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_final_screen);

        LottieAnimationView animationView = findViewById(R.id.check_animation_view);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isAnimationPlayedOnce = prefs.getBoolean(KEY_ANIMATION_PLAYED, true);

        if (isAnimationPlayedOnce) {
            animationView.playAnimation();

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(KEY_ANIMATION_PLAYED, true);
            editor.apply();
        } else {
            animationView.setVisibility(View.GONE);
        }
    }

    public void onHomeClick(View v){
        Intent intent = new Intent(ProviderFinalScreen.this,FirstScreen.class);
        startActivity(intent);
    }


}