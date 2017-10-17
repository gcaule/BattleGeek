package fr.wcs.battlegeek;

import android.graphics.Typeface;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import fr.wcs.battlegeek.controller.AI;
import fr.wcs.battlegeek.controller.DataController;
import fr.wcs.battlegeek.model.PlayerModel;
import fr.wcs.battlegeek.model.Settings;
import fr.wcs.battlegeek.utils.Utils;

import static fr.wcs.battlegeek.R.id.textViewLevelGames1;
import static fr.wcs.battlegeek.R.id.textViewLevelRatio1;

public class StatisticsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private final String TAG = Settings.TAG;

    private PlayerModel mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_statistics);

        ColorFilter filterYellow = new LightingColorFilter( Color.YELLOW, Color.YELLOW);

        final Spinner spinner = (Spinner) findViewById(R.id.spinnerLevel);
        ArrayList<String> spinnerLevels = new ArrayList<>();
        spinnerLevels.add(getString(R.string.button_easy));
        spinnerLevels.add(getString(R.string.button_medium));
        spinnerLevels.add(getString(R.string.button_hard));
        spinnerLevels.add(getString(R.string.button_impossible));
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerLevels);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(StatisticsActivity.this);

        TextView textViewStatistics = (TextView) findViewById(R.id.textViewStatistics);
        Typeface welcomeMessageFont = Typeface.createFromAsset(getAssets(), "fonts/atarifull.ttf");
        textViewStatistics.setTypeface(welcomeMessageFont);

        DataController dataController = new DataController(getApplicationContext());
        dataController.setDataReadyListener(new DataController.DataReadyListener() {
            @Override
            public void onDataReadyListener(PlayerModel player) {
                mPlayer = player;
                spinner.setSelection(0);
            }
        });

        ImageButton buttonHome = (ImageButton) findViewById(R.id.imageButtonHome);
        buttonHome.setColorFilter(filterYellow);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        AI.Level[] levels = new AI.Level[] {AI.Level.I, AI.Level.II, AI.Level.III, AI.Level.IMPOSSIBLE};

        // Game Parts
        TextView textViewLevelGames = (TextView) findViewById(textViewLevelGames1);
        textViewLevelGames.setText(mPlayer.getGameParts().get(String.valueOf(levels[i])).toString());

        // Victory / Defeats
        TextView textViewLevelVictoryDefeates = (TextView) findViewById(R.id.textViewLevelVictoryDefeats1);
        textViewLevelVictoryDefeates.setText(mPlayer.getVictories().get(String.valueOf(levels[i])).toString()
                + "/" + mPlayer.getDefeats().get(String.valueOf(levels[i])).toString());

        // Ratio
        TextView textViewLevelRatio = (TextView) findViewById(textViewLevelRatio1);
        textViewLevelRatio.setText(mPlayer.getRatio().get(String.valueOf(levels[i])).toString() + "%");

        // Best Shots Count
        TextView textViewBestShotsCount = (TextView) findViewById(R.id.textViewLevelBestShotsCountValue);
        int bestShotsCount = mPlayer.getBestShotsCount().get(String.valueOf(levels[i]));
        textViewBestShotsCount.setText(bestShotsCount == 2_147_483_647 ? "-" : String.valueOf(bestShotsCount));

        // Best Time
        TextView textViewLevelBestTime = (TextView) findViewById(R.id.textViewLevelBestTime1);
        long bestTime = mPlayer.getBestTime().get(String.valueOf(levels[i]));
        textViewLevelBestTime.setText( bestTime == 2147483647 ? "-" : Utils.timeFormat(bestTime));

        // Time
        TextView textViewLevelTime = (TextView) findViewById(R.id.textViewLevelTime1);
        long levelTime = mPlayer.getGameTime().get(String.valueOf(levels[i]));
        textViewLevelTime.setText(Utils.timeFormat(levelTime));

        // Total Time
        TextView textViewTotalTime = (TextView) findViewById(R.id.textViewLeveTotalTime);
        long totalTime = mPlayer.getTotalGameTime();
        textViewTotalTime.setText(Utils.timeFormat(totalTime));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
