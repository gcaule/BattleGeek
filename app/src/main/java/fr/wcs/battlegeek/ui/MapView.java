package fr.wcs.battlegeek.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import fr.wcs.battlegeek.Model.Settings;

import static fr.wcs.battlegeek.ui.MapView.Mode.CREATE;
import static fr.wcs.battlegeek.ui.MapView.Mode.PLAY;
import static fr.wcs.battlegeek.ui.Tetromino.Shape.I;
import static fr.wcs.battlegeek.ui.Tetromino.Shape.J;
import static fr.wcs.battlegeek.ui.Tetromino.Shape.S;

/**
 * Created by adphi on 25/09/17.
 */

public class MapView extends View {

    public enum Mode {
        PLAY, CREATE
    }

    private String TAG = "MapView";

    private Mode mMode = CREATE;

    // Grid and Items Container Definition
    private Grid mGrid;
    private ArrayList<Item> mItems = new ArrayList<>();

    private Item mSelectedItem = null;

    /**
     * View Constructor
     * @param context
     */
    public MapView(Context context) {
        super(context);
        init();
    }

    /**
     * View Constructor
     * @param context
     * @param attrs
     */
    public MapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * View Constructor
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public MapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setMode(Mode mode) {
        mMode = mode;
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
        mGrid = new Grid(Settings.GRID_SIZE);

        Tetromino tetromino1 = new Tetromino(this, mGrid, I, Tetromino.Colors.LTBLUE);
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

        Tetromino tetromino5 = new Tetromino(this, mGrid, J, Tetromino.Colors.BLUE);
        tetromino5.setPosition(new PointF(6, 7));
        mItems.add(tetromino5);

        Tetromino tetromino6 = new Tetromino(this, mGrid, Tetromino.Shape.L, Tetromino.Colors.ORANGE);
        tetromino6.setPosition(new PointF(1, 5));
        mItems.add(tetromino6);

        Tetromino tetromino7 = new Tetromino(this, mGrid, S, Tetromino.Colors.GREEN);
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

        if(mMode == PLAY) {
            return false;
        }

        float x = event.getX();
        float y = event.getY();
        PointF pos = mGrid.mapToGrid(x, y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mSelectedItem = getItem(pos);
                if (mSelectedItem != null) {
                    // Move Selected Item at the end of the Item's List
                    // Preventing it to be drawn under other Items
                    mItems.remove(mSelectedItem);
                    mItems.add(mSelectedItem);

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

    /**
     * Method to dump edited map to map Model
     * @return the map model
     */
    public char[][] getMapData(){
        char[][] mapData = new char[10][10];
        for (int row = 0; row < mapData.length; row++) {
            for (int column = 0; column < mapData[row].length; column++) {
                mapData[row][column] = ' ';
            }
        }
        for(Item item : mItems) {
            for(Block block : item.getBlocks()){
                int x = (int)(block.getX() + item.getX());
                int y = (int)(block.getY() + item.getY());
                if(item instanceof Tetromino){
                    Tetromino tetromino = (Tetromino) item;
                    mapData[y][x] = tetromino.getShape().toString().charAt(0);
                }
                else {
                    mapData[y][x] = 'X';
                }
            }
        }
        return mapData;
    }

    /**
     * Method to set Play State on the Player Map
     * @param x
     * @param y
     */
    public void setDead(int x, int y) {
        Item item = getItem(new PointF(x, y));
        Block block = item.getBlock(x,y);
        block.setState(Block.State.DEAD);
        invalidate();
    }

    public void setPlouf(int x, int y) {
        Item item = new Item(this, mGrid, x, y);
        item.setBlock(new Block(0,0));
        mItems.add(item);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int desiredWidth = 100;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int width;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        setMeasuredDimension(width, width);
    }
}
