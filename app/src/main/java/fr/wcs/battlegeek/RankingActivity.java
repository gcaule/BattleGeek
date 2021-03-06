package fr.wcs.battlegeek;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;

import fr.wcs.battlegeek.adapter.CustomListAdapter;
import fr.wcs.battlegeek.controller.AI;
import fr.wcs.battlegeek.controller.DataController;
import fr.wcs.battlegeek.model.PlayerModel;
import fr.wcs.battlegeek.model.Settings;

import static fr.wcs.battlegeek.model.PlayerModel.ComparatorFactor.BEST_TIME;
import static fr.wcs.battlegeek.model.PlayerModel.ComparatorFactor.NAME;
import static fr.wcs.battlegeek.model.PlayerModel.ComparatorFactor.RATIO;
import static fr.wcs.battlegeek.model.PlayerModel.ComparatorFactor.SHOTS_COUNT;
import static fr.wcs.battlegeek.model.PlayerModel.ComparatorFactor.GAME_PARTS;


public class RankingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final String TAG = Settings.TAG;
    private ArrayList<PlayerModel> mPlayerModelList = new ArrayList<>();
    private ListView listView;
    private CustomListAdapter adapter;
    private PlayerModel mPlayer;
    private PlayerModel.ComparatorFactor mComparatorFactor = NAME;
    private AI.Level mLastSelectedLevel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ranking);

        ColorFilter filterYellow = new LightingColorFilter( Color.YELLOW, Color.YELLOW);

        Typeface titleFont = Typeface.createFromAsset(getAssets(), "fonts/emulogic.ttf");

        TextView titleMessage = (TextView) findViewById(R.id.rankingTitle);
        titleMessage.setTypeface(titleFont);
        final TextView labelName = (TextView) findViewById(R.id.textViewLabelName);
        labelName.setTypeface(titleFont);
        final TextView labelRatio = (TextView) findViewById(R.id.textViewLabelRatio);
        labelRatio.setTypeface(titleFont);
        labelRatio.setTextColor(Color.parseColor("#FF960D"));
        final TextView labelBestTime = (TextView) findViewById(R.id.textViewLabelBestTime);
        labelBestTime.setTypeface(titleFont);
        final TextView labelShotsCount = (TextView) findViewById(R.id.textViewLabelShotsCount);
        labelShotsCount.setTypeface(titleFont);
        final TextView labelGames = (TextView) findViewById(R.id.textViewLabelGames);
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

        MySpinnerAdapter spinnerAdapter = new MySpinnerAdapter(this, R.layout.custom_spinner_item, spinnerLevels);
        spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
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

                sortByRatio();

                Log.d(TAG, "onDataChange: " + mPlayerModelList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        labelName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                labelName.setTextColor(Color.parseColor("#FF960D"));
                labelRatio.setTextColor(Color.parseColor("#FFEE00"));
                labelBestTime.setTextColor(Color.parseColor("#FFEE00"));
                labelShotsCount.setTextColor(Color.parseColor("#FFEE00"));
                labelGames.setTextColor(Color.parseColor("#FFEE00"));
                sortByName();
            }

        });
        labelRatio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                labelName.setTextColor(Color.parseColor("#FFEE00"));
                labelRatio.setTextColor(Color.parseColor("#FF960D"));
                labelBestTime.setTextColor(Color.parseColor("#FFEE00"));
                labelShotsCount.setTextColor(Color.parseColor("#FFEE00"));
                labelGames.setTextColor(Color.parseColor("#FFEE00"));
                sortByRatio();
            }

        });
        labelBestTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                labelName.setTextColor(Color.parseColor("#FFEE00"));
                labelRatio.setTextColor(Color.parseColor("#FFEE00"));
                labelBestTime.setTextColor(Color.parseColor("#FF960D"));
                labelShotsCount.setTextColor(Color.parseColor("#FFEE00"));
                labelGames.setTextColor(Color.parseColor("#FFEE00"));
                sortByBestTime();
            }
        });

        labelShotsCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                labelName.setTextColor(Color.parseColor("#FFEE00"));
                labelRatio.setTextColor(Color.parseColor("#FFEE00"));
                labelBestTime.setTextColor(Color.parseColor("#FFEE00"));
                labelShotsCount.setTextColor(Color.parseColor("#FF960D"));
                labelGames.setTextColor(Color.parseColor("#FFEE00"));
                sortByShotCount();
            }
        });

        labelGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                labelName.setTextColor(Color.parseColor("#FFEE00"));
                labelRatio.setTextColor(Color.parseColor("#FFEE00"));
                labelBestTime.setTextColor(Color.parseColor("#FFEE00"));
                labelShotsCount.setTextColor(Color.parseColor("#FFEE00"));
                labelGames.setTextColor(Color.parseColor("#FF960D"));
                sortByGameParts();
            }
        });

    }

    private static class MySpinnerAdapter extends ArrayAdapter<String> {

        Typeface mainFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/emulogic.ttf");

        private MySpinnerAdapter(Context context, int resource, List<String> items) {
            super(context, resource, items);
        }

        public TextView getView(int position, View convertView, ViewGroup parent) {
            TextView spinnerText = (TextView) super.getView(position, convertView, parent);
            spinnerText.setTypeface(mainFont);
            return spinnerText;
        }

        public TextView getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView spinnerText = (TextView) super.getDropDownView(position, convertView, parent);
            spinnerText.setTypeface(mainFont);
            spinnerText.setTextColor(Color.parseColor("#FFEE00"));
            return spinnerText;
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mLastSelectedLevel = PlayerModel.getComparatorLevel();
        PlayerModel.setComparatorLevel(AI.Level.values()[i]);
        switch (mComparatorFactor) {
            case BEST_TIME:
                sortByBestTime();
                break;
            case GAME_PARTS:
                sortByGameParts();
                break;
            case RATIO:
                sortByRatio();
                break;
            case SHOTS_COUNT:
                sortByShotCount();
                break;
            case NAME:
                sortByName();
                break;
        }
        mLastSelectedLevel = PlayerModel.getComparatorLevel();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    /**
     * Sort the PlayerModel List by Best Time
     */
    private void sortByBestTime() {
        if(mComparatorFactor != BEST_TIME || mLastSelectedLevel != PlayerModel.getComparatorLevel()) {
            ArrayList<PlayerModel> filteredPlayers = new ArrayList<>();
            // Filter Bullshit
            for(PlayerModel p : mPlayerModelList) {
                if(p.getBestTime().get(PlayerModel.getComparatorLevel().toString()) == -1L) {
                    filteredPlayers.add(p);
                }
            }
            mPlayerModelList.removeAll(filteredPlayers);
            Collections.sort(mPlayerModelList, PlayerModel.bestTimeComparator);
            mPlayerModelList.addAll(filteredPlayers);
            adapter.notifyDataSetChanged();
            mComparatorFactor = BEST_TIME;
        }
        else {
            Collections.reverse(mPlayerModelList);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Sort the PlayerModel List by Ratio
     */
    private void sortByRatio() {
        if(mComparatorFactor != RATIO || mLastSelectedLevel != PlayerModel.getComparatorLevel()) {
            Collections.sort(mPlayerModelList, PlayerModel.ratioComparator);
            adapter.notifyDataSetChanged();
            mComparatorFactor = RATIO;
        }
        else {
            Collections.reverse(mPlayerModelList);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Sort the PlayerModel List by Victories
     */
    private void sortByGameParts() {
        if(mComparatorFactor != GAME_PARTS || mLastSelectedLevel != PlayerModel.getComparatorLevel()) {
            Collections.sort(mPlayerModelList, PlayerModel.gamePartsComparator);
            adapter.notifyDataSetChanged();
            mComparatorFactor = GAME_PARTS;
        }
        else {
            Collections.reverse(mPlayerModelList);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Sort the PlayerModel List by Shots Count
     */
    private void sortByShotCount() {
        if(mComparatorFactor != SHOTS_COUNT || mLastSelectedLevel != PlayerModel.getComparatorLevel()) {
            Collections.sort(mPlayerModelList, PlayerModel.bestShotsCountComparator);
            adapter.notifyDataSetChanged();
            mComparatorFactor = SHOTS_COUNT;
        }
        else {
            Collections.reverse(mPlayerModelList);
            adapter.notifyDataSetChanged();
        }
    }

    private void sortByName() {
        if(mComparatorFactor != NAME || mLastSelectedLevel != PlayerModel.getComparatorLevel()) {
            Collections.sort(mPlayerModelList, PlayerModel.nameComparator);
            adapter.notifyDataSetChanged();
            mComparatorFactor = NAME;
        }
        else {
            Collections.reverse(mPlayerModelList);
            adapter.notifyDataSetChanged();
        }
    }
}