package fr.wcs.battlegeek;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

public class GameRulesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_rules);

        final ImageButton buttonHome = (ImageButton) findViewById(R.id.buttonHome);

        ColorFilter filterYellow = new LightingColorFilter(Color.YELLOW, Color.YELLOW);
        buttonHome.setColorFilter(filterYellow);

        Typeface titleFont = Typeface.createFromAsset(getAssets(), "fonts/emulogic.ttf");
        Typeface mainFont = Typeface.createFromAsset(getAssets(), "fonts/atarifull.ttf");

        TextView gameRules = (TextView) findViewById(R.id.textViewGameRules);
        gameRules.setTypeface(titleFont);
        TextView gameRulesGeneral = (TextView) findViewById(R.id.gameRulesGeneral);
        gameRulesGeneral.setTypeface(titleFont);
        TextView rulesPrinciple = (TextView) findViewById(R.id.gameRulesPrinciple);
        rulesPrinciple.setTypeface(mainFont);

        TextView rulesMode = (TextView) findViewById(R.id.gameRulesModes);
        rulesMode.setTypeface(titleFont);
        TextView rulesLevels = (TextView) findViewById(R.id.gameRulesLevels);
        rulesLevels.setTypeface(mainFont);

        TextView rulesLevelITitle = (TextView) findViewById(R.id.gameRulesLevelITitle);
        rulesLevelITitle.setTypeface(titleFont);
        TextView rulesLevelI = (TextView) findViewById(R.id.gameRulesLevelI);
        rulesLevelI.setTypeface(mainFont);
        TextView rulesLevelIITitle = (TextView) findViewById(R.id.gameRulesLevelIITitle);
        rulesLevelIITitle.setTypeface(titleFont);
        TextView rulesLevelII = (TextView) findViewById(R.id.gameRulesLevelII);
        rulesLevelII.setTypeface(mainFont);
        TextView rulesLevelIIITitle = (TextView) findViewById(R.id.gameRulesLevelIIITitle);
        rulesLevelIIITitle.setTypeface(titleFont);
        TextView rulesLevelIII = (TextView) findViewById(R.id.gameRulesLevelIII);
        rulesLevelIII.setTypeface(mainFont);
        TextView rulesLevelImpoTitle = (TextView) findViewById(R.id.gameRulesLevelImpoTitle);
        rulesLevelImpoTitle.setTypeface(titleFont);
        TextView rulesLevelImpo = (TextView) findViewById(R.id.gameRulesLevelImpo);
        rulesLevelImpo.setTypeface(mainFont);

        TextView rulesSubtilitiesTitle = (TextView) findViewById(R.id.gameRulesSubtilitiesTitle);
        rulesSubtilitiesTitle.setTypeface(titleFont);
        TextView rulesSubtilities = (TextView) findViewById(R.id.gameRulesSubtilities);
        rulesSubtilities.setTypeface(mainFont);

        TextView rulesBonusITitle = (TextView) findViewById(R.id.gameRulesBonusITitle);
        rulesBonusITitle.setTypeface(titleFont);
        TextView rulesBonusI = (TextView) findViewById(R.id.gameRulesBonusI);
        rulesBonusI.setTypeface(mainFont);
        TextView rulesBonusIITitle = (TextView) findViewById(R.id.gameRulesBonusIITitle);
        rulesBonusIITitle.setTypeface(titleFont);
        TextView rulesBonusII = (TextView) findViewById(R.id.gameRulesBonusII);
        rulesBonusII.setTypeface(mainFont);
        TextView rulesBonusIIITitle = (TextView) findViewById(R.id.gameRulesBonusIIITitle);
        rulesBonusIIITitle.setTypeface(titleFont);
        TextView rulesBonusIII = (TextView) findViewById(R.id.gameRulesBonusIII);
        rulesBonusIII.setTypeface(mainFont);

        TextView rulesEnd = (TextView) findViewById(R.id.gameRulesEnd);
        rulesEnd.setTypeface(mainFont);
        TextView rulesPS = (TextView) findViewById(R.id.gameRulesPS);
        rulesPS.setTypeface(mainFont);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });
    }
}
