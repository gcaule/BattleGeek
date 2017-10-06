package fr.wcs.battlegeek;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import fr.wcs.battlegeek.Model.Settings;

public class MainMenuActivity extends AppCompatActivity {

    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_menu);

        ImageButton buttonSettings = (ImageButton) findViewById(R.id.buttonSettings);
        ImageButton buttonTrophy = (ImageButton) findViewById(R.id.buttonTrophy);
        Button buttonEasyMode = (Button) findViewById(R.id.buttonEasyMode);
        Button buttonMediumMode = (Button) findViewById(R.id.buttonMediumMode);
        Button buttonHardMode = (Button) findViewById(R.id.buttonHardMode);
        Button buttonImpossibleMode = (Button) findViewById(R.id.buttonImpossibleMode);
        TextView showPlayerName = (TextView) findViewById(R.id.show_playername);

        //Call SharedPref
        mSharedPreferences = getSharedPreferences(Settings.FILE_NAME, MODE_PRIVATE);

        //Get Pref for Player Name
        final String playerName = mSharedPreferences.getString("PlayerName", null);

        //Get Pref for Player Name
        showPlayerName.setText(playerName);


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

        buttonEasyMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
                intent.putExtra("Level", "Easy");
                startActivity(intent);
            }

        });

        buttonMediumMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
                intent.putExtra("Level", "Medium");
                startActivity(intent);
            }

        });

        buttonHardMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
                intent.putExtra("Level", "Hard");
                startActivity(intent);
            }

        });

        buttonImpossibleMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
                intent.putExtra("Level", "Impossible");
                startActivity(intent);
            }
        });

    }
}
