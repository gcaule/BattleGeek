package fr.wcs.battlegeek.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;

import fr.wcs.battlegeek.model.Bonus;
import fr.wcs.battlegeek.model.Maps;
import fr.wcs.battlegeek.model.Settings;

import static fr.wcs.battlegeek.ui.MapView.Mode.CREATE;
import static fr.wcs.battlegeek.ui.MapView.Mode.PLAY;

/**
 * Created by adphi on 25/09/17.
 */

public class MapView extends View {

    /**
     * The Map View Mode: Creating the Map, or Playing
     */
    public enum Mode {
        PLAY, CREATE
    }

    private String TAG = Settings.TAG;

    private Mode mMode = CREATE;

    // Grid and PlayerModel Container Definition
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
     * Get the PlayerModel
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
        setRandomPositions();
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
     * Method Drawing the View Content : Grid and PlayerModel
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
     * Method Handling Touch Event and transmitting Event to the PlayerModel
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
                    // Preventing it to be drawn under other PlayerModel
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
     * Method to set Play State on the PlayerModel Map
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

    /**
     * Set (Fake) Random Items on Map
     */
    public void setRandomPositions() {
        // Request random Map
        char[][] map = Maps.getMap();
        setMap(map);
    }

    /**
     * Set Map to the View
     * @param map
     */
    public void setMap(char[][] map) {
        // Clear PlayerModel List
        mItems.clear();
        ArrayList<Item> bonus = new ArrayList<>();
        // Store Blocks in a HashMap;
        HashMap<Tetromino.Shape, ArrayList<PointF> > dict = new HashMap<>();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                PointF point = new PointF(j,i);
                char symbol = map[i][j];
                // If Tetromino
                if(Character.isUpperCase(symbol)) {
                    Tetromino.Shape shape = Tetromino.Shape.valueOf(String.valueOf(symbol));
                    if( shape != Tetromino.Shape.NONE) {
                        // If the shape is not in the hashMap keys, we add it
                        if(! dict.containsKey(shape)) {
                            dict.put(shape, new ArrayList<PointF>());
                        }

                        // add the point to the ArrayList corresponding to the Shape
                        dict.get(shape).add(point);
                    }
                }
                // If Bonus
                else if (symbol != ' ') {
                    Bonus.Type type = null;
                    if (symbol == '-') type = Bonus.Type.MOVE;
                    else if (symbol == '=') type = Bonus.Type.REPLAY;
                    else if (symbol == '+') type = Bonus.Type.CROSS_FIRE;
                    Tetromino itemBonus = new Tetromino(this, mGrid, j, i, Tetromino.Shape.NONE, null);
                    TetrominoBonus blockBonus = new TetrominoBonus(0, 0, type);
                    itemBonus.setBlock(blockBonus);
                    bonus.add(itemBonus);
                }
            }
        }

        // Get Through the hashMap to create Tetromino
        for (Tetromino.Shape shape : dict.keySet()) {
            // min x and min y for offset
            int minX = Settings.GRID_SIZE;
            int minY = Settings.GRID_SIZE;

            Tetromino.Colors color = Tetromino.getColorMap().get(shape);
            Tetromino tetromino = new Tetromino(this, mGrid, shape, color);

            // Find minimum min x and min y
            for(PointF point : dict.get(shape)) {
                minX = (int) Math.min(point.x, minX);
                minY = (int) Math.min(point.y, minY);
            }
            // Now, we can add the block with right offset
            for(PointF point : dict.get(shape)) {
                tetromino.setBlock(new TetrominoBlock(point.x - minX, point.y - minY, color));
            }

            tetromino.setPosition(new PointF(minX, minY));
            mItems.add(tetromino);

            mItems.addAll(bonus);
            //Log.d(TAG, "setRandomPositions: " + shape + " : " + dict.get(shape).size());
        }
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
