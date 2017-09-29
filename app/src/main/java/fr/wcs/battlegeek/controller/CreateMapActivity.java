package fr.wcs.battlegeek.controller;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import fr.wcs.battlegeek.R;
import fr.wcs.battlegeek.ui.MapView;

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

        Button buttonLaunchGame = (Button) findViewById(R.id.buttonLaunchGame);

        buttonLaunchGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MapView mapView = (MapView) findViewById(R.id.mapView);
                char[][] mapData = mapView.getMapData();
                Intent intent = new Intent(CreateMapActivity.this, GameActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("mapData", mapData);
                intent.putExtras(bundle);
                startActivity(intent);
            }

        });


    }
}
