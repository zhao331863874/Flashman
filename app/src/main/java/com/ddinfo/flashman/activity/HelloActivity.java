package com.ddinfo.flashman.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;

import com.blankj.utilcode.utils.AppUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.activity.tab_frame.MainActivity;
import com.ddinfo.flashman.constant.ExampleConfig;

import java.util.ArrayList;

import butterknife.Bind;

public class HelloActivity extends BaseActivity {

    @Bind(R.id.tv_version_name)
    TextView tvVersionName; //软件版本号
    private static final int startGo = 0;
    private boolean isLoad = false;
    private static final String TAG = "HelloActivity";
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case startGo:
                    startActivity(new Intent(HelloActivity.this, MainActivity.class));
                    finish();
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(TextUtils.isEmpty(ExampleConfig.token)) { //判断token是否有值
            startActivity(new Intent(HelloActivity.this, LoginActivity.class));
            finish();
            return;
        }

        mHandler.sendEmptyMessageDelayed(startGo, 2000);
        initData();

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_hello;
    }

    private void initData() {
        refreshToken();
        tvVersionName.setText("v" + AppUtils.getAppVersionName(context));
    }

}
