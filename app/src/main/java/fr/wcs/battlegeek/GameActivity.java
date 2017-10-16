package fr.wcs.battlegeek;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
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

import java.util.Timer;
import java.util.TimerTask;

import fr.wcs.battlegeek.controller.AI;
import fr.wcs.battlegeek.controller.DataController;
import fr.wcs.battlegeek.controller.GameController;
import fr.wcs.battlegeek.controller.SoundController;
import fr.wcs.battlegeek.model.PlayerModel;
import fr.wcs.battlegeek.model.Result;
import fr.wcs.battlegeek.model.Settings;
import fr.wcs.battlegeek.ui.EndGameDefeatFragment;
import fr.wcs.battlegeek.ui.EndGameVictoryFragment;
import fr.wcs.battlegeek.ui.GameView;
import fr.wcs.battlegeek.ui.MapView;
import fr.wcs.battlegeek.ui.QuitGameFragment;
import fr.wcs.battlegeek.utils.Utils;

import static fr.wcs.battlegeek.R.id.viewFlipper;
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

    private Toast mToast;
    private Context mContext;
    private boolean mExit = false;

    private MapView mMapView;
    private GameView mGameView;
    private ViewFlipper mViewFlipper;
    private TextView mTextViewPlayer;
    private TextView mTextViewAI;
    private Button mButtonSwitchView;
    private Button mButtonRandomPosition;
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
    private long mStartTime;
    private int mShotsCounter = 0;
    private Timer mTimer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_game);

        mContext = getApplicationContext();

        Intent intent = getIntent();
        mLevel = (AI.Level) intent.getSerializableExtra("Level");

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
        mAnimationsSpeed = mSharedPreferences.getInt(Settings.ANIMATION_TAG, Settings.ANIMATION_DEFAULT);
        setAnimationIcon(mAnimationsSpeed);
        mImageButtonSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mAnimationsSpeed == Settings.ANIMATION_SLOW) {
                    mAnimationsSpeed = Settings.ANIMATION_MEDIUM;
                }
                else if(mAnimationsSpeed == Settings.ANIMATION_MEDIUM) {
                    mAnimationsSpeed = Settings.ANIMATION_FAST;
                }
                else if(mAnimationsSpeed == Settings.ANIMATION_FAST) {
                    mAnimationsSpeed = Settings.ANIMATION_SLOW;
                }
                mSharedPreferences.edit().putInt(Settings.ANIMATION_TAG, mAnimationsSpeed).apply();
                setAnimationIcon(mAnimationsSpeed);
            }
        });

        // Sound
        mSoundController = new SoundController(mContext);

        mImageButtonMusic = (ImageButton) findViewById(R.id.imageButtonMusic);
        mVolumeMusic = mSharedPreferences.getInt(Settings.MUSIC_TAG, Settings.MUSIC_DEFAULT);

        // Play Music
        mSoundController.playMusic();

        setMusicIcon(mVolumeMusic);
        mImageButtonMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mVolumeMusic > 66) {
                    mVolumeMusic = 0;
                }
                else if(mVolumeMusic > 33) {
                    mVolumeMusic = 100;
                }
                else if(mVolumeMusic > 0) {
                    mVolumeMusic = 66;
                }
                else {
                    mVolumeMusic = 33;
                }
                mSharedPreferences.edit().putInt(Settings.MUSIC_TAG, mVolumeMusic).apply();
                setMusicIcon(mVolumeMusic);
                mSoundController.setMusicVolume(mVolumeMusic);
            }
        });

        mImageButtonEffects = (ImageButton) findViewById(R.id.imageButtonEffects);
        mVolumeEffects = mSharedPreferences.getInt(Settings.EFFECTS_TAG, Settings.EFFECTS_DEFAULT);
        setEffectsIcon(mVolumeEffects);
        mImageButtonEffects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mVolumeEffects > 66) {
                    mVolumeEffects = 0;
                }
                else if(mVolumeEffects > 33) {
                    mVolumeEffects = 100;
                }
                else if(mVolumeEffects > 0) {
                    mVolumeEffects = 66;
                }
                else {
                    mVolumeEffects = 33;
                }
                mSharedPreferences.edit().putInt(Settings.EFFECTS_TAG, mVolumeEffects).apply();
                setEffectsIcon(mVolumeEffects);
                mSoundController.setEffectsVolume(mVolumeEffects);
            }
        });

        mMapView = (MapView) findViewById(R.id.mapView);
        mViewFlipper = (ViewFlipper) findViewById(viewFlipper);
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

        final Button buttonLaunchGame = (Button) findViewById(R.id.buttonLaunchGame);

        buttonLaunchGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mButtonSwitchView.setVisibility(View.VISIBLE);
                mButtonRandomPosition.setVisibility(View.GONE);
                mTextViewPlayer.setText(R.string.player_turn);
                mMapView = (MapView) findViewById(R.id.mapView);
                char[][] mapData = mMapView.getMapData();
                mGameController = new GameController(mapData);
                mMapView.setMode(MapView.Mode.PLAY);

                mAI = new AI();
                if (mLevel == AI.Level.III || mLevel == AI.Level.IMPOSSIBLE) {
                    mAI.setPlayerMap(mapData);
                }
                mAI.setLevel(mLevel);

                buttonLaunchGame.setVisibility(View.GONE);
                mTextViewAI.setTextColor(Color.parseColor("#FF960D"));

                // Randomize first Player
                int player = (int)(Math.random() * 2);
                if(player % 2 == 0) {
                    canPlay = false;
                    aiPlay();
                }
                else {
                    mViewFlipper.showNext();

                }

                mTextViewAI.setText(R.string.AITurn);
                mStartTime = System.currentTimeMillis();
                startTimer();
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

        Typeface mainFont = Typeface.createFromAsset(getAssets(), "fonts/Curvy.ttf");
        Typeface buttonFont = Typeface.createFromAsset(getAssets(), "fonts/DirtyClassicMachine.ttf");

        TextView titleMessage = (TextView) findViewById(R.id.textViewSettings);

        mtextViewTimer = (TextView) findViewById(R.id.textViewTimer);

        //Vibrator
        mVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        // Blink
        mImageViewBlink = (ImageView) findViewById(R.id.imageViewBlink);
        mBlinkAnimation= new AlphaAnimation(0f, 0.7f);
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
        buttonLaunchGame.setTypeface(buttonFont);

    }

    /**
     * Method handling the Player's Game Part
     *
     * @param x
     * @param y
     */
    private void playerPlay(int x, int y) {
        canPlay = false;

        Result result = mAI.shot(x, y);
        mGameController.setPlayResult(x, y, result);
        Result.Type resutlType = result.getType();
        mSoundController.playSound(resutlType);
        switch (resutlType) {
            case TOUCHED:
                mGameView.setTouch(x, y, result.getShape());
                mTextViewPlayer.setText(R.string.touchedPlayAgain);
                canPlay = true;
                return;
            case DROWN:
                mGameView.setTouch(x, y, result.getShape());
                mTextViewPlayer.setText(R.string.drownPlayAgain);
                showToast(R.string.itemDrownMessage);
                canPlay = true;
                return;
            case VICTORY:
                mGameView.setTouch(x, y, result.getShape());
                mPlayer.addGameTime(mLevel, VICTORY, getPlayedTime());
                mPlayer.addVictory(mLevel);
                mPlayer.addShotsCount(mLevel, mShotsCounter);
                mTimer.cancel();
                mDataController.updatePlayer(mPlayer);
                FragmentManager fm = getFragmentManager();
                EndGameVictoryFragment endGameVictoryFragment = new EndGameVictoryFragment();
                endGameVictoryFragment.show(fm, String.valueOf(R.string.end_game_fragment_title));
                endGameVictoryFragment.setCancelable(false);
                return;
            case MISSED:
                mGameView.setPlouf(x, y);
                mTextViewPlayer.setText(R.string.missed);
                // Show the result
                new CountDownTimer(mAnimationsSpeed, mAnimationsSpeed / 3) {
                    public void onTick(long millisUntilFinished) {
                    }

                    // Move to MapView and AI Turn
                    public void onFinish() {
                        mTextViewPlayer.setText(R.string.player_turn);
                        mViewFlipper.showPrevious();
                        aiPlay();
                    }
                }.start();

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
        final Result iaResult = mGameController.shot(aiPlayCoordinates.x, aiPlayCoordinates.y);
        //Utils.printMap(mGameController.getMap());
        final Result.Type resultType = iaResult.getType();
        Log.d(TAG, "onPlayListener: " + aiPlayCoordinates + " " + iaResult);

        mAI.setResult(iaResult);


        new CountDownTimer(mAnimationsSpeed * 3, mAnimationsSpeed) {

            private int cursor = 0;

            @Override
            public void onTick(long l) {
                if (cursor == 1) {
                    mSoundController.playSound(resultType);
                    if (resultType == MISSED) {
                        mMapView.setPlouf(aiPlayCoordinates.x, aiPlayCoordinates.y);
                        mTextViewAI.setText(R.string.missed);
                    } else {
                        mMapView.setDead(aiPlayCoordinates.x, aiPlayCoordinates.y);
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
                cursor++;
            }

            @Override
            public void onFinish() {

                mButtonSwitchView.setVisibility(View.VISIBLE);
                if (resultType == MISSED) {
                    mTextViewAI.setText(R.string.AITurn);
                    canPlay = true;
                    mViewFlipper.showPrevious();
                }
                else if(resultType == VICTORY){
                    mPlayer.addGameTime(mLevel, DEFEATED, getPlayedTime());
                    mPlayer.addDefeat(mLevel);
                    mDataController.updatePlayer(mPlayer);
                    mTimer.cancel();
                    FragmentManager fm = getFragmentManager();
                    EndGameDefeatFragment endGameDefeatFragment = new EndGameDefeatFragment();
                    endGameDefeatFragment.show(fm, String.valueOf(R.string.end_game_fragment_title));
                    endGameDefeatFragment.setCancelable(false);
                }

                else if(!mExit){
                    aiPlay();
                }

            }
        }.start();
    }

    private long getPlayedTime() {
        return System.currentTimeMillis() - mStartTime;
    }

    private void startTimer() {
        mtextViewTimer.setVisibility(View.VISIBLE);
        mtextViewTimer.setText("00:00");
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        long time = System.currentTimeMillis() - mStartTime;
                        mtextViewTimer.setText(Utils.timeFormat(time));
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

    private void setAnimationIcon(int settingSpeed) {
        if (settingSpeed == Settings.ANIMATION_SLOW) {
            mImageButtonSpeed.setImageResource(R.drawable.snail);
        }
        else if (settingSpeed == Settings.ANIMATION_MEDIUM) {
            mImageButtonSpeed.setImageResource(R.drawable.pigeon);
        }
        else if (settingSpeed == Settings.ANIMATION_FAST) {
            mImageButtonSpeed.setImageResource(R.drawable.rabbit);
        }
    }

    private void setMusicIcon(int volume) {
        if(volume > 66) {
            mImageButtonMusic.setImageResource(R.drawable.music_loud);
        }
        else if(volume > 33) {
            mImageButtonMusic.setImageResource(R.drawable.music_medium);
        }
        else if(volume > 0) {
            mImageButtonMusic.setImageResource(R.drawable.music_low);
        }
        else {
            mImageButtonMusic.setImageResource(R.drawable.no_music);
        }
    }

    private void setEffectsIcon(int volume) {
        if(volume > 66) {
            mImageButtonEffects.setImageResource(R.drawable.volume_up_interface_symbol);
        }
        else if(volume > 33) {
            mImageButtonEffects.setImageResource(R.drawable.ic_volume_down_black_24dp);
        }
        else if(volume > 0) {
            mImageButtonEffects.setImageResource(R.drawable.ic_volume_mute_black_24dp);
        }
        else {
            mImageButtonEffects.setImageResource(R.drawable.ic_volume_off_black_24dp);
        }
    }

    private void blink(int repetitions) {
        mBlinkAnimation.setRepeatCount(repetitions);
        mImageViewBlink.startAnimation(mBlinkAnimation);
        //long[] timing = new long[] {70};
        //int[] amplitude = new int[] {VibrationEffect.DEFAULT_AMPLITUDE};
        //mVibrator.vibrate(VibrationEffect.createWaveform(timing, amplitude, repetitions));
        mVibrator.vibrate(70);
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(70L, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(70L);
        }
    }

    /**
     * Method handling Back Button Pressed
     */
    @Override
    public void onBackPressed() {

        FragmentManager fm = getFragmentManager();
        QuitGameFragment quitGameFragment = new QuitGameFragment();
        quitGameFragment.show(fm, String.valueOf(R.string.end_game_fragment_title));
        quitGameFragment.setCancelable(false);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mSoundController.stopMusic();
        mSoundController.stopEffects();
        mExit = true;
    }
}