package fr.wcs.battlegeek.model;

import android.graphics.Point;

import java.util.ArrayList;

/**
 * Created by apprenti on 27/09/17.
 */

/**
 * Class storing Maps and Map related Methods
 *
 * The Maps are two dimentional characters Arrays
 *
 * Item's Storage Definition:
 *  - ' ' : a space character is an empty cell
 *  - '_' : an underscore character is an empty Shoted cell
 *  - A LOWERCASE Tetromino Shape Symbol's character represent a Tetromino Block not Shoted (yet...)
 *  - a lowercase Tetromino Shape symbol's character represent a Tetromino Block Shoted
 */
public class Maps {

    // Pseudo Random Maps Definition
    private static char[][] map1 = new char[][]{
            {' ', ' ', ' ', 'T', 'T', 'T', ' ', ' ', ' ', ' '},
            {'Z', 'Z', ' ', ' ', 'T', ' ', ' ', ' ', ' ', 'I'},
            {' ', 'Z', 'Z', ' ', ' ', ' ', ' ', ' ', ' ', 'I'},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'I'},
            {' ', ' ', ' ', ' ', ' ', ' ', 'S', 'S', ' ', 'I'},
            {' ', 'J', ' ', ' ', ' ', 'S', 'S', ' ', ' ', ' '},
            {' ', 'J', 'J', 'J', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'L', ' '},
            {' ', ' ', 'O', 'O', ' ', ' ', ' ', ' ', 'L', ' '},
            {' ', ' ', 'O', 'O', ' ', ' ', ' ', ' ', 'L', 'L'}
    };

    private static char[][] map2 = new char[][]{
            {' ', 'O', 'O', ' ', ' ', ' ', 'L', 'L', 'L', ' '},
            {' ', 'O', 'O', ' ', ' ', ' ', 'L', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', 'T', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', 'T', 'T', ' ', 'J', 'J', ' ', ' '},
            {' ', 'Z', ' ', ' ', 'T', ' ', 'J', ' ', ' ', ' '},
            {'Z', 'Z', ' ', ' ', ' ', ' ', 'J', ' ', ' ', ' '},
            {'Z', ' ', ' ', ' ', ' ', ' ', ' ', 'S', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', 'S', 'S', ' '},
            {' ', 'I', 'I', 'I', 'I', ' ', ' ', ' ', 'S', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
    };

    private static char[][] map3 = new char[][]{
            {' ', 'J', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', 'J', 'J', 'J', ' ', ' ', ' ', 'O', 'O', ' '},
            {' ', ' ', ' ', ' ', ' ', 'S', ' ', 'O', 'O', ' '},
            {' ', ' ', ' ', ' ', ' ', 'S', 'S', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', 'S', ' ', 'Z', ' '},
            {'I', 'I', 'I', 'I', ' ', ' ', ' ', 'Z', 'Z', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', 'Z', ' ', ' '},
            {' ', ' ', 'L', 'L', 'L', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', 'L', ' ', ' ', ' ', ' ', 'T', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', 'T', 'T', 'T', ' '}
    };

    private static char[][] map4 = new char[][]{
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', 'L', 'L', ' ', ' ', ' ', ' ', 'J', ' ', ' '},
            {' ', ' ', 'L', ' ', ' ', ' ', ' ', 'J', 'J', 'J'},
            {' ', ' ', 'L', ' ', 'I', ' ', ' ', ' ', ' ', ' '},
            {'O', 'O', ' ', ' ', 'I', ' ', ' ', ' ', ' ', ' '},
            {'O', 'O', ' ', ' ', 'I', ' ', ' ', ' ', 'T', ' '},
            {' ', ' ', ' ', ' ', 'I', 'S', ' ', ' ', 'T', 'T'},
            {' ', ' ', ' ', ' ', ' ', 'S', 'S', ' ', 'T', ' '},
            {'Z', 'Z', ' ', ' ', ' ', ' ', 'S', ' ', ' ', ' '},
            {' ', 'Z', 'Z', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
    };

    private static char[][] map5 = new char[][]{
            {' ', ' ', ' ', ' ', ' ', ' ', 'I', 'I', 'I', 'I'},
            {' ', 'Z', 'Z', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', 'Z', 'Z', ' ', ' ', ' ', 'L', 'L', ' '},
            {' ', 'T', ' ', ' ', 'J', 'J', ' ', ' ', 'L', ' '},
            {'T', 'T', 'T', ' ', 'J', ' ', ' ', ' ', 'L', ' '},
            {' ', ' ', ' ', ' ', 'J', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', 'O', 'O', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', 'O', 'O', ' ', ' ', 'S', 'S', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', 'S', 'S', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
    };

    private static char[][] map6 = new char[][]{
            {' ', ' ', ' ', 'Z', ' ', ' ', 'S', 'S', ' ', ' '},
            {' ', ' ', 'Z', 'Z', ' ', 'S', 'S', ' ', ' ', ' '},
            {'I', ' ', 'Z', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {'I', ' ', ' ', ' ', ' ', ' ', ' ', 'J', ' ', ' '},
            {'I', ' ', ' ', ' ', 'O', 'O', ' ', 'J', 'J', 'J'},
            {'I', ' ', ' ', ' ', 'O', 'O', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', 'L', 'L', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', 'L', ' ', ' ', ' ', 'T', ' ', ' ', ' '},
            {' ', ' ', 'L', ' ', ' ', 'T', 'T', 'T', ' ', ' '}
    };

    private static char[][] map7 = new char[][]{
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', 'Z', 'Z', ' ', ' ', ' ', ' ', 'T', ' '},
            {' ', 'I', ' ', 'Z', 'Z', ' ', ' ', 'T', 'T', ' '},
            {' ', 'I', ' ', ' ', ' ', 'O', 'O', ' ', 'T', ' '},
            {' ', 'I', ' ', ' ', ' ', 'O', 'O', ' ', ' ', ' '},
            {' ', 'I', ' ', ' ', 'S', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', 'L', ' ', 'S', 'S', ' ', ' ', ' ', ' '},
            {' ', ' ', 'L', ' ', ' ', 'S', ' ', ' ', 'J', ' '},
            {' ', ' ', 'L', 'L', ' ', ' ', 'J', 'J', 'J', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
    };

    private static char[][] map8 = new char[][]{
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', 'L', 'L', 'L', ' ', 'I', 'I', 'I', 'I', ' '},
            {' ', 'L', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', 'T', ' ', ' ', 'O', 'O', ' '},
            {' ', ' ', 'Z', ' ', 'T', 'T', ' ', 'O', 'O', ' '},
            {' ', 'Z', 'Z', ' ', 'T', ' ', ' ', ' ', ' ', ' '},
            {' ', 'Z', ' ', ' ', ' ', ' ', ' ', 'S', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', 'S', 'S', ' '},
            {' ', ' ', ' ', 'J', 'J', 'J', ' ', ' ', 'S', ' '},
            {' ', ' ', ' ', ' ', ' ', 'J', ' ', ' ', ' ', ' '}
    };

    private static char[][] map9 = new char[][]{
            {' ', ' ', ' ', ' ', ' ', ' ', 'S', 'S', ' ', 'I'},
            {' ', 'T', ' ', ' ', ' ', 'S', 'S', ' ', ' ', 'I'},
            {'T', 'T', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'I'},
            {' ', 'T', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'I'},
            {' ', ' ', ' ', ' ', 'J', 'J', 'J', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', 'J', ' ', 'L', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'L', ' '},
            {' ', 'Z', 'Z', ' ', ' ', ' ', ' ', ' ', 'L', 'L'},
            {' ', ' ', 'Z', 'Z', ' ', 'O', 'O', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', 'O', 'O', ' ', ' ', ' '}
    };

    private static char[][] map10 = new char[][]{
            {' ', ' ', ' ', ' ', 'Z', ' ', ' ', ' ', 'L', ' '},
            {' ', ' ', ' ', 'Z', 'Z', ' ', 'L', 'L', 'L', ' '},
            {' ', ' ', ' ', 'Z', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', 'T', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', 'T', 'T', ' ', 'O', 'O', ' ', ' ', ' ', ' '},
            {' ', ' ', 'T', ' ', 'O', 'O', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', 'I', ' ', ' ', ' ', 'S', 'S', ' '},
            {'J', 'J', ' ', 'I', ' ', ' ', 'S', 'S', ' ', ' '},
            {'J', ' ', ' ', 'I', ' ', ' ', ' ', ' ', ' ', ' '},
            {'J', ' ', ' ', 'I', ' ', ' ', ' ', ' ', ' ', ' '}
    };

    private static char[][] map11 = new char[][]{
            {' ', 'Z', ' ', ' ', ' ', ' ', 'L', ' ', ' ', ' '},
            {'Z', 'Z', ' ', ' ', ' ', ' ', 'L', ' ', ' ', ' '},
            {'Z', ' ', ' ', ' ', ' ', ' ', 'L', 'L', ' ', ' '},
            {' ', ' ', 'J', 'J', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', 'J', ' ', ' ', 'I', 'I', 'I', 'I', ' '},
            {' ', ' ', 'J', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', 'O', 'O', ' ', ' ', 'S', 'S', ' ', ' ', ' '},
            {' ', 'O', 'O', ' ', 'S', 'S', ' ', ' ', 'T', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', 'T', 'T', 'T'}
    };

    private static char[][] map12 = new char[][]{
            {' ', 'J', 'J', 'J', ' ', 'I', 'I', 'I', 'I', ' '},
            {' ', ' ', ' ', 'J', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', 'O', 'O', ' ', 'T', 'T', 'T', ' ', 'L', 'L'},
            {' ', 'O', 'O', ' ', ' ', 'T', ' ', ' ', ' ', 'L'},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'L'},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {'Z', 'Z', ' ', ' ', ' ', ' ', ' ', ' ', 'S', 'S'},
            {' ', 'Z', 'Z', ' ', ' ', ' ', ' ', 'S', 'S', ' '}
    };

    private static char[][] map13 = new char[][]{
            {' ', ' ', ' ', ' ', ' ', ' ', 'J', 'J', 'J', ' '},
            {' ', ' ', 'Z', 'Z', ' ', ' ', ' ', ' ', 'J', ' '},
            {' ', ' ', ' ', 'Z', 'Z', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', 'S', ' ', ' '},
            {' ', 'I', 'I', 'I', 'I', ' ', ' ', 'S', 'S', ' '},
            {' ', ' ', ' ', ' ', ' ', 'L', ' ', ' ', 'S', ' '},
            {' ', 'T', ' ', ' ', ' ', 'L', ' ', ' ', ' ', ' '},
            {' ', 'T', 'T', ' ', ' ', 'L', 'L', ' ', 'O', 'O'},
            {' ', 'T', ' ', ' ', ' ', ' ', ' ', ' ', 'O', 'O'},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
    };

    private static char[][] map14 = new char[][]{
            {' ', 'S', 'S', ' ', ' ', ' ', ' ', 'L', 'L', 'L'},
            {'S', 'S', ' ', ' ', ' ', ' ', ' ', 'L', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', 'O', 'O', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', 'O', 'O', ' ', ' ', ' '},
            {' ', ' ', ' ', 'J', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', 'I', ' ', 'J', 'J', 'J', ' ', ' ', ' ', 'T'},
            {' ', 'I', ' ', ' ', ' ', ' ', 'Z', ' ', 'T', 'T'},
            {' ', 'I', ' ', ' ', ' ', 'Z', 'Z', ' ', ' ', 'T'},
            {' ', 'I', ' ', ' ', ' ', 'Z', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
    };

    private static char[][] map15 = new char[][]{
            {'O', 'O', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {'O', 'O', ' ', ' ', ' ', ' ', ' ', 'L', 'L', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'L', ' '},
            {' ', 'J', ' ', ' ', ' ', ' ', ' ', ' ', 'L', ' '},
            {' ', 'J', 'J', 'J', ' ', 'T', 'T', 'T', ' ', 'I'},
            {' ', ' ', ' ', ' ', ' ', ' ', 'T', ' ', ' ', 'I'},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'I'},
            {' ', ' ', ' ', 'Z', ' ', ' ', 'S', 'S', ' ', 'I'},
            {' ', ' ', 'Z', 'Z', ' ', 'S', 'S', ' ', ' ', ' '},
            {' ', ' ', 'Z', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
    };

    private static char[][] map16 = new char[][]{
            {' ', ' ', ' ', 'I', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', 'I', ' ', ' ', ' ', ' ', 'O', 'O'},
            {'J', 'J', ' ', 'I', ' ', ' ', 'Z', ' ', 'O', 'O'},
            {'J', ' ', ' ', 'I', ' ', 'Z', 'Z', ' ', ' ', ' '},
            {'J', ' ', ' ', ' ', ' ', 'Z', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', 'L', ' ', ' ', ' '},
            {' ', 'T', ' ', ' ', 'L', 'L', 'L', ' ', ' ', ' '},
            {' ', 'T', 'T', ' ', ' ', ' ', ' ', ' ', 'S', 'S'},
            {' ', 'T', ' ', ' ', ' ', ' ', ' ', 'S', 'S', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
    };

    private static char[][] map17 = new char[][]{
            {' ', ' ', ' ', ' ', ' ', 'J', ' ', ' ', ' ', 'Z'},
            {'S', ' ', ' ', ' ', ' ', 'J', ' ', ' ', 'Z', 'Z'},
            {'S', 'S', ' ', ' ', 'J', 'J', ' ', ' ', 'Z', ' '},
            {' ', 'S', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', 'L', ' ', 'I', 'I', 'I', 'I', ' ', ' '},
            {'L', 'L', 'L', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', 'T', ' ', ' ', ' ', ' ', 'O', 'O'},
            {' ', ' ', 'T', 'T', 'T', ' ', ' ', ' ', 'O', 'O'}
    };

    private static char[][] map18 = new char[][]{
            {' ', ' ', 'O', 'O', ' ', 'J', 'J', ' ', ' ', ' '},
            {' ', ' ', 'O', 'O', ' ', 'J', ' ', ' ', ' ', ' '},
            {' ', 'Z', ' ', ' ', ' ', 'J', ' ', ' ', ' ', ' '},
            {'Z', 'Z', ' ', ' ', ' ', ' ', 'I', ' ', 'T', ' '},
            {'Z', ' ', 'L', ' ', ' ', ' ', 'I', ' ', 'T', 'T'},
            {' ', ' ', 'L', ' ', ' ', ' ', 'I', ' ', 'T', ' '},
            {' ', ' ', 'L', 'L', ' ', ' ', 'I', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', 'S', 'S', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', 'S', 'S', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
    };

    private static char[][] map19 = new char[][]{
            {' ', ' ', ' ', 'Z', 'Z', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', 'Z', 'Z', ' ', ' ', ' ', ' '},
            {'S', ' ', ' ', ' ', ' ', ' ', ' ', 'O', 'O', ' '},
            {'S', 'S', ' ', ' ', ' ', ' ', ' ', 'O', 'O', ' '},
            {' ', 'S', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', 'I', 'I', 'I', 'I', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', 'L', 'L', ' '},
            {' ', 'T', 'T', 'T', ' ', ' ', ' ', ' ', 'L', ' '},
            {' ', ' ', 'T', ' ', 'J', 'J', 'J', ' ', 'L', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', 'J', ' ', ' ', ' '}
    };

    private static char[][] map20 = new char[][]{
            {' ', ' ', ' ', ' ', 'O', 'O', ' ', ' ', ' ', 'Z'},
            {' ', ' ', ' ', ' ', 'O', 'O', ' ', ' ', 'Z', 'Z'},
            {'T', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'Z', ' '},
            {'T', 'T', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {'T', ' ', ' ', ' ', 'S', 'S', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', 'S', 'S', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', 'L', ' ', ' ', ' ', ' ', 'J', ' ', ' '},
            {'L', 'L', 'L', ' ', ' ', ' ', ' ', 'J', 'J', 'J'},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', 'I', 'I', 'I', 'I', ' ', ' ', ' ', ' '}
    };

    private static char[][] map21 = new char[][]{
            {' ', ' ', 'J', 'J', 'J', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', 'J', ' ', ' ', 'O', 'O', ' '},
            {'L', ' ', ' ', ' ', ' ', ' ', ' ', 'O', 'O', ' '},
            {'L', ' ', ' ', ' ', 'Z', ' ', ' ', ' ', ' ', ' '},
            {'L', 'L', ' ', 'Z', 'Z', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', 'Z', ' ', ' ', ' ', ' ', ' ', 'T'},
            {' ', 'I', ' ', ' ', ' ', ' ', ' ', ' ', 'T', 'T'},
            {' ', 'I', ' ', ' ', ' ', 'S', 'S', ' ', ' ', 'T'},
            {' ', 'I', ' ', ' ', 'S', 'S', ' ', ' ', ' ', ' '},
            {' ', 'I', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
    };

    private static char[][] map22 = new char[][]{
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', 'Z', 'Z', ' '},
            {' ', ' ', 'T', 'T', 'T', ' ', ' ', ' ', 'Z', 'Z'},
            {' ', ' ', ' ', 'T', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', 'I', 'I', 'I', 'I'},
            {' ', 'J', 'J', 'J', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', 'J', ' ', 'L', ' ', ' ', ' ', ' '},
            {' ', 'O', 'O', ' ', ' ', 'L', ' ', ' ', ' ', ' '},
            {' ', 'O', 'O', ' ', ' ', 'L', 'L', ' ', 'S', 'S'},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', 'S', 'S', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
    };

    private static char[][] map23 = new char[][]{
            {'S', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {'S', 'S', ' ', ' ', 'T', 'T', 'T', ' ', ' ', ' '},
            {' ', 'S', ' ', ' ', ' ', 'T', ' ', 'O', 'O', ' '},
            {' ', ' ', 'J', ' ', ' ', ' ', ' ', 'O', 'O', ' '},
            {' ', ' ', 'J', 'J', 'J', ' ', ' ', ' ', ' ', ' '},
            {'I', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {'I', ' ', 'Z', 'Z', ' ', ' ', ' ', ' ', 'L', ' '},
            {'I', ' ', ' ', 'Z', 'Z', ' ', 'L', 'L', 'L', ' '},
            {'I', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
    };

    private static char[][] map24 = new char[][]{
            {' ', ' ', 'I', ' ', ' ', ' ', ' ', 'L', ' ', ' '},
            {' ', ' ', 'I', ' ', ' ', ' ', ' ', 'L', ' ', ' '},
            {' ', ' ', 'I', ' ', 'J', 'J', ' ', 'L', 'L', ' '},
            {' ', ' ', 'I', ' ', 'J', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', 'J', ' ', ' ', ' ', ' ', ' '},
            {' ', 'Z', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {'Z', 'Z', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {'Z', ' ', ' ', ' ', 'S', ' ', ' ', ' ', 'O', 'O'},
            {' ', ' ', 'T', ' ', 'S', 'S', ' ', ' ', 'O', 'O'},
            {' ', 'T', 'T', 'T', ' ', 'S', ' ', ' ', ' ', ' '}
    };

    private static char[][] map25 = new char[][]{
            {' ', ' ', ' ', ' ', ' ', ' ', 'I', 'I', 'I', 'I'},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', 'J', 'J', ' ', ' ', ' ', ' ', ' ', 'S', 'S'},
            {' ', 'J', ' ', ' ', ' ', ' ', ' ', 'S', 'S', ' '},
            {' ', 'J', ' ', ' ', ' ', 'O', 'O', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', 'O', 'O', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', 'T', ' ', ' ', ' ', ' ', ' '},
            {' ', 'Z', ' ', 'T', 'T', ' ', ' ', ' ', 'L', ' '},
            {'Z', 'Z', ' ', ' ', 'T', ' ', 'L', 'L', 'L', ' '},
            {'Z', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
    };

    private static char[][] map26 = new char[][]{
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', 'S', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', 'S', 'S', ' '},
            {'T', ' ', 'I', 'I', 'I', 'I', ' ', ' ', 'S', ' '},
            {'T', 'T', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {'T', ' ', ' ', ' ', ' ', 'J', 'J', ' ', 'L', 'L'},
            {' ', ' ', ' ', ' ', ' ', 'J', ' ', ' ', ' ', 'L'},
            {' ', ' ', ' ', ' ', ' ', 'J', ' ', ' ', ' ', 'L'},
            {' ', ' ', 'O', 'O', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', 'O', 'O', ' ', 'Z', 'Z', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', 'Z', 'Z', ' ', ' '}
    };

    private static char[][] map27 = new char[][]{
            {' ', ' ', ' ', 'L', 'L', 'L', ' ', ' ', ' ', ' '},
            {'O', 'O', ' ', 'L', ' ', ' ', ' ', ' ', ' ', ' '},
            {'O', 'O', ' ', ' ', ' ', ' ', ' ', 'S', 'S', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', 'S', 'S', ' ', 'T'},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'T', 'T'},
            {' ', ' ', ' ', 'J', 'J', ' ', ' ', ' ', ' ', 'T'},
            {' ', ' ', ' ', 'J', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', 'J', ' ', 'I', 'I', 'I', 'I', ' '},
            {'Z', 'Z', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', 'Z', 'Z', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
    };

    private static char[][] map28 = new char[][]{
            {' ', ' ', ' ', ' ', ' ', ' ', 'T', 'T', 'T', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', 'T', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', 'Z', 'Z', ' ', ' ', ' '},
            {'J', 'J', ' ', ' ', ' ', ' ', 'Z', 'Z', ' ', ' '},
            {'J', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {'J', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'I'},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'I'},
            {'S', ' ', ' ', 'L', 'L', ' ', ' ', ' ', ' ', 'I'},
            {'S', 'S', ' ', ' ', 'L', ' ', 'O', 'O', ' ', 'I'},
            {' ', 'S', ' ', ' ', 'L', ' ', 'O', 'O', ' ', ' '}
    };

    private static char[][] map29 = new char[][]{
            {'T', 'T', 'T', ' ', ' ', ' ', ' ', 'O', 'O', ' '},
            {' ', 'T', ' ', 'L', 'L', 'L', ' ', 'O', 'O', ' '},
            {' ', ' ', ' ', 'L', ' ', ' ', ' ', ' ', ' ', ' '},
            {'S', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {'S', 'S', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', 'S', ' ', ' ', ' ', 'Z', 'Z', ' ', ' ', ' '},
            {' ', ' ', ' ', 'I', ' ', ' ', 'Z', 'Z', ' ', ' '},
            {' ', ' ', ' ', 'I', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', 'I', ' ', ' ', 'J', ' ', ' ', ' '},
            {' ', ' ', ' ', 'I', ' ', ' ', 'J', 'J', 'J', ' '}
    };

    private static char[][] map30 = new char[][]{
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {'J', 'J', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', 'J', ' ', ' ', ' ', 'I', 'I', 'I', 'I', ' '},
            {' ', 'J', ' ', ' ', 'T', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', 'L', ' ', 'T', 'T', ' ', ' ', 'S', ' '},
            {' ', ' ', 'L', ' ', 'T', ' ', ' ', ' ', 'S', 'S'},
            {' ', ' ', 'L', 'L', ' ', 'O', 'O', ' ', ' ', 'S'},
            {'Z', 'Z', ' ', ' ', ' ', 'O', 'O', ' ', ' ', ' '},
            {' ', 'Z', 'Z', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}
    };

    // Store all the predefined Maps in an array
    private static char[][][] maps = new char [][][] {map1, map2, map3, map4, map5, map6, map7, map8, map9, map10,
            map11, map12, map13, map14, map15, map16, map17, map18, map19, map20,
            map21, map22, map23, map24, map25, map26, map27, map28, map29, map30};

    /**
     * Method to get a Random Predefined Map
     * @return the Map as character two dimentional array
     */
    public static char[][] getMap() {
        int random = (int)(Math.random() * (maps.length - 1));
        char[][] map = copy(maps[random]);
        return maps[random];
    }

    /**
     * Method return an ArrayList of the whole (100) Grid Coordinates
     * from Point(0,0) to Point(9,9)
     * @return
     */
    public static ArrayList<Point> getPlayableCoordinates(){
        ArrayList<Point> playableCoordinates = new ArrayList<>();
        for (int i = 0; i < Settings.GRID_SIZE; i++) {
            for (int j = 0; j < Settings.GRID_SIZE; j++) {
                playableCoordinates.add(new Point(i, j));
            }
        }
        return playableCoordinates;
    }

    private static char[][] copy(char[][] map) {
        char[][] copy = new char[Settings.GRID_SIZE][Settings.GRID_SIZE];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                copy[i][j] = map[i][j];
            }
        }
        return copy;
    }

    // TODO DELETE
    public static char[][] getMapFromIndex(int index) {
        return maps[index];
    }

}