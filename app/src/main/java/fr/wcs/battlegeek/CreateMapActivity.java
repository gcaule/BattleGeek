package fr.wcs.battlegeek;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

/**
 * The Custom View in witch the User can place the Items in the Grid in order to create the Map
 */
public class CreateMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_map);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        Button go = (Button) findViewById(R.id.buttonLaunchGame);

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateMapActivity.this, MainActivity.class);
                startActivity(intent);
            }

        });


    }
}
