package fr.wcs.battlegeek;

import android.content.Intent;
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

import static android.view.View.GONE;

public class SettingsActivity extends AppCompatActivity {

    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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

        //Call SharedPref
        mSharedPreferences = getPreferences(MODE_PRIVATE);

        //Get Pref for Music Volume
        int ValueMusicStart = getPreferences(MODE_PRIVATE).getInt("ValueMusic",0);
        seekBarMusic.setProgress(ValueMusicStart);
        seekBarValueMusic.setText(String.valueOf(ValueMusicStart));

        //Get Pref for Effects Volume
        int ValueEffectsStart = getPreferences(MODE_PRIVATE).getInt("ValueEffects",0);
        seekBarEffects.setProgress(ValueEffectsStart);
        seekBarValueEffects.setText(String.valueOf(ValueEffectsStart));

        //Get Pref for Player Name
        String playerName = getPreferences(MODE_PRIVATE).getString("PlayerName", null);
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
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                buttonSave.setVisibility(View.VISIBLE);
            }
        });

        //Button to go to home menu
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }

        });

        //Button to save user preferences
         buttonSave.setOnClickListener(new View.OnClickListener() {
             @Override
           public void onClick(View v) {
                 mSharedPreferences.edit().putString("PlayerName", inputPlayerName.getText().toString()).apply();
                 Toast.makeText(SettingsActivity.this, "Paramètres enregistrés", Toast.LENGTH_SHORT).show();
             }
        });
    }
}
