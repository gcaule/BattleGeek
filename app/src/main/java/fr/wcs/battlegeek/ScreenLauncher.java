package fr.wcs.battlegeek;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class ScreenLauncher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_launcher);

        /*
        // ajout d'une custom font au titre
        TextView homeTitle = (TextView) findViewById(R.id.home_title);
        Typeface policeTetris = Typeface.createFromAsset(getAssets(),"fonts/TETRIS.TTF");
        homeTitle.setTypeface(policeTetris);
        */


        new Timer().schedule(new TimerTask(){
            public void run() {
                ScreenLauncher.this.runOnUiThread(new Runnable() {
                    public void run() {
                        startActivity(new Intent(ScreenLauncher.this, MainMenuActivity.class));
                    }
                });
            }
        }, 2500);


    }
}
