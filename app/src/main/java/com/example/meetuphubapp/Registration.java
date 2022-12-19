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

public class Registration extends AppCompatActivity {

    //create db object DatabaseReference to access firebase realtime db
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://meetuphub-2c305-default-rtdb.firebaseio.com");

    FirebaseAuth firebaseAuth;
     private EditText regusername, emailET,regpassword, repassword;
     private MaterialButton regbtn;
     private String regusernameTxt, emailTxt, regpasswordTxt, repasswordTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

         regusername =(EditText) findViewById(R.id.regusername);
         emailET =(EditText) findViewById(R.id.email);
         regpassword =(EditText) findViewById(R.id.regpassword);
         repassword =(EditText) findViewById(R.id.repassword);

         regbtn = (MaterialButton) findViewById(R.id.regbtn);
         firebaseAuth = FirebaseAuth.getInstance();

        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get data from EditTexts into string variables
                 regusernameTxt = regusername.getText().toString();
                 emailTxt = emailET.getText().toString();
                 regpasswordTxt = regpassword.getText().toString();
                 repasswordTxt = repassword.getText().toString();

                // check if all fields are filled before sending to firebase
                if(regusernameTxt.isEmpty() || emailTxt.isEmpty() || regpasswordTxt.isEmpty()) {
                    Toast.makeText(Registration.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }

                //check if passwords match
                else if(!regpasswordTxt.equals(repasswordTxt)){
                    Toast.makeText(Registration.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }

                else {

                        authenticate ();


                }
            }
        });
    }

    private void authenticate() {
        firebaseAuth.createUserWithEmailAndPassword(emailTxt, regpasswordTxt)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                       //save data
                       saveData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Registration.this, "Error: " +e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void saveData() {
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //check if username is registered before
                if (snapshot.hasChild(regusernameTxt)){
                    Toast.makeText(Registration.this, "Username already exists. Pick another", Toast.LENGTH_SHORT).show();
                }

                else {

                    //send data to db
                    //username is unique identifier; all data comes under username
                    databaseReference.child("users").child(regusernameTxt).child("email").setValue(emailTxt);
                    databaseReference.child("users").child(regusernameTxt).child("password").setValue(regpasswordTxt);

                    Toast.makeText(Registration.this, "User registered Successfully!", Toast.LENGTH_SHORT).show();
                    //creating intent
                    Intent loginpage = new Intent(Registration.this, Login.class);
                    //starting login page
                    startActivity(loginpage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}