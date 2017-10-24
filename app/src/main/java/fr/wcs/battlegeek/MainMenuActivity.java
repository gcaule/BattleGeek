package fr.wcs.battlegeek;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import fr.wcs.battlegeek.controller.AI;
import fr.wcs.battlegeek.model.Maps;
import fr.wcs.battlegeek.model.Settings;

public class MainMenuActivity extends AppCompatActivity {

    private final String TAG = Settings.TAG;
    SharedPreferences mSharedPreferences;
    TextView showPlayerName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_menu);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        ColorFilter filter = new LightingColorFilter( Color.YELLOW, Color.YELLOW);

        Typeface mainFont = Typeface.createFromAsset(getAssets(), "fonts/emulogic.ttf");

        // Init Maps with Firebase
        Maps.init();

        ImageButton buttonRules = (ImageButton) findViewById(R.id.buttonRules);
        ImageButton buttonSettings = (ImageButton) findViewById(R.id.buttonSettings);
        ImageButton buttonTrophy = (ImageButton) findViewById(R.id.buttonTrophy);
        ImageButton buttonStats = (ImageButton) findViewById(R.id.imageButtonStats);
        Button buttonEasyMode = (Button) findViewById(R.id.buttonEasyMode);
        Button buttonMediumMode = (Button) findViewById(R.id.buttonMediumMode);
        Button buttonHardMode = (Button) findViewById(R.id.buttonHardMode);
        Button buttonImpossibleMode = (Button) findViewById(R.id.buttonImpossibleMode);
        showPlayerName = (TextView) findViewById(R.id.show_playername);

        buttonRules.setColorFilter(filter);
        buttonSettings.setColorFilter(filter);
        buttonTrophy.setColorFilter(filter);
        buttonStats.setColorFilter(filter);

        buttonEasyMode.setTypeface(mainFont);
        buttonMediumMode.setTypeface(mainFont);
        buttonHardMode.setTypeface(mainFont);
        buttonImpossibleMode.setTypeface(mainFont);
        showPlayerName.setTypeface(mainFont);

        buttonEasyMode.setTextColor(Color.parseColor("#FFEE00"));
        buttonMediumMode.setTextColor(Color.parseColor("#FFEE00"));
        buttonHardMode.setTextColor(Color.parseColor("#FFEE00"));
        buttonImpossibleMode.setTextColor(Color.parseColor("#FFEE00"));

        buttonRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, GameRulesActivity.class);
                startActivity(intent);
            }

        });

        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, SettingsActivity.class);
                startActivity(intent);
            }

        });

        buttonTrophy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, RankingActivity.class);
                startActivity(intent);
            }

        });

        buttonStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, StatisticsActivity.class);
                startActivity(intent);
            }
        });

        buttonEasyMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
                intent.putExtra("Level", AI.Level.I);
                startActivity(intent);
            }

        });

        buttonMediumMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
                intent.putExtra("Level", AI.Level.II);
                startActivity(intent);
            }

        });

        buttonHardMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
                intent.putExtra("Level", AI.Level.III);
                startActivity(intent);
            }

        });

        buttonImpossibleMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
                intent.putExtra("Level", AI.Level.IMPOSSIBLE);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Call SharedPref
        mSharedPreferences = getSharedPreferences(Settings.FILE_NAME, MODE_PRIVATE);
        String playerName = mSharedPreferences.getString(Settings.PLAYER_NAME, null);

        showPlayerName.setTextColor(Color.parseColor("#FFF825"));
        showPlayerName.setText("Coucou " + playerName + " !");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
