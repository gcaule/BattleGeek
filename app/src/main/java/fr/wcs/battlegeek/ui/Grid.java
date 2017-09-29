package fr.wcs.battlegeek.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

/**
 * Created by adphi on 25/09/17.
 */

public class Grid {

    // Geometry
    private int size;
    private int mWidth;
    private int mHeight;
    private float mCellSize;

    // Painting
    private Paint mBlackPaint = new Paint();

    /**
     * Contructor of the Grid
     * @param view the Parent View
     * @param mWidth the Width of the Grid (Global Coordinates System
     * @param mHeight the Height of the Grid (Global Coordinates System
     * @param size The number of Cells in a Row or in a Column
     */
    public Grid(int mWidth, int mHeight, int size) {
        this.mWidth = mWidth;
        this.mHeight = mHeight;
        this.size = size;
        init();
    }

    /**
     * Contructor of the Grid
     * @param size The number of Cells in a Row or in a Column
     */
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

    /**
     * Method to draw the Grid to the Canvas
     * @param canvas
     */
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

    /**
     * Get the Width of the Grid
     * @return
     */
    public int getWidth() {
        return mWidth;
    }

    /**
     * Set the Width of the Grid
     * @param width
     */
    public void setWidth(int width) {
        this.mWidth = width;
    }

    /**
     * Get the Height of the Width
     * @return
     */
    public int getHeight() {
        return mHeight;
    }

    /**
     * Set the Height of the Frid
     * @param mHeight
     */
    public void setHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    /**
     * Get the number of Cells the Side contains
     * @return
     */
    public int getSize() {
        return size;
    }

    /**
     * Set the number of the Cells the Side contains
     * @param size
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Get the Cell Size in the Global Coordinates System
     * @return
     */
    public float getCellSize() {
        return mCellSize;
    }

    /**
     * Check if the PointF is contains in the Grid
     * @param x
     * @param y
     * @return
     */
    public boolean contains(float x, float y) {
        PointF pointF = mapToGrid(x, y);
        float pX = pointF.x;
        float pY = pointF.y;
        if (pX >= 0 && pX < size && pY >= 0 && pY < size) {
            return true;
        } else return false;
    }

    /**
     * Check if the PointF is contains in the Grid
     * @param pointF
     * @return
     */
    public boolean contains(PointF pointF) {
        if (pointF.x >= 0 && pointF.x < size && pointF.y >= 0 && pointF.y < size) {
            return true;
        } else return false;
    }

    /**
     * Method to Map a point to the Grid Coordinates System
     * @param x
     * @param y
     * @return
     */
    public PointF mapToGrid(float x, float y) {
        PointF pointF = new PointF();
        pointF.set(x / mCellSize, y / mCellSize);
        return pointF;
    }

    /**
     * Method to Map a point to the Grid Coordinates System
     * @param pointF
     * @return
     */
    public PointF mapToGrid(PointF pointF) {
        pointF.set(pointF.x / mCellSize, pointF.y / mCellSize);
        return pointF;
    }

    /**
     * Method to contrains the PointF to the Grid's limits
     * @param pointF
     * @return
     */
    public PointF contrainsToGrid(PointF pointF) {
        if (pointF.x < 0) pointF.x = 0;
        if (pointF.x >= size - 1) pointF.x = size - 1;
        if (pointF.y < 0) pointF.y = 0;
        if (pointF.y >= size - 1) pointF.y = size - 1;
        return pointF;
    }
}
