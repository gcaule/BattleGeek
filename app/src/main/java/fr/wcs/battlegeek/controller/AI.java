package fr.wcs.battlegeek.controller;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import fr.wcs.battlegeek.model.Maps;
import fr.wcs.battlegeek.model.Result;
import fr.wcs.battlegeek.model.Settings;
import fr.wcs.battlegeek.ui.Tetromino;

import static fr.wcs.battlegeek.model.Result.Type.BONUS;
import static fr.wcs.battlegeek.model.Result.Type.DROWN;
import static fr.wcs.battlegeek.model.Result.Type.MISSED;
import static fr.wcs.battlegeek.ui.Tetromino.Shape.NONE;

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

    private final String TAG = Settings.TAG;
    private Level mLevel;
    private Point mLastPlayedCoordinates;
    private Result mLastResult = new Result(NONE, MISSED, null);


    private GameController mGameControler;
    private ArrayList<Point> mPlayablesCoordinates;
    private char[][] mPlayerMap;
    private ArrayList<Point> mSurroudingCoordinates = new ArrayList<>();
    private HashMap<Tetromino.Shape, ArrayList<Point>> mShapeMap = new HashMap<>();
    private Tetromino.Shape mLastTouchedShape;

    /**
     * AI Constructor
     */
    public AI() {
        // Create a Game Controller
        mGameControler = new GameController(Maps.getMap());
        mGameControler.setBonus();
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
                return playLevelImpossible();
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
        if ((level == Level.III || level == Level.IMPOSSIBLE) && mPlayerMap != null) {
            // We only need to get the coordinates of all the Items in the Player's Map
            for (int i = 0; i < mPlayerMap.length; i++) {
                for (int j = 0; j < mPlayerMap[i].length; j++) {
                    // If not a Bonus or Empty
                    if (mPlayerMap[i][j] != ' ' && mPlayerMap[i][j] != '+'
                            && mPlayerMap[i][j] != '-' && mPlayerMap[i][j] != '=') {
                        String symbol = String.valueOf(mPlayerMap[i][j]);
                        Tetromino.Shape shape = Tetromino.Shape.valueOf(symbol);
                        if (!mShapeMap.containsKey(shape)) {
                            mShapeMap.put(shape, new ArrayList<Point>());
                        }
                        Point point = getPointFromPlayableCoordinates(j, i);
                        mShapeMap.get(shape).add(point);

                    }
                }
            }
            Log.d(TAG, "setLevel: " + mShapeMap);
        }
    }

    //Level 1 : Play randomly
    private Point playLevelI() {
        int index = (int) (Math.random() * (mPlayablesCoordinates.size() - 1));
        Point coordinates = mPlayablesCoordinates.get(index);
        mPlayablesCoordinates.remove(index);
        mLastPlayedCoordinates = coordinates;
        return coordinates;
    }

    //Level 2 : Play randomly then play all around when TOUCHED a tetromino
    private Point playLevelII() {
        // Give the type of result (missed, touched ...)
        Result.Type resultType = mLastResult.getType();
        // Get the type of Tetromino shape
        Tetromino.Shape resultShape = mLastResult.getShape();

        //Play randomly during hunt mode (nothing found and looking for tetromino)
        if (mSurroudingCoordinates.isEmpty() && (resultType == MISSED || resultType == BONUS)) {
            return playLevelI();
        }

        //When a boat is drown, clear SurroudingCoordinates to go back in hunt mode
        if (resultType == DROWN) {
            for (Point coordinates : mSurroudingCoordinates) {
                mPlayablesCoordinates.add(coordinates);
            }
            mSurroudingCoordinates.clear();

            mShapeMap.remove((mLastResult.getShape()));

            for (HashMap.Entry<Tetromino.Shape, ArrayList<Point>> entry : mShapeMap.entrySet()) {
                Tetromino.Shape shape = entry.getKey();
                ArrayList<Point> pointArrayList = entry.getValue();
                for (Point currentPoint : pointArrayList) {
                    getSurroundingCoordinates(currentPoint);
                }
            }
            return playLevelI();
        }

        //When a result type is touched, go in target mode by creating a map of possible coordinates

        if (resultType != MISSED && resultType != BONUS) {
            if (!mShapeMap.containsKey(resultShape)) {
                mShapeMap.put(resultShape, new ArrayList<Point>());
            }
            mShapeMap.get(resultShape).add(mLastPlayedCoordinates);
            getSurroundingCoordinates(mLastPlayedCoordinates);
        }

        //Shot in the possible coordinates (target mode)
        int index = (int) (Math.random() * (mSurroudingCoordinates.size() - 1));
        Point coordinates = mSurroudingCoordinates.get(index);
        mSurroudingCoordinates.remove(index);
        mLastPlayedCoordinates = coordinates;
        return coordinates;
    }

    private Point playLevelIII() {
        /*if(mLastPlayedCoordinates == null) {
            return playLevelI();
        }

        int random = (int)(Math.random() * 100);
        Tetromino.Shape lastShape = mLastResult.getShape();
        Result.Type lastResultType = mLastResult.getType();

        if(lastResultType == DROWN) {
            mPlayablesCoordinates.addAll(mSurroudingCoordinates);
            mSurroudingCoordinates.clear();
        }

        if(lastShape != NONE && lastResultType != DROWN) {
            mLastTouchedShape = lastShape;
        }

        if(lastResultType == DROWN) {
            mLastTouchedShape = null;
        }

        if (random < Settings.LEVEL_III_PROBABILITY && mLastTouchedShape != null) {
            ArrayList<Point> shapeCoordinates = mShapeMap.get(mLastTouchedShape);
            mLastPlayedCoordinates = getRandomPoint(shapeCoordinates);
            shapeCoordinates.remove(mLastPlayedCoordinates);
            mPlayablesCoordinates.remove(mLastPlayedCoordinates);
            Log.d(TAG, "playLevelIII: PlayableCoordinates Size : " + mPlayablesCoordinates.size());
            return mLastPlayedCoordinates;
        }
        else if(random < Settings.LEVEL_III_PROBABILITY && mLastTouchedShape == null) {
            Tetromino.Shape[] shapes = Tetromino.Shape.values();
            shapes = Arrays.copyOf(shapes, shapes.length - 1);
            int randomIndex = (int)(Math.random() * (shapes.length - 1));
            Tetromino.Shape randomShape = shapes[randomIndex];
            mLastPlayedCoordinates = getRandomPoint(mShapeMap.get(randomShape));
            mPlayablesCoordinates.remove(mLastPlayedCoordinates);
            return mLastPlayedCoordinates;
        }

        else if(mLastTouchedShape != NONE && mSurroudingCoordinates.isEmpty()){
            getSurroundingCoordinates(mLastPlayedCoordinates);
            mLastPlayedCoordinates = getRandomPoint(mSurroudingCoordinates);
            return mLastPlayedCoordinates;
        }

        else if (!mSurroudingCoordinates.isEmpty() && lastResultType != MISSED) {
            mLastPlayedCoordinates = getRandomPoint(mSurroudingCoordinates);
            return mLastPlayedCoordinates;
        }*/

        return playLevelII();
    }

    private Point playLevelImpossible() {
        for (Tetromino.Shape shape : mShapeMap.keySet()) {
            if(!mShapeMap.get(shape).isEmpty()) {
                ArrayList<Point> shapeCoordinates = mShapeMap.get(shape);
                mLastPlayedCoordinates = shapeCoordinates.get(0);
                shapeCoordinates.remove(mLastPlayedCoordinates);
                return mLastPlayedCoordinates;
            }
        }
        return null;
    }

    /**
     * Set the Surrounding Coordinates of the Point in mSurroudingCoordinates
     * @param point
     */
    private void getSurroundingCoordinates(Point point) {

        int[] tempX = new int[4];
        int[] tempY = new int[4];

        int nordX = point.x;
        tempX[0] = nordX;

        int nordY = Math.max(point.y - 1, 0);
        tempY[0] = nordY;

        int sudX = point.x;
        tempX[1] = sudX;

        int sudY = Math.min(point.y + 1, Settings.GRID_SIZE - 1);
        tempY[1] = sudY;

        int ouestX = Math.max(point.x - 1, 0);
        tempX[2] = ouestX;

        int ouestY = point.y;
        tempY[2] = ouestY;

        int estX = Math.min(point.x + 1, Settings.GRID_SIZE - 1);
        tempX[3] = estX;

        int estY = point.y;
        tempY[3] = estY;

        int row = 0;
        int column = 0;

        for (int i = 0; i < tempX.length; i++) {
            //vérifier qu'ils soient pas joués (utiliser la méthode mController , alreadyplayed)
            if (!mGameControler.alreadyPlayed(tempX[column], tempY[row])) {
                //ne pas rajouter au tableau si deja dans le tableau
                Point p = getPointFromPlayableCoordinates(tempX[column], tempY[row]);
                if (p != null) {
                    //rajouter au tableau les dispos
                    mSurroudingCoordinates.add(p);
                }
            }
            row++;
            column++;
        }
    }

    /**
     * Get the Point from mPlayablesCoordinates (avoiding duplicates)
     * @param x
     * @param y
     * @return
     */
    private Point getPointFromPlayableCoordinates(int x, int y) {
        Point point = new Point(x, y);
        for (Point p : mPlayablesCoordinates) {
            if (p.equals(point)) {
                mPlayablesCoordinates.remove(p);
                return p;
            }
        }
        return null;
    }

    /**
     * Get (and Delete) a Random Point from the Point's Array
     * @param array
     * @return
     */
    private Point getRandomPoint(ArrayList<Point> array) {
        int index = (int)(Math.random() * (array.size() - 1));
        Point returnedPoint = array.get(index);
        array.remove(index);
        return returnedPoint;
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
