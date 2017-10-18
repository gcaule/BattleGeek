package fr.wcs.battlegeek.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import fr.wcs.battlegeek.model.Bonus;
import fr.wcs.battlegeek.model.Settings;

/**
 * Created by adphi on 16/10/17.
 */

public class TetrominoBonus extends TetrominoBlock {
    private final String TAG = Settings.TAG;

    private Bonus.Type mType;
    private String symbol = "";

    // Graphics
    private Paint mPaint = new Paint();

    public TetrominoBonus(PointF position, Bonus.Type type) {
        super(position, null);
        mType = type;
        init();
    }

    public TetrominoBonus(float x, float y, Bonus.Type type) {
        super(x, y, null);
        mType = type;
        init();
    }

    private void init() {
        switch(mType) {
            case MOVE:
                symbol = "\u003c\u003E";
                break;
            case REPLAY:
                symbol = "\u27F3";
                break;
            case CROSS_FIRE:
                symbol = "+";
                break;
        }

        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(4);
    }

    @Override
    public void draw(Canvas canvas, float itemX, float itemY, float blockSize) {
        super.draw(canvas, itemX, itemY, blockSize);
        float x = (Math.round(itemX) + mX) * mBlockSize;
        float y = (Math.round(itemY) + mY) * mBlockSize;
        float centerMargin = this.mBlockSize * 15 / 100;

        float xPos = x + mBlockSize / 2 - (int)(mPaint.measureText(symbol)/2);
        float yPos = (int) (y + mBlockSize / 2 - ((mPaint.descent() + mPaint.ascent()) / 2)) ;

        mPaint.setTextSize(mBlockSize - centerMargin - 10);
        canvas.drawText(symbol, xPos, yPos, mPaint);
    }

    public Bonus.Type getType() {
        return mType;
    }
}
