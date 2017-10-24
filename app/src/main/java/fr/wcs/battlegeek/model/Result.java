package fr.wcs.battlegeek.model;

import android.support.annotation.Nullable;

import java.util.Comparator;

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
        TOUCHED, MISSED, DROWN, VICTORY, DEFEATED, BONUS
    }

    // Shape and Type attributes
    private Tetromino.Shape mShape;
    private Type mType;
    private Bonus.Type mBonusType = null;
    private int x;
    private int y;
    /**
     * Constructor of a Result Object
     * @param x
     * @param y
     * @param shape the Shape of the Touched Tetromino (NONE if Missed)
     * @param type the Result Type
     */
    public Result(int x, int y, Tetromino.Shape shape, Type type, @Nullable Bonus.Type bonusType) {
        this.x = x;
        this.y = y;
        mShape = shape;
        mType = type;
        mBonusType = bonusType;
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
     * Bonus type getter (if Bonus else Null)
     * @return
     */
    public Bonus.Type getBonusType() {
        return mBonusType;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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

    /**
     * In order to compare the Bonus Types, we add Priorities Values;
     * @return
     */
    private int getValue() {
        switch (mType) {
            case BONUS:
                return 0;
            case MISSED:
                return 1;
            case TOUCHED:
                return 2;
            case DROWN:
                return 3;
            case VICTORY:
                return 4;
            case DEFEATED:
                return 5;
        }
        return -1;
    }

    /**
     * Method for sorting the Results by Type Priority
     */
    public static Comparator<Result> resultComparator = new Comparator<Result>() {
        @Override
        public int compare(Result result, Result t1) {
            return result.getValue() - t1.getValue();
        }
    };
}
