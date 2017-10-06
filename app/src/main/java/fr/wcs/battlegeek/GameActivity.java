package fr.wcs.battlegeek;

import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewFlipper;

import fr.wcs.battlegeek.controller.AI;
import fr.wcs.battlegeek.controller.GameController;
import fr.wcs.battlegeek.model.Result;
import fr.wcs.battlegeek.ui.EndGameDefeatFragment;
import fr.wcs.battlegeek.ui.EndGameVictoryFragment;
import fr.wcs.battlegeek.ui.GameView;
import fr.wcs.battlegeek.ui.MapView;

import static fr.wcs.battlegeek.R.id.viewFlipper;
import static fr.wcs.battlegeek.model.Result.Type.MISSED;
import static fr.wcs.battlegeek.model.Result.Type.VICTORY;

public class GameActivity extends AppCompatActivity {

    private final String TAG = "GameActivity";

    private AI mAI;
    private GameController mGameController;
    private boolean canPlay = true;

    private Toast mToast;
    private Context mContext;

    private MapView mMapView;
    private GameView mGameView;
    private ViewFlipper mViewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);

        mContext = getApplicationContext();
        mViewFlipper = (ViewFlipper) findViewById(viewFlipper);
        final Button buttonLaunchGame = (Button) findViewById(R.id.buttonLaunchGame);

        buttonLaunchGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView = (MapView) findViewById(R.id.mapView);
                char[][] mapData = mMapView.getMapData();
                mGameController = new GameController(mapData);
                mMapView.setMode(MapView.Mode.PLAY);
                mAI = new AI();
                buttonLaunchGame.setVisibility(View.GONE);
                mViewFlipper.showNext();
            }

        });

        mGameView = (GameView) findViewById(R.id.gameView);
        mGameView.setOnPlayListener(new GameView.PlayListener() {
            @Override
            public void onPlayListener(int x, int y) {

                if(!canPlay) {
                    return;
                }

                if(mGameController.alreadyPlayed(x, y)) {
                    showToast(R.string.alreadyPlayedMessage);
                    return;
                }

                playerPlay(x, y);
            }
        });
    }

    private void playerPlay(int x, int y) {
        canPlay = false;

        Result result = mAI.shot(x, y);
        mGameController.setPlayResult(x, y, result);
        switch (result.getType()) {
            case TOUCHED:
                mGameView.setTouch(x, y, result.getShape());
                canPlay = true;
                return;
            case DROWN:
                mGameView.setTouch(x, y, result.getShape());
                showToast(R.string.itemDrownMessage);
                canPlay = true;
                return;
            case VICTORY:
                mGameView.setTouch(x, y, result.getShape());
                FragmentManager fm = getFragmentManager();
                EndGameVictoryFragment endGameVictoryFragment = new EndGameVictoryFragment();
                endGameVictoryFragment.show(fm, String.valueOf(R.string.end_game_fragment_title));
                endGameVictoryFragment.setCancelable(false);
                return;
            case MISSED:
                mGameView.setPlouf(x, y);
                // Show the result
                new CountDownTimer(650, 500) {
                    public void onTick(long millisUntilFinished) {
                    }

                    // Move to MapView and AI Turn
                    public void onFinish() {
                        mViewFlipper.showPrevious();
                        aiPlay();
                    }
                }.start();

                break;
        }
    }

    private void aiPlay() {
        final Point aiPlayCoordinates = mAI.play();
        final Result iaResult = mGameController.shot(aiPlayCoordinates.x, aiPlayCoordinates.y);
        final Result.Type resultType = iaResult.getType();
        Log.d(TAG, "onPlayListener: " + aiPlayCoordinates + " " + iaResult);

        if(resultType == VICTORY) {
            FragmentManager fm = getFragmentManager();
            EndGameDefeatFragment endGameDefeatFragment = new EndGameDefeatFragment();
            endGameDefeatFragment.show(fm, String.valueOf(R.string.end_game_fragment_title));
            endGameDefeatFragment.setCancelable(false);
        }

        mAI.setResult(iaResult);

        new CountDownTimer(1750, 350) {
            private int cursor = 0;
            @Override
            public void onTick(long l) {
                if(cursor == 1) {
                    if(resultType == MISSED) {
                        mMapView.setPlouf(aiPlayCoordinates.x, aiPlayCoordinates.y);
                    }
                    else {
                        mMapView.setDead(aiPlayCoordinates.x, aiPlayCoordinates.y);
                    }
                }
                cursor++;
            }
            @Override
            public void onFinish() {
                if(resultType == MISSED) {
                    canPlay = true;
                    mViewFlipper.showPrevious();
                }
                else aiPlay();
            }
        }.start();
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
