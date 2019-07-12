package com.ddinfo.flashman.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddinfo.flashman.R;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.impl.OnItemClickListenerRv;
import com.ddinfo.flashman.impl.OnTaskGetListener;
import com.ddinfo.flashman.models.RouteEntity;
import com.ddinfo.flashman.models.SeckillOrderList;
import com.ddinfo.flashman.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Title: 主页适配器
 * Created by fuh on 2017/1/3.
 * Email：unableApe@gmail.com
 */

public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private boolean isEmpty = false;   //是否为空
    private boolean isLoadAll = false; //是否加载全部完成

    private List<RouteEntity> mListData = new ArrayList<>(); //路线数据信息
    private OnItemClickListenerRv onItemClickListener;
    private boolean isTokenLose;       //token是否过期

    public boolean isTokenLose() {
        return isTokenLose;
    }

    public void setTokenLose(boolean tokenLose) {
        isTokenLose = tokenLose;
    }

    public HomeAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setIsEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public void setIsLoadAll(boolean isLoadAll) {
        this.isLoadAll = isLoadAll;
    }
    //设置路线数据信息
    public void setmListData(List<RouteEntity> mListData) {
        this.mListData = mListData;
        notifyDataSetChanged(); //更新数据
    }

    public void setItemClickListener(OnItemClickListenerRv itemClickListener) {
        this.onItemClickListener = itemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { //负责承载每个子项的布局
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case ExampleConfig.VIEWHOLDER_NORMAL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_content, parent,false);
                holder = new ViewHolderNormal(view);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(onItemClickListener!=null){
                            onItemClickListener.onItemClick(v, (Integer) v.getTag());
                        }
                    }
                });
                break;
            case ExampleConfig.VIEWHOLDER_FOOT:
                View viewFooter = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer_load_more, null);
                holder = new ViewHolderFoot(viewFooter);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) { //负责将每个子项holder绑定数据
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
            if (isTokenLose) {
                viewHolderFoot.tvLoadMore.setText("登录后查看订单信息");
            }
        } else {
            ViewHolderNormal viewHolderNormal = (ViewHolderNormal) holder;
            viewHolderNormal.itemView.setTag(position);
            viewHolderNormal.tvRoundName.setText(mListData.get(position).getRouteName());  //设置区域名称
            viewHolderNormal.tvOrderNumber.setText(mListData.get(position).getOrderTotal()+"单"); //设置接单数量
            viewHolderNormal.tvVolume.setText(mListData.get(position).getCapacity()+"方"); //设置体积
            viewHolderNormal.tvIncome.setText("￥"+Utils.jointNumFromat(mListData.get(position).getSum())); //设置收入
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

        @Bind(R.id.tv_round_name)
        TextView tvRoundName;   //区域名称
        @Bind(R.id.tv_order_number)
        TextView tvOrderNumber; //接单数量
        @Bind(R.id.tv_volume)
        TextView tvVolume;      //体积
        @Bind(R.id.tv_income)
        TextView tvIncome;      //收入
        @Bind(R.id.tv_check)
        TextView tvCheck;       //查看明细

        public ViewHolderNormal(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    class ViewHolderFoot extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_load_more)
        TextView tvLoadMore;   //正在加载
        @Bind(R.id.rl_load_more)
        RelativeLayout rlLoadMore;

        public ViewHolderFoot(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
