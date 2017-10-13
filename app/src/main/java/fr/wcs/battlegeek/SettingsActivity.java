package fr.wcs.battlegeek;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import fr.wcs.battlegeek.controller.DataController;
import fr.wcs.battlegeek.model.PlayerModel;
import fr.wcs.battlegeek.model.Settings;

import static android.view.View.GONE;

public class SettingsActivity extends AppCompatActivity {

    SharedPreferences mSharedPreferences;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUsersDatabaseReference;
    private ImageView mImageViewMusic;
    private ImageView mImageViewEffects;

    private PlayerModel mPlayerModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);

        View backgroundimage = findViewById(R.id.settingsBackgroundView);
        Drawable backgroundView = backgroundimage.getBackground();
        backgroundView.setAlpha(150);

        //Affichage de la value pour la seekbox Music et seekbok Effects

        final SeekBar seekBarMusic = (SeekBar) findViewById(R.id.seekBarMusic);
        final SeekBar seekBarEffects = (SeekBar) findViewById(R.id.seekBarEffects);
        final TextView seekBarValueMusic = (TextView) findViewById(R.id.seekBarValueMusic);
        final TextView seekBarValueEffects = (TextView) findViewById(R.id.seekBarValueEffects);
        final EditText inputPlayerName = (EditText) findViewById(R.id.inputPlayerName);
        final ImageButton buttonHome = (ImageButton) findViewById(R.id.buttonHome);
        final Button buttonSave = (Button) findViewById(R.id.buttonSave);

        // Radio Buttons
        RadioButton mRadioButtonAnimationSlow = (RadioButton) findViewById(R.id.radioButtonAnimationSlow);
        RadioButton mRadioButtonAnimationMedium = (RadioButton) findViewById(R.id.radioButtonAnimationMedium);
        RadioButton mRadioButtonAnimationFast = (RadioButton) findViewById(R.id.radioButtonAnimationFast);

        buttonSave.setVisibility(GONE);

        Typeface titleFont = Typeface.createFromAsset(getAssets(), "fonts/SomeTimeLater.otf");
        Typeface mainFont = Typeface.createFromAsset(getAssets(), "fonts/Curvy.ttf");
        Typeface buttonFont = Typeface.createFromAsset(getAssets(), "fonts/DirtyClassicMachine.ttf");

        TextView titleMessage = (TextView) findViewById(R.id.textViewSettings);

        titleMessage.setTypeface(titleFont);
        inputPlayerName.setTypeface(mainFont);
        buttonSave.setTypeface(buttonFont);

        seekBarValueEffects.setTypeface(mainFont);
        seekBarValueMusic.setTypeface(mainFont);

        //Initialize Firebase components
        mDatabase = FirebaseDatabase.getInstance();

        //Call SharedPref
        mSharedPreferences = getSharedPreferences(Settings.FILE_NAME, MODE_PRIVATE);


        //Get User on SharedPref
        String uidFirebase = mSharedPreferences.getString(Settings.UID, null);

        final TextView DISPLAYKEY = (TextView) findViewById(R.id.DISPLAYKEY);
        DISPLAYKEY.setText(uidFirebase);

        //Get User on Firebase
        mUsersDatabaseReference = mDatabase.getReference().child("Users").child(uidFirebase).child("playerName");

        //Get Pref for Music Volume
        int valueMusic = mSharedPreferences.getInt(Settings.MUSIC_TAG,0);
        seekBarMusic.setProgress(valueMusic);
        seekBarValueMusic.setText(String.valueOf(valueMusic));

        mImageViewMusic = (ImageView) findViewById(R.id.imageViewMusic);
        setMusicIcon(valueMusic);

        //Get Pref for Effects Volume
        int valueEffects = mSharedPreferences.getInt(Settings.EFFECTS_TAG,0);
        seekBarEffects.setProgress(valueEffects);
        seekBarValueEffects.setText(String.valueOf(valueEffects));

        mImageViewEffects = (ImageView) findViewById(R.id.imageViewEffects);
        setEffectIcon(valueEffects);

        //Get Pref for PlayerModel Name
        final DataController dataController = new DataController(getApplicationContext());

        dataController.setDataReadyListener(new DataController.DataReadyListener() {
            @Override
            public void onDataReadyListener(PlayerModel player) {
                mPlayerModel = player;
            }
        });
        String playerName = mSharedPreferences.getString("PlayerName", null);
        inputPlayerName.setText(playerName);

        // Get Pref for Animations Speed
        int valueAnimationSpeed = mSharedPreferences.getInt(Settings.ANIMATION_TAG, 0);
        if (valueAnimationSpeed != 0) {
            switch (valueAnimationSpeed) {
                case Settings.ANIMATION_SLOW:
                    mRadioButtonAnimationSlow.setChecked(true);
                    mRadioButtonAnimationMedium.setChecked(false);
                    mRadioButtonAnimationFast.setChecked(false);
                    break;
                case Settings.ANIMATION_MEDIUM:
                    mRadioButtonAnimationSlow.setChecked(false);
                    mRadioButtonAnimationMedium.setChecked(true);
                    mRadioButtonAnimationFast.setChecked(false);
                    break;
                case Settings.ANIMATION_FAST:
                    mRadioButtonAnimationSlow.setChecked(false);
                    mRadioButtonAnimationMedium.setChecked(false);
                    mRadioButtonAnimationFast.setChecked(true);
                    break;
            }
        }
        else {
            mSharedPreferences.edit().putInt(Settings.ANIMATION_TAG, Settings.ANIMATION_MEDIUM).apply();
        }

        //Seekbar listener for music + Display value
        seekBarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                seekBarValueMusic.setText(String.valueOf(progress));
                int valueMusic = seekBarMusic.getProgress();
                mSharedPreferences.edit().putInt(Settings.MUSIC_TAG, valueMusic).apply();
                setMusicIcon(valueMusic);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });

        //Seekbar listener for effects + display value
        seekBarEffects.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                seekBarValueEffects.setText(String.valueOf(progress));
                int valueEffects = seekBarEffects.getProgress();
                mSharedPreferences.edit().putInt(Settings.EFFECTS_TAG, valueEffects).apply();
                setEffectIcon(valueEffects);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });

        inputPlayerName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                buttonSave.setVisibility(View.VISIBLE);
            }
        });

        //Button to go to home menu
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });

        //Button to save user preferences
         buttonSave.setOnClickListener(new View.OnClickListener() {
             @Override
           public void onClick(View v) {
                 final String name = inputPlayerName.getText().toString();
                 if (name.isEmpty()){
                     Toast.makeText(SettingsActivity.this, R.string.message_error_emptyname, Toast.LENGTH_SHORT).show();
                 }
                 else {
                     mSharedPreferences.edit().putString(Settings.PLAYER_NAME, name).commit();
                     mPlayerModel.setName(name);
                     dataController.updatePlayer(mPlayerModel);
                     buttonSave.setVisibility(GONE);
                     Toast.makeText(SettingsActivity.this, R.string.saved_parameters, Toast.LENGTH_SHORT).show();
                 }
             }
        });
    }

    // RadioButtons Listener
    public void onRadioButtonAnimationSpeedClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButtonAnimationSlow:
                if (checked) {
                    mSharedPreferences.edit().putInt(Settings.ANIMATION_TAG, Settings.ANIMATION_SLOW).apply();
                }
                break;
            case R.id.radioButtonAnimationMedium:
                if (checked) {
                    mSharedPreferences.edit().putInt(Settings.ANIMATION_TAG, Settings.ANIMATION_MEDIUM).apply();
                }
                break;
            case R.id.radioButtonAnimationFast:
                if(checked) {
                    mSharedPreferences.edit().putInt(Settings.ANIMATION_TAG, Settings.ANIMATION_FAST).apply();
                }
                break;
        }
    }

    private void setMusicIcon(int volume){
        if(volume > 66) {
            mImageViewMusic.setImageResource(R.drawable.music_loud);
        }
        else if(volume > 33) {
            mImageViewMusic.setImageResource(R.drawable.music_medium);
        }
        else if(volume > 0) {
            mImageViewMusic.setImageResource(R.drawable.music_low);
        }
        else {
            mImageViewMusic.setImageResource(R.drawable.no_music);
        }
    }

    private void setEffectIcon(int volume) {
        if(volume > 66) {
            mImageViewEffects.setImageResource(R.drawable.volume_up_interface_symbol);
        }
        else if(volume > 33) {
            mImageViewEffects.setImageResource(R.drawable.ic_volume_down_black_24dp);
        }
        else if(volume > 0) {
            mImageViewEffects.setImageResource(R.drawable.ic_volume_mute_black_24dp);
        }
        else {
            mImageViewEffects.setImageResource(R.drawable.ic_volume_off_black_24dp);
        }
    }
}
