package com.example.meetuphubapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Timeline extends AppCompatActivity {

    private ImageView picha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
         picha = findViewById(R.id.picha);

        Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/meetuphub-2c305.appspot.com/o/profile.png?alt=media&token=acc71a35-aec4-465c-9890-ee877768fd7f")
                .into(picha);
    }
}