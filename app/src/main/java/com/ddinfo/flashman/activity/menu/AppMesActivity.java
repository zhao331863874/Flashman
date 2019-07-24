package com.ddinfo.flashman.activity.menu;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.AppUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.activity.tab_frame.MainActivity;

import butterknife.Bind;


/**
 * 关于猪行侠界面
 */
public class AppMesActivity extends BaseActivity {

    @Bind(R.id.iv_app_logo) //软件Logo
    ImageView ivAppLogo;
    @Bind(R.id.tv_app_mes)  //软件名称
    TextView tvAppMes;
    @Bind(R.id.activity_app_mes)
    RelativeLayout activityAppMes;
    private String appVersionCode; //软件版本号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        setTitle("关于");
        appVersionCode = AppUtils.getAppVersionName(context);
        tvAppMes.setText("猪行侠V"+appVersionCode);
    }

    private void initData() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_app_mes;
    }
}
