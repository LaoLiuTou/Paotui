package com.lt.paotui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.lt.paotui.R;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2019/4/10.
 */

public class OrderItemActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);

    }
}
