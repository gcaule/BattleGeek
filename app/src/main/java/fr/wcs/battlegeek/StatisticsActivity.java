package fr.wcs.battlegeek;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import fr.wcs.battlegeek.controller.AI;
import fr.wcs.battlegeek.controller.DataController;
import fr.wcs.battlegeek.model.PlayerModel;

public class StatisticsActivity extends AppCompatActivity {
    private final String TAG = "Statistics";

    private PlayerModel mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        DataController dataController = new DataController(getApplicationContext());
        dataController.setDataReadyListener(new DataController.DataReadyListener() {
            @Override
            public void onDataReadyListener(PlayerModel player) {
                mPlayer = player;
                initView();
            }
        });

    }

    void initView() {
        Log.d(TAG, mPlayer.toString());

        // Game Parts
        TextView textViewLevelGames1 = (TextView) findViewById(R.id.textViewLevelGames1);
        textViewLevelGames1.setText(mPlayer.getGameParts().get(String.valueOf(AI.Level.I)).toString());
        TextView textViewLevelGames2 = (TextView) findViewById(R.id.textViewLevelGames2);
        textViewLevelGames2.setText(mPlayer.getGameParts().get(String.valueOf(AI.Level.II)).toString());
        TextView textViewLevelGames3 = (TextView) findViewById(R.id.textViewLevelGames3);
        textViewLevelGames3.setText(mPlayer.getGameParts().get(String.valueOf(AI.Level.III)).toString());
        TextView textViewLevelGames4 = (TextView) findViewById(R.id.textViewLevelGames4);
        textViewLevelGames4.setText(mPlayer.getGameParts().get(String.valueOf(AI.Level.IMPOSSIBLE)).toString());

        // Victory / Defeats
        TextView textViewLevelVictoryDefeates1 = (TextView) findViewById(R.id.textViewLevelVictoryDefeats1);
        textViewLevelVictoryDefeates1.setText(mPlayer.getVictories().get(String.valueOf(AI.Level.I)).toString()
        + "/" + mPlayer.getDefeats().get(String.valueOf(AI.Level.I)).toString());
        TextView textViewLevelVictoryDefeates2 = (TextView) findViewById(R.id.textViewLevelVictoryDefeats2);
        textViewLevelVictoryDefeates2.setText(mPlayer.getVictories().get(String.valueOf(AI.Level.II)).toString()
                + "/" + mPlayer.getDefeats().get(String.valueOf(AI.Level.II)).toString());
        TextView textViewLevelVictoryDefeates3 = (TextView) findViewById(R.id.textViewLevelVictoryDefeats3);
        textViewLevelVictoryDefeates3.setText(mPlayer.getVictories().get(String.valueOf(AI.Level.III)).toString()
                + "/" + mPlayer.getDefeats().get(String.valueOf(AI.Level.III)).toString());
        TextView textViewLevelVictoryDefeates4 = (TextView) findViewById(R.id.textViewLevelVictoryDefeats4);
        textViewLevelVictoryDefeates4.setText(mPlayer.getVictories().get(String.valueOf(AI.Level.IMPOSSIBLE)).toString()
                + "/" + mPlayer.getDefeats().get(String.valueOf(AI.Level.IMPOSSIBLE)).toString());

        // Ratio
        TextView textViewLevelRatio1 = (TextView) findViewById(R.id.textViewLevelRatio1);
        textViewLevelRatio1.setText(mPlayer.getRatio().get(String.valueOf(AI.Level.I)).toString());
        TextView textViewLevelRatio2 = (TextView) findViewById(R.id.textViewLevelRatio2);
        textViewLevelRatio2.setText(mPlayer.getRatio().get(String.valueOf(AI.Level.II)).toString());
        TextView textViewLevelRatio3 = (TextView) findViewById(R.id.textViewLevelRatio3);
        textViewLevelRatio3.setText(mPlayer.getRatio().get(String.valueOf(AI.Level.III)).toString());
        TextView textViewLevelRatio4 = (TextView) findViewById(R.id.textViewLevelRatio4);
        textViewLevelRatio4.setText(mPlayer.getRatio().get(String.valueOf(AI.Level.IMPOSSIBLE)).toString());

        TextView textViewLevelestTime1 = (TextView) findViewById(R.id.textViewLevelBestTime1);
        textViewLevelestTime1.setText(mPlayer.getBestTime().get(String.valueOf(AI.Level.I)).toString());
        TextView textViewLevelestTime2 = (TextView) findViewById(R.id.textViewLevelBestTime2);
        textViewLevelestTime2.setText(mPlayer.getBestTime().get(String.valueOf(AI.Level.II)).toString());
        TextView textViewLevelestTime3 = (TextView) findViewById(R.id.textViewLevelBestTime3);
        textViewLevelestTime3.setText(mPlayer.getBestTime().get(String.valueOf(AI.Level.III)).toString());
        TextView textViewLevelestTime4 = (TextView) findViewById(R.id.textViewLevelBestTime4);
        textViewLevelestTime4.setText(mPlayer.getBestTime().get(String.valueOf(AI.Level.IMPOSSIBLE)).toString());
    }
}
