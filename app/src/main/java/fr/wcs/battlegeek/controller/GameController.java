package fr.wcs.battlegeek.controller;

import fr.wcs.battlegeek.Model.Result;
import fr.wcs.battlegeek.ui.Tetromino;

import static fr.wcs.battlegeek.Model.Result.Type.DROWN;
import static fr.wcs.battlegeek.Model.Result.Type.MISSED;
import static fr.wcs.battlegeek.Model.Result.Type.TOUCHED;
import static fr.wcs.battlegeek.Model.Result.Type.VICTORY;
import static fr.wcs.battlegeek.ui.Tetromino.Shape.NONE;

/**
 * Created by adphi on 04/10/17.
 */

public class GameController {

    private char[][] mMap;

    public GameController(char[][] map) {
        mMap = map;
    }

    public Result play(int x, int y) {
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

}
