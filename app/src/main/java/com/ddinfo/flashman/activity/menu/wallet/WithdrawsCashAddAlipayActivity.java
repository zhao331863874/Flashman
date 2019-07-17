package com.ddinfo.flashman.activity.menu.wallet;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.utils.NetworkUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.params.AddAlipayAccountParams;
import com.ddinfo.flashman.network.SimpleCallBack;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 添加提现账户
 */
public class WithdrawsCashAddAlipayActivity extends BaseActivity {

    @Bind(R.id.name_et)   //姓名
    EditText nameEt;
    @Bind(R.id.alipay_et) //支付宝账号
    EditText alipayEt;
    @Bind(R.id.ok_btn)    //完成
    Button okBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        setTitle("添加支付宝账户");
    }

    private void initData() {
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_add_alipay;
    }

    @OnClick(R.id.ok_btn) //点击完成
    public void onClick() {
        if (TextUtils.isEmpty(nameEt.getText().toString().trim()) || TextUtils.isEmpty(alipayEt.getText().toString().trim())) {
            ToastUtils.showShortToastSafe("姓名和账户不能为空");
            return;
        }
        addWalletAccountInfo();
    }

    /**
     * 获取提现账户 信息
     */
    private void addWalletAccountInfo() {
        if (NetworkUtils.isConnected()) {
            proDialogHelps.showProDialog();
            AddAlipayAccountParams params = new AddAlipayAccountParams(alipayEt.getText().toString().trim(), nameEt.getText().toString().trim());
            Call<BaseResponseEntity> call = webService.addAlipayAccount(ExampleConfig.token, params);
            call.enqueue(new SimpleCallBack<BaseResponseEntity>(this) {
                @Override
                public void onSuccess(Call<BaseResponseEntity> call, Response<BaseResponseEntity> response) {
                    ToastUtils.showShortToastSafe("绑定成功");
                    finish();
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
