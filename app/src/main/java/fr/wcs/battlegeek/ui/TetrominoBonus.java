package fr.wcs.battlegeek.ui;

import android.graphics.Canvas;
import android.graphics.PointF;

import fr.wcs.battlegeek.model.Bonus;
import fr.wcs.battlegeek.model.Settings;

/**
 * Created by adphi on 16/10/17.
 */

public class TetrominoBonus extends TetrominoBlock {
    private final String TAG = Settings.TAG;

    private Bonus.Type mType;

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
                break;
            case REPLAY:
                break;
            case CROSS_FIRE:
                break;
        }
    }

    @Override
    public void draw(Canvas canvas, float itemX, float itemY, float blockSize) {
        super.draw(canvas, itemX, itemY, blockSize);
    }
}
