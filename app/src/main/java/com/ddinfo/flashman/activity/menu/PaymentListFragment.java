package com.ddinfo.flashman.activity.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseFragment;
import com.ddinfo.flashman.adapter.PaymentListAdapter;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.PaymentListEntity;
import com.ddinfo.flashman.models.params.PaymentListParams;
import com.ddinfo.flashman.network.SimpleCallBack;
import com.ddinfo.flashman.view.RecycleViewItemDecoration.ListItemDecoration;
import com.ddinfo.flashman.view.RefreshLayout.MaterialRefreshLayout;
import com.ddinfo.flashman.view.RefreshLayout.MaterialRefreshListener;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Response;

public class PaymentListFragment extends BaseFragment {

    private static final String KEY_INDEX = "KEY_INDEX";
    private static PaymentListFragment mFragment;
    @Bind(R.id.swipe_search_list)
    MaterialRefreshLayout swipeSearchList;
    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;
    @Bind(R.id.recycle_view)
    RecyclerView recycleView;

    private int fragIndex; //位置索引 0：待支付 1：支付失败 2：支付异常 3：支付成功

    private LinearLayoutManager layoutManager;
    private boolean isPrepared = false;  //是否准备好(View创建成功)
    private PaymentListAdapter mAdapter; //交货款单列表适配器
    private List<PaymentListEntity> mListData = new ArrayList<>();
    private List<PaymentListEntity> mListDataNew = new ArrayList<>();

    private int lastVisibleItem = 0;    //最后一个可见view的位置
    private boolean isLoadMore = false; //是否加载中
    private int offset;
    private int limit = 20;

    private long stamp; //时间 毫秒
    private int type;//支付类型 0：未支付 99：支付成功 -99：支付失败 -98：支付异常

    public PaymentListFragment() {
    }

    /**
     * @param position 选项卡索引 0：待支付 1：支付失败 2：支付异常 3：支付成功
     * @return
     */
    public static PaymentListFragment newInstance(int position) {
        mFragment = new PaymentListFragment();
        Bundle args = new Bundle();
        args.putInt(KEY_INDEX, position);
        mFragment.setArguments(args); //传递参数
        return mFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) { //当onCreateView的View创建成功
        isPrepared = true;
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void lazyResumeLoad() {
        initDatas();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.common_recycle;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        if (getArguments() != null) {
            fragIndex = getArguments().getInt(KEY_INDEX);
        }
        stamp = new Date().getTime();
        layoutManager = new LinearLayoutManager(context);
        mAdapter = new PaymentListAdapter(context);
        recycleView.setLayoutManager(layoutManager);
        recycleView.addItemDecoration(new ListItemDecoration(context,ListItemDecoration.VERTICAL_LIST));
        recycleView.setAdapter(mAdapter);
        initListener();
    }

    @Override
    protected void initDatas() { //初始化数据
        if (!isPrepared) {
            return;
        }
        getData(fragIndex);
    }

    private void getData(final int index){ //获取数据
        switch (index){
            case 1:
                type = -99;
                break;
            case 2:
                type = -98;
                break;
            case 3:
                type = 99;
                break;
        }
        proDialogHelps.showProDialog();
        PaymentListParams params = new PaymentListParams(limit,offset+1,stamp,type);
        Call<BaseResponseEntity<ArrayList<PaymentListEntity>>> callBoardList = webService.getPaymentList(params);
        callBoardList.enqueue(new SimpleCallBack<BaseResponseEntity<ArrayList<PaymentListEntity>>>(context) {
            @Override
            public void onSuccess(Call<BaseResponseEntity<ArrayList<PaymentListEntity>>> call, Response<BaseResponseEntity<ArrayList<PaymentListEntity>>> response) {
                isLoadMore = false;
                if(offset ==0){
                    mListData.clear();
                }

                mListDataNew = response.body().getData();
                mListData.addAll(mListDataNew);
                offset = mListData.size();
                if (mListData.size() == 0) {
                    mAdapter.setEmpty(true);
                }
                if (mListDataNew.size() < 15) {
                    mAdapter.setLoadAll(true);
                }
                mAdapter.setListData(mListData);
            }

            @Override
            public void onProDialogDismiss() {
                if (swipeSearchList !=null && swipeSearchList.isRefreshing()) {
                    swipeSearchList.finishRefresh();
                }
                proDialogHelps.removeProDialog();
            }
        });
    }

    @Override
    protected void initListener() {
        swipeSearchList.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                offset = 0;
                getData(fragIndex);
            }
        });

        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) { //用于监听ListView滑动状态的变化
                super.onScrollStateChanged(recyclerView, newState);
                if (swipeSearchList.isRefreshing()) {
                    swipeSearchList.setRefreshing(false);
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem == mAdapter.getItemCount() - 1) { //SCROLL_STATE_IDLE停止滑动状态
                    if (mListDataNew.size() == 15 && !isLoadMore) {
                        isLoadMore = true;
                        //根据分类获取到商品列表
                        initDatas();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) { //用于监听ListView屏幕滚动
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();  //获取最后一个可见view的位置
            }
        });

        mAdapter.setOnItemClickListenerRv(new PaymentListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) { //点击交货款单布局回调
                Intent intent = new Intent(context,PaymentDetailActivity.class);
                intent.putExtra("index",fragIndex);
                intent.putExtra("id",mListData.get(position).getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isPrepared = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        proDialogHelps.removeProDialog();
        if (isVisible && isPrepared) {
            initDatas();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
