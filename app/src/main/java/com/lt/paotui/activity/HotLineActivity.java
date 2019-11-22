package com.lt.paotui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
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
import com.lt.paotui.adapter.HotLineAdapter;
import com.lt.paotui.adapter.LeavingAdapter;
import com.lt.paotui.adapter.MyGoodAdapter;
import com.lt.paotui.adapter.MyRVAdapter;
import com.lt.paotui.utils.Config;
import com.lt.paotui.utils.SPUtils;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2019/4/16.
 */

public class HotLineActivity extends Activity {
    @BindView(R.id.xrecycle)
    RecyclerView recycler;
    @BindView(R.id.top_bar_title)
    TextView top_bar_title;
    List<Map> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载启动界面
        setContentView(R.layout.activity_hotline_list);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");

        top_bar_title.setText(title);
        getGoodsData(1,100);

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

                    GridLayoutManager recyclerViewLayoutManager = new GridLayoutManager(HotLineActivity.this,4);
                    //给recyclerView设置LayoutManager
                    recycler.setLayoutManager(recyclerViewLayoutManager);
                    HotLineAdapter adapter = new HotLineAdapter(list,HotLineActivity.this);
                    //recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));//设置布局管理器
                    recycler.setAdapter(adapter);
                    // 设置数据后就要给RecyclerView设置点击事件
                    adapter.setOnItemClickListener(new HotLineAdapter.ItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            if((boolean) SPUtils.get(HotLineActivity.this,"islogin",false)){
                                showAlterDialog("便民服务热线",list.get(position).get("phone").toString());
                            }
                            else{
                                showUnloginDialog();
                            }
                        }
                    });
                    break;
                case 1:
                    Toast.makeText(HotLineActivity.this,"暂无更多数据！",Toast.LENGTH_LONG).show();
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
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody  = new FormBody.Builder()
                .add("page", page+"")
                .add("size", size+"")
                .add("state", "0")
                .build();


        Request request = new Request.Builder().url(Config.url+"/listHotline")
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
                    List tempData=(List)dataMap.get("data");
                    list.addAll(tempData);
                    message.what=0;
                    //message.obj=resultMap.get("msg");
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


    private void showUnloginDialog(){
        MyAlertDialog myAlertDialog = new MyAlertDialog(this).builder()
                .setTitle("未登录")
                .setMsg("即将前往登录")
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(HotLineActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        myAlertDialog.show();
    }
    private void showAlterDialog(final String type,final String phonenum){
        MyAlertDialog myAlertDialog = new MyAlertDialog(this).builder()
                .setTitle("确认吗？")
                .setMsg("即将拨打服务电话")
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        callPhone(phonenum);
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
