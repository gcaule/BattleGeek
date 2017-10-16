package fr.wcs.battlegeek;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import fr.wcs.battlegeek.adapter.CustomListAdapter;
import fr.wcs.battlegeek.model.PlayerModel;
import fr.wcs.battlegeek.model.Settings;

public class RankingActivity extends AppCompatActivity {

    private final String TAG = Settings.TAG;
    private ArrayList<PlayerModel> mPlayerModelList = new ArrayList<PlayerModel>();
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ranking);

        ImageButton buttonHome = (ImageButton) findViewById(R.id.buttonHome);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RankingActivity.super.onBackPressed();
            }

        });

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, mPlayerModelList);
        listView.setAdapter(adapter);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference playersReference = firebaseDatabase.getReference("Users");
        playersReference.keepSynced(true);
        playersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    mPlayerModelList.add(data.getValue(PlayerModel.class));
                }

                Collections.sort(mPlayerModelList, PlayerModel.bestTimeComparator);
                Log.d(TAG, "onDataChange: " + mPlayerModelList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    /**
     * Sort the PlayerModel List by Best Time
     */
    private void sortByBestTime() {
        Collections.sort(mPlayerModelList, PlayerModel.bestTimeComparator);
        adapter.notifyDataSetChanged();
    }

    /**
     * Sort the PlayerModel List by Ratio
     */
    private void sortByRatio() {
        Collections.sort(mPlayerModelList, PlayerModel.ratioComparator);
        adapter.notifyDataSetChanged();
    }

    /**
     * Sort the PlayerModel List by Victories
     */
    private void sortByVictories() {
        Collections.sort(mPlayerModelList, PlayerModel.victoriesComparator);
        adapter.notifyDataSetChanged();
    }

    /**
     * Sort the PlayerModel List by Shots Count
     */
    private void sortByShotCount() {
        Collections.sort(mPlayerModelList, PlayerModel.bestShotsCountComparator);
        adapter.notifyDataSetChanged();
    }

    private void sortByName() {
        Collections.sort(mPlayerModelList, PlayerModel.nameComparator);
        adapter.notifyDataSetChanged();
    }
}