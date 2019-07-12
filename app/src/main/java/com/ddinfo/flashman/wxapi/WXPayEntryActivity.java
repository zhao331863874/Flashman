package com.ddinfo.flashman.wxapi;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.utils.ExitUtil;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    private static final String TAG = "wechartPay";
    public IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, ExampleConfig.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {
                ToastUtils.showShortToastSafe("支付成功");
                ExitUtil.getInstance().exitToWalletActivity();
            } else {
                ToastUtils.showShortToastSafe(String.valueOf(resp.errCode));
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("标题");
//                builder.setMessage();
//                builder.show();
            }
            finish();
        }
    }
}
