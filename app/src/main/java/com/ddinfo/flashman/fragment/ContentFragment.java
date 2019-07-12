package com.ddinfo.flashman.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.share.sdk.openapi.APAPIFactory;
import com.alipay.share.sdk.openapi.APMediaMessage;
import com.alipay.share.sdk.openapi.APTextObject;
import com.alipay.share.sdk.openapi.IAPApi;
import com.alipay.share.sdk.openapi.SendMessageToZFB;
import com.blankj.utilcode.utils.NetworkUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.CaptureActivity;
import com.ddinfo.flashman.activity.LoginActivity;
import com.ddinfo.flashman.activity.RegisterActivity;
import com.ddinfo.flashman.activity.base_frame.BaseFragment;
import com.ddinfo.flashman.activity.tab_frame.MainActivity;
import com.ddinfo.flashman.activity.task.TaskAllListActivity;
import com.ddinfo.flashman.activity.task.TaskDetailActivity;
import com.ddinfo.flashman.activity.task.WaitForDeliveryActivity;
import com.ddinfo.flashman.adapter.HomeAdapter;
import com.ddinfo.flashman.application.MyApplication;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.impl.OnItemClickListenerRv;
import com.ddinfo.flashman.impl.OnTaskGetListener;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.RouteEntity;
import com.ddinfo.flashman.models.SeckillOrderList;
import com.ddinfo.flashman.models.params.GoodsGetParams;
import com.ddinfo.flashman.models.params.SeckillParams;
import com.ddinfo.flashman.network.SimpleCallBack;
import com.ddinfo.flashman.utils.Utils;
import com.ddinfo.flashman.view.RefreshLayout.MaterialRefreshLayout;
import com.ddinfo.flashman.view.RefreshLayout.MaterialRefreshListener;
import com.kennyc.view.MultiStateView;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * 线路总览布局
 */
public class ContentFragment extends BaseFragment implements EasyPermissions.PermissionCallbacks {

    @Bind(R.id.left_button)
    ImageButton leftButton; //标题栏左点击按钮
    @Bind(R.id.header_name)
    TextView headerName;    //标题抬头
    @Bind(R.id.right_button)
    ImageButton rightButton;//标题栏右点击按钮
    @Bind(R.id.rightBtn)
    Button rightBtn;
    @Bind(R.id.rcv_home)
    RecyclerView rcvHome;   //路线总览数据显示布局
    @Bind(R.id.swipe_search_list)
    MaterialRefreshLayout swipeSearchList;
    @Bind(R.id.tv_menu_left)
    TextView tvMenuLeft;    //左点击按钮
    @Bind(R.id.tv_menu_right)
    TextView tvMenuRight;   //右点击按钮
    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;
    @Bind(R.id.rel_setting)
    RelativeLayout relSetting;

    private LinearLayoutManager layoutManager;
    private HomeAdapter mAdapter; //主页适配器

    private boolean isLogined; //是否登录
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    private static final int REQUEST_SCAN_CODE = 0x33;

    private List<RouteEntity> mListData = new ArrayList<>();

    public ContentFragment() {
    }

    public static ContentFragment newInstance() {
        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args); //实例化自定义Fragment
        return fragment;
    }

    @Override
    protected void lazyResumeLoad() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_content;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        setTitle("路线总览");
        leftButton.setImageResource(R.mipmap.icon_left_draw);

        if (isLogined) {
            tvMenuLeft.setText("我的任务");
            tvMenuRight.setText("刷新列表");
        } else {
            tvMenuLeft.setText("登录");
            tvMenuRight.setText("注册");
        }
        initRecycle();
        initListener();
    }

    private void initRecycle() {
        layoutManager = new LinearLayoutManager(context);
        mAdapter = new HomeAdapter(context);
        rcvHome.setLayoutManager(layoutManager);
        rcvHome.setAdapter(mAdapter);
    }

    @Override
    public void initDatas() { //初始化/刷新数据
        proDialogHelps.showProDialog("正在加载订单信息...");
        Call<BaseResponseEntity<ArrayList<RouteEntity>>> callSeckillOrderList = webService.getRouteList();
        callSeckillOrderList.enqueue(new SimpleCallBack<BaseResponseEntity<ArrayList<RouteEntity>>>(context) {
            @Override
            public void onSuccess(Call<BaseResponseEntity<ArrayList<RouteEntity>>> call, Response<BaseResponseEntity<ArrayList<RouteEntity>>> response) {
                if (swipeSearchList != null && swipeSearchList.isRefreshing()) {
                    swipeSearchList.finishRefresh(); //刷新动画
                }
                if (mListData.size() == 0) {
                    mAdapter.setIsEmpty(true);
                } else {
                    mAdapter.setIsLoadAll(true);
                }
                mAdapter.setTokenLose(false); //设置token有效
                mListData = response.body().getData();
                mAdapter.setmListData(mListData); //设置路线数据信息
            }

            @Override
            public void onProDialogDismiss() {
                proDialogHelps.removeProDialog();
            }
        });
    }

    @OnClick({R.id.left_button, R.id.tv_menu_left, R.id.tv_menu_right, R.id.rel_setting, R.id.txt_no_network_try_again, R.id.txt_empty_try_again})
    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.tv_menu_left: //左按钮：我的任务/登录
                if (isLogined) {
                    startActivity(TaskAllListActivity.class);
                } else {
                    startActivity(LoginActivity.class);
                }
                break;
            case R.id.tv_menu_right: //右按钮：扫码取货/注册
                //创建工具对象实例，此处的APPID为上文提到的，申请应用生效后，在应用详情页中可以查到的支付宝应用唯一标识
                if (isLogined) {
                    requestCodeQRCodePermissions();
                } else {
                    startActivity(RegisterActivity.class);
                }
                break;
            case R.id.txt_empty_try_again:
                initDatas();
                break;
            case R.id.rel_setting:
                Utils.openSetting(getActivity());
                if (relSetting != null) {
                    relSetting.setVisibility(View.GONE);
                }
                break;
            case R.id.txt_no_network_try_again:
                initDatas();
                break;
            case R.id.left_button:
                ((MainActivity) getActivity()).showLeftDraw();
                break;
        }
    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(context, perms)) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限",
                    REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        } else {
            Intent intent = new Intent(context, CaptureActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle = new Bundle();
            bundle.putInt(ExampleConfig.TYPE_CAMERA, 10);
            bundle.putInt("type", 1);
            intent.putExtras(bundle);
            startActivityForResult(intent, REQUEST_SCAN_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SCAN_CODE:
                if (resultCode == RESULT_OK) { //RESULT_OK = -1
                    Bundle bundle = data.getExtras();
                    String scanResult = bundle.getString("result");
                    Call<BaseResponseEntity> callGoodsGet =
                            webService.goodsGet(ExampleConfig.token, new GoodsGetParams(scanResult));
                    callGoodsGet.enqueue(new SimpleCallBack<BaseResponseEntity>(context) {
                        @Override
                        public void onSuccess(Call<BaseResponseEntity> call,
                                              Response<BaseResponseEntity> response) {
                            ToastUtils.showShortToast("取货成功");
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

    @Override
    protected void initListener() {
        mAdapter.setItemClickListener(new OnItemClickListenerRv() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("routeId", mListData.get(position).getRouteId());
                bundle.putString("routeName", mListData.get(position).getRouteName());
                startActivity(WaitForDeliveryActivity.class, bundle);
            }
        });

        swipeSearchList.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                initDatas();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        proDialogHelps.removeProDialog();
        if (NetworkUtils.isConnected()) { //判断网络是否连接
            initDatas();
        } else {
            ToastUtils.showShortToast("网络不可用");
            setEmptyOrError(rootView, MultiStateView.VIEW_STATE_ERROR);
        }

        if (MyApplication.getSPUtilsInstance().getString(ExampleConfig.TOKEN) == null || MyApplication.getSPUtilsInstance().getString(ExampleConfig.TOKEN).equals("")) {
            isLogined = false;
            mAdapter.setTokenLose(true);
            tvMenuLeft.setText("登录");
            tvMenuRight.setText("注册");
        } else {
            isLogined = true;
            mAdapter.setTokenLose(false);
            tvMenuLeft.setText("我的任务");
            tvMenuRight.setText("扫码取货");
        }
    }


}
