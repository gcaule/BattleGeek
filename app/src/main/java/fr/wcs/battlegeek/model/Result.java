package fr.wcs.battlegeek.model;

import fr.wcs.battlegeek.ui.Tetromino;

/**
 * Created by adphi on 03/10/17.
 */

/**
 * The Result Class Store the different Shot's Result elements :
 * - the Type: Touched, Missed, Drown, Victory
 */
public class Result {
    /**
     * The Result Types
     */
    public enum Type {
        TOUCHED, MISSED, DROWN, VICTORY, DEFEATED, ALREADY_PLAYED;
    }

    // Shape and Type attributes
    private Tetromino.Shape mShape;
    private Type mType;

    /**
     * Constructor of a Result Object
     * @param shape the Shape of the Touched Tetromino (NONE if Missed)
     * @param type the Result Type
     */
    public Result(Tetromino.Shape shape, Type type) {
        mShape = shape;
        mType = type;
    }

    /**
     * Result's Shape getter
     * @return
     */
    public Tetromino.Shape getShape() {
        return mShape;
    }

    /**
     * Result's Type getter
     * @return
     */
    public Type getType() {
        return mType;
    }

    /**
     * Result String representation
     * @return
     */
    @Override
    public String toString() {
        return "Result{" +
                "mShape=" + mShape +
                ", mType=" + mType +
                '}';
    }
}
