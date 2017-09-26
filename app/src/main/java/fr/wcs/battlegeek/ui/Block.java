package fr.wcs.battlegeek.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

/**
 * Created by adphi on 26/09/17.
 */

public class Block {
    public PointF getPosition() {
        return mPosition;
    }

    private PointF mPosition;
    private float mX;
    private float mY;
    private Paint mPaintStroke = new Paint();
    private Paint mPaintFill = new Paint();
    private float mBlockSize;

    public Block(PointF position) {
        mPosition = position;
        mX = position.x;
        mY = position.y;
        init();
    }
    public Block(float x, float y) {
        mPosition = new PointF(x, y);
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
        mPosition.x = x;
    }

    public float getY() {
        return mY;
    }

    public void setY(float y) {
        mY = y;
        mPosition.y = y;
    }

    public boolean contains(PointF pointF) {
        if (mX <= pointF.x && pointF.x < mX + 1 &&
                mY <= pointF.y && pointF.y < mY + 1)
            return true;
        else return false;
    }

    public void draw(Canvas canvas, float itemX, float itemY, float blockSize) {
        mBlockSize = blockSize;
        float x = (itemX + mX) * mBlockSize;
        float y = (itemY + mY) * mBlockSize;
        canvas.drawRect(x, y, x + mBlockSize, y + mBlockSize, mPaintFill);
        canvas.drawRect(x, y, x + mBlockSize, y + mBlockSize, mPaintStroke);
    }
}
