package fr.wcs.battlegeek.controller;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import fr.wcs.battlegeek.Model.Maps;
import fr.wcs.battlegeek.R;
import fr.wcs.battlegeek.ui.GameView;

public class GameActivity extends AppCompatActivity {

    private String TAG = "CustomView";
    private Toast toast;
    
    private char[][] mMap = Maps.getMap();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        
        final GameView gameView = (GameView) findViewById(R.id.gameView);
        gameView.setOnPlayListener(new GameView.PlayListener() {
            @Override
            public void onPlayListener(int x, int y) {
                char result = mMap[y][x];
                if (result == ' ') {
                    gameView.setPlouf(x, y);
                    mMap[y][x] = '_';
                } else if (Character.isLowerCase(result) || result == '_') {
                    Toast.makeText(getApplicationContext(), "Heu... Ca sert à rien ça ...", Toast.LENGTH_SHORT).show();
                } else {
                    gameView.setTouch(x, y, result);
                    mMap[y][x] = Character.toLowerCase(result);
                }
            }

        });
    }



    private boolean victory() {
        return false;
    }

}
