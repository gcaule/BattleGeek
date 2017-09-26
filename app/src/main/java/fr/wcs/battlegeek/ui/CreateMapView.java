package fr.wcs.battlegeek.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by adphi on 25/09/17.
 */

public class CreateMapView extends View{

    private Grid mGrid;
    private Item mSelectedItem = null;

    public ArrayList<Item> getItems() {
        return mItems;
    }

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

        Item item1 = new Item(this,mGrid, 0, 0);
        item1.setBlock(new Block(0, 0));
        item1.setBlock(new Block(1, 0));
        item1.setBlock(new Block(0, 1));
        item1.setBlock(new Block(1, 1));
        mItems.add(item1);

        Item item2 = new Item(this, mGrid, 2, 2);
        item2.setBlock(new Block(0, 0));
        item2.setBlock(new Block(0, 1));
        item2.setBlock(new Block(1, 1));
        item2.setBlock(new Block(1, 2));
        mItems.add(item2);

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
        for (Item item : mItems) {
            item.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        PointF pos = mGrid.mapToGrid(x, y);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mSelectedItem = getItem(pos);
                if (mSelectedItem != null) {
                    mSelectedItem.onTouch(this, event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mSelectedItem != null) {
                    mSelectedItem.onTouch(this, event);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mSelectedItem != null) {
                    mSelectedItem.onTouch(this, event);
                    mSelectedItem = null;
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
