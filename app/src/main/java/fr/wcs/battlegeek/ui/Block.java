package fr.wcs.battlegeek.ui;

import android.graphics.PointF;

/**
 * Created by adphi on 26/09/17.
 */

public class Block {
    private PointF mCoordinates;
    private float mX;
    private float mY;

    public Block(PointF coordinates) {
        mCoordinates = coordinates;
        mX = coordinates.x;
        mY = coordinates.y;
    }

    public Block(float x, float y) {
        mCoordinates = new PointF(x, y);
        mX = x;
        mY = y;
    }

    public float getX() {
        return mX;
    }

    public void setX(float x) {
        mX = x;
        mCoordinates.x = x;
    }

    public float getY() {
        return mY;
    }

    public void setY(float y) {
        mY = y;
        mCoordinates.y = y;
    }

    public boolean contains(PointF pointF) {
        if (mX <= pointF.x && pointF.x < mX + 1 &&
                mY <= pointF.y && pointF.y < mY + 1)
            return true;
        else return false;
    }
}
