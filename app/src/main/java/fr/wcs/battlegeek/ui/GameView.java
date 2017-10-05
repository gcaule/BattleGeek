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

import static fr.wcs.battlegeek.ui.Tetromino.Shape.I;
import static fr.wcs.battlegeek.ui.Tetromino.Shape.J;
import static fr.wcs.battlegeek.ui.Tetromino.Shape.O;
import static fr.wcs.battlegeek.ui.Tetromino.Shape.T;


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
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private Tetromino.Shape getShape(char symbol){
        switch (symbol) {
            case 'T':
                return T;
            case 'Z':
                return Tetromino.Shape.Z;
            case 'S':
                return Tetromino.Shape.S;
            case 'O':
                return O;
            case 'I':
                return I;
            case 'L':
                return Tetromino.Shape.L;
            case 'J':
                return J;
            default:
                return null;
        }
    }

    public void setOnPlayListener(PlayListener listener) {
        this.listener = listener;
    }

}