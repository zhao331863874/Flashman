package com.ddinfo.flashman.activity.task;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.SizeUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.LoginActivity;
import com.ddinfo.flashman.activity.RegisterActivity;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.activity.tab_frame.MainActivity;
import com.ddinfo.flashman.adapter.HomeAdapter;
import com.ddinfo.flashman.adapter.WaitForDeliveryAdapter;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.impl.OnItemClickListenerRv;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.RouteOrderEntity;
import com.ddinfo.flashman.models.SeckillOrderList;
import com.ddinfo.flashman.network.SimpleCallBack;
import com.ddinfo.flashman.utils.Utils;
import com.ddinfo.flashman.view.RefreshLayout.MaterialRefreshLayout;
import com.ddinfo.flashman.view.RefreshLayout.MaterialRefreshListener;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 待配送单列表
 * 2017年08月08日13:55:17 新建 by fuh
 */
public class WaitForDeliveryActivity extends BaseActivity {

    @Bind(R.id.recycle_view)
    RecyclerView recycleView;
    @Bind(R.id.swipe_search_list)
    MaterialRefreshLayout swipeSearchList;
    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;
    @Bind(R.id.rel_setting)
    RelativeLayout relSetting;

    public static final String ROUND_NAME = "ROUND_NAME";
    private String roundName; //路线名称

    private boolean isLoadMore; //是否加载中
    private LinearLayoutManager layoutManager;
    private WaitForDeliveryAdapter mAdapter; //待配送单适配器
    private int routeId; //路线ID

    private List<RouteOrderEntity> mListData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initView(){
        routeId = getIntent().getExtras().getInt("routeId");
        roundName = getIntent().getExtras().getString("routeName");
        setTitle(roundName);
        initRecycle();
        initListener();
    }



    private void initRecycle() {
        layoutManager = new LinearLayoutManager(context);
        mAdapter = new WaitForDeliveryAdapter(context);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(mAdapter);
    }

    private void initListener() {
        swipeSearchList.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                initData();
            }
        });
    }

    private void initData(){
        isLoadMore = true;
        proDialogHelps.showProDialog("正在加载订单信息...");

        Call<BaseResponseEntity<ArrayList<RouteOrderEntity>>> callSeckillOrderList = webService.getRouteOrderList(routeId);
        callSeckillOrderList.enqueue(new SimpleCallBack<BaseResponseEntity<ArrayList<RouteOrderEntity>>>(context) {
            @Override
            public void onSuccess(Call<BaseResponseEntity<ArrayList<RouteOrderEntity>>> call, Response<BaseResponseEntity<ArrayList<RouteOrderEntity>>> response) {
                if (swipeSearchList != null && swipeSearchList.isRefreshing()) {
                    swipeSearchList.finishRefresh();
                }

                isLoadMore = false;
                mListData = response.body().getData();;
                if (mListData.size() == 0) {
                    mAdapter.setIsEmpty(true);
                }else{
                    mAdapter.setIsLoadAll(true);
                }
                mAdapter.setmListData(mListData);
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

    @OnClick({R.id.rel_setting, R.id.txt_no_network_try_again, R.id.txt_empty_try_again})
    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.txt_empty_try_again: //重新加载
                initData();
                break;
            case R.id.rel_setting: //请检查您的网络
                Utils.openSetting(this);
                if (relSetting != null) {
                    relSetting.setVisibility(View.GONE);
                }
                break;
            case R.id.txt_no_network_try_again: //重新加载
                initData();
                break;
        }
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.layout_recycleview;
    }
}
