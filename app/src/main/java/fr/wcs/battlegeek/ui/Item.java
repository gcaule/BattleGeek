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
    float mX = 0;
    float mY = 0;
    PointF mPosition = new PointF();
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

    public Item(CreateMapView view, Grid grid, float x, float y) {
        mView = view;
        mGrid = grid;
        mX = x;
        mY = y;
        mPosition.set(mX, mY);
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

    public PointF getPosition() {
        return mPosition;
    }

    public void setPosition(PointF position) {
        mPosition = position;
        mX = position.x;
        mY = position.y;
        mView.invalidate();
    }

    public void setBlock(Block block) {
        mBlocks.add(block);
        if (block.getX() > mWidth) mWidth = (int) block.getX();
        if (block.getY() > mHeight) mHeight = (int) block.getY();
    }

    public void draw(Canvas canvas) {
        float cellSize = mGrid.getCellSize();
        for (Block block : mBlocks) {
            block.draw(canvas, mX, mY, cellSize);
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
        return new PointF(pointF.x - mX, pointF.y - mY);
    }

    private PointF mapFromItem(PointF pointF) {
        return new PointF(pointF.x + mX, pointF.y + mY);
    }

    @Override
    public boolean onTouch(View parentView, MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        PointF pos = mGrid.mapToGrid(x, y);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mInitialPosition = new PointF(mX, mY);
                dx = mX - pos.x;
                dy = mY - pos.y;
                break;

            case MotionEvent.ACTION_MOVE:
                pos.offset(dx, dy);
                pos = spanToGrid(pos);
                setPosition(pos);
                break;

            case MotionEvent.ACTION_UP:
                pos.x = Math.round(mX);
                pos.y = Math.round(mY);
                pos = spanToGrid(pos);
                pos = avoidSuperposition(pos);
                setPosition(pos);
                break;
        }
        return true;
    }

    private PointF spanToGrid(PointF pointF) {
        int mGridSize = mGrid.getSize();
        if (pointF.x < 0) pointF.x = 0;
        if (pointF.x + mWidth >= mGridSize - 1) pointF.x = mGridSize - 1 - mWidth;
        if (pointF.y < 0) pointF.y = 0;
        if (pointF.y + mHeight >= mGridSize - 1) pointF.y = mGridSize - 1 - mHeight;
        return pointF;
    }

    private PointF avoidSuperposition(PointF pointF) {
        for(Item item : mView.getItems()) {
            for(Block block : mBlocks) {
                if(item != this && item.contains(mapFromItem(block.getPosition()))){
                    return mInitialPosition;
                }
            }
        }
        return pointF;
    }
}
