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

        // Best Time
        TextView textViewLevelBestTime1 = (TextView) findViewById(R.id.textViewLevelBestTime1);
        String time1 = mPlayer.getBestTime().get(String.valueOf(AI.Level.I)).toString();
        textViewLevelBestTime1.setText(time1.equals("-1") ? "-" : time1);
        TextView textViewLevelBestTime2 = (TextView) findViewById(R.id.textViewLevelBestTime2);
        String time2 = mPlayer.getBestTime().get(String.valueOf(AI.Level.II)).toString();
        textViewLevelBestTime2.setText(time1.equals("-1") ? "-" : time2);
        TextView textViewLevelBestTime3 = (TextView) findViewById(R.id.textViewLevelBestTime3);
        String time3 = mPlayer.getBestTime().get(String.valueOf(AI.Level.III)).toString();
        textViewLevelBestTime3.setText(time1.equals("-1") ? "-" : time3);
        TextView textViewLevelBestTime4 = (TextView) findViewById(R.id.textViewLevelBestTime4);
        String time4 = mPlayer.getBestTime().get(String.valueOf(AI.Level.IMPOSSIBLE)).toString();
        textViewLevelBestTime4.setText(time1.equals("-1") ? "-" : time4);

        // Time
        TextView textViewLevelTime1 = (TextView) findViewById(R.id.textViewLevelTime1);
        textViewLevelTime1.setText(mPlayer.getGameTime().get(String.valueOf(AI.Level.I).toString()) + "s");
        TextView textViewLevelTime2 = (TextView) findViewById(R.id.textViewLevelTime2);
        textViewLevelTime2.setText(mPlayer.getGameTime().get(String.valueOf(AI.Level.II).toString()) + "s");
        TextView textViewLevelTime3 = (TextView) findViewById(R.id.textViewLevelTime3);
        textViewLevelTime3.setText(mPlayer.getGameTime().get(String.valueOf(AI.Level.III).toString()) + "s");
        TextView textViewLevelTime4 = (TextView) findViewById(R.id.textViewLevelTime4);
        textViewLevelTime4.setText(mPlayer.getGameTime().get(String.valueOf(AI.Level.IMPOSSIBLE).toString()) + "s");

        // Total Time
        TextView textViewTotalTime = (TextView) findViewById(R.id.textViewLeveTotalTime);
        textViewTotalTime.setText(String.valueOf(mPlayer.getTotalGameTime()) + "s");
    }
}
