package fr.wcs.battlegeek.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;

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
    private static final String TAG = Settings.TAG;
    // Context to access Android's Resource System
    private Context mContext;

    // Soundpool object that will handle the sound
    private SoundPool mSoundPool;

    // MediaPlayer for Music
    private MediaPlayer mMediaPlayer;

    // Some sound IDs
    // TODO: Move all the sound in some lists
    private int soundID_boom = -1;
    private int soundID_plouf = -1;
    private int soundID_drown = -1;
    private int soundID_boom2 = -1;

    private int musicID = -1;
    private float mMusicMixRatio = 0.15f;

    private int mMusicPosition = 0;

    // Audio Streams List
    private ArrayList<Integer> mEffectsStreams = new ArrayList<>();

    // Preferences reference
    private SharedPreferences mSharedPreferences;

    /**
     * SoundController Constructor
     * @param context the application's Context
     */
    @SuppressWarnings("deprecation")
    public SoundController(Context context) {
        mContext = context;
        // Initialisation of the Shared Preferences
        mSharedPreferences = mContext.getSharedPreferences(Settings.FILE_NAME, MODE_PRIVATE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Audio Attributes definition through a Builder Object
            AudioAttributes.Builder attributesBuilder = new AudioAttributes.Builder();
            attributesBuilder.setUsage(AudioAttributes.USAGE_GAME);
            attributesBuilder.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
            // Build the Audio Attributes
            AudioAttributes attributes = attributesBuilder.build();

            // SoundPool initialisation through a Builder Object
            SoundPool.Builder soundPoolBuilder = new SoundPool.Builder();
            soundPoolBuilder.setAudioAttributes(attributes);
            soundPoolBuilder.setMaxStreams(10);
            // Create the SoundPool
            mSoundPool = soundPoolBuilder.build();
        }
        else{
            mSoundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 1);
        }

        // Sound's ID definition from the Raw Resources
        soundID_boom = mSoundPool.load(mContext, R.raw.xplod1, 1);
        soundID_plouf = mSoundPool.load(mContext, R.raw.ploof1, 1);
        soundID_boom2 = mSoundPool.load(mContext, R.raw.longbomb1, 1);
        soundID_drown = mSoundPool.load(mContext, R.raw.wilhelm_scream, 1);

        //musicID = R.raw.music_brahms;
        musicID = R.raw.stupid;

        // Music Player
        mMediaPlayer = MediaPlayer.create(mContext, musicID);
        mMediaPlayer.setLooping(true);
    }

    /**
     * Method Playing the Sound according to the shot's Result's type
     * @param result
     */
    public void playSound(Result.Type result){
        // Get the preferred Volume
        float volume = (float) mSharedPreferences.getInt(Settings.EFFECTS_TAG, Settings.EFFECTS_DEFAULT) / 100 ;
        if (volume <= 0) return;
        int soundID;
        // Get the Sound ID according to the Result's Type
        // TODO : Round Robin (Not play two similar sounds right after)
        switch (result) {
            case MISSED:
                soundID = soundID_plouf;
                break;
            case TOUCHED:
                soundID = soundID_boom2;
                break;
            case DROWN:
                mSoundPool.play(soundID_boom, volume, volume, 0, 0, 1);
                soundID = soundID_drown;
                break;
            case VICTORY:
                mSoundPool.play(soundID_boom, volume, volume, 0, 0, 1);
                soundID = soundID_drown;
                break;
            default:
                soundID = -1;
                break;
        }
        // Play the sound
        int stream = mSoundPool.play(soundID, volume, volume, 0, 0, 1);
        if(! mEffectsStreams.contains(stream)) mEffectsStreams.add(stream);
    }

    public void stopEffects() {
        for(int stream : mEffectsStreams) {
            mSoundPool.stop(stream);
        }
        mEffectsStreams.clear();
    }

    public void setEffectsVolume(int volume) {
        float vol = (float) volume / 100 * 0.7f;
        for(int stream : mEffectsStreams) {
            mSoundPool.setVolume(stream, vol, vol);
        }
    }

    public void playMusic(){
        Log.d(TAG, "playMusic() called");
        if(mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(mContext, musicID);
            Log.d(TAG, "playMusic: Re Creating MediaPlayer");
        }
        final float volume = (float) mSharedPreferences.getInt(Settings.MUSIC_TAG, Settings.MUSIC_DEFAULT) / 100 * mMusicMixRatio;
        mMediaPlayer.setVolume(volume, volume);

        mMediaPlayer.seekTo(mMusicPosition);
        mMediaPlayer.start();
    }

    public void pauseMusic() {
        mMediaPlayer.pause();
    }

    public void resumeMusic() {
        final float volume = (float) mSharedPreferences.getInt(Settings.MUSIC_TAG, Settings.MUSIC_DEFAULT) / 100 * mMusicMixRatio;
        mMediaPlayer.setVolume(volume, volume);
        mMediaPlayer.start();
    }

    public void stopMusic(){
        if(mMediaPlayer != null){
            mMusicPosition = mMediaPlayer.getCurrentPosition();
            mMediaPlayer.stop();
        }
    }

    public void setMusicVolume(int volume){
        if(mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer.create(mContext, musicID);
        }
        float vol = (float) volume / 100 * mMusicMixRatio;
        mMediaPlayer.setVolume(vol, vol);
    }

    public void release(){
        mSoundPool.release();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }
}
