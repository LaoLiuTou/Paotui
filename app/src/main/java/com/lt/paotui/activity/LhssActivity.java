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

public class LhssActivity extends Activity {
    @BindView(R.id.top_bar_title)
    TextView top_bar_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lhss);
        ButterKnife.bind(this);

        top_bar_title.setText("靓号收售");
    }
    @OnClick({R.id.top_back_btn,R.id.dx,R.id.yd,R.id.lt,R.id.xlt,R.id.qqh,
        R.id.ylhm})
    public void btnClick(View view) {

        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.dx:
                intent.setClass(this, GoodsActivity.class);
                intent.putExtra("type", "2");
                intent.putExtra("subtype", "21");
                intent.putExtra("title", "靓号收售-电信");
                startActivity(intent);
                break;
            case R.id.yd:
                intent.setClass(this, GoodsActivity.class);
                intent.putExtra("type", "2");
                intent.putExtra("subtype", "22");
                intent.putExtra("title", "靓号收售-移动");
                startActivity(intent);
                break;
            case R.id.lt:
                intent.setClass(this, GoodsActivity.class);
                intent.putExtra("type", "2");
                intent.putExtra("subtype", "23");
                intent.putExtra("title", "靓号收售-联通");
                startActivity(intent);
                break;
            case R.id.xlt:
                intent.setClass(this, GoodsActivity.class);
                intent.putExtra("type", "2");
                intent.putExtra("subtype", "24");
                intent.putExtra("title", "靓号收售-小灵通");
                startActivity(intent);
                break;
            case R.id.qqh:
                intent.setClass(this, GoodsActivity.class);
                intent.putExtra("type", "2");
                intent.putExtra("subtype", "25");
                intent.putExtra("title", "靓号收售-亲情号");
                startActivity(intent);
                break;
            case R.id.ylhm:
                intent.setClass(this, GoodsActivity.class);
                intent.putExtra("type", "2");
                intent.putExtra("subtype", "26");
                intent.putExtra("title", "靓号收售-优良号码");
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
