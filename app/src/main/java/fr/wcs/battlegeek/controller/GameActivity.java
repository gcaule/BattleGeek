package fr.wcs.battlegeek.controller;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import fr.wcs.battlegeek.Model.Result;
import fr.wcs.battlegeek.R;
import fr.wcs.battlegeek.ui.GameView;

import static fr.wcs.battlegeek.Model.Result.Type.VICTORY;

public class GameActivity extends AppCompatActivity {

    private String TAG = "CustomView";

    private AI mAI;
    private char[][] mStorageMap = new char[10][10];
    private GameController mGameControler;
    private boolean canPlay = true;

    private Toast mToast;
    private Context mContext;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
         mContext = getApplicationContext();
        // Get the Player Map previously created from the intent
        Intent intent = getIntent();

        char[][] mMap = (char[][])intent.getExtras().getSerializable("mapData");
        mGameControler = new GameController(mMap);
        mAI = new AI();

        final GameView gameView = (GameView) findViewById(R.id.gameView);
        gameView.setOnPlayListener(new GameView.PlayListener() {
            @Override
            public void onPlayListener(int x, int y) {

                if(!canPlay) {
                    return;
                }

                if(mGameControler.alreadyPlayed(x, y)) {
                    showToast(R.string.alreadyPlayedMessage);
                    return;
                }

                canPlay = false;

                Result result = mAI.shot(x, y);
                mGameControler.setResult(x, y, result);
                switch (result.getType()) {
                    case MISSED:
                        gameView.setPlouf(x, y);
                        break;
                    case TOUCHED:
                        gameView.setTouch(x, y, result.getShape());
                        break;
                    case DROWN:
                        gameView.setTouch(x, y, result.getShape());
                        showToast(R.string.itemDrownMessage);
                        break;
                    case VICTORY:
                        gameView.setTouch(x, y, result.getShape());
                        showToast(R.string.victoryMessage);
                        break;
                }

                // AI turn
                Point aiPlayCoordinates = mAI.play();
                Result iaResult = mGameControler.play(aiPlayCoordinates.x, aiPlayCoordinates.y);
                if(iaResult.getType() == VICTORY) {
                    showToast(R.string.defeatMessage);
                }
                mAI.setResult(iaResult);

                canPlay = true;
            }

        });
    }

    private void showToast(int stringResource) {
        if(mToast == null) {
            mToast = Toast.makeText(mContext, getString(stringResource), Toast.LENGTH_SHORT);
        }
        mToast.setText(getString(stringResource));
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }
}
