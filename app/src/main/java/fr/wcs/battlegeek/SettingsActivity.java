package fr.wcs.battlegeek;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import fr.wcs.battlegeek.model.Settings;

import static android.view.View.GONE;

public class SettingsActivity extends AppCompatActivity {

    SharedPreferences mSharedPreferences;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUsersDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);

        //Affichage de la value pour la seekbox Music et seekbok Effects

        final SeekBar seekBarMusic = (SeekBar) findViewById(R.id.seekBarMusic);
        final SeekBar seekBarEffects = (SeekBar) findViewById(R.id.seekBarEffects);
        final TextView seekBarValueMusic = (TextView) findViewById(R.id.seekBarValueMusic);
        final TextView seekBarValueEffects = (TextView) findViewById(R.id.seekBarValueEffects);
        final EditText inputPlayerName = (EditText) findViewById(R.id.inputPlayerName);
        final ImageButton buttonHome = (ImageButton) findViewById(R.id.buttonHome);
        final Button buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setVisibility(GONE);

        //Initialize Firebase components
        mDatabase = FirebaseDatabase.getInstance();

        //Call SharedPref
        mSharedPreferences = getSharedPreferences(Settings.FILE_NAME, MODE_PRIVATE);

        //Get User on SharedPref
        String uidFirebase = mSharedPreferences.getString("uidFirebase", null);

        final TextView DISPLAYKEY = (TextView) findViewById(R.id.DISPLAYKEY);
        DISPLAYKEY.setText(uidFirebase);

        //Get User on Firebase
        mUsersDatabaseReference = mDatabase.getReference().child("Users").child(uidFirebase).child("playerName");

        //Get Pref for Music Volume
        int ValueMusicStart = mSharedPreferences.getInt("ValueMusic",0);
        seekBarMusic.setProgress(ValueMusicStart);
        seekBarValueMusic.setText(String.valueOf(ValueMusicStart));

        //Get Pref for Effects Volume
        int ValueEffectsStart = mSharedPreferences.getInt("ValueEffects",0);
        seekBarEffects.setProgress(ValueEffectsStart);
        seekBarValueEffects.setText(String.valueOf(ValueEffectsStart));

        //Get Pref for PlayerModel Name
        String playerName = mSharedPreferences.getString("PlayerName", null);
        inputPlayerName.setText(playerName);

        //Seekbar listener for music + Display value
        seekBarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                // TODO Auto-generated method stub
                seekBarValueMusic.setText(String.valueOf(progress));
                mSharedPreferences.edit().putInt("ValueMusic", seekBarMusic.getProgress()).apply();
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
                mSharedPreferences.edit().putInt("ValueEffects", seekBarEffects.getProgress()).apply();
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
                /*Intent intent = new Intent(SettingsActivity.this, MainMenuActivity.class);
                startActivity(intent);*/
                onBackPressed();
            }

        });

        //Button to save user preferences
         buttonSave.setOnClickListener(new View.OnClickListener() {
             @Override
           public void onClick(View v) {
                 String name = inputPlayerName.getText().toString();
                 if (name.isEmpty()){
                     Toast.makeText(SettingsActivity.this, R.string.message_error_emptyname, Toast.LENGTH_SHORT).show();
                 }
                 else {
                     mSharedPreferences.edit().putString("PlayerName", name).commit();
                     mUsersDatabaseReference.setValue(name);
                     Toast.makeText(SettingsActivity.this, "Paramètres enregistrés", Toast.LENGTH_SHORT).show();
                 }
             }
        });
    }
}
