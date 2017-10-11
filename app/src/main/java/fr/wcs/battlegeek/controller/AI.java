package fr.wcs.battlegeek.controller;

import android.graphics.Point;
import android.os.Parcelable;

import java.util.ArrayList;

import fr.wcs.battlegeek.model.Maps;
import fr.wcs.battlegeek.model.Result;

/**
 * Created by adphi on 03/10/17.
 */


public class AI {
    /**
     * Levels Enumerations
     */
    public enum Level {
        I, II, III, IMPOSSIBLE;
    }

    private final String TAG = "AI";
    private Level mLevel;
    private Point mLastPlayedCoordinates;


    private GameController mGameControler;
    private ArrayList<Point> mPlayablesCoordinates;
    private char[][] mPlayerMap;

    /**
     *  AI Constructor
     */
    public AI(){
        // Create a Game Controller
        mGameControler = new GameController(Maps.getMap());
        // Get all Playables Coordinates
        mPlayablesCoordinates = Maps.getPlayableCoordinates();
    }

    /**
     * Method for the Player to Shot the AI
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
     * @return the coordinates
     */
    public Point play() {
        switch (mLevel){
            case I :
                return playLevelI();
            case II :
                return playLevelII();
            case III :
                return playLevelIII();
            case IMPOSSIBLE :
                return playLevelI();
        }
    }

    /**
     * Method to call after the Player's game controller process the AI play method
     * this allow the AI'game processor to store the result in his Storage Map
     * @param result
     */
    public void setResult(Result result) {
        mGameControler.setPlayResult(mLastPlayedCoordinates.x, mLastPlayedCoordinates.y, result);
    }

    /**
     * Method setting the AI Level
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
                    if(mPlayerMap[i][j] != ' ') {
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

    }

    private Point playLevelIII() {
        return playLevelII();
    }

    /**
     * The Cheating Method: give the Player's Map to the AI
     * @param playerMap
     */
    public void setPlayerMap(char[][] playerMap) {
        mPlayerMap = playerMap;
    }
}
