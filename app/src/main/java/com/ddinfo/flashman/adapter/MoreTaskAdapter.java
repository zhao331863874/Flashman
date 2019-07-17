package com.ddinfo.flashman.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddinfo.flashman.R;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.impl.OnItemClickListenerRv;
import com.ddinfo.flashman.models.SeckillOrderList;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Title: 请输入标题
 * Created by fuh on 2017/1/12.
 * Email：unableApe@gmail.com
 * 更多任务订单列表适配器
 */

public class MoreTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private boolean isEmpty = false; //是否为空
    private boolean isLoadAll = false; //是否全部加载
    private OnItemClickListenerRv onItemClickListenerRv;
    private List<SeckillOrderList> mListData = new ArrayList<>();
    private int flag = 0;

    public void setOnItemClickListenerRv(OnItemClickListenerRv onItemClickListenerRv) {
        this.onItemClickListenerRv = onItemClickListenerRv;
    }

    public void setmListData(List<SeckillOrderList> mListData) {
        this.mListData = mListData;
        notifyDataSetChanged();
    }

    public void setItemType(int flag) {
        this.flag = flag;
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public void setLoadAll(boolean loadAll) {
        isLoadAll = loadAll;
    }

    public MoreTaskAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case ExampleConfig.VIEWHOLDER_NORMAL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_task, null); //构造配送中、返还中数据布局
                holder = new MoreTaskAdapter.ViewHolderNormal(view);
                break;
            case ExampleConfig.VIEWHOLDER_RETURN_BACK:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_task_back, null); //构造退货取货中数据布局
                holder = new ViewHolderBack(view);
                break;
            case ExampleConfig.VIEWHOLDER_FOOT:
                View viewFooter =
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer_load_more, null); //构造正在加载布局
                holder = new MoreTaskAdapter.ViewHolderFoot(viewFooter);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (position == mListData.size()) {
            MoreTaskAdapter.ViewHolderFoot viewHolderFoot = (MoreTaskAdapter.ViewHolderFoot) holder;
            if (!isEmpty) {
                if (isLoadAll) {
                    viewHolderFoot.tvLoadMore.setText("已加载全部");
                } else {
                    viewHolderFoot.tvLoadMore.setText("正在加载");
                }
            } else {
                viewHolderFoot.tvLoadMore.setText("暂无数据");
            }
        } else if (flag == 1) { //订单退货返还、退货完成、退货取消 状态
            MoreTaskAdapter.ViewHolderBack viewHolderNormal = (MoreTaskAdapter.ViewHolderBack) holder;
            viewHolderNormal.itemView.setTag(position);
            viewHolderNormal.relIncomeBack.setVisibility(View.GONE); //隐藏收入布局
            viewHolderNormal.llTaskBottom.setVisibility(View.GONE);  //隐藏送达、拒收布局
            viewHolderNormal.tvCenter.setVisibility(View.VISIBLE);
            viewHolderNormal.textTimeTake.setText("创建时间：");
            viewHolderNormal.tvTimeTake.setText(mListData.get(position).getCreatedTime()); //创建时间
            viewHolderNormal.tvCenter.setVisibility(View.GONE);      //隐藏扫描二维码
            viewHolderNormal.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListenerRv.onItemClick(v, position);
                }
            });

            viewHolderNormal.tvAmountIncome.setText(mListData.get(position).getCommission()); //收入金额
            viewHolderNormal.tvAmountFrozen.setText("冻结：" + mListData.get(position).getOrderAmount() + "元"); //冻结金额
            viewHolderNormal.tvAddressSend.setText(mListData.get(position).getStoreAddress());     //店铺地址
            viewHolderNormal.tvAddressTake.setText(mListData.get(position).getWarehouseAddress()); //收货地址
            viewHolderNormal.tvStoreName.setText(mListData.get(position).getStoreName());          //店铺名称

            if (!TextUtils.isEmpty(String.valueOf(mListData.get(position).getBackOrderId()))) {
                viewHolderNormal.tvStoreInvoiceId.setVisibility(View.VISIBLE);
                viewHolderNormal.tvStoreInvoiceId.setText(String.valueOf(mListData.get(position).getBackOrderId()));
            } else {
                viewHolderNormal.tvStoreInvoiceId.setVisibility(View.GONE);
            }
        } else { //订单取消、完成状态
            MoreTaskAdapter.ViewHolderNormal viewHolderNormal = (MoreTaskAdapter.ViewHolderNormal) holder;
            viewHolderNormal.itemView.setTag(position);
            viewHolderNormal.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListenerRv.onItemClick(v, position);
                }
            });

            viewHolderNormal.tvAmountIncome.setText(mListData.get(position).getCommission()); //收入金额
            viewHolderNormal.tvAmountFrozen.setVisibility(View.GONE);
            viewHolderNormal.tvAmountFrozen.setText(
                    "冻结：" + mListData.get(position).getOrderAmount() + "元"); //冻结金额
            viewHolderNormal.tvAddressSend.setText(mListData.get(position).getStoreAddress()); //店铺地址
            viewHolderNormal.tvAddressTake.setText(mListData.get(position).getWarehouseAddress()); //取货地址

            //BUG: 禅道bugId=5656，解决日期:2017/6/6，解决人:李占晓，解释:更多，已完成订单页，取货时间没有年月日，且多出取货字样
            //viewHolderNormal.tvTimeTake.setText("取货时间："+mListData.get(position).getReceiveTime());
            viewHolderNormal.tvTimeTake.setText(mListData.get(position).getReceiveTime()); //取货时间

            viewHolderNormal.tvStoreName.setText(mListData.get(position).getStoreName());  //收货店铺名称
            viewHolderNormal.tvStoreInvoiceId.setText(mListData.get(position).getInvoiceNumberId()); //退货单号
            viewHolderNormal.viewLine.setVisibility(View.GONE);
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
        } else if (flag == 1) {
            return ExampleConfig.VIEWHOLDER_RETURN_BACK;
        } else {
            return ExampleConfig.VIEWHOLDER_NORMAL;
        }
    }

    public class ViewHolderBack extends RecyclerView.ViewHolder { //退货取货中数据布局
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
        @Bind(R.id.tv_address_take)    //收货地址
        TextView tvAddressTake;
        @Bind(R.id.text_time_take)     //创建时间名称
        TextView textTimeTake;
        @Bind(R.id.tv_time_take)       //创建时间
        TextView tvTimeTake;
        @Bind(R.id.tv_store_name)      //店铺名称
        TextView tvStoreName;
        @Bind(R.id.tv_address_send)    //店铺地址
        TextView tvAddressSend;
        @Bind(R.id.ll_top)
        LinearLayout llTop;
        @Bind(R.id.tv_center)          //扫描二维码
        TextView tvCenter;
        @Bind(R.id.tv_left)            //送达按钮
        TextView tvLeft;
        @Bind(R.id.tv_right)           //拒收按钮
        TextView tvRight;
        @Bind(R.id.tv_store_invoiceId) //退货单号
        TextView tvStoreInvoiceId;
        @Bind(R.id.ll_task_bottom)     //送达、拒收布局
        LinearLayout llTaskBottom;
        @Bind(R.id.rel_income_back)    //收入布局
        RelativeLayout relIncomeBack;

        public ViewHolderBack(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class ViewHolderNormal extends RecyclerView.ViewHolder { //配送中、返还中数据布局

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
        @Bind(R.id.tv_address_take)    //取货地址
        TextView tvAddressTake;
        @Bind(R.id.tv_time_take)       //取货时间
        TextView tvTimeTake;
        @Bind(R.id.tv_store_name)      //收货店铺名称
        TextView tvStoreName;
        @Bind(R.id.tv_address_send)    //店铺地址
        TextView tvAddressSend;
        @Bind(R.id.view_line)
        View viewLine;
        @Bind(R.id.tv_store_invoiceId) //退货单号
        TextView tvStoreInvoiceId;

        public ViewHolderNormal(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class ViewHolderFoot extends RecyclerView.ViewHolder { //正在加载布局
        @Bind(R.id.tv_load_more) //正在加载
        TextView tvLoadMore;
        @Bind(R.id.rl_load_more)
        RelativeLayout rlLoadMore;

        public ViewHolderFoot(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
