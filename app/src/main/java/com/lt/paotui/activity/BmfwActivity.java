package com.lt.paotui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hb.dialog.myDialog.MyAlertDialog;
import com.lt.paotui.R;
import com.lt.paotui.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2019/4/16.
 */

public class BmfwActivity extends Activity {
    @BindView(R.id.top_bar_title)
    TextView top_bar_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmfw);
        ButterKnife.bind(this);

        top_bar_title.setText("便民服务");
    }
    @OnClick({R.id.top_back_btn,R.id.jzfw,R.id.gp,R.id.wzcx,R.id.dc,R.id.bmfwrx,
        R.id.cljy})
    public void btnClick(View view) {
        String [] phonenumbers={"5059898","5059696"};
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.jzfw:
                intent.setClass(this, NewsListActivity.class);
                intent.putExtra("type", "31");
                intent.putExtra("title", "家政服务");
                startActivity(intent);
                break;
            case R.id.gp:
                if((boolean)SPUtils.get(this,"islogin",false)){
                    intent.setClass(this, TicketActivity.class);
                    intent.putExtra("type", "9");
                    intent.putExtra("title", "购票留言");
                    startActivity(intent);
                }
                else{
                    showUnloginDialog();
                }
                break;
            case R.id.wzcx:
                Toast.makeText(BmfwActivity.this,"正在开发中。。。！",Toast.LENGTH_LONG).show();
                break;
            case R.id.dc:
                if((boolean)SPUtils.get(this,"islogin",false)){
                    int random=((int)(1+Math.random()*(10-1+1)))%2;
                    showAlterDialog("一键叫车",phonenumbers[random]);
                }
                else{
                    showUnloginDialog();
                }
                break;
            case R.id.bmfwrx:
                Toast.makeText(BmfwActivity.this,"正在开发中。。。！",Toast.LENGTH_LONG).show();
                break;
            case R.id.cljy:
                if((boolean)SPUtils.get(this,"islogin",false)){
                    int random=((int)(1+Math.random()*(10-1+1)))%2;
                    showAlterDialog("一键叫车",phonenumbers[random]);
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
        MyAlertDialog myAlertDialog = new MyAlertDialog(this).builder()
                .setTitle("未登录")
                .setMsg("即将前往登录")
                .setPositiveButton("确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setClass(BmfwActivity.this, LoginActivity.class);
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
}
