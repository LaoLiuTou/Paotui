package com.lt.paotui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.just.agentweb.AgentWeb;
import com.lt.paotui.R;

/**
 * Created by Administrator on 2019/4/16.
 */

public class FupinActivity extends Activity {
    @BindView(R.id.top_bar_title)
    TextView top_bar_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载启动界面
        setContentView(R.layout.activity_fupin);
        ButterKnife.bind(this);
        top_bar_title.setText("扶贫专区");


    }
    @OnClick({R.id.top_back_btn,R.id.fpcp,R.id.bfsj,R.id.fpzs,R.id.fptt})
    public void btnClick(View view) {

        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.fpcp:
                intent.setClass(FupinActivity.this, GoodsActivity.class);
                intent.putExtra("type", "4");
                intent.putExtra("subtype", "41");
                intent.putExtra("title", "扶贫产品");
                startActivity(intent);
                break;
            case R.id.bfsj:
                intent.setClass(FupinActivity.this, GoodsActivity.class);
                intent.putExtra("type", "4");
                intent.putExtra("subtype", "42");
                intent.putExtra("title", "帮扶商家");
                startActivity(intent);
                break;
            case R.id.fpzs:
                intent.setClass(FupinActivity.this, GoodsActivity.class);
                intent.putExtra("type", "4");
                intent.putExtra("subtype", "43");
                intent.putExtra("title", "扶贫展示");
                startActivity(intent);
                break;
            case R.id.fptt:
                intent.setClass(FupinActivity.this, GoodsActivity.class);
                intent.putExtra("type", "4");
                intent.putExtra("subtype", "44");
                intent.putExtra("title", "扶贫头条");
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
