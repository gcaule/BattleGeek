package fr.wcs.battlegeek.Utils;

import android.util.Log;

/**
 * Created by adphi on 04/10/17.
 */

public class Utils {
    private static final String TAG = "MAP";
    public static void printMap(char[][] map) {
        String line = "";
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                line += map[i][j];
            }
            Log.d(TAG, line);
            line = "";
        }
    }
}