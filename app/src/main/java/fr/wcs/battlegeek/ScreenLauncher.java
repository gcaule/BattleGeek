package fr.wcs.battlegeek;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.facebook.stetho.Stetho;

import java.util.Timer;
import java.util.TimerTask;

import fr.wcs.battlegeek.model.Maps;
import fr.wcs.battlegeek.model.Settings;

import static android.view.View.GONE;

public class ScreenLauncher extends AppCompatActivity {

    private SharedPreferences mSharedPreferences;
    private Timer mTimer;
    private boolean shouldRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_screen_launcher);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        Typeface homeFont = Typeface.createFromAsset(getAssets(), "fonts/emulogic.ttf");
        Typeface sloganFont = Typeface.createFromAsset(getAssets(), "fonts/atarifull.ttf");

        TextView homeTitle = (TextView) findViewById(R.id.home_title);
        TextView homeSlogan = (TextView) findViewById(R.id.home_slogan);
        homeSlogan.setVisibility(GONE);
        homeTitle.setTypeface(homeFont);
        homeSlogan.setTypeface(sloganFont);

        TitleAnimation();

        // Init Maps with Firebase
        Maps.initMaps();

        //Access Internal files, preferences and DB of the APP via Chrome : chrome://inspect/#devices
        Stetho.initializeWithDefaults(this);

        //Call SharedPref
        mSharedPreferences = getSharedPreferences(Settings.FILE_NAME, MODE_PRIVATE);

        //Get Pref for PlayerModel Name
        final String uid = mSharedPreferences.getString(Settings.UID, null);
        shouldRegister = uid == null;

        //Si Playername dans sharedpref vide, allez sur register. Sinon allez à MainActiv
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            public void run() {
                ScreenLauncher.this.runOnUiThread(new Runnable() {
                    public void run() {
                       if (shouldRegister) {

                            startActivity(new Intent(ScreenLauncher.this, FirstTimeUsernameScreen.class));
                        } else {
                            startActivity(new Intent(ScreenLauncher.this, MainMenuActivity.class));
                        }

                    }
                });
            }
        }, 10000);

        View screen = findViewById(R.id.layoutScreenLauncher);
        screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!shouldRegister) {
                    startActivity(new Intent(ScreenLauncher.this, MainMenuActivity.class));
                    mTimer.cancel();
                }
            }
        });
    }

    private void TitleAnimation() {
        Animation titleAnimation = AnimationUtils.loadAnimation(this, R.anim.scale);
        titleAnimation.reset();
        TextView homeTitle = (TextView) findViewById(R.id.home_title);
        homeTitle.clearAnimation();
        titleAnimation.setFillAfter(true);
        homeTitle.startAnimation(titleAnimation);

        titleAnimation.setAnimationListener(new Animation.AnimationListener() {

            Animation sloganAnimation = AnimationUtils.loadAnimation(ScreenLauncher.this, R.anim.fadein);

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                TextView homeSlogan = (TextView) findViewById(R.id.home_slogan);
                homeSlogan.setVisibility(View.VISIBLE);
                sloganAnimation = AnimationUtils.loadAnimation(ScreenLauncher.this, R.anim.fadein);
                sloganAnimation.setFillAfter(true);
                homeSlogan.startAnimation(sloganAnimation);
            }
        });
    }

}