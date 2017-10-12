package fr.wcs.battlegeek.controller;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;

import fr.wcs.battlegeek.model.Maps;
import fr.wcs.battlegeek.model.Result;
import fr.wcs.battlegeek.model.Settings;
import fr.wcs.battlegeek.ui.Tetromino;

import static fr.wcs.battlegeek.model.Result.Type.MISSED;

/**
 * Created by adphi on 03/10/17.
 */


public class AI {
    /**
     * Levels Enumerations
     */
    public enum Level {
        I ("Level I"),
        II ("Level II"),
        III ("Level III"),
        IMPOSSIBLE ("Level Impossible");

        private String name = "";

        Level(String level) {
            this.name = level;
        }

        public String toString() {
            return this.name;
        }
    }

    private final String TAG = "AI";
    private Level mLevel;
    private Point mLastPlayedCoordinates;
    private Result mLastResult = new Result(Tetromino.Shape.NONE, MISSED);


    private GameController mGameControler;
    private ArrayList<Point> mPlayablesCoordinates;
    private char[][] mPlayerMap;
    private ArrayList<Point> mSurroudingCoordinates = new ArrayList<>();

    /**
     * AI Constructor
     */
    public AI() {
        // Create a Game Controller
        mGameControler = new GameController(Maps.getMap());
        // Get all Playables Coordinates
        mPlayablesCoordinates = Maps.getPlayableCoordinates();
        mSurroudingCoordinates = new ArrayList<>();
    }

    /**
     * Method for the Player to Shot the AI
     *
     * @param x
     * @param y
     * @return
     */
    public Result shot(int x, int y) {
        // AI send the coordinates his controller to analyse the result and store the shot
        return mGameControler.shot(x, y);
    }

    /**
     * Method for getting AI play Coordinates
     *
     * @return the coordinates
     */
    public Point play() {
        switch (mLevel) {
            case I:
                return playLevelI();
            case II:
                return playLevelII();
            case III:
                return playLevelIII();
            case IMPOSSIBLE:
                return playLevelI();
        }
        return null;
    }

    /**
     * Method to call after the Player's game controller process the AI play method
     * this allow the AI'game processor to store the result in his Storage Map
     *
     * @param result
     */
    public void setResult(Result result) {
        mGameControler.setPlayResult(mLastPlayedCoordinates.x, mLastPlayedCoordinates.y, result);
        mLastResult = result;
    }

    /**
     * Method setting the AI Level
     *
     * @param level
     */
    public void setLevel(Level level) {
        mLevel = level;
        // Impossible Level Strategy
        if (level == Level.IMPOSSIBLE && mPlayerMap != null) {
            // We only need to get the coordinates of all the Items in the Player's Map
            mPlayablesCoordinates.clear();
            for (int i = 0; i < mPlayerMap.length; i++) {
                for (int j = 0; j < mPlayerMap[i].length; j++) {
                    if (mPlayerMap[i][j] != ' ') {
                        mPlayablesCoordinates.add(new Point(j, i));
                    }
                }
            }
        }
    }

    private Point playLevelI() {
        int index = (int) (Math.random() * (mPlayablesCoordinates.size() - 1));
        Point coordinates = mPlayablesCoordinates.get(index);
        mPlayablesCoordinates.remove(index);
        mLastPlayedCoordinates = coordinates;
        return coordinates;
    }

    private Point playLevelII() {
        // Give the type of result (missed, touched ...)
        Result.Type resultType = mLastResult.getType();
        if (mSurroudingCoordinates.isEmpty() && resultType == MISSED) {
            //Play randomly while nothing found
            return playLevelI();
        }

        //todo si touché , recup les coordonnées autour stockée dans le tableau mSurroundingshits

        if (resultType != MISSED){
            getSurroundingCoordinates(mLastPlayedCoordinates);
        }
        Log.d(TAG, "playLevelII: " + resultType + " " + mSurroudingCoordinates.size() + " " + mSurroudingCoordinates);
        int index = (int) (Math.random() * (mSurroudingCoordinates.size() - 1));
        Point coordinates = mSurroudingCoordinates.get(index);
        mSurroudingCoordinates.remove(index);
        mLastPlayedCoordinates = coordinates;
        return coordinates;
        //todo jouer coordonnées du tableau
        //todo supprimer coordonées du tableau en attente

    }

    private Point playLevelIII() {
        return playLevelII();
    }

    private void getSurroundingCoordinates(Point point) {
        //todo récuperer les coordonnées des points autour
        int x = point.x;
        int y = point.y;
        int minX = Math.max(x - 1, 0);
        int maxX = Math.min(x + 1, Settings.GRID_SIZE - 1);
        int minY = Math.max(y - 1, 0);
        int maxY = Math.min(y + 1, Settings.GRID_SIZE - 1);

        for (int row = minY; row <= maxY; row++) {
            for (int column = minX; column <= maxX; column++) {
                //todo vérifier qu'ils soient pas joués (utiliser la méthode mController , alreadyplayed)
                if (!mGameControler.alreadyPlayed(column, row)) {
                    //todo ne pas rajouter au tableau si deja dans le tableau
                    Point p = getPointFromPlayableCoordinates(column, row);
                    if (p != null) {
                        //todo rajouter au tableau les dispos
                        mSurroudingCoordinates.add(p);
                    }
                }
            }
        }
    }

    private Point getPointFromPlayableCoordinates(int x, int y) {
        Point point = new Point(x, y);
        for(Point p : mPlayablesCoordinates) {
            if (p.equals(point)) {
                mPlayablesCoordinates.remove(p);
                return p;
            }
        }
        return null;
    }

    /**
     * The Cheating Method: give the Player's Map to the AI
     *
     * @param playerMap
     */
    public void setPlayerMap(char[][] playerMap) {
        mPlayerMap = playerMap;
    }
}
