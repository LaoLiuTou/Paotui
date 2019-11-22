package com.lt.paotui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.just.agentweb.AgentWeb;
import com.lt.paotui.R;
import com.lt.paotui.utils.Config;
import com.lt.paotui.utils.QrCodeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2019/4/16.
 */

public class ShowQrcodeActivity extends Activity {

    @BindView(R.id.top_bar_title)
    TextView top_bar_title;
    @BindView(R.id.qrcodetitle)
    TextView qrcodetitle;
    @BindView(R.id.qrcodeimage)
    ImageView qrcodeImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载启动界面
        setContentView(R.layout.activity_qrcode);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");

        if(type.equals("1")){
            qrcodetitle.setText("便民1号线");
            top_bar_title.setText("二维码分享");
            String content= Config.url+"version/bmyhx.apk";
            Bitmap bitmap = QrCodeUtil.createQRCodeWithLogo(content, 500,
                    BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
            qrcodeImage.setImageBitmap(bitmap);
        }
        else{
            qrcodetitle.setText("翼支付付款二维码");
            top_bar_title.setText("付款二维码");
            qrcodeImage.setImageDrawable(ContextCompat.getDrawable(ShowQrcodeActivity.this, R.mipmap.yzfqrcode));
        }


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
