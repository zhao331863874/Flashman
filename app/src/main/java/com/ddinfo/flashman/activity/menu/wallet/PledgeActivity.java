package com.ddinfo.flashman.activity.menu.wallet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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

public class PledgeActivity extends BaseActivity {


    @Bind(R.id.left_button)
    ImageButton leftButton;
    @Bind(R.id.header_name)
    TextView headerName;
    @Bind(R.id.right_button)
    ImageButton rightButton;
    @Bind(R.id.rightBtn)
    Button rightBtn;
    @Bind(R.id.tv_pledge)
    TextView tvPledge;
    @Bind(R.id.ll_pledge_top)
    LinearLayout llPledgeTop;
    @Bind(R.id.tv_pledge_one)
    TextView tvPledgeOne;
    @Bind(R.id.apply_withdraw_crash)
    TextView tvPledgeTwo;
    @Bind(R.id.activity_balance)
    LinearLayout activityBalance;
    WalletEntity wallet;

    private AlertDialog.Builder builderOne;
    private AlertDialog.Builder builderTwo;
    private AlertDialog alertDialogOne;//提示充值押金
    private AlertDialog alertDialogTwo;//提示未绑定账户

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBuilder();
        initView();
        initData();
        initListener();
    }

    private void initView() {
        setTitle("押金");

    }

    private void initBuilder() {
        builderOne = new AlertDialog.Builder(context);
        builderOne.setTitle("您的押金少于100元");
        builderOne.setMessage("当前押金少于100元\n猪行者建议你充值一定金额的押金，以免影响你正常接单。");

        builderOne.setPositiveButton("继续提现", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                widthDrawCash();
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
            tvPledge.setText(Utils.jointNumFromat(wallet.getDeposit()));
        }
    }

    /**
     * 提现之前需要绑定账号
     */
    private void widthDrawCash() {
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
                        bundle.putString(ExampleConfig.FROM,PledgeActivity.class.getSimpleName());
                        Intent intent = new Intent(PledgeActivity.this,WithdrawsCashActivity.class);
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

    private void initListener() {

    }

    @OnClick({R.id.ll_pledge_top, R.id.tv_pledge_one, R.id.apply_withdraw_crash,R.id.history_withdraw_crash})
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.ll_pledge_top:
                intent.setClass(context, WebViewClientActivity.class);
                intent.putExtra(ExampleConfig.WB_URL_KEY, UrlConstant.H5_FREEZE_DETAIL);
                intent.putExtra(ExampleConfig.WB_NAV_TITLE_KEY, "冻结明细");
                startActivity(intent);
                break;
            case R.id.tv_pledge_one: //押金充值
                Bundle bunder1 = new Bundle();
                bunder1.putSerializable("wallet", wallet);

                intent.setClass(context, TopUpInputActivity.class);
                intent.putExtras(bunder1);
                startActivity(intent);
                break;
            case R.id.apply_withdraw_crash:
                //押金低于100友好提示
                if (wallet != null && wallet.getDeposit() < 100) {
                    alertDialogOne.show();
                } else {
                    widthDrawCash();
                }
                break;
            case R.id.history_withdraw_crash:
                intent.setClass(context,WithDrawsHistoryActivity.class);
                startActivity(intent);
                break;
        }
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_pledge;
    }
}
