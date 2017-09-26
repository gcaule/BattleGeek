package fr.wcs.battlegeek.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;

/**
 * Created by adphi on 25/09/17.
 */

public class Item {
    private String TAG = "Item";
    private Grid mGrid;
    CreateMapView mView;
    float mGridX = 0;
    float mGridY = 0;
    PointF position = new PointF();
    Paint mPaintStroke = new Paint();
    Paint mPaintFill = new Paint();
    private ArrayList<Point> mElements = new ArrayList<>();

    public Item(CreateMapView view, Grid grid) {
        mView = view;
        mGrid = grid;
        init();
    }

    public Item(CreateMapView view, Grid grid, float gridX, float gridY) {
        mView = view;
        mGrid = grid;
        mGridX = gridX;
        mGridY = gridY;
        position.set(mGridX, mGridY);
        init();
    }

    public float getGridX() {
        return mGridX;
    }

    public void setGridX(float gridX) {
        mGridX = gridX;
        position.set(mGridX, mGridY);
    }

    public float getGridY() {
        return mGridY;
    }

    public void setGridY(float gridY) {
        mGridY = gridY;
        position.set(mGridX, mGridY);
    }

    public PointF getPosition() {
        return position;
    }

    public void setPosition(PointF position) {
        this.position = position;
        mGridX = position.x;
        mGridY = position.y;
        mView.invalidate();
    }

    private void init() {
        mPaintStroke.setStyle(Paint.Style.STROKE);
        mPaintStroke.setStrokeWidth(2);
        mPaintStroke.setColor(Color.BLACK);
        mPaintStroke.setAntiAlias(true);

        mPaintFill.setStyle(Paint.Style.FILL);
        mPaintFill.setColor(Color.DKGRAY);

        mElements.add(new Point(0, 0));
        mElements.add(new Point(1, 0));
    }

    public void draw(Canvas canvas) {
        float cellSize = mGrid.getCellSize();
        for (Point element : mElements) {
            float x = (mGridX + element.x) * cellSize;
            float y = (mGridY + element.y) * cellSize;
            canvas.drawRect(x, y, x + cellSize, y + cellSize, mPaintFill);
            canvas.drawRect(x, y, x + cellSize, y + cellSize, mPaintStroke);
        }

    }

    public boolean contains(PointF pointF) {
        for (Point point : mElements) {
            if ((int) pointF.x == position.x + point.x && (int) pointF.y == position.y + point.y) {
                return true;
            }
        }
        return false;
    }

}
