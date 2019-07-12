package com.ddinfo.flashman.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.SizeUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.models.PartSendListEntity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Title: 部分送达拒收商品适配器
 * Created by fuh on 2016/11/17.
 * Email：unableApe@gmail.com
 */
public class OrderPartSendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private PartSendListEntity detailEntity = new PartSendListEntity();
    private PartSendListEntity.DetailsBean goodsBean;
    private PartSendListEntity.DetailsBean.SnapshotBean.OnSellGoodsCombsBean giftsBean;

    private OnSelNumClickListener onSelNumClickListener;

    public void setOnSelNumClickListener(OnSelNumClickListener onSelNumClickListener) {
        this.onSelNumClickListener = onSelNumClickListener;
    }

    public OrderPartSendAdapter(Context context) {
        this.context = context;
    }

    public void setPartSendListEntity(PartSendListEntity detailEntity) {
        this.detailEntity = detailEntity;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_part_send, null);
        return new ViewHolderNormal(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolderNormal holderNormal = (ViewHolderNormal) holder;
        goodsBean = detailEntity.getDetails().get(position);

        holderNormal.tvGoodsName.setText(goodsBean.getName());
        if (goodsBean.getGiftFlag() == 1) {//赠品
            holderNormal.tvIndex.setText("赠");
            holderNormal.ivDel.setVisibility(View.INVISIBLE);
            holderNormal.ivAdd.setVisibility(View.INVISIBLE);
            holderNormal.tvGoodsNum.setText(goodsBean.getQuantity() + "");

        } else {
            holderNormal.tvGoodsNum.setText(goodsBean.getRefuseQuantity() + "");

            holderNormal.tvIndex.setText(position + 1 + "");
            holderNormal.ivDel.setVisibility(View.VISIBLE);
            holderNormal.ivAdd.setVisibility(View.VISIBLE);
            holderNormal.ivDel.setClickable(false);
            holderNormal.ivAdd.setClickable(false);

            holderNormal.tvGoodsNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onSelNumClickListener != null) {
                        onSelNumClickListener.onSelNum(view, position);
                    }
                }
            });

            if (goodsBean.getRefuseQuantity() <= 0) {
                holderNormal.ivDel.setClickable(false);
                holderNormal.ivDel.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.img_del_off));
            } else {
                holderNormal.ivDel.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.img_del));
                holderNormal.ivDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onSelNumClickListener != null) {
                            notifyDataSetChanged();
                            onSelNumClickListener.onSelNumDel(v, position);
                        }
                    }
                });
            }

            if (goodsBean.getRefuseQuantity() >= goodsBean.getQuantity()) {
                holderNormal.ivAdd.setClickable(false);
                holderNormal.ivAdd.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.img_add_off));
            } else {
                holderNormal.ivAdd.setImageDrawable(ContextCompat.getDrawable(context, R.mipmap.img_add));
                holderNormal.ivAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onSelNumClickListener != null) {
                            notifyDataSetChanged();
                            onSelNumClickListener.onSelNumAdd(v, position);
                        }
                    }
                });
            }
        }

        //动态添加套装和赠品
        if (detailEntity.getDetails().get(position).getSnapshot().getOnSellGoodsCombs().size() == 1 && detailEntity.getDetails().get(position).getSnapshot().getOnSellGoodsCombs().get(0).getAmount() == 1) {
            //单品不显示商品列表，仅显示标题
            holderNormal.llGifts.removeAllViews();
        } else {
            addCombsGoods(holderNormal, position);
        }
    }

    private void addGoodsItem(LinearLayout llGoodsIn, String name, String amount) {
        //商品信息横向容器
        LinearLayout horLinear = new LinearLayout(context, null);
        horLinear.setOrientation(LinearLayout.HORIZONTAL);
        //商品名称
        TextView tvGoodsName = new TextView(context, null);
        LinearLayout.LayoutParams paramsGoodsName = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tvGoodsName.setText(name);
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

    public void addCombsGoods(ViewHolderNormal holderNormal, int position) {
        holderNormal.llGifts.removeAllViews();
        holderNormal.tvIndex.setText(position + 1 + "");

        //当商品组内的商品种类为1，且数量为1时，为单品销售
        //单品销售时，item名称显示为商品简介 既OnSellGoodsCombs.getName
        if (detailEntity.getDetails().get(position).getSnapshot().getOnSellGoodsCombs().size() == 1 && detailEntity.getDetails().get(position).getSnapshot().getOnSellGoodsCombs().get(0).getAmount() == 1) {
            addGoodsItem(holderNormal.llGifts, detailEntity.getDetails().get(0).getSnapshot().getName(), detailEntity.getDetails().get(position).getSnapshot().getOnSellGoodsCombs().get(0).getAmount() * detailEntity.getDetails().get(0).getRefuseQuantity() + "");
        } else {
            for (int j = 0; j < detailEntity.getDetails().get(position).getSnapshot().getOnSellGoodsCombs().size(); j++) { //商品
                addGoodsItem(holderNormal.llGifts, detailEntity.getDetails().get(position).getSnapshot().getOnSellGoodsCombs().get(j).getName(), detailEntity.getDetails().get(position).getSnapshot().getOnSellGoodsCombs().get(j).getAmount() * detailEntity.getDetails().get(position).getRefuseQuantity() + "");
            }
        }
    }

    @Override
    public int getItemCount() {
        if (detailEntity.getDetails() != null) {
            return detailEntity.getDetails().size();
        } else {
            return 0;
        }
    }

    public static class ViewHolderNormal extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_index)
        TextView tvIndex;
        @Bind(R.id.tv_goods_name)
        TextView tvGoodsName;
        @Bind(R.id.iv_del)
        ImageView ivDel;
        @Bind(R.id.tv_goods_num)
        TextView tvGoodsNum;
        @Bind(R.id.iv_add)
        ImageView ivAdd;
        @Bind(R.id.ll_gifts)
        LinearLayout llGifts;

        public ViewHolderNormal(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnSelNumClickListener {
        void onSelNumAdd(View v, int position);

        void onSelNumDel(View v, int position);

        void onSelNum(View v, int position);
    }


}
