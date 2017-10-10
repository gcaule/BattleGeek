package fr.wcs.battlegeek.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import fr.wcs.battlegeek.R;
import fr.wcs.battlegeek.model.Result;
import fr.wcs.battlegeek.model.Settings;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by adphi on 10/10/17.
 */

public class SoundController {
    private static final String TAG = "Sound";

    private Context mContext;

    private SoundPool soundPool;
    private SoundPool.Builder soundPoolBuilder;
    private AudioAttributes attributes;
    private AudioAttributes.Builder attributesBuilder;
    private int soundID_boom = -1;

    private int soundID_plouf = -1;
    private int soundID_drown = -1;

    private SharedPreferences mSharedPreferences;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SoundController(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(Settings.FILE_NAME, MODE_PRIVATE);
        attributesBuilder = new AudioAttributes.Builder();
        attributesBuilder.setUsage(AudioAttributes.USAGE_GAME);
        attributesBuilder.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
        attributes = attributesBuilder.build();

        soundPoolBuilder = new SoundPool.Builder();
        soundPoolBuilder.setAudioAttributes(attributes);
        soundPool = soundPoolBuilder.build();

        soundID_boom = soundPool.load(mContext, R.raw.xplod1, 1);
        soundID_plouf = soundPool.load(mContext, R.raw.ploof1, 1);
        soundID_drown = soundPool.load(mContext, R.raw.wilhelm_scream, 1);
    }

    public void playSound(Result.Type result){
        Log.d(TAG, "playSound() called with: result = [" + result + "]");
        //int volume = mSharedPreferences.getInt("ValueEffects", 1);
        int volume = 1;
        int soundID = -1;
        switch (result) {
            case MISSED:
                soundID = soundID_plouf;
                break;
            case TOUCHED:
                soundID = soundID_boom;
                break;
            case DROWN:
                soundID = soundID_drown;
                break;
            case VICTORY:
                soundID = soundID_boom;
                break;
        }
        soundPool.play(soundID, volume, volume, 0, 0, 1);
    }
}
