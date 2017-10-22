package fr.wcs.battlegeek;

import android.content.Context;
import android.graphics.Point;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import fr.wcs.battlegeek.controller.AI;
import fr.wcs.battlegeek.controller.GameController;
import fr.wcs.battlegeek.model.Maps;
import fr.wcs.battlegeek.model.Result;
import fr.wcs.battlegeek.model.Settings;

import static fr.wcs.battlegeek.model.Result.Type.DROWN;
import static fr.wcs.battlegeek.model.Result.Type.VICTORY;
import static fr.wcs.battlegeek.ui.Tetromino.Shape.NONE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AITests {
    private static final String TAG = "TEST";
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("fr.wcs.battlegeek", appContext.getPackageName());
    }

    @Test
    public void AILevelI() throws Exception {
        for (int i = 0; i < Maps.maps.length; i++) {
            AI ai = new AI();
            ai.setLevel(AI.Level.I);
            Log.d(TAG, "AILevelI: Using Map " + String.valueOf(i + 1));
            char[][] map = Maps.getMapFromIndex(i);
            GameController gameController = new GameController(map);
            Result result = new Result(0,0,NONE, Result.Type.MISSED,null);
            int drownCount = 0;
            int shootCount = 0;
            while(result.getType() != VICTORY) {
                shootCount++ ;
                Point p = ai.play();
                result = gameController.shot(p.x, p.y);
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
        for (int i = 0; i < Maps.maps.length; i++) {
            AI ai = new AI();
            ai.setLevel(AI.Level.II);
            Log.d(TAG, "AILevelII: Using Map " + String.valueOf(i + 1));
            char[][] map = Maps.getMapFromIndex(i);
            GameController gameController = new GameController(map);
            Result result = new Result(0,0,NONE, Result.Type.MISSED,null);
            int drownCount = 0;
            int shootCount = 0;
            while(result.getType() != VICTORY) {
                Point p = ai.play();
                shootCount ++;
                result = gameController.shot(p.x, p.y);
                if(result.getType() == DROWN || result.getType() == VICTORY) {
                    drownCount ++;
                }
            }
            Log.d(TAG, "AILevelII: Total Shoot Count: " + shootCount);
            assertEquals(7, drownCount);
        }
    }

    @Test
    public void AILevelIII() throws Exception {
        for (int i = 0; i < Maps.maps.length; i++) {
            Log.d(TAG, "AILevelIII: Using Map " + String.valueOf(i + 1));
            AI ai = new AI();
            char[][] map = Maps.getMapFromIndex(i);
            ai.setPlayerMap(map);
            ai.setLevel(AI.Level.III);
            GameController gameController = new GameController(map);
            Result result = new Result(0,0,NONE, Result.Type.MISSED,null);
            int drownCount = 0;
            int shootCount = 0;
            while(result.getType() != VICTORY) {
                Point p = ai.play();
                shootCount ++;
                result = gameController.shot(p.x, p.y);
                if(result.getType() == DROWN || result.getType() == VICTORY) {
                    drownCount ++;
                }
            }
            Log.d(TAG, "AILevelIII: Total Shoot Count: " + shootCount);
            assertEquals(7, drownCount);
        }
    }

    @Test
    public void AILevelImpossible() throws Exception {
        for (int i = 0; i < Maps.maps.length; i++) {
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
                if(result.getType() == DROWN || result.getType() == VICTORY) {
                    drownCount ++;
                }
            }
            Log.d(TAG, "AILevelImpossible: Total Shoot Count: " + shootCount);
            assertEquals(7, drownCount);
            assertNotEquals(100, shootCount);
        }
    }

    @Test
    public void testMaps() throws Exception {
        char[] symbols = new char[] {'I', 'O', 'T', 'S', 'Z', 'L', 'J', ' '};
        boolean valid = true;
        for (int i = 0; i < Maps.maps.length; i++) {
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
}
