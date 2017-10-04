package fr.wcs.battlegeek.controller;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;

import fr.wcs.battlegeek.Model.Maps;
import fr.wcs.battlegeek.Model.Result;

/**
 * Created by adphi on 03/10/17.
 */

public class AI {
    public enum Level {
        I, II, III;
    }

    private final String TAG = "AI";
    private GameController mGameControler;
    private ArrayList<Point> mPlayablesCoordinates;

    public AI(){
        mGameControler = new GameController(Maps.getMap());
        mPlayablesCoordinates = Maps.getPlayableCorrdiantes();
    }

    public Result shot(int x, int y) {
        return mGameControler.play(x, y);
    }

    public Point play() {
        int index = (int) Math.random() * (mPlayablesCoordinates.size() - 1);
        Point coordinates = mPlayablesCoordinates.get(index);
        mPlayablesCoordinates.remove(index);
        return coordinates;
    }

    public void setResult(Result result) {
        Log.d(TAG, "setResult() called with: result = [" + result + "]");
    }
}
