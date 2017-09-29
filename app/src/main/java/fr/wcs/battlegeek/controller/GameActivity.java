package fr.wcs.battlegeek.controller;

import android.content.Context;
import android.content.Intent;
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

    private char[][] mMap = Maps.getMap();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        final Context mContext = getApplicationContext();

        // Get the Player Map previously created from the intent
        Intent intent = getIntent();

        char[][] mMapData = (char[][])intent.getExtras().getSerializable("mapData");
        // TODO: Store the map for Game's Enemy communication

        final GameView gameView = (GameView) findViewById(R.id.gameView);
        gameView.setOnPlayListener(new GameView.PlayListener() {
            @Override
            public void onPlayListener(int x, int y) {
                char result = mMap[y][x];
                if (result == ' ') {
                    gameView.setPlouf(x, y);
                    mMap[y][x] = '_';
                } else if (Character.isLowerCase(result) || result == '_') {
                    Toast.makeText(mContext, getString(R.string.alreadyPlayedMessage), Toast.LENGTH_SHORT).show();
                } else {
                    gameView.setTouch(x, y, result);
                    mMap[y][x] = Character.toLowerCase(result);
                    if(isDrown(result)) {
                        Toast.makeText(mContext, getString(R.string.itemDrownMessage), Toast.LENGTH_SHORT).show();
                    }
                    if(victory()) {
                        Toast.makeText(mContext, getString(R.string.victoryMessage), Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
    }

    private boolean isDrown(char symbol) {
        for(char[] row : mMap){
            for(char letter : row){
                if(symbol == letter) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean victory() {
        for(char[] row : mMap) {
            for(char symbol: row) {
                if(Character.isUpperCase(symbol)) {
                    return false;
                }
            }
        }
        return true;
    }

}
