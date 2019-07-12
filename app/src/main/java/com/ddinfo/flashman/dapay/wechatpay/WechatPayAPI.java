package com.ddinfo.flashman.dapay.wechatpay;

import android.content.Context;

import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.models.PayOrderResponseEntity;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by weitf on 2017/1/17.
 */
public class WechatPayAPI {
    /**
     * 生成订单的方法
     *
     * @param tradeNo  交易号
     * @param totalFee 支付金额
     * @param subject  详细描述
     * @return
     */
    public static String createOrder(String tradeNo, String totalFee, String subject) {
        String result = "";
        String URL_PREPAY = ExampleConfig.URL_PAY_CALLBACK + "/UnifiedOrderServlet";
        try {
            subject = URLEncoder.encode(subject, "UTF-8");
            String url = URL_PREPAY + "?trade_no=" + tradeNo + "&total_fee=" + totalFee + "&subject=" + subject;
            result = HttpUtils.doGet(url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 支付的方法
     *
     * @param context
     * @param order   服务器生成订单返回的
     */
    public static void pay(Context context, PayOrderResponseEntity order) {
        PayOrderResponseEntity.AppParamsBean mWxInfo = order.getAppParams();
        IWXAPI api = WXAPIFactory.createWXAPI(context, ExampleConfig.WX_APP_ID); // 将该app注册到微信
        if (!api.isWXAppInstalled()) {
            ToastUtils.showShortToastSafe("没有安装微信");
            return;
        }
        if (!api.isWXAppSupportAPI()) {
            ToastUtils.showShortToastSafe("当前版本不支持支付功能");
            return;
        }
        PayReq req = new PayReq();
        req.appId = ExampleConfig.WX_APP_ID;
        req.partnerId = mWxInfo.getPartnerid();
        req.prepayId = mWxInfo.getPrepayid();
        req.nonceStr = mWxInfo.getNoncestr();
        req.timeStamp = mWxInfo.getTimestamp();
        req.packageValue = "Sign=WXPay";
        req.sign = mWxInfo.getSign();
        api.sendReq(req);
    }
}
