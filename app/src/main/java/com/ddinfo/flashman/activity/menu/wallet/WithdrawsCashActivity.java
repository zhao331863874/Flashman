package com.ddinfo.flashman.activity.menu.wallet;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.NetworkUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.models.BalanceAccountListEntity;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.WalletEntity;
import com.ddinfo.flashman.models.params.DepositInParams;
import com.ddinfo.flashman.network.SimpleCallBack;
import com.ddinfo.flashman.utils.ExitUtil;
import com.ddinfo.flashman.utils.Utils;

import java.math.BigDecimal;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 余额提现+押金提现
 */
public class WithdrawsCashActivity extends BaseActivity {

    @Bind(R.id.tv_alipay_account) //支付宝账号
    TextView tvAlipayAccount;
    @Bind(R.id.et_withdraws_cash) //提现金额
    EditText etWithdrawsCash;
    @Bind(R.id.textView3)         //钱标识
    TextView textView3;
    @Bind(R.id.balance_desc)      //当前余额名称
    TextView balanceDesc;
    @Bind(R.id.tv_balance_num)    //当前余额
    TextView tvBalanceNum;
    @Bind(R.id.text_password)     //验证码名称
    TextView textPassword;
    @Bind(R.id.et_auth_code)      //验证码
    EditText etAuthCode;
    @Bind(R.id.bt_get_auth_code)  //获取验证码按钮
    Button btGetAuthCode;
    @Bind(R.id.btn_take_cash)     //提现按钮
    Button btnTakeCash;

    private static final int TIMEDELSTART = 0x111;
    private static int lastTime; //获取验证码倒计时
    private static Thread mTimeThread;     //获取验证码倒计时线程
    private static Handler mTimeHandler;
    private static Boolean isStop = false; //获取验证码开关
    WalletEntity wallet;
    BalanceAccountListEntity account;      //账号实体类
    private String from; //类的简写名称

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        from = getIntent().getStringExtra(ExampleConfig.FROM);
        initViews();
        initDatas();
    }

    private void initViews() {
        setTitle(getDescByFrom()+"提现");
        balanceDesc.setText(isFromDeposit(from)?"当前可提现押金":"当前余额");
        mTimeHandler = new TimeHandler();
        if (lastTime > 0) {
            if (mTimeThread != null) {
                startThread();
            }
            btGetAuthCode.setText(lastTime + "秒");
            btGetAuthCode.setEnabled(false); //设置不可点击
            btGetAuthCode.setBackground(ContextCompat.getDrawable(context, R.drawable.corners_deep_gray));
        }
    }

    private void initDatas() {
        Bundle bundle = getIntent().getExtras();
        wallet = (WalletEntity) bundle.getSerializable("wallet");
        account = (BalanceAccountListEntity) bundle.getSerializable("walletAccount");
        if (wallet != null) {
            tvBalanceNum.setText(Utils.jointNumFromat(isFromDeposit(from) ? wallet.getCanDeposit():wallet.getBalance()));
        }
        if (account != null) {
            tvAlipayAccount.setText(account.getAccountNo());
        }

        etWithdrawsCash.addTextChangedListener(new TextWatcher() { //提现金额监听
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                int posDot = temp.indexOf("."); //返回"."字串在父串中首次出现的位置
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    edt.delete(posDot + 3, posDot + 4);
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }
        });


    }

    private String getDescByFrom(){
        return isFromDeposit(from)?"押金":"余额";
    }

    /**
     * 来自押金体现或者是余额体现
     * 默认来自余额体现
     * @param from
     * @return
     */
    private boolean isFromDeposit(String from){
        return PledgeActivity.class.getSimpleName().equals(from);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_withdraws_cash;
    }

    @OnClick({R.id.bt_get_auth_code, R.id.btn_take_cash})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_get_auth_code: //获取验证码
                getAuthCode();
                break;
            case R.id.btn_take_cash: //提现
                encash();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopThread();
    }

    /**
     * 提现
     */
    private void encash() {
        if (NetworkUtils.isConnected()) {
            if (account == null) {
                ToastUtils.showShortToastSafe("没有绑定提现账户");
                return;
            }
            if (wallet == null) {
                ToastUtils.showShortToastSafe(getDescByFrom()+"信息没有获取到");
                return;
            }
            String authCode = etAuthCode.getText().toString().trim();
            double withCashNum = Double.parseDouble(etWithdrawsCash.getText().toString().trim()); //提现金额
            BigDecimal withCashBD = new BigDecimal(withCashNum);
            BigDecimal balanceBD = new BigDecimal(isFromDeposit(from) ? wallet.getCanDeposit() : wallet.getBalance());
            int compareRes = withCashBD.compareTo(balanceBD);
            if (withCashNum < 1.0) {
                ToastUtils.showShortToastSafe("提现金额不能小于1元");
                return;
            } else if (TextUtils.isEmpty(authCode)) {
                ToastUtils.showShortToastSafe("输入验证码");
                return;
            } else if (TextUtils.isEmpty(etWithdrawsCash.getText().toString().trim())) {
                ToastUtils.showShortToastSafe("请输入提现金额");
                return;
            } else if (compareRes == 1) {
                String msg = isFromDeposit(from) ? "提现金额不能大于可提现押金":"提现金额不能大于余额";
                ToastUtils.showShortToastSafe(msg);
                return;
            }

            proDialogHelps.showProDialog();
            DepositInParams params = new DepositInParams(withCashNum, authCode, account.getAccountNo(), account.getUserName(), isFromDeposit(from) ? 1: 0);
            Call<BaseResponseEntity> call = webService.encash(ExampleConfig.token, params);
            call.enqueue(new SimpleCallBack<BaseResponseEntity>(this) {
                @Override
                public void onSuccess(Call<BaseResponseEntity> call, Response<BaseResponseEntity> response) {
                    ToastUtils.showShortToastSafe("提现成功");
                    ExitUtil.getInstance().exitToWalletActivity();
                }

                @Override
                public void onProDialogDismiss() {
                    proDialogHelps.removeProDialog();
                }
            });

        } else {
            ToastUtils.showShortToastSafe("网络不可用");
        }
    }

    /**
     * 提现发送验证码
     */
    private void getAuthCode() {
        lastTime = ExampleConfig.CODE_RESET_TIME;
        if (mTimeThread != null) {
            startThread();
        } else {
            mTimeThread = new TimeThread();
            startThread();
        }
        btGetAuthCode.setEnabled(false);
        btGetAuthCode.setBackground(ContextCompat.getDrawable(context, R.drawable.corners_deep_gray));
        Call<BaseResponseEntity> callSendCode = webService.sendEncashCode(ExampleConfig.token);
        callSendCode.enqueue(new SimpleCallBack<BaseResponseEntity>(this) {
            @Override
            public void onSuccess(Call<BaseResponseEntity> call, Response<BaseResponseEntity> response) {
                ToastUtils.showShortToast("验证码发送成功，请注意接收");
            }

            @Override
            public void onProDialogDismiss() {
                proDialogHelps.removeProDialog();
            }
        });
    }

    private void startThread() {
        isStop = false;
        if (mTimeThread.getState() == Thread.State.NEW) { //一个尚未启动的线程的状态
            if (!mTimeThread.isAlive()) {
                mTimeThread.start();
            }
        } else if (mTimeThread.getState() == Thread.State.TERMINATED) { //个完全运行完成的线程的状态
            mTimeThread = new TimeThread();
            mTimeThread.start();
        }

    }

    private static void stopThread() {
        isStop = true;
    }

    private class TimeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIMEDELSTART:
                    lastTime--;
                    btGetAuthCode = (Button) findViewById(R.id.bt_get_auth_code);
                    if (btGetAuthCode != null) {
                        if (lastTime == 0) {
                            btGetAuthCode.setText("获取验证码");
                            btGetAuthCode.setEnabled(true);
                            btGetAuthCode.setBackground(ContextCompat.getDrawable(context, R.drawable.corners_blue));
                        } else {
                            btGetAuthCode.setText(lastTime + "秒");
                        }
                    } else {
                        ToastUtils.showShortToast("btGetAuthCode is null");
                    }
                    break;
            }
        }
    }

    private class TimeThread extends Thread { //获取验证码倒计时线程
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
                        this.interrupt(); //中断线程
                    }
                } catch (InterruptedException e) {
                    LogUtils.i(e.toString());
                }
            }
        }
    }
}
