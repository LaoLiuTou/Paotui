package com.lt.paotui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.lt.paotui.activity.LeavingListActivity;
import com.lt.paotui.activity.LoginActivity;
import com.lt.paotui.activity.MyinfoActivity;
import com.lt.paotui.activity.OrderListActivity;
import com.lt.paotui.activity.OrderptListActivity;
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
    @BindView(R.id.myinfo)
    RelativeLayout myinfo;
    @BindView(R.id.unlogin_myinfo)
    RelativeLayout unlogin_myinfo;
    @BindView(R.id.logout)
    Button logout;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_my, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        onVisible();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            onVisible();
        }
    }

    protected void onVisible(){
        if((boolean)SPUtils.get(getActivity(),"islogin",false)){
            myinfo.setVisibility(View.VISIBLE);
            logout.setVisibility(View.VISIBLE);
            unlogin_myinfo.setVisibility(View.GONE);
            getUserData();

        }
        else{
            myinfo.setVisibility(View.GONE);
            logout.setVisibility(View.GONE);
            unlogin_myinfo.setVisibility(View.VISIBLE);
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
                    Map userInfo = JSON.parseObject(SPUtils.get(getActivity(),"userinfo","{}").toString());
                    username.setText(userInfo.get("phone").toString());
                    yue.setText(userInfo.get("balance").toString());
                    Glide.with(getActivity()).load(Config.url+userInfo.get("header").toString()).placeholder(R.mipmap.header).error(R.mipmap.header).into(header);
                    break;
                case 1:
                    Toast.makeText(getActivity(), "同步用户信息失败！",Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private void getUserData(){

        Map userInfo = JSON.parseObject(SPUtils.get(getActivity(),"userinfo","{}").toString());
        username.setText(userInfo.get("phone").toString());
        yue.setText(userInfo.get("balance").toString());

        Glide.with(getActivity()).load(Config.url+userInfo.get("header").toString()).placeholder(R.mipmap.header).error(R.mipmap.header).into(header);

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
                    SPUtils.put(getActivity(),"userinfo",resultMap.get("msg"));
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
    @OnClick({R.id.myinfo,R.id.logout,R.id.ptdd,R.id.jcdd,R.id.xggrxx,R.id.qrcodefx,R.id.lyjl,R.id.unlogin_myinfo})
    public void btnClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.myinfo:
                intent.setClass(getActivity(), MyinfoActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.unlogin_myinfo:
                intent.setClass(getActivity(), LoginActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.logout:
                MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity()).builder()
                        .setTitle("确认吗？")
                        .setMsg("即将退出登录")
                        .setPositiveButton("确认", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SPUtils.clear(getActivity());
                                myinfo.setVisibility(View.GONE);
                                logout.setVisibility(View.GONE);
                                unlogin_myinfo.setVisibility(View.VISIBLE);
                                /*Intent intent = new Intent();
                                intent.setClass(getActivity(), LoginActivity.class);
                                getActivity().startActivity(intent);
                                getActivity().finish();*/
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                myAlertDialog.show();

                break;
            case R.id.jcdd:

                if((boolean)SPUtils.get(getActivity(),"islogin",false)){
                    intent.setClass(getActivity(), OrderListActivity.class);
                    getActivity().startActivity(intent);
                }
                else{
                    showUnloginDialog();
                }

                break;
            case R.id.ptdd:
                if((boolean)SPUtils.get(getActivity(),"islogin",false)){
                    intent.setClass(getActivity(), OrderptListActivity.class);
                    getActivity().startActivity(intent);
                }
                else{
                    showUnloginDialog();
                }
                break;
            case R.id.xggrxx:

                if((boolean)SPUtils.get(getActivity(),"islogin",false)){
                    intent.setClass(getActivity(), MyinfoActivity.class);
                    getActivity().startActivity(intent);
                }
                else{
                    showUnloginDialog();
                }
                break;
            case R.id.qrcodefx:


                break;
            case R.id.lyjl:

                if((boolean)SPUtils.get(getActivity(),"islogin",false)){
                    intent.setClass(getActivity(), LeavingListActivity.class);
                    getActivity().startActivity(intent);
                }
                else{
                    showUnloginDialog();
                }
                break;


            default:
                break;
        }

    }

    private void showUnloginDialog(){
        MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity()).builder()
                .setTitle("未登录")
                .setMsg("即将前往登录")
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(getActivity(), LoginActivity.class);
                        getActivity().startActivity(intent);
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        myAlertDialog.show();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
