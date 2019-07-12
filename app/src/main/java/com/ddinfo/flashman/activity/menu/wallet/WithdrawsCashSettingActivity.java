package com.ddinfo.flashman.activity.menu.wallet;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.NetworkUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.models.BalanceAccountListEntity;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.network.SimpleCallBack;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class WithdrawsCashSettingActivity extends BaseActivity {

    @Bind(R.id.btn_add_account)
    Button btnAddAccount;
    @Bind(R.id.tv_alipay_account)
    TextView tvAlipayAccount;
    @Bind(R.id.rel_alipay_account)
    RelativeLayout relAlipayAccount;
    ArrayList<BalanceAccountListEntity> accountLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {
        setTitle("提现设置");
    }

    private void initData() {
        relAlipayAccount.setVisibility(View.GONE);
        getAccountList();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_withdraws_cash_setting;
    }


    @OnClick({R.id.btn_add_account, R.id.rel_alipay_account})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_account:
                startActivity(WithdrawsCashAddAlipayActivity.class);
                break;
            case R.id.rel_alipay_account:
                new AlertDialog.Builder(context)
                        .setTitle("解除绑定")
                        .setMessage("是否确认解除支付宝提现账户绑定？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (accountLists != null && !accountLists.isEmpty() && accountLists.size() > 0) {
                            delAlipayAccount(accountLists.get(0).getId());
                        } else {
                            ToastUtils.showShortToastSafe("解绑失败");
                        }
                        dialog.dismiss();
                    }
                }).show();
                break;
        }

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
                    accountLists = response.body().getData();
                    if (accountLists != null && !accountLists.isEmpty()) {//有账户 直接体现
                        relAlipayAccount.setVisibility(View.VISIBLE);
                        btnAddAccount.setVisibility(View.GONE);
                        tvAlipayAccount.setText(accountLists.get(0).getAccountNo());
                    } else {//没账户 可以添加设置账户
                        relAlipayAccount.setVisibility(View.GONE);
                        btnAddAccount.setVisibility(View.VISIBLE);
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

    /**
     * 解除账户绑定
     */
    private void delAlipayAccount(String id) {
        if (NetworkUtils.isConnected()) {
            proDialogHelps.showProDialog();
            Call<BaseResponseEntity> call = webService.delAlipayAccount(ExampleConfig.token, id);
            call.enqueue(new SimpleCallBack<BaseResponseEntity>(this) {
                @Override
                public void onSuccess(Call<BaseResponseEntity> call, Response<BaseResponseEntity> response) {
                    relAlipayAccount.setVisibility(View.GONE);
                    btnAddAccount.setVisibility(View.VISIBLE);
                    ToastUtils.showShortToastSafe("账号解绑成功");
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
