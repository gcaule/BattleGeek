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


/**
 * Created by apprenti on 27/09/17.
 */

public class GameView extends View{

    public interface PlayListener {
        public void onPlayListener(int x, int y);
    }

    private PlayListener listener;

    private ArrayList<Block> mBlocks = new ArrayList<>();

    private String TAG = "CustomView";

    private Grid mGrid;
    private int mGridSize = 10;

    private HashMap<Tetromino.Shape, Tetromino.Colors> mColorsMap = new HashMap<>();

    public GameView(Context context) {
        super(context);
        init();
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.listener = null;
        this.mGrid = new Grid(mGridSize);

        mColorsMap.put(Tetromino.Shape.I, Tetromino.Colors.LTBLUE);
        mColorsMap.put(Tetromino.Shape.T, Tetromino.Colors.PURPLE);
        mColorsMap.put(Tetromino.Shape.Z, Tetromino.Colors.RED);
        mColorsMap.put(Tetromino.Shape.O, Tetromino.Colors.YELLOW);
        mColorsMap.put(Tetromino.Shape.J, Tetromino.Colors.BLUE);
        mColorsMap.put(Tetromino.Shape.L, Tetromino.Colors.ORANGE);
        mColorsMap.put(Tetromino.Shape.S, Tetromino.Colors.GREEN);
    }

    public void setPlouf(int x, int y) {
        mBlocks.add(new Block(new PointF(x, y)));
        invalidate();
    }

    public void setTouch(int x, int y, Tetromino.Shape shape) {
        Tetromino.Colors color = mColorsMap.get(shape);
        mBlocks.add(new TetrominoBlock(new PointF(x, y), color));
        invalidate();
    }

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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mGrid.setWidth(w);
        mGrid.setHeight(w);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mGrid.draw(canvas);
        for(Block block : mBlocks){
            block.draw(canvas, 0, 0, mGrid.getCellSize());
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

    public void setOnPlayListener(PlayListener listener) {
        this.listener = listener;
    }

}