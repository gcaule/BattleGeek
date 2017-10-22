package fr.wcs.battlegeek.controller;

import android.graphics.Point;

import java.util.ArrayList;

import fr.wcs.battlegeek.model.Bonus;
import fr.wcs.battlegeek.model.Result;
import fr.wcs.battlegeek.model.Settings;
import fr.wcs.battlegeek.ui.Tetromino;

import static fr.wcs.battlegeek.model.Result.Type.BONUS;
import static fr.wcs.battlegeek.model.Result.Type.DROWN;
import static fr.wcs.battlegeek.model.Result.Type.MISSED;
import static fr.wcs.battlegeek.model.Result.Type.TOUCHED;
import static fr.wcs.battlegeek.model.Result.Type.VICTORY;
import static fr.wcs.battlegeek.ui.Tetromino.Shape.NONE;

/**
 * Created by adphi on 04/10/17.
 *
 */

/**
 * This Class handle the analysis of the shots coordinates according to the map
 *
 * It store the Item's Map and the played Shots' Results in the storage map
 */
public class GameController {
    private final String TAG = Settings.TAG;

    // Maps initialisation

    // The map containing the Item's position, also storing the enemy shots
    private char[][] mMap;

    // The map storing the shots' results
    private char[][] mStorageMap = new char[Settings.GRID_SIZE][Settings.GRID_SIZE];

    /**
     * Constructor
     * @param map the map containing the Item's positions
     */
    public GameController(char[][] map) {
        mMap = map;
    }

    /**
     * Method responsible for analysing the shot's coordinates and returning the result
     * @param x
     * @param y
     * @return The result
     */
    public Result shot(int x, int y) {
        // Get the symbo in the map
        char symbol = mMap[y][x];

        // Declaration of the two attributes of the resulting Result Object
        Result.Type resultType;
        Tetromino.Shape resultShape;
        Bonus.Type resultBonus = null;

        // Map's symbol analysis

        // If the symbol is a space character, the shot missed
        if (symbol == ' ') {
            mMap[y][x] = '_';
            resultType = MISSED;
            // Since there is no Item, there also is no Shape
            resultShape = NONE;
        }
        // If Bonus
        else if (symbol == '-' || symbol == '=' || symbol == '+') {
            mMap[y][x] = '_';
            resultType = BONUS;
            resultShape = NONE;
            resultBonus = Bonus.getBonus(symbol);
        }
        // If the symbol is not a space, it did touched something
        else {
            // Store the shot in the map, an touched Block is representing by his Shape symbol
            // to lower case
            mMap[y][x] = Character.toLowerCase(symbol);
            // Get the shape of the Item, according to its symbol
            resultShape = Tetromino.Shape.valueOf(String.valueOf(symbol));
            // Set the default result type: Touched
            resultType = TOUCHED;
            // If the Item is drown
            if(isDrown(symbol)) {
                resultType = DROWN;
            }
            // If all the Items are drown
            if(victory()) {
                resultType = VICTORY;
            }
        }
        return new Result(x, y, resultShape, resultType, resultBonus);
    }

    /**
     * Method responsible for storing the results of shots in the Storage Map
     * @param result the Result Object of the shot's result
     */
    public void setPlayResult(Result result) {
        int x = result.getX();
        int y = result.getY();
        // Get the type and the shape
        Result.Type resultType = result.getType();
        Tetromino.Shape resultShape = result.getShape();

        // If result is Missed, we store it as a underscore character
        if(resultType == MISSED || resultType == BONUS) {
            mStorageMap[y][x] = '_';
        }
        // If it Touched something, we store it as the symbol of its shape as lowercase
        else {
            mStorageMap[y][x] = Character.toLowerCase(resultShape.toString().charAt(0));
        }
    }

    /**
     * Method verifying if an Item is drown
     * @param symbol the Item's symbol's character
     * @return
     */
    private boolean isDrown(char symbol) {
        for(char[] row : mMap){
            for(char letter : row){
                // If the symbol is still in the Map (Lowercase), the Item is not drown
                if(symbol == letter) {
                    return false;
                }
            }
        }
        // We did not find the character, so, Item is drown
        return true;
    }

    /**
     * Method checking for Victory
     * @return
     */
    private boolean victory() {
        for(char[] row : mMap) {
            for(char symbol: row) {
                // As the Items are stored as Item's shape symbol to Lowercase character
                // if we found a lowercase character, the game is not win
                if(Character.isUpperCase(symbol)) {
                    return false;
                }
            }
        }
        // Didn't found any lowercase character, so ... guess what...
        // ... VICTORYYYYYYYYY !
        return true;
    }

    /**
     * Method checking if the coordinates were already played
     * @param x
     * @param y
     * @return
     */
    public boolean alreadyPlayed(int x, int y) {
        // Get the symbol in the Storage Map
        char symbol = mStorageMap[y][x];
        // If the character is and underscore (Missed Symbol) or a lowercase character (touched symbol)
        // the coordinates were already played, else, everything is ok.
        return symbol == '_' || Character.isLowerCase(symbol);
    }

    public ArrayList<Point> getSurrondingcoordinates(int x, int y) {
        ArrayList<Point> points = new ArrayList<Point>();
        int xMin = Math.max(x - 1, 0);
        int xMax = Math.min(x + 1, Settings.GRID_SIZE - 1);
        int yMin = Math.max(y - 1, 0);
        int yMax = Math.min(y + 1, Settings.GRID_SIZE - 1);

        // Add the 3 on the x axis;
        for (int i = xMin; i <= xMax; i++) {
            if(!alreadyPlayed(i, y)) {
                points.add(new Point(i, y));
            }
        }
        // Add the two on the y axis
        for (int i = yMin; i <= yMax; i++) {
            // We already have the Point x,y
            if(! alreadyPlayed(x, i) && i != y) {
                points.add(new Point(x, i));
            }
        }

        return points;
    }

    public void setBonus(){
        // Get Empty Coordinates
        ArrayList<Point> emptyCells = new ArrayList<>();
        for (int i = 0; i < mMap.length; i++) {
            for (int j = 0; j < mMap[i].length; j++) {
                if(mMap[i][j] == ' ') {
                    emptyCells.add(new Point(j, i));
                }
            }
        }
        // Get the bonus
        Bonus.Type[] bonus = Bonus.Type.values();

        // Set Bonus in the Map
        for (int i = 0; i < bonus.length; i++) {
            int index = (int)(Math.random() * (emptyCells.size() - 1));
            Point position = emptyCells.get(index);
            emptyCells.remove(index);
            mMap[position.y][position.x] = bonus[i].toString().charAt(0);
        }
    }

    public char[][] getMap() {
        return mMap;
    }

    public void setMap(char[][] map) {
        mMap = map;
    }

    public char[][] getStorageMap() {
        return mStorageMap;
    }
}
