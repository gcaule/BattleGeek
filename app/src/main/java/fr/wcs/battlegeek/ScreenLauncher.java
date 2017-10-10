package fr.wcs.battlegeek;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.facebook.stetho.Stetho;

import java.util.Timer;
import java.util.TimerTask;

import fr.wcs.battlegeek.Model.Settings;

public class ScreenLauncher extends AppCompatActivity {

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_screen_launcher);

        TextView myTextView = (TextView) findViewById(R.id.home_title);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/RockOn.otf");
        myTextView.setTypeface(typeface);

        RunAnimation();

        //Access Internal files, preferences and DB of the APP via Chrome : chrome://inspect/#devices
        Stetho.initializeWithDefaults(this);

        //Call SharedPref
        mSharedPreferences = getSharedPreferences(Settings.FILE_NAME, MODE_PRIVATE);

        //Get Pref for Player Name
        final String playerName = mSharedPreferences.getString("PlayerName", null);

        //Si Playername dans sharedpref vide, allez sur register. Sinon allez à MainActiv
        new Timer().schedule(new TimerTask() {
            public void run() {
                ScreenLauncher.this.runOnUiThread(new Runnable() {
                    public void run() {
                        if (playerName == null) {
                            startActivity(new Intent(ScreenLauncher.this, FirstTimeUsernameScreen.class));
                        } else {
                            startActivity(new Intent(ScreenLauncher.this, MainMenuActivity.class));
                        }

                    }
                });
            }
        }, 5000);
    }

    private void RunAnimation()
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.scale);
        a.reset();
        TextView tv = (TextView) findViewById(R.id.home_title);
        tv.clearAnimation();
        tv.startAnimation(a);
    }

}
