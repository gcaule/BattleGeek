package fr.wcs.battlegeek;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fr.wcs.battlegeek.model.PlayerModel;
import fr.wcs.battlegeek.model.Settings;

public class StatisticsActivity extends AppCompatActivity {
    private final String TAG = "Statistics";

    private FirebaseDatabase mDatabase;
    private DatabaseReference mUsersDatabaseReference;
    private String uidFirebase;

    private PlayerModel mPlayerModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        //Call SharedPref
        SharedPreferences mSharedPreferences = getSharedPreferences(Settings.FILE_NAME, MODE_PRIVATE);
        uidFirebase = mSharedPreferences.getString(Settings.UID, null);

        mDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mDatabase.getReference().child("Users/" + uidFirebase);

        mUsersDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPlayerModel = dataSnapshot.getValue(PlayerModel.class);
                updateData();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void updateData() {
        Log.d(TAG, "onDataChange: " + mPlayerModel);
    }
}
