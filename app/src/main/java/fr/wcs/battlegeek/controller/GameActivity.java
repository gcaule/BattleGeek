package fr.wcs.battlegeek.controller;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import fr.wcs.battlegeek.Model.Result;
import fr.wcs.battlegeek.R;
import fr.wcs.battlegeek.ui.EndGameDefeatFragment;
import fr.wcs.battlegeek.ui.EndGameVictoryFragment;
import fr.wcs.battlegeek.ui.GameView;
import fr.wcs.battlegeek.ui.Tetromino;

import static fr.wcs.battlegeek.Model.Result.Type.DROWN;
import static fr.wcs.battlegeek.Model.Result.Type.MISSED;
import static fr.wcs.battlegeek.Model.Result.Type.VICTORY;
import static fr.wcs.battlegeek.R.layout.activity_game;

public class GameActivity extends FragmentActivity {

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
        setContentView(activity_game);
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

                if(alreadyPlayed(x, y)) {
                    showToast(R.string.alreadyPlayedMessage);
                    return;
                }

                canPlay = false;

                Result result = mAI.shot(x, y);
                Result.Type resultType = result.getType();
                Tetromino.Shape resultShape = result.getShape();

                if(resultType == MISSED) {
                    gameView.setPlouf(x, y);
                    mStorageMap[y][x] = '_';
                }
                else {
                    gameView.setTouch(x, y, resultShape);
                    mStorageMap[y][x] = Character.toLowerCase(resultShape.toString().charAt(0));
                    if(resultType == DROWN) {
                        showToast(R.string.itemDrownMessage);
                    }

                    if(resultType == VICTORY) {
                        FragmentManager fm = getFragmentManager();
                        EndGameVictoryFragment endGameVictoryFragment = new EndGameVictoryFragment();
                        endGameVictoryFragment.show(fm, String.valueOf(R.string.end_game_fragment_title));
                        endGameVictoryFragment.setCancelable(false);
                        return;
                    }
                }

                // AI turn
                Point aiPlayCoordinates = mAI.play();
                Result iaResult = mGameControler.play(aiPlayCoordinates.x, aiPlayCoordinates.y);
                if(iaResult.getType() == VICTORY) {
                    FragmentManager fm = getFragmentManager();
                    EndGameDefeatFragment endGameDefeatFragment = new EndGameDefeatFragment();
                    endGameDefeatFragment.show(fm, String.valueOf(R.string.end_game_fragment_title));
                    endGameDefeatFragment.setCancelable(false);
                }
                mAI.setResult(iaResult);

                canPlay = true;
            }

        });
    }

    public boolean alreadyPlayed(int x, int y) {
        char symbol = mStorageMap[y][x];
        Log.d(TAG, "alreadyPlayed() called with: x = [" + x + "], y = [" + y + "] symbol : " + symbol);
        return symbol == '_' || Character.isLowerCase(symbol);
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