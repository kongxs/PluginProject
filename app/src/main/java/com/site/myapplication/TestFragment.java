package com.site.myapplication;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pluginproject.R;
import com.google.gson.Gson;
import com.site.myapplication.banner.BannerResult;
import com.site.myapplication.banner.CarouselController;

public class TestFragment extends Fragment {
    private static final String VIEW_TYPE = "view_type";

    public static final int VIEW_TYPE_CAROUSEL = 1;
    public static final int VIEW_TYPE_0 = 0;
    public static final int VIEW_TYPE_2 = 2;

    public static TestFragment newInstance(int viewType) {
        Bundle args = new Bundle();
        args.putInt(VIEW_TYPE, viewType);
        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private int viewType;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            viewType = getArguments().getInt(VIEW_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup inflateView = null;
        if (viewType == VIEW_TYPE_CAROUSEL) {
            inflateView = (ViewGroup) inflater.inflate(R.layout.viewpager2_vertical_item_1, container, false);
            handleBanner(inflateView);
        } else if (viewType == VIEW_TYPE_0) {
            inflateView = (ViewGroup) inflater.inflate(R.layout.viewpager2_vertical_item_0, container, false);
        } else if (viewType == VIEW_TYPE_2) {
            inflateView = (ViewGroup) inflater.inflate(R.layout.viewpager2_vertical_item_2, container, false);
        }
        return inflateView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String content = "";
        if (viewType == VIEW_TYPE_CAROUSEL) {
            content = "竖向 CAROUSEL";
            view.findViewById(R.id.arrow_up).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() != null && getActivity() instanceof MainActivity) {
                        VerticalViewPager pager = ((MainActivity) getActivity()).mVerticalViewPager;
                        if (pager != null) {
                            pager.setCurrentItem(0, true, true);
                        }
                    }
                }
            });
            view.findViewById(R.id.arrow_down).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VerticalViewPager pager = ((MainActivity) getActivity()).mVerticalViewPager;
                    if (pager != null) {
                        pager.setCurrentItem(2, true, true);
                    }
                }
            });
        } else if (viewType == VIEW_TYPE_0) {
            content = "竖向 顶部页面";
        } else if (viewType == VIEW_TYPE_2) {
            content = "竖向 底部页面";
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.i("TestFragment", "setUserVisibleHint : " + isVisibleToUser + " , viewType: " + viewType);
    }

    private void handleBanner(ViewGroup viewGroup) {
        final View carouselLayout = viewGroup.findViewById(R.id.home_banner_root);
        CarouselController carouselController = new CarouselController();
        carouselController.initFocusPager(this.getActivity(), carouselLayout);
        BannerResult bannerResult = new Gson().fromJson(
                "{\n" +
                        "    \"bannerList\":[\n" +
                        "        {\n" +
                        "            \"transferUrl\":\"https://sale.jd.com/act/qQIdxFDe8hKX.html\",\n" +
                        "            \"positionKey\":\"pk1\",\n" +
                        "            \"transferType\":100,\n" +
                        "            \"advertImageUrl\":\"https://m.360buyimg.com/mobilecms/s700x280_jfs/t1/114072/23/3596/150927/5ea93b31Eae6be065/9b428541bc22ea51.jpg!cr_1125x445_0_171!q70.jpg.dpg\",\n" +
                        "            \"sku\":12345676\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"transferUrl\":\"https://sale.jd.com/act/qQIdxFDe8hKX.html\",\n" +
                        "            \"positionKey\":\"pk2\",\n" +
                        "            \"transferType\":100,\n" +
                        "            \"advertImageUrl\":\"https://m.360buyimg.com/mobilecms/s700x280_jfs/t1/31162/17/1128/101786/5c46ead8E22ee9740/f66061da227c1965.jpg!cr_1125x445_0_171!q70.jpg.dpg\",\n" +
                        "            \"sku\":12345676\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"transferUrl\":\"https://sale.jd.com/act/qQIdxFDe8hKX.html\",\n" +
                        "            \"positionKey\":\"pk3\",\n" +
                        "            \"transferType\":100,\n" +
                        "            \"advertImageUrl\":\"https://m.360buyimg.com/mobilecms/s700x280_jfs/t1/116556/37/2900/145016/5ea4ee2cE2e489fec/2243074ec0c519fd.jpg!cr_1125x445_0_171!q70.jpg.dpg\",\n" +
                        "            \"sku\":12345676\n" +
                        "        },\n" +
                        "        {\n" +
                        "            \"transferUrl\":\"https://sale.jd.com/act/qQIdxFDe8hKX.html\",\n" +
                        "            \"positionKey\":\"pk4\",\n" +
                        "            \"transferType\":100,\n" +
                        "            \"advertImageUrl\":\"http://storage.jd.com/appimg/app_desc.png\",\n" +
                        "            \"sku\":12345676\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}"
                , BannerResult.class);
        carouselController.fillDataForBanner(bannerResult);
    }


}