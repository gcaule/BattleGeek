package fr.wcs.battlegeek.controller;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by apprenti on 27/09/17.
 */

public class BattleView extends View{

    public interface PlayListener {
        public void onPlayListener(int x, int y);
    }

    private PlayListener listener;
    private char[][] data;

    private String TAG = "CustomView";

    private int mGridSize = 10;

    private float mGridWidth;
    private Paint mPaint = new Paint();

    public BattleView(Context context) {
        super(context);
        init();
    }

    public BattleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BattleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.listener = null;

        data = new char[mGridSize][mGridSize];

        for (int i = 0; i < mGridSize; i++) {
            for (int j = 0; j < mGridSize; j++) {
                data[i][j] = ' ';
            }
        }
    }

    public void setPlouf(int x, int y) {
        Log.e(TAG, "setPlouf: " + x + " " + y);
        data[y][x] = '_';
        invalidate();
    }

    public void setTouch(int x, int y) {
        Log.e(TAG, "setTouch: " + x + " " + y );
        data[y][x] = 'x';
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x, y;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) (event.getX() / mGridWidth);
                y = (int) (event.getY() / mGridWidth);
                if (listener != null && x < mGridSize && y < mGridSize) {
                    listener.onPlayListener(x, y);
                }
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mGridWidth = w / mGridSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setStrokeWidth(2);

        for (int i = 0; i < mGridSize; i++) {
            for (int j = 0; j < mGridSize; j++) {
                if (data[j][i] == 'x') {
                    mPaint.setColor(Color.BLACK);
                    mPaint.setStyle(Paint.Style.FILL);
                }
                else if (data[j][i] == '_') {
                    mPaint.setColor(Color.LTGRAY);
                    mPaint.setStyle(Paint.Style.FILL);
                }
                else {
                    mPaint.setStyle(Paint.Style.STROKE);
                    mPaint.setColor(Color.GRAY);
                }
                canvas.drawRect(mGridWidth * i, mGridWidth * j, (mGridWidth + 1) * i + mGridWidth, (mGridWidth + 1) * j + mGridWidth, mPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "onMeasure: " + widthMeasureSpec + " " + heightMeasureSpec );
    }

    public void setOnPlayListener(PlayListener listener) {
        this.listener = listener;
    }

}