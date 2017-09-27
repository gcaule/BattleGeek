package fr.wcs.battlegeek;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * The Custom View in witch the User can place the Items in the Grid in order to create the Map
 */
public class CreateMapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_map);
    }
}
