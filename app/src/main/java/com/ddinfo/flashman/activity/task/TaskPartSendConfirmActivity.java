package com.ddinfo.flashman.activity.task;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.models.PartSendConfirmEntity;
import com.ddinfo.flashman.models.params.PartSendDoneParams;

import java.util.List;

import butterknife.Bind;

public class TaskPartSendConfirmActivity extends BaseActivity {

    @Bind(R.id.tv_send_goods_id)
    TextView tvSendGoodsId;
    @Bind(R.id.tv_origin_count)
    TextView tvOriginCount;
    @Bind(R.id.tv_origin_sum)
    TextView tvOriginSum;
    @Bind(R.id.tv_origin_dadou)
    TextView tvOriginDadou;
    @Bind(R.id.tv_origin_other_sale)
    TextView tvOriginOtherSale;
    @Bind(R.id.tv_origin_all_sale)
    TextView tvOriginAllSale;
    @Bind(R.id.tv_origin_receivable)
    TextView tvOriginReceivable;
    @Bind(R.id.tv_origin_payed)
    TextView tvOriginPayed;
    @Bind(R.id.tv_now_payed)
    TextView tvNowPayed;
    @Bind(R.id.tv_now_count)
    TextView tvNowCount;
    @Bind(R.id.tv_now_sum)
    TextView tvNowSum;
    @Bind(R.id.tv_now_dadou)
    TextView tvNowDadou;
    @Bind(R.id.tv_now_other_sale)
    TextView tvNowOtherSale;
    @Bind(R.id.tv_now_all_sale)
    TextView tvNowAllSale;
    @Bind(R.id.tv_now_receivable)
    TextView tvNowReceivable;
    @Bind(R.id.tv_reject_goods_count)
    TextView tvRejectGoodsCount;
    @Bind(R.id.ll_reject_goods_group_out)
    LinearLayout llRejectGoodsGroupOut;
    @Bind(R.id.ll_delivery_goods_group_out)
    LinearLayout llDeliveryGoodsGroupOut;
    @Bind(R.id.tv_done)
    TextView tvDone;

    private PartSendConfirmEntity confirmEntity;
    private PartSendDoneParams confirmParams;

    private static final int GOODS_REJECT = 100;
    private static final int GOODS_ARRIVE = 200;

    private SpannableString msp = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initDatas();
        initListener();
    }

    protected void initViews() {
        setTitle("确认部分送达");
        confirmParams = (PartSendDoneParams) getIntent().getExtras().getSerializable("confirmParams");
        confirmEntity = (PartSendConfirmEntity) getIntent().getExtras().getSerializable("confirmEntity");

        tvSendGoodsId.setText("发货单号：" + confirmEntity.getInvoiceNumberId());
        tvOriginCount.setText("商品总数：" + confirmEntity.getOrgInfo().getGoodsAmount());
        tvOriginSum.setText("商品总金额：" + confirmEntity.getOrgInfo().getSum());
        tvOriginDadou.setText("商品达豆：" + confirmEntity.getOrgInfo().getDadouOff());
        tvOriginOtherSale.setText("其他优惠：" + confirmEntity.getOrgInfo().getOtherOff());
        tvOriginAllSale.setText("合计优惠：" + confirmEntity.getOrgInfo().getTotalOff());
        tvRejectGoodsCount.setText("商品总数：" + confirmEntity.getGoodsDetail().getRejectAmount());

        tvNowCount.setText("商品总数：" + confirmEntity.getNowInfo().getGoodsAmount());
        tvNowSum.setText("商品总金额：" + confirmEntity.getNowInfo().getSum());
        tvNowDadou.setText("商品达豆：" + confirmEntity.getNowInfo().getDadouOff());
        tvNowOtherSale.setText("其他优惠：" + confirmEntity.getNowInfo().getOtherOff());
        tvNowAllSale.setText("合计优惠：" + confirmEntity.getNowInfo().getTotalOff());

        if(confirmEntity.getOrgInfo().getHadPaySum() == 0){ //未支付订单
            tvOriginPayed.setVisibility(View.GONE);
            tvOriginReceivable.setVisibility(View.VISIBLE);
//            msp = new SpannableString("应收：" + confirmEntity.getOrgInfo().getNeedPaySum());
//            msp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context,R.color.text_color_red)), ("应收：").length(), ("应收：").length()+(confirmEntity.getNowInfo().getNeedPaySum()+"").length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvOriginReceivable.setText("应收：" + confirmEntity.getOrgInfo().getNeedPaySum());
        }else{ //已支付订单
            tvOriginPayed.setVisibility(View.VISIBLE);
            tvOriginReceivable.setVisibility(View.GONE);
            tvOriginPayed.setText("已在线支付："+confirmEntity.getOrgInfo().getHadPaySum());
        }

        if(confirmEntity.getNowInfo().getHadPaySum() == 0){ //未支付订单
            tvNowPayed.setVisibility(View.GONE);
            tvNowReceivable.setVisibility(View.VISIBLE);
//            msp = new SpannableString("应收：" + confirmEntity.getNowInfo().getNeedPaySum());
//            msp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context,R.color.text_color_red)), ("应收：").length(), ("应收：").length()+(confirmEntity.getNowInfo().getNeedPaySum()+"").length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvNowReceivable.setTextColor(ContextCompat.getColor(context,R.color.text_color_red));
            tvNowReceivable.setText("应收：" + confirmEntity.getNowInfo().getNeedPaySum());

        }else{ //已支付订单
            tvNowPayed.setVisibility(View.VISIBLE);
            tvNowPayed.setText("已在线支付："+confirmEntity.getNowInfo().getHadPaySum());
//            msp = new SpannableString("应退：" + confirmEntity.getNowInfo().getNeedRefund());
//            msp.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context,R.color.text_color_red)), ("应退：").length(), ("应退：").length()+(confirmEntity.getNowInfo().getNeedRefund()+"").length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvNowReceivable.setTextColor(ContextCompat.getColor(context,R.color.text_color_red));
            tvNowReceivable.setText("应退：" + confirmEntity.getNowInfo().getNeedRefund());

//            if(confirmEntity.getNowInfo().getNeedPaySum() != 0){
//                tvNowReceivable.setText("应收：" + confirmEntity.getNowInfo().getNeedPaySum());
//            }
//            if(confirmEntity.getNowInfo().getNeedRefund() != 0){
//                tvNowReceivable.setText("应退：" + confirmEntity.getNowInfo().getNeedRefund());
//            }


        }


        //动态添加拒收和送达的商品视图
        addGoodsView(confirmEntity.getGoodsDetail().getReject(), GOODS_REJECT);
        addGoodsView(confirmEntity.getGoodsDetail().getArrive(), GOODS_ARRIVE);
    }


    private void addGoodsView(List<PartSendConfirmEntity.GoodsDetailBean.RejectBean> rejectBeans, int status) {
        for (int i = 0; i < rejectBeans.size(); i++) { //商品组
            View goodsGroupView = LayoutInflater.from(context).inflate(R.layout.item_part_goods_in, null);
            LinearLayout llGoodsIn = (LinearLayout) goodsGroupView.findViewById(R.id.ll_goods_group_in);
            TextView tvIndex = (TextView) goodsGroupView.findViewById(R.id.tv_index);
            if(rejectBeans.get(i).getGiftFlag() == 1){
                tvIndex.setText("赠");
            }else{
                tvIndex.setText(i+1+"");
            }

            //当商品组内的商品种类为1，且数量为1时，为单品销售
            //单品销售时，item名称显示为商品简介 既OnSellGoodsCombs.getName
            if(rejectBeans.get(i).getOnSellGoodsCombs().size() == 1 && rejectBeans.get(i).getOnSellGoodsCombs().get(0).getAmount() == 1){
                addGoodsItem(llGoodsIn,rejectBeans.get(i).getName(),rejectBeans.get(i).getQuantity()+"");
            }else{
                //增加标题和商品列表
                addGoodsItem(llGoodsIn,rejectBeans.get(i).getName(),rejectBeans.get(i).getQuantity()+"");
                for (int j = 0; j < rejectBeans.get(i).getOnSellGoodsCombs().size(); j++) { //商品
                    addGoodsItem(llGoodsIn,rejectBeans.get(i).getOnSellGoodsCombs().get(j).getName(),rejectBeans.get(i).getOnSellGoodsCombs().get(j).getAmount()*rejectBeans.get(i).getQuantity()+"");
                }
            }
            switch (status) {
                case GOODS_REJECT:
                    llRejectGoodsGroupOut.addView(goodsGroupView, new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)));
                    break;
                case GOODS_ARRIVE:
                    llDeliveryGoodsGroupOut.addView(goodsGroupView, new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)));
                    break;
            }
        }
    }

    private void addGoodsItem(LinearLayout llGoodsIn,String name,String amount){
        LinearLayout horLinear = new LinearLayout(context, null);//商品信息横向容器
        horLinear.setOrientation(LinearLayout.HORIZONTAL);
        //商品名称
        TextView tvGoodsName = new TextView(context, null);
        LinearLayout.LayoutParams paramsGoodsName = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvGoodsName.setText(name);
        float density = context.getResources().getDisplayMetrics().density;
        tvGoodsName.setMaxWidth(270 * (int)density);
        tvGoodsName.setMaxLines(2);
        //商品数量
        TextView tvGoodsAmount = new TextView(context, null);
        LinearLayout.LayoutParams paramsGoodsAmount = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvGoodsAmount.setGravity(Gravity.RIGHT);
        tvGoodsAmount.setText(amount);

        horLinear.addView(tvGoodsName, paramsGoodsName);
        horLinear.addView(tvGoodsAmount, paramsGoodsAmount);
        llGoodsIn.addView(horLinear,
                new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)));

    }

    protected void initDatas() {

    }

    protected void initListener() {

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("confirmParams", confirmParams);
                bundle.putDouble("hadPaySum", confirmEntity.getOrgInfo().getHadPaySum());
                startActivity(TaskPartSendCauseActivity.class, bundle);
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_task_part_send_confirm;
    }
}
