package com.lt.paotui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.hb.dialog.dialog.LoadingDialog;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.lt.paotui.MainActivity;
import com.lt.paotui.R;
import com.lt.paotui.utils.Config;
import com.lt.paotui.utils.MD5Util;
import com.lt.paotui.utils.SPUtils;
import com.lt.paotui.utils.datepicker.CustomDatePicker;
import com.lt.paotui.utils.datepicker.DateFormatUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
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

public class PaotuiActivity extends Activity {
    @BindView(R.id.top_bar_title)
    TextView top_bar_title;

    @BindView(R.id.qhdz)
    EditText qhdz;
    @BindView(R.id.qhdh)
    EditText qhdh;
    @BindView(R.id.shdz)
    EditText shdz;
    @BindView(R.id.shdh)
    EditText shdh;
    @BindView(R.id.shsj)
    EditText shsj;
    @BindView(R.id.beizhu)
    EditText beizhu;
    @BindView(R.id.kfdh)
    EditText kfdh;
    private CustomDatePicker mTimerPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //加载启动界面
        setContentView(R.layout.activity_paotui);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        top_bar_title.setText(type);
        initTimerPicker();
        shsj.setCursorVisible(false);
        shsj.setFocusable(false);
        shsj.setFocusableInTouchMode(false);
        kfdh.setCursorVisible(false);
        kfdh.setFocusable(false);
        kfdh.setFocusableInTouchMode(false);
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
                    Toast.makeText(PaotuiActivity.this,"提交成功！",Toast.LENGTH_LONG).show();
                    finish();
                    break;
                case 1:
                    Toast.makeText(PaotuiActivity.this,"提交失败！",Toast.LENGTH_LONG).show();
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private void addPaotuiData(){

        final LoadingDialog loadingDialog = new LoadingDialog(this);
        loadingDialog.setMessage("正在加载...");
        loadingDialog.show();

        Map userInfo = JSON.parseObject(SPUtils.get(this,"userinfo","{}").toString());
        final Message message=Message.obtain();

        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("cus_id", userInfo.get("id").toString())
                .add("fromaddress", qhdz.getText().toString())
                .add("fromphone", qhdh.getText().toString())
                .add("toaddress", shdz.getText().toString())
                .add("tophone", shdh.getText().toString())
                .add("send_dt", shsj.getText().toString()+":00")
                .add("note", beizhu.getText().toString())
                .build();
        Request request = new Request.Builder().url(Config.url+"/addOrderspt")
                .addHeader("source", Config.REQUEST_HEADER)// 自定义的header
                .post(formBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                loadingDialog.dismiss();
                // TODO: 17-1-4  请求失败
                message.what=1;
                //message.obj=data;
                handler.sendMessage(message);

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                loadingDialog.dismiss();
                // TODO: 17-1-4 请求成功
                Map resultMap = JSON.parseObject(response.body().string());
                if(resultMap.get("status").equals("0")){
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
    @OnClick({R.id.top_back_btn,R.id.submit,R.id.shsj,R.id.kfdh})
    public void btnClick(View view) {


        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.shsj:
                // 日期格式为yyyy-MM-dd HH:mm
                mTimerPicker.show(shsj.getText().toString());
                break;
            case R.id.kfdh:
                showAlterDialog("5051111");
                break;
            case R.id.submit:
                if(qhdz.getText().toString().equals("")){
                    Toast.makeText(PaotuiActivity.this, "请输入取货地址！",Toast.LENGTH_LONG).show();
                }
                /*else if(!isMobileNO(qhdh.getText().toString())){
                    Toast.makeText(PaotuiActivity.this, "请输入正确的取货电话！",Toast.LENGTH_LONG).show();
                }*/
                else if(shdz.getText().toString().equals("")){
                    Toast.makeText(PaotuiActivity.this, "请输入收货地址！",Toast.LENGTH_LONG).show();
                }
                else{
                    addPaotuiData();
                }

                break;

            default:
                break;
        }
    }

    private void showAlterDialog(final String phonenum){
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
    private void initTimerPicker() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, +1);

        //String beginTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);
        //String endTime = DateFormatUtils.long2Str(c.getInstance().getTimeInMillis(), true);
        String beginTime = "2018-10-17 18:00";
        String currentTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);
        String endTime = "2020-12-12 23:59";
        shsj.setText(currentTime);

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                shsj.setText(DateFormatUtils.long2Str(timestamp, true));
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker.setCancelable(true);
        // 显示时和分
        mTimerPicker.setCanShowPreciseTime(true);
        // 允许循环滚动
        mTimerPicker.setScrollLoop(true);
        // 允许滚动动画
        mTimerPicker.setCanShowAnim(true);
    }
    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {

        String telRegex = "[1][3456789]\\d{9}";
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }
}
