package fr.wcs.battlegeek;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import fr.wcs.battlegeek.controller.AI;
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

        Typeface mainFont = Typeface.createFromAsset(getAssets(), "fonts/Curvy.ttf");
        Typeface buttonFont = Typeface.createFromAsset(getAssets(), "fonts/RaiderCrusaderHalf.ttf");

        ImageButton buttonSettings = (ImageButton) findViewById(R.id.buttonSettings);
        ImageButton buttonTrophy = (ImageButton) findViewById(R.id.buttonTrophy);
        ImageButton buttonStats = (ImageButton) findViewById(R.id.imageButtonStats);
        Button buttonEasyMode = (Button) findViewById(R.id.buttonEasyMode);
        Button buttonMediumMode = (Button) findViewById(R.id.buttonMediumMode);
        Button buttonHardMode = (Button) findViewById(R.id.buttonHardMode);
        Button buttonImpossibleMode = (Button) findViewById(R.id.buttonImpossibleMode);
        showPlayerName = (TextView) findViewById(R.id.show_playername);

        buttonEasyMode.setTypeface(buttonFont);
        buttonMediumMode.setTypeface(buttonFont);
        buttonHardMode.setTypeface(buttonFont);
        buttonImpossibleMode.setTypeface(buttonFont);
        showPlayerName.setTypeface(mainFont);

        buttonEasyMode.setTextColor(Color.parseColor("#FFF825"));
        buttonMediumMode.setTextColor(Color.parseColor("#FFF825"));
        buttonHardMode.setTextColor(Color.parseColor("#FFF825"));
        buttonImpossibleMode.setTextColor(Color.parseColor("#FFF825"));

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
        showPlayerName.setTextColor(Color.parseColor("#ff000000"));
        showPlayerName.setTextSize(26);
        showPlayerName.setText("Coucou " + playerName + " !");
    }
}
