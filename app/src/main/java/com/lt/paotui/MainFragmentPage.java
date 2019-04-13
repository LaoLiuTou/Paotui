package com.lt.paotui;

import android.Manifest;
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
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.github.shenyuanqing.zxingsimplify.zxing.Activity.CaptureActivity;
import com.hb.dialog.dialog.ConfirmDialog;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.hb.dialog.myDialog.MyAlertInputDialog;
import com.lt.paotui.activity.LoginActivity;
import com.lt.paotui.activity.NewsListActivity;
import com.lt.paotui.activity.OrderDetailActivity;
import com.lt.paotui.activity.OrderListActivity;
import com.lt.paotui.utils.Config;
import com.lt.paotui.utils.Constant;
import com.lt.paotui.utils.SPUtils;
import com.lt.paotui.utils.UpdateUtil;
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
    @BindView(R.id.sroll_text)
    TextView sroll_text;
    @BindView(R.id.top_bar_title)
    TextView top_bar_title;

    @BindView(R.id.news1)
    ImageView news1;
    @BindView(R.id.news2)
    ImageView news2;

    private ArrayList<String> list_path;
    private ArrayList<String> list_title;
    UpdateBean updateBean = new UpdateBean();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout_main, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //sroll_text.setText("2019-03-28 共有1000用户  用户***，支付车费5元，成功立减2元。");
        Drawable icon = getResources().getDrawable(R.mipmap.tongzhi2);
        icon.setBounds(10,0,70,60);
        top_bar_title.setCompoundDrawables(icon, null, null, null);
        //top_bar_title.setText("  打车支付     立减2元  ");
        getTitleData();
        getBannerData();
        //getNewsData();

        checkVersion();
    }
    private void checkVersion(){

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
                Map resultMap = JSON.parseObject(versionStr);
                updateBean.setMessage(resultMap.get("message").toString());
                updateBean.setTitle("立即更新");
                updateBean.setUrl(resultMap.get("url").toString());
                updateBean.setVersionCode(Integer.parseInt(resultMap.get("versionCode").toString()));
                updateBean.setVersionName(resultMap.get("version").toString());
                UpdateApk.UpdateVersion(getContext(), updateBean);
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
                    List<Map<String,String>> dataList=(List<Map<String,String>>)msg.obj;
                    for(Map<String,String> temp:dataList){
                        if(temp.get("property")!=null&&temp.get("property").equals("title")){
                            top_bar_title.setText(temp.get("value"));
                        }
                        else if(temp.get("property")!=null&&temp.get("property").equals("roll")){
                            sroll_text.setText(temp.get("value"));
                        }

                    }
                    break;
                case 3://标题失败
                    Toast.makeText(getActivity(),"获取标题失败!!!",Toast.LENGTH_LONG).show();
                    break;
                case 4://标题成功

                    List<Map<String,String>> newsDataList=(List<Map<String,String>>)msg.obj;
                    for(int index=0;index<newsDataList.size();index++){
                        String str=Config.url+newsDataList.get(index).get("image");
                        if(index==0){
                            Glide.with(getContext()).load(Config.url+newsDataList.get(index).get("image")).into(news1);
                        }
                        else if(index==1){
                            Glide.with(getContext()).load(Config.url+newsDataList.get(index).get("image")).into(news2);
                        }

                    }

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
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private void getTitleData(){
        final Message message=Message.obtain();
        //放图片地址的集合
        list_path = new ArrayList<>();
        //放标题的集合
        list_title = new ArrayList<>();

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
                    List<Map<String,String>> dataList=(List<Map<String,String>>)resultMap.get("msg");
                    /*for(Map<String,String> temp :dataList){
                        list_path.add(temp.get("image"));
                        list_title.add(temp.get("title"));
                    }*/
                    message.what=2;
                    message.obj=dataList;
                    handler.sendMessage(message);
                }
                else{
                    message.what=3;
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

        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("page", "1")
                .add("size", "4")
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
                    List<Map<String,String>> dataList=(List<Map<String,String>>)dataMap.get("data");
                    for(Map<String,String> temp :dataList){
                        list_path.add(Config.url+temp.get("image"));
                        list_title.add(temp.get("title"));
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

    private void getNewsData(){
        final Message message=Message.obtain();

        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("page", "1")
                .add("size", "2")
                .build();
        Request request = new Request.Builder().url(Config.url+"/listNews")
                .addHeader("source", Config.REQUEST_HEADER)// 自定义的header
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: 17-1-4  请求失败
                message.what=5;
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
                    List<Map<String,String>> dataList=(List<Map<String,String>>)dataMap.get("data");
                    message.what=4;
                    message.obj=dataList;
                    handler.sendMessage(message);
                }
                else{
                    message.what=5;
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
    public void OnBannerClick(int position) {
        Log.i("tag", "你点了第"+position+"张轮播图");
    }
    //自定义的图片加载器
    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }


    //监听事件
    @OnClick({R.id.top_left_btn,R.id.first1,R.id.first2,R.id.first3,R.id.second1,R.id.second2,R.id.second3,R.id.third1,R.id.third2,R.id.third3,R.id.news1,R.id.news2})//多个控件可以一起发在里面进行监听
    public void btnClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.top_left_btn:
                startQrCode();
                break;
            case R.id.first1:
                showAlterDialog();
                break;
            case R.id.first2:
                showAlterDialog();
                break;
            case R.id.first3:
                showAlterDialog();
                break;
            case R.id.second1:
                showAlterDialog();
                break;
            case R.id.second2:
                showAlterDialog();
                break;
            case R.id.second3:
                showAlterDialog();
                break;
            case R.id.news1:

                intent.setClass(getActivity(), NewsListActivity.class);
                intent.putExtra("type", "1");
                startActivity(intent);
                break;
            case R.id.news2:
                intent.setClass(getActivity(), NewsListActivity.class);
                intent.putExtra("type", "2");
                startActivity(intent);
                break;

            default:
                break;
        }

    }
    private void showAlterDialog(){
        MyAlertDialog myAlertDialog = new MyAlertDialog(getContext()).builder()
                .setTitle("确认吗？")
                .setMsg("即将拨打服务电话")
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callPhone("5059898");
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
            String driver="";
            if(resultMap.containsKey("driver")){
                driver=resultMap.get("driver").toString();
                //将扫描出的信息显示出来
                showInputDialog(driver);

            }
            else{
                Toast.makeText(getActivity(), "无效的扫描结果！", Toast.LENGTH_LONG).show();
            }


        }
    }
    private void showInputDialog(final String driver) {
        final MyAlertInputDialog myAlertInputDialog = new MyAlertInputDialog(getContext()).builder()
                .setTitle("请输入要支付的金额")
                .setEditText("");
        myAlertInputDialog.setPositiveButton("确认", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CommonUtil.isMoney(myAlertInputDialog.getResult())){
                    Map userInfo = JSON.parseObject(SPUtils.get(getContext(),"userinfo","{}").toString());
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
        myAlertInputDialog.show();


    }
    private void addOrder(String cus_id,String price,String driver){
        final Message message=Message.obtain();

        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("cus_id", cus_id)
                .add("price", price)
                .add("driver", driver)
                .build();
        Request request = new Request.Builder().url(Config.url+"/addOrders")
                .addHeader("source", Config.REQUEST_HEADER)// 自定义的header
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // TODO: 17-1-4  请求失败
                message.what=7;
                message.obj="提交订单失败！";
                handler.sendMessage(message);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // TODO: 17-1-4 请求成功
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
                    UpdateApk.UpdateVersion(getContext(), updateBean);
                } else {
                    Toast.makeText(getContext(), "请在应用管理中打开“存储权限”访问权限,否则无法正常使用！", Toast.LENGTH_SHORT).show();
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
