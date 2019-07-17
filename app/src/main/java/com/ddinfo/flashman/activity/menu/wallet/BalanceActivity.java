package com.ddinfo.flashman.activity.menu.wallet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.NetworkUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.activity.webview.WebViewClientActivity;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.constant.UrlConstant;
import com.ddinfo.flashman.models.BalanceAccountListEntity;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.WalletEntity;
import com.ddinfo.flashman.network.SimpleCallBack;
import com.ddinfo.flashman.utils.Utils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 余额界面
 */
public class BalanceActivity extends BaseActivity {

    @Bind(R.id.tv_balance) //余额
    TextView tvBalance;
    @Bind(R.id.ll_balance_top) //余额布局
    LinearLayout llBalanceTop;
    @Bind(R.id.tv_balance_one) //申请提现
    TextView tvBalanceOne;
    @Bind(R.id.tv_balance_two) //提现历史
    TextView tvBalanceTwo;

    private AlertDialog.Builder builderOne; //提示押金少于100元弹框
    private AlertDialog.Builder builderTwo; //提示未设置提现账号弹框
    private AlertDialog alertDialogOne;//提示充值押金
    private AlertDialog alertDialogTwo;//提示未绑定账户

    WalletEntity wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        setTitle("余额");
        initBuilder();
        rightButton.setVisibility(View.VISIBLE);
        rightButton.setText("提现设置");
    }

    private void initBuilder() {
        builderOne = new AlertDialog.Builder(context);
        builderOne.setTitle("您的押金少于100元");
        builderOne.setMessage("当前押金少于100元\n猪行者建议你充值一定金额的押金，以免影响你正常接单。");

        builderOne.setPositiveButton("继续提现", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getAccountList();
                dialog.dismiss();
            }
        });
        builderOne.setNegativeButton("去充押金", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Bundle bunder = new Bundle();
                bunder.putSerializable("wallet", wallet);
                startActivity(TopUpInputActivity.class, bunder);
                dialog.dismiss();
            }
        });

        builderTwo = new AlertDialog.Builder(context);
        builderTwo.setTitle("未设置提现账号");
        builderTwo.setMessage("你需要设置提现账号才能申请提现，提现账号可以为支付宝账号或微信");

        builderTwo.setPositiveButton("暂不设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //测试用
                alertDialogOne.show();
                dialog.dismiss();
            }
        });
        builderTwo.setNegativeButton("现在设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(WithdrawsCashSettingActivity.class);
                dialog.dismiss();
            }
        });

        alertDialogOne = builderOne.create();
        alertDialogTwo = builderTwo.create();

    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        wallet = (WalletEntity) bundle.getSerializable("wallet");
        if (wallet != null) {
            tvBalance.setText(Utils.jointNumFromat(wallet.getBalance()));
        }
    }

    private void initListener() {

    }

    @OnClick({R.id.rightBtn, R.id.ll_balance_top, R.id.tv_balance_one, R.id.tv_balance_two})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_balance_top: //余额明细
                Intent intentBalance = new Intent();
                intentBalance.setClass(context, WebViewClientActivity.class);
                intentBalance.putExtra(ExampleConfig.WB_URL_KEY, UrlConstant.H5_BALANCE_DETAIL);
                intentBalance.putExtra(ExampleConfig.WB_NAV_TITLE_KEY, "余额明细");
                startActivity(intentBalance);
                break;
            case R.id.rightBtn: //提现设置
                startActivity(WithdrawsCashSettingActivity.class);
                break;
            case R.id.tv_balance_one: //申请提现
                if (wallet != null && wallet.getDeposit() < 100) {
                    alertDialogOne.show();
                } else {
                    getAccountList();//获取账户信息
                }
                break;
            case R.id.tv_balance_two: //提现历史
                Intent intent = new Intent();
                intent.setClass(context, WebViewClientActivity.class);
                intent.putExtra(ExampleConfig.WB_URL_KEY, UrlConstant.H5_EXTRACE_HISTORY);
                intent.putExtra(ExampleConfig.WB_NAV_TITLE_KEY, "提现历史");
                startActivity(intent);
                break;
        }
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_balance;
    }

    /**
     * 获取账户列表
     */
    private void getAccountList() {
        if (NetworkUtils.isConnected()) {
            proDialogHelps.showProDialog();
            Call<BaseResponseEntity<ArrayList<BalanceAccountListEntity>>> call = webService.balanceAccountList(ExampleConfig.token);
            call.enqueue(new SimpleCallBack<BaseResponseEntity<ArrayList<BalanceAccountListEntity>>>(this) {
                @Override
                public void onSuccess(Call<BaseResponseEntity<ArrayList<BalanceAccountListEntity>>> call, Response<BaseResponseEntity<ArrayList<BalanceAccountListEntity>>> response) {
                    ArrayList<BalanceAccountListEntity> accountLists = response.body().getData();
                    if (accountLists != null && !accountLists.isEmpty()) {//有账户 直接体现
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("wallet", wallet);
                        bundle.putSerializable("walletAccount", accountLists.get(0));
                        bundle.putString(ExampleConfig.FROM,BalanceActivity.class.getSimpleName()); //类的简写名称
                        Intent intent = new Intent(BalanceActivity.this,WithdrawsCashActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {//没账户 弹对话框 设置账户
                        alertDialogTwo.show();
                    }
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
