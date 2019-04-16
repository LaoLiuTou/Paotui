package com.lt.paotui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.hb.dialog.dialog.LoadingDialog;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.lt.paotui.activity.LoginActivity;
import com.lt.paotui.activity.MyinfoActivity;
import com.lt.paotui.activity.OrderListActivity;
import com.lt.paotui.activity.RegisterActivity;
import com.lt.paotui.utils.Config;
import com.lt.paotui.utils.MD5Util;
import com.lt.paotui.utils.SPUtils;

import java.io.IOException;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2019/4/2.
 */

public class LoginFragmentPage extends Fragment  {

    private Unbinder unbinder;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_login, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
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
                    SPUtils.put(getContext(),"islogin",true);
                    SPUtils.put(getContext(),"userinfo",msg.obj.toString());
                    Intent intent = new Intent();
                    intent.setClass(getContext(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                    break;
                case 1:
                    Toast.makeText(getContext(), msg.obj.toString(),Toast.LENGTH_LONG).show();
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
                    Toast.makeText(getContext(), "用户名或密码不能为空！",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_register:
                Intent intent = new Intent();
                intent.setClass(getContext(), RegisterActivity.class);
                startActivity(intent);

                break;

            default:
                break;
        }
    }


    private void login(){
        final LoadingDialog loadingDialog = new LoadingDialog(getContext());
        loadingDialog.setMessage("正在加载...");
        loadingDialog.show();
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
                loadingDialog.dismiss();
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
}
