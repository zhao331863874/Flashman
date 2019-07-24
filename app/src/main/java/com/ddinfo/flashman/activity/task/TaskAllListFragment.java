package com.ddinfo.flashman.activity.task;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.CaptureActivity;
import com.ddinfo.flashman.activity.base_frame.BaseFragment;
import com.ddinfo.flashman.adapter.AllTaskAdapter;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.helps.TaskRefuseDialog;
import com.ddinfo.flashman.helps.TaskSendDialog;
import com.ddinfo.flashman.impl.OnItemClickListenerRv;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.PayResultEntity;
import com.ddinfo.flashman.models.SeckillOrderList;
import com.ddinfo.flashman.models.params.GoodsGetParams;
import com.ddinfo.flashman.models.params.GoodsRefuseV2Params;
import com.ddinfo.flashman.models.params.GoodsSendParams;
import com.ddinfo.flashman.network.SimpleCallBack;
import com.ddinfo.flashman.view.RefreshLayout.MaterialRefreshLayout;
import com.ddinfo.flashman.view.RefreshLayout.MaterialRefreshListener;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class TaskAllListFragment extends BaseFragment
        implements EasyPermissions.PermissionCallbacks {

    @Bind(R.id.rcv_task_all_task)   //滚动控件
    RecyclerView rcvTaskAllTask;    //当前任务列表
    @Bind(R.id.swipe_search_list)
    MaterialRefreshLayout swipeSearchList;
    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;
    @Bind(R.id.rel_setting)
    RelativeLayout relSetting;
    @Bind(R.id.tv_task_done)       //已完成订单
    TextView tvTaskDone;
    @Bind(R.id.tv_task_cancel)     //已取消订单
    TextView tvTaskCancel;
    @Bind(R.id.tv_task_return_back)//退货返还中
    TextView tvTaskReturnBack;
    @Bind(R.id.tv_task_return_goods_finish) //退货完成
    TextView tvTaskReturnGoodsFinish;
    @Bind(R.id.tv_task_return_goods_cancel) //退货取消
    TextView tvTaskReturnGoodsCancel;
    @Bind(R.id.ll_task_more)       //订单状态列表
    LinearLayout llTaskMore;

    private static final String KEY_TABSTRING = "KEY_TABSTRING"; //当前选卡名称key
    private static final String KEY_POSITION = "KEY_POSITION";   //当前选卡位置key
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;

    private static TaskAllListFragment fragment;


    private String mTabString; //当前选卡名称
    private int mPosition;     //当前选卡位置
    private LinearLayoutManager layoutManager;
    private boolean isLoadMore = false;
    private AllTaskAdapter mAdapter;
    private int offset;
    private boolean isPrepared = false;

    private List<SeckillOrderList> mListData = new ArrayList<>();

    private static final int TYPE_GOODSSEND = 0x11;
    private static final int TYPE_GOODSREFUSE = 0x22;
    private static final int REQUEST_SCAN_CODE = 0x33;

    public static final String PAYTYPE_CASH = "cash";
    public static final String PAYTYPE_ALIPAY = "alipay";
    public static final String PAYTYPE_WXPAY = "wepay";

    private String urlCode;
    private String payType = "";
    private PayResultEntity payResultEntity;
    private int curItemPosition; //选卡位置

    private ArrayList<String> payMethods = new ArrayList<>(); //支付方式

    public TaskAllListFragment() {
    }

    public static TaskAllListFragment instance(String tabString, int position) {
        fragment = new TaskAllListFragment();
        fragment.mPosition = position;
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TABSTRING, tabString);
        bundle.putInt(KEY_POSITION, position);
        fragment.setArguments(bundle);
        Log.i("tag_fragment", position + "");
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        isPrepared = true;
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void lazyResumeLoad() {
        LogUtils.d("lazyResume");
        offset = 0;
        initDatas();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_task_all_list;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        if (getArguments() != null) {
            mTabString = getArguments().getString(KEY_TABSTRING);
            mPosition = getArguments().getInt(KEY_POSITION);
        }

        if (mTabString.equals("更多")) {
            rcvTaskAllTask.setVisibility(View.GONE);
            llTaskMore.setVisibility(View.VISIBLE); //显示订单状态列表
            tvTaskDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { //已完成订单
                    Bundle bundle = new Bundle();
                    bundle.putInt(ExampleConfig.ACTIVITY_TYPE, 4);
                    bundle.putInt(ExampleConfig.ACTIVITY_FLAG, 0);
                    startActivity(TaskMoreListActivity.class, bundle);
                }
            });
            //已取消订单
            tvTaskCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(ExampleConfig.ACTIVITY_TYPE, 5);
                    bundle.putInt(ExampleConfig.ACTIVITY_FLAG, 0);
                    startActivity(TaskMoreListActivity.class, bundle);
                }
            });
            //退货返还中
            tvTaskReturnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(ExampleConfig.ACTIVITY_TYPE, 2);
                    bundle.putInt(ExampleConfig.ACTIVITY_FLAG, 1);
                    startActivity(TaskMoreListActivity.class, bundle);
                }
            });
            //退货完成
            tvTaskReturnGoodsFinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(ExampleConfig.ACTIVITY_TYPE, 3);
                    bundle.putInt(ExampleConfig.ACTIVITY_FLAG, 1);
                    startActivity(TaskMoreListActivity.class, bundle);
                }
            });
            //退货取消
            tvTaskReturnGoodsCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(ExampleConfig.ACTIVITY_TYPE, 4);
                    bundle.putInt(ExampleConfig.ACTIVITY_FLAG, 1);
                    startActivity(TaskMoreListActivity.class, bundle);
                }
            });
        } else {
            llTaskMore.setVisibility(View.GONE);
            rcvTaskAllTask.setVisibility(View.VISIBLE);
            mAdapter = new AllTaskAdapter(context, mPosition);
            layoutManager = new LinearLayoutManager(context);
            rcvTaskAllTask.setLayoutManager(layoutManager);
            //            rcvTaskAllTask.addItemDecoration(new ListItemDecoration(context, ListItemDecoration.VERTICAL_LIST));
            rcvTaskAllTask.setAdapter(mAdapter);
            initListener();
        }
    }

    public void showDiolog(final int type, final int position) {
        final int hasPay = mListData.get(position).getHasPay();
        TaskSendDialog taskSendDialog = new TaskSendDialog(context); //送达Dialog
        TaskRefuseDialog taskRefuseDialog = new TaskRefuseDialog(context); //拒收Dialog
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

    @Override
    protected void initDatas() {
        if (!isPrepared) {
            LogUtils.d("return");
            return;
        }
        if ("更多".equals(mTabString)) {

        } else if (mPosition == 1) {//退货取货中
            proDialogHelps.showProDialog();
            isLoadMore = true;
            //0查询订单 1查询退货单
            Call<BaseResponseEntity<ArrayList<SeckillOrderList>>> callOrderList =
                    webService.getOrderList(ExampleConfig.token, 1, 0, 0, 1);
            callOrderList.enqueue(callBackOrderList);
        }else if(mPosition == 2){
            proDialogHelps.showProDialog();
            isLoadMore = true;
            //0查询订单 1查询退货单
            Call<BaseResponseEntity<ArrayList<SeckillOrderList>>> callOrderList =
                    webService.getOrderList(ExampleConfig.token, 0, 0, 0, 3);
            callOrderList.enqueue(callBackOrderList);
        } else {
            proDialogHelps.showProDialog();
            isLoadMore = true;
            //0查询订单 1查询退货单
            Call<BaseResponseEntity<ArrayList<SeckillOrderList>>> callOrderList =
                    webService.getOrderList(ExampleConfig.token, 0, 0, 0, mPosition);
            callOrderList.enqueue(callBackOrderList);
        }
    }
    //查询回调
    Callback<BaseResponseEntity<ArrayList<SeckillOrderList>>> callBackOrderList =
            new SimpleCallBack<BaseResponseEntity<ArrayList<SeckillOrderList>>>(context) {
                @Override
                public void onSuccess(Call<BaseResponseEntity<ArrayList<SeckillOrderList>>> call,
                                      Response<BaseResponseEntity<ArrayList<SeckillOrderList>>> response) {
                    if (swipeSearchList != null && swipeSearchList.isRefreshing()) {
                        swipeSearchList.finishRefresh();
                    }
                    if (offset == 0) {
                        mListData.clear();
                    }
                    isLoadMore = false;
                    mListData = response.body().getData();
                    offset = mListData.size();
                    mAdapter.setLoadAll(true);
                    if (mListData.size() == 0) {
                        mAdapter.setEmpty(true);
                    }
                    mAdapter.setmListData(mListData);
                }

                @Override
                public void onProDialogDismiss() {
                    proDialogHelps.removeProDialog(); //移除加载框
                }
            };
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

    //送达、拒收、请求回调
    Callback<BaseResponseEntity<PayResultEntity>> callbackGoodsResult =
            new SimpleCallBack<BaseResponseEntity<PayResultEntity>>(context) {
                @Override
                public void onSuccess(Call<BaseResponseEntity<PayResultEntity>> call,
                                      Response<BaseResponseEntity<PayResultEntity>> response) {
                    if (response.body().getData() == null) {
                        ToastUtils.showShortToast("成功");
                        offset = 0;
                        initDatas();
                        return;
                    } else {
                        if (TextUtils.isEmpty(response.body().getData().getCodeUrl()) || TextUtils.isEmpty(
                                response.body().getData().getPayId())) {
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
                        bundle.putString(TaskIncomeActivity.PAY_ID, payResultEntity.getPayId()); //支付ID
                        bundle.putString(TaskIncomeActivity.URL_CODE, urlCode);                  //二维码地址
                        bundle.putString("orderId", mListData.get(curItemPosition).getOrderId() + ""); //订单ID
                        bundle.putString("orderAmount", mListData.get(curItemPosition).getOrderAmount() + ""); //订单金额
                        //优化逻辑，直接传入orderId 2017年06月16日14:43:18 by Fuh
//            bundle.putInt(ExampleConfig.DATA, 0);
//            bundle.putSerializable(TaskIncomeActivity.TASK_DATA, mListData.get(curItemPosition));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    offset = 0;
                    initDatas();
                }

                @Override
                public void onProDialogDismiss() {
                    proDialogHelps.removeProDialog(); //移除加载框
                }
            };

    @Override
    protected void initListener() {
        swipeSearchList.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                offset = 0;
                initDatas();
            }
        });

        mAdapter.setItemListener(new OnItemClickListenerRv() {
            @Override
            public void onItemClick(View view, int position) {
                goToDetail(position);
            }
        });

        mAdapter.setOnTaskOptionClickListener(new AllTaskAdapter.OnTaskOptionClickListener() {
            @Override
            public void onTaskOptionGoodsSendClick(View v, int position) {
                //送达
                curItemPosition = position;
                proDialogHelps.showProDialog("获取城市核销模式...");
                Call<BaseResponseEntity<ArrayList<String>>> callPayMethod = webService.callPayMethod("orderPay");
                callPayMethod.enqueue(callbackPayMethod);
            }

            @Override
            public void onTaskOptionShowCodeClick(View v, int position) {
                //出示二维码
                Intent intent = new Intent(context, QRCodeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(ExampleConfig.NUMBER_ID, mListData.get(position).getNumberId() + "");
                bundle.putString(ExampleConfig.QRCODE, mListData.get(position).getRejectCode() + "");
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onTaskOptionGoodsRefuseClick(View v, int position) {
                //拒收
                curItemPosition = position;
                showDiolog(TYPE_GOODSREFUSE, position);
            }

            @Override
            public void onTaskOptionTakeClick(View v, int position) {
                //取货
                goToDetail(position);
            }
        });
    }

    private void goToDetail(int position) {
        Bundle bundle = new Bundle();
        if (mPosition != 1) {
            bundle.putInt(ExampleConfig.TASK_DETAIL_TYPE, 0);
            bundle.putInt(ExampleConfig.ID, mListData.get(position).getId());
            startActivity(TaskDetailActivity.class, bundle);
        } else {
            bundle.putInt(ExampleConfig.ID, mListData.get(position).getBackOrderId());
            startActivity(ReturnBackTaskDetailActivity.class, bundle);
        }
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
                            ((TaskAllListActivity) context).changeTab(mPosition + 1);
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
            bundle.putInt("type", 0);
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
    public void onDestroyView() {
        super.onDestroyView();
        isPrepared = false;
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        proDialogHelps.removeProDialog();
        if (isVisible && isPrepared) {
            offset = 0;
            initDatas();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

}
