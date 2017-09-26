package fr.wcs.battlegeek.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

/**
 * Created by adphi on 25/09/17.
 */

public class Grid {
    private CreateMapView mView;
    private int size;
    private float mCellSize;
    private int mWidth;
    private int mHeight;
    private Paint mBlackPaint = new Paint();

    public Grid(CreateMapView view, int mWidth, int mHeight, int size) {
        this.mView = view;
        this.mWidth = mWidth;
        this.mHeight = mHeight;
        this.size = size;
        init();
    }

    public Grid(int size) {
        this.size = size;
        init();
    }

    private void init() {
        mBlackPaint.setAntiAlias(true);
        mBlackPaint.setStyle(Paint.Style.STROKE);
        mBlackPaint.setColor(Color.BLACK);
        mBlackPaint.setStrokeWidth(2);
    }

    public void draw(Canvas canvas) {
        int yOffset = mHeight - mWidth;
        mCellSize = mWidth / size;
        for (int row = 0; row < size; row++) {
            for (int column = 0; column < size; column++) {
                float x = column * mCellSize;
                float y = row * mCellSize + yOffset;
                canvas.drawRect(x, y, x + mCellSize, y + mCellSize, mBlackPaint);
            }
        }
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public float getCellSize() {
        return mCellSize;
    }

    public boolean contains(float x, float y) {
        PointF pointF = mapToGrid(x, y);
        float pX = pointF.x;
        float pY = pointF.y;
        if (pX >= 0 && pX < size && pY >= 0 && pY < size) {
            return true;
        } else return false;
    }

    public boolean contains(PointF pointF) {
        pointF = mapToGrid(pointF);
        if (pointF.x >= 0 && pointF.x < size && pointF.y >= 0 && pointF.y < size) {
            return true;
        } else return false;
    }

    public PointF mapToGrid(float x, float y) {
        PointF pointF = new PointF();
        pointF.set(x / mCellSize, y / mCellSize);
        return pointF;
    }

    public PointF mapToGrid(PointF pointF) {
        pointF.set(pointF.x / mCellSize, pointF.y / mCellSize);
        return pointF;
    }

    public PointF contrainsToGrid(PointF pointF) {
        if (pointF.x < 0) pointF.x = 0;
        if (pointF.x >= size - 1) pointF.x = size - 1;
        if (pointF.y < 0) pointF.y = 0;
        if (pointF.y >= size - 1) pointF.y = size - 1;
        return pointF;
    }
}
