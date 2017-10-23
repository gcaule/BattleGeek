package fr.wcs.battlegeek;

import android.content.Context;
import android.graphics.Point;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;

import fr.wcs.battlegeek.controller.AI;
import fr.wcs.battlegeek.controller.GameController;
import fr.wcs.battlegeek.model.Maps;
import fr.wcs.battlegeek.model.PlayerModel;
import fr.wcs.battlegeek.model.Result;
import fr.wcs.battlegeek.model.Settings;

import static fr.wcs.battlegeek.controller.AI.Level.I;
import static fr.wcs.battlegeek.model.Result.Type.DROWN;
import static fr.wcs.battlegeek.model.Result.Type.VICTORY;
import static fr.wcs.battlegeek.ui.Tetromino.Shape.NONE;
import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class Tests {
    private static final String TAG = "TEST";
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("fr.wcs.battlegeek", appContext.getPackageName());
    }

    @Test
    public void AILevelI() throws Exception {
        Maps.init();
        for (int i = 0; i < Maps.maps.size(); i++) {
            AI ai = new AI();
            ai.setLevel(I);
            Log.d(TAG, "AILevelI: Using Map " + String.valueOf(i + 1));
            char[][] map = Maps.getMapFromIndex(i);
            GameController gameController = new GameController(map);
            Result result = new Result(0,0,NONE, Result.Type.MISSED,null);
            int drownCount = 0;
            int shootCount = 0;
            while(result.getType() != VICTORY) {
                Point p = ai.play();
                shootCount ++;
                result = gameController.shot(p.x, p.y);
                ai.setResult(result);
                if(result.getType() == DROWN || result.getType() == VICTORY) {
                    drownCount ++;
                }
            }
            Log.d(TAG, "AILevelI: Total Shoot Count: " + shootCount);
            assertEquals(7, drownCount);
        }
    }

    @Test
    public void AILevelII() throws Exception {
        Maps.init();
        for (int i = 0; i < Maps.maps.size(); i++) {
            Log.d(TAG, "AILevelII: Using Map " + String.valueOf(i + 1));
            AI ai = new AI();
            char[][] map = Maps.getMapFromIndex(i);
            ai.setPlayerMap(map);
            ai.setLevel(AI.Level.II);
            GameController gameController = new GameController(map);
            Result result = new Result(0,0,NONE, Result.Type.MISSED,null);
            int drownCount = 0;
            int shootCount = 0;
            while(result.getType() != VICTORY) {
                Point p = ai.play();
                shootCount ++;
                result = gameController.shot(p.x, p.y);
                ai.setResult(result);
                if(result.getType() == DROWN || result.getType() == VICTORY) {
                    drownCount ++;
                }
            }
            Log.d(TAG, "AILevelII: Total Shoot Count: " + shootCount);
            assertEquals(7, drownCount);
        }
    }

    @Test
    public void AILevelImpossible() throws Exception {
        for (int i = 0; i < Maps.maps.size(); i++) {
            Log.d(TAG, "AILevelImpossible: Using Map " + String.valueOf(i + 1));
            AI ai = new AI();
            char[][] map = Maps.getMapFromIndex(i);
            ai.setPlayerMap(map);
            ai.setLevel(AI.Level.IMPOSSIBLE);
            GameController gameController = new GameController(map);
            Result result = new Result(0,0,NONE, Result.Type.MISSED,null);
            int drownCount = 0;
            int shootCount = 0;
            while(result.getType() != VICTORY) {
                Point p = ai.play();
                shootCount ++;
                result = gameController.shot(p.x, p.y);
                ai.setResult(result);
                if(result.getType() == DROWN || result.getType() == VICTORY) {
                    drownCount ++;
                }
            }
            Log.d(TAG, "AILevelImpossible: Total Shoot Count: " + shootCount);
            assertEquals(7, drownCount);
            assertEquals(28, shootCount);
        }
    }

    @Test
    public void testMaps() throws Exception {
        Maps.init();
        char[] symbols = new char[] {'I', 'O', 'T', 'S', 'Z', 'L', 'J', ' '};
        boolean valid = true;
        for (int i = 0; i < Maps.maps.size(); i++) {
            char[][] map = Maps.getMapFromIndex(i);
            for (int j = 0; j < Settings.GRID_SIZE; j++) {
                for (int k = 0; k < Settings.GRID_SIZE; k++) {
                    if(!contains(symbols, map[j][k])) {
                        Log.d(TAG, "testMaps: Invalid Index: " + i);
                        valid = false;
                        break;
                    }
                }
                if(!valid) {
                    break;
                }
            }
        }

        assertEquals(true, valid);
    }

    public boolean contains(char[] array, char character) {
        for (int i = 0; i < array.length; i++) {
            if(array[i] == character) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void testSortRatio() throws Exception {
        PlayerModel player1 = new PlayerModel("Georges");
        player1.addVictory(AI.Level.III);
        player1.addVictory(AI.Level.III);
        player1.addDefeat(AI.Level.III);

        PlayerModel player2 = new PlayerModel("Leon");
        player2.addVictory(AI.Level.III);
        player2.addDefeat(AI.Level.III);

        ArrayList<PlayerModel> playerModels = new ArrayList<>();
        playerModels.add(player2);
        playerModels.add(player1);

        PlayerModel.setComparatorLevel(AI.Level.III);
        Collections.sort(playerModels, PlayerModel.ratioComparator);

        ArrayList<PlayerModel> result = new ArrayList<>();
        result.add(player1);
        result.add(player2);

        assertEquals(result, playerModels);
    }

    @Test
    public void testSortVictory() throws Exception {
        PlayerModel player1 = new PlayerModel("Georges");
        player1.addVictory(AI.Level.III);
        player1.addVictory(AI.Level.III);

        PlayerModel player2 = new PlayerModel("Leon");
        player2.addVictory(AI.Level.III);

        ArrayList<PlayerModel> playerModels = new ArrayList<>();
        playerModels.add(player2);
        playerModels.add(player1);

        PlayerModel.setComparatorLevel(AI.Level.III);
        Collections.sort(playerModels, PlayerModel.victoriesComparator);

        ArrayList<PlayerModel> result = new ArrayList<>();
        result.add(player1);
        result.add(player2);

        assertEquals(result, playerModels);
    }

    @Test
    public void testSortBestShots() throws Exception {
        PlayerModel player1 = new PlayerModel("Georges");
        player1.addShotsCount(AI.Level.III, 42);
        player1.addShotsCount(AI.Level.III, 24);


        PlayerModel player2 = new PlayerModel("Leon");
        player2.addShotsCount(AI.Level.III, 42);

        ArrayList<PlayerModel> playerModels = new ArrayList<>();
        playerModels.add(player2);
        playerModels.add(player1);

        PlayerModel.setComparatorLevel(AI.Level.III);
        Collections.sort(playerModels, PlayerModel.bestShotsCountComparator);

        ArrayList<PlayerModel> result = new ArrayList<>();
        result.add(player1);
        result.add(player2);

        assertEquals(result, playerModels);
    }

    @Test
    public void testSortName() throws Exception {
        PlayerModel player1 = new PlayerModel("georges");

        PlayerModel player2 = new PlayerModel("Leon");

        ArrayList<PlayerModel> playerModels = new ArrayList<>();
        playerModels.add(player2);
        playerModels.add(player1);

        PlayerModel.setComparatorLevel(AI.Level.III);
        Collections.sort(playerModels, PlayerModel.nameComparator);

        ArrayList<PlayerModel> result = new ArrayList<>();
        result.add(player1);
        result.add(player2);

        assertEquals(result, playerModels);
    }

    @Test
    public void testSortBestTime() throws Exception {
        PlayerModel player1 = new PlayerModel("Georges");
        player1.addGameTime(AI.Level.III, VICTORY, 42L);

        PlayerModel player2 = new PlayerModel("Leon");
        player2.addGameTime(AI.Level.III, VICTORY, 72L);

        PlayerModel player3 = new PlayerModel("Marcel");

        ArrayList<PlayerModel> playerModels = new ArrayList<>();
        playerModels.add(player3);
        playerModels.add(player2);
        playerModels.add(player1);

        PlayerModel.setComparatorLevel(AI.Level.III);
        Collections.sort(playerModels, PlayerModel.bestShotsCountComparator);

        ArrayList<PlayerModel> result = new ArrayList<>();
        result.add(player1);
        result.add(player2);
        result.add(player3);

        assertEquals(result, playerModels);
    }
}
