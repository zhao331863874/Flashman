package com.ddinfo.flashman.activity.menu.wallet;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.share.sdk.openapi.APAPIFactory;
import com.alipay.share.sdk.openapi.APMediaMessage;
import com.alipay.share.sdk.openapi.APWebPageObject;
import com.alipay.share.sdk.openapi.IAPApi;
import com.alipay.share.sdk.openapi.SendMessageToZFB;
import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.NetworkUtils;
import com.blankj.utilcode.utils.SizeUtils;
import com.blankj.utilcode.utils.TimeUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.application.MyApplication;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.dapay.alipay.AlipayAPI;
import com.ddinfo.flashman.dapay.wechatpay.WechatPayAPI;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.PayOrderResponseEntity;
import com.ddinfo.flashman.models.PayOrderResult;
import com.ddinfo.flashman.models.params.GoodsPayParams;
import com.ddinfo.flashman.models.params.RechargeParams;
import com.ddinfo.flashman.network.SimpleCallBack;
import com.ddinfo.flashman.utils.ExitUtil;
import com.ddinfo.flashman.utils.QRBitMapUtil;
import com.google.gson.Gson;
import com.google.zxing.WriterException;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 确认支付金额界面
 */
public class PayOrderActivity extends BaseActivity {

    private static final int payWayAli = 201;
    private static final int payWayWx = 202;
    private static final String payWay_Ali = "alipay";
    private static final String payWay_Wx = "wechat";
    public static final String TRACK_PAY_ORDER_RECORD="TRACK_PAY_ORDER_RECORD";
    public static IWXAPI api;

    @Bind(R.id.left_button)   //返回按钮
    ImageButton leftButton;
    @Bind(R.id.header_name)   //标题抬头
    TextView headerName;
    @Bind(R.id.right_button)
    ImageButton rightButton;
    @Bind(R.id.rightBtn)
    Button rightBtn;
    @Bind(R.id.tv_pay_money)  //充值金额
    TextView tvPayMoney;
    @Bind(R.id.tv_enter)      //确认支付
    TextView tvEnter;
    @Bind(R.id.tv_last_time)  //剩余支付时间
    TextView tvLastTime;
    @Bind(R.id.iv_sel_alipay) //支付宝选中图标
    ImageView ivSelAlipay;
    @Bind(R.id.rl_alipay)     //支付宝选项布局
    RelativeLayout rlAlipay;
    @Bind(R.id.iv_sel_wechat) //微信选中图标
    ImageView ivSelWechat;
    @Bind(R.id.rl_wechatpay)  //微信选项布局
    RelativeLayout rlWechatpay;

    private ArrayList<String> payMethods = new ArrayList<>(); //支付方式
    private String type;
    private Dialog popUpDialog;
    private Timer payTimer;

    private double topUpMoney;    //充值金额
    private int deliveryOrderIds; //交货订单ID
    private int payWayCur = -1;
    private long lastTime;


    /**
     * 倒计时默认30分钟
     */
    CountDownTimer timer = new CountDownTimer(1000 * 60 * 30, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            tvLastTime.setText(TimeUtils.millis2String(millisUntilFinished, "mm:ss"));
            lastTime = millisUntilFinished;
        }

        @Override
        public void onFinish() {
            tvLastTime.setText("00:00");
            lastTime = 0;

        }
    };

    Callback<BaseResponseEntity<ArrayList<String>>> callbackPayMethod = new SimpleCallBack<BaseResponseEntity<ArrayList<String>>>(context) {
        @Override
        public void onSuccess(Call<BaseResponseEntity<ArrayList<String>>> call, Response<BaseResponseEntity<ArrayList<String>>> response) {
            payMethods = response.body().getData();

            if (payMethods.size() == 1) { //如果支付方式仅有一个，则默认选中
                switch (payMethods.get(0)) {
                    case "alipay":
                        rlAlipay.setVisibility(View.VISIBLE);
                        checkOffPayWay(payWayAli);
                        break;
                    case "wepay":
                        rlWechatpay.setVisibility(View.VISIBLE);
                        checkOffPayWay(payWayWx);
                        break;
                }
            } else {
                for (String s : payMethods) {
                    switch (s) {
                        case "alipay":
                            rlAlipay.setVisibility(View.VISIBLE);
                            break;
                        case "wepay":
                            rlWechatpay.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            }
        }

        @Override
        public void onProDialogDismiss() {

            proDialogHelps.removeProDialog();
        }
    };

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_pay_money_enter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, ExampleConfig.WX_APP_ID, false);
        // 将该app注册到微信
        api.registerApp(ExampleConfig.WX_APP_ID);
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeTrackOrderState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();

        closePayStateRecord();
        if(payTimer!=null) payTimer.cancel();
    }

    @OnClick({R.id.tv_enter, R.id.rl_alipay, R.id.rl_wechatpay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_enter: //确认支付
                if (lastTime != 0) {
                    if (deliveryOrderIds != -1) {
                        //交货款
                        goodsPay(payWayCur == payWayAli ? payWayAli : payWayWx);
                    } else {
                        //押金充值
                        cashRecharge(payWayCur == payWayAli ? payWayAli : payWayWx);
                    }
                } else {
                    ToastUtils.showShortToastSafe("支付过期");
                }
                break;
            case R.id.rl_alipay:
                checkOffPayWay(payWayAli);
                break;
            case R.id.rl_wechatpay:
                checkOffPayWay(payWayWx);
                break;
        }
    }

    private void initView() {
        setTitle("确认支付金额");
        deliveryOrderIds = getIntent().getIntExtra("id", -1); //交货款有其他为-1
        type = getIntent().getExtras().getString("type"); //chargeDeposit 押金充值，deliveryManPay 交货款
        topUpMoney = getIntent().getExtras().getDouble(ExampleConfig.INTENT_TOPUPMONEY);
        tvPayMoney.setText(topUpMoney + "");
        timer.start();

        proDialogHelps.showProDialog("获取支付模式...");
        switch (type) {
            case "chargeDeposit": //押金充值
                webService.callPayMethod("chargeDeposit").enqueue(callbackPayMethod);
                break;
            case "deliveryManPay": //交货款
                webService.callPayMethod("deliveryManPay").enqueue(callbackPayMethod);
                break;
        }
    }

    private void startRegularTimeQueryPayOrderState(final String payOrderNo) { //轮循查询付款状态
        if(TextUtils.isEmpty(payOrderNo)) return;

        if(payTimer!=null) payTimer.cancel();
        payTimer = new Timer();
        payTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Call<BaseResponseEntity<PayOrderResult>> callPayStatus =
                        webService.checkBatchPayOrderStatus(payOrderNo); //查看交货款订单状态
                callPayStatus.enqueue(
                        new SimpleCallBack<BaseResponseEntity<PayOrderResult>>(context) {
                            @Override
                            public void onSuccess(Call<BaseResponseEntity<PayOrderResult>> call,
                                                  Response<BaseResponseEntity<PayOrderResult>> response) {
                                PayOrderResult mData = response.body().getData();
                                //LogUtils.d("PayOrderActivity 定时查询订单结果",new Gson().toJson(response.body()));
                                boolean isPayOk = mData!=null && mData.isPayOk();
                                if (isPayOk) {
                                    AlertDialog.Builder  builder = new AlertDialog.Builder(PayOrderActivity.this);
                                    builder.setMessage("支付成功");
                                    builder.setTitle("提示");

                                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //退出所有页面然后进入我的钱包页面
                                            dialog.dismiss();
                                            if(popUpDialog!=null) popUpDialog.dismiss();
                                            ExitUtil.getInstance().exitToWalletActivity();
                                            startActivity(WalletActivity.class);
                                        }
                                    });
                                    builder.show();

                                    closePayStateRecord();
                                    payTimer.cancel();
                                }
                            }

                            @Override
                            public void onProDialogDismiss() {

                            }
                        });
            }
        }, 2000, 5000);
    }

    private void checkOffPayWay(int toPayWay) {
        switch (toPayWay) {
            case payWayAli:
                ivSelAlipay.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.icon_sel_pressed));
                ivSelWechat.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.icon_sel_unpress));
                payWayCur = payWayAli;
                break;
            case payWayWx:
                ivSelAlipay.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.icon_sel_unpress));
                ivSelWechat.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.icon_sel_pressed));
                payWayCur = payWayWx;
                break;
        }
    }

    /**
     * 押金充值
     */
    private void cashRecharge(final int payType) {
        if (NetworkUtils.isConnected()) {
            proDialogHelps.showProDialog();
            Call<BaseResponseEntity<PayOrderResponseEntity>> call = webService.recharge(ExampleConfig.token, new RechargeParams(topUpMoney, payType == payWayAli ? payWay_Ali : payWay_Wx));
            call.enqueue(new SimpleCallBack<BaseResponseEntity<PayOrderResponseEntity>>(this) {
                @Override
                public void onSuccess(Call<BaseResponseEntity<PayOrderResponseEntity>> call, Response<BaseResponseEntity<PayOrderResponseEntity>> response) {
                    PayOrderResponseEntity alipayResponse = response.body().getData();
                    if (alipayResponse != null) {
                        switch (payType) {
                            case payWayAli:
                                if (!TextUtils.isEmpty(alipayResponse.getCodeUrl())) {
                                    AlipayAPI.alipay(PayOrderActivity.this, alipayResponse);
                                }
                                break;
                            case payWayWx:
                                if (alipayResponse.getAppParams() != null) {
                                    WechatPayAPI.pay(PayOrderActivity.this, alipayResponse);
                                }
                                break;
                        }
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
     * 交货款
     */
    private void goodsPay(final int payType) {
        if (NetworkUtils.isConnected()) {
            proDialogHelps.showProDialog();
            Call<BaseResponseEntity<PayOrderResponseEntity>> call = webService.goodsPayWeiJin(ExampleConfig.token, new GoodsPayParams(deliveryOrderIds, payType == payWayAli ? payWay_Ali : payWay_Wx));
            call.enqueue(new SimpleCallBack<BaseResponseEntity<PayOrderResponseEntity>>(this) {
                @Override
                public void onSuccess(Call<BaseResponseEntity<PayOrderResponseEntity>> call, Response<BaseResponseEntity<PayOrderResponseEntity>> response) {
                    //LogUtils.d("goodsPayWeiJin", new Gson().toJson(response.body()));
                    PayOrderResponseEntity alipayResponse = response.body().getData();
                    proDialogHelps.removeProDialog();
                    if (alipayResponse != null) {
                        final String codeUrl = alipayResponse.getCodeUrl(); //代付地址

                        String tradeRecordNo = alipayResponse.tradeRecordNo;
                        trackPayStateRecord(tradeRecordNo);
                        startRegularTimeQueryPayOrderState(tradeRecordNo);

                        switch (payType) {
                            case payWayAli:
                                initQrPopwindow("请使用支付宝扫码支付", codeUrl,false, new Runnable() {
                                    @Override
                                    public void run() {
                                        alipayShare(codeUrl, topUpMoney);
                                    }
                                });
                                break;
                            case payWayWx:
                                initQrPopwindow("请使用微信扫码支付", codeUrl,false, new Runnable() {
                                    @Override
                                    public void run() {
                                        weixinShare(codeUrl, topUpMoney);
                                    }
                                });
                                break;
                        }
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

    private void trackPayStateRecord(String payOrderId){ //跟踪付款状态记录
        if(TextUtils.isEmpty(payOrderId)) return;

        PayOrderStateRecord record=new PayOrderStateRecord(payOrderId);
        MyApplication.getSPUtilsInstance().putString(TRACK_PAY_ORDER_RECORD+'_'+deliveryOrderIds,new Gson().toJson(record));
    }

    private void closePayStateRecord(){ //关闭付款状态记录
        String recordStr=MyApplication.getSPUtilsInstance().getString(TRACK_PAY_ORDER_RECORD+'_'+deliveryOrderIds);
        if(!TextUtils.isEmpty(recordStr)){
            PayOrderStateRecord record = new Gson().fromJson(recordStr,PayOrderStateRecord.class);
            if(record==null) return;
            record.isOpen=false;
            MyApplication.getSPUtilsInstance().putString(TRACK_PAY_ORDER_RECORD+'_'+deliveryOrderIds,new Gson().toJson(record));
        }
    }

    private void resumeTrackOrderState(){ //恢复确认订单状态
        String recordStr=MyApplication.getSPUtilsInstance().getString(TRACK_PAY_ORDER_RECORD+'_'+deliveryOrderIds);
        if(!TextUtils.isEmpty(recordStr)){
            PayOrderStateRecord record = new Gson().fromJson(recordStr,PayOrderStateRecord.class);
            if(record==null) return;
            if(record.isOpen){
                if(payTimer!=null) payTimer.cancel();
                startRegularTimeQueryPayOrderState(record.tradeRecordNo);
            }
        }
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    public byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public void weixinShare(String codeUrl, double money) { //微信邀请
        if (TextUtils.isEmpty(codeUrl)) {
            Toast.makeText(context, "代付地址为空", Toast.LENGTH_SHORT).show();
        }
        if (codeUrl.startsWith("weixin://wxpay")) {
            // 初始化一个WXTextObject对象
            WXTextObject textObj = new WXTextObject();
            textObj.text = codeUrl;
            // 用WXTextObject对象初始化一个WXMediaMessage对象
            WXMediaMessage msg = new WXMediaMessage();
            msg.mediaObject = textObj;
            // 发送文本类型的消息时，title字段不起作用
            // msg.title = "Will be ignored";
            msg.description = "猪行侠交货款";//不显示在界面上
            // 构造一个Req
            SendMessageToWX.Req req = new SendMessageToWX.Req();
            req.transaction = buildTransaction("text"); // transaction字段用于唯一标识一个请求
            req.message = msg;
            req.scene = SendMessageToWX.Req.WXSceneSession;//发送到朋友圈  WXSceneSession会话
            // 调用api接口发送数据到微信
            api.sendReq(req);
        } else {
            IWXAPI api = WXAPIFactory.createWXAPI(context, ExampleConfig.WX_APP_ID);
            WXWebpageObject webPageObject = new WXWebpageObject();
            webPageObject.webpageUrl = codeUrl;

            WXMediaMessage webMessage = new WXMediaMessage();
            webMessage.title = "猪行侠交货款";
            webMessage.description = "交货款金额：" + money + "元";
            webMessage.mediaObject = webPageObject;
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo);
            webMessage.thumbData = bitmapToBytes(bitmap);
            SendMessageToWX.Req webReq = new SendMessageToWX.Req();
            webReq.message = webMessage;
            webReq.transaction = buildTransaction("webpage");
            webReq.scene = SendMessageToWX.Req.WXSceneSession;
            api.sendReq(webReq);
        }
    }

    public void alipayShare(String codeUrl, double money) { //支付宝邀请
        if (TextUtils.isEmpty(codeUrl)) {
            Toast.makeText(context, "代付地址为空", Toast.LENGTH_SHORT).show();
        }
        IAPApi api = APAPIFactory.createZFBApi(context, ExampleConfig.ALPAY_APP_ID, false);

        APWebPageObject webPageObject = new APWebPageObject();
        webPageObject.webpageUrl = codeUrl;

        APMediaMessage webMessage = new APMediaMessage();
        webMessage.title = "猪行侠交货款";
        webMessage.description = "交货款金额：" + money + "元";
        webMessage.mediaObject = webPageObject;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo);
        webMessage.thumbData = bitmapToBytes(bitmap);
        SendMessageToZFB.Req webReq = new SendMessageToZFB.Req();
        webReq.message = webMessage;
        webReq.transaction = buildTransaction("webpage");
        webReq.scene = SendMessageToZFB.Req.ZFBSceneSession;

        //调用api接口发送消息到支付宝
        api.sendReq(webReq);
    }

    /**
     * @param title 支付方式
     * @param urlCode 二维码地址
     * @param enableShare 邀好友支付开关
     * @param call
     */
    private void initQrPopwindow(String title, String urlCode,boolean enableShare, final Runnable call) {
        try {
            popUpDialog = new Dialog(activity);
            View contentView = getLayoutInflater().inflate(R.layout.pop_pay_weijin_qrcode,
                    (ViewGroup) activity.getWindow().getDecorView(), false);
            ImageView ivQrCode = (ImageView) contentView.findViewById(R.id.iv_qr_code); //二维码图片
            TextView tvTtile = (TextView) contentView.findViewById(R.id.tv_title);      //支付方式标题
            tvTtile.setText(title);
            View btnSend = contentView.findViewById(R.id.tv_send); //邀好友代付
            if(urlCode!=null){
                Bitmap www = QRBitMapUtil.createQRCode(urlCode, SizeUtils.dp2px(300));
                ivQrCode.setImageBitmap(www);
            }
            if(enableShare){
                btnSend.setVisibility(View.VISIBLE);
            }else{
                btnSend.setVisibility(View.GONE);
            }

            popUpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            popUpDialog.setCanceledOnTouchOutside(true);
            popUpDialog.setContentView(contentView, contentView.getLayoutParams());
            popUpDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            popUpDialog.show();

            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    call.run();
                    popUpDialog.dismiss();
                }
            });
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
    public class PayOrderStateRecord implements Serializable{
        public PayOrderStateRecord(String tradeRecordNo){
            this.tradeRecordNo=tradeRecordNo;
            isOpen=true;
        }
        public String tradeRecordNo; //交货款订单
        public boolean isOpen;       //付款状态记录开关
    }
}
