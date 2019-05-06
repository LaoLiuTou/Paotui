package com.lt.paotui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.just.agentweb.AgentWeb;
import com.lt.paotui.R;
import com.lt.paotui.utils.Config;
import com.lt.paotui.utils.SPUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2019/4/16.
 */

public class AntugovActivity extends Activity {
    //private TimeCount time;
    @BindView(R.id.container)
    LinearLayout mLinearLayout;
    @BindView(R.id.top_bar_title)
    TextView top_bar_title;
    protected AgentWeb mAgentWeb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载启动界面
        setContentView(R.layout.activity_antugov);
        ButterKnife.bind(this);
        top_bar_title.setText("安图县人民政府");

        mAgentWeb=AgentWeb.with(this)
                .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()//进度条
                //.setAgentWebWebSettings()
                //.setReceivedTitleCallback(mCallback) //设置 Web 页面的 title 回调
                .createAgentWeb()
                .ready()
                .go("http://www.antu.gov.cn/");
        mAgentWeb.getAgentWebSettings().getWebSettings().setUseWideViewPort(true); //将图片调整到适合webview的大小
        mAgentWeb.getAgentWebSettings().getWebSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小

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
