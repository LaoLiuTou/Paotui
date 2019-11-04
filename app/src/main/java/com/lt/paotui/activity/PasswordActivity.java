package com.lt.paotui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hb.dialog.dialog.LoadingDialog;
import com.lt.paotui.MainActivity;
import com.lt.paotui.R;
import com.lt.paotui.utils.Config;
import com.lt.paotui.utils.MD5Util;
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

public class PasswordActivity extends Activity {
    @BindView(R.id.top_bar_title)
    TextView top_bar_title;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.re_password)
    EditText re_password;
    @BindView(R.id.idcode)
    EditText idcode;
    @BindView(R.id.sendcode)
    Button sendcode;
    private TimeCount time;
    String code="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        ButterKnife.bind(this);
        time = new TimeCount(60000, 1000);
        top_bar_title.setText("重置密码");
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
                    Toast.makeText(PasswordActivity.this, "修改成功，请重新登录！",Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case 1:
                    Toast.makeText(PasswordActivity.this, msg.obj.toString(),Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @OnClick({R.id.btn_submit,R.id.sendcode,R.id.top_back_btn})
    public void btnClick(View view) {

        switch (view.getId()) {
            case R.id.btn_submit:
                if(!isMobileNO(username.getText().toString())){
                    Toast.makeText(PasswordActivity.this, "请输入正确的手机号！",Toast.LENGTH_LONG).show();
                }
                else if(password.getText().toString().equals("")){
                    Toast.makeText(PasswordActivity.this, "请输入新密码！",Toast.LENGTH_LONG).show();
                }
                else if(!password.getText().toString().equals(re_password.getText().toString())){
                    Toast.makeText(PasswordActivity.this, "两次输入的密码不一致！",Toast.LENGTH_LONG).show();
                }
                else if(code.equals("")||!code.equals(idcode.getText().toString())){
                    Toast.makeText(PasswordActivity.this, "验证码错误！",Toast.LENGTH_LONG).show();
                }
                else{
                    register();
                }
                break;

            case R.id.sendcode:
                sendIdcode();
                time.start();
                break;
            case R.id.top_back_btn:
                finish();
                break;
            default:
                break;
        }
    }
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            sendcode.setBackgroundColor(Color.parseColor("#B6B6D8"));
            sendcode.setClickable(false);
            sendcode.setText("("+millisUntilFinished / 1000 +") 秒后重发");
        }

        @Override
        public void onFinish() {
            sendcode.setText("重新获取验证码");
            sendcode.setClickable(true);
            sendcode.setBackgroundColor(Color.parseColor("#4cc7a8"));

        }
    }
    private void sendIdcode(){
        if(!isMobileNO(username.getText().toString())){
            Toast.makeText(PasswordActivity.this, "请输入正确的手机号！",Toast.LENGTH_LONG).show();
            return;
        }
        code=((int)((Math.random()*9+1)*100000))+"";
        final Message message=Message.obtain();
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("mobile", username.getText().toString())
                .add("code",code)
                .build();
        Request request = new Request.Builder().url(Config.url+"/sendMessage")
                .addHeader("source", Config.REQUEST_HEADER)// 自定义的header
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: 17-1-4  请求失败
                /*message.what=1;
                handler.sendMessage(message);*/
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO: 17-1-4 请求成功
                /*Map resultMap = JSON.parseObject(response.body().string());
                if(resultMap.get("status").equals("0")){
                    //Map<String,String> userMap=(Map<String,String>)resultMap.get("msg");
                    message.what=0;
                    handler.sendMessage(message);
                }
                else{
                    message.what=1;
                    handler.sendMessage(message);
                }*/
            }
        });
    }
    private void register(){
        final LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.setMessage("正在加载...");
        loadingDialog.show();
        final Message message=Message.obtain();
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("phone", username.getText().toString())
                .add("password", MD5Util.MD5(password.getText().toString()))
                .build();
        Request request = new Request.Builder().url(Config.url+"/passwordCustomer")
                .addHeader("source", Config.REQUEST_HEADER)// 自定义的header
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                loadingDialog.dismiss();
                // TODO: 17-1-4  请求失败
                message.what=1;
                message.obj="请求失败！";
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO: 17-1-4 请求成功
                loadingDialog.dismiss();
                Map resultMap = JSON.parseObject(response.body().string());
                if(resultMap.get("status").equals("0")){
                    //Map<String,String> userMap=(Map<String,String>)resultMap.get("msg");
                    message.what=0;
                    message.obj=resultMap.get("msg");
                    handler.sendMessage(message);
                }
                else{
                    message.what=1;
                    message.obj=resultMap.get("msg");
                    handler.sendMessage(message);
                }


            }
        });
    }
    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {

        String telRegex = "[1][3456789]\\d{9}";
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }
}
