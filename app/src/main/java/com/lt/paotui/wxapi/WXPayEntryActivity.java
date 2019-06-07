package com.lt.paotui.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lt.paotui.R;
import com.lt.paotui.activity.OrderDetailActivity;
import com.lt.paotui.utils.Config;
import com.lt.paotui.utils.SPUtils;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
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


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

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
    @BindView(R.id.balance)
    TextView balance;
    @BindView(R.id.note)
    TextView note;
    @BindView(R.id.top_bar_title)
    TextView top_bar_title;
    @BindView(R.id.number)
    TextView number;
    @BindView(R.id.driverphone)
    TextView driverphone;
    @BindView(R.id.status)
    TextView status;

    private IWXAPI api;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        ButterKnife.bind(this);
        //getOrderData();
        top_bar_title.setText("订单详情");

        api = WXAPIFactory.createWXAPI(this, APP_ID);
        api.registerApp(APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }
    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        //extData:{"user_id":"26"} 怎么取出来呢
        //最好依赖于商户后台的查询结果
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            String message="";
           if(resp.errCode==-2){
               message="您已拒绝付款！";

           }
           else if(resp.errCode==-1){
               message="付款失败！";

           }
           else if(resp.errCode==0){

                //Map resultMap = JSON.parseObject(resp.t);
               message="付款成功！";
           }
            String order_id= SPUtils.get(WXPayEntryActivity.this,"order_id","0").toString();
            if(!order_id.equals("0")){
                getOrderData(order_id);
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage(message);
            builder.show();
        }
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
                    driver.setText(dataMap.get("drivername"));
                    number.setText(dataMap.get("number"));
                    String driverphonenum =dataMap.get("driverphone");
                    if(driverphonenum.length()>7){
                        driverphonenum = driverphonenum.substring(0, 3) + "****" + driverphonenum.substring(7, driverphonenum.length());
                    }
                    driverphone.setText(driverphonenum);
                    customer.setText(dataMap.get("cusname"));
                    phone.setText(dataMap.get("phone"));
                    paytime.setText(dataMap.get("pay_dt"));
                    price.setText(dataMap.get("price")+"元");
                    balance.setText(dataMap.get("balance")+"元");
                    note.setText(dataMap.get("note"));
                    if(dataMap.get("status").equals("0")){
                        status.setText("待支付");
                    }
                    break;
                case 1:
                    Toast.makeText(WXPayEntryActivity.this, "获取订单信息失败！",Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private void getOrderData(String order_id){


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