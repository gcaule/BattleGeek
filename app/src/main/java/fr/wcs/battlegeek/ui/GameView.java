package fr.wcs.battlegeek.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import fr.wcs.battlegeek.model.Bonus;
import fr.wcs.battlegeek.model.Settings;


/**
 * Created by apprenti on 27/09/17.
 */

/**
 * The Game view is the View containing The Player Game's Map
 */
public class GameView extends View{
    private String TAG = Settings.TAG;

    /**
     * Player Played Listener
     * Called when the Player Played
     */
    public interface PlayListener {
        void onPlayListener(int x, int y);
    }

    private PlayListener listener;

    // The Blocks to be drawn on the Grid
    private ArrayList<Block> mBlocks = new ArrayList<>();

    private Grid mGrid;
    private int mGridSize = Settings.GRID_SIZE;

    private boolean mRandomColor = false;


    /**
     * Default Constructor of an View
     * @param context
     */
    public GameView(Context context) {
        super(context);
        init();
    }

    /**
     * Default constructor of any View
     * @param context
     * @param attrs
     */
    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Default constructor of any View
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * private method initialing the GameView Object called in all the different constructors
     */
    private void init() {
        this.listener = null;
        this.mGrid = new Grid(mGridSize);
    }

    public void setRandomColor(boolean random) {
        this.mRandomColor = random;
    }

    /**
     * Method setting MISSED on the Grid
     * @param x the x coordinate in the Grid
     * @param y the y coordinate in the Grid
     */
    public void setPlouf(int x, int y) {
        mBlocks.add(new Block(new PointF(x, y)));
        invalidate();
    }

    /**
     * Method setting TOUCHED on the Grid
     * @param x the x coordinate in the Grid
     * @param y the y coordinate in the Grid
     * @param shape the Shape of the Tetromino
     */
    public void setTouch(int x, int y, Tetromino.Shape shape) {
        Tetromino.Colors color = mRandomColor ? Tetromino.getRandomColorMap().get(shape)
                : Tetromino.getColorMap().get(shape);
        mBlocks.add(new TetrominoBlock(new PointF(x, y), color));
        invalidate();
    }

    public void setBonus(int x, int y, Bonus.Type bonusType) {
        mBlocks.add(new TetrominoBonus(new PointF(x, y), bonusType, getResources()));
        invalidate();
    }

    public void setDead(Bonus.Type bonusType) {
        for(Block block : mBlocks) {
            if(block instanceof TetrominoBonus) {
                TetrominoBonus bonusBlock = (TetrominoBonus) block;
                if(bonusBlock.getType() == bonusType) {
                    bonusBlock.setState(Block.State.DEAD);
                }
            }
        }
        invalidate();
    }

    /**
     * Method handling Player's Touched Events
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                PointF screenPos = new PointF(event.getX(), event.getY());
                PointF pos = mGrid.mapToGrid(screenPos);
                if (listener != null && mGrid.contains(screenPos)) {
                    listener.onPlayListener((int)pos.x, (int)pos.y);
                }
                break;
        }
        return true;
    }

    /**
     * Method Called when the View is resized
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
     * Method called when the View needs to be re-drawn
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mGrid.draw(canvas);
        for(Block block : mBlocks){
            block.draw(canvas, 0, 0, mGrid.getCellSize());
        }
    }

    /**
     * Method called when the layout give/ask size
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
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

        setMeasuredDimension(width - 8, width - 8);
    }

    /**
     * Method setting the OnPlayListener
     * @param listener
     */
    public void setOnPlayListener(PlayListener listener) {
        this.listener = listener;
    }
}