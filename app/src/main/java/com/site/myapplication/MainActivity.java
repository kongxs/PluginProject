package com.site.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pluginproject.R;
import com.google.gson.Gson;
import com.site.myapplication.banner.BannerResult;
import com.site.myapplication.banner.CarouselController;
import com.site.myapplication.banner.HomePageTransformer;

//import androidx.jdviewpager2.adapter.FragmentStateAdapter;
//import androidx.jdviewpager2.widget.ViewPager2;
//import com.site.myapplication.banner.HomePageTransformer;

//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends FragmentActivity {

    private static final int VIEW_TYPE_CAROUSEL = 1;
    private static final int VIEW_TYPE_0 = 0;
    private static final int VIEW_TYPE_2 = 2;

    public VerticalViewPager mVerticalViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewpager2_vertical);
        mVerticalViewPager = (VerticalViewPager) findViewById(R.id.viewpager2);
//        RecyclerView.Adapter adapter = new RecyclerView.Adapter<ViewPagerViewHolder>() {
//            @NonNull
//            @Override
//            public ViewPagerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                ViewGroup inflateView = null;
//                if (viewType == VIEW_TYPE_CAROUSEL) {
//                    inflateView = (ViewGroup) LayoutInflater.from(getApplicationContext()).inflate(R.layout.viewpager2_vertical_item_1, parent, false);
//                    handleBanner(inflateView);
//                } else if (viewType == VIEW_TYPE_0) {
//                    inflateView = (ViewGroup) LayoutInflater.from(getApplicationContext()).inflate(R.layout.viewpager2_vertical_item_0, parent, false);
//                } else if (viewType == VIEW_TYPE_2) {
//                    inflateView = (ViewGroup) LayoutInflater.from(getApplicationContext()).inflate(R.layout.viewpager2_vertical_item_2, parent, false);
//                }
//                return new ViewPagerViewHolder(inflateView);
//            }
//
//            @Override
//            public void onBindViewHolder(@NonNull ViewPagerViewHolder holder, int position) {
//                int viewType = getItemViewType(position);
//                String content = "";
//                if (viewType == VIEW_TYPE_CAROUSEL) {
//                    content = "竖向 position: " + position + ", CAROUSEL";
//                    holder.itemView.findViewById(R.id.arrow_up).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            mViewPager2.setCurrentItem(0, true);
//                        }
//                    });
//                    holder.itemView.findViewById(R.id.arrow_down).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            mViewPager2.setCurrentItem(2, true);
//                        }
//                    });
//                } else if (viewType == VIEW_TYPE_0) {
//                    content = "竖向 position: " + position + ", 顶部页面";
//                } else if (viewType == VIEW_TYPE_2) {
//                    content = "竖向 position: " + position + ", 底部页面";
//                }
//                holder.mTextView.setText(content);
//            }
//
//            @Override
//            public int getItemCount() {
//                return 3;
//            }
//
//            @Override
//            public int getItemViewType(int position) {
//                if (position == 1) {
//                    return VIEW_TYPE_CAROUSEL;
//                } else if (position == 0) {
//                    return VIEW_TYPE_0;
//                } else if (position == 2) {
//                    return VIEW_TYPE_2;
//                }
//                return super.getItemViewType(position);
//            }
//        };

//        FragmentStateAdapter  fragmentStateAdapter = new FragmentStateAdapter(this) {
//            @NonNull
//            @Override
//            public Fragment createFragment(int position) {
//                return TestFragment.newInstance(getItemViewType(position));
//            }
//
//            @Override
//            public int getItemCount() {
//                return 3;
//            }
//
//            @Override
//            public int getItemViewType(int position) {
//                if (position == 1) {
//                    return VIEW_TYPE_CAROUSEL;
//                } else if (position == 0) {
//                    return VIEW_TYPE_0;
//                } else if (position == 2) {
//                    return VIEW_TYPE_2;
//                }
//                return super.getItemViewType(position);
//            }
//        };


//        PagerAdapter pageAdapter = new PagerAdapter() {
//
//            @Override
//            public int getCount() {
//                return 3;
//            }
//
//            @NonNull
//            @Override
//            public Object instantiateItem(@NonNull ViewGroup container, final int position) {
//                ViewGroup inflateView = null;
//                if (position == VIEW_TYPE_CAROUSEL) {
//                    inflateView = (ViewGroup) LayoutInflater.from(getApplicationContext()).inflate(R.layout.viewpager2_vertical_item_1, container, false);
//                    handleBanner(inflateView);
//                } else if (position == VIEW_TYPE_0) {
//                    inflateView = (ViewGroup) LayoutInflater.from(getApplicationContext()).inflate(R.layout.viewpager2_vertical_item_0, container, false);
//                    inflateView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Toast.makeText(MainActivity.this, "button Click: VIEW_TYPE_0 ", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                } else if (position == VIEW_TYPE_2) {
//                    inflateView = (ViewGroup) LayoutInflater.from(getApplicationContext()).inflate(R.layout.viewpager2_vertical_item_2, container, false);
//                    inflateView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Toast.makeText(MainActivity.this, "button Click: VIEW_TYPE_2 ", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//                container.addView(inflateView);
//                return inflateView;
//            }
//
//            @Override
//            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//                if (object instanceof View) {
//                    container.removeView((View) object);
//                }
//            }
//
//            @Override
//            public boolean isViewFromObject(View view, Object object) {
//                return view == object;
//            }
//        };


        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(this.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return TestFragment.newInstance(getItemViewType(position));
            }

            int getItemViewType(int position) {
                if (position == 1) {
                    return VIEW_TYPE_CAROUSEL;
                } else if (position == 0) {
                    return VIEW_TYPE_0;
                } else if (position == 2) {
                    return VIEW_TYPE_2;
                }
                return -1;
            }

            @Override
            public int getCount() {
                return 3;
            }
        };


        mVerticalViewPager.setAdapter(fragmentPagerAdapter);
        mVerticalViewPager.setPageTransformer(true, new HomePageTransformer(0.2f));
        mVerticalViewPager.setCurrentItem(1, false);
    }

    static class ViewPagerViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public ViewPagerViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv);
        }
    }

    private void handleBanner(ViewGroup viewGroup) {
        final View carouselLayout = viewGroup.findViewById(R.id.home_banner_root);
//        viewGroup.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return carouselLayout.findViewById(R.id.home_focus_pager_box).dispatchTouchEvent(event);
//            }
//        });

        CarouselController carouselController = new CarouselController();
        carouselController.initFocusPager(this, carouselLayout);
//        CarouselController.BannerResult bannerResult = new Gson().fromJson("{\"bannerList\":[{\"transferUrl\":\"https://sale.jd.com/act/qQIdxFDe8hKX.html\",\"positionKey\":\"pk1\",\"transferType\":100,\"advertImageUrl\":\"http://storage.jd.com/appimg/app_desc.png\",\"sku\":12345676},{\"transferUrl\":\"https://sale.jd.com/act/qQIdxFDe8hKX.html\",\"positionKey\":\"pk2\",\"transferType\":100,\"advertImageUrl\":\"http://storage.jd.com/appimg/app_desc.png\",\"sku\":12345676},{\"transferUrl\":\"https://sale.jd.com/act/qQIdxFDe8hKX.html\",\"positionKey\":\"pk3\",\"transferType\":100,\"advertImageUrl\":\"http://storage.jd.com/appimg/app_desc.png\",\"sku\":12345676},{\"transferUrl\":\"https://sale.jd.com/act/qQIdxFDe8hKX.html\",\"positionKey\":\"pk4\",\"transferType\":100,\"advertImageUrl\":\"http://storage.jd.com/appimg/app_desc.png\",\"sku\":12345676}]}", CarouselController.BannerResult.class);
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
