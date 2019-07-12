package com.ddinfo.flashman.activity;

import android.os.Bundle;
import android.text.TextUtils;

import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.activity.tab_frame.MainActivity;
import com.ddinfo.flashman.application.MyApplication;
import com.ddinfo.flashman.constant.ExampleConfig;


public class LaunchActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_launch);
        super.onCreate(savedInstanceState);
        refreshToken();
        ExampleConfig.token = MyApplication.getSPUtilsInstance().getString(ExampleConfig.TOKEN);
        MyApplication.getSPUtilsInstance().putBoolean(ExampleConfig.ISUPDATECANCEL, false);
        String email = MyApplication.getSPUtilsInstance().getString(ExampleConfig.LOGIN_EMAIL);
        String pwd = MyApplication.getSPUtilsInstance().getString(ExampleConfig.LOGIN_PASSWORD);
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pwd)) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(LoginActivity.class);
                    finish();
                }
            }, 1000);
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(MainActivity.class);
                    finish();
                }
            }, 1000);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_launch;
    }


}
