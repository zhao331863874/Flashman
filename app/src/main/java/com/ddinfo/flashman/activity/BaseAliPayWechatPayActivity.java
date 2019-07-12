package com.ddinfo.flashman.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.dapay.alipay.AlipayAPI;
import com.ddinfo.flashman.dapay.alipay.AuthResult;
import com.ddinfo.flashman.dapay.alipay.PayResult;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.Map;

public abstract class BaseAliPayWechatPayActivity extends BaseActivity {
    public static IWXAPI api;
    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "";

    /**
     * 支付宝账户登录授权业务：入参pid值
     */
    public static final String PID = "";
    /**
     * 支付宝账户登录授权业务：入参target_id值
     */
    public static final String TARGET_ID = "";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /**
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "";
    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(BaseAliPayWechatPayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(BaseAliPayWechatPayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(BaseAliPayWechatPayActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(BaseAliPayWechatPayActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, ExampleConfig.WX_APP_ID, false);
        // 将该app注册到微信
        api.registerApp(ExampleConfig.WX_APP_ID);
    }

    public void weixinPayTest() {
        if (!api.isWXAppInstalled()) {
            ToastUtils.showShortToastSafe("没有安装微信");
            return;
        }
        if (!api.isWXAppSupportAPI()) {
            ToastUtils.showShortToastSafe("当前版本不支持支付功能");
            return;
        }
        //支付过程三部走
        // 1.获取accessTonken;
        // 2.获取prePayId;
        // 3.发起支付; (建议将1和2放在服务器端处理，客户端负责提供必要的参数)
        PayReq request = new PayReq();
        request.appId = ExampleConfig.WX_APP_ID;
        request.partnerId = "1327244301"; //微信支付分配的商户号
        request.prepayId = "wx2016092009564343e737275f0904349502";//微信返回的支付交易会话ID
        request.packageValue = "Sign=WXPay";//暂填写固定值Sign=WXPay
        request.nonceStr = "70vhVHnJj6ph7mf9";//随机字符串，不长于32位。推荐随机数生成算法
        request.timeStamp = "1474336603";//时间戳，请见接口规则-参数规定
        request.sign = "815BC8D5508FD90F7A978856B2174E21";//签名，详见签名生成算法
        api.sendReq(request);
    }

    /**
     * 支付宝支付异步任务
     *
     */
    public class AliPayThread extends Thread {
        @Override
        public void run() {
            String result = AlipayAPI.pay(BaseAliPayWechatPayActivity.this, "测试的商品", "测试商品的详细描述", "0.01");
            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        }
    }

}
