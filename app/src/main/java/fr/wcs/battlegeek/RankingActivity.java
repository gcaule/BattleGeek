package fr.wcs.battlegeek;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import fr.wcs.battlegeek.adapter.CustomListAdapter;
import fr.wcs.battlegeek.controller.AI;
import fr.wcs.battlegeek.controller.DataController;
import fr.wcs.battlegeek.model.PlayerModel;
import fr.wcs.battlegeek.model.Settings;

public class RankingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final String TAG = Settings.TAG;
    private ArrayList<PlayerModel> mPlayerModelList = new ArrayList<PlayerModel>();
    private ListView listView;
    private CustomListAdapter adapter;
    private PlayerModel mPlayer;

    private PlayerModel.ComparatorFactor mComparatorFactor = PlayerModel.ComparatorFactor.BEST_TIME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ranking);

        ColorFilter filterYellow = new LightingColorFilter( Color.YELLOW, Color.YELLOW);

        Typeface titleFont = Typeface.createFromAsset(getAssets(), "fonts/emulogic.ttf");
        Typeface mainFont = Typeface.createFromAsset(getAssets(), "fonts/atarifull.ttf");

        TextView titleMessage = (TextView) findViewById(R.id.rankingTitle);
        titleMessage.setTypeface(titleFont);
        TextView labelName = (TextView) findViewById(R.id.textViewLabelName);
        labelName.setTypeface(titleFont);
        TextView labelRatio = (TextView) findViewById(R.id.textViewLabelRatio);
        labelRatio.setTypeface(titleFont);
        TextView labelBestTime = (TextView) findViewById(R.id.textViewLabelBestTime);
        labelBestTime.setTypeface(titleFont);
        TextView labelShotsCount = (TextView) findViewById(R.id.textViewLabelShotsCount);
        labelShotsCount.setTypeface(titleFont);
        TextView labelGames = (TextView) findViewById(R.id.textViewLabelGames);
        labelGames.setTypeface(titleFont);

        ImageButton buttonHome = (ImageButton) findViewById(R.id.buttonHome);
        buttonHome.setColorFilter(filterYellow);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RankingActivity.super.onBackPressed();
            }

        });

        final Spinner spinnerRanking = (Spinner) findViewById(R.id.spinnerLevelRanking);
        ArrayList<String> spinnerLevels = new ArrayList<>();
        spinnerLevels.add(getString(R.string.button_easy));
        spinnerLevels.add(getString(R.string.button_medium));
        spinnerLevels.add(getString(R.string.button_hard));
        spinnerLevels.add(getString(R.string.button_impossible));
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerLevels);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRanking.setAdapter(spinnerAdapter);
        spinnerRanking.setOnItemSelectedListener(RankingActivity.this);

        DataController dataController = new DataController(getApplicationContext());
        dataController.setDataReadyListener(new DataController.DataReadyListener() {
            @Override
            public void onDataReadyListener(PlayerModel player) {
                mPlayer = player;
                spinnerRanking.setSelection(0);
            }
        });

        listView = (ListView) findViewById(R.id.list);
        adapter = new CustomListAdapter(this, mPlayerModelList);
        listView.setAdapter(adapter);

        FirebaseDatabase firebaseDatabase = DataController.getDatabase();
        DatabaseReference playersReference = firebaseDatabase.getReference("Users");
        playersReference.keepSynced(true);
        playersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPlayerModelList.clear();
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


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}