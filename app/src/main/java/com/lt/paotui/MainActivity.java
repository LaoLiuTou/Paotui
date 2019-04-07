package com.lt.paotui;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabview)
    TabLayoutView tabLayoutView;

    private ViewPagerAdapter viewPagerAdapter;

    private List<Fragment> fragmentPages;
    private MainFragmentPage mainfragmentPage;
    private MyFragmentPage myFragmentPage;

    private String[] titles = {"首页",  "我的"};
    private int[] imgs = {R.drawable.nav_main_selector, R.drawable.nav_me_selector};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor( ContextCompat.getColor(this,R.color.color_title_bg));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        initFragments();

        tabLayoutView.setDataSource(titles, imgs, 0);
        tabLayoutView.setImageStyle(25, 25);
        tabLayoutView.setTextStyle(12, R.color.color_999999,R.color.color_title_bg);
        tabLayoutView.initDatas();
        setDots();
        tabLayoutView.setOnItemOnclickListener(new TabLayoutView.OnItemOnclickListener() {
            @Override
            public void onItemClick(int index) {
                viewPager.setCurrentItem(index, true);
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position, false);
                tabLayoutView.setSelectStyle(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void initFragments()
    {
        fragmentPages = new ArrayList<>();
        mainfragmentPage = new MainFragmentPage();
        myFragmentPage = new MyFragmentPage();

        fragmentPages.add(mainfragmentPage);
        fragmentPages.add(myFragmentPage);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentPages);
        viewPager.setAdapter(viewPagerAdapter);
    }

    public void setDots()
    {
        tabLayoutView.setDotsCount(0, 0);
        tabLayoutView.setDotsCount(1, 0);
    }





}
