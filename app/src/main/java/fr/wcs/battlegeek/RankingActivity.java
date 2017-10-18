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
import fr.wcs.battlegeek.controller.DataController;
import fr.wcs.battlegeek.model.PlayerModel;
import fr.wcs.battlegeek.model.Settings;


public class RankingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private final String TAG = Settings.TAG;
    private ArrayList<PlayerModel> mPlayerModelList = new ArrayList<PlayerModel>();
    private ListView listView;
    private CustomListAdapter adapter;
    private PlayerModel mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ranking);

        ColorFilter filterYellow = new LightingColorFilter( Color.YELLOW, Color.YELLOW);

        Typeface titleFont = Typeface.createFromAsset(getAssets(), "fonts/emulogic.ttf");

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

        MySpinnerAdapter spinnerAdapter = new MySpinnerAdapter(this, R.layout.my_spinner_style, spinnerLevels);
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

                Collections.sort(mPlayerModelList, PlayerModel.ratioComparator);

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
            spinnerText.setTextSize(8);
            spinnerText.setTextColor(Color.parseColor("#FFEE00"));
            return spinnerText;
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}