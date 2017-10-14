package fr.wcs.battlegeek;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import fr.wcs.battlegeek.model.PlayerModel;
import fr.wcs.battlegeek.model.Settings;

public class FirstTimeUsernameScreen extends AppCompatActivity {

    private static final String TAG = Settings.TAG;
    // Firebase instance variables
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUsersDatabaseReference;
    private ChildEventListener mChildEventListener;
    SharedPreferences mSharedPreferences;
    private String uid;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_first_time_username_screen);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        Typeface titleHomeFont = Typeface.createFromAsset(getAssets(), "fonts/SomeTimeLater.otf");
        TextView messageTitle = (TextView) findViewById(R.id.message_title);
        messageTitle.setTypeface(titleHomeFont);

        Typeface welcomeMessageFont = Typeface.createFromAsset(getAssets(), "fonts/Curvy.ttf");
        Typeface buttonFont = Typeface.createFromAsset(getAssets(), "fonts/DirtyClassicMachine.ttf");

        TextView welcomeMessage = (TextView) findViewById(R.id.message_welcome);
        welcomeMessage.setTypeface(welcomeMessageFont);

        TextView infoMessage = (TextView) findViewById(R.id.textView4);
        infoMessage.setTypeface(welcomeMessageFont);

        Button buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setTypeface(buttonFont);

        final EditText playerName = (EditText) findViewById(R.id.input_playername);
        playerName.setTypeface(welcomeMessageFont);

        //Call SharedPref
        mSharedPreferences = getSharedPreferences(Settings.FILE_NAME, MODE_PRIVATE);


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerName.getText().toString().trim().length() == 0) {
                    Toast.makeText(FirstTimeUsernameScreen.this, R.string.message_error_emptyname, Toast.LENGTH_SHORT).show();
                } else {
                    PlayerModel newPlayer = new PlayerModel(playerName.getText().toString());

                    mDatabase = FirebaseDatabase.getInstance();
                    mUsersDatabaseReference = mDatabase.getReference().child("Users");
                    uid = mUsersDatabaseReference.child("Users").push().getKey();
                    mUsersDatabaseReference.child(uid).setValue(newPlayer);
                    mSharedPreferences.edit().putString(Settings.UID, uid).apply();
                    mSharedPreferences.edit().putString(Settings.PLAYER_NAME, newPlayer.getName()).apply();

                    startActivity(new Intent(FirstTimeUsernameScreen.this, MainMenuActivity.class));
                }
            }
        });
    }
}

