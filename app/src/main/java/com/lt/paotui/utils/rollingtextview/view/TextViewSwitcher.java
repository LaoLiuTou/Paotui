package com.lt.paotui.utils.rollingtextview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AnimRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewAnimator;

import com.lt.paotui.R;
import com.lt.paotui.utils.rollingtextview.RollingTextAdapter;

/**
 * Created by lihuayong on 18/7/20.
 * 代码参考ViewFlipper源码
 * 修改了部分源码，如不再接收手机锁屏和开屏广播，需要调用api手动设置自动播放
 *
 */

public class TextViewSwitcher extends ViewAnimator {

    private static final  String TAG = "TextViewSwitcher";

    private static final int DEFAULT_FLIP_DURATION = 500;

    private static final int DEFAULT_FLIP_INTERVAL = 3000;

    private static final int DEFAULT_IN_ANIMATION = R.anim.rolling_text_in;

    private static final int DEFAULT_OUT_ANIMATION = R.anim.rolling_text_out;

    /**
     * 切换的时间间隔
     * */
    private int mFlipInterval = DEFAULT_FLIP_INTERVAL;

    /**
     * 动画切换时间间隔
     */
    private int mFlipDuration = DEFAULT_FLIP_DURATION;

    @AnimRes
    private int mInAnimation = DEFAULT_IN_ANIMATION;

    @AnimRes
    private int mOutAnimation = DEFAULT_OUT_ANIMATION;

    private boolean mAutoStart = false;
    private boolean mVisible = false;
    private boolean mStarted = false;
    private boolean mRunning = false;

    private RollingTextAdapter mAdapter;

    private OnItemClickListener mListener;

    public TextViewSwitcher(Context context) {
        this(context,null);
    }

    public TextViewSwitcher(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseConfig(context,attrs);
        init(context);
    }

    private void parseConfig(Context context, @Nullable  AttributeSet attrs){
        if(null != attrs) {
            /** get set from xml files */
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextViewSwitcher);
            mFlipDuration = a.getInteger(R.styleable.TextViewSwitcher_flipDuration, DEFAULT_FLIP_DURATION);
            mFlipInterval = a.getInteger(R.styleable.TextViewSwitcher_flipInterval, DEFAULT_FLIP_INTERVAL);
            mAutoStart = a.getBoolean(R.styleable.TextViewSwitcher_autoStart, false);
            mInAnimation = a.getResourceId(R.styleable.TextViewSwitcher_inAnimation, DEFAULT_IN_ANIMATION);
            mOutAnimation = a.getResourceId(R.styleable.TextViewSwitcher_outAnimation, DEFAULT_OUT_ANIMATION);

            a.recycle();
        }
    }

    private void init(Context context){
        Animation animIn = AnimationUtils.loadAnimation(context, mInAnimation);
        setInAnimation(animIn);
        Animation animOut = AnimationUtils.loadAnimation(context, mOutAnimation);
        setOutAnimation(animOut);

        setFlipInterval(mFlipInterval);
        setFlipDuration(mFlipDuration);
        setAutoStart(mAutoStart);
    }

    /**
     * load subview
     */
    private void start(){
        if(this.mAdapter == null||this.mAdapter.getCount() <= 0){
            return;
        }
        removeAllViews();

        //auto start flipping while data size >= 2
        if(this.mAdapter.getCount() >2)
            setAutoStart(true);
        else
            setAutoStart(false);

        for(int i =0;i<this.mAdapter.getCount();i= i+2){
            View view = this.mAdapter.getView(getContext(),getCurrentView(),i);
            addView(view);
            if(mListener != null){
                view.setTag(i);
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.onClick((int)view.getTag());
                    }
                });
            }
        }
    }

    /**
     * set view adapter
     * @param adapter the adapter
     */
    public void setAdapter(RollingTextAdapter adapter){
        this.mAdapter = adapter;
        start();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
        start();
    }

    /**
     *  更新view显示
     */
    private void updateRunning(){
        updateRunning(true);
    }

    /**
     * 在布局中只显示当前id的子view，其他view不显示
     * @param childIndex 子view的id
     * @param animate 是否要显示动画
     */
    void showOnlyOneChild(int childIndex, boolean animate) {
        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (i == childIndex) {
                if (animate && getInAnimation() != null) {
                    child.startAnimation(getInAnimation());
                }
                child.setVisibility(View.VISIBLE);
            } else {
                if (animate && getOutAnimation() != null && child.getVisibility() == View.VISIBLE) {
                    child.startAnimation(getOutAnimation());
                } else if (child.getAnimation() == getInAnimation())
                    child.clearAnimation();
                child.setVisibility(View.GONE);
            }
        }
    }

    private void updateRunning(boolean flipNow){
        boolean running = mVisible && mStarted ;
        if (running != mRunning) {
            if (running) {
                showOnlyOneChild(getDisplayedChild(),flipNow);
                postDelayed(mRollRunnable, mFlipInterval);
            } else {
                removeCallbacks(mRollRunnable);
            }
            mRunning = running;
        }
    }

    @Override
    public void removeAllViews() {
        super.removeAllViews();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i(TAG,"onAttachedToWindow");

        if(mAutoStart){
            startFlipping();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.i(TAG,"onDetachedFromWindow");

        mVisible = false;
        updateRunning();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);

        Log.i(TAG,"onWindowVisibilityChanged  "+(visibility == VISIBLE ? "VISIBLE":"INVISIBLE"));
        mVisible = visibility == VISIBLE;
        updateRunning(false);
    }

    /**
     * Set if this view automatically calls {@link #startFlipping()} when it
     * becomes attached to a window.
     */
    public void setAutoStart(boolean autoStart){
        this.mAutoStart = autoStart;
    }

    /**
     * Returns true if this view automatically calls {@link #startFlipping()}
     * when it becomes attached to a window.
     */
    public boolean isAutoStart(){return mAutoStart;}

    /**
     * Start a timer to cycle through child views
     * without show animation.
     */
    public void startFlipping() {
        mStarted = true;
        updateRunning(false);
    }

    /**
     * No more flips
     */
    public void stopFlipping() {
        mStarted = false;
        updateRunning();
    }

    /**
     * Returns true if the child views are flipping.
     */
    public boolean isFlipping() {
        return mStarted;
    }

    /**
     * How long to wait before flipping to the next view
     * @param milliseconds time in milliseconds
     */
    public void setFlipInterval(int milliseconds) {
        this.mFlipInterval = milliseconds;
    }

    /**
     * How long to finish flipping in/out animation
     * @param milliseconds time in milliseconds
     */
    public void setFlipDuration(int milliseconds){
        this.mFlipDuration = milliseconds;
        getInAnimation().setDuration(mFlipDuration);
        getOutAnimation().setDuration(mFlipDuration);
    }

    private final Runnable mRollRunnable = new Runnable() {
        @Override
        public void run() {
            if (mRunning) {
                showNext();
                postDelayed(mRollRunnable, mFlipInterval);

                Log.i(TAG,"child :" +getDisplayedChild()+" is "+(getChildAt(getDisplayedChild()).getVisibility() == VISIBLE ? "VISIBLE":"INVISIBLE"));
                Log.d(TAG, "updateRunning() mVisible=" + mVisible + ", mStarted=" + mStarted
                        + ", mRunning=" + mRunning);
            }
        }
    };

    public interface OnItemClickListener{
        void onClick(int position);
    }
}