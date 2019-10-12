package com.lt.paotui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bumptech.glide.Glide;
import com.just.agentweb.AgentWeb;
import com.lt.paotui.MainFragmentPage;
import com.lt.paotui.R;
import com.lt.paotui.adapter.MyGoodAdapter;
import com.lt.paotui.adapter.MyRVAdapter;
import com.lt.paotui.utils.Config;
import com.lt.paotui.utils.SPUtils;
import com.lt.paotui.utils.rollingtextview.RollingTextAdapter;
import com.lt.paotui.utils.update.UpdateApk;
import com.lt.paotui.wxapi.WXPayActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;

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

/**
 * Created by Administrator on 2019/4/12.
 */

public class GoodsDetailActivity extends Activity implements OnBannerListener {
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.pp)
    TextView pptextView;
    @BindView(R.id.xh)
    TextView xhtextView;
    @BindView(R.id.cs)
    TextView cstextView;
    @BindView(R.id.pz)
    TextView pztextView;
    @BindView(R.id.wl)
    TextView wltextView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.content)
    TextView content;

    @BindView(R.id.configLin)
    LinearLayout configLin;
    //@BindView(R.id.container)
    //LinearLayout mLinearLayout;
    @BindView(R.id.top_bar_title)
    TextView top_bar_title;
    //protected AgentWeb mAgentWeb;

    private ArrayList<String> list_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载启动界面
        setContentView(R.layout.activity_goods_details);
        ButterKnife.bind(this);
        top_bar_title.setText("详情");
        getBannerData();
       /* mAgentWeb=AgentWeb.with(GoodsDetailActivity.this)
                .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()//进度条
                //.setAgentWebWebSettings()
                //.setReceivedTitleCallback(mCallback) //设置 Web 页面的 title 回调
                .createAgentWeb()
                .ready()
                .go(Config.url+"pages/app_news_detail.html?id=");
        mAgentWeb.getAgentWebSettings().getWebSettings().setUseWideViewPort(true); //将图片调整到适合webview的大小
        mAgentWeb.getAgentWebSettings().getWebSettings().setLoadWithOverviewMode(true); // 缩放至屏幕的大小*/


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
                    Map dataMap= (Map) msg.obj;
                    String type=dataMap.get("type")+"";
                    if(type.equals("1")){
                        configLin.setVisibility(View.VISIBLE);
                    }
                    else{
                        configLin.setVisibility(View.GONE);
                    }


                    title.setText(dataMap.get("title")+"");
                    pptextView.setText(dataMap.get("brand")+"");
                    xhtextView.setText(dataMap.get("model")+"");
                    cstextView.setText(dataMap.get("oldlevel")+"");
                    pztextView.setText(dataMap.get("configure")+"");
                    wltextView.setText(dataMap.get("network")+"");
                    //CharSequence desc = Html.fromHtml(dataMap.get("content")+"");
                    //content.setText(desc);
                    //在第一次调用RichText之前先调用RichText.initCacheDir()方法设置缓存目录，不设置会报错
                    RichText.initCacheDir(GoodsDetailActivity.this);
                    //这里是取后台返回的集合数据

                    RichText.from(dataMap.get("content")+"").bind(this)
                            .showBorder(false)
                            .size(ImageHolder.MATCH_PARENT, ImageHolder.WRAP_CONTENT)
                            .into(content);

                    break;
                case 1://轮播图失败
                    Toast.makeText(GoodsDetailActivity.this,"获取数据失败!!!",Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };


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
        banner.setBannerTitles(null);
        banner.setBannerIdlist(null);
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
    //轮播图的监听方法
    @Override
    public void OnBannerClick(List<String> ids,int position) {

        /*Intent intent = new Intent();
        intent.setClass(GoodsDetailActivity.this, BannerDetailActivity.class);
        String banner_id=ids.get(position).toString();
        intent.putExtra("banner_id", banner_id);
        startActivity(intent);*/
    }
    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }
    private void getBannerData(){
        final Message message=Message.obtain();
        //放图片地址的集合
        list_path = new ArrayList<>();
        Intent intent = getIntent();
        String goods_id = intent.getStringExtra("goods_id");
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("id", goods_id)
                .build();
        Request request = new Request.Builder().url(Config.url+"/selectGoods")
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
                    JSONArray imageList = JSON.parseArray(dataMap.get("banners")+"");
                    for(int i=0;i<imageList.size();i++){
                        list_path.add(Config.url+imageList.get(i));
                    }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //结束时清空内容
        RichText.clear(this);
    }
}
