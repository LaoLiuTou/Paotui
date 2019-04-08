package com.lt.paotui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.github.shenyuanqing.zxingsimplify.zxing.Activity.CaptureActivity;
import com.lt.paotui.MainActivity;
import com.lt.paotui.R;
import com.lt.paotui.utils.Constant;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_login,R.id.btn_register})
    public void btnClick(View view) {
        Intent intent = new Intent();

        switch (view.getId()) {
            case R.id.btn_login:
                intent.setClass(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_register:

                intent.setClass(this, RegisterActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
