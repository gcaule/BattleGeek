package fr.wcs.battlegeek.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by adphi on 25/09/17.
 */

public class Item implements View.OnTouchListener {
    CreateMapView mView;
    float mGridX = 0;
    float mGridY = 0;
    PointF position = new PointF();
    Paint mPaintStroke = new Paint();
    Paint mPaintFill = new Paint();
    private String TAG = "Item";
    private Grid mGrid;
    private ArrayList<Block> mBlocks = new ArrayList<>();

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

        mBlocks.add(new Block(0, 0));
    }

    public void draw(Canvas canvas) {
        float cellSize = mGrid.getCellSize();
        for (Block block : mBlocks) {
            float x = (mGridX + block.getX()) * cellSize;
            float y = (mGridY + block.getY()) * cellSize;
            canvas.drawRect(x, y, x + cellSize, y + cellSize, mPaintFill);
            canvas.drawRect(x, y, x + cellSize, y + cellSize, mPaintStroke);
        }

    }

    public boolean contains(PointF pointF) {
        for (Block block : mBlocks) {
            PointF itemCoordiantes = mapToItem(pointF);
            if (block.contains(itemCoordiantes)) {
                return true;
            }
        }
        return false;
    }

    private PointF mapToItem(PointF pointF) {
        return new PointF(pointF.x - mGridX, pointF.y - mGridY);
    }

    private float dx = 0;
    private float dy = 0;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        PointF pos = mGrid.mapToGrid(x, y);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dx = mGridX - pos.x;
                dy = mGridY - pos.y;
                break;

            case MotionEvent.ACTION_MOVE:
                //Log.e(TAG, "onTouchEvent: Move");
                pos.offset(dx, dy);
                pos = mGrid.contrainsToGrid(pos);
                setPosition(pos);
                break;

            case MotionEvent.ACTION_UP:
                pos = mGrid.contrainsToGrid(pos);
                setPosition(new PointF((float) (int) pos.x, (float) (int) pos.y));
                break;
        }
        return true;
    }
}
