package com.lt.paotui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.lt.paotui.R;

/**
 * Created by Administrator on 2019/4/16.
 */

public class YijiaActivity extends Activity {
    @BindView(R.id.top_bar_title)
    TextView top_bar_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载启动界面
        setContentView(R.layout.activity_yijia);
        ButterKnife.bind(this);
        top_bar_title.setText("翼家超市");



    }
    @OnClick({R.id.top_back_btn,R.id.ggsc,R.id.ly,R.id.sjpj,R.id.sjzq,R.id.ggttc,R.id.jdzq,R.id.ncjql,R.id.ybcs,R.id.yzfzq,R.id.yhzq,R.id.sqdg,R.id.fkewm})
    public void btnClick(View view) {

        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.ggsc:
                intent.setClass(YijiaActivity.this, GoodsActivity.class);
                intent.putExtra("type", "6");
                intent.putExtra("subtype", "601");
                intent.putExtra("title", "瓜果蔬菜");
                startActivity(intent);
                break;
            case R.id.ly:
                intent.setClass(YijiaActivity.this, GoodsActivity.class);
                intent.putExtra("type", "6");
                intent.putExtra("subtype", "602");
                intent.putExtra("title", "粮油");
                startActivity(intent);
                break;
            case R.id.sjpj:
                intent.setClass(YijiaActivity.this, GoodsActivity.class);
                intent.putExtra("type", "6");
                intent.putExtra("subtype", "603");
                intent.putExtra("title", "手机配件");
                startActivity(intent);
                break;
            case R.id.sjzq:
                intent.setClass(YijiaActivity.this, GoodsActivity.class);
                intent.putExtra("type", "6");
                intent.putExtra("subtype", "604");
                intent.putExtra("title", "手机专区");
                startActivity(intent);
                break;
            case R.id.ggttc:
                intent.setClass(YijiaActivity.this, GoodsActivity.class);
                intent.putExtra("type", "6");
                intent.putExtra("subtype", "605");
                intent.putExtra("title", "干果土特产");
                startActivity(intent);
                break;
            case R.id.jdzq:
                intent.setClass(YijiaActivity.this, GoodsActivity.class);
                intent.putExtra("type", "6");
                intent.putExtra("subtype", "606");
                intent.putExtra("title", "家电专区");
                startActivity(intent);
                break;
            case R.id.ncjql:
                intent.setClass(YijiaActivity.this, GoodsActivity.class);
                intent.putExtra("type", "6");
                intent.putExtra("subtype", "607");
                intent.putExtra("title", "农村家禽类");
                startActivity(intent);
                break;
            case R.id.ybcs:
                intent.setClass(YijiaActivity.this, GoodsActivity.class);
                intent.putExtra("type", "6");
                intent.putExtra("subtype", "608");
                intent.putExtra("title", "延百超市");
                startActivity(intent);
                break;
            case R.id.yzfzq:
                intent.setClass(YijiaActivity.this, GoodsActivity.class);
                intent.putExtra("type", "6");
                intent.putExtra("subtype", "609");
                intent.putExtra("title", "翼支付专区");
                startActivity(intent);
                break;
            case R.id.yhzq:
                intent.setClass(YijiaActivity.this, GoodsActivity.class);
                intent.putExtra("type", "6");
                intent.putExtra("subtype", "610");
                intent.putExtra("title", "优惠专区");
                startActivity(intent);
                break;
            case R.id.sqdg:
                intent.setClass(YijiaActivity.this, GoodsActivity.class);
                intent.putExtra("type", "6");
                intent.putExtra("subtype", "611");
                intent.putExtra("title", "商圈代购区");
                startActivity(intent);
                break;
            case R.id.fkewm:
                intent.setClass(YijiaActivity.this, ShowQrcodeActivity.class);
                intent.putExtra("type", "2");
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
