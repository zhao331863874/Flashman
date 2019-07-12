package com.ddinfo.flashman.activity.menu.wallet;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.NetworkUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.PayOrderResponseEntity;
import com.ddinfo.flashman.models.WalletEntity;
import com.ddinfo.flashman.models.params.RechargeParams;
import com.ddinfo.flashman.network.SimpleCallBack;
import com.ddinfo.flashman.utils.ExitUtil;
import com.ddinfo.flashman.utils.Utils;

import java.math.BigDecimal;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class ModeOfPaymentActivity extends BaseActivity {


    @Bind(R.id.tv_pay_money)
    TextView tvPayMoney;
    @Bind(R.id.tv1)
    TextView tv1;
    @Bind(R.id.tv_balance_money)
    TextView tvBalanceMoney;
    @Bind(R.id.iv_sel_by_balance)
    ImageButton ivByBalance;
    @Bind(R.id.rl_alipay)
    RelativeLayout rlAlipay;
    @Bind(R.id.tv2)
    TextView tv2;
    @Bind(R.id.iv_sel_by_aliwx_pay)
    ImageButton ivByAliwxPay;
    @Bind(R.id.rl_wechatpay)
    RelativeLayout rlWechatpay;
    @Bind(R.id.btn_enter)
    Button btnEnter;
    int checkedType = 0;
    double topUpMoney;
    WalletEntity wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_mode_of_payment_layout;
    }

    private void initData() {
        setTitle("选择支付方式");
        Bundle bundle = getIntent().getExtras();
        wallet = (WalletEntity) bundle.getSerializable("wallet");
        topUpMoney = bundle.getDouble(ExampleConfig.INTENT_TOPUPMONEY);
        tvPayMoney.setText(Utils.jointNumFromat(topUpMoney));
        if (wallet != null) {
            tvBalanceMoney.setText(Utils.jointNumFromat(wallet.getBalance()));
        }
    }

    /**
     * 押金充值
     */
    private void cashRecharge() {
        if (NetworkUtils.isConnected()) {
            proDialogHelps.showProDialog();
            Call<BaseResponseEntity<PayOrderResponseEntity>> call = webService.recharge(ExampleConfig.token, new RechargeParams(topUpMoney, "balance"));
            call.enqueue(new SimpleCallBack<BaseResponseEntity<PayOrderResponseEntity>>(this) {
                @Override
                public void onSuccess(Call<BaseResponseEntity<PayOrderResponseEntity>> call, Response<BaseResponseEntity<PayOrderResponseEntity>> response) {
                    ToastUtils.showShortToastSafe("操作成功");
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

    @OnClick({R.id.iv_sel_by_balance, R.id.iv_sel_by_aliwx_pay, R.id.btn_enter})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_sel_by_balance:
                checkedType = 0;
                ivByBalance.setImageResource(R.mipmap.icon_sel_pressed);
                ivByAliwxPay.setImageResource(R.mipmap.icon_sel_unpress);

                break;
            case R.id.iv_sel_by_aliwx_pay:
                checkedType = 1;
                ivByBalance.setImageResource(R.mipmap.icon_sel_unpress);
                ivByAliwxPay.setImageResource(R.mipmap.icon_sel_pressed);
                break;
            case R.id.btn_enter:
                if (checkedType == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putDouble(ExampleConfig.INTENT_TOPUPMONEY, topUpMoney);
                    bundle.putString("type","chargeDeposit"); //标记押金充值
                    startActivity(PayOrderActivity.class, bundle);
                } else {
                    //余额 冲 押金
                    BigDecimal toAddMoneyBD = new BigDecimal(topUpMoney);
                    BigDecimal balanceBD = new BigDecimal(wallet.getBalance());
                    int compareRes = toAddMoneyBD.compareTo(balanceBD);
                    if (compareRes == 1) {
                        ToastUtils.showShortToastSafe("余额不足");
                        return;
                    }

                    cashRecharge();
                }
                break;
        }
    }
}
