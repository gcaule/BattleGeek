package fr.wcs.battlegeek.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by adphi on 25/09/17.
 */

public class CreateMapView extends View{

    private Grid mGrid;
    private Item mSelectedItem;
    private ArrayList<Item> mItems = new ArrayList<>();
    private Paint mPaint = new Paint();

    public CreateMapView(Context context) {
        super(context);
        init();
    }

    public CreateMapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CreateMapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mGrid = new Grid(8);

        mItems.add(new Item(this,mGrid,  0, 0));
        mItems.add(new Item(this, mGrid, 2, 2));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mGrid.setWidth(w);
        mGrid.setHeight(w);
    }

    @Override implements View.OnTouchListener
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mGrid.draw(canvas);
        for (Item item : mItems) {
            item.draw(canvas);
        }
    }

    private float dx = 0;
    private float dy = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        PointF pos = mGrid.mapToGrid(x, y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.e(TAG, "onTouchEvent: Down");
                if( mGrid.contains(x, y)) {
                    Item item = getItem(pos);
                    Log.e(TAG, "onTouchEvent: Inside Grid");
                    if (item != null) {
                        mSelectedItem = item;
                        PointF itemPos = mSelectedItem.getPosition();
                        dx = itemPos.x - pos.x;
                        dy = itemPos.y - pos.y;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.e(TAG, "onTouchEvent: Move");
                if (mSelectedItem != null) {
                    pos.offset(dx, dy);
                    pos = mGrid.contrainsToGrid(pos);
                    mSelectedItem.setPosition(pos);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mSelectedItem != null) {
                    int gridSize = mGrid.getSize();
                    pos = mGrid.contrainsToGrid(pos);
                    mSelectedItem.setPosition(new PointF((float) (int) pos.x, (float) (int) pos.y));
                    mSelectedItem = null;
                    dx = 0;
                    dy = 0;
                }
                break;
        }
        return true;
    }

    private Item getItem(PointF point) {
        for (Item item : mItems) {
            if (item.contains(point)) return item;
        }
        return null;
    }


}
