package fr.wcs.battlegeek;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
    private Typeface mTypeface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_statistics);

        ColorFilter filterYellow = new LightingColorFilter( Color.YELLOW, Color.YELLOW);

        final Typeface mFont = Typeface.createFromAsset(getAssets(),
                "fonts/atarifull.ttf");
        final ViewGroup mContainer = (ViewGroup)
                findViewById(android.R.id.content).getRootView();

        StatisticsActivity.setAppFont(mContainer, mFont);

        final Spinner spinner = (Spinner) findViewById(R.id.spinnerLevel);
        ArrayList<String> spinnerLevels = new ArrayList<>();
        spinnerLevels.add(getString(R.string.button_easy));
        spinnerLevels.add(getString(R.string.button_medium));
        spinnerLevels.add(getString(R.string.button_hard));
        spinnerLevels.add(getString(R.string.button_impossible));
        MySpinnerAdapter spinnerAdapter = new MySpinnerAdapter(this, R.layout.custom_spinner_item, spinnerLevels);
        spinnerAdapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(StatisticsActivity.this);

        TextView textViewStatistics = (TextView) findViewById(R.id.textViewStatistics);
        Typeface welcomeMessageFont = Typeface.createFromAsset(getAssets(), "fonts/atarifull.ttf");
        mTypeface = Typeface.createFromAsset(getAssets(), "fonts/atarifull.ttf");
        textViewStatistics.setTypeface(welcomeMessageFont);

        DataController dataController = new DataController(getApplicationContext());
        dataController.setDataReadyListener(new DataController.DataReadyListener() {
            @Override
            public void onDataReadyListener(PlayerModel player) {
                mPlayer = player;
                updateView(AI.Level.I);
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

        if (mPlayer == null){
            return;
        }

        AI.Level[] levels = new AI.Level[] {AI.Level.I, AI.Level.II, AI.Level.III, AI.Level.IMPOSSIBLE};
        updateView(levels[i]);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void updateView (AI.Level level){
        // Game Parts
        TextView textViewLevelGames = (TextView) findViewById(textViewLevelGames1);
        textViewLevelGames.setTypeface(mTypeface);
        textViewLevelGames.setText(mPlayer.getGameParts().get(String.valueOf(level)).toString());

        // Victory / Defeats
        TextView textViewLevelVictoryDefeates = (TextView) findViewById(R.id.textViewLevelVictoryDefeats1);
        textViewLevelVictoryDefeates.setTypeface(mTypeface);
        textViewLevelVictoryDefeates.setText(mPlayer.getVictories().get(String.valueOf(level)).toString()
                + "/" + mPlayer.getDefeats().get(String.valueOf(level)).toString());

        // Ratio
        TextView textViewLevelRatio = (TextView) findViewById(textViewLevelRatio1);
        textViewLevelRatio.setTypeface(mTypeface);
        textViewLevelRatio.setText(mPlayer.getRatio().get(String.valueOf(level)).toString() + "%");

        // Best Shots Count
        TextView textViewBestShotsCount = (TextView) findViewById(R.id.textViewLevelBestShotsCountValue);
        textViewBestShotsCount.setTypeface(mTypeface);
        int bestShotsCount = mPlayer.getBestShotsCount().get(String.valueOf(level));
        textViewBestShotsCount.setText(bestShotsCount == 2_147_483_647 ? "-" : String.valueOf(bestShotsCount));

        // Best Time
        TextView textViewLevelBestTime = (TextView) findViewById(R.id.textViewLevelBestTime1);
        textViewLevelBestTime.setTypeface(mTypeface);
        long bestTime = mPlayer.getBestTime().get(String.valueOf(level));
        textViewLevelBestTime.setText( bestTime == 2147483647 ? "-" : Utils.timeFormat(bestTime));

        // Time
        TextView textViewLevelTime = (TextView) findViewById(R.id.textViewLevelTime1);
        textViewLevelTime.setTypeface(mTypeface);
        long levelTime = mPlayer.getGameTime().get(String.valueOf(level));
        textViewLevelTime.setText(Utils.timeFormat(levelTime));

        // Total Time
        TextView textViewTotalTime = (TextView) findViewById(R.id.textViewLeveTotalTime);
        textViewTotalTime.setTypeface(mTypeface);
        long totalTime = mPlayer.getTotalGameTime();
        textViewTotalTime.setText(Utils.timeFormat(totalTime));
    }

    private static class MySpinnerAdapter extends ArrayAdapter<String> {

        Typeface mainFont = Typeface.createFromAsset(getContext().getAssets(), "fonts/atarifull.ttf");

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
            return spinnerText;
        }

    }

    public static final void setAppFont(ViewGroup mContainer, Typeface mFont)
    {
        if (mContainer == null || mFont == null) return;

        final int mCount = mContainer.getChildCount();

        // Loop through all of the children.
        for (int i = 0; i < mCount; ++i)
        {
            final View mChild = mContainer.getChildAt(i);
            if (mChild instanceof TextView)
            {
                // Set the font if it is a TextView.
                ((TextView) mChild).setTypeface(mFont);
            }
            else if (mChild instanceof ViewGroup)
            {
                // Recursively attempt another ViewGroup.
                setAppFont((ViewGroup) mChild, mFont);
            }
        }
    }

}


