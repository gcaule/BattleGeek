package fr.wcs.battlegeek.controller;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

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
    private Point mLastPlayedCoordinates;
    private Result mLastResult = new Result(-1, -1, NONE, MISSED, null);


    private GameController mGameControler;
    private ArrayList<Point> mPlayablesCoordinates;
    private ArrayList<Point> mEvenCoordinates;
    private char[][] mPlayerMap;
    private ArrayList<Point> mSurroudingCoordinates = new ArrayList<>();
    private HashMap<Tetromino.Shape, ArrayList<Point>> mShapeMap = new HashMap<>();
    private HashMap<Tetromino.Shape, ArrayList<Point>> mCheatMap = new HashMap<>();
    private Tetromino.Shape mLastTouchedShape = null;

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
        mGameControler.setPlayResult(result);
        mLastResult = result;
    }

    /**
     * Method setting the AI Level
     *
     * @param level
     */
    public void setLevel(Level level) {
        mLevel = level;
        if(level == Level.II || level == Level.III) {
            mEvenCoordinates = getEvenCoordinates();
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
            Log.d(TAG, "setLevel: " + mShapeMap);
        }
    }

    //Level 1 : Play randomly
    private Point playLevelI() {
        mLastPlayedCoordinates = getRandomPoint(mPlayablesCoordinates);
        return mLastPlayedCoordinates;
    }

    //Level 2 : Play randomly then play all around when TOUCHED a tetromino
    private Point playLevelII() {
        // Give the type of result (missed, touched ...)
        Result.Type resultType = mLastResult.getType();
        // Get the type of Tetromino shape
        Tetromino.Shape resultShape = mLastResult.getShape();

        //Play randomly during hunt mode (nothing found and looking for tetromino)
        if (mSurroudingCoordinates.isEmpty() && (resultType == MISSED || resultType == BONUS)) {
            if(!mEvenCoordinates.isEmpty()) {
                mLastPlayedCoordinates = getRandomPoint(mEvenCoordinates);
                mPlayablesCoordinates.remove(mLastPlayedCoordinates);
            }
            else{
                mLastPlayedCoordinates = getRandomPoint(mPlayablesCoordinates);
            }

            return mLastPlayedCoordinates;
        }

        //When a boat is drown, clear SurroundingCoordinates to go back in hunt mode
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
            mLastPlayedCoordinates = getRandomPoint(mEvenCoordinates);
            Log.d(TAG, "playLevelII: " + mPlayablesCoordinates.size());
            mPlayablesCoordinates.remove(mLastPlayedCoordinates);
            Log.d(TAG, "playLevelII: " + mPlayablesCoordinates.size());
            return mLastPlayedCoordinates;
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

        mLastPlayedCoordinates = getRandomPoint(mSurroudingCoordinates);
        mEvenCoordinates.remove(mLastPlayedCoordinates);
        return mLastPlayedCoordinates;
    }

    private Point playLevelIII() {
        Result.Type resultType = mLastResult.getType();
        Tetromino.Shape resultShape = mLastResult.getShape();

        /*// TODO : Remove Debug Code
        if(mPlayablesCoordinates.size() == 100) {
            mLastPlayedCoordinates = mCheatMap.get(Z).get(0);
            mPlayablesCoordinates.remove(mLastPlayedCoordinates);
            return mLastPlayedCoordinates;
        }*/

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

        if(resultType == TOUCHED && mLastTouchedShape == null) {
            mLastTouchedShape = resultShape;
            mLastPlayedCoordinates = hunt(resultShape);
            mPlayablesCoordinates.remove(mLastPlayedCoordinates);
            mEvenCoordinates.remove(mLastPlayedCoordinates);
            return mLastPlayedCoordinates;
        }
        else if(mLastTouchedShape != null) {
            mLastPlayedCoordinates = hunt(mLastTouchedShape);
            mPlayablesCoordinates.remove(mLastPlayedCoordinates);
            mEvenCoordinates.remove(mLastPlayedCoordinates);
            return mLastPlayedCoordinates;
        }

        if(!mEvenCoordinates.isEmpty()) {
            mLastPlayedCoordinates = getRandomPoint(mEvenCoordinates);
            mPlayablesCoordinates.remove(mLastPlayedCoordinates);
        }
        else{
            mLastPlayedCoordinates = getRandomPoint(mPlayablesCoordinates);
        }
        return mLastPlayedCoordinates;
    }

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

    private Point hunt(Tetromino.Shape shape) {
        ArrayList<Point> foundedCoordinates = mShapeMap.get(shape);

        if(shape == O) {
            if(foundedCoordinates.size() == 1) {
                if(mLastResult.getType() == TOUCHED) {
                    getSurroundingCoordinates(foundedCoordinates.get(0));
                }
                mLastPlayedCoordinates = getRandomPoint(mSurroudingCoordinates);
                return mLastPlayedCoordinates;
            }
            else if(foundedCoordinates.size() == 2) {
                mPlayablesCoordinates.addAll(mSurroudingCoordinates);
                mSurroudingCoordinates.clear();

                Point point1 = foundedCoordinates.get(0);
                Point point2 = foundedCoordinates.get(1);

                if(point1.x == point2.x) {
                    mSurroudingCoordinates.addAll(getSurroundingX(point1));
                }
                else if(point1.y == point2.y) {
                    mSurroudingCoordinates.addAll(getSurroundingY(point1));
                }
                else {
                    Point p1 = getPointFromPlayableCoordinates(point1.x, point2.y);
                    Point p2 = getPointFromPlayableCoordinates(point2.x, point1.y);
                    if(p1 != null) mSurroudingCoordinates.add(p1);
                    if(p2 != null) mSurroudingCoordinates.add(p2);
                }

                return getRandomPoint(mSurroudingCoordinates);
            }
            else if(foundedCoordinates.size() == 3){
                ArrayList<Integer> xCoordinates = new ArrayList<>();
                ArrayList<Integer> yCoordinates = new ArrayList<>();
                for (Point point : foundedCoordinates) {
                    xCoordinates.add(point.x);
                    yCoordinates.add(point.y);
                }
                int x = 0;
                for (int i = 0; i < xCoordinates.size(); i++) {
                    if(Collections.frequency(xCoordinates, xCoordinates.get(i)) == 1){
                        x = xCoordinates.get(i);
                    }
                }

                int y = 0;
                for (int i = 0; i < yCoordinates.size(); i++) {
                    if(Collections.frequency(yCoordinates, yCoordinates.get(i)) == 1){
                        y = yCoordinates.get(i);
                    }
                }

                return getPointFromPlayableCoordinates(x, y);
            }
        }
        else if(shape == Tetromino.Shape.I) {
            if(foundedCoordinates.size() == 1) {
                getSurroundingCoordinates(foundedCoordinates.get(0));
                return getRandomPoint(mSurroudingCoordinates);
            }
            else if(foundedCoordinates.size() >= 2) {
                mPlayablesCoordinates.addAll(mSurroudingCoordinates);
                mSurroudingCoordinates.clear();
                Point point1 = foundedCoordinates.get(0);
                Point point2 = foundedCoordinates.get(1);

                if(point1.x == point2.x) {
                    for(Point p : foundedCoordinates) {
                        mSurroudingCoordinates.addAll(getSurroundingY(p));
                    }
                }
                else if(point1.y == point2.y) {
                    for(Point p : foundedCoordinates) {
                        mSurroudingCoordinates.addAll(getSurroundingX(p));
                    }
                }
                return  getRandomPoint(mSurroudingCoordinates);
            }
            else if(foundedCoordinates.size() == 3) {
                mPlayablesCoordinates.addAll(mSurroudingCoordinates);
                mSurroudingCoordinates.clear();
                // TODO
                Point point1 = foundedCoordinates.get(0);
                Point point2 = foundedCoordinates.get(1);
                // Vertical
                if(point1.x == point2.x) {
                    int x = point1.x;
                    int minY = min(foundedCoordinates, "y");
                    int maxY = max(foundedCoordinates, "y");
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
        else if(shape == T && foundedCoordinates.size() == 3) {
            mPlayablesCoordinates.addAll(mSurroudingCoordinates);
            mSurroudingCoordinates.clear();
            Point point1 = foundedCoordinates.get(0);
            Point point2 = foundedCoordinates.get(1);
            Point point3 = foundedCoordinates.get(2);
            // Vertical
            if(point1.x == point2.x && point2.x == point3.x) {
                // Find Middle
                ArrayList<Integer> yCoordinates = new ArrayList<>();
                yCoordinates.add(point1.y);
                yCoordinates.add(point2.y);
                yCoordinates.add(point3.y);
                Collections.sort(yCoordinates);
                int x = point1.x;
                int y = yCoordinates.get(1);
                if(x - 1 >= 0 && !mGameControler.alreadyPlayed(x - 1,y)) mSurroudingCoordinates.add(new Point(x - 1, y));
                if(x + 1 < Settings.GRID_SIZE && !mGameControler.alreadyPlayed(x + 1, y)) mSurroudingCoordinates.add
                        (new Point(x + 1, y));
                return getRandomPoint(mSurroudingCoordinates);
            }
            else if(point1.y == point2.y && point2.y == point3.y) {
                // Find Middle
                ArrayList<Integer> xCoordinates = new ArrayList<>();
                xCoordinates.add(point1.x);
                xCoordinates.add(point2.x);
                xCoordinates.add(point3.x);
                Collections.sort(xCoordinates);
                int y = point1.y;
                int x = xCoordinates.get(1);
                if(x - 1 >= 0 && !mGameControler.alreadyPlayed(x - 1, y)) mSurroudingCoordinates.add
                        (new Point(x - 1, y));
                if(x + 1 < Settings.GRID_SIZE && !mGameControler.alreadyPlayed(x + 1, y)) mSurroudingCoordinates.add
                        (new Point(x + 1, y));
                return getRandomPoint(mSurroudingCoordinates);
            }

            // Common Shape
            else {
                ArrayList<Integer> xCoordinates = new ArrayList<>();
                ArrayList<Integer> yCoordinates = new ArrayList<>();
                for (Point point : foundedCoordinates) {
                    xCoordinates.add(point.x);
                    yCoordinates.add(point.y);
                }
                int x = 0;
                for (int i = 0; i < xCoordinates.size(); i++) {
                    if(Collections.frequency(xCoordinates, xCoordinates.get(i)) == 2){
                        x = xCoordinates.get(i);
                    }
                }

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
        Log.d(TAG, "getSurroundingCoordinates: " + mSurroudingCoordinates.size());
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

    private ArrayList<Point> getEvenCoordinates(){
        ArrayList<Point> evenCoordinates = new ArrayList<>();
        for(Point point : mPlayablesCoordinates) {
            if(point.x % 2 == 0 && point.y % 2 == 0) {
                evenCoordinates.add(new Point(point));
            }
            else if(point.x % 2 == 1 && point.y % 2 == 1) {
                evenCoordinates.add(new Point(point));
            }
        }
        return evenCoordinates;
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
