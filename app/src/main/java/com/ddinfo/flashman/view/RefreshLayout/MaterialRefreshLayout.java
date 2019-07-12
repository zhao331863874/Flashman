package com.ddinfo.flashman.view.RefreshLayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.ViewPropertyAnimatorUpdateListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.blankj.utilcode.utils.ConvertUtils;
import com.blankj.utilcode.utils.Utils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.application.MyApplication;

import static com.ddinfo.flashman.application.MyApplication.context;

public class MaterialRefreshLayout extends FrameLayout {

    public static final String Tag = MaterialRefreshLayout.class.getSimpleName();
    private final static int DEFAULT_WAVE_HEIGHT = 140;
    private final static int HIGHER_WAVE_HEIGHT = 180;
    private final static int DEFAULT_HEAD_HEIGHT = 70;
    private final static int hIGHER_HEAD_HEIGHT = 100;
    private RefeshView mRefeshView;
    private boolean isOverlay; //是否悬浮在顶端
    private int waveType;
    protected float mWaveHeight;
    protected float mHeadHeight;
    private View mChildView;
    protected boolean isRefreshing; //是否刷新
    private float mTouchY;
    private float mCurrentY;
    private DecelerateInterpolator decelerateInterpolator; //动画效果
    private float headHeight;
    private float waveHeight;
    private MaterialRefreshListener refreshListener;

    public MaterialRefreshLayout(Context context) {
        this(context, null, 0);
        Utils.init(context);
    }

    public MaterialRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        Utils.init(context);
    }

    public MaterialRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Utils.init(context);
        init(context, attrs, defStyleAttr);
    }

    /**
     * 初始化控件
     *
     * @param context
     * @param attrs
     * @param defstyleAttr
     */
    private void init(Context context, AttributeSet attrs, int defstyleAttr) {
        //在编辑的状态下返回true
        if (isInEditMode()) {
            return;
        }
        if (getChildCount() > 1) {
            throw new RuntimeException("can only have one child widget");
        }

        decelerateInterpolator = new DecelerateInterpolator(10); //在动画开始的地方快然后慢

        Utils.init(context);
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.MaterialRefreshLayout, defstyleAttr, 0); //构造自定义控件
        //是否悬浮在顶端
        isOverlay = t.getBoolean(R.styleable.MaterialRefreshLayout_overlay, false);
        /**attrs for materialWaveView*/
        //是否显示波纹效果
        waveType = t.getInt(R.styleable.MaterialRefreshLayout_wave_height_type, 0);
        headHeight = DEFAULT_HEAD_HEIGHT;
        waveHeight = DEFAULT_WAVE_HEIGHT;
        t.recycle();
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i(Tag, "onAttachedToWindow");

        Context context = getContext();

        mChildView = getChildAt(0);

        if (mChildView == null) {
            return;
        }
        if(context!=null){
            Utils.init(context);
        }
        setWaveHeight(ConvertUtils.dp2px(waveHeight));
        setHeaderHeight(ConvertUtils.dp2px(headHeight));
        mRefeshView = new RefeshView(context);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ConvertUtils.dp2px(hIGHER_HEAD_HEIGHT));
        layoutParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        mRefeshView.setVisibility(View.GONE);
        setHeaderView(mRefeshView);

    }

    //ViewGroup的一个方法，目的是在系统向该ViewGroup及其各个childView触发onTouchEvent()之前对相关事件进行一次拦截
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //正在刷新
        if (isRefreshing) return false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchY = ev.getY();//按下的y值
                mCurrentY = mTouchY;//中间变量
                break;
            case MotionEvent.ACTION_MOVE:
                float currentY = ev.getY();
                float dy = currentY - mTouchY;//计算滑动的方向
                if (dy > 0 && !canChildScrollUp()) {//当向下滑并且子类在顶部
                    if (mRefeshView != null) {
                        mRefeshView.setVisibility(View.VISIBLE);
                        mRefeshView.onBegin(this);
                    }
                    return true;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (isRefreshing) {
            return super.onTouchEvent(e);
        }

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                mCurrentY = e.getY();
                float dy = mCurrentY - mTouchY;//现在的位置减去按下的位置，开始移动
                dy = Math.min(mWaveHeight * 2, dy);
                dy = Math.max(0, dy);
                if (mChildView != null) {
                    float offsetY = decelerateInterpolator.getInterpolation(dy / mWaveHeight / 2) * dy / 2;
                    float fraction = offsetY / mHeadHeight;
                    if (mRefeshView != null) {
                        mRefeshView.getLayoutParams().height = (int) offsetY;
                        mRefeshView.requestLayout();
                        mRefeshView.onPull(this, fraction);
                    }
                    if (!isOverlay)
                        ViewCompat.setTranslationY(mChildView, offsetY);

                }
                return true;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mChildView != null) {
                    if (mRefeshView != null) {
                        if (isOverlay) {
                            if (mRefeshView.getLayoutParams().height > mHeadHeight) {

                                updateListener();

                                mRefeshView.getLayoutParams().height = (int) mHeadHeight;
                                mRefeshView.requestLayout();

                            } else {
                                mRefeshView.getLayoutParams().height = 0;
                                mRefeshView.requestLayout();
                            }

                        } else {
                            if (ViewCompat.getTranslationY(mChildView) >= mHeadHeight) {
                                createAnimatorTranslationY(mChildView, mHeadHeight, mRefeshView);
                                updateListener();
                            } else {
                                createAnimatorTranslationY(mChildView, 0, mRefeshView);
                            }
                        }
                    }


                }
                return true;
        }

        return super.onTouchEvent(e);

    }


    public void updateListener() {
        isRefreshing = true;
        if (mRefeshView != null) {
            mRefeshView.onRefreshing(MaterialRefreshLayout.this);
        }
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (refreshListener != null) {
                    refreshListener.onRefresh(MaterialRefreshLayout.this);
                }
            }
        }, 500);


    }


    public void createAnimatorTranslationY(final View v, final float h, final LinearLayout fl) {
        ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = ViewCompat.animate(v);
        viewPropertyAnimatorCompat.setDuration(250); //动画持续时间
        viewPropertyAnimatorCompat.setInterpolator(new DecelerateInterpolator()); //设置减速插值器：动画越来越慢
        viewPropertyAnimatorCompat.translationY(h);  //Y轴平移
        viewPropertyAnimatorCompat.start();
        viewPropertyAnimatorCompat.setUpdateListener(new ViewPropertyAnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(View view) {
                float height = ViewCompat.getTranslationY(v);
                fl.getLayoutParams().height = (int) height;
                fl.requestLayout();
            }
        });
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     * 判断子类是不是在顶部true 表示不在顶部
     */
    public boolean canChildScrollUp() {
        if (mChildView == null) {
            return false;
        }
        if (Build.VERSION.SDK_INT < 14) {
            if (mChildView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mChildView;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mChildView, -1) || mChildView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mChildView, -1);
        }
    }



    public void setWaveHigher() {
        headHeight = hIGHER_HEAD_HEIGHT;
        waveHeight = HIGHER_WAVE_HEIGHT;
    }

    public void finishRefreshing() { //刷新动画
        if (mChildView != null) {
            ViewPropertyAnimatorCompat viewPropertyAnimatorCompat = ViewCompat.animate(mChildView);
            viewPropertyAnimatorCompat.setDuration(200); //动画持续时间
            viewPropertyAnimatorCompat.y(ViewCompat.getTranslationY(mChildView));     //更改在屏幕上的坐标
            viewPropertyAnimatorCompat.translationY(0); //Y轴平移
            viewPropertyAnimatorCompat.setInterpolator(new DecelerateInterpolator()); //设置减速插值器：动画越来越慢
            viewPropertyAnimatorCompat.start();
            if (mRefeshView != null) {
                mRefeshView.onComlete(MaterialRefreshLayout.this);
            }
            if (refreshListener != null) {
                refreshListener.onfinish();
            }
        }
        isRefreshing = false;
    }

    public void finishRefresh() {
        this.post(new Runnable() {
            @Override
            public void run() {
                finishRefreshing();
            }
        });
    }

    private void setHeaderView(final View headerView) {
        addView(headerView);
    }

    public void setHeader(final View headerView) {
        setHeaderView(headerView);
    }


    public void setWaveHeight(float waveHeight) {
        this.mWaveHeight = waveHeight;
    }

    public void setHeaderHeight(float headHeight) {
        this.mHeadHeight = headHeight;
    }

    public void setMaterialRefreshListener(MaterialRefreshListener refreshListener) {
        this.refreshListener = refreshListener;
    }

    public boolean isRefreshing() {
        return isRefreshing;
    }

    public void setRefreshing(boolean isRefreshing) {
        if (!isRefreshing)
            finishRefresh();
    }
}
