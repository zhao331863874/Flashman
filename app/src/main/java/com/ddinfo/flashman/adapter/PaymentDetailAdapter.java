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
 */

public class PaymentDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private int index;
    private PaymentDetailEntity mData;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case ExampleConfig.VIEWHOLDER_TOP:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_detail_top, parent, false);
                holder = new ViewHolderTop(view);
                break;
            case ExampleConfig.VIEWHOLDER_NORMAL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_detail_normal,null);
                holder = new ViewHolderNormal(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
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
                        viewHolderTop.tvErrorDate.setVisibility(View.GONE);
                        break;
                }
                viewHolderTop.tvOrderStatus.setText(mData.getInfo().getState());
                viewHolderTop.tvOrderId.setText("交货款单号:" + mData.getInfo().getBatchId());
                viewHolderTop.tvDate.setText(mData.getInfo().getTime());
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
    public int getItemCount() {
        if(mData == null){
            return 0;
        }else{
            return mListData.size() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ExampleConfig.VIEWHOLDER_TOP;
        } else {
            return ExampleConfig.VIEWHOLDER_NORMAL;
        }
    }

    static class ViewHolderTop extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_order_status)
        TextView tvOrderStatus;
        @Bind(R.id.tv_order_id)
        TextView tvOrderId;
        @Bind(R.id.tv_date)
        TextView tvDate;
        @Bind(R.id.tv_error_date)
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
