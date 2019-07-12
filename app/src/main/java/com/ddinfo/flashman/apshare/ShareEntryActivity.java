package com.ddinfo.flashman.apshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.alipay.share.sdk.openapi.APAPIFactory;
import com.alipay.share.sdk.openapi.BaseReq;
import com.alipay.share.sdk.openapi.BaseResp;
import com.alipay.share.sdk.openapi.IAPAPIEventHandler;
import com.alipay.share.sdk.openapi.IAPApi;
import com.ddinfo.flashman.constant.ExampleConfig;

public class ShareEntryActivity extends Activity implements IAPAPIEventHandler {

    private IAPApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //创建工具对象实例，此处的APPID为上文提到的，申请应用生效后，在应用详情页中可以查到的支付宝应用唯一标识
        api = APAPIFactory.createZFBApi(getApplicationContext(), ExampleConfig.ALPAY_APP_ID, false);
        Intent intent = getIntent();
        if (api == null) return;
        //通过调用工具实例提供的handleIntent方法，绑定消息处理对象实例，
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (api == null) return;
        api.handleIntent(intent, this);
    }

    @Override
    public void onResp(BaseResp baseResp) {
        String result;
        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = "发送成功";
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = "发送取消";
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = "权限验证失败";
                break;
            case BaseResp.ErrCode.ERR_SENT_FAILED:
                result = "发送失败";
                break;
            default:
                result = "未知错误";
                break;
        }
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        finish();
    }
}
