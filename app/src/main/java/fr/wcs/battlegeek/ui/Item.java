package fr.wcs.battlegeek.ui;

import android.graphics.Canvas;
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
    private String TAG = "Item";
    private Grid mGrid;
    private ArrayList<Block> mBlocks = new ArrayList<>();

    private int mWidth = 0;
    private int mHeight = 0;
    private float dx = 0;
    private float dy = 0;
    private PointF mInitialPosition;

    public Item(CreateMapView view, Grid grid) {
        mView = view;
        mGrid = grid;
    }

    public Item(CreateMapView view, Grid grid, float gridX, float gridY) {
        mView = view;
        mGrid = grid;
        mGridX = gridX;
        mGridY = gridY;
        position.set(mGridX, mGridY);
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
        Block block = getBlock(position);
        if (block != null) {
            float offsetX = - block.getX();
            float offsetY = - block.getY();
            position.offset(offsetX, offsetY);
        }
        this.position = contrainsToGrid(position);
        mGridX = position.x;
        mGridY = position.y;
        mView.invalidate();
    }

    public void setBlock(Block block){
        mBlocks.add(block);
        if(block.getX() > mWidth) mWidth = (int) block.getX();
        if(block.getY() > mHeight) mHeight = (int) block.getY();
    }

    public void draw(Canvas canvas) {
        float cellSize = mGrid.getCellSize();
        for (Block block : mBlocks) {
            block.draw(canvas, mGridX, mGridY, cellSize);
        }

    }

    public boolean contains(PointF pointF) {
        for (Block block : mBlocks) {
            PointF itemCoordinates = mapToItem(pointF);
            if (block.contains(itemCoordinates)) {
                return true;
            }
        }
        return false;
    }

    private PointF mapToItem(PointF pointF) {
        return new PointF(pointF.x - mGridX, pointF.y - mGridY);
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        PointF pos = mGrid.mapToGrid(x, y);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mInitialPosition = new PointF(mGridX, mGridY);
                dx = mGridX - pos.x;
                dy = mGridY - pos.y;
                break;

            case MotionEvent.ACTION_MOVE:
                //Log.e(TAG, "onTouchEvent: Move");
                pos.offset(dx, dy);
                setPosition(pos);
                break;

            case MotionEvent.ACTION_UP:
                setPosition(new PointF((float) (int) pos.x, (float) (int) pos.y));
                break;
        }
        return true;
    }

    private Block getBlock(PointF pointF) {
        for (Block block : mBlocks) {
            PointF pos = mapToItem(pointF);
            if (block.contains(pos)) {
                return block;
            }
        }
        return null;
    }

    private PointF contrainsToGrid(PointF pointF) {
        int mGridSize = mGrid.getSize();
        if (pointF.x < 0) pointF.x = 0;
        if (pointF.x + mWidth >= mGridSize - 1) pointF.x = mGridSize - 1 - mWidth;
        if (pointF.y < 0) pointF.y = 0;
        if (pointF.y + mHeight >= mGridSize - 1) pointF.y = mGridSize - 1 - mHeight;
        return pointF;
    }
}
