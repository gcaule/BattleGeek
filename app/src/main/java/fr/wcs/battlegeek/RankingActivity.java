package fr.wcs.battlegeek;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import fr.wcs.battlegeek.adapterRanking.CustomListAdapter;
import fr.wcs.battlegeek.model.PlayerModel;

public class RankingActivity extends AppCompatActivity {

    private List<PlayerModel> mPlayerModelList = new ArrayList<PlayerModel>();
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ranking);

        ImageButton buttonHome = (ImageButton) findViewById(R.id.buttonHome);

        buttonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RankingActivity.super.onBackPressed();
            }

        });

        SQLiteDatabase myDB = null;

        try {

            //Create a Database if doesnt exist otherwise Open It

            myDB = this.openOrCreateDatabase("leaderboard", MODE_PRIVATE, null);

            //Create table in database if it doesnt exist allready

            myDB.execSQL("CREATE TABLE IF NOT EXISTS scores (name TEXT, score TEXT);");

            //Select all rows from the table

            Cursor cursor = myDB.rawQuery("SELECT * FROM scores", null);

            //If there are no rows (data) then insert some in the table

            if (cursor != null) {
                if (cursor.getCount() == 0) {

                    myDB.execSQL("INSERT INTO scores (name, score) VALUES ('Andy', '7');");
                    myDB.execSQL("INSERT INTO scores (name, score) VALUES ('Marie', '4');");
                    myDB.execSQL("INSERT INTO scores (name, score) VALUES ('George', '1');");

                }

            }


        } catch (Exception e) {

        } finally {

            //Initialize and create a new adapter with layout named list found in activity_main layout

            listView = (ListView) findViewById(R.id.list);
            adapter = new CustomListAdapter(this, mPlayerModelList);
            listView.setAdapter(adapter);

            Cursor cursor = myDB.rawQuery("SELECT * FROM scores", null);

            if (cursor.moveToFirst()) {

                //read all rows from the database and add to the PlayerModel array

                while (!cursor.isAfterLast()) {

                    PlayerModel playerModel = new PlayerModel();

                    playerModel.setName(cursor.getString(0));
                    mPlayerModelList.add(playerModel);
                    cursor.moveToNext();


                }
            }

            //All done, so notify the adapter to populate the list using the PlayerModel Array

            adapter.notifyDataSetChanged();
        }

    }
}