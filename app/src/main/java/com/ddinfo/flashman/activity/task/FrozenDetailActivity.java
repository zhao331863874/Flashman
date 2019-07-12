package com.ddinfo.flashman.activity.task;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.adapter.FrozenDetailAdapter;
import com.ddinfo.flashman.adapter.SupervinsingJuniorAdapter;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.FrozenDetailsEntity;
import com.ddinfo.flashman.network.SimpleCallBack;
import com.ddinfo.flashman.network.WebService;
import com.ddinfo.flashman.view.RefreshLayout.MaterialRefreshLayout;
import com.ddinfo.flashman.view.RefreshLayout.MaterialRefreshListener;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FrozenDetailActivity extends BaseActivity {

    @Bind(R.id.recycle_view)
    RecyclerView recycleView;
    @Bind(R.id.swipe_search_list)
    MaterialRefreshLayout swipeSearchList;
    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;
    @Bind(R.id.rel_setting)
    RelativeLayout relSetting;

    private boolean isLoadMore = false;
    private LinearLayoutManager layoutManager;
    private FrozenDetailAdapter mAdapter;

    private List<FrozenDetailsEntity> mListData = new ArrayList<>();
    private List<FrozenDetailsEntity> mListDataNew = new ArrayList<>();
    private int deliveryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        setTitle("冻结明细");
        try{
            deliveryId = getIntent().getExtras().getInt("deliveryId");
        }catch (NullPointerException e){
            deliveryId = -1;
        }
        initRecycle();

    }

    private void initRecycle() {
        layoutManager = new LinearLayoutManager(context);
        mAdapter = new FrozenDetailAdapter(context);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(mAdapter);
    }

    private void initData() {
        Call<BaseResponseEntity<ArrayList<FrozenDetailsEntity>>> callFrozen;
        if(deliveryId == -1){
            callFrozen = webService.getFrozenDetail();
        }else{
            callFrozen = webService.getFrozenDetail(deliveryId);
        }
        callFrozen.enqueue(callbackFrozen);
    }

    Callback<BaseResponseEntity<ArrayList<FrozenDetailsEntity>>> callbackFrozen = new SimpleCallBack<BaseResponseEntity<ArrayList<FrozenDetailsEntity>>>(context) {
        @Override
        public void onSuccess(Call<BaseResponseEntity<ArrayList<FrozenDetailsEntity>>> call,
            Response<BaseResponseEntity<ArrayList<FrozenDetailsEntity>>> response) {
            mListData = response.body().getData();
            if(mListData.size() == 0){
                mAdapter.setEmpty(true);
            }else{
                mAdapter.setLoadAll(true);
            }
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
                initData();
            }
        });

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_recycleview;
    }
}
