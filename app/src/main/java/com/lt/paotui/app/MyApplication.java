package com.lt.paotui.app;


import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lt.paotui.MainActivity;
import com.lt.paotui.R;
import com.lt.paotui.utils.Config;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import butterknife.internal.Utils;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2019/4/10.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();


    }


}