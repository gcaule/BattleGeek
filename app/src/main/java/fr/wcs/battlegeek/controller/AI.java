package fr.wcs.battlegeek.controller;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import fr.wcs.battlegeek.model.Bonus;
import fr.wcs.battlegeek.model.Maps;
import fr.wcs.battlegeek.model.Result;
import fr.wcs.battlegeek.model.Settings;
import fr.wcs.battlegeek.ui.Tetromino;
import fr.wcs.battlegeek.utils.Utils;

import static fr.wcs.battlegeek.model.Result.Type.BONUS;
import static fr.wcs.battlegeek.model.Result.Type.DROWN;
import static fr.wcs.battlegeek.model.Result.Type.MISSED;
import static fr.wcs.battlegeek.model.Result.Type.TOUCHED;
import static fr.wcs.battlegeek.ui.Tetromino.Shape.J;
import static fr.wcs.battlegeek.ui.Tetromino.Shape.L;
import static fr.wcs.battlegeek.ui.Tetromino.Shape.NONE;
import static fr.wcs.battlegeek.ui.Tetromino.Shape.O;
import static fr.wcs.battlegeek.ui.Tetromino.Shape.S;
import static fr.wcs.battlegeek.ui.Tetromino.Shape.T;
import static fr.wcs.battlegeek.ui.Tetromino.Shape.Z;

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
    private Bonus.Type mSelectedBonus = null;

    private Point mLastPlayedCoordinates;
    private Result mLastResult = new Result(-1, -1, NONE, MISSED, null);


    private GameController mGameControler;
    private ArrayList<Point> mPlayablesCoordinates;
    private ArrayList<Point> mProbableCoordinates;
    private char[][] mPlayerMap;
    private ArrayList<Point> mSurroudingCoordinates = new ArrayList<>();
    private HashMap<Tetromino.Shape, ArrayList<Point>> mShapeMap = new HashMap<>();
    private HashMap<Tetromino.Shape, ArrayList<Point>> mCheatMap = new HashMap<>();
    private Tetromino.Shape mLastTouchedShape = null;
    private ArrayList<Bonus.Type> mAvailablesBonuses = new ArrayList<>();

    /**
     * AI Constructor
     */
    public AI() {
        // Create a Game Controller
        mGameControler = new GameController(Maps.getMap());
        mGameControler.setBonus();
        Utils.printMap(mGameControler.getMap());
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
                return playLevelII();
            case IMPOSSIBLE:
                return playLevelImpossible();
        }
        return null;
    }

    /**
     * Method returning the Selected Bonus of th AI.
     * @return
     */
    public Bonus.Type getSelectedBonus() {
        return mSelectedBonus;
    }

    /**
     * Method to call after the Player's game controller process the AI play method
     * this allow the AI'game processor to store the result in his Storage Map and process
     * the result in order to adapt the AI strategy
     *
     * @param result
     */
    public void setResult(Result result) {
        mGameControler.setPlayResult(result);
        mLastResult = result;
        mLastPlayedCoordinates = new Point(result.getX(), result.getY());
        Result.Type resultType = result.getType();
        Tetromino.Shape resultShape = result.getShape();
        if(resultType == BONUS) {
            Bonus.Type bonus = mLastResult.getBonusType();
            // We don't want to implement MOVE Bonus
            if(bonus != Bonus.Type.MOVE) {
                mAvailablesBonuses.add(bonus);
            }
        }

        if(resultType == TOUCHED) {
            if (!mShapeMap.containsKey(resultShape)) {
                mShapeMap.put(resultShape, new ArrayList<Point>());
            }
            mShapeMap.get(resultShape).add(mLastPlayedCoordinates);
        }

        if(resultType == DROWN) {
            mShapeMap.remove(mLastTouchedShape);
            mPlayablesCoordinates.addAll(mSurroudingCoordinates);
            mSurroudingCoordinates.clear();
            mLastTouchedShape = mShapeMap.isEmpty() ? null : (Tetromino.Shape) mShapeMap.keySet().toArray()[0];
        }
    }

    /**
     * Method setting the AI Level
     *
     * @param level
     */
    public void setLevel(Level level) {
        mLevel = level;
        if(level != Level.IMPOSSIBLE) {
            mProbableCoordinates = getProbablePoints();
        }
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
                        if (!mCheatMap.containsKey(shape)) {
                            mCheatMap.put(shape, new ArrayList<Point>());
                        }
                        Point point = new Point(j, i);
                        mCheatMap.get(shape).add(point);

                    }
                }
            }
        }
    }

    /**
     * Method that process the shot's result to return AI plays Coordinates
     * Level 1 : Play randomly then play all around when TOUCHED a Tetromino
     * @return
     */
    private Point playLevelI() {
        // Give the type of result (missed, touched ...)
        Result.Type resultType = mLastResult.getType();
        // Get the type of Tetromino shape
        Tetromino.Shape resultShape = mLastResult.getShape();

        // AI Use REPLAY Bonus if Possible
        if(mAvailablesBonuses.contains(Bonus.Type.REPLAY)) {
            mSelectedBonus = Bonus.Type.REPLAY;
            mAvailablesBonuses.remove(Bonus.Type.REPLAY);
        }
        else if(resultType == MISSED){
            mSelectedBonus = null;
        }
        else if(mSelectedBonus == Bonus.Type.BOMB) {
            mSelectedBonus = null;
        }

        // BONUS CROSS FIRE
        if(mAvailablesBonuses.contains(Bonus.Type.BOMB)) {
            mSelectedBonus = Bonus.Type.BOMB;
            mAvailablesBonuses.remove(mSelectedBonus);
            // Get A Point
            mLastPlayedCoordinates = getRandomPoint(mProbableCoordinates);
            mPlayablesCoordinates.remove(mLastPlayedCoordinates);
            // Remove the surrounding Points
            ArrayList<Point> bombPoints = mGameControler.getSurrondingcoordinates(mLastPlayedCoordinates.x,
                    mLastPlayedCoordinates.y);
            mPlayablesCoordinates.removeAll(bombPoints);
            mSurroudingCoordinates.removeAll(bombPoints);
            return mLastPlayedCoordinates;
        }

        //Play randomly during hunt mode (nothing found and looking for tetromino)
        if (mSurroudingCoordinates.isEmpty() && (resultType == MISSED || resultType == BONUS)) {
            if(!mProbableCoordinates.isEmpty()) {
                mLastPlayedCoordinates = getRandomPoint(mProbableCoordinates);
                mPlayablesCoordinates.remove(mLastPlayedCoordinates);
            }
            else{
                mLastPlayedCoordinates = getRandomPoint(mPlayablesCoordinates);
            }

            return mLastPlayedCoordinates;
        }

        //When a boat is drown go back in hunt mode
        if (resultType == DROWN) {
            if(mLastTouchedShape != null) {
                ArrayList<Point> touchedPoint = mShapeMap.get(mLastTouchedShape);
                for(Point p : touchedPoint) {
                    getSurroundingCoordinates(p);
                }
                mLastPlayedCoordinates = getRandomPoint(mSurroudingCoordinates);
                mPlayablesCoordinates.remove(mLastPlayedCoordinates);
                return mLastPlayedCoordinates;
            }
            else {
                mLastPlayedCoordinates = getRandomPoint(mProbableCoordinates);
                mPlayablesCoordinates.remove(mLastPlayedCoordinates);
                return mLastPlayedCoordinates;
            }
        }

        //When a result type is touched, go in target mode by creating a map of possible coordinates

        if (resultType != MISSED && resultType != BONUS) {
            mLastTouchedShape = resultShape;
            getSurroundingCoordinates(mLastPlayedCoordinates);
        }

        //Shot in the possible coordinates (target mode)
        mLastPlayedCoordinates = getRandomPoint(mSurroudingCoordinates);
        mProbableCoordinates.remove(mLastPlayedCoordinates);
        return mLastPlayedCoordinates;
    }

    /**
     * Method that process the shot's result to return AI plays Coordinates
     * Level 2 : Play randomly in probables coordinates (counting empty surrounding cells)
     * When it founds a Tetromino it starts analysing the possibles coordinates of the
     * remaining Blocks.
     * @return
     */
    private Point playLevelII() {
        // Store the Result's Type and Shape
        Result.Type resultType = mLastResult.getType();
        Tetromino.Shape resultShape = mLastResult.getShape();

        // AI Use REPLAY Bonus if Possible
        if(mAvailablesBonuses.contains(Bonus.Type.REPLAY)) {
            mSelectedBonus = Bonus.Type.REPLAY;
            mAvailablesBonuses.remove(Bonus.Type.REPLAY);
        }
        else if(resultType == MISSED){
            mSelectedBonus = null;
        }
        else if(mSelectedBonus == Bonus.Type.BOMB) {
            mSelectedBonus = null;
        }

        // Update Probability Coordinates
        mProbableCoordinates = getProbablePoints();

        // We Touched Something !! Yihaaaaa !
        if(resultType == TOUCHED && mLastTouchedShape == null) {
            // Store the Shape to hunt
            mLastTouchedShape = resultShape;
            // Try to find Coordinates according to the Shape
            // We store the result directly in the mLastPlayCoordinates buffer
            mLastPlayedCoordinates = hunt(resultShape);
            // Remove Point for PlayablesCoordinates
            mPlayablesCoordinates.remove(mLastPlayedCoordinates);
            // Remove Point from ProbableCoordinates
            mProbableCoordinates.remove(mLastPlayedCoordinates);
            // Return the coordinates
            return mLastPlayedCoordinates;
        }
        // Are we hunting something ?
        else if(mLastTouchedShape != null) {
            // Let's hunt !
            mLastPlayedCoordinates = hunt(mLastTouchedShape);
            // Clean Duplicates
            mPlayablesCoordinates.remove(mLastPlayedCoordinates);
            mProbableCoordinates.remove(mLastPlayedCoordinates);
            // Shoooooooot !
            return mLastPlayedCoordinates;
        }
        // We don't have any Shape for hunting
        if(!mProbableCoordinates.isEmpty()) {
            // BONUS CROSS FIRE
            // Let's try to drop a Bomb
            if(mAvailablesBonuses.contains(Bonus.Type.BOMB)) {
                // Select the Bomb bonus
                mSelectedBonus = Bonus.Type.BOMB;
                // Bomb is not available anymore
                mAvailablesBonuses.remove(mSelectedBonus);
                // Get An interesting Point
                mLastPlayedCoordinates = getRandomPoint(mProbableCoordinates);
                // Clean duplicates
                mPlayablesCoordinates.remove(mLastPlayedCoordinates);
                // Remove the surrounding Points
                ArrayList<Point> bombPoints = mGameControler.getSurrondingcoordinates(mLastPlayedCoordinates.x,
                        mLastPlayedCoordinates.y);
                mPlayablesCoordinates.removeAll(bombPoints);
                mSurroudingCoordinates.removeAll(bombPoints);
                // Let's drop a Big Bomb
                return mLastPlayedCoordinates;
            }
            // We don't have a bomb, so, sadly try a point in probables Coordinates
            mLastPlayedCoordinates = getRandomPoint(mProbableCoordinates);
            // Cleaning
            mPlayablesCoordinates.remove(mLastPlayedCoordinates);
        }
        // We don't have Interesting Points (should not happen)
        else{
            mLastPlayedCoordinates = getRandomPoint(mPlayablesCoordinates);
        }
        return mLastPlayedCoordinates;
    }

    /**
     * Method return each point of the Cheating Hash Map, witch contains the position of all the
     * enemy Tetromino
     * @return
     */
    private Point playLevelImpossible() {
        for (Tetromino.Shape shape : mCheatMap.keySet()) {
            if(!mCheatMap.get(shape).isEmpty()) {
                ArrayList<Point> shapeCoordinates = mCheatMap.get(shape);
                mLastPlayedCoordinates = shapeCoordinates.get(0);
                shapeCoordinates.remove(mLastPlayedCoordinates);
                return mLastPlayedCoordinates;
            }
        }
        return null;
    }

    /**
     * Method hunting the Tetromino depending on the Shape
     * @param shape
     * @return
     */
    private Point hunt(Tetromino.Shape shape) {
        // Let's Start the Hunting with the Shape
        // Get the Shape's Founded Coordinates
        ArrayList<Point> foundedCoordinates = mShapeMap.get(shape);
        // Hunt Tetromino O
        if(shape == O) {
            // We only have found 1 point for now
            if(foundedCoordinates.size() == 1) {
                // Get surrounding Coordinates
                if(mLastResult.getType() == TOUCHED) {
                    getSurroundingCoordinates(foundedCoordinates.get(0));
                }
                mLastPlayedCoordinates = getRandomPoint(mSurroudingCoordinates);
                return mLastPlayedCoordinates;
            }
            // We have Two Points
            else if(foundedCoordinates.size() == 2) {
                // Cleaning
                mPlayablesCoordinates.addAll(mSurroudingCoordinates);
                mSurroudingCoordinates.clear();

                // What are this points
                Point point1 = foundedCoordinates.get(0);
                Point point2 = foundedCoordinates.get(1);

                // We have to try to find the Y value
                if(point1.x == point2.x) {
                    mSurroudingCoordinates.addAll(getSurroundingX(point1));
                }
                // We have to try to find the X value
                else if(point1.y == point2.y) {
                    mSurroudingCoordinates.addAll(getSurroundingY(point1));
                }
                // We have the X and Y value, so lets find the 2 remaining
                else {
                    Point p1 = getPointFromPlayableCoordinates(point1.x, point2.y);
                    Point p2 = getPointFromPlayableCoordinates(point2.x, point1.y);
                    if(p1 != null) mSurroudingCoordinates.add(p1);
                    if(p2 != null) mSurroudingCoordinates.add(p2);
                }

                return getRandomPoint(mSurroudingCoordinates);
            }
            //Let's find the last one
            else if(foundedCoordinates.size() == 3){
                ArrayList<Integer> xCoordinates = new ArrayList<>();
                ArrayList<Integer> yCoordinates = new ArrayList<>();
                for (Point point : foundedCoordinates) {
                    xCoordinates.add(point.x);
                    yCoordinates.add(point.y);
                }
                // Find X
                int x = 0;
                // We need to find the only X occurrence
                for (int i = 0; i < xCoordinates.size(); i++) {
                    if(Collections.frequency(xCoordinates, xCoordinates.get(i)) == 1){
                        x = xCoordinates.get(i);
                    }
                }

                // Find Y
                int y = 0;
                // We need to find the only Y occurrence
                for (int i = 0; i < yCoordinates.size(); i++) {
                    if(Collections.frequency(yCoordinates, yCoordinates.get(i)) == 1){
                        y = yCoordinates.get(i);
                    }
                }

                // We got it !!!
                return getPointFromPlayableCoordinates(x, y);
            }
        }
        // Oh my God !! We are hunting the easiest One
        else if(shape == Tetromino.Shape.I) {
            if(foundedCoordinates.size() == 1) {
                // Try to get the orientation
                getSurroundingCoordinates(foundedCoordinates.get(0));
                return getRandomPoint(mSurroudingCoordinates);
            }
            // We got the orientation
            else if(foundedCoordinates.size() >= 2) {
                mPlayablesCoordinates.addAll(mSurroudingCoordinates);
                mSurroudingCoordinates.clear();
                Point point1 = foundedCoordinates.get(0);
                Point point2 = foundedCoordinates.get(1);
                // Horizontal
                if(point1.x == point2.x) {
                    for(Point p : foundedCoordinates) {
                        mSurroudingCoordinates.addAll(getSurroundingY(p));
                    }
                }
                // Vertical
                else if(point1.y == point2.y) {
                    for(Point p : foundedCoordinates) {
                        mSurroudingCoordinates.addAll(getSurroundingX(p));
                    }
                }
                return  getRandomPoint(mSurroudingCoordinates);
            }
            // Let's find the Last One
            else if(foundedCoordinates.size() == 3) {
                mPlayablesCoordinates.addAll(mSurroudingCoordinates);
                mSurroudingCoordinates.clear();

                Point point1 = foundedCoordinates.get(0);
                Point point2 = foundedCoordinates.get(1);
                // Vertical
                if(point1.x == point2.x) {
                    int x = point1.x;
                    int minY = min(foundedCoordinates, "y");
                    int maxY = max(foundedCoordinates, "y");
                    // Try before and after the 3 known blocks (X axis)
                    if(minY - 1 >= 0 && !mGameControler.alreadyPlayed(x, minY - 1)) {
                        mSurroudingCoordinates.add(new Point(x, minY - 1));
                    }
                    if(maxY + 1 < Settings.GRID_SIZE && !mGameControler.alreadyPlayed(x, maxY + 1)) {
                        mSurroudingCoordinates.add(new Point(x, maxY + 1));
                    }
                }
                // Horizontal
                else {
                    int y = point1.y;
                    int minX = min(foundedCoordinates, "x");
                    int maxX = max(foundedCoordinates, "x");
                    // Try before and after the 3 known blocks (Y axis)
                    if(minX - 1 >= 0 && !mGameControler.alreadyPlayed(minX - 1, y)) {
                        mSurroudingCoordinates.add(new Point(minX - 1, y));
                    }
                    if(maxX + 1 < Settings.GRID_SIZE && !mGameControler.alreadyPlayed(maxX + 1, y)) {
                        mSurroudingCoordinates.add(new Point(maxX + 1, y));
                    }
                }
                return getRandomPoint(mSurroudingCoordinates);

            }
        }
        // The Tetromino T is kind of funny
        else if(shape == T && foundedCoordinates.size() == 3) {
            mPlayablesCoordinates.addAll(mSurroudingCoordinates);
            mSurroudingCoordinates.clear();
            // Let's see... what do we got ?
            Point point1 = foundedCoordinates.get(0);
            Point point2 = foundedCoordinates.get(1);
            Point point3 = foundedCoordinates.get(2);
            // Vertical ?
            if(point1.x == point2.x && point2.x == point3.x) {
                // Find Middle
                ArrayList<Integer> yCoordinates = new ArrayList<>();
                yCoordinates.add(point1.y);
                yCoordinates.add(point2.y);
                yCoordinates.add(point3.y);
                Collections.sort(yCoordinates);
                int x = point1.x;
                int y = yCoordinates.get(1);
                // Let's try to get the last One.
                if(x - 1 >= 0 && !mGameControler.alreadyPlayed(x - 1,y)) mSurroudingCoordinates.add(new Point(x - 1, y));
                if(x + 1 < Settings.GRID_SIZE && !mGameControler.alreadyPlayed(x + 1, y)) mSurroudingCoordinates.add
                        (new Point(x + 1, y));
                // Feel Lucky ?
                return getRandomPoint(mSurroudingCoordinates);
            }
            // Horizontal ?
            else if(point1.y == point2.y && point2.y == point3.y) {
                // Find Middle
                ArrayList<Integer> xCoordinates = new ArrayList<>();
                xCoordinates.add(point1.x);
                xCoordinates.add(point2.x);
                xCoordinates.add(point3.x);
                Collections.sort(xCoordinates);
                int y = point1.y;
                int x = xCoordinates.get(1);
                // The last One is not that far away
                if(y - 1 >= 0 && !mGameControler.alreadyPlayed(x, y - 1)) mSurroudingCoordinates.add
                        (new Point(x, y - 1));
                if(y + 1 < Settings.GRID_SIZE && !mGameControler.alreadyPlayed(x, y + 1)) mSurroudingCoordinates.add
                        (new Point(x, y + 1));
                // Let's try something
                return getRandomPoint(mSurroudingCoordinates);
            }

            // Common Shape
            else {
                // Let's find the Pair of x and y of the last Block
                ArrayList<Integer> xCoordinates = new ArrayList<>();
                ArrayList<Integer> yCoordinates = new ArrayList<>();
                for (Point point : foundedCoordinates) {
                    xCoordinates.add(point.x);
                    yCoordinates.add(point.y);
                }
                // We need the x Axis (2 points with same X)
                int x = 0;
                for (int i = 0; i < xCoordinates.size(); i++) {
                    if(Collections.frequency(xCoordinates, xCoordinates.get(i)) == 2){
                        x = xCoordinates.get(i);
                    }
                }
                // We need the y Axis (2 points with same Y)
                int y = 0;
                for (int i = 0; i < yCoordinates.size(); i++) {
                    if(Collections.frequency(yCoordinates, yCoordinates.get(i)) == 2){
                        y = yCoordinates.get(i);
                    }
                }
                getSurroundingCoordinates(new Point(x,y));
                return getRandomPoint(mSurroudingCoordinates);
            }


        }
        else if((shape == L || shape == J) && foundedCoordinates.size() > 2) {
            mPlayablesCoordinates.addAll(mSurroudingCoordinates);
            mSurroudingCoordinates.clear();
            Point point1 = foundedCoordinates.get(0);
            Point point2 = foundedCoordinates.get(1);
            Point point3 = foundedCoordinates.get(2);
            // Vertical
            if(point1.x == point2.x && point2.x == point3.x) {
                int x = point1.x;
                // find y min and max
                int minY = min(foundedCoordinates, "y");
                int maxY = max(foundedCoordinates, "y");
                if(shape == L) {
                    if(x - 1 >= 0 && !mGameControler.alreadyPlayed(x - 1, minY)) {
                        mSurroudingCoordinates.add(new Point(x - 1, minY));
                    }
                    if(x + 1 < Settings.GRID_SIZE && !mGameControler.alreadyPlayed(x + 1, maxY)) {
                        mSurroudingCoordinates.add(new Point(x + 1, maxY));
                    }
                }
                // Shape J
                else {
                    if(x - 1 >= 0 && !mGameControler.alreadyPlayed(x - 1, maxY)) {
                        mSurroudingCoordinates.add(new Point(x - 1, maxY));
                    }
                    if(x + 1 < Settings.GRID_SIZE && !mGameControler.alreadyPlayed(x + 1, minY)) {
                        mSurroudingCoordinates.add(new Point(x + 1, minY));
                    }
                }
            }
            // Horizontal
            else if(point1.y == point2.y && point2.y == point3.y) {
                int y = point1.y;
                // find y min and max
                int minX = min(foundedCoordinates, "x");
                int maxX = max(foundedCoordinates, "x");
                if(shape == L) {
                    if(y - 1 >= 0 && !mGameControler.alreadyPlayed(maxX, y - 1)) {
                        mSurroudingCoordinates.add(new Point(maxX, y - 1));
                    }
                    if(y + 1 < Settings.GRID_SIZE && !mGameControler.alreadyPlayed(minX, y + 1)) {
                        mSurroudingCoordinates.add(new Point(minX, y + 1));
                    }
                }
                // Shape J
                else {
                    if(y - 1 >= 0 && !mGameControler.alreadyPlayed(minX, y - 1)) {
                        mSurroudingCoordinates.add(new Point(minX, y - 1));
                    }
                    if(y + 1 < Settings.GRID_SIZE && !mGameControler.alreadyPlayed(maxX, y + 1)) {
                        mSurroudingCoordinates.add(new Point(maxX, y + 1));
                    }
                }
            }
            // Common Shape
            else {
                // Which rotation ?
                int rotation = getCommonShapeMatrixRotation(foundedCoordinates);
                int minX = min(foundedCoordinates, "x");
                int maxX = max(foundedCoordinates, "x");
                int minY = min(foundedCoordinates, "y");
                int maxY = max(foundedCoordinates, "y");
                if(rotation == 0) {
                    if (shape == L) {
                        mSurroudingCoordinates.add(new Point(maxX + 1, minY));
                    } else {
                        mSurroudingCoordinates.add(new Point(minX, maxY + 1));
                    }
                }
                else if (rotation == 1) {
                    if (shape == L) {
                        mSurroudingCoordinates.add(new Point(maxX, maxY + 1));
                    } else {
                        mSurroudingCoordinates.add(new Point(minX - 1, minY));
                    }
                }
                else if (rotation == 2) {
                    if (shape == L) {
                        mSurroudingCoordinates.add(new Point(minX - 1, maxY));
                    } else {
                        mSurroudingCoordinates.add(new Point(maxX, minY - 1));
                    }
                }
                else if (rotation == 3) {
                    if (shape == L) {
                        mSurroudingCoordinates.add(new Point(minX, minY - 1));
                    } else {
                        mSurroudingCoordinates.add(new Point(maxX + 1, maxY));
                    }
                }
            }

            return getRandomPoint(mSurroudingCoordinates);
        }
        else if((shape == S || shape == Z) && foundedCoordinates.size() == 3) {
            mPlayablesCoordinates.addAll(mSurroudingCoordinates);
            mSurroudingCoordinates.clear();
            int rotation = getCommonShapeMatrixRotation(foundedCoordinates);
            int minX = min(foundedCoordinates, "x");
            int maxX = max(foundedCoordinates, "x");
            int minY = min(foundedCoordinates, "y");
            int maxY = max(foundedCoordinates, "y");
            if(rotation == 0) {
                if(shape == S) {
                    mSurroudingCoordinates.add(new Point(minX - 1, maxY));
                }
                else {
                    mSurroudingCoordinates.add(new Point(maxX, minY - 1));
                }
            }
            else if(rotation == 1) {
                if(shape == S) {
                    mSurroudingCoordinates.add(new Point(minX, minY - 1));
                }
                else {
                    mSurroudingCoordinates.add(new Point(maxX + 1, maxY));
                }
            }
            else if(rotation == 2) {
                if(shape == S) {
                    mSurroudingCoordinates.add(new Point(maxX + 1, minY));
                }
                else {
                    mSurroudingCoordinates.add(new Point(minX, maxY + 1));
                }
            }
            else if(rotation == 3) {
                if(shape == S) {
                    mSurroudingCoordinates.add(new Point(maxX, maxY + 1));
                }
                else {
                    mSurroudingCoordinates.add(new Point(minX - 1, minY));
                }
            }
            return getRandomPoint(mSurroudingCoordinates);
        }
        else {
            if(foundedCoordinates.size() > 0) {
                for(Point p : foundedCoordinates) {
                    getSurroundingCoordinates(p);
                }
            }
            else if (mLastResult.getType() == TOUCHED) {
                getSurroundingCoordinates(mLastPlayedCoordinates);
            }
            return getRandomPoint(mSurroudingCoordinates);
        }
        return null;
    }

    // Common Shape Rotation's Matrix Definition
    private int[][] commonShapeRotation1 = new int[][]{
            {1,1},
            {1,0}
    };
    private int[][] commonShapeRotation2 = new int[][]{
            {1,1},
            {0,1}
    };
    private int[][] commonShapeRotation3 = new int[][]{
            {0,1},
            {1,1}
    };
    private int[][] commonShapeRotation4 = new int[][]{
            {1,0},
            {1,1}
    };

    /**
     * Method returning the rotation's ID if the common shape.
     * The common shape is present in all Tetrominos except I.
     * @param points
     * @return
     */
    private int getCommonShapeMatrixRotation(ArrayList<Point> points) {
        // Rotations Definitions
        ArrayList<Point> pointsCopy = Utils.copyPoints(points);
        int minX = min(pointsCopy, "x");
        int minY = min(pointsCopy, "y");

        // Apply Offset && Convert to matrix
        int[][] matrix = new int[][]{{0,0},{0,0}};
        for (Point p : pointsCopy) {
            p.x -= minX;
            p.y -= minY;
            matrix[p.y][p.x] = 1;
        }

        ArrayList<int[][]> rotations = new ArrayList<>();
        rotations.add(commonShapeRotation1);
        rotations.add(commonShapeRotation2);
        rotations.add(commonShapeRotation3);
        rotations.add(commonShapeRotation4);

        for (int i = 0; i < rotations.size(); i++) {
            if(Arrays.deepEquals(matrix, rotations.get(i))) {
                return i;
            }
        }

        return - 1;
    }

    /**
     * Method return the minimum for the selected axis in a Point ArrayList
     * @param points
     * @param axis the axis : "x" or "y"
     * @return
     */
    private int min(ArrayList<Point> points, String axis) {
        if(axis.equals("x")) {
            int minX = points.get(0).x;
            for (Point p : points) {
                if (p.x < minX) {
                    minX = p.x;
                }
            }
            return minX;
        }
        else {
            int minY = points.get(0).y;
            for (Point p : points) {
                if (p.y < minY) {
                    minY = p.y;
                }
            }
            return minY;
        }
    }

    /**
     * Method return the maximum for the selected axis in a Point ArrayList
     * @param points
     * @param axis the axis : "x" or "y"
     * @return
     */
    private int max(ArrayList<Point> points, String axis) {
        if(axis.equals("x")) {
            int maxX = points.get(0).x;
            for (Point p : points) {
                if (p.x > maxX) {
                    maxX = p.x;
                }
            }
            return maxX;
        }
        else {
            int maxY = points.get(0).y;
            for (Point p : points) {
                if (p.y > maxY) {
                    maxY = p.y;
                }
            }
            return maxY;
        }
    }

    /**
     * Method returning the empty cells surrounding the given Point on the X axis
     * @param point
     * @return
     */
    private ArrayList<Point> getSurroundingX(Point point) {
        ArrayList<Point> surroundingX = new ArrayList<>();
        int minX = Math.max(point.x - 1, 0);
        int maxX = Math.min(point.x + 1, Settings.GRID_SIZE - 1);
        for (int i = minX; i <= maxX; i++) {
            Point p = getPointFromPlayableCoordinates(i, point.y);
            if(p != null) {
                surroundingX.add(p);
            }
        }
        return surroundingX;
    }

    /**
     * Method returning the empty cells surrounding the given Point on the Y axis
     * @param point
     * @return
     */
    private ArrayList<Point> getSurroundingY(Point point) {
        ArrayList<Point> surroundingY = new ArrayList<>();
        int minY = Math.max(point.y - 1, 0);
        int maxY = Math.min(point.y + 1, Settings.GRID_SIZE - 1);
        for (int i = minY; i <= maxY; i++) {
            Point p = getPointFromPlayableCoordinates(point.x, i);
            if(p != null) {
                surroundingY.add(p);
            }
        }
        return surroundingY;
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
     * Method returning an ArrayList of Points based on the empty cells' count
     * @return
     */
    private ArrayList<Point> getProbablePoints(){
        ArrayList<Point> probablePoints = new ArrayList<>();
        // Try to find the points with at least 6 empty cells around it
        for(Point p : mPlayablesCoordinates) {
            int minX = Math.max(p.x - 1, 0);
            int maxX = Math.min(p.x + 1, Settings.GRID_SIZE - 1);
            int minY = Math.max(p.y - 1, 0);
            int maxY = Math.min(p.y + 1, Settings.GRID_SIZE - 1);
            int count = 0;
            for (int i = minX; i <= maxX; i++) {
                for (int j = minY; j <= maxY; j++) {
                    if(!mGameControler.alreadyPlayed(i,j)) {
                        count++ ;
                    }
                }
            }
            if(count >= 6) {
                probablePoints.add(p);
            }
        }
        // If non empty return the points
        if(probablePoints.size() != 0) {
            return probablePoints;
        }
        // If empty we try with less surrounding empty cells
        else {
            for(Point p : mPlayablesCoordinates) {
                ArrayList<Point> surrondingFreeCells = mGameControler.getSurrondingcoordinates(p.x, p.y);
                if(surrondingFreeCells.size() >= 3) {
                    probablePoints.add(p);
                }
            }
            return probablePoints;
        }
    }

    /**
     * Method returning the AI's Game Controller;
     * @return
     */
    public GameController getGameController() {
        return mGameControler;
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
