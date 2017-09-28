package fr.wcs.battlegeek;

import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Toast;

import fr.wcs.battlegeek.controller.BattleView;
import fr.wcs.battlegeek.controller.Maps;

public class MainActivity extends AppCompatActivity {

    private String TAG = "CustomView";
    private Toast toast;

    // Audio
    private SoundPool soundPool;
    private SoundPool.Builder soundPoolBuilder;
    private AudioAttributes attributes;
    private AudioAttributes.Builder attributesBuilder;
    private int soundID_boom = -1;
    private int soundID_plouf = -1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);

        // Init Sound
        attributesBuilder = new AudioAttributes.Builder();
        attributesBuilder.setUsage(AudioAttributes.USAGE_GAME);
        attributesBuilder.setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION);
        attributes = attributesBuilder.build();

        soundPoolBuilder = new SoundPool.Builder();
        soundPoolBuilder.setAudioAttributes(attributes);
        soundPool = soundPoolBuilder.build();

        soundID_boom = soundPool.load(this, R.raw.xplod1, 1);
        soundID_plouf = soundPool.load(this, R.raw.ploof1, 1);


        final BattleView battleView = (BattleView) findViewById(R.id.battleView);
        battleView.setOnPlayListener(new BattleView.PlayListener() {
            @Override
            public void onPlayListener(int x, int y) {
                if (Maps.map1[y][x] == 'T' || Maps.map1[y][x] == 'Z' || Maps.map1[y][x] == 'S' || Maps.map1[y][x] == 'O' || Maps.map1[y][x] == 'I' || Maps.map1[y][x] == 'L' || Maps.map1[y][x] == 'J') {
                    battleView.setTouch(x, y);
                    Maps.map1[y][x] = 'X';
                    playSound(soundID_boom);

                    if (victory()) {
                        toast.makeText(getApplicationContext(), "Yahoo !! Gagné !!", Toast.LENGTH_SHORT).show();
                    }
                }
                else if (Maps.map1[y][x] == ' '){
                    battleView.setPlouf(x, y);
                    Maps.map1[y][x] = '_';
                    playSound(soundID_plouf);
                }
                else toast.makeText(getApplicationContext(), "Heu.. Ca sert à rien ça", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void playSound(int soundID){
        AssetManager assetManager = this.getAssets();
        AssetFileDescriptor descriptor;
        soundPool.play(soundID, 1, 1, 0, 0, 1);
    }

    private boolean victory() {
        for (int i = 0; i < Maps.map1.length; i++) {
            for (int j = 0; j < Maps.map1[i].length; j++) {
                if(Maps.map1[i][j] == 'x') return false;
            }
        }
        return true;
    }

}
