package fr.wcs.battlegeek.utils;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;

import fr.wcs.battlegeek.model.Settings;
import fr.wcs.battlegeek.ui.Block;
import fr.wcs.battlegeek.ui.Item;

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

    public static ArrayList<Point> copyPoints(ArrayList<Point> points) {
        ArrayList<Point> copyPoints = new ArrayList<>();
        for(Point p : points) {
            copyPoints.add(new Point(p));
        }
        return copyPoints;
    }

    /**
     * Format time from millisecond to String
     * @param milliseconds
     * @return
     */
    public static String timeFormat(long milliseconds) {
        int seconds = (int) (milliseconds / 1000);
        int minutes = seconds / 60;
        int hours = minutes / 60;
        minutes =minutes % 60;
        seconds = seconds % 60;
        String time = hours == 0 ? String.format("%02d:%02d", minutes, seconds)
                : String.format("%d:%02d:%02d",hours, minutes, seconds);
        return time;
    }

    /**
     * Method Printing An Item as a two dimensional array
     * @param item
     */
    public static void printItem(Item item) {
        int size = Math.max(item.getWidth(), item.getHeight());
        int[][] matrix = new int[size + 1][size + 1];
        for (Block block : item.getBlocks()) {
            matrix[(int)block.getY()][(int)block.getX()] = 1;
        }
        String print = "";
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                print += String.valueOf(matrix[i][j]);
            }
            print += "\n";
        }
        Log.d(TAG, "printItem: \n" + print);
    }
}