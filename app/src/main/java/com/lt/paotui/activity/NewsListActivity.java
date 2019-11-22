package com.lt.paotui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshViewFooter;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.lt.paotui.R;
import com.lt.paotui.adapter.NewsAdapter;
import com.lt.paotui.adapter.SimpleAdapter;
import com.lt.paotui.utils.Config;
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
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2019/4/12.
 */

public class NewsListActivity extends Activity   {

    @BindView(R.id.top_bar_title)
    TextView top_bar_title;
    @BindView(R.id.recycler_view_test_rv)
    RecyclerView recyclerView;
    @BindView(R.id.xrefreshview)
    XRefreshView xRefreshView;
    @BindView(R.id.addleaving)
    ImageView addleaving;
    NewsAdapter adapter;
    List<Map> listData = new ArrayList<>();
    LinearLayoutManager layoutManager;
    private int mLoadCount = 0;
    private boolean isList = true;//false 为grid布局
    private int page;
    private int size=10;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ButterKnife.bind(this);
        page=1;
        getOrderData(page,size);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String type = intent.getStringExtra("type");

        top_bar_title.setText(title);
        if(type!=null&&(type.equals("1")||type.equals("2")||type.equals("5")
                ||type.equals("6")||type.equals("7")||type.equals("11")
                ||type.equals("21")||type.equals("22")||type.equals("23")
                ||type.equals("24")||type.equals("25")||type.equals("26")||type.equals("27"))){
            addleaving.setVisibility(View.VISIBLE);
        }
        else{
            addleaving.setVisibility(View.GONE);
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
                    initListView();
                    xRefreshView.stopRefresh();
                    xRefreshView.stopLoadMore(true);

                    break;
                case 1:
                    Toast.makeText(NewsListActivity.this, msg.obj.toString(),Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    xRefreshView.setLoadComplete(true);
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private void getOrderData(final int page,int size){
        xRefreshView.startRefresh();
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        String ismain = intent.getStringExtra("ismain");
        final Message message=Message.obtain();

        OkHttpClient okHttpClient = new OkHttpClient();

        FormBody.Builder builder = new FormBody.Builder();
        builder.add("page", page+"");
        builder.add("size", size+"");
        if(type!=null){
            builder.add("type", type);
        }
        if(ismain!=null){
            builder.add("ismain", ismain);
        }
        RequestBody formBody=builder.build();
        Request request = new Request.Builder().url(Config.url+"/listNews")
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
                    Map dataMap= (Map)resultMap.get("msg");
                    int totleNum=Integer.parseInt(dataMap.get("num").toString());
                    List tempData=(List)dataMap.get("data");
                    if(tempData.size()==0){
                        message.what=2;
                        //message.obj=resultMap.get("msg");
                        handler.sendMessage(message);
                    }
                    else{
                        if (page == 1) {
                            listData=tempData;
                        }
                        else{
                            listData.addAll(tempData);
                        }
                        message.what=0;
                        //message.obj=resultMap.get("msg");
                        handler.sendMessage(message);
                    }

                }
                else{
                    message.what=1;
                    message.obj=resultMap.get("msg");
                    handler.sendMessage(message);
                }

            }
        });
    }

    private void initListView(){
        adapter = new NewsAdapter(listData, this);
        // 设置静默加载模式
//        xRefreshView1.setSilenceLoadMore();
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // 静默加载模式不能设置footerview
        recyclerView.setAdapter(adapter);
        //设置刷新完成以后，headerview固定的时间
        xRefreshView.setPinnedTime(1000);
        xRefreshView.setMoveForHorizontal(true);
        xRefreshView.setPullLoadEnable(true);
        xRefreshView.setAutoLoadMore(false);
        adapter.setCustomLoadMoreView(new XRefreshViewFooter(this));
        xRefreshView.enableReleaseToLoadMore(true);
        xRefreshView.enableRecyclerViewPullUp(true);
        xRefreshView.enablePullUpWhenLoadCompleted(true);
        xRefreshView.setHideFooterWhenComplete(true);
        //设置静默加载时提前加载的item个数
//        xefreshView1.setPreLoadCount(4);
        //设置Recyclerview的滑动监听
        xRefreshView.setOnRecyclerViewScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        xRefreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh(boolean isPullDown) {
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        xRefreshView.stopRefresh();
                    }
                }, 500);*/
                page=1;
                getOrderData(page,size);
            }

            @Override
            public void onLoadMore(boolean isSilence) {

                page++;
                getOrderData(page,size);

            }
        });
        // 设置数据后就要给RecyclerView设置点击事件
        adapter.setOnItemClickListener(new NewsAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent();
                intent.setClass(NewsListActivity.this, NewsDetailActivity.class);
                String order_id=listData.get(position).get("id").toString();
                intent.putExtra("news_id", order_id);
                startActivity(intent);
            }
        });
    }
    @OnClick({R.id.top_back_btn,R.id.addleaving})
    public void btnClick(View view) {


        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.addleaving:
                if((boolean)SPUtils.get(NewsListActivity.this,"islogin",false)){
                    Intent intent = getIntent();
                    String title = intent.getStringExtra("title");
                    String type = intent.getStringExtra("type");

                    intent = new Intent();
                    intent.setClass(this, TicketActivity.class);
                    intent.putExtra("type", type);
                    intent.putExtra("title", title+"留言");
                    startActivity(intent);
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
        MyAlertDialog myAlertDialog = new MyAlertDialog(NewsListActivity.this).builder()
                .setTitle("未登录")
                .setMsg("即将前往登录")
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(NewsListActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        myAlertDialog.show();
    }
}
