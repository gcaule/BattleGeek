package fr.wcs.battlegeek;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RankingFirebaseActivity extends AppCompatActivity {

    private static final String TAG = "RankingFirebaseActivity";
    // Firebase instance variables
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUsersDatabaseReference;
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_firebase);

        TextView displayFirebase = (TextView) findViewById(R.id.displayFirebase);

        //Call SharedPref
        mSharedPreferences = getPreferences(MODE_PRIVATE);

        //Get Pref for Player Name
        String PlayerName = getPreferences(MODE_PRIVATE).getString("PlayerName", null);

        // Initialize Firebase components
        mDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mDatabase.getReference().child("Users");

        // Write a message to the database
        mUsersDatabaseReference.push().setValue(PlayerName);

        // Read from the database
        mUsersDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }
}
