package com.ddinfo.flashman.activity.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.NetworkUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.adapter.PaymentAdapter;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.impl.OnItemClickListenerRv;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.BatchOrderEntity;
import com.ddinfo.flashman.models.PaymentEntity;
import com.ddinfo.flashman.models.params.CreateBatchOrderParams;
import com.ddinfo.flashman.network.SimpleCallBack;
import com.ddinfo.flashman.utils.Utils;
import com.ddinfo.flashman.view.RefreshLayout.MaterialRefreshLayout;
import com.ddinfo.flashman.view.RefreshLayout.MaterialRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 交货款单界面
 */
public class PaymentActivity extends BaseActivity {

    @Bind(R.id.rcv_payment)
    RecyclerView rcvPayment;
    @Bind(R.id.swipe_search_list)
    MaterialRefreshLayout swipeSearchList;
    @Bind(R.id.activity_payment)
    LinearLayout activityPayment;
    @Bind(R.id.left_button)   //左返回按钮
    ImageButton leftButton;
    @Bind(R.id.header_name)   //标题抬头
    TextView headerName;
    @Bind(R.id.right_button)
    ImageButton rightButton;
    @Bind(R.id.rightBtn)
    Button rightBtn;
    @Bind(R.id.text_sel_order)//已选单数名称
    TextView textSelOrder;
    @Bind(R.id.tv_sel_number) //已选单数
    TextView tvSelNumber;
    @Bind(R.id.tv_sel_amount) //交货款金额
    TextView tvSelAmount;
    @Bind(R.id.tv_enter)      //交货款按钮
    TextView tvEnter;
    @Bind(R.id.tv_all_sel)    //全选
    TextView tvAllSel;
    @Bind(R.id.tv_adverse_sel)//反选
    TextView tvAdverseSel;


    private LinearLayoutManager layoutManager;
    private PaymentAdapter mAdapter;
    private int lastVisibleItem = 0; //最后一个可见view的位置
    private boolean isLoadMore = false;
    private int offset;
    int selNum = 0;    //已选单数
    double amount = 0; //交货款金额
    private List<PaymentEntity> mListData = new ArrayList<>();
    private List<PaymentEntity> mListDataNew = new ArrayList<>();
    private ArrayList<Integer> deliveryOrderIds = new ArrayList<>(); //所选中交货款项


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initListener();
    }

    private void initView() {
        setTitle("交货款");
        layoutManager = new LinearLayoutManager(context);
        mAdapter = new PaymentAdapter(this);
        rcvPayment.setLayoutManager(layoutManager);
        rcvPayment.setAdapter(mAdapter);
    }

    private void initData() {
        proDialogHelps.showProDialog();
        Call<BaseResponseEntity<ArrayList<PaymentEntity>>> callPaymentList = webService.getPayments(ExampleConfig.token, offset, 15);
        callPaymentList.enqueue(callbackPaymentList);
        isLoadMore = true;
    }

    Callback<BaseResponseEntity<ArrayList<PaymentEntity>>> callbackPaymentList = new SimpleCallBack<BaseResponseEntity<ArrayList<PaymentEntity>>>(this) {
        @Override
        public void onSuccess(Call<BaseResponseEntity<ArrayList<PaymentEntity>>> call, Response<BaseResponseEntity<ArrayList<PaymentEntity>>> response) {
            if (swipeSearchList.isRefreshing()) {
                swipeSearchList.finishRefresh();
            }
            if(offset == 0){
                mListData.clear();
            }
            isLoadMore = false;
            mListDataNew = response.body().getData();
            for (int i = 0; i < mListDataNew.size(); i++) {
                mListDataNew.get(i).setChecked(false);
            }
            mListData.addAll(mListDataNew);
            offset = mListData.size();
            if (mListData.size() == 0) {
                mAdapter.setIsEmpty(true);
            }

            if (mListDataNew.size() < 15) {
                mAdapter.setIsLoadAll(true);
            }
            mAdapter.setmListData(mListData);
            updataSelItem();
        }

        @Override
        public void onProDialogDismiss() {
            proDialogHelps.removeProDialog();
        }

    };

    @OnClick({R.id.tv_enter,R.id.tv_all_sel,R.id.tv_adverse_sel})
    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.tv_enter: //点击交货款
                if(selNum == 0){
                    ToastUtils.showShortToast("请选择所交货款");
                }else{
                    CreateBatchOrderParams params = new CreateBatchOrderParams(deliveryOrderIds,Utils.numFormat(amount));
                    Call<BaseResponseEntity<BatchOrderEntity>> callCreate = webService.createBatchOrder(params);
                    callCreate.enqueue(new SimpleCallBack<BaseResponseEntity<BatchOrderEntity>>(context) {
                        @Override
                        public void onSuccess(Call<BaseResponseEntity<BatchOrderEntity>> call, Response<BaseResponseEntity<BatchOrderEntity>> response) {
                            Intent intent = new Intent(context,PaymentDetailActivity.class);
                            intent.putExtra("index",99);
                            intent.putExtra("id",response.body().getData().getId());
                            startActivity(intent);
                        }

                        @Override
                        public void onProDialogDismiss() {
                            if(swipeSearchList !=null && swipeSearchList.isRefreshing()){
                                swipeSearchList.finishRefresh();
                            }
                            proDialogHelps.removeProDialog();
                        }
                    });

                }
                break;
            case R.id.tv_all_sel: //全选
                for (int i = 0; i < mListData.size(); i++) {
                    mListData.get(i).setChecked(true);
                    deliveryOrderIds.add(mListData.get(i).getId());
                }
                mAdapter.notifyDataSetChanged();
                updataSelItem();
                break;
            case R.id.tv_adverse_sel: //反选
                for (int i = 0; i < mListData.size(); i++) {
                    if(mListData.get(i).getChecked()){
                        mListData.get(i).setChecked(false);
                    }else{
                        mListData.get(i).setChecked(true);
                        deliveryOrderIds.add(mListData.get(i).getId());
                    }
                }
                mAdapter.notifyDataSetChanged();
                updataSelItem();
                break;
        }
    }

    private void initListener() {
        swipeSearchList.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                offset = 0;
                mListData.clear();
                initData();
            }
        });

        mAdapter.setItemClickListener(new OnItemClickListenerRv() {
            @Override
            public void onItemClick(View view, int position) { //点击单条交货款项回调
                if (mListData.get(position).getChecked()) { //如果已选中当条交货款则设置未选中
                    mListData.get(position).setChecked(false);
                } else {
                    mListData.get(position).setChecked(true);
                }
                mAdapter.notifyDataSetChanged();
                updataSelItem();
            }
        });

        rcvPayment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) { //滚动状态变化时回调
                super.onScrollStateChanged(recyclerView, newState);
                if (swipeSearchList.isRefreshing()) {
                    swipeSearchList.setRefreshing(false);
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem == mAdapter.getItemCount() - 1) {
                    if (mListDataNew.size() == 15 && !isLoadMore) {

                        isLoadMore = true;
                        //根据分类获取到商品列表
                        initData();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) { //滚动时回调
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();  //获取最后一个可见view的位置
            }
        });
    }

    private void updataSelItem(){ //更新所选数量和总金额
        selNum = 0;
        amount = 0.0;
        deliveryOrderIds.clear();
        for (int i = 0; i < mListData.size(); i++) {
            if (mListData.get(i).getChecked()) {
                selNum++;
                amount += Double.parseDouble(mListData.get(i).getOrderAmount());
                deliveryOrderIds.add(mListData.get(i).getId());
            }
        }
        tvSelNumber.setText("" + selNum);
        tvSelAmount.setText("￥" + Utils.numFormat(amount));
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_payment;
    }


    @Override
    protected void onResume() {
        offset = 0;
        if (NetworkUtils.isConnected()) {
            initData();
        } else {
            ToastUtils.showShortToast("网络不可用");
        }
        super.onResume();
    }
}
