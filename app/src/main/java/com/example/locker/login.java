package com.example.locker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login extends AppCompatActivity {

    DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference("users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        final EditText phonenum = findViewById(R.id.phonenum);
        final EditText password = findViewById(R.id.password);
        final Button login = findViewById(R.id.loginbtn);
        final TextView signup = findViewById(R.id.signup);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String phnnum=phonenum.getText().toString().trim();
                final String pswrd = password.getText().toString().trim();

                if (phnnum.isEmpty()|| pswrd.isEmpty()){
                    Toast.makeText(login.this, "Please enter the username or password ", Toast.LENGTH_SHORT).show();
                }
                else{
                    dbreference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //check if theuser already exists
                            if(!snapshot.hasChild(phnnum)){
                                Toast.makeText(login.this, "user doesn't exist", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                    final String getpassword=snapshot.child(phnnum).child("password").getValue(String.class);
                                    if(getpassword.equals(pswrd)){
                                        dbreference.child("active").setValue(phnnum);
                                        startActivity(new Intent(login.this, MainActivity.class));
                                        Toast.makeText(login.this, "Successfully Logged in ", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                    else{
                                        Toast.makeText(login.this, "Wrong password", Toast.LENGTH_SHORT).show();
                                    }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {


                        }
                    });

                }
            }
        });

                signup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(login.this, register.class));
                        finish();
                    }
                });
    }
}