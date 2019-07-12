package com.ddinfo.flashman.activity.menu;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.TranStatusBarActivity;
import com.ddinfo.flashman.adapter.BoardAdapter;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.BoardEntity;
import com.ddinfo.flashman.network.SimpleCallBack;
import com.ddinfo.flashman.view.RefreshLayout.MaterialRefreshLayout;
import com.ddinfo.flashman.view.RefreshLayout.MaterialRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Response;

public class BoardActivity extends TranStatusBarActivity {
    @Bind(R.id.tv_cur_income)
    TextView tvCurIncome;
    @Bind(R.id.tv_cur_income_text)
    TextView tvCurIncomeText;
    @Bind(R.id.tv_cur_all_count)
    TextView tvCurAllCount;
    @Bind(R.id.tv_cur_all_count_text)
    TextView tvCurAllCountText;
    @Bind(R.id.tv_cur_all_money)
    TextView tvCurAllMoney;
    @Bind(R.id.tv_cur_all_money_text)
    TextView tvCurAllMoneyText;
    @Bind(R.id.tv_month)
    TextView tvMonth;
    @Bind(R.id.ll_board_head)
    LinearLayout llBoardHead;
    @Bind(R.id.rcv_payment)
    RecyclerView rcvPayment;
    @Bind(R.id.swipe_search_list)
    MaterialRefreshLayout swipeSearchList;
    @Bind(R.id.activity_board)
    LinearLayout activityBoard;
    private LinearLayoutManager layoutManager;
    private BoardAdapter mAdapter;
    private int deliveryId;

    private List<BoardEntity> mListData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    @Override
    protected Object statusBarStyle() {
        return R.color.bg_blue;
    }

    private void initView() {
        try{
            deliveryId = getIntent().getExtras().getInt("deliveryId",-1);
        }catch (NullPointerException e){
            deliveryId = -1;
        }

        if(deliveryId == -1){
            setTitle("我的看板");
        }else{
            setTitle("下级配送看板");
        }

        layoutManager = new LinearLayoutManager(context);
        mAdapter = new BoardAdapter(this);
        rcvPayment.setLayoutManager(layoutManager);
        rcvPayment.setAdapter(mAdapter);
    }

    private void initData() {
        getData();
    }

    private void getData() {
        proDialogHelps.showProDialog();

        if(deliveryId ==-1){
            Call<BaseResponseEntity<ArrayList<BoardEntity>>> callBoardList = webService.getBoardList();
            callBoardList.enqueue(callBackBoardList);
        }else{
            Call<BaseResponseEntity<ArrayList<BoardEntity>>> callDeliveryBoardList = webService.getDeliveryBoardList(deliveryId);
            callDeliveryBoardList.enqueue(callBackBoardList);
        }
    }

    SimpleCallBack<BaseResponseEntity<ArrayList<BoardEntity>>> callBackBoardList = new SimpleCallBack<BaseResponseEntity<ArrayList<BoardEntity>>>(context) {
        @Override
        public void onSuccess(Call<BaseResponseEntity<ArrayList<BoardEntity>>> call, Response<BaseResponseEntity<ArrayList<BoardEntity>>> response) {
            mListData = response.body().getData();
            mAdapter.setListData(mListData);
        }

        @Override
        public void onProDialogDismiss() {
            if (swipeSearchList.isRefreshing()) {
                swipeSearchList.setRefreshing(false);
            }
            proDialogHelps.removeProDialog();
        }
    };

    private void initListener() {
        swipeSearchList.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                getData();
            }
        });

        //下拉刷新时会显示刷新动画 避免吸顶View遮挡
        //等刷新回调完成 再显示
        swipeSearchList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                llBoardHead.setVisibility(View.GONE);
                return false;
            }
        });

        rcvPayment.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (swipeSearchList.isRefreshing()) {
                    swipeSearchList.setRefreshing(false);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                showBoardHeadView();
                llBoardHead.setVisibility(View.VISIBLE);
            }
        });


    }

    private void showBoardHeadView() {
        llBoardHead.setVisibility(View.VISIBLE);
        View stickyInfoView = rcvPayment.findChildViewUnder(llBoardHead.getMeasuredWidth() / 2, 10);
        if (stickyInfoView != null) {
            int groupIndex = ((BoardAdapter.ItemMessage) stickyInfoView.getTag()).getParentPosition();
            tvMonth.setText(mListData.get(groupIndex).getMonths());
            BoardEntity boardEntity = mListData.get(groupIndex);
            tvCurAllCount.setText(boardEntity.getNum()+"笔");
            tvCurAllCountText.setText(boardEntity.getMonths() + "月份累计总笔数");
            tvCurAllMoney.setText("￥" + boardEntity.getOrderSum());
            tvCurAllMoneyText.setText(boardEntity.getMonths() + "月份总金额");
            tvCurIncome.setText(boardEntity.getCommissionSum()+"");
            tvCurIncomeText.setText(boardEntity.getMonths() + "月份收益");
        }
        // 找到固定在屏幕上方那个FakeStickyLayout下面一个像素位置的RecyclerView的item，
        // 我们根据这个item来更新假的StickyLayout要translate多少距离.
        // 并且只处理HAS_STICKY_VIEW和NONE_STICKY_VIEW这两种tag，
        // 因为第一个item的StickyLayout虽然展示，但是一定不会引起FakeStickyLayout的滚动.
        View transInfoView = rcvPayment.findChildViewUnder(
                llBoardHead.getMeasuredWidth() / 2, llBoardHead.getMeasuredHeight() + 1);

        if (transInfoView != null && transInfoView.getTag() != null) {
            int transViewStatus = ((BoardAdapter.ItemMessage) transInfoView.getTag()).getViewType();
            int dealtY = transInfoView.getTop() - llBoardHead.getMeasuredHeight();

            // 如果当前item需要展示StickyLayout，
            // 那么根据这个item的getTop和FakeStickyLayout的高度相差的距离来滚动FakeStickyLayout.
            // 这里有一处需要注意，如果这个item的getTop已经小于0，也就是滚动出了屏幕，
            // 那么我们就要把假的StickyLayout恢复原位，来覆盖住这个item对应的吸顶信息.
            if (transViewStatus == ExampleConfig.VIEWHOLDER_HEAD) {
                if (transInfoView.getTop() > 0) {
                    llBoardHead.setTranslationY(dealtY);
                } else {
                    llBoardHead.setTranslationY(0);
                }
            } else if (transViewStatus == ExampleConfig.VIEWHOLDER_NORMAL) {
                // 如果当前item不需要展示StickyLayout，那么就不会引起FakeStickyLayout的滚动.
                llBoardHead.setTranslationY(0);
            }
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_board;
    }
}
