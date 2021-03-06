package fr.wcs.battlegeek.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v4.graphics.ColorUtils;

/**
 * Created by adphi on 27/09/17.
 */

public class TetrominoBlock extends Block {

    private Tetromino.Colors mColor;
    private Paint mPaint = new Paint();
    private Path mPath = new Path();


    protected float mMainHSLColorIndex;
    protected float[] mCenterHSLColor;
    protected float[] mTopHSLColor;
    protected float[] mBottomHSLColor;
    protected float[] mSidesHSLColor;
    private float mCenterMargin = 15;


    /**
     * TetrominoBlock is the smallest element of Tetrominos
     * @param position
     * @param color
     */
    public TetrominoBlock(PointF position, @Nullable Tetromino.Colors color) {
        super(position);
        this.setColor(color);
    }

    public TetrominoBlock(float x, float y, @Nullable Tetromino.Colors color) {
        super(x, y);
        this.setColor(color);
    }

    /**
     * method setting the Color of the Tetromino Block
     * @param color
     */
    public void setColor(Tetromino.Colors color) {
        this.mColor = color;
        float[] hsl = new float[3];
        if (color != null) {
            switch (color) {
                case YELLOW:
                    ColorUtils.colorToHSL(Color.YELLOW, hsl);
                    //mMainHSLColorIndex = 60;
                    break;
                case ORANGE:
                    hsl[0] = 40;
                    break;
                case RED:
                    ColorUtils.colorToHSL(Color.RED, hsl);
                    //mMainHSLColorIndex = 360;
                    break;
                case PURPLE:
                    ColorUtils.colorToHSL(Color.MAGENTA, hsl);
                    mMainHSLColorIndex = 290;
                    break;
                case GREEN:
                    ColorUtils.colorToHSL(Color.GREEN, hsl);
                    //mMainHSLColorIndex = 130;
                    break;
                case BLUE:
                    ColorUtils.colorToHSL(Color.BLUE, hsl);
                    //mMainHSLColorIndex = 260;
                    break;
                case LTBLUE:
                    ColorUtils.colorToHSL(Color.CYAN, hsl);
                    //mMainHSLColorIndex = 180;
                    break;
            }
            mMainHSLColorIndex = hsl[0];
            mCenterHSLColor = new float[] {mMainHSLColorIndex, 1f, 0.5f};
            mTopHSLColor = new float[] {mMainHSLColorIndex, 1f, 0.7f};
            mBottomHSLColor = new float[] {mMainHSLColorIndex, 1f, 0.2f};
            mSidesHSLColor = new float[] {mMainHSLColorIndex, 1f, 0.45f};
        }
        // BONUS
        else {
            mCenterHSLColor = new float[] {0f, 0f, 0.5f};
            mTopHSLColor = new float[] {0f, 0f, 0.7f};
            mBottomHSLColor = new float[] {0f, 0f, 0.2f};
            mSidesHSLColor = new float[] {0f, 0f, 0.45f};
        }
    }

    /**
     * Method setting the State of the Tetromino Block: DEAD or ALIVE
     * @param state
     */
    @Override
    public void setState(State state) {
        super.setState(state);
        if(state == State.DEAD) {
            if(mColor != null) {
                mCenterHSLColor = new float[]{mMainHSLColorIndex, 1f, 0.1f};
                mTopHSLColor = new float[]{mMainHSLColorIndex, 1f, 0.2f};
                mBottomHSLColor = new float[]{mMainHSLColorIndex, 1f, 0.05f};
                mSidesHSLColor = new float[]{mMainHSLColorIndex, 1f, 0.08f};
            }
            // BONUS
            else {
                mCenterHSLColor = new float[]{mMainHSLColorIndex, 0f, 0.1f};
                mTopHSLColor = new float[]{mMainHSLColorIndex, 0f, 0.2f};
                mBottomHSLColor = new float[]{mMainHSLColorIndex, 0f, 0.05f};
                mSidesHSLColor = new float[]{mMainHSLColorIndex, 0f, 0.08f};
            }
        }
    }

    @Override
    public void draw(Canvas canvas, float itemX, float itemY, float blockSize) {
        this.mBlockSize = blockSize;
        float x = (itemX + mX) * mBlockSize;
        float y = (itemY + mY) * mBlockSize;
        mCenterMargin = this.mBlockSize * 15 / 100;

        float right = x + this.mBlockSize;
        float bottom = y + this.mBlockSize;
        // Draw Left Side
        mPath.reset();
        mPath.moveTo(x, y);
        mPath.lineTo(x, bottom);
        mPath.lineTo(x + mCenterMargin, bottom - mCenterMargin);
        mPath.lineTo(x + mCenterMargin, y + mCenterMargin);
        mPath.lineTo(x, y);

        // Draw Right Side
        mPath.moveTo(right, y);
        mPath.lineTo(right, bottom);
        mPath.lineTo(right - mCenterMargin, bottom - mCenterMargin);
        mPath.lineTo(right - mCenterMargin, y + mCenterMargin);
        mPaint.setColor(ColorUtils.HSLToColor(mSidesHSLColor));
        canvas.drawPath(mPath, mPaint);

        // Draw Top Side
        mPath.reset();
        mPath.moveTo(x, y);
        mPath.lineTo(right, y);
        mPath.lineTo(right - mCenterMargin, y + mCenterMargin);
        mPath.lineTo(x + mCenterMargin, y + mCenterMargin);
        mPath.lineTo(x, y);
        mPaint.setColor(ColorUtils.HSLToColor(mTopHSLColor));
        canvas.drawPath(mPath, mPaint);

        //Draw Bottom Side
        mPath.reset();
        mPath.moveTo(x, bottom);
        mPath.lineTo(right, bottom);
        mPath.lineTo(right - mCenterMargin, bottom - mCenterMargin);
        mPath.lineTo(x + mCenterMargin, bottom - mCenterMargin);
        mPath.lineTo(x, bottom);
        mPaint.setColor(ColorUtils.HSLToColor(mBottomHSLColor));
        canvas.drawPath(mPath, mPaint);

        // Draw Center Rect
        mPaint.setColor(ColorUtils.HSLToColor(mCenterHSLColor));
        canvas.drawRect(x + mCenterMargin, y + mCenterMargin, right - mCenterMargin, bottom - mCenterMargin, mPaint);
    }
}
