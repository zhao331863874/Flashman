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
import com.ddinfo.flashman.models.PaymentListEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fuh on 2017/5/10.
 * Email：unableApe@gmail.com
 */

public class PaymentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<PaymentListEntity> mListData = new ArrayList<>();
    private List<ItemMessage> mListMessage = new ArrayList<>();
    private OnItemClickListener onItemClickListenerRv;

    private boolean isEmpty = false;
    private boolean isLoadAll = false;

    public void setOnItemClickListenerRv(OnItemClickListener onItemClickListenerRv) {
        this.onItemClickListenerRv = onItemClickListenerRv;
    }

    public void setListData(List<PaymentListEntity> mListData) {
        this.mListData = mListData;
        if (mListData.size() != 0) {
            mListMessage = getListItemMessage(mListData);
        }
        notifyDataSetChanged();
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public void setLoadAll(boolean loadAll) {
        isLoadAll = loadAll;
    }

    public PaymentListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case ExampleConfig.VIEWHOLDER_HEAD:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_list_head, parent, false);
                holder = new ViewHolderHead(view);
                break;
            case ExampleConfig.VIEWHOLDER_NORMAL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_list_normal, parent, false);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onItemClickListenerRv.onItemClick(v, (Integer) v.getTag());
                    }
                });
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            switch (mListMessage.get(position).getViewType()) {
                case ExampleConfig.VIEWHOLDER_HEAD:
                    ViewHolderHead viewHolderHead = (ViewHolderHead) holder;
                    viewHolderHead.tvDate.setText(mListMessage.get(position).getDate());
                    viewHolderHead.tvMoney.setText(mListMessage.get(position).getSum() + "元");
                    break;
                case ExampleConfig.VIEWHOLDER_NORMAL:
                    int listDataPosition = mListMessage.get(position).getChildPosition();
                    ViewHolderNormal viewHolderNormal = (ViewHolderNormal) holder;
                    viewHolderNormal.itemView.setTag(mListMessage.get(position).getChildPosition());
                    viewHolderNormal.tvId.setText(mListData.get(listDataPosition).getBatchId() + "");
                    viewHolderNormal.tvDate.setText(mListData.get(listDataPosition).getTime() + "");
                    viewHolderNormal.tvMoney.setText("￥" + mListData.get(listDataPosition).getSum());
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
        } catch (Exception e) {
            e.printStackTrace(); //为了处理(PaymentListAdapter.java:98) IndexOutOfBoundsException
        }
    }

    @Override
    public int getItemCount() {
        return mListMessage.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mListMessage.get(position).getViewType();
    }

    private List<ItemMessage> getListItemMessage(List<PaymentListEntity> listData) {

        //情况：
        // i == 0   : head + normal
        // i == i-1 : normal
        // i != i-1 : head + normal
        // i == listData.size : normal + foot
        // eg: 1  1  1  2  2  3  3  3  3
        //     hn n  n  hn n  hn n  n  nf
        //确定h的headSum是在下一个head生成的时候，此时将计算的headSum赋给上一个head。最后一个head的headSum则是i==listdata.size的时候确定
        List<ItemMessage> listMessage = new ArrayList<>(); //存储child的viewType......
//        List<Double> headSums = getHeadSums();
        double headSum = 0;
        if (mListData.size() == 1) {
            ItemMessage itemMessageHead = new ItemMessage();
            itemMessageHead.setViewType(ExampleConfig.VIEWHOLDER_HEAD);
            itemMessageHead.setSum(mListData.get(0).getSum());
            itemMessageHead.setDate(mListData.get(0).getFmt());
            listMessage.add(itemMessageHead);

            ItemMessage itemMessageNormal = new ItemMessage();
            itemMessageNormal.setViewType(ExampleConfig.VIEWHOLDER_NORMAL);
            itemMessageNormal.setChildPosition(0);
            listMessage.add(itemMessageNormal);
            return listMessage;
        } else {
            int headIndex = 0;
            int headNum = 0;

            for (int i = 0; i < listData.size(); i++) {
                if (i == 0) {
                    //第一条肯定是Head，除了headSum外的属性均可确定
                    ItemMessage itemMessageHead = new ItemMessage();
                    itemMessageHead.setViewType(ExampleConfig.VIEWHOLDER_HEAD);
                    itemMessageHead.setDate(mListData.get(i).getFmt());
                    listMessage.add(itemMessageHead);
                    headIndex = i;


                    //同时加入一个Normal视图,放入listData的Position,并把金额加到headSum
                    ItemMessage itemMessageNormal = new ItemMessage();
                    itemMessageNormal.setViewType(ExampleConfig.VIEWHOLDER_NORMAL);
                    itemMessageNormal.setChildPosition(i);
                    listMessage.add(itemMessageNormal);
                    headSum += mListData.get(i).getSum();
                } else if (i == mListData.size() - 1) { //最后一个
                    ItemMessage itemMessageNormal = new ItemMessage();
                    itemMessageNormal.setViewType(ExampleConfig.VIEWHOLDER_NORMAL);
                    itemMessageNormal.setChildPosition(i);
                    listMessage.add(itemMessageNormal);
                    headSum += mListData.get(i).getSum();

                    ItemMessage itemMessageFoot = new ItemMessage();
                    itemMessageFoot.setViewType(ExampleConfig.VIEWHOLDER_FOOT);
                    listMessage.add(itemMessageFoot);

                    //给上一个head的headSum赋值，并将headSum初始化
                    listMessage.get(headIndex + headNum).setSum(headSum);
                    headSum = 0;
                } else {
                    if (!listData.get(i).getFmt().equals(listData.get(i - 1).getFmt())) { //不同的item
                        //日期不相等代表上一组数据已经全部遍历完成，添加一个head视图和Normal视图
                        //此Head视图的headSum 则是下一组数据的Sum总和
                        ItemMessage itemMessageHead = new ItemMessage();
                        itemMessageHead.setViewType(ExampleConfig.VIEWHOLDER_HEAD);
                        itemMessageHead.setDate(mListData.get(i).getFmt());
                        listMessage.add(itemMessageHead);

                        //给上一个head的headSum赋值，并将headSum初始化
                        listMessage.get(headIndex + headNum).setSum(headSum);
                        headNum++;
                        headIndex = i;
                        headSum = 0;
                        //同时加入一个Normal视图,放入listData的Position
                        ItemMessage itemMessageNormal = new ItemMessage();
                        itemMessageNormal.setViewType(ExampleConfig.VIEWHOLDER_NORMAL);
                        itemMessageNormal.setChildPosition(i);
                        listMessage.add(itemMessageNormal);
                        headSum += mListData.get(i).getSum();
                    } else {
                        ItemMessage itemMessageNormal = new ItemMessage();
                        itemMessageNormal.setViewType(ExampleConfig.VIEWHOLDER_NORMAL);
                        itemMessageNormal.setChildPosition(i);
                        listMessage.add(itemMessageNormal);
                        headSum += mListData.get(i).getSum();
                    }
                }
            }
        }
        return listMessage;
    }


    public static class ItemMessage {
        private int viewType;
        private int parentPosition = -1;
        private int childPosition = -1;
        private String date;
        private double sum;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public double getSum() {
            return sum;
        }

        public void setSum(double sum) {
            this.sum = sum;
        }

        public int getParentPosition() {
            return parentPosition;
        }

        public void setParentPosition(int parentPosition) {
            this.parentPosition = parentPosition;
        }

        public int getChildPosition() {
            return childPosition;
        }

        public void setChildPosition(int childPosition) {
            this.childPosition = childPosition;
        }

        public int getViewType() {
            return viewType;
        }

        public void setViewType(int viewType) {
            this.viewType = viewType;
        }
    }

    static class ViewHolderHead extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_date)
        TextView tvDate;
        @Bind(R.id.tv_money)
        TextView tvMoney;

        public ViewHolderHead(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ViewHolderNormal extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_id)
        TextView tvId;
        @Bind(R.id.tv_date)
        TextView tvDate;
        @Bind(R.id.tv_money)
        TextView tvMoney;

        public ViewHolderNormal(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ViewHolderFoot extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_load_more)
        TextView tvLoadMore;
        @Bind(R.id.rl_load_more)
        RelativeLayout rlLoadMore;

        public ViewHolderFoot(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
}
