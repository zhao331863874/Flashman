package com.ddinfo.flashman.activity.task;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.adapter.FrozenDetailAdapter;
import com.ddinfo.flashman.adapter.SupervinsingJuniorAdapter;
import com.ddinfo.flashman.view.RefreshLayout.MaterialRefreshLayout;
import com.ddinfo.flashman.view.RefreshLayout.MaterialRefreshListener;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Gavin on 2017/8/14.
 * 下级配送员_配送完成情况
 */

public class FrozenDeliveryActivity extends BaseActivity {

    @Bind(R.id.recycle_view)
    RecyclerView recycleView;
    @Bind(R.id.swipe_search_list)
    MaterialRefreshLayout swipeSearchList;
    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;
    @Bind(R.id.rel_setting)
    RelativeLayout relSetting;

    private int offset;
    private static final int limit = 20;
    private int lastVisibleItem = 0;
    private boolean isLoadMore = false;
    private LinearLayoutManager layoutManager;
    private FrozenDetailAdapter mAdapter;

    private List<String> mListData = new ArrayList<>();
    private List<String> mListDataNew = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        setTitle("冻结明细");
        initRecycle();

    }

    private void initRecycle() {
        layoutManager = new LinearLayoutManager(context);
        mAdapter = new FrozenDetailAdapter(context);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(mAdapter);
    }

    private void initData() {
        for (int i = 0; i < 30; i++) {
            mListData.add(i + "");
        }
    }

    private void initListener() {

        swipeSearchList.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                offset = 0;
                initData();
            }
        });

        recycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (swipeSearchList != null && swipeSearchList.isRefreshing()) {
                    swipeSearchList.setRefreshing(false);
                }
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItem == mAdapter.getItemCount() - 1) {
                    if (mListDataNew.size() == offset && !isLoadMore) {
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
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_recycleview;
    }
}
