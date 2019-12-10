package com.e.carparking.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.e.carparking.R;

public class SigninActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Sign In");
        setContentView(R.layout.activity_signin);
        TextView uName = findViewById(R.id.userName);
        TextView uMailId = findViewById(R.id.userMailID);
        TextView uPhoneNo = findViewById(R.id.userPhoneNo);
        Button signUpButton = findViewById(R.id.signUpButton);

    }
}
