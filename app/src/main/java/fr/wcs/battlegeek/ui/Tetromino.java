package fr.wcs.battlegeek.ui;

import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by adphi on 27/09/17.
 */

public class Tetromino extends Item {
    private String TAG = "Tetromino";

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

    private Shape mShape;
    private Colors mColor;
    private ArrayList<Block> mBlocks = new ArrayList<>();

    public Tetromino(CreateMapView view, Grid grid, Shape shape, @Nullable Colors color) {
        super(view, grid);
        this.mShape = shape;
        this.mColor = color;
        init();
    }

    public Tetromino(CreateMapView view, Grid grid, float x, float y, Shape shape, @Nullable Colors color) {
        super(view, grid, x, y);
        this.mShape = shape;
        this.mColor = color;
        init();
    }

    private void init() {

        if(this.mColor == null) {
            this.mColor = Colors.BLUE;
        }

        switch (mShape) {
            case I:
                this.setBlock(new Block(0, 0));
                this.setBlock(new Block(0, 1));
                this.setBlock(new Block(0, 2));
                this.setBlock(new Block(0, 3));
                break;
            case O:
                this.setBlock(new Block(0, 0));
                this.setBlock(new Block(0, 1));
                this.setBlock(new Block(1, 0));
                this.setBlock(new Block(1, 1));
                break;
            case T:
                this.setBlock(new Block(0, 0));
                this.setBlock(new Block(1, 0));
                this.setBlock(new Block(2, 0));
                this.setBlock(new Block(1, 1));
                break;
            case J:
                this.setBlock(new Block(0, 0));
                this.setBlock(new Block(1, 0));
                this.setBlock(new Block(2, 0));
                this.setBlock(new Block(2, 1));
                break;
            case L:
                this.setBlock(new Block(0, 0));
                this.setBlock(new Block(0, 1));
                this.setBlock(new Block(0, 2));
                this.setBlock(new Block(1, 2));
                break;
            case S:
                this.setBlock(new Block(1, 0));
                this.setBlock(new Block(2, 0));
                this.setBlock(new Block(0, 1));
                this.setBlock(new Block(1, 1));
                break;
            case Z:
                this.setBlock(new Block(0, 0));
                this.setBlock(new Block(1, 0));
                this.setBlock(new Block(1, 1));
                this.setBlock(new Block(2, 1));
                break;
        }
    }

    public enum Shape {
        I, O, T, J, L, S, Z;
    }

    public enum Colors {
        YELLOW, ORANGE, RED, PURPLE, GREEN, BLUE, LTBLUE;
    }

}
