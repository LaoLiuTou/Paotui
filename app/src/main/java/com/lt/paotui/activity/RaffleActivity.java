package com.lt.paotui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.lt.paotui.MainActivity;
import com.lt.paotui.R;
import com.lt.paotui.utils.Config;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2019/4/16.
 */

public class RaffleActivity extends Activity {
    //private TimeCount time;
    @BindView(R.id.container)
    LinearLayout mLinearLayout;
    protected AgentWeb mAgentWeb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载启动界面
        setContentView(R.layout.activity_raffle);
        ButterKnife.bind(this);

        mAgentWeb=AgentWeb.with(this)
                .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()//进度条
                //.setReceivedTitleCallback(mCallback) //设置 Web 页面的 title 回调
                .createAgentWeb()
                .ready()
                .go(Config.url+"listNews?page=1&size=10");

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
