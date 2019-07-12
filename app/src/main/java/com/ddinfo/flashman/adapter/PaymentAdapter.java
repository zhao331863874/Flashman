package com.ddinfo.flashman.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddinfo.flashman.R;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.impl.OnItemClickListenerRv;
import com.ddinfo.flashman.models.PaymentEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Title: 提现
 * Created by fuh on 2017/1/7.
 * Email：unableApe@gmail.com
 */

public class PaymentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private boolean isEmpty = false;
    private boolean isLoadAll = false;

    private OnItemClickListenerRv onItemClickListenerRv;
    private OnPaySelClickListener onPaySelClickListener;

    public void setOnPaySelClickListener(OnPaySelClickListener onPaySelClickListener) {
        this.onPaySelClickListener = onPaySelClickListener;
    }

    public PaymentAdapter(Context context) {
        this.context = context;
    }

    public void setmListData(List<PaymentEntity> mListData) {
        this.mListData = mListData;
        notifyDataSetChanged();
    }

    public void setIsEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public void setIsLoadAll(boolean isLoadAll) {
        this.isLoadAll = isLoadAll;
    }

    private List<PaymentEntity> mListData = new ArrayList<>();

    public void setItemClickListener(OnItemClickListenerRv itemClickListener) {
        this.onItemClickListenerRv = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case ExampleConfig.VIEWHOLDER_NORMAL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment, null);
                holder = new ViewHolderNormal(view);
                break;
            case ExampleConfig.VIEWHOLDER_FOOT:
                View viewFooter = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer_load_more, null);
                holder = new ViewHolderFoot(viewFooter);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position == mListData.size()) {
            PaymentAdapter.ViewHolderFoot viewHolderFoot = (PaymentAdapter.ViewHolderFoot) holder;
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
            PaymentAdapter.ViewHolderNormal viewHolderNormal = (PaymentAdapter.ViewHolderNormal) holder;
            viewHolderNormal.itemView.setTag(position);
            viewHolderNormal.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListenerRv != null) {
                        onItemClickListenerRv.onItemClick(v, position);
                    }
                }
            });

//            viewHolderNormal.ivPayState.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    onPaySelClickListener.onPalSelClick(v, position);
//                }
//            });

            viewHolderNormal.tvPayId.setText(mListData.get(position).getNumberId());
            viewHolderNormal.tvPayAmount.setText("￥" + mListData.get(position).getOrderAmount());
            if (!mListData.get(position).getChecked()) {
                viewHolderNormal.ivPayState.setImageResource(R.mipmap.content_button_normal);
            } else {
                viewHolderNormal.ivPayState.setImageResource(R.mipmap.content_button_pressed);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mListData.size() == 0 ? 1 : mListData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mListData.size()) {
            return ExampleConfig.VIEWHOLDER_FOOT;
        } else {
            return ExampleConfig.VIEWHOLDER_NORMAL;
        }
    }

    class ViewHolderNormal extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_pay_id)
        TextView tvPayId;
        @Bind(R.id.tv_pay_one)
        TextView tvPayOne;
        @Bind(R.id.tv_pay_two)
        TextView tvPayTwo;
        @Bind(R.id.tv_pay_amount)
        TextView tvPayAmount;
        @Bind(R.id.iv_pay_state)
        ImageView ivPayState;

        public ViewHolderNormal(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ViewHolderFoot extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_load_more)
        TextView tvLoadMore;
        @Bind(R.id.rl_load_more)
        RelativeLayout rlLoadMore;

        public ViewHolderFoot(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnPaySelClickListener {
        void onPalSelClick(View v, int position);
    }
}
