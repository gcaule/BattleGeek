package fr.wcs.battlegeek.ui;

import android.support.annotation.Nullable;

import java.util.HashMap;

/**
 * Created by adphi on 27/09/17.
 */

public class Tetromino extends Item {
    private String TAG = "Tetromino";


    private Shape mShape;
    private Colors mColor;
    private static HashMap<Shape, Colors> mColorsMap = new HashMap<>();

    public Tetromino(MapView view, Grid grid, Shape shape, @Nullable Colors color) {
        super(view, grid);
        this.mShape = shape;
        this.mColor = color;
        init();
    }

    public Tetromino(MapView view, Grid grid, float x, float y, Shape shape, @Nullable Colors color) {
        super(view, grid, x, y);
        this.mShape = shape;
        this.mColor = color;
        init();
    }

    private void init() {

        if(this.mColor == null) {
            this.mColor = getColorMap().get(mShape);
        }
    }

    /**
     * Tetromino's Shape's Symbols Enumeration
     */
    public enum Shape {
        I ("I"),
        O ("O"),
        T ("T"),
        J ("J"),
        L ("J"),
        S ("S"),
        Z ("Z"),
        NONE ("X");

        private String name = "";

        Shape(String name){
            this.name = name;
        }

        public String toString(){
            return name;
        }
    }

    /**
     * Tetromino Colors enumeration
     */
    public enum Colors {
        YELLOW, ORANGE, RED, PURPLE, GREEN, BLUE, LTBLUE
    }

    /**
     * Static Method to get the color of a Tetromino according to its shape
     * @return
     */
    public static HashMap<Shape, Colors> getColorMap() {
        if(mColorsMap.isEmpty()) {
            mColorsMap.put(Tetromino.Shape.I, Tetromino.Colors.LTBLUE);
            mColorsMap.put(Tetromino.Shape.T, Tetromino.Colors.PURPLE);
            mColorsMap.put(Tetromino.Shape.Z, Tetromino.Colors.RED);
            mColorsMap.put(Tetromino.Shape.O, Tetromino.Colors.YELLOW);
            mColorsMap.put(Tetromino.Shape.J, Tetromino.Colors.BLUE);
            mColorsMap.put(Tetromino.Shape.L, Tetromino.Colors.ORANGE);
            mColorsMap.put(Tetromino.Shape.S, Tetromino.Colors.GREEN);
        }
        return mColorsMap;
    }

    public Shape getShape() {
        return mShape;
    }

    public void setShape(Shape shape) {
        mShape = shape;
    }

    public Colors getColor() {
        return mColor;
    }

    public void setColor(Colors color) {
        mColor = color;
    }
}
