package com.ddinfo.flashman.activity.task;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.adapter.OrderPartSendAdapter;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.PartSendConfirmEntity;
import com.ddinfo.flashman.models.PartSendListEntity;
import com.ddinfo.flashman.models.params.PartSendDoneParams;
import com.ddinfo.flashman.network.SimpleCallBack;
import com.ddinfo.flashman.view.RecycleViewItemDecoration.ListItemDecoration;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 部分送达界面
 */
public class TaskPartSendActivity extends BaseActivity {
    @Bind(R.id.tv_order_id) //发货单号
    TextView tvOrderId;
    @Bind(R.id.tv_order_price) //货品价格
    TextView tvOrderPrice;
    @Bind(R.id.rv_order_part_send)
    RecyclerView rvOrderPartSend;
    @Bind(R.id.tv_next)     //下一步
    TextView tvNext;

    private PartSendListEntity partSendListEntity;
    private OrderPartSendAdapter mAdapter;
    private PartSendConfirmEntity partSendConfirmEntity;
    private PartSendDoneParams confirmParams;
    private String deliveryOrderId ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initDatas();
        initListener();
    }

    public void initViews() {
        setTitle("请选择拒收商品");
        rvOrderPartSend.setLayoutManager(new LinearLayoutManager(this)); //设置布局管理器
        rvOrderPartSend.addItemDecoration(new ListItemDecoration(this, ListItemDecoration.VERTICAL_LIST)); //设置分割线
        mAdapter = new OrderPartSendAdapter(this);
        rvOrderPartSend.setAdapter(mAdapter);
    }
    public void initDatas() {
        try {
            deliveryOrderId = getIntent().getExtras().getString("deliveryOrderId");
        }catch (NullPointerException e){
            deliveryOrderId = "";
        }
        PartSendDoneParams params = null;
        if(!TextUtils.isEmpty(deliveryOrderId)){
            params = new PartSendDoneParams();
            params.setDeliveryOrderId(deliveryOrderId);
        }else{
            ToastUtils.showShortToast("不好意思崩溃了！没错就是Android的问题，请联系客服");
            params = new PartSendDoneParams();
        }
        proDialogHelps.showProDialog();
        Call<BaseResponseEntity<PartSendListEntity>> callPartList = webService.getPartSendList(params);
        callPartList.enqueue(callbackPartSendList);
    }

    /**
     * @param goodsAmount 商品数量
     * @param position 商品索引
     */
    private void showNumEditDialog(final int goodsAmount, final int position){
        final View view = LayoutInflater.from(this).inflate(R.layout.dialog_goods_num_edit, null); //加载商品数量输入布局
        final EditText etGoodsNum = (EditText) view.findViewById(R.id.et_goods_num);
        etGoodsNum.setHint("商品总数："+goodsAmount);
        etGoodsNum.setBackground(ContextCompat.getDrawable(context,R.drawable.edit_white));
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("请输入商品数量");
        builder.setView(view);
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Animation waggleAnim = AnimationUtils.loadAnimation(context,R.anim.left_right_waggle);

                if(TextUtils.isEmpty(etGoodsNum.getText().toString().trim())){
                    etGoodsNum.startAnimation(waggleAnim);
                    ToastUtils.showShortToast("请输入商品数量");
                    return;
                }else {
                    int changeGoodsAmount = Integer.parseInt(etGoodsNum.getText().toString().trim());
                    if (changeGoodsAmount > goodsAmount) {
                        etGoodsNum.startAnimation(waggleAnim);
                        ToastUtils.showShortToast("输入的数量应少于商品总数量（" + goodsAmount + "）");
                        return;
                    } else {
                        int refuseQuantity = goodsAmount - changeGoodsAmount;
                        partSendListEntity.getDetails().get(position).setRefuseQuantity(refuseQuantity);
                        mAdapter.notifyItemChanged(position); //更新数据
                    }
                }

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void initListener() {
        mAdapter.setOnSelNumClickListener(new OrderPartSendAdapter.OnSelNumClickListener() {
            @Override
            public void onSelNumAdd(View v, int position) { //点击增加按钮回调
                PartSendListEntity.DetailsBean detailsBean = partSendListEntity.getDetails().get(position);
                partSendListEntity.getDetails().get(position).setRefuseQuantity(detailsBean.getRefuseQuantity() + 1);
                mAdapter.notifyItemChanged(position); //更新数据
            }

            @Override
            public void onSelNumDel(View v, int position) { //点击减少按钮回调
                PartSendListEntity.DetailsBean detailsBean = partSendListEntity.getDetails().get(position);
                partSendListEntity.getDetails().get(position).setRefuseQuantity(detailsBean.getRefuseQuantity() - 1);
                mAdapter.notifyItemChanged(position); //更新数据
            }

            @Override
            public void onSelNum(View v, int position) { //点击商品数量回调
                showNumEditDialog(partSendListEntity.getDetails().get(position).getQuantity(),position);

            }
        });



        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //点击下一步回调
                confirmParams = new PartSendDoneParams();
                int allCount = 0;//商品所有的数量
                int allRefuseCount = 0;//拒收商品所有的数量
                for (int i = 0; i < partSendListEntity.getDetails().size(); i++) {
                    allCount += partSendListEntity.getDetails().get(i).getQuantity();
                    allRefuseCount += partSendListEntity.getDetails().get(i).getRefuseQuantity();
                }
                if(allRefuseCount == 0){
                    ToastUtils.showShortToast("请选择商品");
                    return;
                }
                if(allRefuseCount == allCount){
                    ToastUtils.showShortToast("商品不能全部拒收");
                    return;
                }
                Map<String,Integer> goodsMap = new HashMap<String, Integer>();
                for (int i = 0; i < partSendListEntity.getDetails().size(); i++) {
                    if(partSendListEntity.getDetails().get(i).getRefuseQuantity()>0){
                        goodsMap.put(partSendListEntity.getDetails().get(i).getId()+"",partSendListEntity.getDetails().get(i).getRefuseQuantity());

                    }
                }
                confirmParams.setDeliveryOrderId(partSendListEntity.getDeliveryOrderId());
                confirmParams.setDetails(new Gson().toJson(goodsMap));
                webService.getPartSendConfirmList(confirmParams).enqueue(callBackPartSendConfirm);
            }
        });
    }

    //获取商品列表
    Callback<BaseResponseEntity<PartSendListEntity>> callbackPartSendList = new SimpleCallBack<BaseResponseEntity<PartSendListEntity>>(context) {
        @Override
        public void onSuccess(Call<BaseResponseEntity<PartSendListEntity>> call, Response<BaseResponseEntity<PartSendListEntity>> response) {
            partSendListEntity = response.body().getData();
            for (int i = 0; i < partSendListEntity.getDetails().size(); i++) {
                partSendListEntity.getDetails().get(i).setRefuseQuantity(0);
            }
            tvOrderId.setText(" 订单号:"+partSendListEntity.getId());

            mAdapter.setPartSendListEntity(partSendListEntity);
        }

        @Override
        public void onProDialogDismiss() {
            proDialogHelps.removeProDialog();
        }
    };

    //提交拒收信息并获取确认列表
    Callback<BaseResponseEntity<PartSendConfirmEntity>> callBackPartSendConfirm = new SimpleCallBack<BaseResponseEntity<PartSendConfirmEntity>>(context) {
        @Override
        public void onSuccess(Call<BaseResponseEntity<PartSendConfirmEntity>> call, Response<BaseResponseEntity<PartSendConfirmEntity>> response) {
            partSendConfirmEntity = response.body().getData();
            Bundle bundle = new Bundle();
            bundle.putSerializable("confirmEntity",partSendConfirmEntity);
            bundle.putSerializable("confirmParams",confirmParams);
            startActivity(TaskPartSendConfirmActivity.class,bundle);
        }

        @Override
        public void onProDialogDismiss() {
            proDialogHelps.removeProDialog();
        }
    };

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_task_part_send;
    }

}
