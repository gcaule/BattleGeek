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
import fr.wcs.battlegeek.controller.AI;
import fr.wcs.battlegeek.model.PlayerModel;
import fr.wcs.battlegeek.model.Settings;

public class RankingActivity extends AppCompatActivity {

    private final String TAG = Settings.TAG;
    private ArrayList<PlayerModel> mPlayerModelList = new ArrayList<PlayerModel>();
    private ListView listView;
    private CustomListAdapter adapter;

    private PlayerModel.ComparatorFactor mComparatorFactor = PlayerModel.ComparatorFactor.BEST_TIME;

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
        playersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    mPlayerModelList.add(data.getValue(PlayerModel.class));
                }

                Collections.sort(mPlayerModelList, PlayerModel.BestTimeComparator);
                Log.d(TAG, "onDataChange: " + mPlayerModelList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sortByBestTime(AI.Level level) {
        PlayerModel.setComparatorLevel(level);
        Collections.sort(mPlayerModelList, PlayerModel.BestTimeComparator);
        adapter.notifyDataSetChanged();
    }

    private void sortByRatio(AI.Level level) {
        PlayerModel.setComparatorLevel(level);
        Collections.sort(mPlayerModelList, PlayerModel.RatioComparator);
        adapter.notifyDataSetChanged();
    }

    private void sortByVictories(AI.Level level) {
        PlayerModel.setComparatorLevel(level);
        Collections.sort(mPlayerModelList, PlayerModel.VictoriesComparator);
        adapter.notifyDataSetChanged();
    }

    private void sortByShotCount(AI.Level level) {
        PlayerModel.setComparatorLevel(level);
        Collections.sort(mPlayerModelList, PlayerModel.BestShotsCountComparator);
        adapter.notifyDataSetChanged();
    }
}