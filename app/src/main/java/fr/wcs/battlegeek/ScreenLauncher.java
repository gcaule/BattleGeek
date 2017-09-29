package fr.wcs.battlegeek;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class ScreenLauncher extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_screen_launcher);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

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
