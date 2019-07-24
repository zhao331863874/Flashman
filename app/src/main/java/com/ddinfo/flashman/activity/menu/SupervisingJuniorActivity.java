package com.ddinfo.flashman.activity.menu;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.blankj.utilcode.utils.NetworkUtils;
import com.blankj.utilcode.utils.SizeUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.activity.task.FrozenDetailActivity;
import com.ddinfo.flashman.adapter.SupervinsingJuniorAdapter;
import com.ddinfo.flashman.adapter.WaitForDeliveryAdapter;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.ChildManEntity;
import com.ddinfo.flashman.models.WalletEntity;
import com.ddinfo.flashman.models.params.CreditManParams;
import com.ddinfo.flashman.network.SimpleCallBack;
import com.ddinfo.flashman.utils.QRBitMapUtil;
import com.ddinfo.flashman.view.RefreshLayout.MaterialRefreshLayout;
import com.ddinfo.flashman.view.RefreshLayout.MaterialRefreshListener;
import com.google.zxing.WriterException;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import retrofit2.Call;
import retrofit2.Response;

/**
 * 管理下级配送员
 * 2017年08月08日13:46:26 新建 by fuh
 */
public class SupervisingJuniorActivity extends BaseActivity {

    @Bind(R.id.recycle_view)
    RecyclerView recycleView;
    @Bind(R.id.swipe_search_list)
    MaterialRefreshLayout swipeSearchList;
    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;
    @Bind(R.id.rel_setting)
    RelativeLayout relSetting;

    private WalletEntity data;
    private boolean isLoadMore = false;
    private LinearLayoutManager layoutManager;
    private SupervinsingJuniorAdapter mAdapter; //下级配送员适配器

    private List<ChildManEntity> mListData = new ArrayList<>();
    private PopupWindow popupWindow; //任意视图的弹窗

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        setTitle("管理下级配送员");
        initRecycle();

    }

    private ImageView imageView;

    private void initQrPopwindow(String urlCode) { //初始化我的二维码弹框
        LinearLayout layout = new LinearLayout(context);
        layout.setBackgroundColor(ContextCompat.getColor(context,R.color.white));
        layout.setLayoutParams(new RecyclerView.LayoutParams(SizeUtils.dp2px(300), SizeUtils.dp2px(300)));
        imageView = new ImageView(context);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        layout.addView(imageView);
        popupWindow = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                getWindow().getDecorView().setAlpha(1f);
            }
        });
    }

    private void initData() {
        getWalletInfo();
    }

    /**
     * 查询钱包余额 押金 信息
     */
    private void getWalletInfo() {
        if (NetworkUtils.isConnected()) {
            proDialogHelps.showProDialog("正在获取个人信息");
            Call<BaseResponseEntity<WalletEntity>> call = webService.myWalletInfo(ExampleConfig.token);
            call.enqueue(new SimpleCallBack<BaseResponseEntity<WalletEntity>>(this) {
                @Override
                public void onSuccess(Call<BaseResponseEntity<WalletEntity>> call, Response<BaseResponseEntity<WalletEntity>> response) {

                    data = response.body().getData();
                    mAdapter.setTopData(data);
                    initQrPopwindow(data.getDeliveryManId()+"");
                    proDialogHelps.showProDialog("正在获取下级配送员列表...");
                    webService.getChildManList().enqueue(new SimpleCallBack<BaseResponseEntity<ArrayList<ChildManEntity>>>(context) {
                        @Override
                        public void onSuccess(Call<BaseResponseEntity<ArrayList<ChildManEntity>>> call, Response<BaseResponseEntity<ArrayList<ChildManEntity>>> response) {
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
                    });
                }

                @Override
                public void onProDialogDismiss() {
                    if (swipeSearchList.isRefreshing()) {
                        swipeSearchList.setRefreshing(false);
                    }
                    proDialogHelps.removeProDialog();
                }
            });
        } else {
            ToastUtils.showShortToastSafe("网络不可用");
        }
    }

    private void initListener() {
        mAdapter.setOnJuniorMenuClickListener(new SupervinsingJuniorAdapter.OnJuniorMenuClickListener() {
            @Override
            public void onQRcodeClick(View view, String urlCode) { //点击我的二维码回调
                try {
                    Bitmap www = QRBitMapUtil.createQRCode(urlCode, SizeUtils.dp2px(300));
                    imageView.setImageBitmap(www);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
                getWindow().getDecorView().setAlpha(0.5f);
            }

            @Override
            public void onFrozenClick(View view, int position) { //点击冻结明细回调
                Bundle bundle = new Bundle();
                bundle.putInt("deliveryId",mListData.get(position).getDeliveryManId());
                startActivity(FrozenDetailActivity.class,bundle);
            }

            @Override
            public void onLeftMenuClick(View view, int position) { //点击授信监听回调
                showLeftMenuDialog(position);
            }

            @Override
            public void onRightMenuClick(View view, int position) { //点击配送完成情况回调
                Bundle bundle = new Bundle();
                bundle.putInt("deliveryId",mListData.get(position).getDeliveryManId());
                startActivity(BoardActivity.class,bundle);

            }
        });



        swipeSearchList.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                initData();
            }
        });

    }

    private void showLeftMenuDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("授权额度");
        LinearLayout layout = new LinearLayout(context);
        layout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        final EditText editText = new EditText(context);
        editText.setHint("修改为："); //输入提示
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.leftMargin = SizeUtils.dp2px(20);
        layoutParams.rightMargin = SizeUtils.dp2px(20);
        layout.addView(editText,layoutParams);
        builder.setView(layout);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(!TextUtils.isEmpty(editText.getText().toString())){
                    proDialogHelps.showProDialog();
                    webService.creditMan(new CreditManParams(editText.getText().toString(),mListData.get(position).getDeliveryManId())).enqueue(new SimpleCallBack<BaseResponseEntity>(context) {
                        @Override
                        public void onSuccess(Call<BaseResponseEntity> call, Response<BaseResponseEntity> response) {
                            ToastUtils.showShortToast("授信成功");
                            initData();
                        }

                        @Override
                        public void onProDialogDismiss() {
                            proDialogHelps.removeProDialog();
                        }
                    });
                }else{
                    ToastUtils.showShortToast("请输入正确的数值");
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void initRecycle() {
        layoutManager = new LinearLayoutManager(context);
        mAdapter = new SupervinsingJuniorAdapter(context);
        recycleView.setLayoutManager(layoutManager);
        recycleView.setAdapter(mAdapter);
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.layout_recycleview;
    }
}
