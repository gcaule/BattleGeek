package fr.wcs.battlegeek.controller;

import android.graphics.Point;

import java.util.ArrayList;

import fr.wcs.battlegeek.model.Maps;
import fr.wcs.battlegeek.model.Result;

/**
 * Created by adphi on 03/10/17.
 */

public class AI {
    public enum Level {
        I, II, III, IMPOSSIBLE;
    }

    private final String TAG = "AI";
    private Level mLevel;


    private GameController mGameControler;
    private ArrayList<Point> mPlayablesCoordinates;
    private char[][] mPlayerMap;

    public AI(){
        mGameControler = new GameController(Maps.getMap());
        mPlayablesCoordinates = Maps.getPlayableCorrdiantes();
    }

    public Result shot(int x, int y) {
        return mGameControler.shot(x, y);
    }

    public Point play() {
        int index = (int) (Math.random() * (mPlayablesCoordinates.size() - 1));
        Point coordinates = mPlayablesCoordinates.get(index);
        mPlayablesCoordinates.remove(index);
        return coordinates;
    }

    public void setResult(Result result) {
    }

    public void setLevel(Level level) {
        mLevel = level;
        if (level == Level.IMPOSSIBLE && mPlayerMap != null) {
            mPlayablesCoordinates.clear();
            for (int i = 0; i < mPlayerMap.length; i++) {
                for (int j = 0; j < mPlayerMap[i].length; j++) {
                    if(mPlayerMap[i][j] != ' ') {
                        mPlayablesCoordinates.add(new Point(j, i));
                    }
                }
            }
        }
    }

    public void setPlayerMap(char[][] playerMap) {
        mPlayerMap = playerMap;
    }
}
