package com.ddinfo.flashman.activity.task;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.adapter.MoreTaskAdapter;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.impl.OnItemClickListenerRv;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.SeckillOrderEntity;
import com.ddinfo.flashman.models.SeckillOrderList;
import com.ddinfo.flashman.network.SimpleCallBack;
import com.ddinfo.flashman.view.RefreshLayout.MaterialRefreshLayout;
import com.ddinfo.flashman.view.RefreshLayout.MaterialRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 更多任务列表界面
 */
public class TaskMoreListActivity extends BaseActivity {

    @Bind(R.id.tv_count_left) //今日完成订单数量
    TextView tvCountLeft;
    @Bind(R.id.tv_text_left)  //今日完成订单数
    TextView tvTextLeft;
    @Bind(R.id.tv_count_right_top) //上月累计完成订单数量
    TextView tvCountRightTop;
    @Bind(R.id.text_count_right_top) //上月累计完成订单数
    TextView textCountRightTop;
    @Bind(R.id.tv_count_right_bottom) //本月累计完成订单数量
    TextView tvCountRightBottom;
    @Bind(R.id.text_count_right_bottom) //本月累计完成订单数
    TextView textCountRightBottom;
    @Bind(R.id.rcv_task_more_task) //订单数量展示列表
    RecyclerView rcvTaskMoreTask;
    @Bind(R.id.swipe_search_list)
    MaterialRefreshLayout swipeSearchList;
    @Bind(R.id.activity_task_more_list)
    LinearLayout activityTaskMoreList;
    @Bind(R.id.ll_order_top)
    LinearLayout llOrderTop;


    private int type, flag;
    private List<SeckillOrderList> mListData = new ArrayList<>();
    private List<SeckillOrderList> mListDataNew = new ArrayList<>();
    private MoreTaskAdapter mAdapter;
    private LinearLayoutManager layoutManager;

    private int lastVisibleItem = 0;
    private boolean isLoadMore = false; //是否加载中
    private int offset;
    private int limit = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        type = getIntent().getExtras().getInt(ExampleConfig.ACTIVITY_TYPE);
        flag = getIntent().getExtras().getInt(ExampleConfig.ACTIVITY_FLAG);
        switch (type) {
            case 4: //退货取消、已完成订单
                if (flag == 1) {
                    setTitle("退货取消");
                    llOrderTop.setVisibility(View.GONE);
                } else {
                    setTitle("已完成订单");
                    tvTextLeft.setText("今日完成订单数");
                    textCountRightTop.setText("上月累计完成订单数");
                    textCountRightBottom.setText("本月累计完成订单数");
                }
                break;
            case 5:
                setTitle("已取消订单");
                tvTextLeft.setText("今日取消订单数");
                textCountRightTop.setText("上月累计取消订单数");
                textCountRightBottom.setText("本月累计取消订单数");
                break;
            case 2:
                setTitle("退货返还中");
                llOrderTop.setVisibility(View.GONE);
                break;
            case 3:
                setTitle("退货完成");
                tvTextLeft.setText("今日完成退货单数");
                textCountRightTop.setText("上月累积结束退货单数");
                textCountRightBottom.setText("本月累积结束退货单数");
                break;
            default:
                break;
        }

        layoutManager = new LinearLayoutManager(context);
        mAdapter = new MoreTaskAdapter(this);
        rcvTaskMoreTask.setLayoutManager(layoutManager);
        //        rcvTaskMoreTask.addItemDecoration(new ListItemDecoration(context,ListItemDecoration.VERTICAL_LIST));
        rcvTaskMoreTask.setAdapter(mAdapter);
    }

    private void initData() {
        proDialogHelps.showProDialog();
        //            Call<BaseResponseEntity<ArrayList<SeckillOrderList>>> callOrderList = webService.getOrderList(ExampleConfig.token,mPosition,offset,15);
        isLoadMore = true;
        Call<BaseResponseEntity<SeckillOrderEntity>> callOrderList = webService.getHistoryOrderList(ExampleConfig.token, flag, type, offset, limit);
        callOrderList.enqueue(callBackOrderList);
    }

    Callback<BaseResponseEntity<SeckillOrderEntity>> callBackOrderList = new SimpleCallBack<BaseResponseEntity<SeckillOrderEntity>>(this) {
        @Override
        public void onSuccess(Call<BaseResponseEntity<SeckillOrderEntity>> call, Response<BaseResponseEntity<SeckillOrderEntity>> response) {
            if (swipeSearchList.isRefreshing()) {
                swipeSearchList.finishRefresh();
            }
            isLoadMore = false;
            SeckillOrderEntity entity = response.body().getData();
            tvCountLeft.setText(entity.getTodayOrderCount() + ""); //今日完成订单数量
            tvCountRightTop.setText(entity.getPreMonthOrderCount() + ""); //上月累计完成订单数量
            tvCountRightBottom.setText(entity.getThisMonthOrderCount() + ""); //本月累计完成订单数量
            mListDataNew = entity.getList();
            mListData.addAll(mListDataNew);
            offset = mListData.size();
            if (mListDataNew.size() < limit) {
                mAdapter.setLoadAll(true);
            }

            if (mListData.size() == 0) {
                mAdapter.setEmpty(true);
            }
            mAdapter.setItemType(flag);
            mAdapter.setmListData(mListData);
        }

        @Override
        public void onProDialogDismiss() {
            proDialogHelps.removeProDialog();
        }
    };

    private void initListener() {
        swipeSearchList.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                offset = 0;
                mListData.clear();
                initData();
            }
        });

        rcvTaskMoreTask.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) { //滚动状态变化时回调
                super.onScrollStateChanged(recyclerView, newState);
                if (swipeSearchList != null && swipeSearchList.isRefreshing()) {
                    swipeSearchList.setRefreshing(false);
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem == mAdapter.getItemCount() - 1) {
                    if (mListDataNew.size() <= limit && !isLoadMore) {
                        isLoadMore = true;
                        //根据分类获取到商品列表
                        initData();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) { //滚动时回调
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition(); //返回当前RecycelrView中最后一个可见的item的adapter postion
            }
        });

        mAdapter.setOnItemClickListenerRv(new OnItemClickListenerRv() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle bundle = new Bundle();
                if (flag == 1) {//退货
                    bundle.putInt(ExampleConfig.ID, mListData.get(position).getBackOrderId());
                    bundle.putString(ExampleConfig.ACTIVITY_TYPE, "more");
                    startActivity(ReturnBackTaskDetailActivity.class, bundle);
                } else { //已完成、已取消订单
                    bundle.putInt(ExampleConfig.TASK_DETAIL_TYPE, type);
                    bundle.putInt(ExampleConfig.ID, mListData.get(position).getId());
                    startActivity(TaskDetailActivity.class, bundle);
                }
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_task_more_list;
    }
}
