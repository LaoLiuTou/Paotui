package com.lt.paotui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.github.shenyuanqing.zxingsimplify.zxing.Activity.CaptureActivity;
import com.lt.paotui.MainActivity;
import com.lt.paotui.R;
import com.lt.paotui.utils.Config;
import com.lt.paotui.utils.Constant;
import com.lt.paotui.utils.MD5Util;
import com.lt.paotui.utils.SPUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

public class LoginActivity extends Activity {
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        if((boolean)SPUtils.get(this,"islogin",false)){
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, MainActivity.class);
            startActivity(intent);
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
                    SPUtils.put(LoginActivity.this,"islogin",true);
                    SPUtils.put(LoginActivity.this,"userinfo",msg.obj.toString());
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    Toast.makeText(LoginActivity.this, msg.obj.toString(),Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @OnClick({R.id.btn_login,R.id.btn_register})
    public void btnClick(View view) {


        switch (view.getId()) {
            case R.id.btn_login:
                if(username.getText()!=null&&password.getText()!=null){
                    login();
                }
                else{
                    Toast.makeText(LoginActivity.this, "用户名或密码不能为空！",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_register:
                Intent intent = new Intent();
                intent.setClass(this, RegisterActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }


    private void login(){
        final Message message=Message.obtain();
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("phone", username.getText().toString())
                .add("password", MD5Util.MD5(password.getText().toString()))
                .build();
        Request request = new Request.Builder().url(Config.url+"/loginCustomer")
                .addHeader("source", Config.REQUEST_HEADER)// 自定义的header
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: 17-1-4  请求失败
                message.what=1;
                message.obj="请求失败！";
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO: 17-1-4 请求成功
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
}
