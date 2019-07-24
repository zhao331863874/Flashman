package com.ddinfo.flashman.activity.menu.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.activity.task.ReturnBackTaskDetailActivity;
import com.ddinfo.flashman.activity.task.TaskDetailActivity;
import com.ddinfo.flashman.adapter.MoreTaskAdapter;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.impl.OnItemClickListenerRv;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.RecordsEntity;
import com.ddinfo.flashman.models.SeckillOrderEntity;
import com.ddinfo.flashman.models.params.WithdrawCashParams;
import com.ddinfo.flashman.network.SimpleCallBack;
import com.ddinfo.flashman.view.RecycleViewItemDecoration.ListItemDecoration;
import com.ddinfo.flashman.view.RefreshLayout.MaterialRefreshLayout;
import com.ddinfo.flashman.view.RefreshLayout.MaterialRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 提现历史 当前只包含押金体现历史
 */
public class WithDrawsHistoryActivity extends BaseActivity {
    @Bind(R.id.withdraw_history)
    RecyclerView withdrawHistoryViews;
    @Bind(R.id.swipe_search_list)
    MaterialRefreshLayout swipeSearchList;
    private WithdrawHistoryAdapter mAdapter;

    private List<RecordsEntity> list=new ArrayList<>();

    private int lastVisibleItem = 0;
    private boolean isLoadMore = false; //是否在加载
    private int offset;
    private int limit = 20; //最多加载数据

    private Callback<BaseResponseEntity<ArrayList<RecordsEntity>>> callbackInit;
    private LinearLayoutManager layoutManager;
    private ArrayList<RecordsEntity> newList;

    {
        callbackInit = new SimpleCallBack<BaseResponseEntity<ArrayList<RecordsEntity>>>(this) {
            @Override
            public void onSuccess(Call<BaseResponseEntity<ArrayList<RecordsEntity>>> call, Response<BaseResponseEntity<ArrayList<RecordsEntity>>> response) {
                if (swipeSearchList.isRefreshing()) {
                    swipeSearchList.finishRefresh();
                }
                isLoadMore = false;
                newList = response.body().getData();

                if(offset==0) list.clear();

                list.addAll(newList);
                offset = list.size();
                mAdapter.setLoadAll(newList.size() < limit); //加载数据不能大于20条
                mAdapter.setEmpty(list.size() == 0);

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onProDialogDismiss() {
                proDialogHelps.removeProDialog();
            }
        };
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("提现历史");
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        swipeSearchList.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                offset = 0;
                initData();
            }
        });

        withdrawHistoryViews.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (swipeSearchList != null && swipeSearchList.isRefreshing()) {
                    swipeSearchList.setRefreshing(false);
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem == mAdapter.getItemCount() - 1) {
                    if (newList.size() >= limit && !isLoadMore) {
                        isLoadMore = true;
                        //根据分类获取到商品列表
                        initData();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
            }
        });

        mAdapter.setOnItemClickListenerRv(new OnItemClickListenerRv() { //点击单条提现布局回调
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(context,WithdrawsDetailActivity.class);
                intent.putExtra(ExampleConfig.DATA,list.get(position));
                startActivity(intent);
            }
        });
    }

    private void initData() {
        proDialogHelps.showProDialog();
        isLoadMore = true;
        WithdrawCashParams params = new WithdrawCashParams(offset,limit,-1,1);
        Map map = new HashMap();
        map.put("offset",params.getOffset());
        map.put("limit",params.getLimit());
        map.put("type",params.getType());
        map.put("target",params.getTarget());

        Call<BaseResponseEntity<ArrayList<RecordsEntity>>> call = webService.balanceRecords(ExampleConfig.token
                , params.getOffset(),params.getLimit(),params.getType(),params.getTarget());
        call.enqueue(callbackInit);
    }

    private void initView() {
        layoutManager = new LinearLayoutManager(context);
        mAdapter = new WithdrawHistoryAdapter(list);
        withdrawHistoryViews.setLayoutManager(layoutManager);
        withdrawHistoryViews.addItemDecoration(new ListItemDecoration(this, ListItemDecoration.VERTICAL_LIST));
        withdrawHistoryViews.setAdapter(mAdapter);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_with_draws_history;
    }

    static class WithdrawHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{ //押金体现历史适配器
        List<RecordsEntity> list;
        private boolean isEmpty = false; //是否为空
        private boolean isLoadAll = false; //是否全部加载
        private OnItemClickListenerRv onItemClickListenerRv;

        public WithdrawHistoryAdapter(List<RecordsEntity> list) {
            this.list = list;
        }

        public void setEmpty(boolean empty) {
            isEmpty = empty;
        }

        public void setLoadAll(boolean loadAll) {
            isLoadAll = loadAll;
        }

        public void setOnItemClickListenerRv(OnItemClickListenerRv onItemClickListenerRv) {
            this.onItemClickListenerRv = onItemClickListenerRv;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            switch (viewType) {
                case ExampleConfig.VIEWHOLDER_NORMAL:
                    return new HistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_withdraw_record,parent,false)); //提现单条数据布局
                case ExampleConfig.VIEWHOLDER_FOOT:
                    View viewFooter = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer_load_more, null); //正在加载布局
                    return new ViewHolderFoot(viewFooter);
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
            if (position == list.size()) {
                ViewHolderFoot viewHolderFoot = (ViewHolderFoot) holder;
                if (!isEmpty) {
                    if (isLoadAll) {
                        viewHolderFoot.tvLoadMore.setText("已加载全部");
                    } else {
                        viewHolderFoot.tvLoadMore.setText("正在加载");
                    }
                } else {
                    viewHolderFoot.tvLoadMore.setText("暂无数据");
                }
            } else {
                HistoryViewHolder viewHolderNormal = (HistoryViewHolder) holder;
                viewHolderNormal.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { //点击提现单条数据布局
                        onItemClickListenerRv.onItemClick(v, position);
                    }
                });
                RecordsEntity recordsEntity = list.get(position);
                viewHolderNormal.Time.setText(recordsEntity.getCreatedAt()); //提现时间
                viewHolderNormal.Money.setText(String.format("%.2f",recordsEntity.getTransferAmount())); //提现金额
                viewHolderNormal.TypeState.setText(recordsEntity.getTypeState());
            }
        }

        @Override
        public int getItemCount() {
            return list==null?1:list.size()+1;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == list.size()) {
                return ExampleConfig.VIEWHOLDER_FOOT;
            }else {
                return ExampleConfig.VIEWHOLDER_NORMAL;
            }
        }
    }

    static class HistoryViewHolder extends RecyclerView.ViewHolder { //提现单条数据布局
        TextView Time,Money,TypeState;
        public HistoryViewHolder(View itemView) {
            super(itemView);
            Time = (TextView) itemView.findViewById(R.id.time); //提现时间
            Money = (TextView) itemView.findViewById(R.id.money); //提现金额
            TypeState = (TextView) itemView.findViewById(R.id.type_state); //提现名称
        }
    }

    static class ViewHolderFoot extends RecyclerView.ViewHolder { //正在加载布局
        public TextView tvLoadMore;
        public  RelativeLayout rlLoadMore;

        public ViewHolderFoot(View itemView) {
            super(itemView);
            tvLoadMore = (TextView) itemView.findViewById(R.id.tv_load_more); //正在加载
            rlLoadMore = (RelativeLayout) itemView.findViewById(R.id.rl_load_more);
        }
    }
}
