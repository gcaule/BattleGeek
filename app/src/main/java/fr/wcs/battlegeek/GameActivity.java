package fr.wcs.battlegeek;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ViewFlipper;

import fr.wcs.battlegeek.controller.AI;
import fr.wcs.battlegeek.controller.GameController;
import fr.wcs.battlegeek.model.Result;
import fr.wcs.battlegeek.ui.GameView;
import fr.wcs.battlegeek.ui.MapView;

import static fr.wcs.battlegeek.R.id.viewFlipper;
import static fr.wcs.battlegeek.model.Result.Type.VICTORY;

public class GameActivity extends AppCompatActivity {

    private AI mAI;
    private GameController mGameControler;
    private boolean canPlay = true;

    private Toast mToast;
    private Context mContext;

    private MapView mMapView;
    private GameView mGameView;
    private ViewFlipper mViewFlipper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        mContext = getApplicationContext();
        mViewFlipper = (ViewFlipper) findViewById(viewFlipper);
        Button buttonLaunchGame = (Button) findViewById(R.id.buttonLaunchGame);

        buttonLaunchGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapView = (MapView) findViewById(R.id.mapView);
                char[][] mapData = mMapView.getMapData();
                mGameControler = new GameController(mapData);
                mAI = new AI();
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

                if(mGameControler.alreadyPlayed(x, y)) {
                    showToast(R.string.alreadyPlayedMessage);
                    return;
                }

                canPlay = false;

                Result result = mAI.shot(x, y);
                mGameControler.setResult(x, y, result);
                switch (result.getType()) {
                    case MISSED:
                        mGameView.setPlouf(x, y);
                        break;
                    case TOUCHED:
                        mGameView.setTouch(x, y, result.getShape());
                        break;
                    case DROWN:
                        mGameView.setTouch(x, y, result.getShape());
                        showToast(R.string.itemDrownMessage);
                        break;
                    case VICTORY:
                        mGameView.setTouch(x, y, result.getShape());
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
