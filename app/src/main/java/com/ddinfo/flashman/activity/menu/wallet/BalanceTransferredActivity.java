package com.ddinfo.flashman.activity.menu.wallet;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.utils.NetworkUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.WalletEntity;
import com.ddinfo.flashman.network.SimpleCallBack;
import com.ddinfo.flashman.utils.ExitUtil;
import com.ddinfo.flashman.utils.Utils;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class BalanceTransferredActivity extends BaseActivity {

    @Bind(R.id.et_transferred)
    EditText etTransferred;
    @Bind(R.id.tv_cash_pledge)
    TextView tvCashPledge;
    @Bind(R.id.btn_transferred)
    Button btnTransferred;
    @Bind(R.id.btn_affirm)
    Button btnAffirm;
    WalletEntity wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        wallet = (WalletEntity) bundle.getSerializable("wallet");
        if (wallet != null) {
            tvCashPledge.setText(Utils.jointNumFromat(wallet.getDeposit()));
        }
    }

    private void initView() {
        setTitle("余额");

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_balance_transferred;
    }

    @OnClick({R.id.btn_transferred, R.id.btn_affirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_transferred:
                if (wallet != null) {
                    double num = wallet.getDeposit() - wallet.getFrozenAmount();

                    etTransferred.setText(num + "");
                }
                break;
            case R.id.btn_affirm:
                final String transferredNum = etTransferred.getText().toString().trim();
                if(TextUtils.isEmpty(transferredNum)){
                    ToastUtils.showShortToastSafe("请输入正确的金额");
                    return;
                }
                if (Double.parseDouble(transferredNum) < 1) {
                    ToastUtils.showShortToastSafe("转入金额必须大于1元");
                    return;
                }
                new AlertDialog.Builder(context)
                        .setTitle("确认将押金转入余额")
                        .setMessage("确认将押金" + transferredNum + " 元全部转入余额？\n押金转入余额后，相应需要押金的订单你讲无法接单，取货，完成等操作。")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("确认转入", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                double num = wallet.getDeposit() - wallet.getFrozenAmount();
                                if (Double.parseDouble(transferredNum) > num) {
                                    ToastUtils.showShortToastSafe("押金不足，您有部分资金被冻结");
                                } else {
                                    if(TextUtils.isEmpty(etTransferred.getText())){
                                        ToastUtils.showShortToastSafe("请输入正确的金额");
                                    }else{
                                        cashPledgeRollIn();
                                    }
                                }
                                dialog.dismiss();
                            }
                        }).create().show();
                break;
        }
    }

    /**
     * 押金转余额
     */
    private void cashPledgeRollIn() {
        if (NetworkUtils.isConnected()) {
            proDialogHelps.showProDialog();
            double transferredNum = etTransferred.getText().toString().trim().equals("") ? 0 : Double.parseDouble(etTransferred.getText().toString().trim());
            Call<BaseResponseEntity> call = webService.balanceDepositin(ExampleConfig.token, transferredNum);
            call.enqueue(new SimpleCallBack<BaseResponseEntity>(this) {
                @Override
                public void onSuccess(Call<BaseResponseEntity> call, Response<BaseResponseEntity> response) {
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
}
