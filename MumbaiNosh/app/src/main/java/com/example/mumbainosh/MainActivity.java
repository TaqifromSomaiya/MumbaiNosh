package com.example.mumbainosh;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VideoView videoView = findViewById(R.id.videoView);

        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro2);
        videoView.setVideoURI(videoUri);
        videoView.setOnCompletionListener(mp -> navigateToNextScreen());
        findViewById(R.id.main).setOnClickListener(v -> navigateToNextScreen());
        videoView.start();
    }

    private void navigateToNextScreen() {
        Intent intent = new Intent(MainActivity.this, FirstScreen.class);
        startActivity(intent);
        finish();
    }
}
