package com.example.meetuphubapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    //create db object DatabaseReference to access firebase realtime db
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://meetuphub-2c305-default-rtdb.firebaseio.com");
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText email =(EditText) findViewById(R.id.emailET);
        final EditText password =(EditText) findViewById(R.id.password);

        final MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbtn);

        final TextView registerInstead = (TextView) findViewById(R.id.registerInstead);

        firebaseAuth = FirebaseAuth.getInstance();

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String emailTxt = email.getText().toString();
                final String passwordTxt = password.getText().toString();

                if(emailTxt.isEmpty() || passwordTxt.isEmpty()){
                    Toast.makeText(Login.this,"Please input username or password",Toast.LENGTH_SHORT).show();

                }else{
                    /**
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            //check if username exists
                            if (snapshot.hasChild(usernameTxt)){

                                // confirm password matches
                                final String getPassword = snapshot.child("password").getValue(String.class);

                                if (getPassword.equals(passwordTxt)){
                                    Toast.makeText(Login.this,"Login Successful",Toast.LENGTH_SHORT).show();
                                    //creating intent
                                    Intent timeline = new Intent(Login.this, Timeline.class);
                                    //starting home feed
                                    startActivity(timeline);
                                } else {
                                    Toast.makeText(Login.this,"Incorrect password",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(Login.this,"Incorrect password",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    */

                    firebaseAuth.signInWithEmailAndPassword(emailTxt,passwordTxt)
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    //creating intent
                                    Intent home = new Intent(Login.this, Timeline.class);
                                    //starting registration activity
                                    startActivity(home);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Login.this, "Error: " +e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }

            }
        });

        registerInstead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating intent
                Intent registration = new Intent(Login.this, Registration.class);
                //starting registration activity
                startActivity(registration);
            }
        });

    }
}