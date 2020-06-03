package com.site.myapplication.banner;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.pluginproject.R;

import java.util.List;

//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.viewpager.widget.PagerAdapter;

class FocusPagerAdapter extends PagerAdapter {
    private List<BannerResult.ResultBean> mFocusPagerBannerList = null;
    private Activity mActivity;

    public FocusPagerAdapter(Activity activity, List<BannerResult.ResultBean> focusPagerBannerList) {
        mActivity = activity;
        mFocusPagerBannerList = focusPagerBannerList;
    }

    /* 设置最大大小为真实数据的10倍。防止设置成 Integer.MAX_VALUE 在调用 setCurrentItem 时 ANR 问题 */
    public int getMaxCount() {
        return mFocusPagerBannerList.size() * 10;
    }

    /* 默认设置开始循环滚动位置为中间位置 */
    int getInitItemIndex() {
        return needLoop() ? getMaxCount() / 2 + 1 - 1 : 0;
    }

    /* 向左滑动，设置position结束前，一个数据大小最后一个index为reset的值 */
    boolean isLeftNeedReset(int position) {
        return needLoop() && position <= mFocusPagerBannerList.size() - 1;
    }

    /* 向右滑动，设置position结束前，一个数据大小前一个index为reset的值 */
    boolean isRightNeedReset(int position) {
        return needLoop() && position >= getMaxCount() - mFocusPagerBannerList.size() - 1;
    }

    boolean needLoop() {
        return /*mFocusPagerBannerList.size() > 1*/false;
    }

    int getRealPosition(int position) {
        if (needLoop()) {
            return position == -1 ? mFocusPagerBannerList.size() - 1 : position % mFocusPagerBannerList.size();
        } else {
            return position;
        }
    }

    public int getRealCount() {
        return mFocusPagerBannerList.size();
    }

    @Override
    public int getCount() {
        return needLoop() ? getMaxCount() : mFocusPagerBannerList.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        if (mActivity == null || mActivity.isFinishing()) {
            return "";
        }
        final int realPosition = getRealPosition(position);
        Log.d("FocusViewPager", "instantiateItem position : " + position + ", realPosition : " + realPosition);
        final BannerResult.ResultBean resultBean = mFocusPagerBannerList.get(realPosition);
        ConstraintLayout layout = (ConstraintLayout) LayoutInflater.from(mActivity).inflate(R.layout.banner_home_focus_pager_item, container, false);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mActivity == null || mActivity.isFinishing()) {
                    return;
                }
                Toast.makeText(mActivity, "onClick: " + position, Toast.LENGTH_SHORT).show();
            }
        });
        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        if (object instanceof View) {
            container.removeView((View) object);
        }
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }


}
