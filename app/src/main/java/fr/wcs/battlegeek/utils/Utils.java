package fr.wcs.battlegeek.utils;

import android.util.Log;

import java.util.ArrayList;

import fr.wcs.battlegeek.model.Settings;
import fr.wcs.battlegeek.ui.Block;

/**
 * Created by adphi on 04/10/17.
 */

public class Utils {
    private static final String TAG = Settings.TAG;

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

    /*public static <T> ArrayList<T> copy(Class<T> clazz, ArrayList<T> array) {
        ArrayList<T> copy = new ArrayList<>();
        for(Object object : array) {
            T t = clazz.clone(object);
            copy.add(t);
        }
        return copy;
    }*/

    public static ArrayList<Block> copyBlocks(ArrayList<Block> blocks) {
        ArrayList<Block> copy = new ArrayList<>();
        for(Block block : blocks) {
            copy.add(block.clone());
        }
        return copy;
    }

    public static String timeFormat(long milliseconds) {
        int seconds = (int) (milliseconds / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }
}