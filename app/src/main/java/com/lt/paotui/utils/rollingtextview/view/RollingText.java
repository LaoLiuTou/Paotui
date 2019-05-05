package com.lt.paotui.utils.rollingtextview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ViewFlipper;

import com.lt.paotui.R;
import com.lt.paotui.utils.rollingtextview.RollingTextAdapter;

public class RollingText extends ViewFlipper {

    /**
     * 切换的时间间隔
     * */
    private int flipIntervalTime = 2000;
    /**
     * 切换的动画的时间
     * */
    private int durationTime = 500;
    private RollingTextAdapter mAdapter;
    private OnItemClickListener listener;

    public RollingText(Context context) {
        this(context,null);
    }

    public RollingText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 设置切换的时间间隔
     * */
    public void setFlipIntervalTime(int flipIntervalTime) {
        this.flipIntervalTime = flipIntervalTime;
    }
    /**
     * 设置切换的动画的时间
     * */
    public void setDurationTime(int durationTime) {
        this.durationTime = durationTime;
    }
    /**
     * 设置适配器
     * */
    public void setAdapter(RollingTextAdapter mAdapter){
        this.mAdapter = mAdapter;
        start();
    }

    private void init() {
        setFlipInterval(flipIntervalTime);
        setInAnimation(getContext(), R.anim.rolling_text_in);
        setOutAnimation(getContext(),R.anim.rolling_text_out);
        setAutoStart(false);
    }

    private void start(){
        if(this.mAdapter == null||this.mAdapter.getCount() <= 0){
            return;
        }
        removeAllViews();
        //只有数据源大于2条的时候才会开启自动切换
        if(this.mAdapter.getCount() >1)
            setAutoStart(true);
        else
            setAutoStart(false);
        for(int i =0;i<this.mAdapter.getCount();i++){
            View view = this.mAdapter.getView(getContext(),getCurrentView(),i);
            addView(view);
            if(listener != null){
                view.setTag(i);
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onClick((Integer) view.getTag());
                    }
                });
            }
        }
    }



    /**
     * 设置Item点击事件
     * */
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener  = listener;
        start();
    }


    public interface OnItemClickListener{
        void onClick(int position);
    }
}
