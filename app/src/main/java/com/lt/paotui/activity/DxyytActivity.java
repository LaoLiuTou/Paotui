package com.lt.paotui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.lt.paotui.MainActivity;
import com.lt.paotui.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2019/4/16.
 */

public class DxyytActivity extends Activity {
    @BindView(R.id.top_bar_title)
    TextView top_bar_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dxyyt);
        ButterKnife.bind(this);
        top_bar_title.setText("电信营业厅");

    }
    @OnClick({R.id.top_back_btn,R.id.hfcx,R.id.kdyw,R.id.tccx})
    public void btnClick(View view) {

        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.hfcx:
                break;
            case R.id.kdyw:
                intent.setClass(this, NewsListActivity.class);
                intent.putExtra("type", "4");
                intent.putExtra("title", "宽带业务");
                startActivity(intent);
            case R.id.tccx:
                break;
            default:
                break;
        }
    }
}
