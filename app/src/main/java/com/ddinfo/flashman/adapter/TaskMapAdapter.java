package com.ddinfo.flashman.adapter;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.application.MyApplication;
import com.ddinfo.flashman.impl.OnItemClickListenerRv;
import com.ddinfo.flashman.models.SeckillOrderList;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 李占晓 on 2017/5/25.
 */

public class TaskMapAdapter extends RecyclerView.Adapter<TaskMapAdapter.ViewHolder> {
  //监听: 李占晓 数据详情
  //private OnItemClickListenerRv onItemClick;
  private List<SeckillOrderList> mListData;
  //监听: 李占晓 送达与拒收
  private OnTaskOptionClickListener onTaskClick;
  private OnPhoneBtnClickListener onPhoneClick;
  private OnStoreImgClickListener onStoreImgClick;

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
    View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_task_map, null);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder viewHolder, final int i) {
    //viewHolder.llItemView.setOnClickListener(new View.OnClickListener() {
    //  @Override
    //  public void onClick(View view) {
    //    onItemClick.onItemClick(view, i);
    //  }
    //});
    viewHolder.tvStoreName.setText(mListData.get(i).getStoreName());
    //BUG: 禅道bugId=5659，解决日期:2017/6/6，解决人:李占晓，解释:地图里，订单详情，发货单号取错
    viewHolder.tvNumberid.setText(mListData.get(i).getInvoiceNumberId());
    viewHolder.tvMapDingwei.setText(mListData.get(i).getDistance());
    viewHolder.tvStoreLbname.setText(mListData.get(i).getStoreAcceptName());
    viewHolder.tvStorePhoneNum.setText(mListData.get(i).getStorePhone());
    Glide.with(MyApplication.context)
        .load(mListData.get(i).getStoreImg())

        .placeholder(R.mipmap.icon_store_default_img)
        .error(R.mipmap.icon_store_default_img)
        .into(viewHolder.imgMapShoppre);
    viewHolder.imgMapShoppre.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onStoreImgClick.onStoreImgClick(view, i);
      }
    });
    viewHolder.tvStoreAddress.setText(mListData.get(i).getStoreAddress());
    viewHolder.tvLeft.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onTaskClick.onTaskOptionLeftClick(view, i);
      }
    });
    viewHolder.tvRight.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onTaskClick.onTaskOptionRightClick(view, i);
      }
    });
    viewHolder.tvStorePhoneNum.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onPhoneClick.onPhoneClick(i);
      }
    });

    viewHolder.tvNewLeft.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onTaskClick.onTaskOptionNewLeftClick(v,i);
      }
    });
  }

  @Override
  public int getItemCount() {
    return mListData == null ? 0 : mListData.size();
  }

  public List<SeckillOrderList> getmListData() {
    return mListData;
  }

  public void setmListData(List<SeckillOrderList> mListData) {
    this.mListData = mListData;
    notifyDataSetChanged();
  }

  public void setOnStoreImgClick(OnStoreImgClickListener onStoreImgClick) {
    this.onStoreImgClick = onStoreImgClick;
  }

  //public void setOnItemClick(OnItemClickListenerRv onItemClick) {
  //  this.onItemClick = onItemClick;
  //}

  public interface OnStoreImgClickListener {
    void onStoreImgClick(View view, int position);
  }

  public void setOnPhoneClick(OnPhoneBtnClickListener onPhoneClick) {
    this.onPhoneClick = onPhoneClick;
  }

  public interface OnPhoneBtnClickListener {
    void onPhoneClick(int position);
  }

  public interface OnTaskOptionClickListener {
    void onTaskOptionLeftClick(View v, int position);

    void onTaskOptionRightClick(View v, int position);

    void onTaskOptionNewLeftClick(View v,int position);
  }

  public void setOnTaskOptionClickListener(OnTaskOptionClickListener onTaskClick) {
    this.onTaskClick = onTaskClick;
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.ll_itemView) LinearLayout llItemView;
    @Bind(R.id.tv_store_name) TextView tvStoreName;
    @Bind(R.id.tv_numberid) TextView tvNumberid;
    @Bind(R.id.tv_map_dingwei) TextView tvMapDingwei;
    @Bind(R.id.tv_store_lbname) TextView tvStoreLbname;
    @Bind(R.id.tv_store_phoneNum) TextView tvStorePhoneNum;
    @Bind(R.id.img_map_shoppre) ImageView imgMapShoppre;
    @Bind(R.id.tv_left) TextView tvLeft;
    @Bind(R.id.tv_new_left) TextView tvNewLeft;
    @Bind(R.id.tv_right) TextView tvRight;
    @Bind(R.id.ll_task_bottom) LinearLayout llTaskBottom;
    @Bind(R.id.tv_store_Address) TextView tvStoreAddress;

    ViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }
}
