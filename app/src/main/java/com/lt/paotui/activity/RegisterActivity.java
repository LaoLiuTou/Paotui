package com.lt.paotui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.lt.paotui.MainActivity;
import com.lt.paotui.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }
    @OnClick({R.id.btn_submit,R.id.btn_cancel})
    public void btnClick(View view) {
        Intent intent = new Intent();

        switch (view.getId()) {
            case R.id.btn_login:
                intent.setClass(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_cancel:

                intent.setClass(this, RegisterActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
