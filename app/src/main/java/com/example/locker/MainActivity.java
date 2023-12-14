package com.example.locker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.nfc.NfcAdapter;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.content.Intent;
import android.content.IntentFilter;
import android.app.PendingIntent;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    TextView nfc;
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    String NFCUID = null;
    String active;
    DatabaseReference dbreference;

    Intent intent2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent2 = new Intent(MainActivity.this, password.class);

        nfc = findViewById(R.id.nfcid);


        @SuppressLint("MissingInflatedId")
        Button log = findViewById(R.id.log_button);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, log.class);
                startActivity(intent);
            }
        });
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_MUTABLE);

        dbreference = FirebaseDatabase.getInstance().getReference("users");
        dbreference.child("active").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                active = snapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Database error: " + error.getMessage());
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            String NFCUID = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));

            dbreference.child(active).child("NFCid").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    final String nfcidcheck = snapshot.getValue(String.class);
                    if (NFCUID.equals(nfcidcheck)) {
                        nfc.setText("NFC authenticated");
                        startActivity(intent2);
                        finish();
                    } else {
                        nfc.setText("Incorrect NFC tag scanned");
                        Intent intent3 = new Intent(MainActivity.this, login.class);
                        startActivity(intent3);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("TAG", "Database error: " + error.getMessage());
                }
            });
        }
    }

    private String ByteArrayToHexString(byte[] byteArrayExtra) {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";

        for(j = 0 ; j < byteArrayExtra.length ; ++j)
        {
            in = (int) byteArrayExtra[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

        @Override
        protected void onResume() {
            super.onResume();
            IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
            IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
            IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
            IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected, tagDetected, ndefDetected};


            if (nfcAdapter != null)
                nfcAdapter.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);
        }


    }
