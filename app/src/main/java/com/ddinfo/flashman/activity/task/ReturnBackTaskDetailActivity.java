package com.ddinfo.flashman.activity.task;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.adapter.RBDetailTaskAdapter;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.ReturnBackGroupOrderEntity;
import com.ddinfo.flashman.models.ReturnBackGroupOrderListEntity;
import com.ddinfo.flashman.models.params.BackGoodsParams;
import com.ddinfo.flashman.view.RefreshLayout.MaterialRefreshLayout;
import com.ddinfo.flashman.view.RefreshLayout.MaterialRefreshListener;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 退货取货操作
 */
public class ReturnBackTaskDetailActivity extends BaseActivity {

    @Bind(R.id.rcv_task_all_task)   //滚动控件
    RecyclerView rcvTaskAllTask;
    @Bind(R.id.swipe_search_list)   //动画刷新布局
    MaterialRefreshLayout swipeSearchList;
    @Bind(R.id.tv_real_back_count)  //实退商品总数
    TextView tvRealBackCount;
    @Bind(R.id.tv_real_back_money)  //实退退货金额
    TextView tvRealBackMoney;
    @Bind(R.id.btn_take_goods)      //确认收货
    Button btnTakeGoods;
    @Bind(R.id.ll_return_back_bottom)
    LinearLayout llReturnBackBottom;
    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;

    @Bind(R.id.tv_return_back_order_id) //退货单号
    TextView tvReturnBackOrderId;
    private int offset;
    private boolean isLoadMore = false;

    private LinearLayoutManager layoutManager;
    private RBDetailTaskAdapter mAdapter;
    int backOrderid = 0; //退货单号
    String type = "";    //状态
    List<ReturnBackGroupOrderEntity> backGroupOrderEntities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initDatas();
        initListener();
    }

    private void initViews() {
        llReturnBackBottom.setVisibility(View.VISIBLE);
        mAdapter = new RBDetailTaskAdapter(context);
        layoutManager = new LinearLayoutManager(context);
        rcvTaskAllTask.setLayoutManager(layoutManager);
        rcvTaskAllTask.setAdapter(mAdapter);
        setTitle("退货取货-取货操作");
    }

    private void initListener() {
        mAdapter.setOnEditTextInputListener(new RBDetailTaskAdapter.OnEditTextInputClickListener() {
            @Override
            public void onEditInputListener(int position, String count) { //实际退货数量输入完成回调
                if (TextUtils.isEmpty(count)) {
                    return;
                }
                for (int i = 0; i < backGroupOrderEntities.size(); i++) {
                    if (i == position) {
                        backGroupOrderEntities.get(position).setRealAmount(Integer.valueOf(count));
                    }
                }
                computeRealPrice();

            }
        });
        swipeSearchList.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                offset = 0;
                initDatas();
            }
        });
    }

    private void initDatas() {
        backOrderid = getIntent().getExtras().getInt(ExampleConfig.ID, -1);
        type = getIntent().getExtras().getString(ExampleConfig.ACTIVITY_TYPE);
        btnTakeGoods.setVisibility(TextUtils.equals(type, "more") ? View.GONE : View.VISIBLE);
        tvReturnBackOrderId.setText(String.format("退货单号:%s", backOrderid));
        proDialogHelps.showProDialog("数据获取中...");
        Call<BaseResponseEntity<ReturnBackGroupOrderListEntity>> callPayMethod = webService.getBackOrderListDetail(backOrderid);
        callPayMethod.enqueue(new Callback<BaseResponseEntity<ReturnBackGroupOrderListEntity>>() {
            @Override
            public void onResponse(Call<BaseResponseEntity<ReturnBackGroupOrderListEntity>> call, Response<BaseResponseEntity<ReturnBackGroupOrderListEntity>> response) {
                proDialogHelps.removeProDialog();
                if (swipeSearchList != null && swipeSearchList.isRefreshing()) {
                    swipeSearchList.finishRefresh(); //刷新动画
                }
                if (response.isSuccessful() && response.body().getStatus() == 1) {
                    if (response.body().getData().getDetails().size() != 0) {
                        if (offset == 0) {
                            backGroupOrderEntities.clear();
                        }
                        isLoadMore = false;
                        backGroupOrderEntities = response.body().getData().getDetails();
                        offset = backGroupOrderEntities.size();
                        mAdapter.setLoadAll(true);
                        if (backGroupOrderEntities.size() == 0) {
                            mAdapter.setEmpty(true);
                        }
                        computeRealPrice();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.setmListData(backGroupOrderEntities);
                            }
                        });

                    }
                }

                if (TextUtils.equals(type, "more")) {
                    tvRealBackCount.setText(String.format("实退商品总数:%s", response.body().getData().getCount()));
                    tvRealBackMoney.setText(String.format("实退退货金额:%.2f", response.body().getData().getSum()));
                }
            }

            @Override
            public void onFailure(Call<BaseResponseEntity<ReturnBackGroupOrderListEntity>> call, Throwable t) {
                proDialogHelps.removeProDialog();
                Log.d("dd", t.getMessage());
            }
        });
    }

    private void computeRealPrice() { //计算实际价格
        if (!TextUtils.equals(type, "more")) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    int realSum = 0;
                    for (ReturnBackGroupOrderEntity order : backGroupOrderEntities) {
                        realSum += order.getRealAmount();
                    }
                    calulateBackGoods(backGroupOrderEntities);
                    tvRealBackCount.setText(String.format("实退商品总数:%s", realSum));
                }
            });
        }

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_return_back_task_detail;
    }

    @OnClick(R.id.btn_take_goods)
    public void onViewClicked() {//确认取货
        takeGoods(backGroupOrderEntities);
    }

    /**
     *  取货 & 取货退款金额计算 请求参数
     */
    private BackGoodsParams getBackGoodsParams(List<ReturnBackGroupOrderEntity> backGroupOrderEntities) {
        BackGoodsParams params = new BackGoodsParams();
        List<BackGoodsParams.Detail> details = new ArrayList<>();
        for (ReturnBackGroupOrderEntity order : backGroupOrderEntities) {
            details.add(new BackGoodsParams.Detail(order.getGoodId(), order.getId(), order.getRealAmount()));
            //实退数量小于等于退货数量,且>=0
            int count = order.getRealAmount();
            if (Integer.valueOf(count) > order.getOrgAmount() || Integer.valueOf(count) < 0) {
                ToastUtils.showShortToast("实退数量不能大于退货数量");
                return null;
            }
        }
        params.setBackOrderId(backOrderid);
        params.setDetail(details);
        return params;
    }

    /**
     *  取货取货退款金额计算
     */
    private  void calulateBackGoods(List<ReturnBackGroupOrderEntity> backGroupOrderEntities) {
        if (backGroupOrderEntities.size() == 0) {
            return;
        }

        BackGoodsParams params = getBackGoodsParams(backGroupOrderEntities);
        if (params == null) {
            return;
        }
        Call<BaseResponseEntity<Map>> callPayMethod = webService.calculateBackGoods(params);

        callPayMethod.enqueue(new Callback<BaseResponseEntity<Map>>() {
            @Override
            public void onResponse(Call<BaseResponseEntity<Map>> call, Response<BaseResponseEntity<Map>> response) {
                if (response.body() == null) {
                    return;
                }

                if (response.body().getStatus() == 1) {
                    Log.d("取货金额为：", response.body().toString());
                    Map data = response.body().getData();
                    Float sum = Float.parseFloat(data.get("realSum").toString()); //获取实际退货金额
                    if (sum != null) {
                        tvRealBackMoney.setText(String.format("实退退货金额:%.2f", sum));
                    } else {
                        tvRealBackMoney.setText("实退退货金额:0.00");
                    }
                } else {
                    ToastUtils.showShortToast(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseEntity<Map>> call, Throwable t) {

            }
        });

    }

    /**
     * 取货
     *
     * @param backGroupOrderEntities
     */
    private void takeGoods(List<ReturnBackGroupOrderEntity> backGroupOrderEntities) {
        if (backGroupOrderEntities.size() == 0) {
            return;
        }
        BackGoodsParams params = getBackGoodsParams(backGroupOrderEntities);
        if (params == null) {
            return;
        }
        proDialogHelps.showProDialog("取货中...");
        Call<BaseResponseEntity> callPayMethod = webService.returnBackOrder(params);
        callPayMethod.enqueue(new Callback<BaseResponseEntity>() {
            @Override
            public void onResponse(Call<BaseResponseEntity> call, Response<BaseResponseEntity> response) {
                proDialogHelps.removeProDialog();
                if (response.body() == null) {
                    return;
                }
                if (response.body().getStatus() == 1) {
                    ToastUtils.showShortToast("取货成功");
                    finish();
                } else {
                    ToastUtils.showShortToast(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<BaseResponseEntity> call, Throwable t) {
                proDialogHelps.removeProDialog();
            }
        });
    }
}
