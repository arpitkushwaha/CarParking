package com.e.carparking.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.e.carparking.R;
import com.e.carparking.SplashActivity;
import com.e.carparking.databinding.ActivitySigninBinding;

public class SigninActivity extends AppCompatActivity {

    ActivitySigninBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Sign In");
        binding = DataBindingUtil.setContentView(this,R.layout.activity_signin);
        binding.btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SigninActivity.this, MapActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}
