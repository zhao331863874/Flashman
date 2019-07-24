package com.ddinfo.flashman.activity.menu;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.LoginActivity;
import com.ddinfo.flashman.activity.PerfectUserInfoActivity;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.activity.menu.wallet.CallServiceActivity;
import com.ddinfo.flashman.application.MyApplication;
import com.ddinfo.flashman.constant.ExampleConfig;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 设置界面
 */
public class SettingActivity extends BaseActivity {

    @Bind(R.id.tv_app_mes)      //关于猪行侠
    TextView tvAppMes;
    @Bind(R.id.tv_checkout_new) //检查新版本
    TextView tvCheckoutNew;
    @Bind(R.id.tv_call_service) //联系客服
    TextView tvCallService;
    @Bind(R.id.tv_quit)         //退出登录
    TextView tvQuit;
    @Bind(R.id.activity_setting)
    LinearLayout activitySetting;

    private AlertDialog.Builder builder;
    private AlertDialog alertDialog; //退出提示弹框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        setTitle("设置");
        builder = new AlertDialog.Builder(this);
        builder.setMessage("确认退出吗？");
        builder.setTitle("提示");

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(LoginActivity.class);
                dialog.dismiss();
                finish();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog = builder.create();
    }

    private void initData() {

    }


    private void initListener() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_setting;
    }

    @OnClick({R.id.perfectUserInfo,R.id.tv_app_mes, R.id.tv_checkout_new, R.id.tv_call_service, R.id.tv_quit})
    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.perfectUserInfo: //完善个人信息
                startActivity(PerfectUserInfoActivity.class);
                break;
            case R.id.tv_app_mes: //关于猪行侠
                startActivity(AppMesActivity.class);
                break;
            case R.id.tv_checkout_new: //检查新版本
                ToastUtils.showShortToast("查看更新");
                break;
            case R.id.tv_call_service: //联系客服
                startActivity(CallServiceActivity.class);
                break;
            case R.id.tv_quit: //退出登录
                MyApplication.getSPUtilsInstance().remove(ExampleConfig.TOKEN);
                MyApplication.getSPUtilsInstance().remove(ExampleConfig.LOGIN_PHONE);
                ExampleConfig.token = null;

                alertDialog.show();
                break;
        }
    }
}
