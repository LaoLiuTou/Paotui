package com.lt.paotui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lt.paotui.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2019/4/16.
 */

public class CskxActivity extends Activity {
    @BindView(R.id.top_bar_title)
    TextView top_bar_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cskx);
        ButterKnife.bind(this);

        top_bar_title.setText("城市快讯");
    }
    @OnClick({R.id.top_back_btn,R.id.fwxx,R.id.zpqz,R.id.eswp,R.id.jypx,R.id.ys,
        R.id.cdcs,R.id.esc, R.id.jz,R.id.hmxx})
    public void btnClick(View view) {

        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.fwxx:
                intent.setClass(this, NewsListActivity.class);
                intent.putExtra("type", "21");
                intent.putExtra("title", "房屋信息");
                startActivity(intent);
                break;
            case R.id.zpqz:
                intent.setClass(this, NewsListActivity.class);
                intent.putExtra("type", "22");
                intent.putExtra("title", "招聘求职");
                startActivity(intent);
                break;
            case R.id.eswp:
                intent.setClass(this, NewsListActivity.class);
                intent.putExtra("type", "23");
                intent.putExtra("title", "二手物品");
                startActivity(intent);
                break;
            case R.id.jypx:
                intent.setClass(this, NewsListActivity.class);
                intent.putExtra("type", "24");
                intent.putExtra("title", "教育培训");
                startActivity(intent);
                break;
            case R.id.ys:
                intent.setClass(this, NewsListActivity.class);
                intent.putExtra("type", "25");
                intent.putExtra("title", "饮食");
                startActivity(intent);
                break;
            case R.id.cdcs:
                intent.setClass(this, NewsListActivity.class);
                intent.putExtra("type", "26");
                intent.putExtra("title", "出兑出售");
                startActivity(intent);
                break;
            case R.id.esc:
                intent.setClass(this, NewsListActivity.class);
                intent.putExtra("type", "28");
                intent.putExtra("title", "二手车");
                startActivity(intent);
                break;
            case R.id.jz:
                intent.setClass(this, NewsListActivity.class);
                intent.putExtra("type", "29");
                intent.putExtra("title", "兼职");
                startActivity(intent);
                break;
            case R.id.hmxx:
                intent.setClass(this, NewsListActivity.class);
                intent.putExtra("type", "30");
                intent.putExtra("title", "惠民信息");
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
