package com.ddinfo.flashman.activity.task;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.CheckPayStatusEntity;
import com.ddinfo.flashman.models.params.CheckPayStatusParams;
import com.ddinfo.flashman.network.SimpleCallBack;
import com.ddinfo.flashman.utils.QRBitMapUtil;
import com.google.zxing.WriterException;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 收款布局界面
 */
public class TaskIncomeActivity extends BaseActivity {

    @Bind(R.id.iv_QRcode)       //二维码显示图片
    ImageView ivQRcode;
    @Bind(R.id.tv_order_id)     //订单号
    TextView tvOrderId;
    @Bind(R.id.tv_order_income) //金额
    TextView tvOrderIncome;
    @Bind(R.id.ll_order_id)     //订单号布局
    LinearLayout llOrderId;
    @Bind(R.id.ll_amount)       //金额布局
    LinearLayout llAmount;
    @Bind(R.id.tv_branch_order_id) //分流订单号
    TextView tvBranchOrderId;
    @Bind(R.id.ll_branch_order_id) //分流订单号布局
    LinearLayout llBranchOrderId;


    public static final int TYPE_ALIPAY = 0x001; //支付宝
    public static final int TYPE_WXPAY = 0x002;  //微信
    public static final String URL_CODE = "URL_CODE"; //二维码地址
    public static final String PAY_ID = "PAY_ID";     //支付ID

    public String urlCode;

    private int intentType; //支付方式
    private Timer mTimer;   //定时计时器
    private String payId;   //支付ID

    private CheckPayStatusEntity mData;
    private String orderId;
    private String branchOrderId;
    private String orderAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        intentType = getIntent().getExtras().getInt(ExampleConfig.INTENT_TYPE,-1); //获取传过来的支付方式

        switch (intentType) {
            case TYPE_ALIPAY:
                setTitle("支付宝收款");
                break;
            case TYPE_WXPAY:
                setTitle("微信收款");
                break;
        }

        urlCode = getIntent().getExtras().getString(URL_CODE);//二维码
        payId = getIntent().getExtras().getString(PAY_ID);//支付ID

        orderId = getIntent().getExtras().getString("orderId");//订单Id
        orderAmount = getIntent().getExtras().getString("orderAmount");//订单金额

        try {
            Bitmap www = QRBitMapUtil.createQRCode(urlCode, 600); //url地址生成二维码
            ivQRcode.setImageBitmap(www);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        llBranchOrderId.setVisibility(View.GONE); //隐蔽分流订单号布局
        llOrderId.setVisibility(View.GONE);       //隐蔽订单号布局
        llAmount.setVisibility(View.GONE);        //隐蔽金额布局

        if(null != orderId &&!TextUtils.isEmpty(orderId) &&  !orderId.equals("null")){
            llOrderId.setVisibility(View.VISIBLE); //显示订单号布局
            tvOrderId.setText(orderId+"");
        }

        if( null != branchOrderId && !TextUtils.isEmpty(branchOrderId)&& !branchOrderId.equals("null")){
            llBranchOrderId.setVisibility(View.VISIBLE); //显示分流订单号布局
            tvBranchOrderId.setText(branchOrderId+"");
        }

        if(null != orderAmount &&!TextUtils.isEmpty(orderAmount) &&  !orderAmount.equals("null")){
            llAmount.setVisibility(View.VISIBLE); //显示金额布局
            tvOrderIncome.setText("￥" + orderAmount);
        }



        //        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_logo);
        //        Bitmap www = EncodingHandler.createQRCode(urlCode, 600, 600, bitmap);
        //        ivQRcode.setImageBitmap(www);
//    if (mOrderDatailData != null) {
//      //tvOrderId.setText(mOrderDatailData.getOrderId() + "");
//      tvOrderIncome.setText("￥" + mOrderDatailData.getOrderAmount());
//    } else if (mOrderData != null) {
//      tvOrderId.setText(mOrderData.getOrderId() + "");
//      tvOrderIncome.setText("￥" + mOrderData.getOrderAmount());
//    }
    }

    private void initData() {

        //方式 1：接口轮询扫码结果

        //1.1 全局避免被杀死方案
        //        IncomeResuleService.count = 0;
        //        PollUtil.startPollingService(context,5,IncomeResuleService.class,IncomeResuleService.ACTION);
        //1.2 仅在本页面轮询
        mTimer = new Timer();
        startTimerTask();

        //方式 2：Socket长连接
        //        initSocket();
    }

    private void initListener() {

    }

    private void startTimerTask() {
        mTimer.schedule(new TimerTask() { //时间等于或者超过2000time首次执行task，之后每隔5000period毫秒重复执行一次任务
            @Override
            public void run() {
                CheckPayStatusParams params = new CheckPayStatusParams();
                params.setId(payId);
                Call<BaseResponseEntity<CheckPayStatusEntity>> callPayStatus =
                        webService.CheckPayOrderStatus(params);
                callPayStatus.enqueue(
                        new SimpleCallBack<BaseResponseEntity<CheckPayStatusEntity>>(context) {
                            @Override
                            public void onSuccess(Call<BaseResponseEntity<CheckPayStatusEntity>> call,
                                                  Response<BaseResponseEntity<CheckPayStatusEntity>> response) {
                                mData = response.body().getData();
                                boolean isOK= mData!=null&&mData.getHasPay() == 1;
                                if (isOK) {
                                    mTimer.cancel(); //清除计时任务

                                    Bundle bundle = new Bundle();
                                    Intent intent =
                                            new Intent(TaskIncomeActivity.this, TaskIncomeResultActivity.class);
                                    bundle.putSerializable(TaskIncomeResultActivity.TASK_RESULT_MODEL, mData);
                                    bundle.putString(TaskIncomeResultActivity.PAY_TYPE,
                                            intentType == TYPE_ALIPAY ? "支付宝" : "微信");
                                    try {
                                        bundle.putString(TaskIncomeResultActivity.ORDER_ID,
                                                orderId);
                                    } catch (NullPointerException e) {
                                        //bundle.putString(TaskIncomeResultActivity.ORDER_ID,
                                        //mOrderDatailData.getOrderId() + "");
                                    }
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                    TaskIncomeActivity.this.finish();
                                }
                            }

                            @Override
                            public void onProDialogDismiss() {

                            }
                        });
            }
        }, 2000, 5000);
    }

    //    private void initSocket(){
    //        socketManager = SocketManager.getSocketManage();
    //        socketManager.getSocket().on(Socket.EVENT_CONNECT, onConnect);
    //        socketManager.getSocket().on(Socket.EVENT_DISCONNECT, onDisconnect);
    //        socketManager.getSocket().on(Socket.EVENT_CONNECT_ERROR, onConnectError);
    //        socketManager.getSocket().on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
    //        socketManager.getSocket().on("some message", onResultReceiver);
    //    }

    //    private Emitter.Listener onConnect = new Emitter.Listener() {
    //        @Override
    //        public void call(final Object... args) {
    //            runOnUiThread(new Runnable() {
    //                @Override
    //                public void run() {
    //                    ToastUtils.showShortToast("连接成功！" + args.toString());
    //                }
    //            });
    //        }
    //    };
    //
    //    private Emitter.Listener onDisconnect = new Emitter.Listener() {
    //        @Override
    //        public void call(final Object... args) {
    //            runOnUiThread(new Runnable() {
    //                @Override
    //                public void run() {
    //                    ToastUtils.showShortToast("断开连接！" + args.toString());
    //                }
    //            });
    //        }
    //    };
    //
    //    private Emitter.Listener onConnectError = new Emitter.Listener() {
    //        @Override
    //        public void call(final Object... args) {
    //            runOnUiThread(new Runnable() {
    //                @Override
    //                public void run() {
    //                    ToastUtils.showShortToast("连接失败！" + args.toString());
    //                }
    //            });
    //        }
    //    };
    //
    //    private Emitter.Listener onResultReceiver = new Emitter.Listener() {
    //        @Override
    //        public void call(final Object... args) {
    //            runOnUiThread(new Runnable() {
    //                @Override
    //                public void run() {
    //                    JSONObject obj = (JSONObject) args[0];
    //                    Gson gson = new Gson();
    //                    try {
    ////                        WeiPayEntity model = gson.fromJson(obj.toString(), WeiPayEntity.class);
    ////                        System.out.println(obj.toString());
    ////                        if (model != null && model.getStatus() == 1) {
    ////                            if (orderId.equals(model.getId())) {
    ////                                if (checkPriceIsEqual(price, model.getSum())) {
    ////                                    EventBus.getDefault().post(new HomeRefreshEvent());
    ////                                    startActivity(new Intent(WeixinActivity.this, PaySuccessActivity.class).putExtra("details", model));
    ////                                    finish();
    ////                                } else
    ////                                    ToastUtils.showShortToast(WeixinActivity.this, "支付失败");
    ////                            }
    ////                        } else {
    ////                            ToastUtils.showShortToast(WeixinActivity.this, (model == null || TextUtils.isEmpty(model.getMessage())) ? "支付失败" : model.getMessage());
    ////                        }
    //                    } catch (Exception e) {
    //                        e.printStackTrace();
    //                    }
    //                }
    //            });
    //        }
    //    };

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mTimer!=null){
            mTimer.cancel();
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_task_income;
    }
}
