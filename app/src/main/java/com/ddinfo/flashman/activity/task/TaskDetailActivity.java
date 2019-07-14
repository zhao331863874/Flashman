package com.ddinfo.flashman.activity.task;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.Bind;

import com.blankj.utilcode.utils.NetworkUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.bumptech.glide.Glide;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.CaptureActivity;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.application.MyApplication;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.helps.TaskRefuseDialog;
import com.ddinfo.flashman.helps.TaskSendDialog;
import com.ddinfo.flashman.location.DdmalBDLocation;
import com.ddinfo.flashman.location.LocationService;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.OrderDetailEntity;
import com.ddinfo.flashman.models.OrderDetailEntityV1;
import com.ddinfo.flashman.models.PayResultEntity;
import com.ddinfo.flashman.models.params.DeliveryOrderParams;
import com.ddinfo.flashman.models.params.GoodsGetParams;
import com.ddinfo.flashman.models.params.GoodsRefuseV2Params;
import com.ddinfo.flashman.models.params.GoodsSendParams;
import com.ddinfo.flashman.network.SimpleCallBack;

import java.util.ArrayList;
import java.util.List;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 单项任务详情界面
 */
public class TaskDetailActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private static final int REQUEST_GETGOODS = 1;

    // 整数: 李占晓 送达 static
    private static final int TYPE_GOODSSEND = 0x11;
    // 整数: 李占晓 拒收 static
    private static final int TYPE_GOODSREFUSE = 0x22;

    public static final String PAYTYPE_CASH = "cash";     //现金
    public static final String PAYTYPE_ALIPAY = "alipay"; //支付宝
    public static final String PAYTYPE_WXPAY = "wepay";   //微信

    private PayResultEntity payResultEntity; //支付请求返回数据
    private String urlCode;       //生成二维码地址
    private String mPayType = ""; //支付方式

    @Bind(R.id.left_button)   //返回按钮
    ImageButton leftButton;
    @Bind(R.id.header_name)   //标题抬头
    TextView headerName;
    @Bind(R.id.right_button)
    ImageButton rightButton;
    @Bind(R.id.rightBtn)
    Button rightBtn;
    //@Bind(R.id.tv_detail_type) TextView tvDetailType;
    //@Bind(R.id.ll_detasil_type) LinearLayout llDetasilType;
    @Bind(R.id.tv_address_take) //取货地址
    TextView tvAddressTake;
    @Bind(R.id.tv_time_take)    //取货时间
    TextView tvTimeTake;
    @Bind(R.id.tv_map_ditu)     //地图
    TextView tvMapDitu;
    @Bind(R.id.tv_map_dingwei)  //距离
    TextView tvMapDingwei;
    @Bind(R.id.img_map_shoppre) //店铺图片
    ImageView imgMapShoppre;
    @Bind(R.id.ll_detail_map)   //地图布局
    LinearLayout llDetailMap;
    @Bind(R.id.tv_store_name)   //店铺名称
    TextView tvStoreName;
    @Bind(R.id.tv_store_lbname) //店铺老板名称
    TextView tvStoreLbname;
    @Bind(R.id.tv_store_phoneNum)  //店铺老板电话
    TextView tvStorePhoneNum;
    @Bind(R.id.ll_detail_name)
    LinearLayout llDetailName;
    @Bind(R.id.tv_address_send)    //收货地址？
    TextView tvAddressSend;
    @Bind(R.id.tv_hope_time_title) //取货
    TextView tvHopeTimeTitle;
    @Bind(R.id.tv_hope_time)       //取货时间
    TextView tvHopeTime;
    @Bind(R.id.ll_detail_mes)
    LinearLayout llDetailMes;
    @Bind(R.id.tv_order_mes)    //订单备注
    TextView tvOrderMes;
    @Bind(R.id.tv_order_money)  //订单金额
    TextView tvOrderMoney;
    @Bind(R.id.tv_task_id)      //运单编号
    TextView tvTaskId;
    @Bind(R.id.tv_order_id)     //订单编号
    TextView tvOrderId;
    @Bind(R.id.ll_detail_invoiceId)//发货单号布局
    LinearLayout llDetailInvoiceId;
    @Bind(R.id.tv_order_invoiceId) //发货单号
    TextView tvOrderInvoiceId;
    @Bind(R.id.tv_income)       //预期收入
    TextView tvIncome;
    @Bind(R.id.activity_task_detail)
    LinearLayout activityTaskDetail;
    @Bind(R.id.sv_task_detail)
    ScrollView svTaskDetail;
    @Bind(R.id.tv_center)       //扫码二维码
    TextView tvCenter;
    @Bind(R.id.tv_left)         //送达
    TextView tvLeft;
    @Bind(R.id.tv_new_left)     //部分送达
    TextView tvNewLeft;
    @Bind(R.id.tv_right)        //拒收
    TextView tvRight;
    @Bind(R.id.ll_task_bottom)  //送达拒收布局
    LinearLayout llTaskBottom;
    @Bind(R.id.ll_detail_button)//扫码二维码及送达拒收布局
    LinearLayout llDetailButton;
    private int mOrderId;
    private int mPosition;      //当前选卡位置
    private int state;          //任务状态
    private LocationService locationService;

    private OrderDetailEntity mData;
    private OrderDetailEntityV1 mDataV1;
    private boolean isPermission;
    private double lat = 0, lon = 0;

    private ArrayList<String> payMethods = new ArrayList<>(); //支付方式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPosition = getIntent().getExtras().getInt(ExampleConfig.TASK_DETAIL_TYPE, -1);

        if (NetworkUtils.isConnected()) {
            //注册订阅者(简化应用组件间的通信；解耦事件的发送者和接收者；避免复杂和容易出错的依赖和生命周期的问题)
            EventBus.getDefault().register(this);
            proDialogHelps.showProDialog();
            switch (mPosition) {
                case -1://接单
                    initData();
                    break;
                case 0://待取货
                    initData();
                    break;
                case 1://配送中
                    getPermissionLocation();
                    break;
                case 2://返还中
                    initData();
                    break;
                case 3://扫一扫
                    getPermissionLocation();
                    break;
                case 4://已完成
                    initDataV1();
                    break;
                case 5://已取消
                    initDataV1();
                    break;
                default:
                    break;
            }
        } else {
            ToastUtils.showShortToast("网络不可用");
        }
        initView();
    }

    private void initDefaultListener() {
        tvStorePhoneNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //点击店铺老板电话回调
                String storePhone = mData.getStorePhone();
                checkPhonePermission(storePhone);
            }
        });
    }

    //    设置监听
    private void initListener() {
        initDefaultListener();
        switch (state) {
            case 1:
                //扫描二维码
                tvCenter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestCodeQRCodePermissions();
                    }
                });
                break;
            case 2:
                //送到
                tvLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        proDialogHelps.showProDialog("获取城市核销模式...");
                        Call<BaseResponseEntity<ArrayList<String>>> callPayMethod = webService.callPayMethod("orderPay");
                        callPayMethod.enqueue(new SimpleCallBack<BaseResponseEntity<ArrayList<String>>>(context) {
                            @Override
                            public void onSuccess(Call<BaseResponseEntity<ArrayList<String>>> call, Response<BaseResponseEntity<ArrayList<String>>> response) {
                                payMethods = response.body().getData();
                                showDiolog(TYPE_GOODSSEND);
                            }

                            @Override
                            public void onProDialogDismiss() {

                                proDialogHelps.removeProDialog();
                            }
                        });
                    }
                });

                tvNewLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putString("deliveryOrderId", mData.getId() + "");
                        startActivity(TaskPartSendActivity.class, bundle);
                    }
                });
                //拒收
                tvRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        showDiolog(TYPE_GOODSREFUSE);
                    }
                });
                //                地图
                tvMapDitu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        toMapActivity();
                    }
                });
                imgMapShoppre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(TaskDetailActivity.this, StoreImgActivity.class);
                        intent.putExtra("lat", mData.getLat());
                        intent.putExtra("lon", mData.getLon());
                        intent.putExtra(ExampleConfig.INVOICENUMBER_ID, mData.getInvoiceNumberId());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            startActivity(intent,
                                    ActivityOptions.makeSceneTransitionAnimation(TaskDetailActivity.this, view, "shareView").toBundle());
                        } else {
                            startActivity(intent);
                        }
                    }
                });
                break;
            case 3:
                //出示二维码
                tvCenter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //出示二维码
                        Intent intent = new Intent(context, QRCodeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(ExampleConfig.NUMBER_ID, mData.getNumberId() + "");
                        bundle.putString(ExampleConfig.QRCODE, mData.getRejectCode() + "");
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                break;
            default:
                break;
        }
    }

    //取货
    @AfterPermissionGranted(ExampleConfig.PERCAMERA)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}; //相机权限和存储权限
        if (!EasyPermissions.hasPermissions(context, perms)) { //判断权限是否授权
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", ExampleConfig.PERCAMERA, perms);
        } else {
            Bundle bundle = new Bundle();
            bundle.putInt(ExampleConfig.TYPE_CAMERA, 10);
            bundle.putInt("type", 0);
            Intent intent = new Intent(context, CaptureActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtras(bundle);
            startActivityForResult(intent, REQUEST_GETGOODS);
        }
    }

    @AfterPermissionGranted(ExampleConfig.PERCALLPHONE)
    private void checkPhonePermission(String storePhone) { //检查拨打电话权限
        String[] perms = {Manifest.permission.CALL_PHONE};
        if (EasyPermissions.hasPermissions(this, perms)) { //判断权限是否授权
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + storePhone));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            try {
                startActivity(intent); //拨打电话
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            EasyPermissions.requestPermissions(this, "请允许拨号权限", ExampleConfig.PERCALLPHONE, perms);
        }
    }

    private void initDataView() {
        if (mPosition == -1) {
            setTitle("订单详情");
            //tvHopeTimeTitle.setText("取货：");
            //tvHopeTime.setText(mData.getReceiveTime());
            tvHopeTimeTitle.setVisibility(View.GONE);
            tvHopeTime.setVisibility(View.GONE);
            initDefaultView();
        } else {
            initView();
            switch (state) {
                //取货中
                case 1:
                    setTitle("待取货");
                    tvCenter.setText("扫描二维码");
                    tvHopeTimeTitle.setText("接单：");
                    tvHopeTime.setText(mData.getReceiveTime()); //接单时间
                    initDefaultView();

                    break;
                //配送中
                case 2:
                    setTitle("配送中");
                    tvMapDingwei.setText(mData.getDistance()); //距离
                    tvHopeTimeTitle.setText("取货：");
                    tvHopeTime.setText(mData.getPickupTime()); //取货时间
                    Glide.with(this) //图片加载库
                            .load(mData.getStoreImg()) //加载网络图片
                            .placeholder(R.mipmap.icon_store_default_img) //设置默认占位图
                            .error(R.mipmap.icon_store_default_img)       //设置加载失败的图片
                            .into(imgMapShoppre);
                    initDefaultView();

                    break;
                //返还中
                case 3:
                    setTitle("返还中");
                    tvHopeTimeTitle.setText("取货：");
                    tvHopeTime.setText(mData.getPickupTime());
                    tvCenter.setText("出示二维码");
                    initDefaultView();
                    break;
                default:
                    initDefaultView();
                    break;
            }
        }
    }
    //设置页面中的数据

    private void initDataV1View() {

        setTitle("订单详情");
        if (mPosition == 4 || mPosition == 5) {
            if (mDataV1 != null) {
                tvAddressTake.setText(mDataV1.getWarehouseAddress());
                tvAddressSend.setText(mDataV1.getStoreAddress());
                tvHopeTime.setText(mDataV1.getReceiveTime());
                tvStoreName.setText(mDataV1.getStoreName());
                try {
                    tvOrderMes.setText(mData.getRemark());
                } catch (Exception e) {
                    llDetailMes.setVisibility(View.GONE);
                    e.printStackTrace();
                }
                tvOrderMoney.setText(mDataV1.getOrderAmount());
                tvTaskId.setText(mDataV1.getNumberId());
                tvIncome.setText(mDataV1.getCommission());
                tvOrderId.setText(mDataV1.getOrderId() + "");
                llDetailInvoiceId.setVisibility(View.GONE);
                try {
                    tvOrderInvoiceId.setText(mDataV1.getInvoiceNumberId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //if (!TextUtils.isEmpty(mDataV1.getInvoiceNumberId())) {
                //  llDetailInvoiceId.setVisibility(View.VISIBLE);
                //  tvOrderInvoiceId.setText(mDataV1.getInvoiceNumberId());
                //} else {
                //  llDetailInvoiceId.setVisibility(View.GONE);
                //}
            }
        }
    }

    //设置显示与隐藏页面内容
    private void initView() {

        if (mPosition == -1 || mPosition == 4 || mPosition == 5) {
            llDetailMap.setVisibility(View.GONE);
            llDetailButton.setVisibility(View.GONE);
        } else {
            switch (state) {
                //取货中
                case 1:
                    llDetailButton.setVisibility(View.VISIBLE);
                    llDetailMap.setVisibility(View.GONE);  //隐藏地图布局
                    llTaskBottom.setVisibility(View.GONE); //隐藏送达拒收布局
                    tvCenter.setVisibility(View.VISIBLE);  //显示扫码二维码
                    break;
                //配送中
                case 2:
                    llDetailButton.setVisibility(View.VISIBLE);
                    tvCenter.setVisibility(View.GONE);
                    llTaskBottom.setVisibility(View.VISIBLE);
                    llDetailMap.setVisibility(View.VISIBLE);
                    break;
                //返还中
                case 3:
                    llDetailButton.setVisibility(View.VISIBLE);
                    llDetailMap.setVisibility(View.GONE);
                    llTaskBottom.setVisibility(View.GONE);
                    tvCenter.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    }

    //不确定订单状态时，设置默认页面
    private void initDefaultView() {
        if (mData != null) {
            tvAddressTake.setText(mData.getWarehouseAddress()); //取货地址
            tvAddressSend.setText(mData.getStoreAddress());     //收货地址
            tvStoreName.setText(mData.getStoreName());          //店铺名称
            tvOrderMes.setText(mData.getRemark());              //订单备注
            tvOrderMoney.setText(mData.getOrderAmount());       //订单金额
            tvTaskId.setText(mData.getNumberId());              //运单编号
            tvIncome.setText(mData.getCommission());            //预期收入
            tvOrderId.setText(mData.getOrderId() + "");         //订单编号
            if (!TextUtils.isEmpty(mData.getInvoiceNumberId())) {
                llDetailInvoiceId.setVisibility(View.VISIBLE);
                tvOrderInvoiceId.setText(mData.getInvoiceNumberId()); //发货单号
            } else {
                llDetailInvoiceId.setVisibility(View.GONE);
            }
            tvStoreLbname.setText(mData.getStoreAcceptName());  //店铺老板名称
            tvStorePhoneNum.setText(mData.getStorePhone());     //店铺老板电话
        }
    }

    //调用老接口更新数据
    private void initDataV1() {
        Call<BaseResponseEntity<OrderDetailEntityV1>> callOrderDetailV1 = null;
        int id = getIntent().getExtras().getInt(ExampleConfig.ID);
        callOrderDetailV1 = webService.callOrderDetail(ExampleConfig.token, id);
        callOrderDetailV1.enqueue(new SimpleCallBack<BaseResponseEntity<OrderDetailEntityV1>>(this) {
            @Override
            public void onSuccess(Call<BaseResponseEntity<OrderDetailEntityV1>> call,
                                  Response<BaseResponseEntity<OrderDetailEntityV1>> response) {
                mDataV1 = response.body().getData();
                initDataV1View();
                //initListener();
            }

            @Override
            public void onProDialogDismiss() {
                proDialogHelps.removeProDialog();
            }
        });
    }

    //初始化数据
    private void initData() {
        DeliveryOrderParams params = new DeliveryOrderParams();
        Call<BaseResponseEntity<OrderDetailEntity>> callOrderDetail = null;
        //判断二维码数据格式
        String orderIdStr = getIntent().getExtras().getString(ExampleConfig.ID);
        //如果传来的值是int型
        if (orderIdStr == null) {
            int id = getIntent().getExtras().getInt(ExampleConfig.ID);
            orderIdStr = id + "";
            params.setDeliveryOrderId(orderIdStr);
            callOrderDetail = webService.getDeliveryOrder(ExampleConfig.token, params);
        }
        //传来的值是String型
        if (orderIdStr.contains("FH")) {
            params.setInvoiceNumberId(orderIdStr);
            //含有  发货单号 invoiceNumberId
            callOrderDetail = webService.getDeliveryOrder(ExampleConfig.token, params);
        } else if (orderIdStr.contains("Order")) {
            String[] split = orderIdStr.split("_");
            int orderId = Integer.parseInt(split[split.length - 1]);
            //  不含有  配送订单 deliveryOrderId
            params.setOrderId(orderId+"");
            callOrderDetail = webService.getDeliveryOrder(ExampleConfig.token, params);
        }

        callOrderDetail.enqueue(new SimpleCallBack<BaseResponseEntity<OrderDetailEntity>>(this) {
            @Override
            public void onSuccess(Call<BaseResponseEntity<OrderDetailEntity>> call,
                                  Response<BaseResponseEntity<OrderDetailEntity>> response) {
                mData = response.body().getData();
                System.out.println(response.body().getData().toString());
                state = mData.getState();
                initDataView();
                initListener();
            }

            @Override
            public void onError(Call<BaseResponseEntity<OrderDetailEntity>> call,
                                Response<BaseResponseEntity<OrderDetailEntity>> response) {
                super.onError(call, response);
                TaskDetailActivity.this.finish();
            }

            @Override
            public void onProDialogDismiss() {
                proDialogHelps.removeProDialog();
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_task_detail;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GETGOODS:
                if (resultCode == RESULT_OK) { //RESULT_OK = -1
                    Bundle bundle = data.getExtras();
                    String scanResult = bundle.getString("result");
                    Call<BaseResponseEntity> callGoodsGet =
                            webService.goodsGet(ExampleConfig.token, new GoodsGetParams(scanResult));
                    callGoodsGet.enqueue(new SimpleCallBack<BaseResponseEntity>(context) {
                        @Override
                        public void onSuccess(Call<BaseResponseEntity> call, Response<BaseResponseEntity> response) {
                            ToastUtils.showShortToast("取货成功");
                            mPosition = 1;
                            getPermissionLocation();
                            //((TaskAllListActivity) context).changeTab(mPosition + 1);
                        }
                        @Override
                        public void onProDialogDismiss() {
                            proDialogHelps.removeProDialog();
                        }
                    });
                }
                break;
        }
    }

    public void showDiolog(final int type) {
        final int hasPay = mData.getHasPay();
        TaskSendDialog taskSendDialog = new TaskSendDialog(context); //送达弹框
        TaskRefuseDialog taskRefuseDialog = new TaskRefuseDialog(context); //拒收弹框
        switch (type) {
            case TYPE_GOODSSEND: //Type == 送达
                taskSendDialog.show(hasPay, payMethods, mData.getNumberId(), new TaskSendDialog.OnSendOptionClickListener() {
                    @Override
                    public void onSendEnterClick(String numberId, String goodsCode, String payType) {
                        GoodsSendParams goodsSendParams;
                        if (TextUtils.isEmpty(payType)) {
                            goodsSendParams = new GoodsSendParams(numberId + "", goodsCode);
                        } else {
                            goodsSendParams = new GoodsSendParams(numberId + "", goodsCode, payType);
                        }
                        mPayType = payType;
                        Call<BaseResponseEntity<PayResultEntity>> callGoodsSend =
                                webService.goodsSend(ExampleConfig.token, goodsSendParams);
                        callGoodsSend.enqueue(callbackGoodsResult);
                    }
                });
                break;
            case TYPE_GOODSREFUSE: //Type == 拒收
                taskRefuseDialog.show(new TaskRefuseDialog.OnRefuseOptionClickListener() {
                    @Override
                    public void onRefuseEnterClick(String reason, String goodsCode) {
                        Call<BaseResponseEntity<PayResultEntity>> callGoodsRefuse =
                                webService.goodsRefuse(ExampleConfig.token,
                                        new GoodsRefuseV2Params(mData.getNumberId() + "", reason,
                                                goodsCode));
                        callGoodsRefuse.enqueue(callbackGoodsResult);
                    }
                });
                break;
        }
    }

    Callback<BaseResponseEntity<PayResultEntity>> callbackGoodsResult = //送达、拒收请求回调
            new SimpleCallBack<BaseResponseEntity<PayResultEntity>>(context) {
                @Override
                public void onSuccess(Call<BaseResponseEntity<PayResultEntity>> call,
                                      Response<BaseResponseEntity<PayResultEntity>> response) {
                    if (response.body().getData() == null) {
                        ToastUtils.showShortToast("成功");
                        //offset = 0;
                        //initDatas();
                        //成功关闭页面
                        TaskDetailActivity.this.finish();
                        return;
                    } else {
                        if (TextUtils.isEmpty(response.body().getData().getCodeUrl()) || TextUtils.isEmpty(
                                response.body().getData().getPayId())) {
                            ToastUtils.showShortToast("成功");
                            TaskDetailActivity.this.finish();
                            return;
                        }
                        payResultEntity = response.body().getData();
                        urlCode = payResultEntity.getCodeUrl();

                        Bundle bundle = new Bundle();
                        switch (mPayType) {
                            case PAYTYPE_ALIPAY:
                                bundle.putInt(ExampleConfig.INTENT_TYPE, TaskIncomeActivity.TYPE_ALIPAY);
                                break;
                            case PAYTYPE_WXPAY:
                                bundle.putInt(ExampleConfig.INTENT_TYPE, TaskIncomeActivity.TYPE_WXPAY);
                                break;
                        }
                        bundle.putString(TaskIncomeActivity.PAY_ID, payResultEntity.getPayId());
                        bundle.putString(TaskIncomeActivity.URL_CODE, urlCode);
                        bundle.putString("orderId", mData.getOrderId() + "");
                        bundle.putString("orderAmount", mData.getOrderAmount() + "");

                        Intent intent = new Intent(context, TaskIncomeActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    //offset = 0;
                    //initDatas();
                }

                @Override
                public void onProDialogDismiss() {
                    proDialogHelps.removeProDialog();
                }
            };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //获取定位权限
    @AfterPermissionGranted(ExampleConfig.RC_LOCATION)
    private void getPermissionLocation() {
        String[] perms = {
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
        };
        if (EasyPermissions.hasPermissions(this, perms)) {
            isPermission = true;
            //开启定位 获取经纬度
            locationService = MyApplication.getMyApplication().locationService;
            locationService.start();
        } else {
            EasyPermissions.requestPermissions(this, "地图需要获取位置信息权限", ExampleConfig.RC_LOCATION, perms);
        }
    }

    private void toMapActivity() {
        Bundle bundle = new Bundle();
        bundle.putDouble("la", lat);
        bundle.putDouble("lo", lon);
        bundle.putDouble("currentLa", mData.getLat());
        bundle.putDouble("currentLo", mData.getLon());
        bundle.putInt(ExampleConfig.DELIVERY_ID, mData.getId());
        startActivity(TaskMapActivity.class, bundle);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //申请权限成功
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        isPermission = true;
        locationService = MyApplication.getMyApplication().locationService;
        locationService.start();
    }

    //申请失败
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        switch (requestCode) {
            case ExampleConfig.RC_INITLOCATION:
                isPermission = false;
                ToastUtils.showShortToast("定位失败，没有获取位置信息权限！");
                break;
            case ExampleConfig.RC_LOCATION:
                ToastUtils.showShortToast("打开地图失败，没有获取位置信息权限！");
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DdmalBDLocation event) {
        System.out.println("TaskDetailActivity.onEvent");
        if (isPermission) {
            lat = event.getmLatitude();
            lon = event.getmLongitude();
            initData();
        }
    }
}
