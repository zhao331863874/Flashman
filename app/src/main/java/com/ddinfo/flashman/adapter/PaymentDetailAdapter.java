package com.ddinfo.flashman.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ddinfo.flashman.R;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.models.PaymentDetailEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fuh on 2017/5/11.
 * Email：unableApe@gmail.com
 * 交货款单详情界面适配器
 */

public class PaymentDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private int index;
    private PaymentDetailEntity mData; //交货款单详情实体类
    private List<PaymentDetailEntity.OrderDetailBean> mListData = new ArrayList<>();

    public PaymentDetailAdapter(Context context) {
        this.context = context;
    }

    public void setData(PaymentDetailEntity mData, int index) {
        this.mData = mData;
        this.index = index;
        mListData = mData.getOrderDetail();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { //创建ViewHolder，如果是header或者footer直接返回响应holder即可
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case ExampleConfig.VIEWHOLDER_TOP:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_detail_top, parent, false); //交货款单详情布局抬头
                holder = new ViewHolderTop(view);
                break;
            case ExampleConfig.VIEWHOLDER_NORMAL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_detail_normal,null); //交货款单详情布局
                holder = new ViewHolderNormal(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) { //填充视图
        if (mData != null) {
            if (position == 0) {
                ViewHolderTop viewHolderTop = (ViewHolderTop) holder;
                switch (index) {
                    case 0:
                        viewHolderTop.tvErrorDate.setVisibility(View.GONE);
                        break;
                    case 1:
                        viewHolderTop.tvErrorDate.setVisibility(View.VISIBLE);
                        viewHolderTop.tvErrorDate.setText("支付失败时间:" + mData.getInfo().getModifyTime());
                        break;
                    case 2:
                        viewHolderTop.tvErrorDate.setVisibility(View.VISIBLE);
                        viewHolderTop.tvErrorDate.setText("支付异常时间:" + mData.getInfo().getModifyTime());
                        break;
                    case 3:
                        viewHolderTop.tvErrorDate.setVisibility(View.VISIBLE);
                        viewHolderTop.tvErrorDate.setText("支付时间:" + mData.getInfo().getModifyTime());
                        break;
                    case 99:
                        viewHolderTop.tvErrorDate.setVisibility(View.GONE); //隐藏支付失败时间
                        break;
                }
                viewHolderTop.tvOrderStatus.setText(mData.getInfo().getState()); //交货款状态
                viewHolderTop.tvOrderId.setText("交货款单号:" + mData.getInfo().getBatchId());
                viewHolderTop.tvDate.setText(mData.getInfo().getTime()); //创建时间
            } else {
                int relPosition = position - 1;
                ViewHolderNormal viewHolderNormal = (ViewHolderNormal) holder;
                viewHolderNormal.tvId.setText(mListData.get(relPosition).getNumberId() + "");
                viewHolderNormal.tvFlashId.setText(mListData.get(relPosition).getInvoiceNumberId() + "");
                viewHolderNormal.tvMoney.setText("￥" + mListData.get(relPosition).getOrderAmount());
            }
        }
    }

    @Override
    public int getItemCount() { //返回item个数，由于footerview的存在，所以item个数要+1
        if(mData == null){
            return 0;
        }else{
            return mListData.size() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) { //获取当前项Item(position参数)是哪种类型的布局
        if (position == 0) { //头布局类型
            return ExampleConfig.VIEWHOLDER_TOP;
        } else { //默认布局类型
            return ExampleConfig.VIEWHOLDER_NORMAL;
        }
    }

    static class ViewHolderTop extends RecyclerView.ViewHolder { //交货款单详情布局
        @Bind(R.id.tv_order_status) //交货款状态
        TextView tvOrderStatus;
        @Bind(R.id.tv_order_id)     //交货款单号
        TextView tvOrderId;
        @Bind(R.id.tv_date)         //创建时间
        TextView tvDate;
        @Bind(R.id.tv_error_date)   //支付失败时间
        TextView tvErrorDate;

        public ViewHolderTop(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ViewHolderNormal extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_id)
        TextView tvId;
        @Bind(R.id.tv_flash_id)
        TextView tvFlashId;
        @Bind(R.id.tv_money)
        TextView tvMoney;

        public ViewHolderNormal(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
