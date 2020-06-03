package com.site.myapplication.banner;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;

class PagerLoopHandler extends Handler {
    WeakReference<CarouselController> controllerWeakReference;

    PagerLoopHandler(CarouselController carouselController) {
        controllerWeakReference = new WeakReference<>(carouselController);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case CarouselController.MSG_FOCUS_PAGER_LOOP:
                CarouselController controller = controllerWeakReference.get();
                if (controller == null || controller.mFocusViewPager == null || controller.mFocusPagerAdapter == null) {
                    break;
                }
                if (controller.isTouchFocusViewPager) {
                    controller.isTouchFocusViewPager = false;
                }

                int currentItemIndex = controller.mFocusViewPager.getCurrentItem();
                int itemCount = controller.mFocusPagerAdapter.getCount();
                if (currentItemIndex + 1 < itemCount) {
                    controller.mFocusViewPager.setCurrentItem(currentItemIndex + 1, true);
                    Log.d("FocusViewPager", "handleMessage : " + currentItemIndex + 1);
                } else {
                    controller.mFocusViewPager.setCurrentItem(0, false);
                    Log.d("FocusViewPager", "handleMessage 0 : ");
                }

                if (controller.isStartLoop) {
                    controller.mPagerLoopHandler.sendEmptyMessageDelayed(CarouselController.MSG_FOCUS_PAGER_LOOP, 5000);
                }
                break;
            default:
                break;
        }
    }
}
