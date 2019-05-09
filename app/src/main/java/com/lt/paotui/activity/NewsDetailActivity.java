package com.lt.paotui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.bumptech.glide.Glide;
import com.just.agentweb.AgentWeb;
import com.lt.paotui.R;
import com.lt.paotui.adapter.NewsAdapter;
import com.lt.paotui.adapter.NewsDetailAdapter;
import com.lt.paotui.utils.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2019/4/12.
 */

public class NewsDetailActivity extends Activity   {

    @BindView(R.id.container)
    LinearLayout mLinearLayout;
    @BindView(R.id.top_bar_title)
    TextView top_bar_title;
    protected AgentWeb mAgentWeb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载启动界面
        setContentView(R.layout.activity_new_web);
        ButterKnife.bind(this);
        top_bar_title.setText("资讯详情");
        Intent intent = getIntent();
        String news_id = intent.getStringExtra("news_id");
        mAgentWeb=AgentWeb.with(NewsDetailActivity.this)
                .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()//进度条
                //.setAgentWebWebSettings()
                //.setReceivedTitleCallback(mCallback) //设置 Web 页面的 title 回调
                .createAgentWeb()
                .ready()
                .go(Config.url+"pages/app_news_detail.html?id="+news_id);
        //mAgentWeb.getAgentWebSettings().getWebSettings().setUseWideViewPort(true); //将图片调整到适合webview的大小
        //mAgentWeb.getAgentWebSettings().getWebSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小


    }


    @OnClick({R.id.top_back_btn})
    public void btnClick(View view) {


        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;

            default:
                break;
        }
    }
}
