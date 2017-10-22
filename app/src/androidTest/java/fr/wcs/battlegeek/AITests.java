package fr.wcs.battlegeek;

import android.content.Context;
import android.graphics.Point;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import fr.wcs.battlegeek.controller.AI;
import fr.wcs.battlegeek.controller.GameController;
import fr.wcs.battlegeek.model.Maps;
import fr.wcs.battlegeek.model.Result;

import static fr.wcs.battlegeek.model.Result.Type.VICTORY;
import static fr.wcs.battlegeek.ui.Tetromino.Shape.NONE;
import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("fr.wcs.battlegeek", appContext.getPackageName());
    }

    @Test
    public void AILevelI() throws Exception {
        AI ai = new AI();
        ai.setLevel(AI.Level.I);

        char[][] map = Maps.getMap();
        GameController gameController = new GameController(map);
        Result result = new Result(0,0,NONE, Result.Type.MISSED,null);
        while(result.getType() != VICTORY) {
            Point p = ai.play();
            result = gameController.shot(p.x, p.y);
        }

        assertEquals(VICTORY, result.getType());
    }

    @Test
    public void AILevelII() throws Exception {
        AI ai = new AI();
        ai.setLevel(AI.Level.II);

        char[][] map = Maps.getMap();
        GameController gameController = new GameController(map);
        Result result = new Result(0,0,NONE, Result.Type.MISSED,null);
        while(result.getType() != VICTORY) {
            Point p = ai.play();
            result = gameController.shot(p.x, p.y);
        }

        assertEquals(VICTORY, result.getType());
    }

    @Test
    public void AILevelIII() throws Exception {
        AI ai = new AI();
        ai.setLevel(AI.Level.III);

        char[][] map = Maps.getMap();
        GameController gameController = new GameController(map);
        Result result = new Result(0,0,NONE, Result.Type.MISSED,null);
        while(result.getType() != VICTORY) {
            Point p = ai.play();
            result = gameController.shot(p.x, p.y);
        }

        assertEquals(VICTORY, result.getType());
    }

    @Test
    public void AILevelImpossible() throws Exception {
        char[][] map = Maps.getMap();
        AI ai = new AI();
        ai.setPlayerMap(map);
        ai.setLevel(AI.Level.IMPOSSIBLE);
        GameController gameController = new GameController(map);
        Result result = new Result(0,0,NONE, Result.Type.MISSED,null);
        while(result.getType() != VICTORY) {
            Point p = ai.play();
            result = gameController.shot(p.x, p.y);
        }

        assertEquals(VICTORY, result.getType());
    }
}
