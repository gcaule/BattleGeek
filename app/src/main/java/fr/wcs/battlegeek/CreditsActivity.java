package fr.wcs.battlegeek;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class CreditsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        final ImageButton buttonHome = (ImageButton) findViewById(R.id.buttonHome);
        final TextView CreditsText = (TextView) findViewById(R.id.CreditsText);

        ColorFilter filterYellow = new LightingColorFilter(Color.YELLOW, Color.YELLOW);
        Typeface mainFont = Typeface.createFromAsset(getAssets(), "fonts/openShip.otf");

        buttonHome.setColorFilter(filterYellow);
        CreditsText.setTextColor(Color.parseColor("#FFEE00"));
        CreditsText.setTypeface(mainFont);


        //button to go to credits page
        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreditsActivity.this, MainMenuActivity.class));
            }
        });


    }
}
