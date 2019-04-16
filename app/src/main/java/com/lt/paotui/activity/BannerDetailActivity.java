package com.lt.paotui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
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

public class BannerDetailActivity extends Activity {
    @BindView(R.id.banner_title)
    TextView title;
    @BindView(R.id.banner_image)
    ImageView image;
    @BindView(R.id.banner_content)
    TextView content;
    @BindView(R.id.top_bar_title)
    TextView top_bar_title;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_detail);
        ButterKnife.bind(this);
        top_bar_title.setText("详情");
        getDetail();
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
                    Map detailMap = (Map)msg.obj;
                    title.setText(detailMap.get("title").toString());
                    content.setText(detailMap.get("content").toString());
                    Glide.with(BannerDetailActivity.this).load(Config.url+detailMap.get("image").toString()).into(image);

                    break;
                case 1:
                    Toast.makeText(BannerDetailActivity.this, msg.obj.toString(),Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
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

    private void getDetail(){
        final LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.setMessage("正在加载...");
        loadingDialog.show();
        Intent intent = getIntent();
        String banner_id = intent.getStringExtra("banner_id");
        final Message message=Message.obtain();
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("id", banner_id)
                .build();
        Request request = new Request.Builder().url(Config.url+"/selectBanner")
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
