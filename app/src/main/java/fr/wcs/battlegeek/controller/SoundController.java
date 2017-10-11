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

/**
 * Class handling game's Sound
 */
public class SoundController {
    private static final String TAG = "Sound";
    // Context to access Android's Resource System
    private Context mContext;

    // Soundpool object that will handle the sound
    private SoundPool soundPool;

    // Some sound IDs
    // TODO: Move all the sound in some lists
    private int soundID_boom = -1;
    private int soundID_plouf = -1;
    private int soundID_drown = -1;

    // Preferences reference
    private SharedPreferences mSharedPreferences;

    /**
     * SoundController Constructor
     * @param context the application's Context
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SoundController(Context context) {
        mContext = context;
        // Initialisation of the Shared Preferences
        mSharedPreferences = mContext.getSharedPreferences(Settings.FILE_NAME, MODE_PRIVATE);

        // Audio Attributes definition through a Builder Object
        AudioAttributes.Builder attributesBuilder = new AudioAttributes.Builder();
        attributesBuilder.setUsage(AudioAttributes.USAGE_GAME);
        attributesBuilder.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
        // Build the Audio Attributes
        AudioAttributes attributes = attributesBuilder.build();

        // SoundPool initialisation through a Builder Object
        SoundPool.Builder soundPoolBuilder = new SoundPool.Builder();
        soundPoolBuilder.setAudioAttributes(attributes);
        // Create the SoundPool
        soundPool = soundPoolBuilder.build();

        // Sound's ID definition from the Raw Resources
        soundID_boom = soundPool.load(mContext, R.raw.xplod1, 1);
        soundID_plouf = soundPool.load(mContext, R.raw.ploof1, 1);
        soundID_drown = soundPool.load(mContext, R.raw.wilhelm_scream, 1);
    }

    /**
     * Method Playing the Sound according to the shot's Result's type
     * @param result
     */
    public void playSound(Result.Type result){
        Log.d(TAG, "playSound() called with: result = [" + result + "]");
        // Get the preferred Volume
        float volume = mSharedPreferences.getInt("ValueEffects", 1) / 100;
        int soundID = -1;
        // Get the Sound ID according to the Result's Type
        // TODO : Round Robin (Not play two similar sounds right after)
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

        // Play the sound
        soundPool.play(soundID, volume, volume, 0, 0, 1);
    }
}
