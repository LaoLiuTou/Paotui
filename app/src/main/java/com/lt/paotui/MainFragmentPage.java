package com.lt.paotui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.github.shenyuanqing.zxingsimplify.zxing.Activity.CaptureActivity;
import com.hb.dialog.dialog.ConfirmDialog;
import com.hb.dialog.dialog.LoadingDialog;
import com.hb.dialog.myDialog.ActionSheetDialog;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.hb.dialog.myDialog.MyAlertInputDialog;
import com.lt.paotui.activity.AntugovActivity;
import com.lt.paotui.activity.BannerDetailActivity;
import com.lt.paotui.activity.CskxActivity;
import com.lt.paotui.activity.DxyytActivity;
import com.lt.paotui.activity.JlattvActivity;
import com.lt.paotui.activity.LoginActivity;
import com.lt.paotui.activity.MyinfoActivity;
import com.lt.paotui.activity.NewsDetailActivity;
import com.lt.paotui.activity.NewsListActivity;
import com.lt.paotui.activity.OrderDetailActivity;
import com.lt.paotui.activity.OrderListActivity;
import com.lt.paotui.activity.PaotuiActivity;
import com.lt.paotui.activity.RaffleActivity;
import com.lt.paotui.activity.TicketActivity;
import com.lt.paotui.adapter.MyRVAdapter;
import com.lt.paotui.utils.Config;
import com.lt.paotui.utils.Constant;
import com.lt.paotui.utils.SPUtils;
import com.lt.paotui.utils.SystemUtil;
import com.lt.paotui.utils.UpdateUtil;
import com.lt.paotui.utils.rollingtextview.RollingTextAdapter;
import com.lt.paotui.utils.rollingtextview.view.RollTextItem;
import com.lt.paotui.utils.rollingtextview.view.TextViewSwitcher;
import com.lt.paotui.utils.update.CommonUtil;
import com.lt.paotui.utils.update.UpdateApk;
import com.lt.paotui.utils.update.UpdateBean;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
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

import static android.app.Activity.RESULT_OK;

/**
 * Created by Administrator on 2019/4/2.
 */

public class MainFragmentPage extends Fragment implements OnBannerListener {
    private ProgressDialog progressDialog;
    private Unbinder unbinder;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.bottombanner)
    Banner bottombanner;
    //@BindView(R.id.sroll_text)
    //TextView sroll_text;
    @BindView(R.id.top_bar_title)
    TextView top_bar_title;
    @BindView(R.id.month)
    TextView month;
    @BindView(R.id.day)
    TextView day;
    @BindView(R.id.rolltext)
    TextViewSwitcher rollingText;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.fourthmenus)
    LinearLayout fourthmenus;
    private List<RollTextItem> data = new ArrayList<>();

    private ArrayList<String> list_path;
    private ArrayList<String> list_title;
    private ArrayList<String> list_id;
    private ArrayList<String> bottom_list_path;
    private ArrayList<String> bottom_list_title;
    private ArrayList<String> bottom_list_id;
    UpdateBean updateBean = new UpdateBean();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_main, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            getTitleData();
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //sroll_text.setText("2019-03-28 共有1000用户  用户***，支付车费5元，成功立减2元。");
       /* Drawable icon = getResources().getDrawable(R.mipmap.tongzhi2);
        icon.setBounds(10,0,70,60);
        top_bar_title.setCompoundDrawables(icon, null, null, null);*/


        Calendar cd = Calendar.getInstance();
        month.setText(cd.get(Calendar.MONTH)+1+"月");
        day.setText(cd.get(Calendar.DATE)+"");

        getTitleData();
        getBannerData();
        getNewsData();
        getOrdersData();
        checkVersion();
        getBottomBannerData();
        if(!(boolean)SPUtils.get(getActivity(),"isinstall",false)){
            sendSysInfo();
        }



    }

    private void sendSysInfo(){

        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("devicebrand", SystemUtil.getDeviceBrand())
                .add("systemmodel", SystemUtil.getSystemModel())
                .add("systemversion", SystemUtil.getSystemVersion())
                //.add("imei", SystemUtil.getIMEI(getApplicationContext()))
                .build();
        Request request = new Request.Builder().url(Config.url+"/addInstallinfo")
                .addHeader("source", Config.REQUEST_HEADER)// 自定义的header
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: 17-1-4  请求失败

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO: 17-1-4 请求成功
                Map resultMap = JSON.parseObject(response.body().string());
                if(resultMap.get("status").equals("0")){
                    SPUtils.put(getActivity(),"isinstall",true);
                }
                else{

                }
            }
        });
    }
    private void checkVersion(){
        final Message message=Message.obtain();
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .build();
        Request request = new Request.Builder().url(Config.url+"/version/version.json")
                .addHeader("source", Config.REQUEST_HEADER)// 自定义的header
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String versionStr=response.body().string();
                message.what=8;
                message.obj=versionStr;
                handler.sendMessage(message);

            }
        });
    }
    /**
     * 接收解析后传过来的数据
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //Object model = (Object) msg.obj;
            switch (msg.what){
                case 0://轮播图成功
                    initBanner();
                    break;
                case 1://轮播图失败
                    Toast.makeText(getActivity(),"获取主页轮播图失败!!!",Toast.LENGTH_LONG).show();
                    break;
                case 2://标题成功
                    SPUtils.put(getActivity(),"sysconfig",msg.obj.toString());
                    List<Map> dataList=JSON.parseArray(SPUtils.get(getActivity(),"sysconfig","{}").toString(),Map.class);
                    for(Map<String,String> temp:dataList){
                        if(temp.get("property")!=null&&temp.get("property").equals("title")){
                            top_bar_title.setText(temp.get("value"));
                        }
                        /*else if(temp.get("property")!=null&&temp.get("property").equals("roll")){
                            sroll_text.setText(temp.get("value"));
                        }*/

                    }
                    break;
                case 3://标题失败
                    Toast.makeText(getActivity(),"获取标题失败!!!",Toast.LENGTH_LONG).show();
                    break;

                case 5://标题失败
                    Toast.makeText(getActivity(),"获取图片新闻失败!!!",Toast.LENGTH_LONG).show();
                    break;
                case 6://订单成功

                    Intent intent = new Intent();
                    intent.setClass(getActivity(), OrderDetailActivity.class);
                    intent.putExtra("order_id", msg.obj.toString());
                    startActivity(intent);
                    break;
                case 7://订单失败
                    Toast.makeText(getActivity(),msg.obj.toString(),Toast.LENGTH_LONG).show();
                    break;
                case 8:////更新
                    String versionStr=msg.obj.toString();
                    Map resultMap = JSON.parseObject(versionStr);
                    updateBean.setMessage(resultMap.get("message").toString());
                    updateBean.setTitle("立即更新");
                    updateBean.setUrl(resultMap.get("url").toString());
                    updateBean.setVersionCode(Integer.parseInt(resultMap.get("versionCode").toString()));
                    updateBean.setVersionName(resultMap.get("version").toString());
                    UpdateApk.UpdateVersion(getActivity(), updateBean);
                    break;
                case 9://拨打电话记录
                    callPhone(msg.obj.toString());
                    break;
                case 10://拨打电话记录
                    Toast.makeText(getActivity(),"系统忙，请重试！",Toast.LENGTH_LONG).show();
                    break;
                case 11://滚动订单
                    rollingText.setAdapter(new RollingTextAdapter() {
                        @Override
                        public int getCount() {
                            return data.size()/2;
                        }
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public View getView(Context context, View contentView, int position) {
                            View view = View.inflate(context,R.layout.item_layout,null);
                            ((TextView)view.findViewById(R.id.tv_1)).setText(data.get(position).getMsg());
                            return view;
                        }
                    });
                    rollingText.startFlipping();
                    break;
                case 12://滚动订单
                    Toast.makeText(getActivity(),"系统忙，请重试！",Toast.LENGTH_LONG).show();
                    break;
                case 13://资讯

                    final List<Map> temp=(List<Map> )msg.obj;
                    if(temp.size()>0){
                        MyRVAdapter adapter = new MyRVAdapter(temp,getActivity());
                        rv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));//设置布局管理器
                        rv.setAdapter(adapter);
                        // 设置数据后就要给RecyclerView设置点击事件
                        adapter.setOnItemClickListener(new MyRVAdapter.ItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Intent intent = new Intent();
                                intent.setClass(getActivity(), NewsDetailActivity.class);
                                String order_id=temp.get(position).get("id").toString();
                                intent.putExtra("news_id", order_id);
                                startActivity(intent);
                            }
                        });
                    }
                    else{
                        fourthmenus.setVisibility(View.GONE);
                    }


                    break;
                case 14://资讯
                    Toast.makeText(getActivity(),"系统忙，请重试！",Toast.LENGTH_LONG).show();
                    break;
                case 15://轮播图成功
                    initBottomBanner();
                    break;
                case 16://轮播图失败
                    Toast.makeText(getActivity(),"获取主页轮播图失败!!!",Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };



    private void getTitleData(){
        final Message message=Message.obtain();

        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .build();
        Request request = new Request.Builder().url(Config.url+"/allConfigure")
                .addHeader("source", Config.REQUEST_HEADER)// 自定义的header
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: 17-1-4  请求失败
                message.what=3;
                //message.obj=data;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO: 17-1-4 请求成功
                Map resultMap = JSON.parseObject(response.body().string());
                if(resultMap.get("status").equals("0")){
                    //List<Map<String,String>> dataList=(List<Map<String,String>>)resultMap.get("msg");
                    /*for(Map<String,String> temp :dataList){
                        list_path.add(temp.get("image"));
                        list_title.add(temp.get("title"));
                    }*/
                    message.what=2;
                    message.obj=resultMap.get("msg");
                    handler.sendMessage(message);
                }
                else{
                    message.what=3;
                    handler.sendMessage(message);
                }


            }
        });
    }
    private void getOrdersData(){
        final Message message=Message.obtain();

        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("page", "1")
                .add("size", "10")
                .build();
        Request request = new Request.Builder().url(Config.url+"/listOrders")
                .addHeader("source", Config.REQUEST_HEADER)// 自定义的header
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: 17-1-4  请求失败
                message.what=12;
                //message.obj=data;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO: 17-1-4 请求成功
                Map resultMap = JSON.parseObject(response.body().string());
                if(resultMap.get("status").equals("0")){
                    Map dataMap= (Map)resultMap.get("msg");
                    List<Map> dataList=(List<Map>)dataMap.get("data");
                    for(Map temp :dataList){
                        String time= temp.get("pay_dt").toString();
                        String price= temp.get("price").toString();
                        String phone= temp.get("phone").toString();
                        if(phone.length()>7){
                            phone = phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
                        }
                        String message=time+" 用户"+phone+"使用代金券"+price+"元支付打车费";
                        data.add(new RollTextItem(message));
                    }
                    message.what=11;
                    handler.sendMessage(message);
                }
                else{
                    message.what=12;
                    handler.sendMessage(message);
                }


            }
        });
    }
    private void getNewsData(){

        final Message message=Message.obtain();

        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("page", "1")
                .add("size", "3")
                .add("ismain", "1")
                .build();
        Request request = new Request.Builder().url(Config.url+"/listNews")
                .addHeader("source", Config.REQUEST_HEADER)// 自定义的header
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: 17-1-4  请求失败
                message.what=14;
                message.obj="请求失败！";
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO: 17-1-4 请求成功
                Map resultMap = JSON.parseObject(response.body().string());
                if(resultMap.get("status").equals("0")){
                    Map dataMap= (Map)resultMap.get("msg");
                    int totleNum=Integer.parseInt(dataMap.get("num").toString());
                    List tempData=(List)dataMap.get("data");

                    message.what=13;
                    message.obj=tempData;
                    handler.sendMessage(message);

                }
                else{
                    message.what=14;
                    message.obj=resultMap.get("msg");
                    handler.sendMessage(message);
                }

            }
        });
    }
    private void getBannerData(){
        final Message message=Message.obtain();
        //放图片地址的集合
        list_path = new ArrayList<>();
        //放标题的集合
        list_title = new ArrayList<>();
        list_id = new ArrayList<>();

        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("page", "1")
                .add("size", "4")
                .add("type", "0")
                .add("state", "0")
                .build();
        Request request = new Request.Builder().url(Config.url+"/listBanner")
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
                    Map dataMap= (Map)resultMap.get("msg");
                    int totleNum=Integer.parseInt(dataMap.get("num").toString());
                    List<Map> dataList=(List<Map>)dataMap.get("data");
                    for(Map temp :dataList){
                        list_path.add(Config.url+temp.get("image").toString());
                        list_title.add(temp.get("title").toString());
                        list_id.add(temp.get("id").toString());
                    }
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
    private void getBottomBannerData(){
        final Message message=Message.obtain();
        //放图片地址的集合
        bottom_list_path = new ArrayList<>();
        //放标题的集合
        bottom_list_title = new ArrayList<>();
        bottom_list_id = new ArrayList<>();

        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("page", "1")
                .add("size", "3")
                .add("type", "1")
                .add("state", "0")
                .build();
        Request request = new Request.Builder().url(Config.url+"/listBanner")
                .addHeader("source", Config.REQUEST_HEADER)// 自定义的header
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: 17-1-4  请求失败
                message.what=16;
                //message.obj=data;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO: 17-1-4 请求成功
                Map resultMap = JSON.parseObject(response.body().string());
                if(resultMap.get("status").equals("0")){
                    Map dataMap= (Map)resultMap.get("msg");
                    int totleNum=Integer.parseInt(dataMap.get("num").toString());
                    List<Map> dataList=(List<Map>)dataMap.get("data");
                    for(Map temp :dataList){
                        bottom_list_path.add(Config.url+temp.get("image").toString());
                        bottom_list_title.add(temp.get("title").toString());
                        bottom_list_id.add(temp.get("id").toString());
                    }
                    message.what=15;
                    handler.sendMessage(message);
                }
                else{
                    message.what=16;
                    handler.sendMessage(message);
                }
            }
        });
    }



    private void initBanner() {

        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        //banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器，图片加载器在下方
        banner.setImageLoader(new MyLoader());
        //设置图片网址或地址的集合
        banner.setImages(list_path);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        banner.setBannerTitles(list_title);
        banner.setBannerIdlist(list_id);
        //设置轮播间隔时间
        banner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                .setOnBannerListener(this)
                //必须最后调用的方法，启动轮播图。
                .start();

    }
    private void initBottomBanner() {

        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        //banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        bottombanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器，图片加载器在下方
        bottombanner.setImageLoader(new MyLoader());
        //设置图片网址或地址的集合
        bottombanner.setImages(bottom_list_path);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        bottombanner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        bottombanner.setBannerTitles(bottom_list_title);
        bottombanner.setBannerIdlist(bottom_list_id);
        //设置轮播间隔时间
        bottombanner.setDelayTime(3000);
        //设置是否为自动轮播，默认是“是”。
        bottombanner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        bottombanner.setIndicatorGravity(BannerConfig.CENTER)
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                .setOnBannerListener(this)
                //必须最后调用的方法，启动轮播图。
                .start();


    }
    //轮播图的监听方法
    @Override
    public void OnBannerClick(List<String> ids,int position) {

        Intent intent = new Intent();
        intent.setClass(getActivity(), BannerDetailActivity.class);
        String banner_id=ids.get(position).toString();
        intent.putExtra("banner_id", banner_id);
        startActivity(intent);
    }
    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }


    //监听事件
    @OnClick({R.id.top_left_btn,R.id.first1,R.id.first2,R.id.first3,
            R.id.yjjc,R.id.yjjcprev,
            R.id.dxyyt,R.id.yhtc, R.id.sjbj,R.id.esjss,R.id.lhss,
            R.id.cskx,R.id.mflsj,R.id.gp,
            R.id.news1,R.id.news2,R.id.news3,R.id.jlattv})//多个控件可以一起发在里面进行监听
    public void btnClick(View view) {
        Intent intent = new Intent();
        String [] phonenumbers={"5059898","5059696"};
        int random=0;
        switch (view.getId()) {
            case R.id.top_left_btn:
                if((boolean)SPUtils.get(getActivity(),"islogin",false)){
                    startQrCode();
                }
                else{
                    showUnloginDialog();
                }
                break;
            case R.id.first1:
                if((boolean)SPUtils.get(getActivity(),"islogin",false)){
                    intent.setClass(getActivity(), PaotuiActivity.class);
                    intent.putExtra("type", "帮我买");
                    startActivity(intent);
                }
                else{
                    showUnloginDialog();
                }

                break;
            case R.id.first2:
                if((boolean)SPUtils.get(getActivity(),"islogin",false)){
                    intent.setClass(getActivity(), PaotuiActivity.class);
                    intent.putExtra("type", "帮我送");
                    startActivity(intent);
                }
                else{
                    showUnloginDialog();
                }

                break;
            case R.id.first3:
                if((boolean)SPUtils.get(getActivity(),"islogin",false)){
                    intent.setClass(getActivity(), PaotuiActivity.class);
                    intent.putExtra("type", "帮我取");
                    startActivity(intent);
                }
                else{
                    showUnloginDialog();
                }

                break;
            case R.id.yjjc:
                if((boolean)SPUtils.get(getActivity(),"islogin",false)){
                    random=((int)(1+Math.random()*(10-1+1)))%2;
                    showAlterDialog("一键叫车",phonenumbers[random]);
                }
                else{
                    showUnloginDialog();
                }

                break;
            case R.id.yjjcprev:
                if((boolean)SPUtils.get(getActivity(),"islogin",false)){
                    random=((int)(1+Math.random()*(10-1+1)))%2;
                    showAlterDialog("一键叫车",phonenumbers[random]);
                }
                else{
                    showUnloginDialog();
                }

                break;

            case R.id.dxyyt:
                intent.putExtra("type", "8");
                intent.putExtra("title", "电信营业厅");
                intent.setClass(getActivity(), DxyytActivity.class);
                startActivity(intent);
                break;
            case R.id.yhtc:
                intent.setClass(getActivity(), NewsListActivity.class);
                intent.putExtra("type", "5");
                intent.putExtra("title", "优惠套餐");
                startActivity(intent);
                break;
            case R.id.sjbj:
                intent.setClass(getActivity(), NewsListActivity.class);
                intent.putExtra("type", "6");
                intent.putExtra("title", "手机报价");
                startActivity(intent);
                break;
            case R.id.esjss:
                intent.setClass(getActivity(), NewsListActivity.class);
                intent.putExtra("type", "7");
                intent.putExtra("title", "二手机收售");
                startActivity(intent);
                break;
            case R.id.lhss:
                intent.setClass(getActivity(), NewsListActivity.class);
                intent.putExtra("type", "11");
                intent.putExtra("title", "靓号收集");
                startActivity(intent);
                break;
            case R.id.cskx:
                intent.setClass(getActivity(), CskxActivity.class);
                startActivity(intent);
                break;
            case R.id.mflsj:
                intent.setClass(getActivity(), NewsListActivity.class);
                intent.putExtra("type", "2");
                intent.putExtra("title", "免费领手机");
                startActivity(intent);
                break;
            case R.id.gp:
                if((boolean)SPUtils.get(getActivity(),"islogin",false)){
                    intent.setClass(getActivity(), TicketActivity.class);
                    intent.putExtra("type", "9");
                    intent.putExtra("title", "购票留言");
                    startActivity(intent);
                }
                else{
                    showUnloginDialog();
                }

                break;
            case R.id.news1:
                intent.setClass(getActivity(), NewsListActivity.class);
                intent.putExtra("type", "1");
                intent.putExtra("title", "电信资讯");
                startActivity(intent);
                break;
            case R.id.news2:
                if((boolean)SPUtils.get(getActivity(),"islogin",false)){
                    intent.setClass(getActivity(), RaffleActivity.class);
                    startActivity(intent);
                }
                else{
                    showUnloginDialog();
                }

                break;
            case R.id.news3:
                intent.setClass(getActivity(), AntugovActivity.class);
                startActivity(intent);
                break;
            case R.id.jlattv:
                intent.setClass(getActivity(), JlattvActivity.class);
                startActivity(intent);
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
    private void showAlterDialog(final String type,final String phonenum){
        MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity()).builder()
                .setTitle("确认吗？")
                .setMsg("即将拨打服务电话")
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addRecordData(type,phonenum);
                        //callPhone(phonenum);
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        myAlertDialog.show();

    }

    /**
     * 拨打电话（直接拨打电话）
     * @param phoneNum 电话号码
     */
    public void callPhone(String phoneNum){
        /*Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);*/
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    private void addRecordData(String type,final String phone){
        Map userInfo = JSON.parseObject(SPUtils.get(getActivity(),"userinfo","{}").toString());
        final Message message=Message.obtain();

        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("cus_id", userInfo.get("id").toString())
                .add("type", type)
                .add("phone", phone)
                .build();
        Request request = new Request.Builder().url(Config.url+"/addDialrecord")
                .addHeader("source", Config.REQUEST_HEADER)// 自定义的header
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: 17-1-4  请求失败
                message.what=10;
                //message.obj=data;
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO: 17-1-4 请求成功
                Map resultMap = JSON.parseObject(response.body().string());
                if(resultMap.get("status").equals("0")){
                    message.what=9;
                    message.obj=phone;
                    handler.sendMessage(message);
                }
                else{
                    message.what=10;
                    handler.sendMessage(message);
                }
            }
        });
    }

    // 开始扫码
    private void startQrCode() {
        // 申请相机权限
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, Constant.REQ_PERM_CAMERA);
            return;
        }
        // 申请文件读写权限（部分朋友遇到相册选图需要读写权限的情况，这里一并写一下）
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 申请权限
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, Constant.REQ_PERM_EXTERNAL_STORAGE);
            return;
        }
        // 二维码扫码
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        startActivityForResult(intent, Constant.REQ_QR_CODE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == Constant.REQ_QR_CODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            //String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            //Toast.makeText(MainActivity.this, data.getStringExtra("barCode"), Toast.LENGTH_LONG).show();
            Map resultMap = JSON.parseObject(data.getStringExtra("barCode"));
            String id="";
            if(resultMap.containsKey("id")&&resultMap.containsKey("number")&&resultMap.containsKey("driver")){
                id=resultMap.get("id").toString();
                //将扫描出的信息显示出来
                showInputDialog(id);

            }
            else{
                Toast.makeText(getActivity(), "无效的扫描结果！", Toast.LENGTH_LONG).show();
            }


        }
    }
    private void showInputDialog(final String id) {
        final Map userInfo = JSON.parseObject(SPUtils.get(getActivity(),"userinfo","{}").toString());
        List<Map> dataList=JSON.parseArray(SPUtils.get(getActivity(),"sysconfig","{}").toString(),Map.class);
        String tempprice="1";
        String student="0";
        for(Map<String,String> temp:dataList){
            if(temp.get("property")!=null&&temp.get("property").equals("price")){
                tempprice=temp.get("value");
            }
            if(temp.get("property")!=null&&temp.get("property").equals("student")){
                student=temp.get("value");
            }
        }
        final String price=tempprice;
        ActionSheetDialog dialog = new ActionSheetDialog(getActivity()).builder().setTitle("支付提示")
                .addSheetItem("代金券支付", null, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {

                        MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity()).builder()
                                .setTitle("确认吗？")
                                .setMsg("即将使用"+price+"元代金券"+"\n"+"可用代金券："+userInfo.get("balance").toString())
                                .setPositiveButton("确认", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Map userInfo = JSON.parseObject(SPUtils.get(getActivity(),"userinfo","{}").toString());
                                        String cus_id=userInfo.get("id").toString();
                                        addOrder(cus_id,price,id,"代金券支付");
                                    }
                                }).setNegativeButton("取消", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                });
                        myAlertDialog.show();
                    }
                });

        if(student.equals("1")){
            dialog.addSheetItem("考生免费乘车", null, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {

                        MyAlertDialog myAlertDialog = new MyAlertDialog(getActivity()).builder()
                                .setTitle("确认吗？")
                                .setMsg("此功能只适用于考生，请确认")
                                .setPositiveButton("确认", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Map userInfo = JSON.parseObject(SPUtils.get(getActivity(),"userinfo","{}").toString());
                                        String cus_id=userInfo.get("id").toString();
                                        addOrder(cus_id,"0",id,"考生免单");
                                    }
                                }).setNegativeButton("取消", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                    }
                                });
                        myAlertDialog.show();

                    }
            });
        }
        dialog.show();

        /*final MyAlertInputDialog myAlertInputDialog = new MyAlertInputDialog(getActivity()).builder()
                .setTitle("请输入要支付的金额"+"\n"+"可用代金券："+userInfo.get("balance").toString())
                .setEditText("");
        myAlertInputDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CommonUtil.isMoney(myAlertInputDialog.getResult())){
                    Map userInfo = JSON.parseObject(SPUtils.get(getActivity(),"userinfo","{}").toString());
                    String cus_id=userInfo.get("id").toString();
                    addOrder(cus_id,myAlertInputDialog.getResult(),driver);
                    myAlertInputDialog.dismiss();
                }
                else{
                    Toast.makeText(getActivity(), "无效的金额！", Toast.LENGTH_LONG).show();
                }


            }
        }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myAlertInputDialog.dismiss();
            }
        });
        myAlertInputDialog.show();*/


    }
    private void addOrder(String cus_id,String price,String id,String note){
        final LoadingDialog loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.setMessage("正在提交...");
        loadingDialog.show();
        final Message message=Message.obtain();
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("cus_id", cus_id)
                .add("price", price)
                .add("driver", id)
                .add("note", note)
                .add("status", "1")
                .build();
        Request request = new Request.Builder().url(Config.url+"/addOrders")
                .addHeader("source", Config.REQUEST_HEADER)// 自定义的header
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: 17-1-4  请求失败
                loadingDialog.dismiss();
                message.what=7;
                message.obj="提交订单失败！";
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO: 17-1-4 请求成功
                loadingDialog.dismiss();
                Map resultMap = JSON.parseObject(response.body().string());
                if(resultMap.get("status").equals("0")){
                    message.what=6;
                    message.obj=resultMap.get("msg").toString();
                    handler.sendMessage(message);
                }
                else{
                    message.what=7;
                    message.obj=resultMap.get("msg").toString();
                    handler.sendMessage(message);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1000:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    UpdateApk.UpdateVersion(getActivity(), updateBean);
                } else {
                    Toast.makeText(getActivity(), "请在应用管理中打开“存储权限”访问权限,否则无法正常使用！", Toast.LENGTH_SHORT).show();
                }
                break;
            case Constant.REQ_PERM_CAMERA:
                // 摄像头权限申请
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获得授权
                    startQrCode();
                } else {
                    // 被禁止授权
                    Toast.makeText(getActivity(), "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show();
                }
                break;
            case Constant.REQ_PERM_EXTERNAL_STORAGE:
                // 文件读写权限申请
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获得授权
                    startQrCode();
                } else {
                    // 被禁止授权
                    Toast.makeText(getActivity(), "请至权限中心打开本应用的文件读写权限", Toast.LENGTH_LONG).show();
                }
                break;

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
