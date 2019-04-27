package com.lt.paotui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hb.dialog.dialog.LoadingDialog;
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

/**
 * Created by Administrator on 2019/4/16.
 */

public class TicketActivity extends Activity {
    @BindView(R.id.top_bar_title)
    TextView top_bar_title;

    @BindView(R.id.note)
    EditText note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载启动界面
        setContentView(R.layout.activity_ticket);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        top_bar_title.setText(title);
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
                    Toast.makeText(TicketActivity.this,"提交成功！",Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case 1:
                    Toast.makeText(TicketActivity.this,"提交失败！",Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private void addTicketData(){

        final LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.setMessage("正在加载...");
        loadingDialog.show();

        Map userInfo = JSON.parseObject(SPUtils.get(this,"userinfo","{}").toString());
        final Message message=Message.obtain();

        OkHttpClient okHttpClient = new OkHttpClient();
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        FormBody formBody = new FormBody.Builder()
                .add("cus_id", userInfo.get("id").toString())
                .add("note", note.getText().toString())
                .add("type", type)
                .build();
        Request request = new Request.Builder().url(Config.url+"/addTicket")
                .addHeader("source", Config.REQUEST_HEADER)// 自定义的header
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                loadingDialog.dismiss();
                // TODO: 17-1-4  请求失败
                message.what=1;
                //message.obj=data;
                handler.sendMessage(message);

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                loadingDialog.dismiss();
                // TODO: 17-1-4 请求成功
                Map resultMap = JSON.parseObject(response.body().string());
                if(resultMap.get("status").equals("0")){
                    message.what=0;
                    handler.sendMessage(message);
                }
                else{
                    message.what=1;
                    handler.sendMessage(message);
                }
            }
        });
    }
    @OnClick({R.id.top_back_btn,R.id.submit})
    public void btnClick(View view) {


        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.submit:
                if(note.getText().toString().equals("")){
                    Toast.makeText(TicketActivity.this, "请输入您的留言！",Toast.LENGTH_LONG).show();
                }
                else{
                    addTicketData();
                }

                break;

            default:
                break;
        }
    }

}
