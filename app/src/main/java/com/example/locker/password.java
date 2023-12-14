package com.example.locker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class password extends AppCompatActivity {

    EditText lockerpassword;
    String activeuser;


    Button btn;
    DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference("users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        lockerpassword=findViewById(R.id.lockerpassword);
        btn=findViewById(R.id.btn);
        dbreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                activeuser = Objects.requireNonNull(snapshot.child("active").getValue()).toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btn.setOnClickListener(v -> {
            String lckpassword = lockerpassword.getText().toString().trim();
            if(lckpassword.isEmpty()){
                Toast.makeText(password.this, "Please enter the locker password", Toast.LENGTH_SHORT).show();
            }
            else{
                dbreference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                       final String passwordfirebaseget=snapshot.child(activeuser).child("lockerpassword").getValue(String.class);
                        if(passwordfirebaseget.equals(lckpassword)){

                            dbreference.child(activeuser).child("Status").setValue("True");
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(password.this, "Locker Password Authenticated", Toast.LENGTH_SHORT).show();
                                }
                            }, 1000);
                            Intent intent = new Intent(password.this,login.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(password.this, "Wrong password Please try again", Toast.LENGTH_SHORT).show();

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });
    }
}