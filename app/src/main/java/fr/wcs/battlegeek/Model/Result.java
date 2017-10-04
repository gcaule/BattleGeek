package fr.wcs.battlegeek.Model;

import fr.wcs.battlegeek.ui.Tetromino;

/**
 * Created by adphi on 03/10/17.
 */

public class Result {
    public enum Type {
        TOUCHED, MISSED, DROWN, VICTORY, ALREADY_PLAYED;
    }
    private Tetromino.Shape mShape;

    public Tetromino.Shape getShape() {
        return mShape;
    }

    public Type getType() {
        return mType;
    }

    private Type mType;

    public Result(Tetromino.Shape shape, Type type) {
        mShape = shape;
        mType = type;
    }

    @Override
    public String toString() {
        return "Result{" +
                "mShape=" + mShape +
                ", mType=" + mType +
                '}';
    }
}
