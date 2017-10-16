package fr.wcs.battlegeek.controller;

import fr.wcs.battlegeek.model.Result;
import fr.wcs.battlegeek.model.Settings;
import fr.wcs.battlegeek.ui.Tetromino;

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

        // Map's symbol analysis

        // If the symbol is a space character, the shot missed
        if (symbol == ' ') {
            mMap[y][x] = '_';
            resultType = MISSED;
            // Since there is no Item, there also is no Shape
            resultShape = NONE;
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
        return new Result(resultShape, resultType);
    }

    /**
     * Method responsible for storing the results of shots in the Storage Map
     * @param x the x coordinate of the shot
     * @param y the y coordinate of the shot
     * @param result the Result Object of the shot's result
     */
    public void setPlayResult(int x, int y, Result result) {
        // Get the type and the shape
        Result.Type resultType = result.getType();
        Tetromino.Shape resultShape = result.getShape();

        // If result is Missed, we store it as a underscore character
        if(resultType == MISSED) {
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

    public char[][] getMap() {
        return mMap;
    }
}
