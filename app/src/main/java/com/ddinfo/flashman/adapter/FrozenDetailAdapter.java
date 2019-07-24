package com.ddinfo.flashman.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddinfo.flashman.R;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.models.FrozenDetailsEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Gavin on 2017/8/11.
 * 暂不显示头部数据 后台没有返回
 * 冻结明细适配器
 */

public class FrozenDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private Context context;
  private List<FrozenDetailsEntity> mListData = new ArrayList<>();
  private boolean isEmpty = false; //是否为空
  private boolean isLoadAll = false; //是否加载全部

  public FrozenDetailAdapter(Context context) {
    this.context = context;
  }

  public void setListData(List<FrozenDetailsEntity> mListData) {
    this.mListData = mListData;
    notifyDataSetChanged();
  }

  public void setEmpty(boolean empty) {
    isEmpty = empty;
  }

  public void setLoadAll(boolean loadAll) {
    isLoadAll = loadAll;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = null;
    RecyclerView.ViewHolder holder = null;
    switch (viewType) {
      case ExampleConfig.VIEWHOLDER_TOP:
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_frozen_top, null);
        holder = new ViewHolderTop(view);
        holder.itemView.setVisibility(View.GONE);
        break;
      case ExampleConfig.VIEWHOLDER_NORMAL:
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_frozen_normal, null); //加载冻结明细布局
        holder = new ViewHolderNormal(view);
        break;
      case ExampleConfig.VIEWHOLDER_FOOT:
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer_load_more, null); //正在加载布局
        holder = new ViewHolderFoot(view);
        break;
    }
    return holder;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    int viewType = getViewType(position);
    switch (viewType) {
      case ExampleConfig.VIEWHOLDER_TOP:
        ViewHolderTop viewHolderTop = (ViewHolderTop) holder;

        break;
      case ExampleConfig.VIEWHOLDER_NORMAL:
        ViewHolderNormal viewHolderNormal = (ViewHolderNormal) holder;
        viewHolderNormal.tvOrderId.setText(mListData.get(position).getNumberId()); //订单ID
        viewHolderNormal.tvOrderPrice.setText(mListData.get(position).getOrderAmount());   //冻结金额
        viewHolderNormal.tvOrderPriceTop.setText("");
        viewHolderNormal.tvOrderState.setText(mListData.get(position).getState());         //冻结状态
        viewHolderNormal.tvOrderStateTop.setText(mListData.get(position).getState());
        viewHolderNormal.tvOrderStateTwo.setText(mListData.get(position).getFinishState());//订单完成状态
        viewHolderNormal.tvOrderTime.setText(mListData.get(position).getReceiveTime());    //接收时间

        break;
      case ExampleConfig.VIEWHOLDER_FOOT:
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
        break;
    }
  }

  @Override
  public int getItemCount() {
    if (mListData.size() == 0) {
      return 1;
    } else {
      return mListData.size() + 1;
    }
  }

  @Override
  public int getItemViewType(int position) {
    return getViewType(position);
  }

  private int getViewType(int position) {
    if (mListData.size() == 0) {
      //if(position == 0){
      //    return ExampleConfig.VIEWHOLDER_TOP;
      //}else{
      //    return ExampleConfig.VIEWHOLDER_FOOT;
      //}
      return ExampleConfig.VIEWHOLDER_FOOT;
    } else {
      //if (position == 0) {
      //    return ExampleConfig.VIEWHOLDER_TOP;
      //} else
      if (position == mListData.size()) {
        return ExampleConfig.VIEWHOLDER_FOOT;
      } else {
        return ExampleConfig.VIEWHOLDER_NORMAL;
      }
    }
  }

  static class ViewHolderTop extends RecyclerView.ViewHolder {

    @Bind(R.id.tv_junior_name) TextView tvJuniorName;
    @Bind(R.id.text_frozen_num) TextView textFrozenNum;
    @Bind(R.id.tv_frozen_num) TextView tvFrozenNum;

    public ViewHolderTop(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  static class ViewHolderNormal extends RecyclerView.ViewHolder { //冻结明细布局

    @Bind(R.id.tv_order_state_top) TextView tvOrderStateTop; //冻结状态
    @Bind(R.id.tv_order_price_top) TextView tvOrderPriceTop;
    @Bind(R.id.tv_order_id) TextView tvOrderId;       //订单ID
    @Bind(R.id.tv_order_state) TextView tvOrderState; //状态
    @Bind(R.id.tv_order_state_two) TextView tvOrderStateTwo; //订单完成状态
    @Bind(R.id.tv_order_price) TextView tvOrderPrice; //冻结金额
    @Bind(R.id.ll_center) LinearLayout llCenter;
    @Bind(R.id.tv_order_time) TextView tvOrderTime;   //时间

    public ViewHolderNormal(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  static class ViewHolderFoot extends RecyclerView.ViewHolder { //正在加载布局

    @Bind(R.id.tv_load_more) TextView tvLoadMore;
    @Bind(R.id.rl_load_more) RelativeLayout rlLoadMore;

    public ViewHolderFoot(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
