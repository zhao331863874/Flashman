package com.ddinfo.flashman.activity.task;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.blankj.utilcode.utils.NetworkUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.adapter.TaskMapAdapter;
import com.ddinfo.flashman.application.MyApplication;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.helps.TaskRefuseDialog;
import com.ddinfo.flashman.helps.TaskSendDialog;
import com.ddinfo.flashman.location.DdmalBDLocation;
import com.ddinfo.flashman.location.LocationService;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.PayResultEntity;
import com.ddinfo.flashman.models.SeckillOrderList;
import com.ddinfo.flashman.models.params.GoodsRefuseV2Params;
import com.ddinfo.flashman.models.params.GoodsSendParams;
import com.ddinfo.flashman.network.SimpleCallBack;
import com.ddinfo.flashman.utils.MapUtils;
import com.ddinfo.flashman.view.RecycleViewItemDecoration.GridItemDecoration;
import com.ddinfo.flashman.view.RecyclerViewViewPager.RecyclerViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 地图Activity
 * Created by 李占晓 on 2017/5/24.
 */

public class TaskMapActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    // 整数: 李占晓 送达 static
    private static final int TYPE_GOODSSEND = 0x11;
    // 整数: 李占晓 拒收 static
    private static final int TYPE_GOODSREFUSE = 0x22;

    // 整数: 李占晓 当前项
    private int curItemPosition;
    // 整数: 李占晓
    private int lastVisibleItem;
    public static final String PAYTYPE_CASH = "cash";
    public static final String PAYTYPE_ALIPAY = "alipay";
    public static final String PAYTYPE_WXPAY = "wepay";

    private PayResultEntity payResultEntity;
    private String urlCode;   //二维码地址
    private String payType = "";
    @Bind(R.id.bmapView) //百度地图加载布局
    MapView bmapView;
    @Bind(R.id.rcv_view) //店铺详细信息布局控件
    RecyclerViewPager rcvView;
    @Bind(R.id.rel_map)
    RelativeLayout relMap;
    @Bind(R.id.left_button) //返回按钮
    ImageButton leftButton;
    @Bind(R.id.header_name) //标题抬头
    TextView headerName;
    @Bind(R.id.right_button)
    ImageButton rightButton;
    @Bind(R.id.rightBtn)
    Button rightBtn;
    @Bind(R.id.container)
    RelativeLayout container;

    private LinearLayoutManager horizontalLayManager;
    private TaskMapAdapter adapter; //店铺详细信息布局适配器

    private BaiduMap mBaiduMap;

    private MapUtils mapUtils;

    private int orderId;

    // 浮点: 李占晓 经度和纬度
    private double la, lo;
    int flag = 0;
    private double currentLa, currentLo; //当前经度、维度
    private int offset;

    private List<SeckillOrderList> mListData = new ArrayList<>();
    private List<SeckillOrderList> mListDataNew = new ArrayList<>();
    private int currentId;

    private ArrayList<String> payMethods = new ArrayList<>(); //支付方式

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_task_map;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
        if (NetworkUtils.isConnected()) {
            initMap();
            EventBus.getDefault().register(this); //Android的事件发布-订阅总线
        } else {
            ToastUtils.showShortToast("网络不可用");
        }

        la = getIntent().getDoubleExtra("la", 0);
        lo = getIntent().getDoubleExtra("lo", 0);
        currentLa = getIntent().getDoubleExtra("currentLa", 0);
        currentLo = getIntent().getDoubleExtra("currentLo", 0);
        currentId = getIntent().getIntExtra(ExampleConfig.DELIVERY_ID, 0);

        initData();

        if (la > 0 && lo > 0) {
            mapUtils.moveLocation(la, lo);
        } else {
            LocationService locationService = MyApplication.getMyApplication().locationService;
            locationService.start();
        }
        initListener();
    }

    private void initListener() {
        adapter.setOnTaskOptionClickListener(new TaskMapAdapter.OnTaskOptionClickListener() {
            @Override
            public void onTaskOptionLeftClick(View v, int position) { //点击送达回调
                curItemPosition = position;
                proDialogHelps.showProDialog("获取城市核销模式...");
                Call<BaseResponseEntity<ArrayList<String>>> callPayMethod = webService.callPayMethod("orderPay");
                callPayMethod.enqueue(callbackPayMethod);
            }

            @Override
            public void onTaskOptionRightClick(View v, int position) { //点击拒收回调
                showDiolog(TYPE_GOODSREFUSE, position);
            }

            @Override
            public void onTaskOptionNewLeftClick(View v, int position) { //点击部分送达回调
                Bundle bundle = new Bundle();
                bundle.putString("deliveryOrderId", mListData.get(position).getId() + "");
                startActivity(TaskPartSendActivity.class, bundle);
            }
        });
        //BUG: 禅道bugId=5661，解决日期:2017/6/6，解决人:李占晓，解释:我的任务,地图里，点击订单，不用再跳出订单详情页。
        //RecyclerView'Adapter 项点击事件  显示具体配送信息
        //adapter.setOnItemClick(new OnItemClickListenerRv() {
        //  @Override
        //  public void onItemClick(View view, int position) {
        //    Bundle bundle = new Bundle();
        //    bundle.putInt(ExampleConfig.TASK_DETAIL_TYPE, 1);
        //    bundle.putInt(ExampleConfig.ORDER_ID, mListData.get(position).getOrderId());
        //    startActivity(TaskDetailActivity.class, bundle);
        //  }
        //});
        adapter.setOnPhoneClick(new TaskMapAdapter.OnPhoneBtnClickListener() {
            @Override
            public void onPhoneClick(int position) { //点击店铺老板电话回调
                String storePhone = mListData.get(position).getStorePhone();
                checkPhonePermission(storePhone);
            }
        });
        rcvView.addOnPageChangedListener(new RecyclerViewPager.OnPageChangedListener() {
            @Override
            public void OnPageChanged(int oldPosition, int newPosition) {
                SeckillOrderList seckillOrderList = mListData.get(newPosition);
                System.out.println("TaskMapActivity.OnPageChanged seckListSize: "
                        + mListData.size()
                        + "  newPosition "
                        + newPosition);
                mapUtils.moveWithList(seckillOrderList.getLat(), seckillOrderList.getLon(), newPosition);
            }
        });
        adapter.setOnStoreImgClick(new TaskMapAdapter.OnStoreImgClickListener() {
            @Override
            public void onStoreImgClick(View view, int position) { //点击店铺图片回调
                Intent intent = new Intent(TaskMapActivity.this, StoreImgActivity.class);
                intent.putExtra("lat", mListData.get(position).getLat());
                intent.putExtra("lon", mListData.get(position).getLon());
                intent.putExtra(ExampleConfig.INVOICENUMBER_ID, mListData.get(position).getInvoiceNumberId());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //当前设备的API Level 大于等于Android 6.0版本
                    startActivity(intent,
                            ActivityOptions.makeSceneTransitionAnimation(TaskMapActivity.this, view, "shareView")
                                    .toBundle()); //设置过渡动画
                } else {
                    startActivity(intent);
                }
            }
        });
    }

    public void showDiolog(final int type, final int position) {
        final int hasPay = mListData.get(position).getHasPay();
        TaskSendDialog taskSendDialog = new TaskSendDialog(context); //送达弹框
        TaskRefuseDialog taskRefuseDialog = new TaskRefuseDialog(context); //拒收弹框
        switch (type) {
            case TYPE_GOODSSEND: //Type == 送达
                taskSendDialog.show(hasPay, payMethods, mListData.get(position).getNumberId(), new TaskSendDialog.OnSendOptionClickListener() {
                    @Override
                    public void onSendEnterClick(String numberId, String goodsCode, String payType) {
                        GoodsSendParams goodsSendParams;
                        if (TextUtils.isEmpty(payType)) {
                            goodsSendParams = new GoodsSendParams(numberId + "", goodsCode);
                        } else {
                            goodsSendParams = new GoodsSendParams(numberId + "", goodsCode, payType);
                        }
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
                                        new GoodsRefuseV2Params(mListData.get(position).getNumberId() + "", reason,
                                                goodsCode));
                        callGoodsRefuse.enqueue(callbackGoodsResult);
                    }
                });
                break;
        }
    }

    Callback<BaseResponseEntity<ArrayList<String>>> callbackPayMethod = new SimpleCallBack<BaseResponseEntity<ArrayList<String>>>(context) {
        @Override
        public void onSuccess(Call<BaseResponseEntity<ArrayList<String>>> call, Response<BaseResponseEntity<ArrayList<String>>> response) {
            payMethods = response.body().getData();
            showDiolog(TYPE_GOODSSEND, curItemPosition);
        }

        @Override
        public void onProDialogDismiss() {
            proDialogHelps.removeProDialog();
        }
    };
    //送达、拒收请求回调
    Callback<BaseResponseEntity<PayResultEntity>> callbackGoodsResult =
            new SimpleCallBack<BaseResponseEntity<PayResultEntity>>(context) {
                @Override
                public void onSuccess(Call<BaseResponseEntity<PayResultEntity>> call,
                                      Response<BaseResponseEntity<PayResultEntity>> response) {
                    if (response.body().getData() == null) {
                        ToastUtils.showShortToast("成功");
                        offset = 0;
                        currentId = 0;
                        initData();
                        return;
                    } else {
                        if (TextUtils.isEmpty(response.body().getData().getCodeUrl()) || TextUtils.isEmpty(
                                response.body().getData().getPayId())) {
                            ToastUtils.showShortToast("成功");
                            currentId = 0;
                            offset = 0;
                            initData();
                            return;
                        }
                        payResultEntity = response.body().getData();
                        urlCode = payResultEntity.getCodeUrl();

                        Intent intent = new Intent(context, TaskIncomeActivity.class);
                        Bundle bundle = new Bundle();
                        switch (payType) {
                            case PAYTYPE_ALIPAY:
                                bundle.putInt(ExampleConfig.INTENT_TYPE, TaskIncomeActivity.TYPE_ALIPAY);
                                break;
                            case PAYTYPE_WXPAY:
                                bundle.putInt(ExampleConfig.INTENT_TYPE, TaskIncomeActivity.TYPE_WXPAY);
                                break;
                        }
                        bundle.putString(TaskIncomeActivity.PAY_ID, payResultEntity.getPayId());
                        bundle.putString(TaskIncomeActivity.URL_CODE, urlCode);
                        bundle.putString("orderId", mListData.get(curItemPosition).getOrderId() + "");
                        bundle.putString("orderAmount", mListData.get(curItemPosition).getOrderAmount() + "");
                        intent.putExtras(bundle);
                        startActivity(intent); //跳转到收款布局界面
                    }
                    offset = 0;
                    initData();
                }

                @Override
                public void onProDialogDismiss() {
                    proDialogHelps.removeProDialog();
                }
            };

    @AfterPermissionGranted(ExampleConfig.PERCALLPHONE)
    private void checkPhonePermission(String storePhone) {
        String[] perms = {Manifest.permission.CALL_PHONE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + storePhone));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            try {

                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            EasyPermissions.requestPermissions(this, "请允许拨号权限", ExampleConfig.PERCALLPHONE, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void initData() {
        proDialogHelps.showProDialog();
        Call<BaseResponseEntity<ArrayList<SeckillOrderList>>> callOderList =
                webService.getOrderList(ExampleConfig.token, flag, la, lo, 2);
        callOderList.enqueue(callBackOrderList);
    }

    Callback<BaseResponseEntity<ArrayList<SeckillOrderList>>> callBackOrderList =
            new SimpleCallBack<BaseResponseEntity<ArrayList<SeckillOrderList>>>(context) {

                @Override
                public void onSuccess(Call<BaseResponseEntity<ArrayList<SeckillOrderList>>> call,
                                      Response<BaseResponseEntity<ArrayList<SeckillOrderList>>> response) {
                    mapUtils.clearAllMarker();
                    if (offset == 0) {
                        mListData.clear();
                    }
                    //                    isLoadMore = false;
                    mListDataNew = response.body().getData();
                    mListData.addAll(mListDataNew);
                    offset = mListData.size();
                    if (mListData.size() != 0) {
                        mapUtils.addMarker(mListData);
                    }
                    adapter.setmListData(mListData);
                    for (int i = 0; i < mListData.size(); i++) {
                        if (mListData.get(i).getId() == currentId) {
                            mapUtils.moveWithList(currentLa, currentLo, i);
                            rcvView.scrollToPosition(i);
                        }
                    }
                    //                    if (mListDataNew.size() < 15) {
                    //                        allocationAdapter.setLoadAll(true);
                    //                    }
                    //                    allocationAdapter.setmListData(mListData);
                }

                @Override
                public void onProDialogDismiss() {
                    proDialogHelps.removeProDialog();
                }
            };

    private void initView() {

        setTitle("我的任务");
        horizontalLayManager = new LinearLayoutManager(this);
        horizontalLayManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcvView.setLayoutManager(horizontalLayManager);
        //        LinearSnapHelper snapHelper = new LinearSnapHelper();
        //        snapHelper.attachToRecyclerView(rcvView);
        rcvView.addItemDecoration(new GridItemDecoration( //实现添加自定义分割线
                ContextCompat.getDrawable(this, R.drawable.screening_item_decoration)));
        adapter = new TaskMapAdapter();
        rcvView.setAdapter(adapter);
    }

    //初始化地图
    private void initMap() {

        mBaiduMap = bmapView.getMap();
        bmapView.showZoomControls(false);
        mapUtils = new MapUtils(bmapView, this);
        mapUtils.setListener(new MapUtils.ClickInterface() {
            @Override
            public void clickWhich(int position) {
                if (adapter != null && position < adapter.getItemCount()) {
                    if (rcvView != null) {
                        rcvView.scrollToPosition(position); //滚动到指定position
                    }
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DdmalBDLocation event) {
        la = event.getmLatitude();
        lo = event.getmLongitude();
        mapUtils.moveLocation(la, lo);
        MyLocationData locData = new MyLocationData.Builder().accuracy(event.getmRadius())
                // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(event.getmLatitude()).longitude(event.getmLongitude()).build();
        mBaiduMap.setMyLocationData(locData);
        LatLng ll = new LatLng(event.getmLatitude(), event.getmLongitude());
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(18.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    @Override
    protected void onResume() {
        bmapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        bmapView.onDestroy();
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        bmapView.onPause();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}
