package com.ddinfo.flashman.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.ddinfo.flashman.models.params.RegisterParams;
import com.ddinfo.flashman.models.params.SendCodeParams;
import com.ddinfo.flashman.network.SimpleCallBack;
import com.ddinfo.flashman.utils.ProDialogHelps;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 账号注册界面
 */
public class RegisterActivity extends BaseActivity {

    @Bind(R.id.left_button)
    ImageButton leftButton; //左返回按钮
    @Bind(R.id.header_name)
    TextView headerName;    //标题抬头
    @Bind(R.id.right_button)
    ImageButton rightButton;
    @Bind(R.id.rightBtn)
    Button rightBtn;        //右点击按钮
    @Bind(R.id.textView)
    TextView textView;      //注册提示
    @Bind(R.id.et_phone)
    EditText etPhone;       //手机号
    @Bind(R.id.text_password)
    TextView textPassword;
    @Bind(R.id.et_passwrod)
    EditText etPasswrod;    //验证码
    @Bind(R.id.bt_get_password)
    TextView btGetPassword; //获取验证码
    @Bind(R.id.bt_register)
    Button btRegister;      //注册按钮
    @Bind(R.id.tv_service_contract)
    TextView tvServiceContract; //猪行侠服务协议
    @Bind(R.id.activity_register)
    LinearLayout activityRegister;
    @Bind(R.id.et_username)
    EditText etUsername;    //用户名
    @Bind(R.id.et_ID_code)
    EditText etIDCode;      //身份证号

    private String mLoginPhone;
    private String mPassWord;
    private String mUserName;
    private String mIDCode;

    private static final int TIMEDELSTART = 0x111;
    private static final int TIMEDELEND = 0x222;
    private static Thread mTimeThread;     //验证码倒计时线程
    private static Handler mTimeHandler;
    private static Boolean isStop = false; //短信倒计时开关
    private static int lastTime;           //获取验证码倒计时
    private ProDialogHelps proDialogHelps; //加载框


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        setTitle("注册");
        btGetPassword = (TextView) findViewById(R.id.bt_get_password); //获取验证码按钮
        proDialogHelps = new ProDialogHelps(this);
        mTimeHandler = new TimeHandler();
        if (lastTime > 0) {
            if (mTimeThread != null) {
                startThread();
            }
            btGetPassword.setText(lastTime + "秒");
            btGetPassword.setEnabled(false);
            btGetPassword.setBackground(ContextCompat.getDrawable(context, R.drawable.corners_gray));
        } else {

        }
    }

    private void initData() {

    }

    private void initListener() {

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

    @OnClick({R.id.bt_get_password, R.id.bt_register, R.id.tv_service_contract})
    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.bt_get_password: //点击获取验证码
                mLoginPhone = etPhone.getText().toString().trim();

                if (StringUtils.isEmpty(mLoginPhone)) {
                    ToastUtils.showShortToast("请输入手机号");
                    return;
                }
                etPasswrod.requestFocus(); //获取焦点
                lastTime = ExampleConfig.CODE_RESET_TIME;

                if (mTimeThread != null) {
                    startThread();
                } else {
                    mTimeThread = new TimeThread();
                    startThread();
                }
                btGetPassword.setEnabled(false); //设置不可点击
                btGetPassword.setBackground(ContextCompat.getDrawable(context, R.drawable.corners_deep_gray));
                SendCodeParams params = new SendCodeParams(mLoginPhone);
                Call<BaseResponseEntity> callSendCode = webService.sendCode(params);
                callSendCode.enqueue(new SimpleCallBack<BaseResponseEntity>(this) {
                    @Override
                    public void onSuccess(Call<BaseResponseEntity> call, Response<BaseResponseEntity> response) {
                        ToastUtils.showShortToast("验证码发送成功，请注意接收");
                    }
                    @Override
                    public void onProDialogDismiss() {
                        proDialogHelps.removeProDialog(); //移除加载框
                    }
                });

                break;
            case R.id.bt_register: //点击注册按钮
//                //调用注册接口并登录
//                MyApplication.getSPUtilsInstance().putString(ExampleConfig.TOKEN,"11111");
//                startActivity(MainActivity.class);
                attemptRegister();

                break;
            case R.id.tv_service_contract:
                ToastUtils.showShortToast("猪行侠服务协议");

                break;
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_register;
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
                            btGetPassword.setEnabled(true);
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

    /**
     * 校验并登陆
     */
    private void attemptRegister() {

        mLoginPhone = etPhone.getText().toString().trim();  //手机号
        mPassWord = etPasswrod.getText().toString().trim(); //验证码
        mUserName = etUsername.getText().toString().trim(); //用户名
        mIDCode = etIDCode.getText().toString().trim();     //身份证号

        if (StringUtils.isEmpty(mLoginPhone)) {
            ToastUtils.showShortToast("请输入手机号");
            return;
        }
        if (StringUtils.isEmpty(mPassWord)) {
            ToastUtils.showShortToast("请输入验证码");
            return;
        }

        RegisterParams params = new RegisterParams(mLoginPhone,mPassWord,mUserName,mIDCode);
        proDialogHelps.showProDialog();
        Call<BaseResponseEntity<TokenEntity>> callRegister = webService.register(params);
        callRegister.enqueue(new SimpleCallBack<BaseResponseEntity<TokenEntity>>(this) { //发送网络请求
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
                super.onFail(call, t);
                MyApplication.getSPUtilsInstance().remove(ExampleConfig.TOKEN); //删除token数据
                MyApplication.getSPUtilsInstance().remove(ExampleConfig.LOGIN_PHONE); //删除手机号数据
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
