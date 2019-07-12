package com.ddinfo.flashman.view.RefreshLayout;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ddinfo.flashman.R;
import com.ddinfo.flashman.utils.Utils;


/**
 * Created by dianda on 2016/5/6.
 */
public class RefeshView extends LinearLayout implements MaterialHeadListener{
    private LayoutInflater mInflater;
    private ImageView imageView;
    private View view;
    private AnimationDrawable frameAnim; //帧动画
    public RefeshView(Context context) {
        this(context, null);
    }

    public RefeshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefeshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mInflater = LayoutInflater.from(context);
        view  = mInflater.inflate(R.layout.refresh_layout, null);
        frameAnim=(AnimationDrawable) getResources().getDrawable(R.drawable.load_anim); //构造帧动画
        initImageView(view);
        this.setGravity(Gravity.CENTER_HORIZONTAL); //水平居中
        // 把AnimationDrawable设置为ImageView的背景
        addView(view);
//        LayoutParams params = (LayoutParams) view.getLayoutParams();
//        params.gravity = Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM;
//        frameAnim=(AnimationDrawable) getResources().getDrawable(R.drawable.load_anim);
    }

    private void initImageView(View view) {
        imageView = (ImageView) view.findViewById(R.id.img_refesh);
        imageView.setBackgroundDrawable(frameAnim); //代表以这个drawable为背景来填充ImageView的宽高及ImageView多高多宽，drawable也相应放大至多高多宽
    }

    @Override
    public void onComlete(MaterialRefreshLayout materialRefreshLayout) {
        cancelSunLineAnim();
        ViewCompat.setScaleX(this, 0);
        ViewCompat.setScaleY(this, 0);
    }

    @Override
    public void onBegin(MaterialRefreshLayout materialRefreshLayout) {
        ViewCompat.setScaleX(this, 0.001f);
        ViewCompat.setScaleY(this, 0.001f);
    }


    @Override
    public void onPull(MaterialRefreshLayout materialRefreshLayout, float fraction) {
        float a = Utils.limitValue(1, fraction);
        ViewCompat.setScaleX(this, a);
        ViewCompat.setScaleY(this, a);
        ViewCompat.setAlpha(this, a);
    }

    @Override
    public void onRelease(MaterialRefreshLayout materialRefreshLayout, float fraction) {

    }

    @Override
    public void onRefreshing(MaterialRefreshLayout materialRefreshLayout) {
        startSunLineAnim();
    }
    /**
     * 停止动画
     */
    public void cancelSunLineAnim() {
        if (frameAnim != null && frameAnim.isRunning()) {
            frameAnim.stop();
        }
    }
    void startSunLineAnim(){
        if (frameAnim != null && !frameAnim.isRunning()) {
            frameAnim.start();
        }
    }
}
