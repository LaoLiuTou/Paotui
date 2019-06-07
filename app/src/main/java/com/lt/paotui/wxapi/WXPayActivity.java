package com.lt.paotui.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lt.paotui.R;
import com.lt.paotui.activity.OrderDetailActivity;
import com.lt.paotui.utils.Config;
import com.lt.paotui.utils.PayDialog;
import com.lt.paotui.utils.SPUtils;
import com.lt.paotui.wxapi.util.WeiXinConstants;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


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

import static com.lt.paotui.wxapi.util.WeiXinConstants.APP_ID;


public class WXPayActivity extends AppCompatActivity {
    private IWXAPI wxapi;

    @BindView(R.id.ordernum)
    TextView ordernum;

    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.balance)
    TextView balance;

    @BindView(R.id.top_bar_title)
    TextView top_bar_title;

    private String order_id,ordernumber,price_pay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpay);
        ButterKnife.bind(this);
        getOrderData();
        top_bar_title.setText("订单支付");
        wxapi = WXAPIFactory.createWXAPI(this, APP_ID,false);
        wxapi.registerApp(APP_ID);





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
                    Map dataMap=(Map)msg.obj;
                    ordernum.setText(dataMap.get("ordernum").toString());
                    price.setText(dataMap.get("price")+"元");
                    balance.setText(dataMap.get("balance")+"元");

                    order_id=dataMap.get("id").toString();
                    ordernumber=dataMap.get("ordernum").toString();
                    price_pay=(Integer.parseInt(dataMap.get("price").toString())-Integer.parseInt(dataMap.get("balance").toString()))+"";



                    new PayDialog(WXPayActivity.this)
                            .setData(Double.parseDouble(price_pay), 0)
                            .haveWXPay(true)
                            .haveAliPay(true)
                            .haveBalance(false)
                            .setListener(new PayDialog.OnPayClickListener() {
                                @Override
                                public void onPayClick(int payType) {
                                    switch (payType) {
                                        case PayDialog.ALI_PAY:
                                            Toast.makeText(WXPayActivity.this, "暂不支持支付宝支付！", Toast.LENGTH_LONG).show();
                                            break;
                                        case PayDialog.WX_PAY:
                                            wxpay();
                                            break;
                                        case PayDialog.BALANCE_PAY:
                                            Toast.makeText(WXPayActivity.this, "余额", Toast.LENGTH_LONG).show();
                                            break;
                                    }
                                }
                            }).show();

                    break;
                case 1:
                    Toast.makeText(WXPayActivity.this, "获取订单信息失败！",Toast.LENGTH_LONG).show();
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

    private void wxpay(){

        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("userId", order_id)
                .add("totalFee", Integer.parseInt(price_pay)*100+"")
                .add("out_trade_no", ordernumber)
                .build();
        Request request = new Request.Builder().url(WeiXinConstants.url)
                .addHeader("source", Config.REQUEST_HEADER)// 自定义的header
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: 17-1-4  请求失败
                //button.setEnabled(true);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO: 17-1-4 请求成功
                //button.setEnabled(true);


                Map resultMap = JSON.parseObject(response.body().string());
                if(resultMap.get("status").equals("0")){
                    Map<String,String> payMap=(Map<String,String>)resultMap.get("msg");
                    String appId = payMap.get("appid");
                    String partnerId = payMap.get("partnerid");
                    String prepayId = payMap.get("prepayid");
                    String packageValue = payMap.get("package");
                    String nonceStr = payMap.get("noncestr");
                    String timeStamp = payMap.get("timestamp");
                    String extData = payMap.get("extdata");
                    String sign = payMap.get("sign");
                    PayReq req = new PayReq();
                    req.appId = appId;
                    req.partnerId = partnerId;
                    req.prepayId = prepayId;
                    req.packageValue = packageValue;
                    req.nonceStr = nonceStr;
                    req.timeStamp = timeStamp;
                    req.extData = extData;
                    req.sign = sign;

                    boolean result = wxapi.sendReq(req);
                    Log.i("调起支付结果:",result+"");
//

                    SPUtils.put(WXPayActivity.this,"order_id",order_id);

                }
                else{
                    Log.i("调起支付结果:","数据出错");
                }



            }
        });
    }
}
