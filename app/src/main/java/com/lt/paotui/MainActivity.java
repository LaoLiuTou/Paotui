package com.lt.paotui;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


import com.alibaba.fastjson.JSON;
import com.lt.paotui.utils.Config;
import com.lt.paotui.utils.SPUtils;
import com.lt.paotui.utils.update.UpdateApk;
import com.lt.paotui.utils.update.UpdateBean;
import com.startsmake.mainnavigatetabbar.widget.MainNavigateTabBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.mainTabBar)
    MainNavigateTabBar mNavigateTabBar;

    private static final String TAG_PAGE_HOME = "首页";
    private static final String TAG_PAGE_PUBLISH = "拨打电话";
    private static final String TAG_PAGE_PERSON = "我的";

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
        mNavigateTabBar.onRestoreInstanceState(savedInstanceState);
        initFragments();
    }

    private void initFragments(){
        mNavigateTabBar.addTab(MainFragmentPage.class, new MainNavigateTabBar.TabParam(R.mipmap.nav1_dark, R.mipmap.nav1_light, TAG_PAGE_HOME));
        mNavigateTabBar.addTab(null, new MainNavigateTabBar.TabParam(0, 0, TAG_PAGE_PUBLISH));
        mNavigateTabBar.addTab(MyFragmentPage.class, new MainNavigateTabBar.TabParam(R.mipmap.nav3_dark, R.mipmap.nav3_light, TAG_PAGE_PERSON));

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mNavigateTabBar.onSaveInstanceState(outState);
    }


    public void onClickPublish(View v) {
        Toast.makeText(this, "发布", Toast.LENGTH_LONG).show();
    }

    // Activity中
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 获取到Activity下的Fragment
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments == null)
        {
            return;
        }
        // 查找在Fragment中onRequestPermissionsResult方法并调用
        for (Fragment fragment : fragments)
        {
            if (fragment != null)
            {
                // 这里就会调用我们Fragment中的onRequestPermissionsResult方法
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }
    private long exitTime = 0;
    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

}
