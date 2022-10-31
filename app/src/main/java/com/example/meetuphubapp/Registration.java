package com.example.meetuphubapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class Registration extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        TextView regusername =(TextView) findViewById(R.id.regusername);
       //TextView email =(TextView) findViewById(R.id.email);
        TextView regpassword =(TextView) findViewById(R.id.regpassword);
       // TextView repassword =(TextView) findViewById(R.id.repassword);

        MaterialButton regbtn = (MaterialButton) findViewById(R.id.regbtn);

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(regusername.getText().toString().equals("wambui") && regpassword.getText().toString().equals("wambui")){
                    //correct
                    Toast.makeText(Registration.this,"REGISTRATION SUCCESSFUL",Toast.LENGTH_SHORT).show();
                    //creating intent
                    Intent feed = new Intent(Registration.this, Timeline.class);
                    //starting home feed
                    startActivity(feed);
                }else
                    //incorrect
                    Toast.makeText(Registration.this,"REGISTRATION FAILED! TRY AGAIN",Toast.LENGTH_SHORT).show();
            }
        });
    }
}