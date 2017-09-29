package fr.wcs.battlegeek;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        ImageButton buttonSettings = (ImageButton) findViewById(R.id.buttonSettings);
        ImageButton buttonTrophy = (ImageButton) findViewById(R.id.buttonTrophy);
        Button buttonEasyMode = (Button) findViewById(R.id.buttonEasyMode);
        Button buttonMediumMode = (Button) findViewById(R.id.buttonMediumMode);
        Button buttonHardMode = (Button) findViewById(R.id.buttonHardMode);

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
                Intent intent = new Intent(MainMenuActivity.this, CreateMapActivity.class);
                startActivity(intent);
            }

        });

        buttonMediumMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, CreateMapActivity.class);
                startActivity(intent);
            }

        });

        buttonHardMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, CreateMapActivity.class);
                startActivity(intent);
            }

        });

    }
}
