package com.lt.paotui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.just.agentweb.AgentWeb;
import com.lt.paotui.MainActivity;
import com.lt.paotui.R;

import butterknife.BindView;
import butterknife.ButterKnife;

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
                .go("http://www.jd.com");

    }

}
