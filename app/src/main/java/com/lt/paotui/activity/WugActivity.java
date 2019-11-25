package com.lt.paotui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.just.agentweb.AgentWeb;
import com.lt.paotui.R;
import com.lt.paotui.utils.SearchEditText;

/**
 * Created by Administrator on 2019/4/16.
 */

public class WugActivity extends Activity {
    @BindView(R.id.top_bar_title)
    TextView top_bar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载启动界面
        setContentView(R.layout.activity_fiveg);
        ButterKnife.bind(this);
        top_bar_title.setText("5G生活");

    }
    @OnClick({R.id.top_back_btn,R.id.znjj,R.id.zncd,R.id.wgwl,R.id.zhjy,R.id.zhnc,R.id.paxq,R.id.zhyl,R.id.zhlret,R.id.ycjk,R.id.zxsj})
    public void btnClick(View view) {

        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.znjj:
                intent.setClass(WugActivity.this, GoodsActivity.class);
                intent.putExtra("type", "5");
                intent.putExtra("subtype", "501");
                intent.putExtra("title", "智能家居");
                startActivity(intent);
                break;
            case R.id.zncd:
                intent.setClass(WugActivity.this, GoodsActivity.class);
                intent.putExtra("type", "5");
                intent.putExtra("subtype", "502");
                intent.putExtra("title", "智能穿戴");
                startActivity(intent);
                break;
            case R.id.wgwl:
                intent.setClass(WugActivity.this, GoodsActivity.class);
                intent.putExtra("type", "5");
                intent.putExtra("subtype", "503");
                intent.putExtra("title", "5G物联");
                startActivity(intent);
                break;
            case R.id.zhjy:
                intent.setClass(WugActivity.this, GoodsActivity.class);
                intent.putExtra("type", "5");
                intent.putExtra("subtype", "504");
                intent.putExtra("title", "智慧教育");
                startActivity(intent);
                break;
            case R.id.zhnc:
                intent.setClass(WugActivity.this, GoodsActivity.class);
                intent.putExtra("type", "5");
                intent.putExtra("subtype", "505");
                intent.putExtra("title", "智慧农村");
                startActivity(intent);
                break;
            case R.id.paxq:
                intent.setClass(WugActivity.this, GoodsActivity.class);
                intent.putExtra("type", "5");
                intent.putExtra("subtype", "506");
                intent.putExtra("title", "平安小区");
                startActivity(intent);
                break;
            case R.id.zhyl:
                intent.setClass(WugActivity.this, GoodsActivity.class);
                intent.putExtra("type", "5");
                intent.putExtra("subtype", "507");
                intent.putExtra("title", "智慧医疗");
                startActivity(intent);
                break;
            case R.id.zhlret:
                intent.setClass(WugActivity.this, GoodsActivity.class);
                intent.putExtra("type", "5");
                intent.putExtra("subtype", "508");
                intent.putExtra("title", "智慧老人儿童");
                startActivity(intent);
                break;
            case R.id.ycjk:
                intent.setClass(WugActivity.this, GoodsActivity.class);
                intent.putExtra("type", "5");
                intent.putExtra("subtype", "509");
                intent.putExtra("title", "远程监控");
                startActivity(intent);
                break;
            case R.id.zxsj:
                intent.setClass(WugActivity.this, GoodsActivity.class);
                intent.putExtra("type", "5");
                intent.putExtra("subtype", "510");
                intent.putExtra("title", "最新手机5G");
                startActivity(intent);
                break;



            default:
                break;
        }
    }
}
