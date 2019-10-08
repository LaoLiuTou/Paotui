package com.lt.paotui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.just.agentweb.AgentWeb;
import com.lt.paotui.R;
import com.lt.paotui.adapter.LeavingAdapter;
import com.lt.paotui.adapter.MyGoodAdapter;
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
import okhttp3.Response;

/**
 * Created by Administrator on 2019/4/16.
 */

public class GoodsActivity extends Activity {
    //private TimeCount time;
    @BindView(R.id.xrecycle)
    XRecyclerView recycler;
    @BindView(R.id.top_bar_title)
    TextView top_bar_title;
    List<Map> list = new ArrayList<>();
    MyGoodAdapter goodAdapter;
    private int page;
    private int size=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载启动界面
        setContentView(R.layout.activity_goods_list);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        //String type = intent.getStringExtra("type");

        top_bar_title.setText(title);
        initListView();
        page=1;
        //getGoodsData(page,size);
        recycler.refresh();
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
                    Toast.makeText(GoodsActivity.this, msg.obj.toString(),Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    recycler.refreshComplete();
                    Toast.makeText(GoodsActivity.this,"暂无更多数据！",Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private void getGoodsData(final int page,int size){

        ///Map userInfo = JSON.parseObject(SPUtils.get(GoodsActivity.this,"userinfo","{}").toString());
        //final  String cus_id=userInfo.get("id").toString();

        final Message message=Message.obtain();
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        String subtype = intent.getStringExtra("subtype");
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody =null;
        if(type.equals("2")){
            formBody = new FormBody.Builder()
                    .add("page", page+"")
                    .add("size", size+"")
                    .add("type", type)
                    .add("subtype", subtype)
                    .build();
        }
        else{
            formBody = new FormBody.Builder()
                    .add("page", page+"")
                    .add("size", size+"")
                    .add("type", type)
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


        //       这是一个线性布局
        //LinearLayoutManager manager = new LinearLayoutManager(this);
        //recycler.setLayoutManager(manager);
        RecyclerView.LayoutManager manager = new GridLayoutManager(this, 2);
        recycler.setLayoutManager(manager);
//        下拉刷新的代码
//        添加的一些样式
        recycler.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        recycler.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        //recycler.setArrowImageView(R.drawable.ic_launcher_background);

        goodAdapter = new MyGoodAdapter(list, this, 2);
        recycler.setAdapter(goodAdapter);

        goodAdapter.setOnItemClickListener(new LeavingAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent();
                intent.setClass(GoodsActivity.this, GoodsDetailActivity.class);
                String goods_id=list.get(position).get("id").toString();
                intent.putExtra("goods_id", goods_id);
                startActivity(intent);

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
                getGoodsData(page,size);
                //recycler.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                page++;
                getGoodsData(page,size);
                //recycler.loadMoreComplete();
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
}
