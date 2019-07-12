package com.ddinfo.flashman.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.share.sdk.openapi.APAPIFactory;
import com.alipay.share.sdk.openapi.APMediaMessage;
import com.alipay.share.sdk.openapi.APTextObject;
import com.alipay.share.sdk.openapi.IAPApi;
import com.alipay.share.sdk.openapi.SendMessageToZFB;
import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.activity.tab_frame.MainActivity;
import com.ddinfo.flashman.application.MyApplication;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.TokenEntity;
import com.ddinfo.flashman.models.params.LoginParams;
import com.ddinfo.flashman.models.params.SendCodeParams;
import com.ddinfo.flashman.network.SimpleCallBack;
import com.ddinfo.flashman.utils.Utils;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A login screen that offers login via email/password.
 * 账号登录界面
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.left_button)
    ImageButton leftButton; //左返回按钮
    @Bind(R.id.header_name)
    TextView headerName;    //标题抬头
    @Bind(R.id.right_button)
    ImageButton rightButton;
    @Bind(R.id.rightBtn)
    Button rightBtn;        //右点击按钮
    @Bind(R.id.et_phone)
    EditText etPhone;       //手机号码
    @Bind(R.id.et_passwrod)
    EditText etPasswrod;    //验证码
    @Bind(R.id.bt_login)
    Button btLogin;         //登录按钮
    @Bind(R.id.tv_register)
    TextView tvRegister;    //新用户注册
    @Bind(R.id.activity_register)
    LinearLayout activityRegister;
    private String mLoginPhone;
    private String mPassWord;

    private static final int TIMEDELSTART = 0x111;
    private static final int TIMEDELEND = 0x222;
    private static int lastTime;       //获取验证码倒计时
    private static Thread mTimeThread; //验证码倒计时线程
    private static Handler mTimeHandler;
    private static Boolean isStop = false; //短信倒计时开关

    TextView btGetPassword; //获取验证码

    //    private LoginParams loginParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE); //隐藏标题栏
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        setTitle("登录");
        btGetPassword = (TextView) findViewById(R.id.bt_get_password);
        mTimeHandler = new TimeHandler();
        if (lastTime > 0) {
            if (mTimeThread != null) {
                startThread();
            }
            btGetPassword.setText(lastTime + "秒");
            btGetPassword.setEnabled(false); //设置不可点击
            btGetPassword.setBackground(ContextCompat.getDrawable(context, R.drawable.corners_deep_gray));
        }
    }

    private void initData() {

    }

    private void initListener() {
    }

    private class TimeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIMEDELSTART:
                    lastTime--;
                    btGetPassword = (TextView) findViewById(R.id.bt_get_password);
                    if (btGetPassword != null) {
                        if (lastTime == 0) {
                            btGetPassword.setText("获取验证码");
                            btGetPassword.setEnabled(true); //设置按钮可点击
                        } else {
                            btGetPassword.setText(lastTime + "秒");
                        }
                    } else {
                        ToastUtils.showShortToast("btGetPassword is null");
                    }
                    break;
            }
        }
    }

    private static class TimeThread extends Thread {
        @Override
        public void run() {
            while (!isStop) {
                try {
                    if (lastTime > 1) {
                        Thread.sleep(1000);
                        Message msg = new Message();
                        msg.what = TIMEDELSTART;
                        mTimeHandler.sendMessage(msg);
                    } else {
                        stopThread();
                    }

                    if (isStop) {
                        this.interrupt(); //中断本线程
                    }
                } catch (InterruptedException e) {
                    LogUtils.i(e.toString());
                }
            }
        }
    }

    @OnClick({R.id.bt_get_password, R.id.bt_login, R.id.tv_register})
    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.bt_get_password: //点击获取验证码
                if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {
                    ToastUtils.showShortToast("请输入正确的手机号码");
                    return;
                }
                etPasswrod.requestFocus(); //获取焦点
                lastTime = ExampleConfig.CODE_RESET_TIME; //设置验证码倒计时为60秒
                mLoginPhone = etPhone.getText().toString().trim(); //设置手机号
                SendCodeParams params = new SendCodeParams(mLoginPhone);
                proDialogHelps.showProDialog();
                Call<BaseResponseEntity> callSendCode = webService.sendCode(params);
                callSendCode.enqueue(new SimpleCallBack<BaseResponseEntity>(this) { //发送网络请求
                    @Override
                    public void onSuccess(Call<BaseResponseEntity> call, Response<BaseResponseEntity> response) { //请求成功
                        ToastUtils.showShortToast("验证码发送成功，请注意接收");
                        if (mTimeThread != null) {
                            startThread();
                        } else {
                            mTimeThread = new TimeThread();
                            startThread();
                        }
                        btGetPassword.setEnabled(false);
                        btGetPassword.setBackground(ContextCompat.getDrawable(context, R.drawable.corners_deep_gray));
                    }

                    @Override
                    public void onProDialogDismiss() {
                        proDialogHelps.removeProDialog(); //移除加载框
                    }

                    @Override
                    public void onFail(Call<BaseResponseEntity> call, Throwable t) { //请求失败
                        super.onFail(call, t);
                    }
                });

                break;
            case R.id.bt_login: //点击登录按钮
                attemptLogin();
                break;
            case R.id.tv_register: //点击新用户注册
                startActivity(RegisterActivity.class);
                break;
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }


    private static void startThread() {
        isStop = false;
        if (mTimeThread.getState() == Thread.State.NEW) { //判断是否是一个尚未启动的线程的状态
            if (!mTimeThread.isAlive()) { //当前是否为可用状态
                mTimeThread.start(); //启动线程
            }
        } else if (mTimeThread.getState() == Thread.State.TERMINATED) { //判断是否是一个完全运行完成的线程的状态
            mTimeThread = new TimeThread();
            mTimeThread.start();
        }

    }

    private static void stopThread() {
        isStop = true;
    }

    /**
     * 校验并登陆
     */
    private void attemptLogin() {
        mLoginPhone = etPhone.getText().toString().trim();
        mPassWord = etPasswrod.getText().toString().trim();

        if (StringUtils.isEmpty(mLoginPhone) && Utils.checkPhone(mLoginPhone)) {
            ToastUtils.showShortToast("请输入正确的手机号");
            return;
        }
        if (StringUtils.isEmpty(mPassWord)) {
            ToastUtils.showShortToast("请输入验证码");
            return;
        }

        LoginParams params = new LoginParams(mLoginPhone, mPassWord);
        Call<BaseResponseEntity<TokenEntity>> callTokenEntity = webService.login(params);
        callTokenEntity.enqueue(callBackLogin);
        proDialogHelps.showProDialog();
    }
    //登录请求回调
    Callback<BaseResponseEntity<TokenEntity>> callBackLogin = new SimpleCallBack<BaseResponseEntity<TokenEntity>>(this) {
        @Override
        public void onSuccess(Call<BaseResponseEntity<TokenEntity>> call, Response<BaseResponseEntity<TokenEntity>> response) { //请求成功
            ExampleConfig.token = response.body().getData().getToken();
            MyApplication.getSPUtilsInstance().putString(ExampleConfig.TOKEN, ExampleConfig.token); //存储token
            MyApplication.getSPUtilsInstance().putString(ExampleConfig.LOGIN_PHONE, mLoginPhone); //存储手机号
            startActivity(MainActivity.class);
            ToastUtils.showShortToast("登陆成功");
            finish();
        }

        @Override
        public void onProDialogDismiss() {
            proDialogHelps.removeProDialog(); //移除加载框
        }

        @Override
        public void onFail(Call<BaseResponseEntity<TokenEntity>> call, Throwable t) { //请求失败
            super.onFailure(call, t);
            MyApplication.getSPUtilsInstance().remove(ExampleConfig.TOKEN); //删除token数据
            MyApplication.getSPUtilsInstance().remove(ExampleConfig.LOGIN_PHONE); //删除手机号数据
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtils.i(lastTime + "");

    }
}

