package fr.wcs.battlegeek.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by adphi on 25/09/17.
 */

public class CreateMapView extends View {

    // Grid and Items Container Definition
    private Grid mGrid;
    private ArrayList<Item> mItems = new ArrayList<>();

    private Item mSelectedItem = null;

    // Painting
    private Paint mPaint = new Paint();

    /**
     * View Constructor
     * @param context
     */
    public CreateMapView(Context context) {
        super(context);
        init();
    }

    /**
     * View Constructor
     * @param context
     * @param attrs
     */
    public CreateMapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * View Constructor
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public CreateMapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * Get the Items
     * @return
     */
    public ArrayList<Item> getItems() {
        return mItems;
    }

    /**
     * Initialisation Method
     * This is where the Blocks of the Item are defined
     */
    private void init() {
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mGrid = new Grid(10);

        Tetromino tetromino1 = new Tetromino(this, mGrid, Tetromino.Shape.I, Tetromino.Colors.LTBLUE);
        tetromino1.setPosition(new PointF(0,0));
        mItems.add(tetromino1);

        Tetromino tetromino2 = new Tetromino(this, mGrid, Tetromino.Shape.T, Tetromino.Colors.PURPLE);
        tetromino2.setPosition(new PointF(2, 1));
        mItems.add(tetromino2);

        Tetromino tetromino3 = new Tetromino(this, mGrid, Tetromino.Shape.Z, Tetromino.Colors.RED);
        tetromino3.setPosition(new PointF(5, 5));
        mItems.add(tetromino3);

        Tetromino tetromino4 = new Tetromino(this, mGrid, Tetromino.Shape.O, Tetromino.Colors.YELLOW);
        tetromino4.setPosition(new PointF(5, 2));
        mItems.add(tetromino4);

        Tetromino tetromino5 = new Tetromino(this, mGrid, Tetromino.Shape.J, Tetromino.Colors.BLUE);
        tetromino5.setPosition(new PointF(6, 7));
        mItems.add(tetromino5);

        Tetromino tetromino6 = new Tetromino(this, mGrid, Tetromino.Shape.L, Tetromino.Colors.ORANGE);
        tetromino6.setPosition(new PointF(1, 5));
        mItems.add(tetromino6);

        Tetromino tetromino7 = new Tetromino(this, mGrid, Tetromino.Shape.S, Tetromino.Colors.GREEN);
        tetromino7.setPosition(new PointF(3, 6));
        mItems.add(tetromino7);


    }

    /**
     * Method that refresh the grid Size on View Size Changed Event
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mGrid.setWidth(w);
        mGrid.setHeight(w);
    }

    /**
     * Method Drawing the View Content : Grid and Items
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mGrid.draw(canvas);
        for (Item item : mItems) {
            item.draw(canvas);
        }
    }

    /**
     * Method Handling Touch Event and transmitting Event to the Items
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        PointF pos = mGrid.mapToGrid(x, y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mSelectedItem = getItem(pos);
                if (mSelectedItem != null) {
                    mSelectedItem.onTouch(this, event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mSelectedItem != null) {
                    mSelectedItem.onTouch(this, event);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mSelectedItem != null) {
                    mSelectedItem.onTouch(this, event);
                    mSelectedItem = null;
                }
                break;
        }
        return true;
    }

    /**
     * Method returning Item at the given PointF
     * @param point
     * @return
     */
    private Item getItem(PointF point) {
        for (Item item : mItems) {
            if (item.contains(point)) return item;
        }
        return null;
    }
}
