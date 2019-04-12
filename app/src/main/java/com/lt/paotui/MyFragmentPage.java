package com.lt.paotui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.lt.paotui.activity.LoginActivity;
import com.lt.paotui.activity.OrderListActivity;
import com.lt.paotui.utils.Config;
import com.lt.paotui.utils.SPUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

public class MyFragmentPage extends Fragment  {

    private Unbinder unbinder;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.yue)
    TextView yue;
    @BindView(R.id.header)
    ImageView header;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_my, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getUserData();
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


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
                    Map userInfo = JSON.parseObject(SPUtils.get(getContext(),"userinfo","{}").toString());
                    username.setText(userInfo.get("phone").toString());
                    yue.setText(userInfo.get("balance").toString());
                    Glide.with(getContext()).load(Config.url+userInfo.get("header").toString()).into(header);
                    break;
                case 1:
                    Toast.makeText(getContext(), "同步用户信息失败！",Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private void getUserData(){

        Map userInfo = JSON.parseObject(SPUtils.get(getContext(),"userinfo","{}").toString());
        username.setText(userInfo.get("phone").toString());
        yue.setText(userInfo.get("balance").toString());
        Glide.with(getContext()).load(Config.url+userInfo.get("header").toString()).into(header);

        final Message message=Message.obtain();

        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("id", userInfo.get("id").toString())
                .build();
        Request request = new Request.Builder().url(Config.url+"/selectCustomer")
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
                    SPUtils.put(getContext(),"userinfo",resultMap.get("msg"));
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


    //监听事件
    @OnClick({R.id.myinfo,R.id.logout,R.id.zfjl})
    public void btnClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.myinfo:

                break;
            case R.id.logout:
                SPUtils.clear(getContext());
                intent.setClass(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.zfjl:
                intent.setClass(getActivity(), OrderListActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
