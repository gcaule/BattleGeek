package fr.wcs.battlegeek;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
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

import static fr.wcs.battlegeek.R.id.textViewLevelGames1;
import static fr.wcs.battlegeek.R.id.textViewLevelRatio1;

public class StatisticsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private final String TAG = Settings.TAG;

    private PlayerModel mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

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

        DataController dataController = new DataController(getApplicationContext());
        dataController.setDataReadyListener(new DataController.DataReadyListener() {
            @Override
            public void onDataReadyListener(PlayerModel player) {
                mPlayer = player;
                spinner.setSelection(0);
            }
        });

        ImageButton buttonHome = (ImageButton) findViewById(R.id.imageButtonHome);
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Log.d(TAG, "onItemSelected() called with: adapterView = [" + adapterView + "], view = [" + view + "], i = [" + i + "], l = [" + l + "]");
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

        // Best Time
        TextView textViewLevelBestTime = (TextView) findViewById(R.id.textViewLevelBestTime1);
        String time1 = mPlayer.getBestTime().get(String.valueOf(levels[i])).toString();
        textViewLevelBestTime.setText(time1.equals("2147483647") ? "-" : time1 + "s");

        // Time
        TextView textViewLevelTime = (TextView) findViewById(R.id.textViewLevelTime1);
        textViewLevelTime.setText(mPlayer.getGameTime().get(String.valueOf(levels[i]).toString()) + "s");

        // Total Time
        TextView textViewTotalTime = (TextView) findViewById(R.id.textViewLeveTotalTime);
        textViewTotalTime.setText(String.valueOf(mPlayer.getTotalGameTime()) + "s");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
