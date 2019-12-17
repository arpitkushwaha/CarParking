package com.e.carparking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.e.carparking.activities.MapActivity;
import com.e.carparking.activities.SigninActivity;


public class SplashActivity extends AppCompatActivity {

    private static int TIME_OUT = 1800;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SplashActivity.this, SigninActivity.class);
                startActivity(i);
                finish();
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MapActivity.class);
                startActivity(i);
                finish();
            }
        }, TIME_OUT);
    }

}
