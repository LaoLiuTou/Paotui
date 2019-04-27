package com.lt.paotui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.just.agentweb.AgentWeb;
import com.lt.paotui.MainActivity;
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

public class RaffleActivity extends Activity {
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
        setContentView(R.layout.activity_raffle);
        ButterKnife.bind(this);
        top_bar_title.setText("免费抽大奖");

        Map userInfo = JSON.parseObject(SPUtils.get(this,"userinfo","{}").toString());
        mAgentWeb=AgentWeb.with(this)
                .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()//进度条
                //.setReceivedTitleCallback(mCallback) //设置 Web 页面的 title 回调
                .createAgentWeb()
                .ready()
                .go(Config.url+"pages/goldegg/index.html?cus_id="+userInfo.get("id").toString());

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
