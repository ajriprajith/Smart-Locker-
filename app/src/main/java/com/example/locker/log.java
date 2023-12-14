package com.example.locker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class log extends AppCompatActivity {

    DatabaseReference logsRef = FirebaseDatabase.getInstance().getReference().child("access_logs");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        // Reference to the 'access_logs' node in the database

        ArrayList list=new ArrayList<>();
        Adapter Adapter;
        Adapter=new Adapter(this,list);

        Button back = findViewById(R.id.button2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(log.this, MainActivity.class);
                startActivity(intent);
            }
        });
// Retrieve the log data
        logsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Iterate over each log entry
                for (DataSnapshot logSnapshot : dataSnapshot.getChildren()) {
                    String logKey = logSnapshot.getKey();
                    String logData = logSnapshot.getValue().toString();


                    // Extract the relevant information from the log entry
                    String user = logData.split(":")[1].trim();

                    String timestamp = logData.replaceAll(".*?(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}).*", "$1");

                    // Display the log information (e.g., update a TextView in your UI)
                    String logInfo = "User: " + user + "\n" + "Timestamp: " + timestamp + "\n" + "---\n";

                    // Assuming you have a TextView with id "logTextView" in your layout
                    TextView logTextView = findViewById(R.id.logTextView);
                    list.addAll(Collections.singleton(logInfo));
                    logTextView.append(logInfo);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error in case the data retrieval is unsuccessful
                Log.e("Firebase", "Error retrieving log data: " + databaseError.getMessage());
            }
        });

    }
}