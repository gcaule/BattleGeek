package fr.wcs.battlegeek.ui;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import fr.wcs.battlegeek.Model.Settings;
import fr.wcs.battlegeek.R;
import fr.wcs.battlegeek.Utils.Utils;

/**
 * Created by adphi on 25/09/17.
 */

public class Item implements View.OnTouchListener {
    private String TAG = "Item";

    enum State {
        Alive,
        Hit,
        Dead;
    }

    // Item State
    State mState = State.Alive;

    // References
    private MapView mView;
    private Grid mGrid;

    // Geometry
    private float mX = 0;
    private float mY = 0;
    private PointF mPosition = new PointF();
    private int mWidth = 0;
    private int mHeight = 0;

    // List of Contained Blocks
    private ArrayList<Block> mBlocks = new ArrayList<>();

    // Moved Item helpers
    private float dx = 0;
    private float dy = 0;
    private PointF mInitialPosition;

    /**
     * Constructor of an Item
     *
     * @param view the Parent View
     * @param grid the Grid
     */
    public Item(MapView view, Grid grid) {
        this.mView = view;
        this.mGrid = grid;
    }
    /**
     * Constructor of an Item
     *
     * @param view the Parent View
     * @param grid the Grid
     * @param x    the x Position in the Grid
     * @param y    the y Position in the Grid
     */
    public Item(MapView view, Grid grid, float x, float y) {
        this.mView = view;
        this.mGrid = grid;
        this.mX = x;
        this.mY = y;
        this.mPosition.set(mX, mY);
    }

    public Item(Item item) {
        this.mView = item.mView;
        this.mGrid = item.mGrid;
        this.mBlocks = item.mBlocks;
        this.mWidth = item.mWidth;
        this.mHeight = item.mHeight;
        this.dx = item.dx;
        this.dy = item.dy;
        this.mX = item.mX;
        this.mY = item.mY;
        this.mPosition = item.mPosition;
        this.mState = item.mState;
    }

    public State getState() {
        return mState;
    }

    public void setState(State state) {
        mState = state;
    }

    /**
     * Get the x Position in the Grid Coordinates System
     *
     * @return
     */
    public float getX() {
        return mX;
    }

    /**
     * Set the x Position in the Grid Coordinates System
     *
     * @param x
     */
    public void setX(float x) {
        this.mX = x;
        this.mPosition.x = x;
    }

    /**
     * Get the y Position in the Grid Coordinates System
     *
     * @return
     */
    public float getY() {
        return mY;
    }

    /**
     * Set the y Position in the Grid Coordinates System
     *
     * @param y
     */
    public void setY(float y) {
        this.mY = y;
        this.mPosition.y = y;
    }

    /**
     * Get the Position in the Grid Coordiantes System
     *
     * @return
     */
    public PointF getPosition() {
        return mPosition;
    }

    /**
     * Set the Position in the Grid Coordiantes System
     *
     * @param position
     */
    public void setPosition(PointF position) {
        this.mPosition = position;
        this.mX = position.x;
        this.mY = position.y;
        // Refresh the View
        this.mView.invalidate();
    }

    public void rotate() {
        // Get the square's size in witch the piece rotate
        int size = Math.max(getWidth(), getHeight());
        PointF rotationCenter = new PointF(size / 2, size / 2);

        // Initiate value to remove blocks offset
        int minX = Settings.GRID_SIZE;
        int minY = Settings.GRID_SIZE;

        // Rotate Blocks
        for(Block block : mBlocks) {
            int x = (int) block.getX();
            int y = (int) block.getY();
            int x1 = (int)(rotationCenter.x + rotationCenter.y - y);
            int y1 = (int)(rotationCenter.y - rotationCenter.x + x);
            block.setX(x1);
            block.setY(y1);

            // Get Min offset
            minX = Math.min(minX, x1);
            minY = Math.min(minY, y1);
        }

        // Apply Offset On the Blocks
        for(Block block : mBlocks) {
            block.setX(block.getX() - minX);
            block.setY(block.getY() - minY);
        }

        // Rotate width and height
        int width = mWidth;
        mWidth = mHeight;
        mHeight = width;
    }

    private int getWidth() {
        mWidth = 0;
        for(Block block : mBlocks) {
            if (block.getX() > mWidth) mWidth = (int) block.getX();
        }
        return mWidth;
    }

    private int getHeight() {
        mHeight = 0;
        for(Block block : mBlocks) {
            if (block.getY() > mHeight) mHeight = (int) block.getY();
        }
        return mHeight;
    }

    /**
     * Populate the Item with a Block
     *
     * @param block
     */
    public void setBlock(Block block) {
        this.mBlocks.add(block);
        if (block.getX() > mWidth) mWidth = (int) block.getX();
        if (block.getY() > mHeight) mHeight = (int) block.getY();
    }

    /**
     * Method to get the Item's Block from Grid Coordinates
     * @param x
     * @param y
     * @return
     */
    public Block getBlock(int x, int y) {
        for(Block block : mBlocks) {
            PointF mappedCoordinates = mapToItem(new PointF(x, y));
            if(block.getPosition().x == mappedCoordinates.x
                    && block.getPosition().y == mappedCoordinates.y) {
                return block;
            }
        }
        return null;
    }

    /**
     * Get the Blocks of the Item
     *
     * @return the ArrayList containing the Blocks
     */
    public ArrayList<Block> getBlocks(){
        return this.mBlocks;
    }

    /**
     * Method to draw the Item on the Canvas
     *
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
     *
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
     *
     * @param pointF
     * @return
     */
    private PointF mapToItem(PointF pointF) {
        return new PointF(pointF.x - mX, pointF.y - mY);
    }

    /**
     * Map the PointF from Item Coordinates System to the Grid Coordinate System
     *
     * @param pointF
     * @return
     */
    private PointF mapFromItem(PointF pointF) {
        return new PointF(pointF.x + mX, pointF.y + mY);
    }

    /**
     * The Method Handling Touch Event
     *
     * @param parentView the parent transmitting the Touch Event
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View parentView, MotionEvent event) {
        // Get the Coordinates of the Touch Event
        float x = event.getX();
        float y = event.getY();
        // Mapt Coordinates to the Grid Coordinates System
        PointF pos = mGrid.mapToGrid(x, y);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Store Initial Position
                this.mInitialPosition = new PointF(mX, mY);
                // Get the offset of the finger in the Item in order to not set the (0,0) of the Item at the Event Position
                dx = mX - pos.x;
                dy = mY - pos.y;
                break;

            case MotionEvent.ACTION_MOVE:
                // Apply the Offset
                pos.offset(dx, dy);
                // Limit position to the Grid's limits
                pos = contrainsToGrid(pos);
                // Apply the position change
                setPosition(pos);
                break;

            case MotionEvent.ACTION_UP:

                if(mInitialPosition.x == mX && mInitialPosition.y == mY) {
                    //ArrayList<Block> blocks = Utils.copy(Block.class, mBlocks);
                    ArrayList<Block> blocks = Utils.copyBlocks(mBlocks);
                    rotate();
                    if(isHoverItem(mInitialPosition)) {
                        mBlocks = blocks;
                        Toast.makeText(mView.getContext(), R.string.RotationImpossible, Toast.LENGTH_SHORT).show();
                    }
                }
                // Snap the Item to the Grid
                pos.x = Math.round(mX);
                pos.y = Math.round(mY);
                // Limit position to the Grid's limits
                pos = contrainsToGrid(pos);
                // Check for Superposition
                pos = isHoverItem(pos) ? mInitialPosition : pos;
                // Apply the position change
                setPosition(pos);
                break;
        }
        return true;
    }

    /**
     * Method Constraining the Point into the Grid limits
     *
     * @param pointF
     * @return
     */
    private PointF contrainsToGrid(PointF pointF) {
        // get the grid Size
        int mGridSize = mGrid.getSize();
        // Apply limits
        if (pointF.x < 0) pointF.x = 0;
        if (pointF.x + mWidth >= mGridSize - 1) pointF.x = mGridSize - 1 - mWidth;
        if (pointF.y < 0) pointF.y = 0;
        if (pointF.y + mHeight >= mGridSize - 1) pointF.y = mGridSize - 1 - mHeight;
        return pointF;
    }

    /**
     * Method that Check for superposition
     *
     * @param pointF The (Grid) Coordinates of the Item
     * @return the PointF if no superposition, the initial position otherwise
     */
    private boolean isHoverItem(PointF pointF) {
        // In order to avoid superposition we need to iterate through each item
        // contained by the parent View
        for (Item item : mView.getItems()) {
            // We also need to check for each block of the current Item
            for (Block block : mBlocks) {
                // We switch to the Grid Coordinate System
                PointF blockPos = mapFromItem(block.getPosition());
                // the tricky part: the last position was not snap to grid
                // so we need to Snap the position since the compared blocks
                // have snap coordinates
                blockPos.x = Math.round(blockPos.x);
                blockPos.y = Math.round(blockPos.y);
                // If the Item is not the one we are checking superposition for,
                // and compared Item is on the same position
                if (item != this && item.contains(blockPos)) {
                    // we return the initial Touch Sequence position
                    return true;
                }
            }
        }
        // if we are here, everything is ok
        return false;
    }

    /**
     * String representation of the Item
     *
     * @return
     */
    @Override
    public String toString() {
        return "Item{" +
                "mBlocks=" + mBlocks +
                "}";
    }
}