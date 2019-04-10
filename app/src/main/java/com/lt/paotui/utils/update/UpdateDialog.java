package com.lt.paotui.utils.update;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lt.paotui.R;


/**
 * Created by mjm on 2017/5/17.
 * 更新对话框
 */

public class UpdateDialog extends Dialog implements View.OnClickListener {
    private UpdateDialogOperate aDialogOperate; // 操作接口
    private Context context;
    private TextView ver;
    private TextView content;
    private Button update_ok;
    private Button upadte_cancle;
    private UpdateBean upadtebean;

    private TextView tip;

    public UpdateDialog(Context context) {
        super(context, R.style.common_dialog);
        this.context = context;
        this.setContentView(R.layout.dialog_update);
        ver = (TextView) findViewById(R.id.update_ver);
        content = (TextView) findViewById(R.id.update_content);
        update_ok = (Button) findViewById(R.id.update_btn_ok);
        upadte_cancle = (Button) findViewById(R.id.update_btn_cancle);
        tip = (TextView) findViewById(R.id.update_tip);
        update_ok.setOnClickListener(this);
        upadte_cancle.setOnClickListener(this);

    }

    public void setData(UpdateBean upadtebean, boolean flag, UpdateDialogOperate aDialogOperate) {
        this.aDialogOperate = aDialogOperate;
        this.upadtebean = upadtebean;


//      if (upadteEntity.getForce() == 1) {
//        LogUtils.d("zj", "upadteEntity.getForce()" + upadteEntity.getForce());
        upadte_cancle.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutParams.setMargins(80, 10, 80, 10);//4个参数按顺序分别是左上右下
//        update_ok.setLayoutParams(layoutParams);
//      upadte_cancle.setLayoutParams(layoutParams);
        tip.setVisibility(View.GONE);
        //关闭以后再说。。。
        upadte_cancle.setVisibility(View.GONE);
        update_ok.setVisibility(View.VISIBLE);
        ver.setText(upadtebean.getVersionName());
        content.setText(upadtebean.getMessage());
        this.setCancelable(false);
//      } else {
//        tip.setVisibility(View.GONE);
//        upadte_cancle.setVisibility(View.VISIBLE);
//        update_ok.setVisibility(View.VISIBLE);
//        ver.setText(upadteEntity.getVersion());
//        content.setText(upadteEntity.getLog());
//        this.setCancelable(false);
//      }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_btn_cancle:
                aDialogOperate.executeCancel("");
                break;
            case R.id.update_btn_ok:
                aDialogOperate.executeCommit("");
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}
