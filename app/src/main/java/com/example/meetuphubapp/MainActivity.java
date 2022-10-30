package com.example.meetuphubapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // creating a new intent
                Intent i = new Intent(MainActivity.this, Login.class);

                // starting a new activity.
                startActivity(i);

                // finishing the current activity.
                finish();
            }
        }, 5000);

    }
}