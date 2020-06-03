package com.site.myapplication.banner;

import android.support.v4.view.ViewPager;
import android.view.View;

//import androidx.viewpager.widget.ViewPager;

public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.85f;
    private static final float MIN_ALPHA = 0.5f;

    @Override
    public void transformPage(View page, float position) {
        int pageWidth = page.getWidth();
        int pageHeight = page.getHeight();
        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
//            page.setAlpha(MIN_ALPHA);
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
        } else if (position <= 1) { // [-1,1]
            // Modify the default slide transition to shrink the page as well
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                page.setTranslationX(horzMargin - vertMargin / 2);
                page.setScaleX(1 + 0.15f * position);
                page.setScaleY(1 + 0.15f * position);
            } else {
                page.setTranslationX(-horzMargin + vertMargin / 2);

                page.setScaleX(1 - 0.15f * position);
                page.setScaleY(1 - 0.15f * position);
            }

            // Scale the page down (between MIN_SCALE and 1)

            // Fade the page relative to its size.
//            page.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            page.setScaleX(MIN_SCALE);
            page.setScaleY(MIN_SCALE);
//            page.setAlpha(MIN_ALPHA);
        }
    }
}
