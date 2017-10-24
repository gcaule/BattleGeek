package fr.wcs.battlegeek;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import fr.wcs.battlegeek.controller.AI;
import fr.wcs.battlegeek.controller.DataController;
import fr.wcs.battlegeek.controller.GameController;
import fr.wcs.battlegeek.controller.SoundController;
import fr.wcs.battlegeek.model.Bonus;
import fr.wcs.battlegeek.model.Maps;
import fr.wcs.battlegeek.model.PlayerModel;
import fr.wcs.battlegeek.model.Result;
import fr.wcs.battlegeek.model.Settings;
import fr.wcs.battlegeek.ui.EndGameDefeatFragment;
import fr.wcs.battlegeek.ui.EndGameVictoryFragment;
import fr.wcs.battlegeek.ui.GameView;
import fr.wcs.battlegeek.ui.MapView;
import fr.wcs.battlegeek.ui.QuitGameFragment;
import fr.wcs.battlegeek.utils.Utils;

import static fr.wcs.battlegeek.model.Bonus.Type.BOMB;
import static fr.wcs.battlegeek.model.Bonus.Type.MOVE;
import static fr.wcs.battlegeek.model.Bonus.Type.REPLAY;
import static fr.wcs.battlegeek.model.Result.Type.BONUS;
import static fr.wcs.battlegeek.model.Result.Type.DEFEATED;
import static fr.wcs.battlegeek.model.Result.Type.DROWN;
import static fr.wcs.battlegeek.model.Result.Type.MISSED;
import static fr.wcs.battlegeek.model.Result.Type.VICTORY;

/**
 * The Game Activity is handling the Game's Action and Views
 */
public class GameActivity extends AppCompatActivity {

    private final String TAG = Settings.TAG;

    private AI.Level mLevel;
    private PlayerModel mPlayer;
    private DataController mDataController;

    private AI mAI;
    private GameController mGameController;
    private boolean canPlay = true;
    private Bonus.Type mSelectedBonus = null;
    private boolean mAIShouldPlay = false;

    private Toast mToast;
    private Context mContext;
    private boolean mExit = false;
    private boolean mAlertDialogOpened = false;

    private MapView mMapView;
    private GameView mGameView;
    private Button mButtonRandomPosition;
    private ViewFlipper mViewFlipper;

    // Bonus
    private Button mButtonMove;
    private Button mButtonReplay;
    private Button mButtonCrossFire;

    private TextView mTextViewPlayer;
    private TextView mTextViewAI;
    private Button mButtonSwitchView;
    private Button mButtonLaunchGame;
    private ImageButton mImageButtonSpeed;
    private ImageButton mImageButtonMusic;
    private ImageButton mImageButtonEffects;
    private TextView mtextViewTimer;
    private ImageView mImageViewBlink;
    private AlphaAnimation mBlinkAnimation;
    private Vibrator mVibrator;

    private SharedPreferences mSharedPreferences;
    private int mAnimationsSpeed;


    private SoundController mSoundController;
    private int mVolumeMusic;
    private int mVolumeEffects;
    private int mShotsCounter = 0;

    private Timer mTimer;
    private long mTime = 0;
    private boolean mTimerPaused = false;

    private boolean mBlinkState;
    private boolean mVibrateState;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_game);

        Typeface mainFont = Typeface.createFromAsset(getAssets(), "fonts/openShip.otf");
        Typeface buttonFont = Typeface.createFromAsset(getAssets(), "fonts/emulogic.ttf");

        ColorFilter filterYellow = new LightingColorFilter(Color.YELLOW, Color.YELLOW);

        mContext = getApplicationContext();

        Intent intent = getIntent();
        mLevel = (AI.Level) intent.getSerializableExtra("Level");

        // Sound
        mSoundController = new SoundController(mContext);

        //Call SharedPref
        mSharedPreferences = getSharedPreferences(Settings.FILE_NAME, MODE_PRIVATE);

        // Get Player
        mDataController = new DataController(getApplicationContext());
        mDataController.setDataReadyListener(new DataController.DataReadyListener() {
            @Override
            public void onDataReadyListener(PlayerModel player) {
                mPlayer = player;
                Log.d(TAG, "onCreate: " + mPlayer.toString());
            }
        });

        // Settings
        mImageButtonSpeed = (ImageButton) findViewById(R.id.imageButtonSpeed);
        mImageButtonSpeed.setColorFilter(filterYellow);
        mAnimationsSpeed = mSharedPreferences.getInt(Settings.ANIMATION_TAG, Settings.ANIMATION_DEFAULT);
        setAnimationIcon(mAnimationsSpeed);
        mImageButtonSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAnimationsSpeed == Settings.ANIMATION_SLOW) {
                    mAnimationsSpeed = Settings.ANIMATION_MEDIUM;
                } else if (mAnimationsSpeed == Settings.ANIMATION_MEDIUM) {
                    mAnimationsSpeed = Settings.ANIMATION_FAST;
                } else if (mAnimationsSpeed == Settings.ANIMATION_FAST) {
                    mAnimationsSpeed = Settings.ANIMATION_SLOW;
                }
                mSharedPreferences.edit().putInt(Settings.ANIMATION_TAG, mAnimationsSpeed).apply();
                setAnimationIcon(mAnimationsSpeed);
            }
        });

        mImageButtonMusic = (ImageButton) findViewById(R.id.imageButtonMusic);
        mImageButtonMusic.setColorFilter(filterYellow);
        mVolumeMusic = mSharedPreferences.getInt(Settings.MUSIC_TAG, Settings.MUSIC_DEFAULT);

        setMusicIcon(mVolumeMusic);
        mImageButtonMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mVolumeMusic > 66) {
                    mVolumeMusic = 0;
                } else if (mVolumeMusic > 33) {
                    mVolumeMusic = 100;
                } else if (mVolumeMusic > 0) {
                    mVolumeMusic = 66;
                } else {
                    mVolumeMusic = 33;
                }
                mSharedPreferences.edit().putInt(Settings.MUSIC_TAG, mVolumeMusic).apply();
                setMusicIcon(mVolumeMusic);
                mSoundController.setMusicVolume(mVolumeMusic);
            }
        });

        mImageButtonEffects = (ImageButton) findViewById(R.id.imageButtonEffects);
        mImageButtonEffects.setColorFilter(filterYellow);
        mVolumeEffects = mSharedPreferences.getInt(Settings.EFFECTS_TAG, Settings.EFFECTS_DEFAULT);
        setEffectsIcon(mVolumeEffects);
        mImageButtonEffects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mVolumeEffects > 66) {
                    mVolumeEffects = 0;
                } else if (mVolumeEffects > 33) {
                    mVolumeEffects = 100;
                } else if (mVolumeEffects > 0) {
                    mVolumeEffects = 66;
                } else {
                    mVolumeEffects = 33;
                }
                mSharedPreferences.edit().putInt(Settings.EFFECTS_TAG, mVolumeEffects).apply();
                setEffectsIcon(mVolumeEffects);
                mSoundController.setEffectsVolume(mVolumeEffects);
            }
        });

        //Get Pref for Vibrate and Blink
        mBlinkState = mSharedPreferences.getBoolean(Settings.BLINK_TAG, true);
        mVibrateState = mSharedPreferences.getBoolean(Settings.VIBRATE_TAG, true);

        mMapView = (MapView) findViewById(R.id.mapView);
        mViewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        mTextViewPlayer = (TextView) findViewById(R.id.textViewPlayer);
        mTextViewAI = (TextView) findViewById(R.id.textViewAI);
        mButtonRandomPosition = (Button) findViewById(R.id.buttonRandomPositions);
        mButtonRandomPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMapView.setRandomPositions();
            }
        });
        mButtonSwitchView = (Button) findViewById(R.id.buttonSwitchView);

        mButtonLaunchGame = (Button) findViewById(R.id.buttonLaunchGame);

        mButtonLaunchGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start The Game
                if (mSelectedBonus == null) {
                    mButtonSwitchView.setVisibility(View.VISIBLE);
                    mButtonRandomPosition.setVisibility(View.GONE);
                    mTextViewPlayer.setText(R.string.player_turn);

                    mMapView = (MapView) findViewById(R.id.mapView);
                    char[][] mapData = mMapView.getMapData();
                    // Send map to Firebase
                    Maps.addMap(mapData);
                    mGameController = new GameController(mapData);
                    mGameController.setBonus();
                    mMapView.setMap(mGameController.getMap());
                    mMapView.setMode(MapView.Mode.PLAY);

                    mAI = new AI();
                    if (mLevel == AI.Level.III || mLevel == AI.Level.IMPOSSIBLE) {
                        mAI.setPlayerMap(mapData);
                        Log.d(TAG, "voir map: " + mapData);
                    }

                    if (mLevel == AI.Level.III) {
                        mGameView.setRandomColor(true);
                    }
                    mAI.setLevel(mLevel);

                    mButtonLaunchGame.setVisibility(View.INVISIBLE);
                    mTextViewAI.setTextColor(Color.parseColor("#FF960D"));

                    // Randomize first Player
                    int player = (int) (Math.random() * 2);
                    if (player % 2 == 0) {
                        canPlay = false;
                        aiPlay();
                    } else {
                        mViewFlipper.showNext();

                    }
                    // TODO remove Test Code Configuration
                    /*canPlay = false;
                    aiPlay();*/

                    mTextViewAI.setText(R.string.AITurn);
                    startTimer();
                }
                // Move Bonus
                else {
                    mSelectedBonus = null;
                    mButtonMove.setEnabled(false);
                    mButtonSwitchView.setVisibility(View.VISIBLE);
                    mViewFlipper.showNext();
                    mGameController.setMap(mMapView.getMapData());
                    mButtonLaunchGame.setVisibility(View.INVISIBLE);
                }
            }

        });

        mButtonSwitchView.setVisibility(View.GONE);

        mButtonSwitchView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mButtonSwitchView.setPressed(true);
                        mTextViewAI.setText(R.string.playermap_view);
                        mTextViewAI.setTextColor(Color.parseColor("#FFEE00"));
                        mViewFlipper.showPrevious();
                        break;
                    case MotionEvent.ACTION_UP:
                        mButtonSwitchView.setPressed(false);
                        mViewFlipper.showNext();
                        mTextViewAI.setVisibility(View.VISIBLE);
                        mTextViewAI.setText(" ");
                        mTextViewAI.setTextColor(Color.parseColor("#FF960D"));
                        break;
                }
                return true;
            }
        });

        mGameView = (GameView) findViewById(R.id.gameView);
        mGameView.setOnPlayListener(new GameView.PlayListener() {
            @Override
            public void onPlayListener(int x, int y) {

                if (!canPlay) {
                    return;
                }

                if (mGameController.alreadyPlayed(x, y)) {
                    showToast(R.string.alreadyPlayedMessage);
                    return;
                }

                playerPlay(x, y);
                mShotsCounter++;
            }
        });

        mtextViewTimer = (TextView) findViewById(R.id.textViewTimer);
        mtextViewTimer.setTypeface(mainFont);

        //Vibrator
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Blink
        mImageViewBlink = (ImageView) findViewById(R.id.imageViewBlink);
        mBlinkAnimation = new AlphaAnimation(0f, 0.7f);
        mBlinkAnimation.setDuration(70);
        mBlinkAnimation.setRepeatMode(Animation.RESTART);

        mBlinkAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mImageViewBlink.setAlpha(1f);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mImageViewBlink.setAlpha(0f);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mTextViewAI.setTypeface(mainFont);
        mTextViewPlayer.setTypeface(mainFont);

        mButtonRandomPosition.setTypeface(buttonFont);
        mButtonSwitchView.setTypeface(buttonFont);
        mButtonLaunchGame.setTypeface(buttonFont);

        mButtonMove = (Button) findViewById(R.id.buttonBonusMove);
        mButtonMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedBonus == null) {
                    mSelectedBonus = MOVE;
                    mButtonMove.setEnabled(false);
                    mViewFlipper.showPrevious();
                    mMapView.setMode(MapView.Mode.CREATE);
                    Log.d(TAG, "onClick: Settting Launch Button Visible");
                    mButtonLaunchGame.setVisibility(View.VISIBLE);
                    mButtonSwitchView.setVisibility(View.INVISIBLE);
                    mGameView.setDead(mSelectedBonus);
                }
                else {
                    showToast(R.string.multiBonusError);
                }
            }
        });

        mButtonReplay = (Button) findViewById(R.id.buttonBonusReplay);
        mButtonReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedBonus == null) {
                    mSelectedBonus = REPLAY;
                    mButtonReplay.setEnabled(false);
                    mGameView.setDead(mSelectedBonus);
                }
                else {
                    showToast(R.string.multiBonusError);
                }
            }
        });

        mButtonCrossFire = (Button) findViewById(R.id.buttonBonusCrossFire);
        mButtonCrossFire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedBonus == null) {
                    mSelectedBonus = BOMB;
                    mButtonCrossFire.setEnabled(false);
                    mGameView.setDead(mSelectedBonus);
                }
                else {
                    showToast(R.string.multiBonusError);
                }
            }
        });


        mGameView = (GameView) findViewById(R.id.gameView);
        mGameView.setOnPlayListener(new GameView.PlayListener() {
            @Override
            public void onPlayListener(int x, int y) {

                if (!canPlay) {
                    return;
                }

                if (mGameController.alreadyPlayed(x, y)) {
                    showToast(R.string.alreadyPlayedMessage);
                    return;
                }

                playerPlay(x, y);
                mShotsCounter++;
            }
        });

    }

    /**
     * Method handling the Player's Game Part
     *
     * @param x
     * @param y
     */
    private void playerPlay(int x, int y) {
        canPlay = false;

        Result.Type resultType = null;

        if (mSelectedBonus != BOMB) {
            Result result = mAI.shot(x, y);
            mGameController.setPlayResult(result);
            resultType = result.getType();
            showResult(result);
            mSoundController.playSound(resultType);
        }
        // CROSS FIRE Bonus
        else {
            // Deactivate bonus
            mSelectedBonus = null;

            ArrayList<Point> points = mGameController.getSurrondingcoordinates(x, y);
            // Get the results first
            ArrayList<Result> results = new ArrayList<>();
            for (Point point : points) {
                Result result = mAI.shot(point.x, point.y);
                mGameController.setPlayResult(result);
                results.add(result);
            }
            // Sort Result
            Collections.sort(results, Result.resultComparator);
            // Apply the Results
            // TODO: Animation
            for (Result result : results) {
                showResult(result);
                resultType = result.getType();
            }
            mSoundController.playSoundBigBomb();
        }

        if (mSelectedBonus == REPLAY && resultType == MISSED) {
            mSelectedBonus = null;
            canPlay = true;
        } else if (resultType == MISSED) {
            // Show the result
            new CountDownTimer(mAnimationsSpeed, mAnimationsSpeed / 3) {
                public void onTick(long millisUntilFinished) {
                }

                // Move to MapView and AI Turn
                public void onFinish() {
                    mTextViewPlayer.setText(R.string.player_turn);
                    mViewFlipper.showPrevious();
                    mAIShouldPlay = true;
                    aiPlay();
                }
            }.start();
        } else {
            canPlay = true;
        }
    }

    /**
     * Method displaying Result on Player Game View
     * @param result
     */
    private void showResult(Result result) {
        Result.Type resultType = result.getType();
        int x = result.getX();
        int y = result.getY();
        switch (resultType) {
            case BONUS:
                Bonus.Type bonusType = result.getBonusType();
                mGameView.setBonus(x, y, bonusType);
                switch (bonusType) {
                    case MOVE:
                        mButtonMove.setVisibility(View.VISIBLE);
                        mButtonMove.setEnabled(true);
                        break;
                    case REPLAY:
                        mButtonReplay.setVisibility(View.VISIBLE);
                        mButtonReplay.setEnabled(true);
                        break;
                    case BOMB:
                        mButtonCrossFire.setVisibility(View.VISIBLE);
                        mButtonCrossFire.setEnabled(true);
                        break;
                }
                break;

            case MISSED:
                mGameView.setPlouf(x, y);
                mTextViewPlayer.setText(R.string.missed);
                break;

            case TOUCHED:
                mGameView.setTouch(x, y, result.getShape());
                mTextViewPlayer.setText(R.string.touchedPlayAgain);
                break;

            case DROWN:
                mGameView.setTouch(x, y, result.getShape());
                mTextViewPlayer.setText(R.string.drownPlayAgain);
                showToast(R.string.itemDrownMessage);
                break;

            case VICTORY:
                mGameView.setTouch(x, y, result.getShape());
                mTimer.cancel();
                updatePlayerStatistics();
                FragmentManager fm = getFragmentManager();
                EndGameVictoryFragment endGameVictoryFragment = new EndGameVictoryFragment();
                endGameVictoryFragment.show(fm, String.valueOf(R.string.end_game_fragment_title));
                endGameVictoryFragment.setCancelable(false);
                break;
        }
    }

    /**
     * Method handling the AI's part
     */
    private void aiPlay() {
        mButtonSwitchView.setVisibility(View.GONE);
        mTextViewAI.setTextColor(Color.parseColor("#FF960D"));
        mTextViewAI.setText(R.string.AITurn);

        final Point aiPlayCoordinates = mAI.play();

        // Notify Player AI Using REPLAY Bonus
        if(mAI.getSelectedBonus() == REPLAY) {
            showToast(R.string.aiUseBonusReplay);
        }

        new CountDownTimer(mAnimationsSpeed * 3, mAnimationsSpeed) {
            private Result.Type resultType;
            private int cursor = 0;

            @Override
            public void onTick(long l) {
                if (cursor == 1) {
                    // We need to drop that Bomb ...
                    if(mAI.getSelectedBonus() == BOMB) {
                        ArrayList<Point> points = mAI.getGameController()
                                .getSurrondingcoordinates(aiPlayCoordinates.x, aiPlayCoordinates.y);
                        // Get the results first
                        ArrayList<Result> results = new ArrayList<>();
                        for (Point point : points) {
                            Result result = mGameController.shot(point.x, point.y);
                            results.add(result);
                        }
                        // Sort Result
                        Collections.sort(results, Result.resultComparator);
                        // Apply the Results
                        // TODO: Animation
                        for (Result result : results) {
                            mAI.setResult(result);
                            showAIResult(result);
                            resultType = result.getType();
                        }
                        mSoundController.playSoundBigBomb();
                    }
                    else {
                        Result aiResult = mGameController.shot(aiPlayCoordinates.x, aiPlayCoordinates.y);
                        resultType = aiResult.getType();
                        mAI.setResult(aiResult);
                        mSoundController.playSound(resultType);
                        showAIResult(aiResult);
                    }
                }
                cursor++;
            }

            @Override
            public void onFinish() {
                Bonus.Type aiSelectedBonus = mAI.getSelectedBonus();
                if (resultType == MISSED && aiSelectedBonus != REPLAY) {
                    mTextViewAI.setText(R.string.AITurn);
                    canPlay = true;
                    mViewFlipper.showPrevious();
                    mButtonSwitchView.setVisibility(View.VISIBLE);
                    mAIShouldPlay = false;

                }
                else if (resultType == VICTORY) {
                    mPlayer.addGameTime(mLevel, DEFEATED, mTime);
                    mPlayer.addDefeat(mLevel);
                    mDataController.updatePlayer(mPlayer);
                    mTimer.cancel();
                    FragmentManager fm = getFragmentManager();
                    EndGameDefeatFragment endGameDefeatFragment = new EndGameDefeatFragment();
                    endGameDefeatFragment.show(fm, String.valueOf(R.string.end_game_fragment_title));
                    endGameDefeatFragment.setCancelable(false);
                    mAIShouldPlay = false;
                    mSoundController.playMusicDefeat();
                } else if (!mExit) {
                    aiPlay();
                } else if (mExit) {
                    mAIShouldPlay = true;
                }
            }
        }.start();
    }

    /**
     * Method displaying AI shot result on the Player Map View
     * @param result
     */
    private void showAIResult(Result result) {
        Result.Type resultType = result.getType();

        if (resultType == MISSED) {
            mMapView.setPlouf(result.getX(), result.getY());
            mTextViewAI.setText(R.string.missed);
        }
        else if (resultType == BONUS) {
            mMapView.setDead(result.getX(), result.getY());
            switch (result.getBonusType()) {
                case BOMB:
                    mTextViewAI.setText(R.string.aiBonusCrossFire);
                    showToast(R.string.aiBonusCrossFire);
                    break;
                case REPLAY:
                    mTextViewAI.setText(R.string.aiBonusReplay);
                    showToast(R.string.aiBonusReplay);
                    break;
                case MOVE:
                    mTextViewAI.setText(R.string.aiBonusMove);
                    showToast(R.string.aiBonusMove);
                    break;
            }
        }
        else {
            mMapView.setDead(result.getX(), result.getY());
            mTextViewAI.setText(R.string.AITouched);
            blink(0);
            if (resultType == DROWN) {
                mTextViewAI.setText(R.string.AIDrown);
                blink(5);
            }
            if (resultType == VICTORY) {
                mTextViewAI.setText(R.string.AIVictory);
            }
        }
    }

    /**
     * Method handling Player Statistics Update
     */
    private void updatePlayerStatistics() {
        mPlayer.addGameTime(mLevel, VICTORY, mTime);
        mPlayer.addVictory(mLevel);
        mPlayer.addShotsCount(mLevel, mShotsCounter);
        mDataController.updatePlayer(mPlayer);
    }

    /**
     * Method stating the Timer
     */
    private void startTimer() {
        mTimerPaused = false;
        mtextViewTimer.setVisibility(View.VISIBLE);
        mtextViewTimer.setText("00:00");
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!mTimerPaused) {
                            mTime += 1000;
                            mtextViewTimer.setText(Utils.timeFormat(mTime));
                        }
                    }
                });
            }
        }, 1000, 1000);

    }

    /**
     * Method Showing a Toast, avoiding Latency
     *
     * @param stringResource
     */
    private void showToast(int stringResource) {
        if (mToast == null) {
            mToast = Toast.makeText(mContext, getString(stringResource), Toast.LENGTH_SHORT);
        }
        mToast.setText(getString(stringResource));
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * Method handling and showing the Icon's selection according to the Animation's speed
     * @param settingSpeed
     */
    private void setAnimationIcon(int settingSpeed) {
        if (settingSpeed == Settings.ANIMATION_SLOW) {
            mImageButtonSpeed.setImageResource(R.drawable.snail);
        } else if (settingSpeed == Settings.ANIMATION_MEDIUM) {
            mImageButtonSpeed.setImageResource(R.drawable.pigeon);
        } else if (settingSpeed == Settings.ANIMATION_FAST) {
            mImageButtonSpeed.setImageResource(R.drawable.rabbit);
        }
    }

    /**
     * Method Displaying the Music Icon for selected Volume
     * @param volume
     */
    private void setMusicIcon(int volume) {
        if (volume > 66) {
            mImageButtonMusic.setImageResource(R.drawable.music_loud);
        } else if (volume > 33) {
            mImageButtonMusic.setImageResource(R.drawable.music_medium);
        } else if (volume > 0) {
            mImageButtonMusic.setImageResource(R.drawable.music_low);
        } else {
            mImageButtonMusic.setImageResource(R.drawable.no_music);
        }
    }

    /**
     * Method Displaying the Effect Icon for selected Volume
     * @param volume
     */
    private void setEffectsIcon(int volume) {
        if (volume > 66) {
            mImageButtonEffects.setImageResource(R.drawable.volume_up_interface_symbol);
        } else if (volume > 33) {
            mImageButtonEffects.setImageResource(R.drawable.ic_volume_down_black_24dp);
        } else if (volume > 0) {
            mImageButtonEffects.setImageResource(R.drawable.ic_volume_mute_black_24dp);
        } else {
            mImageButtonEffects.setImageResource(R.drawable.ic_volume_off_black_24dp);
        }
    }

    /**
     * Method That try to kill epileptic People
     * @param repetitions
     */
    private void blink(int repetitions) {
        if (mBlinkState == true) {
            mBlinkAnimation.setRepeatCount(repetitions);
            mImageViewBlink.startAnimation(mBlinkAnimation);
        } else {
            mBlinkAnimation.setDuration(0);
        }
        //long[] timing = new long[] {70};
        //int[] amplitude = new int[] {VibrationEffect.DEFAULT_AMPLITUDE};
        //mVibrator.vibrate(VibrationEffect.createWaveform(timing, amplitude, repetitions));
        if (mVibrateState == true) {
            mVibrator.vibrate(70);
            if (Build.VERSION.SDK_INT >= 26) {
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(70L, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(70L);
            }
        } else {
            mVibrator.vibrate(0);
        }
    }

    /**
     * Method handling Back Button Pressed
     */
    @Override
    public void onBackPressed() {
        mAlertDialogOpened = true;
        mTimerPaused = true;
        pauseGame();
        FragmentManager fm = getFragmentManager();
        QuitGameFragment quitGameFragment = new QuitGameFragment();
        quitGameFragment.show(fm, String.valueOf(R.string.end_game_fragment_title));
        quitGameFragment.setCancelable(false);
        quitGameFragment.setOnCancelListener(new QuitGameFragment.OnCancelListener() {
            @Override
            public void onCancel() {
                mAlertDialogOpened = false;
                resumeGame();
            }
        });
    }

    /**
     * Method pausing the game
     */
    private void pauseGame() {
        mExit = true;
        mSoundController.pauseMusic();
        mSoundController.stopEffects();
    }

    /**
     * Method resuming the game
     */
    private void resumeGame() {
        if (!mAlertDialogOpened) {
            mTimerPaused = false;
            mExit = false;
            mSoundController.resumeMusic();
            if (mAIShouldPlay) {
                aiPlay();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeGame();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause() called");
        super.onPause();
        pauseGame();
        mTimerPaused = true;
    }
}