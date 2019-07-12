package com.ddinfo.flashman.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.application.MyApplication;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.impl.OnItemClickListenerRv;
import com.ddinfo.flashman.models.SeckillOrderList;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 配送中Fragment适配器
 * Created by 李占晓 on 2017/5/24.
 */
public class AllocationTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  //监听: 李占晓 数据详情
  private OnItemClickListenerRv onItemClick;
  //监听: 李占晓 地图跳转
  private onMapBtnClickListener onMapClick;
  //监听: 李占晓 送达与拒收
  private OnTaskOptionClickListener onTaskClick;
  private OnStoreImgClickListener onimgClick;

  private boolean isEmpty = false;
  private boolean isLoadAll = false;

  private List<SeckillOrderList> mListData = new ArrayList<>();

  public void resetEmptyAndLoadAll() {
    isLoadAll = false;
    isEmpty = false;
    notifyDataSetChanged();
  }

  public void setEmpty(boolean empty) {
    isEmpty = empty;
    //notifyDataSetChanged();
  }

  public void setLoadAll(boolean loadAll) {
    isLoadAll = loadAll;
    //notifyDataSetChanged();
  }

  public void setmListData(List<SeckillOrderList> mListData) {
    this.mListData = mListData;
    notifyDataSetChanged();
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    View view = null;
    RecyclerView.ViewHolder holder = null;
    switch (viewType) {
      case ExampleConfig.VIEWHOLDER_NORMAL:
        view =
            LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_all_task_map, null);
        holder = new ViewHolder(view);
        break;
      case ExampleConfig.VIEWHOLDER_FOOT:
        View viewFooter = LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.item_footer_load_more, null);
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
      ViewHolder viewHolder = (ViewHolder) holder;
      viewHolder.itemView.setTag(position);

      viewHolder.llTop.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          onItemClick.onItemClick(view, position);
        }
      });

      viewHolder.tvMapDingwei.setText(mListData.get(position).getDistance());

      viewHolder.tvAmountIncome.setText(mListData.get(position).getCommission());
      viewHolder.tvAmountFrozen.setText(
          String.format("冻结：%s元", mListData.get(position).getOrderAmount()));
      Glide.with(MyApplication.context)
          .load(mListData.get(position).getStoreImg())
          .placeholder(R.mipmap.icon_store_default_img)
          .error(R.mipmap.icon_store_default_img)
          .into(viewHolder.imgMapShoppre);
      viewHolder.imgMapShoppre.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          onimgClick.onclick(view, position);
        }
      });
      viewHolder.tvWarehouseAddress.setText(mListData.get(position).getWarehouseAddress());
      viewHolder.tvStoreAddress.setText(mListData.get(position).getStoreAddress());
      viewHolder.tvStoreName.setText(mListData.get(position).getStoreName());
      viewHolder.tvTimeTake.setText(mListData.get(position).getPickupTime());

      System.out.println("mlistdata " + mListData.get(position).toString());
      viewHolder.tvMapDitu.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          onMapClick.onMapBtnClick(view, position);
        }
      });
      viewHolder.llTaskBottom.setVisibility(View.VISIBLE);

      viewHolder.tvLeft.setText("送达");
      viewHolder.tvNewLeft.setText("部分送达");
      viewHolder.tvRight.setText("拒收");

      if (!TextUtils.isEmpty(mListData.get(position).getInvoiceNumberId())) {
        viewHolder.tvStoreInvoiceId.setVisibility(View.VISIBLE);
        viewHolder.tvStoreInvoiceId.setText(mListData.get(position).getInvoiceNumberId());
      } else {
        viewHolder.tvStoreInvoiceId.setVisibility(View.GONE);
      }

      viewHolder.tvLeft.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          onTaskClick.onTaskOptionLeftClick(v, position);
        }
      });

      viewHolder.tvNewLeft.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          onTaskClick.onTaskOptionNewLeftClick(v, position);
        }
      });
      viewHolder.tvRight.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          onTaskClick.onTaskOptionRightClick(v, position);
        }
      });
    }
  }

  @Override
  public int getItemCount() {
    return mListData.size() == 0 ? 1 : mListData.size() + 1;
  }

  @Override
  public int getItemViewType(int position) {
    return position == mListData.size() ? ExampleConfig.VIEWHOLDER_FOOT
        : ExampleConfig.VIEWHOLDER_NORMAL;
  }

  public void setOnStoreImgClick(OnStoreImgClickListener onimgClick) {
    this.onimgClick = onimgClick;
  }

  //地图按钮点击事件
  public interface onMapBtnClickListener {
    void onMapBtnClick(View view, int position);
  }

  public interface OnTaskOptionClickListener {
    void onTaskOptionLeftClick(View v, int position);

    void onTaskOptionRightClick(View v, int position);

    void onTaskOptionNewLeftClick(View v, int position);
  }

  public interface OnStoreImgClickListener {
    void onclick(View view, int position);
  }

  public void setOnTaskOptionClickListener(OnTaskOptionClickListener onTaskClick) {
    this.onTaskClick = onTaskClick;
  }

  public void setOnItemClick(OnItemClickListenerRv onItemClick) {
    this.onItemClick = onItemClick;
  }

  public void setOnMapBtnClickListener(onMapBtnClickListener onclick) {
    this.onMapClick = onclick;
  }

  class ViewHolderFoot extends RecyclerView.ViewHolder {
    @Bind(R.id.tv_load_more) TextView tvLoadMore;
    @Bind(R.id.rl_load_more) RelativeLayout rlLoadMore;

    public ViewHolderFoot(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.text_amount_income) TextView textAmountIncome;
    @Bind(R.id.tv_amount_frozen) TextView tvAmountFrozen;
    @Bind(R.id.tv_amount_income) TextView tvAmountIncome;
    @Bind(R.id.tv_map_ditu) TextView tvMapDitu;
    @Bind(R.id.tv_map_dingwei) TextView tvMapDingwei;
    @Bind(R.id.img_map_shoppre) ImageView imgMapShoppre;
    @Bind(R.id.tv_warehouseAddress) TextView tvWarehouseAddress;
    @Bind(R.id.text_time_take) TextView textTimeTake;
    @Bind(R.id.tv_time_take) TextView tvTimeTake;
    @Bind(R.id.tv_store_Name) TextView tvStoreName;
    @Bind(R.id.tv_store_invoiceId) TextView tvStoreInvoiceId;
    @Bind(R.id.tv_store_Address) TextView tvStoreAddress;
    @Bind(R.id.view_line) View viewLine;
    @Bind(R.id.ll_top) LinearLayout llTop;
    @Bind(R.id.tv_left) TextView tvLeft;
    @Bind(R.id.tv_new_left) TextView tvNewLeft;
    @Bind(R.id.tv_right) TextView tvRight;
    @Bind(R.id.ll_task_bottom) LinearLayout llTaskBottom;

    ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
