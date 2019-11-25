package com.lt.paotui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.alibaba.fastjson.JSON;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.lt.paotui.R;
import com.lt.paotui.adapter.LeavingAdapter;
import com.lt.paotui.adapter.MyGoodAdapter;
import com.lt.paotui.utils.Config;
import com.lt.paotui.utils.SPUtils;
import com.lt.paotui.utils.SearchEditText;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/4/16.
 */

public class GoodsSearchActivity extends Activity {
    //private TimeCount time;
    @BindView(R.id.xrecycle)
    XRecyclerView recycler;
    @BindView(R.id.top_bar_title)
    TextView top_bar_title;
    @BindView(R.id.m_search_one)
    SearchEditText mSearchEditTextOne;
    StaggeredGridLayoutManager manager;
    List<Map> list = new ArrayList<>();
    MyGoodAdapter goodAdapter;
    @BindView(R.id.addleaving)
    ImageView addleaving;
    private int page;
    private int size=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载启动界面
        setContentView(R.layout.activity_goods_search_list);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String type = intent.getStringExtra("type");

        top_bar_title.setText(title);
        initListView();
        page=1;
        //getGoodsData(page,size);
        //recycler.refresh();

        /** 搜索框实现 */
        // 实现TextWatcher监听即可
        mSearchEditTextOne.setOnSearchClickListener(new SearchEditText.OnSearchClickListener() {
            @Override
            public void onSearchClick(View view) {
                //Toast.makeText(WugActivity.this, "i'm going to seach"+mSearchEditTextOne.getText(), Toast.LENGTH_SHORT).show();
                getGoodsData(page,size,mSearchEditTextOne.getText().toString());
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
                case 0:
                    if(list.size()>1){
                        manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
                    }

                    recycler.refreshComplete();
                    goodAdapter.notifyDataSetChanged();
                    break;
                case 1:
                    recycler.loadMoreComplete();
                    goodAdapter.notifyDataSetChanged();
                    break;

                case 2:
                    recycler.refreshComplete();
                    recycler.loadMoreComplete();
                    Toast.makeText(GoodsSearchActivity.this, msg.obj.toString(),Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    recycler.refreshComplete();
                    Toast.makeText(GoodsSearchActivity.this,"暂无更多数据！",Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private void getGoodsData(final int page,int size,String searchText){

        ///Map userInfo = JSON.parseObject(SPUtils.get(GoodsActivity.this,"userinfo","{}").toString());
        //final  String cus_id=userInfo.get("id").toString();

        final Message message=Message.obtain();
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        String subtype = intent.getStringExtra("subtype");
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody =null;
        if(type.equals("1")){
            formBody = new FormBody.Builder()
                    .add("page", page+"")
                    .add("size", size+"")
                    .add("type", type)
                    .build();
        }
        else if(type.equals("2")){
            formBody = new FormBody.Builder()
                    .add("page", page+"")
                    .add("size", size+"")
                    .add("type", type)
                    .add("subtype", subtype)
                    .build();
        }
        else if(type.equals("3")){
            formBody = new FormBody.Builder()
                    .add("page", page+"")
                    .add("size", size+"")
                    .add("type", type)
                    .build();
        }
        else if(type.equals("4")){
            formBody = new FormBody.Builder()
                    .add("page", page+"")
                    .add("size", size+"")
                    .add("subtype", subtype)
                    .add("type", type)
                    .build();
        }
        else if(type.equals("5")){
            formBody = new FormBody.Builder()
                    .add("page", page+"")
                    .add("size", size+"")
                    .add("type", type)
                    .add("subtype", subtype)
                    .build();
        }
        else if(type.equals("6")){
            formBody = new FormBody.Builder()
                    .add("page", page+"")
                    .add("size", size+"")
                    .add("type", type)
                    .add("subtype", subtype)
                    .build();
        }
        Request request = new Request.Builder().url(Config.url+"/listGoods")
                .addHeader("source", Config.REQUEST_HEADER)// 自定义的header
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: 17-1-4  请求失败
                message.what=2;
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
                        message.what=3;
                        //message.obj=resultMap.get("msg");
                        handler.sendMessage(message);
                    }
                    else{
                        if (page == 1) {
                            message.what=0;
                            list.addAll(tempData);
                        }
                        else{
                            message.what=1;
                            list.addAll(tempData);
                        }
                        //message.obj=resultMap.get("msg");
                        handler.sendMessage(message);
                    }

                }
                else{
                    message.what=2;
                    message.obj=resultMap.get("msg");
                    handler.sendMessage(message);
                }

            }
        });
    }
    private void initListView(){



        //recycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        //recycler.setAdapter(new StaggeredGridAdapter(PuRecyclerViewActivity.this));


        //RecyclerView.LayoutManager manager = new GridLayoutManager(this, 2);
        //recycler.setLayoutManager(manager);


        //final StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        //manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);

        manager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);


        final Intent intent = getIntent();
        final String type = intent.getStringExtra("type");
        final String title = intent.getStringExtra("title");
        if(type.equals("1")){
            //       这是一个线性布局
            LinearLayoutManager linearManager = new LinearLayoutManager(this);
            recycler.setLayoutManager(linearManager);
            goodAdapter = new MyGoodAdapter(list, this, 1);

        }
        else if(type.equals("2")){


            //RecyclerView.LayoutManager manager = new GridLayoutManager(this, 2);
            recycler.setLayoutManager(manager);

            goodAdapter = new MyGoodAdapter(list, this, 2);

        }
        else if(type.equals("3")){

            recycler.setLayoutManager(manager);
            goodAdapter = new MyGoodAdapter(list, this, 2);
        }
        else if(type.equals("4")){

            recycler.setLayoutManager(manager);
            goodAdapter = new MyGoodAdapter(list, this, 2);
        }
        else if(type.equals("5")){

            recycler.setLayoutManager(manager);
            goodAdapter = new MyGoodAdapter(list, this, 3);
        }
        else if(type.equals("6")){

            recycler.setLayoutManager(manager);
            goodAdapter = new MyGoodAdapter(list, this, 3);
        }
        recycler.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recycler.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        //recycler.setArrowImageView(R.drawable.ic_launcher_background);
        recycler.setAdapter(goodAdapter);

        goodAdapter.setOnItemClickListener(new LeavingAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if(type.equals("5")){
                    Intent intent = new Intent();
                    intent.setClass(GoodsSearchActivity.this, Goods5GDetailActivity.class);
                    String goods_id=list.get(position).get("id").toString();
                    intent.putExtra("goods_id", goods_id);
                    intent.putExtra("title", title);
                    startActivity(intent);
                }
                else if(type.equals("6")){
                    Intent intent = new Intent();
                    intent.setClass(GoodsSearchActivity.this, Goods5GDetailActivity.class);
                    String goods_id=list.get(position).get("id").toString();
                    intent.putExtra("goods_id", goods_id);
                    intent.putExtra("title", title);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent();
                    intent.setClass(GoodsSearchActivity.this, GoodsDetailActivity.class);
                    String goods_id=list.get(position).get("id").toString();
                    intent.putExtra("goods_id", goods_id);
                    intent.putExtra("title", title);
                    startActivity(intent);
                }


                //Toast.makeText(GoodsActivity.this,"正在开发中。。。！",Toast.LENGTH_LONG).show();
            }
        });
//        设置上拉刷新，下拉加载
        recycler.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
//                刷新
                list.clear();
                goodAdapter.notifyDataSetChanged();
                page=1;
                getGoodsData(page,size,mSearchEditTextOne.getText().toString());
                //recycler.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                page++;
                getGoodsData(page,size,mSearchEditTextOne.getText().toString());
                //recycler.loadMoreComplete();
            }
        });
        recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                manager.invalidateSpanAssignments();//这行主要解决了当加载更多数据时，底部需要重绘，否则布局可能衔接不上。
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
                if((boolean)SPUtils.get(GoodsSearchActivity.this,"islogin",false)){
                    Intent intent = getIntent();
                    String title = intent.getStringExtra("title");
                    String type = intent.getStringExtra("type");
                    String subtype = intent.getStringExtra("subtype");
                    intent = new Intent();
                    intent.setClass(this, TicketActivity.class);
                    intent.putExtra("type", subtype);
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
        MyAlertDialog myAlertDialog = new MyAlertDialog(GoodsSearchActivity.this).builder()
                .setTitle("未登录")
                .setMsg("即将前往登录")
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(GoodsSearchActivity.this, LoginActivity.class);
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
