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

    private String TAG = "Item";

    // References
    CreateMapView mView;
    private Grid mGrid;

    // Geometry
    float mX = 0;
    float mY = 0;
    private int mWidth = 0;
    private int mHeight = 0;
    PointF mPosition = new PointF();

    // List of Contained Blocks
    private ArrayList<Block> mBlocks = new ArrayList<>();

    // Moved Item helpers
    private float dx = 0;
    private float dy = 0;
    private PointF mInitialPosition;

    /**
     * Constructor of an Item
     * @param view the Parent View
     * @param grid the Grid
     */
    public Item(CreateMapView view, Grid grid) {
        mView = view;
        mGrid = grid;
    }

    /**
     * Constructor of an Item
     * @param view the Parent View
     * @param grid the Grid
     * @param x the x Position in the Grid
     * @param y the y Position in the Grid
     */
    public Item(CreateMapView view, Grid grid, float x, float y) {
        mView = view;
        mGrid = grid;
        mX = x;
        mY = y;
        mPosition.set(mX, mY);
    }

    /**
     * Get the x Position in the Grid Coordinates System
     * @return
     */
    public float getX() {
        return mX;
    }

    /**
     * Set the x Position in the Grid Coordinates System
     * @param x
     */
    public void setX(float x) {
        mX = x;
        mPosition.x = x;
    }

    /**
     * Get the y Position in the Grid Coordinates System
     * @return
     */
    public float getY() {
        return mY;
    }

    /**
     * Set the y Position in the Grid Coordinates System
     * @param y
     */
    public void setY(float y) {
        mY = y;
        mPosition.y = y;
    }

    /**
     * Get the Position in the Grid Coordiantes System
     * @return
     */
    public PointF getPosition() {
        return mPosition;
    }

    /**
     * Set the Position in the Grid Coordiantes System
     * @param position
     */
    public void setPosition(PointF position) {
        mPosition = position;
        mX = position.x;
        mY = position.y;
        mView.invalidate();
    }

    /**
     * Populate the Item with a Block
     * @param block
     */
    public void setBlock(Block block) {
        mBlocks.add(block);
        if (block.getX() > mWidth) mWidth = (int) block.getX();
        if (block.getY() > mHeight) mHeight = (int) block.getY();
    }

    /**
     * Method to draw the Item on the Canvas
     * @param canvas
     */
    public void draw(Canvas canvas) {
        float cellSize = mGrid.getCellSize();
        for (Block block : mBlocks) {
            block.draw(canvas, mX, mY, cellSize);
        }
    }

    /**
     * Check if the PointF is contained in the Item
     * @param pointF in the Grid Coordinates System
     * @return
     */
    public boolean contains(PointF pointF) {
        for (Block block : mBlocks) {
            PointF itemCoordinates = mapToItem(pointF);
            if (block.contains(itemCoordinates)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Map the PointF from the Grid Coordinate System to Item Coordinates System
     * @param pointF
     * @return
     */
    private PointF mapToItem(PointF pointF) {
        return new PointF(pointF.x - mX, pointF.y - mY);
    }

    /**
     * Map the PointF from Item Coordinates System to the Grid Coordinate System
     * @param pointF
     * @return
     */
    private PointF mapFromItem(PointF pointF) {
        return new PointF(pointF.x + mX, pointF.y + mY);
    }

    /**
     * The Method Handling Touch Event
     * @param parentView the parent transmitting the Touch Event
     * @param event
     * @return
     */
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
                pos = contrainsToGrid(pos);
                setPosition(pos);
                break;

            case MotionEvent.ACTION_UP:
                pos.x = Math.round(mX);
                pos.y = Math.round(mY);
                pos = contrainsToGrid(pos);
                pos = avoidSuperposition(pos);
                setPosition(pos);
                break;
        }
        return true;
    }

    /**
     * Method Constraining the Point into the Grid limits
     * @param pointF
     * @return
     */
    private PointF contrainsToGrid(PointF pointF) {
        int mGridSize = mGrid.getSize();
        if (pointF.x < 0) pointF.x = 0;
        if (pointF.x + mWidth >= mGridSize - 1) pointF.x = mGridSize - 1 - mWidth;
        if (pointF.y < 0) pointF.y = 0;
        if (pointF.y + mHeight >= mGridSize - 1) pointF.y = mGridSize - 1 - mHeight;
        return pointF;
    }

    /**
     * Method that Check for superposition
     * @param pointF The (Grid) Coordinates of the Item
     * @return the PointF if no superposition, the initial position otherwise
     */
    private PointF avoidSuperposition(PointF pointF) {
        for (Item item : mView.getItems()) {
            for (Block block : mBlocks) {
                PointF blockPos = mapFromItem(block.getPosition());
                blockPos.x = Math.round(blockPos.x);
                blockPos.y = Math.round(blockPos.y);
                if (item != this && item.contains(blockPos)) {
                    return mInitialPosition;
                }
            }
        }
        return pointF;
    }

    /**
     * String representation of the Item
     * @return
     */
    @Override
    public String toString() {
        return "Item{" +
                "mBlocks=" + mBlocks +
                '}';
    }
}
