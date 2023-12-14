package com.example.locker;

import static com.example.locker.R.id.regphonenum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class register extends AppCompatActivity {


    // NFCUID IS THE VARIABLE TO SEND
    TextView nfc;
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    // dont remove fuck up
    String NFCUID="";
    DatabaseReference databasereference = FirebaseDatabase.getInstance().getReference("users");
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            byte[] tagId = tag.getId();
            String tagIdHex = ByteArrayToHexString(tagId);
            nfc.setText("Successfully Scanned"); // Update the NFC tag ID in your TextView
            NFCUID=tagIdHex; // Store the NFC tag ID in a variable for later use
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final TextView regphonenum = findViewById(R.id.regphonenum);
        final TextView regpwrd = findViewById(R.id.regpswrd);
        final TextView reglckpswrd = findViewById(R.id.reglckpswrd);
        nfc=findViewById(R.id.regnfcid);
        final TextView reglogin = findViewById(R.id.reglogin);
        final Button regsignup = findViewById(R.id.regsignup);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_MUTABLE);

        /////Start NFC


        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC Available", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "NFC Not Available", Toast.LENGTH_SHORT).show();
        }


        ////////Done nfc

        regsignup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final String phonenum = regphonenum.getText().toString().trim();
                final String pswrd = regpwrd.getText().toString().trim();
                final String lckpswrd = reglckpswrd.getText().toString().trim();

                if (phonenum.isEmpty() /*&&(phonenum.length()==10))*/ || pswrd.isEmpty() || lckpswrd.isEmpty() /* ||  NFCUID.isEmpty()*/) {
                    Toast.makeText(register.this, "Please enter the details", Toast.LENGTH_SHORT).show();
                } else {

                    //to check if the user already exists or not
                    databasereference. addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(phonenum)) {


                                Toast.makeText(register.this, "User Already Exists", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(register.this, login.class));

                            } else {
                                // User does not exist in the database
                                // Handle the case here
                                databasereference.child(phonenum).child("password").setValue(pswrd);
                                databasereference.child(phonenum).child("lockerpassword").setValue(lckpswrd);
                                databasereference.child(phonenum).child("NFCid").setValue(NFCUID);
                                databasereference.child(phonenum).child("Fingerprint").setValue("null");
                                databasereference.child(phonenum).child("Status").setValue("False");

                                Toast.makeText(register.this, "Successfully registered ", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(register.this, login.class));

                                finish();

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle error
                        }
                    });


                }
            }
        });

        reglogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(register.this, login.class));
                finish();
            }
        });

    }


    private String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = {
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
                "A", "B", "C", "D", "E", "F"
        };
        StringBuilder out = new StringBuilder();

        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out.append(hex[i]);
            i = in & 0x0f;
            out.append(hex[i]);
        }
        return out.toString();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

}