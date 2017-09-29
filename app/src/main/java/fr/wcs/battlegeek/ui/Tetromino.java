package fr.wcs.battlegeek.ui;

import android.support.annotation.Nullable;

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
            this.mColor = this.mColor;
        }

        switch (mShape) {
            case I:
                this.setBlock(new TetrominoBlock(0, 0, this.mColor));
                this.setBlock(new TetrominoBlock(0, 1, this.mColor));
                this.setBlock(new TetrominoBlock(0, 2, this.mColor));
                this.setBlock(new TetrominoBlock(0, 3, this.mColor));
                break;
            case O:
                this.setBlock(new TetrominoBlock(0, 0, this.mColor));
                this.setBlock(new TetrominoBlock(0, 1, this.mColor));
                this.setBlock(new TetrominoBlock(1, 0, this.mColor));
                this.setBlock(new TetrominoBlock(1, 1, this.mColor));
                break;
            case T:
                this.setBlock(new TetrominoBlock(0, 0, this.mColor));
                this.setBlock(new TetrominoBlock(1, 0, this.mColor));
                this.setBlock(new TetrominoBlock(2, 0, this.mColor));
                this.setBlock(new TetrominoBlock(1, 1, this.mColor));
                break;
            case J:
                this.setBlock(new TetrominoBlock(0, 0, this.mColor));
                this.setBlock(new TetrominoBlock(1, 0, this.mColor));
                this.setBlock(new TetrominoBlock(2, 0, this.mColor));
                this.setBlock(new TetrominoBlock(2, 1, this.mColor));
                break;
            case L:
                this.setBlock(new TetrominoBlock(0, 0, this.mColor));
                this.setBlock(new TetrominoBlock(0, 1, this.mColor));
                this.setBlock(new TetrominoBlock(0, 2, this.mColor));
                this.setBlock(new TetrominoBlock(1, 2, this.mColor));
                break;
            case S:
                this.setBlock(new TetrominoBlock(1, 0, this.mColor));
                this.setBlock(new TetrominoBlock(2, 0, this.mColor));
                this.setBlock(new TetrominoBlock(0, 1, this.mColor));
                this.setBlock(new TetrominoBlock(1, 1, this.mColor));
                break;
            case Z:
                this.setBlock(new TetrominoBlock(0, 0, this.mColor));
                this.setBlock(new TetrominoBlock(1, 0, this.mColor));
                this.setBlock(new TetrominoBlock(1, 1, this.mColor));
                this.setBlock(new TetrominoBlock(2, 1, this.mColor));
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
