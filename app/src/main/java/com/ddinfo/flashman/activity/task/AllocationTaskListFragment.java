package com.ddinfo.flashman.activity.task;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseFragment;
import com.ddinfo.flashman.adapter.AllocationTaskAdapter;
import com.ddinfo.flashman.application.MyApplication;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.helps.TaskRefuseDialog;
import com.ddinfo.flashman.helps.TaskSendDialog;
import com.ddinfo.flashman.impl.OnItemClickListenerRv;
import com.ddinfo.flashman.location.DdmalBDLocation;
import com.ddinfo.flashman.location.LocationService;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.PayResultEntity;
import com.ddinfo.flashman.models.SeckillOrderList;
import com.ddinfo.flashman.models.params.GoodsRefuseV2Params;
import com.ddinfo.flashman.models.params.GoodsSendParams;
import com.ddinfo.flashman.network.SimpleCallBack;
import com.ddinfo.flashman.view.RefreshLayout.MaterialRefreshLayout;
import com.ddinfo.flashman.view.RefreshLayout.MaterialRefreshListener;
import com.kennyc.view.MultiStateView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 配送中Fragment
 * Created by lizuanxiao on 2017/5/24.
 */
public class AllocationTaskListFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks {
    // 整数: 李占晓 送达 static
    private static final int TYPE_GOODSSEND = 0x11;
    // 整数: 李占晓 拒收 static
    private static final int TYPE_GOODSREFUSE = 0x22;
    private static final String DIALOGLOCATIONCONTENT = "定位中，请稍后...";
    @Bind(R.id.txt_location_details)
    TextView txtLocationDetails;
    @Bind(R.id.img_refresh)
    ImageView imgRefresh;

    private double lat = 0;
    private double lon = 0;
    int flag = 0;//0查询订单 1查询退货单

    @Bind(R.id.swipe_search_list)
    MaterialRefreshLayout swipeSearchList;
    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;
    @Bind(R.id.rcv_task_allcation_task)
    RecyclerView rcvTaskAllcationTask;
    // 整数: 李占晓 分页加载偏移量
    private int offset = 0;
    // 布尔: 李占晓  页面是否显示完
    private boolean isPrepared = false;

    // 布尔: 李占晓 正在加载数据
    private boolean isLoadMore = false;
    // 整数: 李占晓 当前分页数
    private int mPosition;

    private boolean isPermission = false;

    public static final String PAYTYPE_CASH = "cash";
    public static final String PAYTYPE_ALIPAY = "alipay";
    public static final String PAYTYPE_WXPAY = "wepay";

    private PayResultEntity payResultEntity;
    private String urlCode;
    private String payType = "";

    private AllocationTaskAdapter allocationAdapter;
    // 整数: 李占晓 当前项
    private int curItemPosition;
    // 整数: 李占晓
    private int lastVisibleItem;
    private LinearLayoutManager layoutmanager;

    private List<SeckillOrderList> mListData = new ArrayList<>();
    private List<SeckillOrderList> mListDataNew = new ArrayList<>();

    public ProgressDialog progressDialog = null;
    private long lastClickTime;
    private LocationService locationService;

    private ArrayList<String> payMethods = new ArrayList<>();

    /**
     * 静态内部类单例模式
     *
     * @return the allocation task list fragment
     */
    public AllocationTaskListFragment() {

    }

    public static AllocationTaskListFragment getInstance() {
        return AllocationFragmentHolder.sInstance;
    }

    private static class AllocationFragmentHolder {
        private static final AllocationTaskListFragment sInstance = new AllocationTaskListFragment();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        isPrepared = true;
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_task_allocation_list;
    }

    @Override
    protected void lazyResumeLoad() {
        initLocationPermission();
        initLocation();
        initDatas();
    }

    private void initLocation() {
        locationService = MyApplication.getMyApplication().locationService;
        locationService.start();
    }

    @Override
    protected void initDatas() {
        offset = 0;
        if (!isPrepared) {
            LogUtils.d("return");
            return;
        }
        //显示加载框
        proDialogHelps.showProDialog();
        isLoadMore = true;
        Call<BaseResponseEntity<ArrayList<SeckillOrderList>>> callOderList = webService.getOrderList(ExampleConfig.token, flag, lat, lon, 2);
        callOderList.enqueue(callBackOrderList);
    }

    Callback<BaseResponseEntity<ArrayList<SeckillOrderList>>> callBackOrderList =
            new SimpleCallBack<BaseResponseEntity<ArrayList<SeckillOrderList>>>(context) {

                @Override
                public void onSuccess(Call<BaseResponseEntity<ArrayList<SeckillOrderList>>> call,
                                      Response<BaseResponseEntity<ArrayList<SeckillOrderList>>> response) {
                    proDialogHelps.removeProDialog();
                    if (swipeSearchList != null && swipeSearchList.isRefreshing()) {
                        swipeSearchList.finishRefresh();
                    }
                    System.out.println("offset " + offset);
                    if (offset == 0) {
                        mListData.clear();
                    }
                    isLoadMore = false;
                    mListDataNew = response.body().getData();
                    mListData.addAll(mListDataNew);
                    offset = mListData.size();
                    if (mListData.size() == 0) {
                        //                        multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                        allocationAdapter.setEmpty(true);
                    }
                    if (mListDataNew.size() < 15) {
                        allocationAdapter.setLoadAll(true);
                    }
                    allocationAdapter.setmListData(mListData);
                }

                @Override
                public void onProDialogDismiss() {
                    proDialogHelps.removeProDialog();
                }
            };

    //在onViewCreate时被调用
    @Override
    protected void initView(View view, Bundle savedInstanceState) {

        EventBus.getDefault().register(this);
        allocationAdapter = new AllocationTaskAdapter();
        layoutmanager = new LinearLayoutManager(context);
        rcvTaskAllcationTask.setLayoutManager(layoutmanager);
        rcvTaskAllcationTask.setAdapter(allocationAdapter);
        initListener();
    }

    //各种控件监听
    @Override
    protected void initListener() {
        //下拉刷新
        swipeSearchList.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                offset = 0;
                initDatas();
            }
        });

        //RecyclerView'Adapter 项点击事件  显示具体配送信息
        allocationAdapter.setOnItemClick(new OnItemClickListenerRv() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt(ExampleConfig.TASK_DETAIL_TYPE, 1);
                bundle.putString(ExampleConfig.ID, mListData.get(position).getInvoiceNumberId());
                startActivity(TaskDetailActivity.class, bundle);
            }
        });

        //地图监听
        allocationAdapter.setOnMapBtnClickListener(new AllocationTaskAdapter.onMapBtnClickListener() {
            @Override
            public void onMapBtnClick(View view, int position) {
                getPermissionLocation(position);
            }
        });

        //送达与拒收监听
        allocationAdapter.setOnTaskOptionClickListener(new AllocationTaskAdapter.OnTaskOptionClickListener() {

            @Override
            public void onTaskOptionLeftClick(View v, int position) {
                //送达
                curItemPosition = position;
                proDialogHelps.showProDialog("获取城市核销模式...");
                Call<BaseResponseEntity<ArrayList<String>>> callPayMethod = webService.callPayMethod("orderPay");
                callPayMethod.enqueue(callbackPayMethod);
            }

            @Override
            public void onTaskOptionRightClick(View v, int position) {
                //拒收
                curItemPosition = position;
                showDiolog(TYPE_GOODSREFUSE, position);
            }

            @Override
            public void onTaskOptionNewLeftClick(View v, int position) {
                //部分送达
                Bundle bundle = new Bundle();
                bundle.putString("deliveryOrderId", mListData.get(position).getId() + "");
                startActivity(TaskPartSendActivity.class, bundle);
            }
        });

        rcvTaskAllcationTask.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (swipeSearchList.isRefreshing()) {
                    swipeSearchList.setRefreshing(false);
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem == allocationAdapter.getItemCount() - 1) {
                    if (mListDataNew.size() == 15 && !isLoadMore) {
                        isLoadMore = true;
                        //根据分类获取到商品列表
                        initDatas();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutmanager.findLastVisibleItemPosition();
            }
        });
        //商店图点击
        allocationAdapter.setOnStoreImgClick(new AllocationTaskAdapter.OnStoreImgClickListener() {
            @Override
            public void onclick(View view, int position) {
                Intent intent = new Intent(getContext(), StoreImgActivity.class);
                intent.putExtra("lat", mListData.get(position).getLat());
                intent.putExtra("lon", mListData.get(position).getLon());
                intent.putExtra(ExampleConfig.INVOICENUMBER_ID, mListData.get(position).getInvoiceNumberId());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(getActivity(), view, "shareView").toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });
    }

    @OnClick(R.id.img_refresh)
    public void onClick(View view) {
        if ((System.currentTimeMillis() - lastClickTime) > 2000) {
            offset = 0;
            txtLocationDetails.setText(String.format("当前位置：%s", "正在定位！"));
            locationService.requestLocation();
            lastClickTime = System.currentTimeMillis();
        }
    }

    @AfterPermissionGranted(ExampleConfig.RC_INITLOCATION)
    private void initLocationPermission() {
        String[] perms = {
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
        };
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            isPermission = true;
        } else {
            isPermission = false;
            EasyPermissions.requestPermissions(this, "定位需要获取位置信息权限", ExampleConfig.RC_INITLOCATION, perms);
        }
    }

    //获取定位权限
    @AfterPermissionGranted(ExampleConfig.RC_LOCATION)
    private void getPermissionLocation(int position) {
        String[] perms = {
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
        };
        //if (EasyPermissions.somePermissionPermanentlyDenied(getContext(),perms)) {
        //  new AppSettingsDialog.Builder(this).build().show();
        //}
        if (EasyPermissions.hasPermissions(getContext(), perms)) {
            Bundle bundle = new Bundle();
            bundle.putDouble("la", lat);
            bundle.putDouble("lo", lon);
            bundle.putDouble("currentLa", mListData.get(position).getLat());
            bundle.putDouble("currentLo", mListData.get(position).getLon());
            bundle.putInt(ExampleConfig.DELIVERY_ID, mListData.get(position).getId());
            startActivity(TaskMapActivity.class, bundle);
        } else {
            EasyPermissions.requestPermissions(this, "地图需要获取位置信息权限", ExampleConfig.RC_LOCATION, perms);
        }
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
        //        switch (requestCode) {
        //            case ExampleConfig.RC_INITLOCATION:
        //                LocationService locationService = ((MyApplication) getApplication()).locationService;
        //                locationService.start();
        //                EventBus.getDefault().register(this);
        //                break;
        //            case ExampleConfig.RC_LOCATION:
        //                Bundle bundle = new Bundle();
        //                bundle.putInt(ExampleConfig.ORDER_ID,mListData.get());
        //                startActivity(TaskMapActivity.class, bundle);
        //                break;
        //        }
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
        if (!isPrepared || !isVisible) {
            return;
        }
        if (isPermission) {
            lat = event.getmLatitude();
            lon = event.getmLongitude();
        }
        initDatas();
        txtLocationDetails.setText(String.format("当前位置：%s", TextUtils.isEmpty(event.getAddress()) ? "" : event.getAddress()));
        System.out.println("dingwei " + lat + "   " + lon);
    }

    public void showDiolog(final int type, final int position) {
        final int hasPay = mListData.get(position).getHasPay();
        TaskSendDialog taskSendDialog = new TaskSendDialog(context);
        TaskRefuseDialog taskRefuseDialog = new TaskRefuseDialog(context);
        switch (type) {
            case TYPE_GOODSSEND: //Type == 送达
                taskSendDialog.show(hasPay, payMethods, mListData.get(position).getNumberId(), new TaskSendDialog.OnSendOptionClickListener() {
                    @Override
                    public void onSendEnterClick(String numberId, String goodsCode, String payTypeString) {
                        payType = payTypeString;
                        GoodsSendParams goodsSendParams;
                        if (TextUtils.isEmpty(payType)) {
                            goodsSendParams = new GoodsSendParams(numberId + "", goodsCode);
                        } else {
                            goodsSendParams = new GoodsSendParams(numberId + "", goodsCode, payType);
                        }
                        Call<BaseResponseEntity<PayResultEntity>> callGoodsSend = webService.goodsSend(ExampleConfig.token, goodsSendParams);
                        callGoodsSend.enqueue(callbackGoodsResult);
                    }
                });
                break;
            case TYPE_GOODSREFUSE: //Type == 拒收
                taskRefuseDialog.show(new TaskRefuseDialog.OnRefuseOptionClickListener() {
                    @Override
                    public void onRefuseEnterClick(String reason, String goodsCode) {
                        Call<BaseResponseEntity<PayResultEntity>> callGoodsRefuse =
                                webService.goodsRefuse(ExampleConfig.token, new GoodsRefuseV2Params(mListData.get(position).getNumberId() + "", reason, goodsCode));
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

    Callback<BaseResponseEntity<PayResultEntity>> callbackGoodsResult = new SimpleCallBack<BaseResponseEntity<PayResultEntity>>(context) {
        @Override
        public void onSuccess(Call<BaseResponseEntity<PayResultEntity>> call, Response<BaseResponseEntity<PayResultEntity>> response) {
            if (response.body().getData() == null) {
                ToastUtils.showShortToast("成功");
                offset = 0;
                initDatas();
                return;
            } else {
                if (TextUtils.isEmpty(response.body().getData().getCodeUrl()) || TextUtils.isEmpty(response.body().getData().getPayId())) {
                    ToastUtils.showShortToast("成功");
                    offset = 0;
                    initDatas();
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
                bundle.putString("orderAmount", mListData.get(curItemPosition).getOrderAmount());
                intent.putExtras(bundle);
                startActivity(intent);
            }
            offset = 0;
            initDatas();
        }

        @Override
        public void onProDialogDismiss() {
            proDialogHelps.removeProDialog();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);

        System.out.println("顺序 4 onCreateView");
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //ButterKnife.unbind(this);
        isPrepared = false;
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        initLocationPermission();
        proDialogHelps.removeProDialog();
        if (isVisible && isPrepared) {
            offset = 0;
            initDatas();
        }
        //        EventBus.getDefault().unregister(this);
    }
}
