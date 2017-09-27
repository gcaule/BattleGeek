package fr.wcs.battlegeek.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import static fr.wcs.battlegeek.ui.Block.State.Alive;

/**
 * Created by adphi on 26/09/17.
 */

public class Block {

    // State
    private State mState = Alive;

    // Geometry
    private PointF mPosition;
    private float mX;
    private float mY;
    private float mBlockSize;

    // Painting
    private Paint mPaintStroke = new Paint();
    private Paint mPaintAliveFill = new Paint();
    private Paint mPaintDeadFill = new Paint();

    /**
     * Constructor
     *
     * @param position PointF in Item
     */
    public Block(PointF position) {
        mPosition = position;
        mX = position.x;
        mY = position.y;
        init();
    }

    /**
     * Constructor
     *
     * @param x position in Item
     * @param y position in Item
     */
    public Block(float x, float y) {
        mPosition = new PointF(x, y);
        mX = x;
        mY = y;
        init();
    }

    /**
     * @return the State of the Block
     */
    public State getState() {
        return mState;
    }

    /**
     * Set the State of the Block
     *
     * @param state
     */
    public void setState(State state) {
        mState = state;
    }

    /**
     * String Representation of a Block
     *
     * @return
     */
    @Override
    public String toString() {
        return "Block{" +
                "mPosition=" + mPosition +
                '}';
    }

    /**
     * Return the Position of the Block relative to it's parent Item
     *
     * @return
     */
    public PointF getPosition() {
        return mPosition;
    }

    private void init() {
        mPaintStroke.setStyle(Paint.Style.STROKE);
        mPaintStroke.setStrokeWidth(2);
        mPaintStroke.setColor(Color.BLACK);
        mPaintStroke.setAntiAlias(true);

        mPaintAliveFill.setStyle(Paint.Style.FILL);
        mPaintAliveFill.setColor(Color.DKGRAY);

        mPaintDeadFill.setStyle(Paint.Style.FILL);
        mPaintDeadFill.setColor(Color.BLACK);
    }

    /**
     * @return the x poisition in Item
     */
    public float getX() {
        return mX;
    }

    /**
     * @param x the x position in Item
     */
    public void setX(float x) {
        mX = x;
        mPosition.x = x;
    }

    /**
     * @return the y position in Item
     */
    public float getY() {
        return mY;
    }

    /**
     * @param y the y position in Item
     */
    public void setY(float y) {
        mY = y;
        mPosition.y = y;
    }

    /**
     * Check if the Block contains the PointF
     *
     * @param pointF
     * @return
     */
    public boolean contains(PointF pointF) {
        if (mX <= pointF.x && pointF.x < mX + 1 &&
                mY <= pointF.y && pointF.y < mY + 1)
            return true;
        else return false;
    }

    /**
     * Method to draw the Block to the Canvas
     *
     * @param canvas    the Canvas
     * @param itemX     the x Position of the parent Item relative to the Grid Coordinate System
     * @param itemY     the y Position of the parent Item relative to the Grid Coordinate System
     * @param blockSize the Size of the Block relative to the Canvas Coordinate System
     */
    public void draw(Canvas canvas, float itemX, float itemY, float blockSize) {
        mBlockSize = blockSize;
        float x = (itemX + mX) * mBlockSize;
        float y = (itemY + mY) * mBlockSize;
        switch (mState) {
            case Alive:
                canvas.drawRect(x, y, x + mBlockSize, y + mBlockSize, mPaintAliveFill);
                break;
            case Dead:
                canvas.drawRect(x, y, x + mBlockSize, y + mBlockSize, mPaintDeadFill);
                break;
        }
        canvas.drawRect(x, y, x + mBlockSize, y + mBlockSize, mPaintStroke);
    }

    /**
     * Enumeration Block's Stats : Alive (initial State), Dead (When the Block in the Item is Touch)
     */
    enum State {
        Alive, Dead;
    }
}
