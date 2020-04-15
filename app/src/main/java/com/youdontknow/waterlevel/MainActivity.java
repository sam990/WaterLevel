package com.youdontknow.waterlevel;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int MAXDEPTH = 21;
    private static final int MINDIFF = 5;
    ProgressBar pBar;
    TextView tt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pBar = findViewById(R.id.progressBar);
        tt = findViewById(R.id.textView3);
        tt.setText("Connecting....");
        pBar.setMax(MAXDEPTH);
        pBar.setProgress(0, true);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://waterlevelindicator-ba42a.firebaseio.com");
        DatabaseReference myRef = database.getReference("testing123");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Long value = dataSnapshot.getValue(Long.class);
                Log.d(TAG, "Value is: " + value);
                updateProgress(value.intValue());
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void updateProgress(int new_Val){
        int offset = MAXDEPTH - new_Val;
        offset = (int)(((double)offset/MAXDEPTH)*MINDIFF) + offset;
        int percentage;
        double frac;
        if(offset >= MAXDEPTH){
            pBar.setProgress(MAXDEPTH, true);
            tt.setText("Completely Filled");
            return;
        }
        if(offset <= 0){
            pBar.setProgress(0, true);
            tt.setText("Empty");
        }
        else {
            percentage = (offset * 100) / (MAXDEPTH);
            pBar.setProgress(offset);
            tt.setText(percentage + "% Filled");
        }
    }
}
