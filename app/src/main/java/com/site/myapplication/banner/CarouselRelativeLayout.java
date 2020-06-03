package com.site.myapplication.banner;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.RelativeLayout;

public class CarouselRelativeLayout extends RelativeLayout {

    private static final String TAG = "CarouselRelativeLayout";
    private int mTouchSlop;
    private boolean needTry = false;

    private float mInitialX = -1f;
    private float mInitialY = -1f;

    public CarouselRelativeLayout(Context context) {
        super(context);
        init(context);
    }

    public CarouselRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CarouselRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public CarouselRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledPagingTouchSlop();
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mInitialX = ev.getX();
//                mInitialY = ev.getY();
//                needTry = true;
//                Log.d(TAG, "request 0 down mInitialX: " + mInitialX + " , mInitialY: " + mInitialY);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (mInitialX != -1f && mInitialY != -1f
//                        && needTry && getParent() != null) {
//                    float h = ev.getX() - mInitialX;
//                    float v = ev.getY() - mInitialY;
//                    if (Math.abs(h) > Math.abs(v)) {
//                        if (Math.abs(h) < mTouchSlop) {
//                            getParent().requestDisallowInterceptTouchEvent(true);
//                            needTry = true;
//                            Log.d(TAG, "request 1 2 h: " + h + " , v: " + v + " ,  mTouchSlop: " + mTouchSlop);
//                        } else {
//                            getParent().requestDisallowInterceptTouchEvent(true);
//                            needTry = false;
//                            Log.d(TAG, "request 1 2 h: " + h + " , v: " + v + " ,  mTouchSlop: " + mTouchSlop);
//                        }
//                    } else {
//                        if (Math.abs(v) < mTouchSlop) {
//                            getParent().requestDisallowInterceptTouchEvent(true);
//                            needTry = true;
//                            Log.d(TAG, "request 2 h: " + h + " , v: " + v + " ,  mTouchSlop: " + mTouchSlop);
//                        } else {
//                            getParent().requestDisallowInterceptTouchEvent(false);
//                            needTry = false;
//                            Log.d(TAG, "request 3 h: " + h + " , v: " + v + " ,  mTouchSlop: " + mTouchSlop);
//                        }
//                    }
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                mInitialX = -1f;
//                mInitialY = -1f;
//                if (getParent() != null) {
//                    getParent().requestDisallowInterceptTouchEvent(false);
//                }
//                needTry = false;
//                break;
//        }
//        return super.onInterceptTouchEvent(ev);
//    }


}
