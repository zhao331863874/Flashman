package com.ddinfo.flashman.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.SizeUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.models.ChildManEntity;
import com.ddinfo.flashman.models.WalletEntity;
import com.ddinfo.flashman.utils.QRBitMapUtil;
import com.google.zxing.WriterException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Gavin on 2017/8/9.
 * 下级配送员适配器
 */

public class SupervinsingJuniorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ChildManEntity> mListData = new ArrayList<>();
    private boolean isEmpty = false; //是否为空数据
    private boolean isLoadAll = false; //是否加载全部
    private WalletEntity data;


    private OnJuniorMenuClickListener onJuniorMenuClickListener;


    public SupervinsingJuniorAdapter(Context context) {
        this.context = context;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public void setLoadAll(boolean loadAll) {
        isLoadAll = loadAll;
    }

    public void setOnJuniorMenuClickListener(OnJuniorMenuClickListener onJuniorMenuClickListener) {
        this.onJuniorMenuClickListener = onJuniorMenuClickListener;
    }

    public void setTopData(WalletEntity data) {
        this.data = data;
        notifyItemChanged(0);
    }

    public void setListData(List<ChildManEntity> mListData) {
        this.mListData = mListData;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case ExampleConfig.VIEWHOLDER_TOP:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_super_junior_top, null); //管理下级配送员布局
                holder = new ViewHolderTop(view);
                break;
            case ExampleConfig.VIEWHOLDER_NORMAL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_super_junior_normal, null); //单条下级配送员布局
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        int viewType = getViewType(position);
        switch (viewType) {
            case ExampleConfig.VIEWHOLDER_TOP:
                ViewHolderTop viewHolderTop = (ViewHolderTop) holder;
                if(data!=null){
                    final String urlCode = data.getDeliveryManId();
                    try {
                        Bitmap www = QRBitMapUtil.createQRCode(urlCode, SizeUtils.dp2px(30));
                        viewHolderTop.ivQrCode.setImageBitmap(www);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                    viewHolderTop.ivQrCode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) { //我的二维码监听
                            if (onJuniorMenuClickListener != null) {
                                onJuniorMenuClickListener.onQRcodeClick(v, urlCode);
                            }
                        }
                    });
                    viewHolderTop.tvAmountSum.setText("总授信额度："+data.getCreditAmount());
                    viewHolderTop.tvGoodsAmount.setText(data.getUsable()+"");
                    viewHolderTop.tvFrozenSum.setText(data.getFrozenAmount()+"");
                    viewHolderTop.tvJuniorSum.setText("下级配送员人数："+mListData.size());
                }else{
                    viewHolderTop.ivQrCode.setVisibility(View.GONE);
                }

                break;
            case ExampleConfig.VIEWHOLDER_NORMAL:
                ViewHolderNormal viewHolderNormal = (ViewHolderNormal) holder;
                viewHolderNormal.tvSetAmount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { //授信监听
                        if (onJuniorMenuClickListener != null) {
                            onJuniorMenuClickListener.onLeftMenuClick(v, position-1);
                        }
                    }
                });
                viewHolderNormal.tvDeliveryMes.setOnClickListener(new View.OnClickListener() { //配送完成情况监听
                    @Override
                    public void onClick(View v) { //配送完成情况监听
                        if (onJuniorMenuClickListener != null) {
                            onJuniorMenuClickListener.onRightMenuClick(v, position-1);
                        }
                    }
                });
                viewHolderNormal.tvFrozenDetail.setOnClickListener(new View.OnClickListener() { //冻结明细
                    @Override
                    public void onClick(View v) { //冻结明细监听
                        if (onJuniorMenuClickListener != null) {
                            onJuniorMenuClickListener.onFrozenClick(v, position-1);
                        }
                    }
                });
                viewHolderNormal.tvJuniorName.setText(mListData.get(position-1).getDeliveryManName()); //下级配送员名称
                viewHolderNormal.tvJuniorTel.setText(mListData.get(position-1).getPhone()); //下级配送员电话
                viewHolderNormal.tvAmount.setText("授信额度："+mListData.get(position-1).getCreditAmount());
                viewHolderNormal.tvFrozen.setText("冻结金额"+mListData.get(position-1).getFrozenAmount());
                viewHolderNormal.tvTakeAmount.setText("可接货额度"+mListData.get(position-1).getUsable());
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
            return 2;
        } else {
            return mListData.size() + 2;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return getViewType(position);
    }

    private int getViewType(int position) {
        if (mListData.size() == 0) {
            if(position ==0){
                return ExampleConfig.VIEWHOLDER_TOP;
            }else{
                return ExampleConfig.VIEWHOLDER_FOOT;
            }
        } else {
            if (position == 0) {
                return ExampleConfig.VIEWHOLDER_TOP;
            } else if (position == mListData.size() + 1) {
                return ExampleConfig.VIEWHOLDER_FOOT;
            } else {
                return ExampleConfig.VIEWHOLDER_NORMAL;
            }
        }
    }

    public interface OnJuniorMenuClickListener {
        void onQRcodeClick(View view, String urlCode);

        void onFrozenClick(View view, int position);

        void onLeftMenuClick(View view, int position);

        void onRightMenuClick(View view, int position);
    }
    static class ViewHolderTop extends RecyclerView.ViewHolder { //管理下级配送员布局
        @Bind(R.id.text_goods_amount) //我的接货额度名称
        TextView textGoodsAmount;
        @Bind(R.id.tv_goods_amount)   //我的接货额度
        TextView tvGoodsAmount;
        @Bind(R.id.iv_qrCode)         //我的二维码
        ImageView ivQrCode;
        @Bind(R.id.tv_junior_sum)     //下级配送员人数
        TextView tvJuniorSum;
        @Bind(R.id.tv_amount_sum)     //总授信额度
        TextView tvAmountSum;
        @Bind(R.id.text_frozen_sum)   //总冻结金额名称
        TextView textFrozenSum;
        @Bind(R.id.tv_frozen_sum)     //总冻结金额
        TextView tvFrozenSum;

        public ViewHolderTop(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ViewHolderNormal extends RecyclerView.ViewHolder { //单条下级配送员布局
        @Bind(R.id.tv_junior_name) //下级配送员名称
        TextView tvJuniorName;
        @Bind(R.id.tv_junior_tel)  //下级配送员电话
        TextView tvJuniorTel;
        @Bind(R.id.tv_amount)      //授信额度
        TextView tvAmount;
        @Bind(R.id.tv_frozen)      //冻结金额
        TextView tvFrozen;
        @Bind(R.id.tv_take_amount) //可接货额度
        TextView tvTakeAmount;
        @Bind(R.id.tv_frozen_detail) //冻结明细
        TextView tvFrozenDetail;
        @Bind(R.id.tv_set_amount)  //授信
        TextView tvSetAmount;
        @Bind(R.id.tv_delivery_mes)//配送完成情况
        TextView tvDeliveryMes;

        public ViewHolderNormal(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ViewHolderFoot extends RecyclerView.ViewHolder { //正在加载布局
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
