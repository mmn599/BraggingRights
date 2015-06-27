package io.normyle.ui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.View;

import io.matthew.braggingrights.R;

/**
 * Created by matthew on 5/27/15.
 */
public class GoalTypeView extends View {

    public static int DEFAULT_SIZE = 63;

    Paint mCirclePaint;
    Paint mSelectedPaint;
    int mWidth;
    int mHeight;
    int mColor;
    int mSize = 63;
    int mImageResource;
    boolean mSelected;
    Bitmap mImage;

    public GoalTypeView(Context context) {
        this(context,null);
    }

    public GoalTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if(attrs!=null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.GoalTypeView,
                    0, 0);

            try {
                mImageResource = a.getResourceId(R.styleable.GoalTypeView_imageResource, -1);
                if (mImageResource != -1) {
                    Resources res = getResources();
                    mImage = BitmapFactory.decodeResource(res, mImageResource);
                }
                mColor = a.getColor(R.styleable.GoalTypeView_cc, Color.BLUE);
            } finally {
                a.recycle();
            }
        }

        init();
    }

    public GoalTypeView(Context context, AttributeSet attrs, int color) {
        this(context, attrs);
        mColor = color;
    }

    public void setColor(int color) {
        mColor = color;
        mCirclePaint.setColor(mColor);
        invalidate();
    }

    public void setImageResource(int id) {
        Resources res = getResources();
        mImageResource = id;
        mImage = BitmapFactory.decodeResource(res, mImageResource);
        invalidate();
    }

    private void init() {
        mSelected = false;
        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(mColor);
        mSelectedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectedPaint.setStyle(Paint.Style.STROKE);
        mSelectedPaint.setColor(getResources().getColor(R.color.accent));
        mSelectedPaint.setStrokeWidth(6);
    }

    public void setSize(int size) {
        mSize = size;
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        mSelected = selected;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mWidth / 2, mHeight / 2, 60
                , mCirclePaint);
        if(mImage!=null) {
            canvas.drawBitmap(mImage, mWidth/2 - mImage.getWidth()/2,
                    mHeight/2 - mImage.getHeight()/2, null);
        }
        if(mSelected) {
            canvas.drawCircle(mWidth / 2, mHeight / 2, mSize, mSelectedPaint);
        }
    }

    public int getColor() {
        return mColor;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mSize*2+5, mSize*2+5);
    }
}
