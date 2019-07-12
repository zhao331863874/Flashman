package com.ddinfo.flashman.activity.menu;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.activity.menu.wallet.PayOrderActivity;
import com.ddinfo.flashman.adapter.PaymentDetailAdapter;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.PaymentDetailEntity;
import com.ddinfo.flashman.models.params.IdParams;
import com.ddinfo.flashman.network.SimpleCallBack;
import com.ddinfo.flashman.view.RefreshLayout.MaterialRefreshLayout;
import com.kennyc.view.MultiStateView;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class PaymentDetailActivity extends BaseActivity {

    @Bind(R.id.recycle_view)
    RecyclerView recycleView;
    @Bind(R.id.swipe_search_list)
    MaterialRefreshLayout swipeSearchList;
    @Bind(R.id.tv_money)
    TextView tvMoney;
    @Bind(R.id.tv_re_pay)
    TextView tvRePay;
    @Bind(R.id.ll_bottom)
    LinearLayout llBottom;
    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;
    @Bind(R.id.activity_payment_detail)
    LinearLayout activityPaymentDetail;

    private LinearLayoutManager layoutManager;
    private PaymentDetailAdapter mAdapter;
    private PaymentDetailEntity mData;

    private int index;
    private int id;

    private double sum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        setTitle("交货款单详情");
        index = getIntent().getIntExtra("index",-1);
        id = getIntent().getIntExtra("id",-1);
        layoutManager = new LinearLayoutManager(context);
        mAdapter = new PaymentDetailAdapter(this);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(mAdapter);

    }

    private void initData() {
        switch (index){
            case 0: //交货款充值
                tvRePay.setText("支付");
                break;
            case 1:
                tvRePay.setText("重新支付");
                break;
            case 2:
                llBottom.setVisibility(View.GONE);
                break;
            case 3:
                llBottom.setVisibility(View.GONE);
                break;
            case 99: //生成货款单
                tvRePay.setText("支付");
                break;
        }

        proDialogHelps.showProDialog();
        IdParams params = new IdParams(id);
        Call<BaseResponseEntity<PaymentDetailEntity>> callDetail = webService.getPaymentDetail(params);
        callDetail.enqueue(new SimpleCallBack<BaseResponseEntity<PaymentDetailEntity>>(this) {
            @Override
            public void onSuccess(Call<BaseResponseEntity<PaymentDetailEntity>> call, Response<BaseResponseEntity<PaymentDetailEntity>> response) {
                mData = response.body().getData();
                mAdapter.setData(mData,index);
                sum = mData.getInfo().getSum();
                tvMoney.setText("￥"+sum);
                id = mData.getInfo().getId();
            }

            @Override
            public void onProDialogDismiss() {
                if (swipeSearchList.isRefreshing()) {
                    swipeSearchList.setRefreshing(false);
                }
                proDialogHelps.removeProDialog();
            }
        });
    }

    private void initListener() {

    }

    @OnClick({R.id.tv_re_pay})
    public void doClick(View view) {
        switch (view.getId()){
            case R.id.tv_re_pay:
                if(sum!=0 && id !=0){
                    Bundle bundle = new Bundle();
                    bundle.putDouble(ExampleConfig.INTENT_TOPUPMONEY, sum);
                    bundle.putInt("id",id);
                    bundle.putString("type","deliveryManPay"); //交货款标记
                    startActivity(PayOrderActivity.class, bundle);
                }else{
                    ToastUtils.showShortToast("未获取到货款信息");
                }
                break;
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_payment_detail;
    }
}
