package com.site.myapplication.banner;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.example.pluginproject.R;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;

//import androidx.appcompat.app.AppCompatActivity;
//import androidx.collection.LruCache;
//import androidx.viewpager.widget.ViewPager;

public class CarouselController {

    public static final int MSG_FOCUS_PAGER_LOOP = 0x1000;
    /**
     * 轮播指示器选中的宽度
     */
    private static int INDICATOR_WIDTH_SELECTED;
    /**
     * 轮播指示器未选中的宽度
     */
    private static int INDICATOR_WIDTH_NORMAL;

    private RelativeLayout mFocusViewPagerBox;
    //    public ViewPager2 mFocusViewPager;
    public ViewPager mFocusViewPager;
    public FocusPagerAdapter mFocusPagerAdapter;
    private List<BannerResult.ResultBean> mFocusPagerBannerList = new ArrayList<>();
    /**
     * Handler机制实现定时滚动
     */
    public PagerLoopHandler mPagerLoopHandler = new PagerLoopHandler(this);
    public boolean isStartLoop = false;
    private int mCurrentItemIndex;
    private LinearLayout mIndicatorFrame;
    private SparseArray<View> mIndicatorViewMap = new SparseArray<>();
    private View mPreIndicatorView;
    /**
     * 是否触摸ViewPager，如手势左右滑动
     */
    public boolean isTouchFocusViewPager = false;
    private Activity activity;

    private static final int SWITCH_ANIM_TIME = 300;    //300 ms
    private static final int MSG_JUDGE_RESET = 0x1;
    private static final int MSG_SHOW_BLUR = 0x2;

    private ExecutorService mThreadPool;
//    private MyHandler mHandler;
//    private NotifyRunnable mNotifyRunnable;

    //    private ImageView mBlurImage;
    private boolean isSetBuried = false;

    private int mCurDirection = AnimMode.D_RIGHT;
    private int mSwitchAnimTime = SWITCH_ANIM_TIME;
    private float mLastOffset;
    private int mRadius = 50;
    private int mCurPosition;
    private int mRealLastPosition;
    private boolean isSwitching;

    private int realPosition = 0;

    private WeakHashMap<Integer, Bitmap> mBannerMap = new WeakHashMap<>();
    private LruCache<String, Bitmap> blurCache;

    /**
     * 初始化首焦轮播图
     *
     * @param layout view
     */
    @SuppressLint("ClickableViewAccessibility")
    public void initFocusPager(Activity baseActivity, View layout) {
        this.activity = baseActivity;
//        initBlur(layout);
        mFocusViewPagerBox = (RelativeLayout) layout.findViewById(R.id.home_focus_pager_box);
        mFocusViewPagerBox.setVisibility(View.GONE);
        mIndicatorFrame = (LinearLayout) layout.findViewById(R.id.home_focus_dot_frame);
        mFocusViewPager = (ViewPager) layout.findViewById(R.id.home_focus_pager);
        mFocusViewPager.setOverScrollMode(View.OVER_SCROLL_ALWAYS);

        mFocusViewPager.setOffscreenPageLimit(3);
        mFocusViewPager.setPageTransformer(true, new CarouselPagerTransformer(baseActivity));

        mFocusPagerAdapter = new FocusPagerAdapter(activity, mFocusPagerBannerList);

        mFocusViewPager.setAdapter(mFocusPagerAdapter);
        mFocusViewPager.setCurrentItem(mFocusPagerAdapter.getInitItemIndex());

        mFocusViewPagerBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mFocusViewPager.dispatchTouchEvent(event);
            }
        });

        mFocusViewPager.setPageMargin(dip2px(activity, 24));

        mFocusViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(CarouselController.this.activity, "onPageSelected: " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        mFocusViewPager.registerOnPageChangeCallback((new ViewPager2.OnPageChangeCallback() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                Toast.makeText(CarouselController.this.activity, "onPageSelected: " + position, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                super.onPageScrollStateChanged(state);
//            }
//        }));

//        mFocusViewPager.getChildAt(0).setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_MOVE:
//                    case MotionEvent.ACTION_DOWN:
//                        mFocusViewPager.getParent().requestDisallowInterceptTouchEvent(true);
//                        break;
//                    case MotionEvent.ACTION_UP:
//                    case MotionEvent.ACTION_CANCEL:
//                        mFocusViewPager.getParent().requestDisallowInterceptTouchEvent(false);
//                        break;
//                }
//                return false;
//            }
//        });

//        ((RecyclerView)mFocusViewPager.getChildAt(0)).addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
//            @Override
//            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//                return false;
//            }
//
//            @Override
//            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
//
//            }
//
//            @Override
//            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//            }
//        });
    }

    private void initFocusIndicator() {
        if (activity != null) {
            INDICATOR_WIDTH_SELECTED = dip2px(activity, 20);
            INDICATOR_WIDTH_NORMAL = dip2px(activity, 10);
        }
        mIndicatorFrame.removeAllViews();
        for (int i = 0; i < mFocusPagerBannerList.size(); i++) {
            View view = new View(activity);
            mIndicatorViewMap.put(i, view);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (i != 0) {
                params.leftMargin = dip2px(activity, 5);
            }
            mIndicatorFrame.addView(view, params);
            if (i == 0) {
                view.setBackgroundResource(R.drawable.banner_shape_loop_indicator_selected);
                setIndicatorViewWidth(view, INDICATOR_WIDTH_SELECTED);
            } else {
                view.setBackgroundResource(R.drawable.banner_shape_loop_indicator_normal);
                setIndicatorViewWidth(view, INDICATOR_WIDTH_NORMAL);
            }
        }
        mPreIndicatorView = mIndicatorViewMap.get(mFocusPagerAdapter.getRealPosition(mCurrentItemIndex));
    }

    private void checkIndicator(final View checkView) {
        if (mPreIndicatorView == checkView) {
            return;
        }
        int count = mIndicatorFrame.getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = mIndicatorFrame.getChildAt(i);
            if (mPreIndicatorView != null && mPreIndicatorView == childView) {
                ValueAnimator animator = ValueAnimator.ofInt(mPreIndicatorView.getLayoutParams().width, INDICATOR_WIDTH_NORMAL);
                animator.setDuration(200);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        int width = (int) animation.getAnimatedValue();
                        mPreIndicatorView.setBackgroundResource(R.drawable.banner_shape_loop_indicator_normal);
                        setIndicatorViewWidth(mPreIndicatorView, width);
                    }
                });
                animator.start();
            } else {
                childView.setBackgroundResource(R.drawable.banner_shape_loop_indicator_normal);
                setIndicatorViewWidth(childView, INDICATOR_WIDTH_NORMAL);
            }
        }

        if (checkView != null) {
            ValueAnimator animator = ValueAnimator.ofInt(checkView.getLayoutParams().width, INDICATOR_WIDTH_SELECTED);
            animator.setDuration(200);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int width = (int) animation.getAnimatedValue();
                    checkView.setBackgroundResource(R.drawable.banner_shape_loop_indicator_selected);
                    setIndicatorViewWidth(checkView, width);
                }
            });
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mPreIndicatorView = checkView;
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            animator.start();
        }
    }

    private void setIndicatorViewWidth(View view, int width) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = width;
        view.setLayoutParams(params);
    }

    private void startLoopFocusPager(boolean delay) {
        if (isStartLoop || mPagerLoopHandler == null) {
            return;
        }
        isStartLoop = true;
        mPagerLoopHandler.removeMessages(MSG_FOCUS_PAGER_LOOP);
        if (delay) {
            mPagerLoopHandler.sendEmptyMessageDelayed(MSG_FOCUS_PAGER_LOOP, 3000);
        } else {
            mPagerLoopHandler.sendEmptyMessageDelayed(MSG_FOCUS_PAGER_LOOP, 0);
        }
    }

    private void stopLoopFocusPager() {
        if (!isStartLoop || mPagerLoopHandler == null) {
            return;
        }
        isStartLoop = false;
        mPagerLoopHandler.removeMessages(MSG_FOCUS_PAGER_LOOP);
    }


    public void fillDataForBanner(BannerResult result) {
        if (result == null) {
            Log.e("CarouselController", "fillDataForBanner> result == null");
            mFocusViewPagerBox.setVisibility(View.GONE);
            return;
        }
        List<BannerResult.ResultBean> resultBeanList = result.bannerList;
        if (resultBeanList == null || resultBeanList.size() == 0) {
            Log.e("CarouselController", "fillDataForBanner> resultBeanList : " + (resultBeanList == null ? "null" : resultBeanList.size()));
            mFocusViewPagerBox.setVisibility(View.GONE);
            return;
        }

        if (mFocusPagerBannerList.size() == resultBeanList.size()) {
            boolean same = true;
            for (int i = 0; i < mFocusPagerBannerList.size(); i++) {
                if (!mFocusPagerBannerList.get(i).equals(resultBeanList.get(i))) {
                    same = false;
                    break;
                }
            }
            if (same) {
                return;
            }
        }

        mFocusViewPagerBox.setVisibility(View.VISIBLE);
        mFocusPagerBannerList.clear();
        mFocusPagerBannerList.addAll(resultBeanList);
        mFocusPagerAdapter.notifyDataSetChanged();
        mFocusViewPager.setCurrentItem(mFocusPagerAdapter.getInitItemIndex(), false);
        if (mFocusPagerAdapter.needLoop()) {
            startLoopFocusPager(true);
            initFocusIndicator();
        }
    }

//    private void updateNextRes(final int position) {
//        mCurPosition = position;
//        if (mThreadPool != null && !mThreadPool.isShutdown()) {
//            mThreadPool.execute(new Runnable() {
//                @Override
//                public void run() {
//                    synchronized (CarouselController.this) {
//                        if (mBlurImage != null) {
//                            int nextRealPosition = mFocusPagerAdapter.getRealPosition(position + 1);
//                            Bitmap nextBlurBp = blurBitmap(nextRealPosition);
//                            addBitmapToMemoryCache(String.valueOf(nextRealPosition), nextBlurBp);
//                        }
//                        sendMsg();
//                    }
//                }
//            });
//        }
//    }
//
//    private void updateLastRes(final int position) {
//        mCurPosition = position;
//        if (mThreadPool != null && !mThreadPool.isShutdown()) {
//            mThreadPool.execute(new Runnable() {
//                @Override
//                public void run() {
//                    synchronized (CarouselController.this) {
//                        if (mBlurImage != null) {
//                            int lastRealPosition = mFocusPagerAdapter.getRealPosition(position - 1);
//                            Bitmap nextBlurBp = blurBitmap(lastRealPosition);
//                            addBitmapToMemoryCache(String.valueOf(lastRealPosition), nextBlurBp);
//                        }
//                        sendMsg();
//                    }
//                }
//            });
//        }
//    }

//    private void startTrans(ImageView targetImage, BitmapDrawable startBp, BitmapDrawable endBp) {
//        TransitionDrawable td = new TransitionDrawable(new Drawable[]{startBp, endBp});
//        targetImage.setImageDrawable(td);
//        td.startTransition(mSwitchAnimTime);
//    }
//
//    private void switchBgToNext(int lastPosition, final int targetPosition) {
//        if (isSwitching) {
//            return;
//        }
//        isSwitching = true;
//        Bitmap curBlurBp = getBitmapFromMemCache(String.valueOf(lastPosition));
//        Bitmap nextBlurBp = getBitmapFromMemCache(String.valueOf(targetPosition));
//        if (mBlurImage != null) {
//            if (curBlurBp != null && !curBlurBp.isRecycled() && nextBlurBp != null && !nextBlurBp.isRecycled()) {
//                startTrans(mBlurImage, new BitmapDrawable(activity.getResources(), curBlurBp), new BitmapDrawable(activity.getResources(), nextBlurBp));
//            } else if (nextBlurBp != null && !nextBlurBp.isRecycled()) {
//                mBlurImage.setImageDrawable(new BitmapDrawable(activity.getResources(), nextBlurBp));
//            } else {
//                mBlurImage.setImageDrawable(null);
//            }
//        }
//        mNotifyRunnable.setTarget(targetPosition, true);
//        mBlurImage.postDelayed(mNotifyRunnable, mSwitchAnimTime);
//    }
//
//    private void switchBgToLast(int lastPosition, final int targetPosition) {
//        if (isSwitching) {
//            return;
//        }
//        isSwitching = true;
//        Bitmap curBlurBp = getBitmapFromMemCache(String.valueOf(lastPosition));
//        Bitmap lastBlurBp = getBitmapFromMemCache(String.valueOf(targetPosition));
//        if (mBlurImage != null) {
//            if (curBlurBp != null && !curBlurBp.isRecycled() && lastBlurBp != null && !lastBlurBp.isRecycled()) {
//                startTrans(mBlurImage, new BitmapDrawable(activity.getResources(), curBlurBp), new BitmapDrawable(activity.getResources(), lastBlurBp));
//            } else if (lastBlurBp != null && !lastBlurBp.isRecycled()) {
//                mBlurImage.setImageDrawable(new BitmapDrawable(activity.getResources(), lastBlurBp));
//            } else {
//                mBlurImage.setImageDrawable(null);
//            }
//        }
//        mNotifyRunnable.setTarget(targetPosition, false);
//        mBlurImage.postDelayed(mNotifyRunnable, mSwitchAnimTime);
//    }
//
//    private void jumpBgToTarget(int lastPosition, final int targetPosition) {
//        this.mCurPosition = targetPosition;
//        if (isSwitching) {
//            return;
//        }
//        isSwitching = true;
//        Bitmap curBlurBp = getBitmapFromMemCache(String.valueOf(lastPosition));
//        Bitmap targetBlurBp = getBitmapFromMemCache(String.valueOf(targetPosition));
//        if (mBlurImage != null && curBlurBp != null && !curBlurBp.isRecycled() && targetBlurBp != null && !targetBlurBp.isRecycled()) {
//            TransitionDrawable tdb = new TransitionDrawable(new Drawable[]{new BitmapDrawable(activity.getResources(), curBlurBp), new BitmapDrawable(activity.getResources(), targetBlurBp)});
//            mBlurImage.setImageDrawable(tdb);
//            isSetBuried = true;
//            tdb.startTransition(mSwitchAnimTime);
//        }
//        mBlurImage.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (mThreadPool != null && !mThreadPool.isShutdown()) {
//                    mThreadPool.execute(new Runnable() {
//                        @Override
//                        public void run() {
//                            synchronized (CarouselController.this) {
//                                if (mBlurImage != null) {
//                                    Bitmap curBlurBp = blurBitmap(targetPosition);
//                                    Bitmap nextBlurBp = blurBitmap(mFocusPagerAdapter.getRealPosition(targetPosition + 1));
//                                    Bitmap lastBlurBp = blurBitmap(mFocusPagerAdapter.getRealPosition(targetPosition - 1));
//
//                                    addBitmapToMemoryCache(String.valueOf(targetPosition), curBlurBp);
//                                    addBitmapToMemoryCache(String.valueOf(mFocusPagerAdapter.getRealPosition(targetPosition + 1)), nextBlurBp);
//                                    addBitmapToMemoryCache(String.valueOf(mFocusPagerAdapter.getRealPosition(targetPosition - 1)), lastBlurBp);
//                                }
//                                sendMsg();
//                            }
//                        }
//                    });
//                }
//            }
//        }, mSwitchAnimTime);
//    }
//
//    private void sendMsg() {
//        Message msg = new Message();
//        msg.what = MSG_JUDGE_RESET;
//        if (mHandler != null) {
//            mHandler.sendMessage(msg);
//        }
//    }
//
//    private void sendShowMsg() {
//        Message msg = new Message();
//        msg.what = MSG_SHOW_BLUR;
//        if (mHandler != null) {
//            mHandler.sendMessage(msg);
//        }
//    }
//
//
//    private void judgeReset() {
//        isSwitching = false;
//        if (Math.abs(mCurPosition - mRealLastPosition) <= 1) {
//            if (mCurPosition > mRealLastPosition) {
//                switchBgToLast(mCurPosition, mRealLastPosition);
//            } else if (mCurPosition < mRealLastPosition) {
//                switchBgToNext(mCurPosition, mRealLastPosition);
//            }
//        } else {
//            jumpBgToTarget(mCurPosition, mRealLastPosition);
//        }
//    }
//
//    @Nullable
//    private Bitmap blurBitmap(int targetPosition) {
//        Bitmap cachedBlurBp = getBitmapFromMemCache(String.valueOf(targetPosition));//TODO
//        if (cachedBlurBp != null) {
//            return cachedBlurBp;
//        }
//        Bitmap bitmap = mProvider.onProvider(targetPosition);
//        if (bitmap == null || bitmap.isRecycled()) {
//            return null;
//        }
//        Bitmap blurBitmap = blur(bitmap);
//        addBitmapToMemoryCache(String.valueOf(targetPosition), blurBitmap);
//        return blurBitmap;
//    }
//
//    private Bitmap blur(Bitmap source) {
//        int width = source.getWidth() / 2;
//        int height = source.getHeight() / 2;
//        if (width == 0 || height == 0) {
//            return null;
//        }
//        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);
//        Paint paint = new Paint();
//        paint.setFlags(Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG);
//        PorterDuffColorFilter filter = new PorterDuffColorFilter(0xa0000000, PorterDuff.Mode.SRC_ATOP);
//        paint.setColorFilter(filter);
//        canvas.drawBitmap(source, null, new Rect(0, 0, width, height), paint);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2
//                && (mRadius > 0 && mRadius <= 25)) {
//            try {
//                bitmap = RSBlur.blur(activity, bitmap, mRadius);
//            } catch (RSRuntimeException e) {
//                bitmap = FastBlur.blur(bitmap, mRadius, true);
//            }
//        } else {
//            bitmap = FastBlur.blur(bitmap, mRadius, true);
//        }
//        return bitmap;
//    }
//
//    public void setSwitchAnimTime(int switchAnimTime) {
//        mSwitchAnimTime = switchAnimTime;
//    }

    public void destroy() {
        stopLoopFocusPager();
        mFocusPagerBannerList.clear();
        if (mFocusPagerAdapter != null) {
            mFocusPagerAdapter.notifyDataSetChanged();
        }
        isSetBuried = false;
//        if (mBlurImage != null && mNotifyRunnable != null) {
//            mBlurImage.removeCallbacks(mNotifyRunnable);
//        }
//        if (mHandler != null) {
//            mHandler.removeCallbacksAndMessages(null);
//            mHandler = null;
//        }
        shutdownPool();
        if (blurCache != null) {
            blurCache.evictAll();
        }
    }

    private void shutdownPool() {
        if (mThreadPool != null) {
            if (!mThreadPool.isShutdown()) {
                mThreadPool.shutdown();
            }
            mThreadPool = null;
        }
    }

//    private class NotifyRunnable implements Runnable {
//
//        private int targetPosition;
//        private boolean isNext;
//
//        @Override
//        public void run() {
//            if (isNext) {
//                updateNextRes(targetPosition);
//            } else {
//                updateLastRes(targetPosition);
//            }
//        }
//
//        void setTarget(int targetPosition, boolean isNext) {
//            this.targetPosition = targetPosition;
//            this.isNext = isNext;
//        }
//    }
//
//    private static class MyHandler extends Handler {
//
//        WeakReference<CarouselController> controllerWeakReference;
//
//        MyHandler(CarouselController controller) {
//            controllerWeakReference = new WeakReference<>(controller);
//        }
//
//        @Override
//        public void handleMessage(Message msg) {
//            CarouselController controller = controllerWeakReference.get();
//            if (controller == null) {
//                return;
//            }
//            switch (msg.what) {
//                case MSG_JUDGE_RESET:
//                    controller.judgeReset();
//                    break;
//                case MSG_SHOW_BLUR:
//                    controller.showBlur();
//                    break;
//            }
//        }
//    }
//
//    private void showBlur() {
//        if (isSwitching) {
//            return;
//        }
//        mCurPosition = CarouselController.this.realPosition;
//        if (mBlurImage != null) {
//            Bitmap curBlurBp = getBitmapFromMemCache(String.valueOf(realPosition));//TODO
//            if (curBlurBp != null) {
//                mBlurImage.setImageBitmap(curBlurBp);
//            }
//        }
//        isSetBuried = true;
//    }

    private ImageProvider mProvider = new ImageProvider() {
        @Override
        public Bitmap onProvider(int position) {
            Bitmap bitmap = mBannerMap.get(position);
            if (bitmap == null || bitmap.isRecycled()) {
                return null;
            }
            Log.d("FocusViewPager", "blur onProvider : " + position);
            return bitmap;
        }
    };

    public interface AnimMode {

        int D_LEFT = -1;
        int D_RIGHT = 1;

        void transformPage(ImageView ivBg, float position, int direction);
    }

    public interface ImageProvider {

        Bitmap onProvider(int position);
    }

//    private void initBlur(View carouselLayout) {
//        mThreadPool = Executors.newCachedThreadPool();
//        mNotifyRunnable = new NotifyRunnable();
//        mHandler = new MyHandler(this);
//        mBlurImage = carouselLayout.findViewById(R.id.home_banner_blur);
//        initCache();
//    }
//
//    /**
//     * 模糊图缓存
//     */
//    private void initCache() {
//        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
//        final int cacheSize = maxMemory / 8;
//        blurCache = new LruCache<String, Bitmap>(cacheSize) {
//            @Override
//            protected int sizeOf(String key, Bitmap bitmap) {
//                return bitmap != null ? bitmap.getByteCount() / 1024 : 0;
//            }
//        };
//    }
//
//    private void addBitmapToMemoryCache(String key, Bitmap bitmap) {
//        if (blurCache == null || bitmap == null) {
//            return;
//        }
//        if (getBitmapFromMemCache(key) == null) {
//            blurCache.put(key, bitmap);
//        }
//    }
//
//    private Bitmap getBitmapFromMemCache(String key) {
//        return blurCache.get(key);
//    }

    public void pause() {
        if (mFocusPagerAdapter != null && mFocusPagerAdapter.needLoop()) {
            stopLoopFocusPager();
        }
    }

    public void resume() {
        if (mFocusPagerAdapter != null && mFocusPagerAdapter.needLoop()) {
            startLoopFocusPager(true);
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
