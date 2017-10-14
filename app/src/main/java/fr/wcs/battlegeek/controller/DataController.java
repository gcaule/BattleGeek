package fr.wcs.battlegeek.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fr.wcs.battlegeek.model.PlayerModel;
import fr.wcs.battlegeek.model.Settings;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by adphi on 12/10/17.
 */

public class DataController {
    private final String TAG = "DataController";
    private SharedPreferences mSharedPreferences;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUsersDatabaseReference;
    private String mPlayerUID;

    private PlayerModel mPlayerModel;

    private DataReadyListener listener;

    public DataController(Context context){
        //Call SharedPref
        mSharedPreferences = context.getSharedPreferences(Settings.FILE_NAME, MODE_PRIVATE);
        mPlayerUID = mSharedPreferences.getString(Settings.UID, null);

        // FireBase
        mDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mDatabase.getReference().child("Users").child(mPlayerUID);
        mUsersDatabaseReference.keepSynced(true);

        mUsersDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPlayerModel = dataSnapshot.getValue(PlayerModel.class);
                if(listener != null) {
                    listener.onDataReadyListener(mPlayerModel);
                }
                Log.d(TAG, "onDataChange() called with: dataSnapshot = [" + dataSnapshot + "] " + mPlayerModel);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updatePlayer(PlayerModel player) {
        mPlayerModel = player;
        mUsersDatabaseReference.setValue(player);
    }
    
    public void setDataReadyListener(DataReadyListener listener) {
        this.listener = listener;
    }

    public interface DataReadyListener {
        void  onDataReadyListener(PlayerModel player);
    }
}
