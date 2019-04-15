package com.lt.paotui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.hb.dialog.dialog.LoadingDialog;
import com.hb.dialog.myDialog.ActionSheetDialog;
import com.hb.dialog.myDialog.MyAlertDialog;
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

public class MyinfoActivity extends Activity {
    @BindView(R.id.header)
    ImageView header;
    @BindView(R.id.nickname)
    EditText nickname;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.balance)
    TextView balance;
    @BindView(R.id.c_dt)
    TextView c_dt;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        ButterKnife.bind(this);
        initMyinfo();
    }
    private void initMyinfo(){
        Map userInfo = JSON.parseObject(SPUtils.get(this,"userinfo","{}").toString());
        Glide.with(this).load(Config.url+userInfo.get("header").toString()).placeholder(R.mipmap.header).error(R.mipmap.header).into(header);
        nickname.setText(userInfo.get("nickname").toString());
        phone.setText(userInfo.get("phone").toString());
        balance.setText(userInfo.get("balance").toString());
        c_dt.setText(userInfo.get("c_dt").toString());

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
                    SPUtils.put(MyinfoActivity.this,"userinfo",msg.obj.toString());
                    initMyinfo();
                    Toast.makeText(MyinfoActivity.this, "修改成功！",Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(MyinfoActivity.this, msg.obj.toString(),Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @OnClick({R.id.header,R.id.saveBtn})
    public void btnClick(View view) {


        switch (view.getId()) {
            case R.id.header:
                ActionSheetDialog dialog = new ActionSheetDialog(this).builder().setTitle("请选择")
                        .addSheetItem("相册", null, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {

                            }
                        }).addSheetItem("拍照", null, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {


                            }
                        });
                dialog.show();
                break;
            case R.id.saveBtn:
                MyAlertDialog myAlertDialog = new MyAlertDialog(MyinfoActivity.this).builder()
                        .setTitle("确认吗？")
                        .setMsg("即将修改个人信息")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                update();
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                myAlertDialog.show();

                break;

            default:
                break;
        }
    }


    private void update(){
        final LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.setMessage("正在加载...");
        loadingDialog.show();
        Map userInfo = JSON.parseObject(SPUtils.get(this,"userinfo","{}").toString());
        final Message message=Message.obtain();
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("id", userInfo.get("id").toString())
                .add("nickname", nickname.getText().toString())
                .build();
        Request request = new Request.Builder().url(Config.url+"/updateCustomer")
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
