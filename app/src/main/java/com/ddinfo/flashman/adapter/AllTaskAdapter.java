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
 * Created by fuh on 2017/1/6.
 * Email：unableApe@gmail.com
 */

public class AllTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private OnItemClickListenerRv itemListener;
    private OnTaskOptionClickListener onTaskOptionClickListener;
    private List<SeckillOrderList> mListData = new ArrayList<>();

    private int mTaskType; //当前选卡位置; 1：配送中 2：退货取货中 3：返还中 4：更多

    private boolean isEmpty = false; //是否为空
    private boolean isLoadAll = false; //是否全部加载

    public void setItemListener(OnItemClickListenerRv itemListener) {
        this.itemListener = itemListener;
    }

    public void setOnTaskOptionClickListener(OnTaskOptionClickListener onTaskOptionClickListener) {
        this.onTaskOptionClickListener = onTaskOptionClickListener;
    }

    public void setmListData(List<SeckillOrderList> mListData) {
        this.mListData = mListData;
        notifyDataSetChanged(); //更新数据
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public void setLoadAll(boolean loadAll) {
        isLoadAll = loadAll;
    }

    public AllTaskAdapter(Context context, int position) {
        this.context = context;
        mTaskType = position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case ExampleConfig.VIEWHOLDER_NORMAL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_task, null); //构造配送中、返还中数据布局
                holder = new ViewHolderNormal(view);
                break;
            case ExampleConfig.VIEWHOLDER_RETURN_BACK:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_task_back, null); //构造退货取货中数据布局
                holder = new ViewHolderBack(view);
                break;
            case ExampleConfig.VIEWHOLDER_FOOT:
                View viewFooter = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer_load_more, null); //构造正在加载布局
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
        } else if (mTaskType == 1) { //退货取货中
            ViewHolderBack viewHolderNormal = (ViewHolderBack) holder;
            viewHolderNormal.itemView.setTag(position);
            viewHolderNormal.relIncomeBack.setVisibility(View.GONE); //隐藏收入
            viewHolderNormal.llTaskBottom.setVisibility(View.GONE);  //隐藏送达/拒收/按钮
            viewHolderNormal.tvCenter.setVisibility(View.VISIBLE);   //显示扫码二维码
            viewHolderNormal.textTimeTake.setText("创建时间：");      //取货创建时间名称
            viewHolderNormal.tvTimeTake.setText(mListData.get(position).getCreatedTime()); //取货时间
            viewHolderNormal.tvCenter.setText("取货");

            viewHolderNormal.tvCenter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onTaskOptionClickListener.onTaskOptionTakeClick(v, position);
                }
            });

            viewHolderNormal.llTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemListener != null) {
                        itemListener.onItemClick(v, position);
                    }
                }
            });

            viewHolderNormal.tvAmountIncome.setText(mListData.get(position).getCommission()); //收入金额
            viewHolderNormal.tvAmountFrozen.setText("冻结：" + mListData.get(position).getOrderAmount() + "元"); //冻结金额
            viewHolderNormal.tvAddressSend.setText(mListData.get(position).getStoreAddress()); //收货地址
            viewHolderNormal.tvAddressTake.setText(mListData.get(position).getWarehouseAddress()); //取货地址
            viewHolderNormal.tvStoreName.setText(mListData.get(position).getStoreName()); //收货店铺名称

            if (!TextUtils.isEmpty(String.valueOf(mListData.get(position).getBackOrderId()))) {
                viewHolderNormal.tvStoreInvoiceId.setVisibility(View.VISIBLE);
                viewHolderNormal.tvStoreInvoiceId.setText(String.valueOf(mListData.get(position).getBackOrderId()));
            } else {
                viewHolderNormal.tvStoreInvoiceId.setVisibility(View.GONE);
            }
        } else {
            ViewHolderNormal viewHolderNormal = (ViewHolderNormal) holder;
            viewHolderNormal.itemView.setTag(position);
            switch (mTaskType) {
                case 0: //配送中
                    viewHolderNormal.llTaskBottom.setVisibility(View.VISIBLE); //显示送达/拒收/按钮
                    viewHolderNormal.tvCenter.setVisibility(View.GONE);        //隐蔽扫码二维码
                    viewHolderNormal.textTimeTake.setText("取货：");           //取货时间名称
                    viewHolderNormal.tvTimeTake.setText(mListData.get(position).getPickupTime()); //取货时间
                    viewHolderNormal.tvLeft.setText("送达");
                    viewHolderNormal.tvRight.setText("拒收");

                    viewHolderNormal.tvLeft.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onTaskOptionClickListener.onTaskOptionGoodsSendClick(v, position);
                        }
                    });
                    viewHolderNormal.tvRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onTaskOptionClickListener.onTaskOptionGoodsRefuseClick(v, position);
                        }
                    });
                    break;
                case 2: //返还中
                    viewHolderNormal.relIncomeBack.setVisibility(View.VISIBLE); //显示收入布局
                    viewHolderNormal.llTaskBottom.setVisibility(View.GONE);     //隐藏送达、拒收按钮
                    viewHolderNormal.tvCenter.setVisibility(View.VISIBLE);      //显示扫码二维码
                    viewHolderNormal.textTimeTake.setText("取货：");             //取货时间名称
                    viewHolderNormal.tvTimeTake.setText(mListData.get(position).getPickupTime()); //取货时间
                    viewHolderNormal.tvCenter.setText("出示二维码");

                    viewHolderNormal.tvCenter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onTaskOptionClickListener.onTaskOptionShowCodeClick(v, position);
                        }
                    });
                    break;

                default:
                    break;
            }

            viewHolderNormal.llTop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { //单项任务列表布局监听
                    if (itemListener != null) {
                        itemListener.onItemClick(v, position);
                    }
                }
            });

            viewHolderNormal.tvAmountIncome.setText(mListData.get(position).getCommission());                   //设置收入金额
            viewHolderNormal.tvAmountFrozen.setText("冻结：" + mListData.get(position).getOrderAmount() + "元"); //冻结金额
            viewHolderNormal.tvAddressSend.setText(mListData.get(position).getStoreAddress());                  //收货地址
            viewHolderNormal.tvAddressTake.setText(mListData.get(position).getWarehouseAddress());              //取货地址
            viewHolderNormal.tvStoreName.setText(mListData.get(position).getStoreName());                       //收货店铺名称

            if (!TextUtils.isEmpty(mListData.get(position).getInvoiceNumberId())) {
                viewHolderNormal.tvStoreInvoiceId.setVisibility(View.VISIBLE);
                viewHolderNormal.tvStoreInvoiceId.setText(mListData.get(position).getInvoiceNumberId());
            } else {
                viewHolderNormal.tvStoreInvoiceId.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mListData.size() == 0 ? 1 : mListData.size() + 1;
    }

    @Override
    public int getItemViewType(int position) { //获取当前Item位置
        if (position == mListData.size()) {
            return ExampleConfig.VIEWHOLDER_FOOT;
        } else if (mTaskType == 1) { //退款取货中
            return ExampleConfig.VIEWHOLDER_RETURN_BACK;
        } else { //配送中、返还中
            return ExampleConfig.VIEWHOLDER_NORMAL;
        }
    }

    class ViewHolderNormal extends RecyclerView.ViewHolder { //配送中布局元素
        @Bind(R.id.text_amount_income)
        TextView textAmountIncome;
        @Bind(R.id.tv_amount_frozen) //冻结金额
        TextView tvAmountFrozen;
        @Bind(R.id.tv_amount_income) //收入金额
        TextView tvAmountIncome;
        @Bind(R.id.tv_distance_take)
        TextView tvDistanceTake;
        @Bind(R.id.tv_distance_send)
        TextView tvDistanceSend;
        @Bind(R.id.tv_address_take)//取货地址
        TextView tvAddressTake;
        @Bind(R.id.text_time_take) //取货时间名称
        TextView textTimeTake;
        @Bind(R.id.tv_time_take)   //取货时间
        TextView tvTimeTake;
        @Bind(R.id.tv_store_name)  //收货店铺名称
        TextView tvStoreName;
        @Bind(R.id.tv_address_send)//收货地址
        TextView tvAddressSend;
        @Bind(R.id.ll_top)
        LinearLayout llTop;
        @Bind(R.id.tv_center) //扫码二维码
        TextView tvCenter;
        @Bind(R.id.tv_left)   //送达按钮
        TextView tvLeft;
        @Bind(R.id.tv_right)  //拒收按钮
        TextView tvRight;
        @Bind(R.id.tv_store_invoiceId)
        TextView tvStoreInvoiceId;
        @Bind(R.id.ll_task_bottom)   //送达、拒收按钮
        LinearLayout llTaskBottom;
        @Bind(R.id.rel_income_back)
        RelativeLayout relIncomeBack;

        public ViewHolderNormal(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ViewHolderBack extends RecyclerView.ViewHolder { //退货取货中布局元素
        @Bind(R.id.text_amount_income)
        TextView textAmountIncome;
        @Bind(R.id.tv_amount_frozen) //冻结金额
        TextView tvAmountFrozen;
        @Bind(R.id.tv_amount_income) //收入金额
        TextView tvAmountIncome;
        @Bind(R.id.tv_distance_take)
        TextView tvDistanceTake;
        @Bind(R.id.tv_distance_send)
        TextView tvDistanceSend;
        @Bind(R.id.tv_address_take)  //取货地址
        TextView tvAddressTake;
        @Bind(R.id.text_time_take)   //收货时间名称
        TextView textTimeTake;
        @Bind(R.id.tv_time_take)     //收货时间
        TextView tvTimeTake;
        @Bind(R.id.tv_store_name)    //收货店铺名称
        TextView tvStoreName;
        @Bind(R.id.tv_address_send)  //收货地址
        TextView tvAddressSend;
        @Bind(R.id.ll_top)
        LinearLayout llTop;
        @Bind(R.id.tv_center)        //取货
        TextView tvCenter;
        @Bind(R.id.tv_left)
        TextView tvLeft;
        @Bind(R.id.tv_right)
        TextView tvRight;
        @Bind(R.id.tv_store_invoiceId) //商户发票/订单号ID
        TextView tvStoreInvoiceId;
        @Bind(R.id.ll_task_bottom)   //送达、拒收按钮
        LinearLayout llTaskBottom;
        @Bind(R.id.rel_income_back)  //收入
        RelativeLayout relIncomeBack;

        public ViewHolderBack(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ViewHolderFoot extends RecyclerView.ViewHolder { //正在加载布局元素
        @Bind(R.id.tv_load_more)
        TextView tvLoadMore;
        @Bind(R.id.rl_load_more)
        RelativeLayout rlLoadMore;

        public ViewHolderFoot(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnTaskOptionClickListener {
        /**
         * 送达
         *
         * @param v
         * @param position
         */
        void onTaskOptionGoodsSendClick(View v, int position);

        /**
         * 出示二维码
         *
         * @param v
         * @param position
         */
        void onTaskOptionShowCodeClick(View v, int position);

        /**
         * 拒收
         *
         * @param v
         * @param position
         */
        void onTaskOptionGoodsRefuseClick(View v, int position);

        /**
         * 取货
         *
         * @param v
         * @param position
         */
        void onTaskOptionTakeClick(View v, int position);
    }
}
