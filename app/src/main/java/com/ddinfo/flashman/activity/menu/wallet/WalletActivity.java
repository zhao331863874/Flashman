package com.ddinfo.flashman.activity.menu.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.NetworkUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.activity.task.FrozenDetailActivity;
import com.ddinfo.flashman.activity.webview.WebViewClientActivity;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.constant.UrlConstant;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.WalletEntity;
import com.ddinfo.flashman.network.SimpleCallBack;
import com.ddinfo.flashman.utils.Utils;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 我的钱包界面
 */
public class WalletActivity extends BaseActivity {

    @Bind(R.id.left_button) //左返回按钮
    ImageButton leftButton;
    @Bind(R.id.header_name) //抬头标题
    TextView headerName;
    @Bind(R.id.right_button)
    ImageButton rightButton;
    @Bind(R.id.rightBtn)
    Button rightBtn;
    @Bind(R.id.tv_balance)  //余额
    TextView tvBalance;
    @Bind(R.id.rl_balance)  //余额布局
    RelativeLayout rlBalance;
    @Bind(R.id.tv_pledge)   //押金
    TextView tvPledge;
    @Bind(R.id.rl_pledge)   //押金布局
    RelativeLayout rlPledge;
    @Bind(R.id.activity_purse)
    LinearLayout activityPurse;
    @Bind(R.id.tv_credit)   //授信额度
    TextView tvCredit;
    @Bind(R.id.tv_total_credit) //可接货额度
    TextView tvTotalCredit;
    @Bind(R.id.tv_frozen)   //冻结额度
    TextView tvFrozen;

    WalletEntity data;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setTitle("我的钱包");


    }

    private void initData() {
        getWalletInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @OnClick({R.id.rl_balance, R.id.rl_pledge,R.id.rl_total_credit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_balance: //点击余额
                Bundle bunder = new Bundle();
                bunder.putSerializable("wallet", data);
                startActivity(BalanceActivity.class, bunder);
                break;
            case R.id.rl_pledge: //点击押金
                Bundle pledgeBunder = new Bundle();
                pledgeBunder.putSerializable("wallet", data);
                startActivity(PledgeActivity.class, pledgeBunder);
                break;
            case R.id.rl_total_credit: //点击可接货额度
                startActivity(FrozenDetailActivity.class);
                break;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_purse;
    }

    /**
     * 查询钱包余额 押金 信息
     */
    private void getWalletInfo() {
        if (NetworkUtils.isConnected()) {
            proDialogHelps.showProDialog();
            Call<BaseResponseEntity<WalletEntity>> call = webService.myWalletInfo(ExampleConfig.token);
            call.enqueue(new SimpleCallBack<BaseResponseEntity<WalletEntity>>(this) {
                @Override
                public void onSuccess(Call<BaseResponseEntity<WalletEntity>> call, Response<BaseResponseEntity<WalletEntity>> response) {
                    data = response.body().getData();
                    if (data != null) {
                        setWalletInfo(data);
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

    public void setWalletInfo(WalletEntity walletInfo) { //设置钱包信息
        try {
            tvBalance.setText(Utils.jointNumFromat(walletInfo.getBalance()));
            tvPledge.setText(Utils.jointNumFromat(walletInfo.getDeposit()));
            tvCredit.setText(Utils.jointNumFromat(walletInfo.getCreditAmount()));
            tvTotalCredit.setText(Utils.jointNumFromat(walletInfo.getUsable()));
            if(walletInfo.getFrozenAmount()!=0){
                tvFrozen.setVisibility(View.VISIBLE); //显示冻结额度
                tvFrozen.setText("(冻结" + Utils.jointNumFromat(walletInfo.getFrozenAmount()) + ")");
            }else{
                tvFrozen.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
