package fr.wcs.battlegeek.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;

import fr.wcs.battlegeek.R;
import fr.wcs.battlegeek.model.Bonus;
import fr.wcs.battlegeek.model.Settings;

/**
 * Created by adphi on 16/10/17.
 */

public class TetrominoBonus extends TetrominoBlock {
    private final String TAG = Settings.TAG;

    private Bonus.Type mType;

    // Graphics
    private Resources mResources;
    private Paint mPaint = new Paint();
    Bitmap mImageAlive;
    Bitmap mImageDead;

    public TetrominoBonus(PointF position, Bonus.Type type, Resources resources) {
        super(position, null);
        mType = type;
        mResources = resources;
        init();

    }

    public TetrominoBonus(float x, float y, Bonus.Type type, Resources resources) {
        super(x, y, null);
        mType = type;
        mResources = resources;
        init();
    }

    private void init() {
        mImageAlive = BitmapFactory.decodeResource(mResources, R.drawable.tetromino_bonus_alive);
        mImageDead = BitmapFactory.decodeResource(mResources, R.drawable.tetromino_bonus);
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
        Bitmap image = mState == State.ALIVE ? mImageAlive : mImageDead;
        image = Bitmap.createScaledBitmap(image, (int)mBlockSize, (int)mBlockSize, true);
        canvas.drawBitmap(image, x, y, null);


    }

    public Bonus.Type getType() {
        return mType;
    }
}
