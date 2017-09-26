package fr.wcs.battlegeek.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

/**
 * Created by adphi on 26/09/17.
 */

public class Block {
    private PointF mCoordinates;
    private float mX;
    private float mY;
    private Paint mPaintStroke = new Paint();
    private Paint mPaintFill = new Paint();

    public Block(PointF coordinates) {
        mCoordinates = coordinates;
        mX = coordinates.x;
        mY = coordinates.y;
        init();
    }

    public Block(float x, float y) {
        mCoordinates = new PointF(x, y);
        mX = x;
        mY = y;
        init();
    }

    private void init() {
        mPaintStroke.setStyle(Paint.Style.STROKE);
        mPaintStroke.setStrokeWidth(2);
        mPaintStroke.setColor(Color.BLACK);
        mPaintStroke.setAntiAlias(true);

        mPaintFill.setStyle(Paint.Style.FILL);
        mPaintFill.setColor(Color.DKGRAY);
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

    public void draw(Canvas canvas, float itemX, float itemY, float blockSize) {
        float x = (itemX + mX) * blockSize;
        float y = (itemY + mY) * blockSize;
        canvas.drawRect(x, y, x + blockSize, y + blockSize, mPaintFill);
        canvas.drawRect(x, y, x + blockSize, y + blockSize, mPaintStroke);
    }
}
