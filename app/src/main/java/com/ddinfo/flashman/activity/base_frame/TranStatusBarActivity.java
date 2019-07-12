package com.ddinfo.flashman.activity.base_frame;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by fuh on 2017年3月9日
 */
public abstract class TranStatusBarActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Object style = statusBarStyle();
        if(style!=null){
            if(style instanceof Drawable){
                windowFit((Drawable) style,0);
            }else if(style instanceof Integer){
                windowFit(null,(int)style);
            }else{
                windowFit(null,0);
            }
        }else{
            windowFit(null,0);
        }
    }

    protected abstract Object statusBarStyle();

    private void windowFit(Drawable drawable, @ColorRes int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //Android 4.4 +
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 生成一个状态栏大小的矩形
            View statusView = null;
            if(drawable == null && color!=0){
                statusView = createStatusView(activity, color);
            }else if(color == 0 && drawable !=null){
                statusView = createStatusView(activity, drawable);
            }else{
                statusView = createStatusView(activity, null,0);
            }
            // 添加 statusView 到布局中
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(statusView);
            // 设置根布局的参数
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

    private static View createStatusView(Activity activity, Drawable drawable) {
        return createStatusView(activity,drawable,0);
    }

    private static View createStatusView(Activity activity, @ColorRes int color) {
        return createStatusView(activity,null,color);
    }

    private static View createStatusView(Activity activity, Drawable drawable, @ColorRes int color) {
        //获取状态栏高度
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);

        // 绘制一个和状态栏一样高的矩形
        ImageView statusView = new ImageView(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        statusView.setLayoutParams(params);
        if(drawable == null && color!=0){
            statusView.setBackgroundColor(ContextCompat.getColor(activity,color));
        }else if(color == 0 && drawable !=null){
            statusView.setBackgroundDrawable(drawable);
        }else{
            statusView.setBackgroundColor(Color.TRANSPARENT);
        }
        statusView.setScaleType(ImageView.ScaleType.FIT_START);
        return statusView;
    }
}
