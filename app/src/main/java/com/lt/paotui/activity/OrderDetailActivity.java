package com.lt.paotui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.lt.paotui.R;
import com.lt.paotui.utils.Config;
import com.lt.paotui.utils.SPUtils;

import java.io.IOException;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OrderDetailActivity extends Activity {
    @BindView(R.id.ordernum)
    TextView ordernum;
    @BindView(R.id.driver)
    TextView driver;
    @BindView(R.id.customer)
    TextView customer;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.paytime)
    TextView paytime;
    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.note)
    TextView note;
    @BindView(R.id.top_bar_title)
    TextView top_bar_title;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        ButterKnife.bind(this);
        getOrderData();
        top_bar_title.setText("订单详情");
    }
    /**
     * 接收解析后传过来的数据
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //Object model = (Object) msg.obj;
            switch (msg.what){
                case 0:
                    Map<String,String> dataMap=(Map<String,String>)msg.obj;
                    ordernum.setText(dataMap.get("ordernum"));
                    driver.setText(dataMap.get("driver"));
                    customer.setText(dataMap.get("cusname"));
                    phone.setText(dataMap.get("phone"));
                    paytime.setText(dataMap.get("pay_dt"));
                    price.setText(dataMap.get("price")+"元");
                    note.setText(dataMap.get("note"));
                    break;
                case 1:
                    Toast.makeText(OrderDetailActivity.this, "获取订单信息失败！",Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private void getOrderData(){

        Intent intent = getIntent();
        String order_id = intent.getStringExtra("order_id");
        final Message message=Message.obtain();

        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("id", order_id)
                .build();
        Request request = new Request.Builder().url(Config.url+"/selectOrders")
                .addHeader("source", Config.REQUEST_HEADER)// 自定义的header
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: 17-1-4  请求失败
                message.what=1;
                //message.obj=data;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO: 17-1-4 请求成功
                Map resultMap = JSON.parseObject(response.body().string());
                if(resultMap.get("status").equals("0")){
                    Map<String,String> dataMap=(Map<String,String>)resultMap.get("msg");
                    message.what=0;
                    message.obj=dataMap;
                    handler.sendMessage(message);
                }
                else{
                    message.what=1;
                    handler.sendMessage(message);
                }
            }
        });
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
