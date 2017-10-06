package fr.wcs.battlegeek.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.v4.graphics.ColorUtils;

/**
 * Created by adphi on 27/09/17.
 */

public class TetrominoBlock extends Block {

    private Tetromino.Colors mColor;
    private Paint mPaint = new Paint();
    private Path mPath = new Path();


    private float mMainHSLColorIndex;
    private float[] mCenterHSLColor;
    private float[] mTopHSLColor;
    private float[] mBottomHSLColor;
    private float[] mSidesHSLColor;
    private float mCenterMargin = 15;


    /**
     * TetrominoBlock is the smallest element of Tetrominos
     * @param position
     * @param color
     */
    public TetrominoBlock(PointF position, Tetromino.Colors color) {
        super(position);
        this.setColor(color);
    }

    public TetrominoBlock(float x, float y, Tetromino.Colors color) {
        super(x, y);
        this.setColor(color);
    }

    public void setColor(Tetromino.Colors color) {
        this.mColor = color;
        switch (color) {
            case YELLOW :
                mMainHSLColorIndex = 60;
                break;
            case ORANGE :
                mMainHSLColorIndex = 40;
                break;
            case RED :
                mMainHSLColorIndex = 360;
                break;
            case PURPLE:
                mMainHSLColorIndex = 290;
                break;
            case GREEN :
                mMainHSLColorIndex = 130;
                break;
            case BLUE :
                mMainHSLColorIndex = 260;
                break;
            case LTBLUE:
                mMainHSLColorIndex = 180;
                break;
        }
        mCenterHSLColor = new float[] {mMainHSLColorIndex, 1f, 0.65f};
        mTopHSLColor = new float[] {mMainHSLColorIndex, 1f, 0.7f};
        mBottomHSLColor = new float[] {mMainHSLColorIndex, 1f, 0.2f};
        mSidesHSLColor = new float[] {mMainHSLColorIndex, 1f, 0.45f};
    }

    @Override
    public void setState(State state) {
        super.setState(state);
        if(state == State.DEAD) {
            mCenterHSLColor = new float[] {mMainHSLColorIndex, 1f, 0.1f};
            mTopHSLColor = new float[] {mMainHSLColorIndex, 1f, 0.2f};
            mBottomHSLColor = new float[] {mMainHSLColorIndex, 1f, 0.05f};
            mSidesHSLColor = new float[] {mMainHSLColorIndex, 1f, 0.08f};
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
