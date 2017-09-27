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

    /**
     * TetrominoBlock is the smallest element of Tetrominos
     * @param color The color of the Block
     */

    private float mainHSLColorIndex;
    private float[] centerHSLColor;
    private float[] topHSLColor;
    private float[] bottomHSLColor;
    private float[] sidesHSLColor;
    private float mCenterMargin = 15;


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
                mainHSLColorIndex = 60;
                break;
            case ORANGE :
                mainHSLColorIndex = 40;
                break;
            case RED :
                mainHSLColorIndex = 360;
                break;
            case PURPLE:
                mainHSLColorIndex = 290;
                break;
            case GREEN :
                mainHSLColorIndex = 130;
                break;
            case BLUE :
                mainHSLColorIndex = 260;
                break;
            case LTBLUE:
                mainHSLColorIndex = 180;
                break;
        }
        centerHSLColor = new float[] {mainHSLColorIndex, 1f, 0.65f};
        topHSLColor = new float[] {mainHSLColorIndex, 1f, 0.9f};
        bottomHSLColor = new float[] {mainHSLColorIndex, 1f, 0.2f};
        sidesHSLColor = new float[] {mainHSLColorIndex, 1f, 0.45f};
    }

    @Override
    public void draw(Canvas canvas, float itemX, float itemY, float blockSize) {
        this.mBlockSize = blockSize;
        float x = (itemX + mX) * mBlockSize;
        float y = (itemY + mY) * mBlockSize;

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
        mPaint.setColor(ColorUtils.HSLToColor(sidesHSLColor));
        canvas.drawPath(mPath, mPaint);

        // Draw Top Side
        mPath.reset();
        mPath.moveTo(x, y);
        mPath.lineTo(right, y);
        mPath.lineTo(right - mCenterMargin, y + mCenterMargin);
        mPath.lineTo(x + mCenterMargin, y + mCenterMargin);
        mPath.lineTo(x, y);
        mPaint.setColor(ColorUtils.HSLToColor(topHSLColor));
        canvas.drawPath(mPath, mPaint);

        //Draw Bottom Side
        mPath.reset();
        mPath.moveTo(x, bottom);
        mPath.lineTo(right, bottom);
        mPath.lineTo(right - mCenterMargin, bottom - mCenterMargin);
        mPath.lineTo(x + mCenterMargin, bottom - mCenterMargin);
        mPath.lineTo(x, bottom);
        mPaint.setColor(ColorUtils.HSLToColor(bottomHSLColor));
        canvas.drawPath(mPath, mPaint);

        // Draw Center Rect
        mPaint.setColor(ColorUtils.HSLToColor(centerHSLColor));
        canvas.drawRect(x + mCenterMargin, y + mCenterMargin, right - mCenterMargin, bottom - mCenterMargin, mPaint);
    }
}
