package com.ddinfo.flashman.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddinfo.flashman.R;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.impl.OnItemClickListenerRv;
import com.ddinfo.flashman.models.RouteOrderEntity;
import com.ddinfo.flashman.models.SeckillOrderList;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Gavin on 2017/8/8.
 * 待配送单适配器
 */

public class WaitForDeliveryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context mContext;
    private boolean isEmpty = false;
    private boolean isLoadAll = false;

    private List<RouteOrderEntity> mListData = new ArrayList<>();
    private OnItemClickListenerRv itemClickListener;

    public WaitForDeliveryAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setIsEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public void setIsLoadAll(boolean isLoadAll) {
        this.isLoadAll = isLoadAll;
    }

    public void setmListData(List<RouteOrderEntity> mListData) {
        this.mListData = mListData;
        notifyDataSetChanged();
    }

    public void setItemClickListener(OnItemClickListenerRv itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case ExampleConfig.VIEWHOLDER_NORMAL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wait_delivery, null); //待配送单布局
                holder = new ViewHolderNormal(view);
                break;
            case ExampleConfig.VIEWHOLDER_FOOT:
                View viewFooter = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer_load_more, null); //正在加载布局
                holder = new ViewHolderFoot(viewFooter);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position == mListData.size()) {
            ViewHolderFoot viewHolderFoot = (ViewHolderFoot) holder;
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
            ViewHolderNormal viewHolderNormal = (ViewHolderNormal) holder;
            viewHolderNormal.itemView.setTag(position);
            viewHolderNormal.tvAmountIncome.setText(mListData.get(position).getCommission()+""); //收入金额
            viewHolderNormal.tvAmountFrozen.setText("金额：" + mListData.get(position).getOrderAmount() + "元"); //冻结金额
            viewHolderNormal.tvAddressSend.setText(mListData.get(position).getStoreAddress());   //收货地址
            viewHolderNormal.tvAddressTake.setText(mListData.get(position).getWarehouseName());  //取货地址
            viewHolderNormal.tvOrderTime.setText(mListData.get(position).getOrderTime());        //下单时间
            viewHolderNormal.tvLineName.setText(mListData.get(position).getRouteName());         //线路名称
            viewHolderNormal.tvStoreName.setText(mListData.get(position).getStoreName());        //店铺名称
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

    class ViewHolderNormal extends RecyclerView.ViewHolder { //待配送单布局
        @Bind(R.id.text_amount_income) //收入
        TextView textAmountIncome;
        @Bind(R.id.tv_amount_frozen)   //冻结金额
        TextView tvAmountFrozen;
        @Bind(R.id.tv_amount_income)   //收入金额
        TextView tvAmountIncome;
        @Bind(R.id.tv_distance_take)   //取货距离
        TextView tvDistanceTake;
        @Bind(R.id.tv_distance_send)   //送货距离
        TextView tvDistanceSend;
        @Bind(R.id.tv_order_time)      //下单时间
        TextView tvOrderTime;
        @Bind(R.id.tv_address_take)    //取货地址
        TextView tvAddressTake;
        @Bind(R.id.text_time_take)     //取货时间名称
        TextView textTimeTake;
        @Bind(R.id.tv_time_take)       //取货时间
        TextView tvTimeTake;
        @Bind(R.id.tv_store_name)      //店铺名称
        TextView tvStoreName;
        @Bind(R.id.tv_line_name)       //线路
        TextView tvLineName;
        @Bind(R.id.tv_address_send)    //收货地址
        TextView tvAddressSend;
        @Bind(R.id.ll_top)
        LinearLayout llTop;


        public ViewHolderNormal(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ViewHolderFoot extends RecyclerView.ViewHolder { //正在加载布局
        @Bind(R.id.tv_load_more)
        TextView tvLoadMore;
        @Bind(R.id.rl_load_more)
        RelativeLayout rlLoadMore;

        public ViewHolderFoot(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
