package com.example.zhoubiao.cxcourses.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.zhoubiao.cxcourses.R;

/**
 * Created by zhoubiao on 2015/9/18.
 */
public class DotView extends LinearLayout implements View.OnClickListener{

    public interface OnDotClickHandler {
        void onDotClick(int index);
    }
    private float mDotRadius = 6f;
    private int mDotSpan = 36;
    private int mLittleDotSize;

    private int mCurrent;
    private int mTotal= 0;

    private int mSelectedColor = 0x7FFFFFFF;
    private int mUnSelectedColor = 0x42bd41;

    private OnDotClickHandler mOnDotClickHandler;
    public DotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setGravity(HORIZONTAL);
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.DotView,0,0);
        if(attr != null){
            if(attr.hasValue(R.styleable.DotView_dot_radius)){
                   mDotRadius = attr.getDimension(R.styleable.DotView_dot_radius,mDotRadius);
            }
            if(attr != null){
                  mDotSpan = (int)attr.getDimension(R.styleable.DotView_dot_span,mDotSpan);
            }
            mSelectedColor = attr.getColor(R.styleable.DotView_dot_selected_color,mSelectedColor);
            mUnSelectedColor = attr.getColor(R.styleable.DotView_dot_unselected_color,mUnSelectedColor);

        }
        mLittleDotSize =(int) (mDotSpan/2 + mDotRadius*2);

    }

    public final void setNum(int num) {
        if (num < 0)
            return;
        mTotal = num;

        removeAllViews();
        setOrientation(HORIZONTAL);
        for (int i = 0; i < num; i++) {
            LittleDotView dot = new LittleDotView(getContext(), i);
            if (i == 0) {
                dot.setColor(mSelectedColor);
            } else {
                dot.setColor(mUnSelectedColor);
            }
            dot.setLayoutParams(new LayoutParams(mLittleDotSize, (int) mDotRadius * 2, 1));
            dot.setClickable(true);
            dot.setOnClickListener(this);
            addView(dot);
        }
    }

    public int getTotal() {
        return mTotal;
    }

    public int getCurrentIndex() {
        return mCurrent;
    }

    public void setOnDotClickHandler(OnDotClickHandler handler) {
        mOnDotClickHandler = handler;
    }

    @Override
    public void onClick(View v) {

        if (v instanceof LittleDotView && null != mOnDotClickHandler) {
            mOnDotClickHandler.onDotClick(((LittleDotView) v).getIndex());
        }
    }

    public final void setSelected(int index) {
        if (index < 0 || mCurrent == index || getChildCount() == 0) {
            return;
        }
        if (index >= getChildCount()) {
            index = index % getChildCount();
        }
        ((LittleDotView) getChildAt(mCurrent)).setColor(mUnSelectedColor);
        ((LittleDotView) getChildAt(index)).setColor(mSelectedColor);
        mCurrent = index;
    }
    private class LittleDotView extends View{
        private int mColor;
        private Paint mPaint;
        private int mIndex;

        public LittleDotView(Context context, int index) {
            super(context);
            mPaint = new Paint();
            mPaint.setAntiAlias(true);
            mIndex = index;
        }

    public int getIndex() {
        return mIndex;
    }

    public void setColor(int color) {
        if (color == mColor)
            return;
        mColor = color;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mColor);
        canvas.drawCircle(mLittleDotSize / 2, mDotRadius, mDotRadius, mPaint);
    }
  }

}
