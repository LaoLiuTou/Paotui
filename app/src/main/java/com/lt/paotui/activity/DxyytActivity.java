package com.lt.paotui.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.hb.dialog.myDialog.ActionSheetDialog;
import com.hb.dialog.myDialog.MyAlertDialog;
import com.lt.paotui.MainActivity;
import com.lt.paotui.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2019/4/16.
 */

public class DxyytActivity extends Activity {
    @BindView(R.id.top_bar_title)
    TextView top_bar_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dxyyt);
        ButterKnife.bind(this);
        top_bar_title.setText("电信营业厅");

    }
    @OnClick({R.id.top_back_btn,R.id.hfcx,R.id.kdyw,R.id.tccx})
    public void btnClick(View view) {

        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.top_back_btn:
                finish();
                break;
            case R.id.hfcx:
                showActionSheet();
                break;
            case R.id.kdyw:
                showActionSheet();
                break;
            case R.id.tccx:
                showActionSheet();
                break;
            default:
                break;
        }
    }
    private void showActionSheet(){
        ActionSheetDialog dialog = new ActionSheetDialog(this).builder().setTitle("请选择要拨打的电话")
                .addSheetItem("  客服电话:5051111  ", null, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        showAlterDialog("5051111");
                    }
                }).addSheetItem("监督电话:5525777", null, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        showAlterDialog("5525777");
                    }
                });
        dialog.show();
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
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        startActivity(intent);
    }
}
