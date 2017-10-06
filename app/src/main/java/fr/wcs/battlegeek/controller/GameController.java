package fr.wcs.battlegeek.controller;

import fr.wcs.battlegeek.model.Result;
import fr.wcs.battlegeek.ui.Tetromino;

import static fr.wcs.battlegeek.model.Result.Type.DROWN;
import static fr.wcs.battlegeek.model.Result.Type.MISSED;
import static fr.wcs.battlegeek.model.Result.Type.TOUCHED;
import static fr.wcs.battlegeek.model.Result.Type.VICTORY;
import static fr.wcs.battlegeek.ui.Tetromino.Shape.NONE;

/**
 * Created by adphi on 04/10/17.
 */

public class GameController {

    private char[][] mMap;
    private char[][] mStorageMap = new char[10][10];

    public GameController(char[][] map) {
        mMap = map;
    }

    public Result shot(int x, int y) {
        char symbol = mMap[y][x];
        Result.Type resultType;
        Tetromino.Shape resultShape;
        if (symbol == ' ') {
            mMap[y][x] = '_';
            resultType = MISSED;
            resultShape = NONE;
        }
        else {
            mMap[y][x] = Character.toLowerCase(symbol);
            resultShape = Tetromino.Shape.valueOf(String.valueOf(symbol));
            resultType = TOUCHED;
            if(isDrown(symbol)) {
                resultType = DROWN;
            }
            if(victory()) {
                resultType = VICTORY;
            }
        }
        return new Result(resultShape, resultType);
    }

    public void setPlayResult(int x, int y, Result result) {
        Result.Type resultType = result.getType();
        Tetromino.Shape resultShape = result.getShape();

        if(resultType == MISSED) {
            mStorageMap[y][x] = '_';
        }
        else {
            mStorageMap[y][x] = Character.toLowerCase(resultShape.toString().charAt(0));
        }
    }

    private boolean isDrown(char symbol) {
        for(char[] row : mMap){
            for(char letter : row){
                if(symbol == letter) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean victory() {
        for(char[] row : mMap) {
            for(char symbol: row) {
                if(Character.isUpperCase(symbol)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean alreadyPlayed(int x, int y) {
        char symbol = mStorageMap[y][x];
        return symbol == '_' || Character.isLowerCase(symbol);
    }



}
