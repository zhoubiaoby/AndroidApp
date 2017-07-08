package com.example.zhoubiao.cxcourses.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Scroller;


/**
 * Created by zhoubiao on 2015/9/24.
 */
public class MyLayout extends FrameLayout {

    private float mLastMotionX;
    private float totalMoveX;

    private MarginLayoutParams lp ;
    private Scroller mScroller;
    private int mTouchSlop;
    private int mMinimumVelocity;
    private int mMaximumVelocity;
    private VelocityTracker mVelocityTracker;

    private int mScrollX;
    private int mScrollY;
     int screenWidth;
    private Handler handler = new Handler();
    private Runnable r;
    private int times = 1;

    public MyLayout(Context context){
        super(context);
        init(context);
    }

    public MyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    void init(Context context) {
        Log.v("MyLayout--->", "init方法开始");

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        screenWidth  =  wm.getDefaultDisplay().getWidth();//获得屏幕宽度

        mScroller = new Scroller(getContext());
        setFocusable(true);
        setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        setWillNotDraw(false);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

    }

    public void fling(int velocityY) {
        if (getChildCount() > 0) {
            mScroller.fling(getScrollX(), getScrollY(), 0, velocityY, 0, screenWidth, 0,
                     0);
            final boolean movingDown = velocityY > 0;
            awakenScrollBars(mScroller.getDuration());
            invalidate();
        }
    }

    private void obtainVelocityTracker(MotionEvent event) {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    private void releaseVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    @Override
    public void computeScroll() {

        Log.v("Mylayout-->", "computeScroll:" + times++);
        if (mScroller.computeScrollOffset()) {
            int scrollX = getScrollX();
            int scrollY = getScrollY();
            int oldX = scrollX;
            int oldY = scrollY;
            Log.v("Mylayout-->", "getCurrX:" + mScroller.getCurrX());
            Log.v("Mylayout-->", "getCurrY:" + mScroller.getCurrY());
            int x = mScroller.getCurrX();
            int y = mScroller.getCurrY();
            scrollX = x;
            scrollY = y;
            scrollY = scrollY + 10;
            scrollTo(scrollX, scrollY);
            postInvalidate();
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x,y);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.v("MyLayout-onTouch:", event.getAction() + "方法执行");
        Log.v("MyLayout-onTouch:", "X坐标：" + event.getX());
        Log.v("MyLayout-onTouch:", "Y坐标：" + event.getY());
        obtainVelocityTracker(event);
        float x = event.getX();
        float y = event.getY();


        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                lp = (MarginLayoutParams) this.getLayoutParams();
//                if (!mScroller.isFinished()) {
//                    mScroller.abortAnimation();
//                }
              mLastMotionX = x;
                break;
            case MotionEvent.ACTION_MOVE:
             int deltaX = (int)(x - mLastMotionX);
                if(deltaX > 0){
                    Log.v("Mylayout-->", deltaX + "");
                    totalMoveX += deltaX;
                    Log.v("Mylayout-->", totalMoveX + "");
                    if(totalMoveX <= screenWidth) {
//                        this.scrollBy(deltaX, 0);
//                        this.setAlpha(1 - totalMoveX /(2*screenWidth));
//                    this.setScaleX(1 - totalMoveX / (2*screenWidth));
                    Log.v("Mylayouy-->", (1 - totalMoveX / 450) + "");

//                    lp = new RelativeLayout.LayoutParams(this.getMeasuredWidth(),this.getMeasuredHeight());
                    lp.setMargins((int)totalMoveX,0,0,0);
                    this.setLayoutParams(lp);
                    }
                    
                }
                mLastMotionX = x;
                break;
            case MotionEvent.ACTION_UP:
//                final VelocityTracker velocityTracker = mVelocityTracker;
//                velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
//                int initialVelocity = (int) velocityTracker.getYVelocity();
//                Log.v("MyLayout-->","最小fling速度："+mMinimumVelocity);
//                Log.v("MyLayout-->","当前fling速度："+initialVelocity);
//                if ((Math.abs(initialVelocity) > mMinimumVelocity)
//                        && getChildCount() > 0) {
//                    Log.v("MyLayout-->","满足fling速度");
                    if(totalMoveX < screenWidth) {
                        int startMarginRightX = (int)(screenWidth - totalMoveX);
                        Log.v("MyLayout-->", "startScroll()");
                        mScroller.startScroll(0, 0, startMarginRightX, 0, 5000);
                        final int addMarginX =  1;
                          r = new Runnable() {
                            @Override
                            public void run() {
                                if(totalMoveX < screenWidth) {
                                    totalMoveX += addMarginX;
                                    lp.setMargins((int) totalMoveX, 0, 0, 0);
                                    MyLayout.this.setLayoutParams(lp);
                                    handler.postDelayed(r,1);
                                }
                            }
                        };
                        handler.postDelayed(r,0);
//                        while (mScroller.computeScrollOffset()){
//
//                        }
                    }
//                }

//                releaseVelocityTracker();
                break;
        }
        return true;
    }
}
