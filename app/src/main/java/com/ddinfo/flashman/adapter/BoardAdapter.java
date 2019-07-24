package com.ddinfo.flashman.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.utils.ConstUtils;
import com.blankj.utilcode.utils.TimeUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.models.BoardEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.blankj.utilcode.utils.TimeUtils.string2Millis;

/**
 * Created by fuh on 2017/5/5.
 * Email：unableApe@gmail.com
 * 下级配送员配送完成情况适配器
 */

public class BoardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<BoardEntity> mListData = new ArrayList<>(); //看板实体类ListData
    private List<ItemMessage> mListItemMes = new ArrayList<>();

    public BoardAdapter(Context context) {
        this.context = context;
    }

    public void setListData(List<BoardEntity> mListData) {
        this.mListData = mListData;
        mListItemMes = getListItemMessage(mListData);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case ExampleConfig.VIEWHOLDER_HEAD:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_head, parent,false);
                holder = new ViewHolderHead(view);
                break;
            case ExampleConfig.VIEWHOLDER_NORMAL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_board_normal, parent,false); //下级配送员配送明细
                holder = new ViewHolderNormal(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(mListItemMes.get(position));
        int childPosition = mListItemMes.get(position).getChildPosition();
        int parentPosition = mListItemMes.get(position).getParentPosition();
        switch (mListItemMes.get(position).getViewType()){
            case ExampleConfig.VIEWHOLDER_HEAD:
                ViewHolderHead viewHolderHead = (ViewHolderHead) holder;
                viewHolderHead.tvMonth.setText(mListData.get(parentPosition).getMonths());
                break;
            case ExampleConfig.VIEWHOLDER_NORMAL:
                BoardEntity.DayListBean dayBean = mListData.get(parentPosition).getDayList().get(childPosition);
                ViewHolderNormal viewHolderNormal = (ViewHolderNormal) holder;
                viewHolderNormal.tvCount.setText(dayBean.getNum()+""); //数量
                viewHolderNormal.tvIncome.setText("￥"+dayBean.getCommissionSum()); //收入
                viewHolderNormal.tvDay.setText(getFriendlyTimeSpanByNow(dayBean.getDays(),"yyyy-MM-dd")); //时间
                viewHolderNormal.tvSum.setText("￥"+dayBean.getOrderSum()); //订单金额
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mListItemMes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mListItemMes.get(position).getViewType();
    }

    private List<ItemMessage> getListItemMessage(List<BoardEntity> listData) {
        List<ItemMessage> listItemMes = new ArrayList<>();
        for (int i = 0; i < listData.size(); i++) {
            ItemMessage itemMesHead = new ItemMessage();
            itemMesHead.setViewType(ExampleConfig.VIEWHOLDER_HEAD);
            itemMesHead.setParentPosition(i);
            listItemMes.add(itemMesHead);
            for (int j = 0; j < listData.get(i).getDayList().size(); j++) {
                ItemMessage itemMesNormal = new ItemMessage();
                itemMesNormal.setViewType(ExampleConfig.VIEWHOLDER_NORMAL);
                itemMesNormal.setParentPosition(i);
                itemMesNormal.setChildPosition(j);
                listItemMes.add(itemMesNormal);
            }
        }
        return listItemMes;
    }

    public static String getFriendlyTimeSpanByNow(String time, String pattern) {
        return getFriendlyTimeSpanByNow(string2Millis(time, pattern));
    }

    @SuppressLint("DefaultLocale")
    public static String getFriendlyTimeSpanByNow(long millis) {
        long now = System.currentTimeMillis();
        long wee = getTodayZero();
        if (millis >= wee) {
            return String.format("今天%tF", millis);
        } else if (millis >= wee - ConstUtils.DAY) {
            return String.format("昨天%tF", millis);
        } else {
            return String.format("%tc", millis).substring(0,3)+"\n"+String.format("%tF",millis);
        }
    }

    public static long getTodayZero() {
        Date date = new Date();
        long l = 24*60*60*1000; //每天的毫秒数
        //date.getTime()是现在的毫秒数，它 减去 当天零点到现在的毫秒数（ 现在的毫秒数%一天总的毫秒数，取余。），理论上等于零点的毫秒数，不过这个毫秒数是UTC+0时区的。
        //减8个小时的毫秒值是为了解决时区的问题。
        return (date.getTime() - (date.getTime()%l) - 8* 60 * 60 *1000);
    }



    public static class ItemMessage {
        private int viewType;
        private int parentPosition = -1; //下级配送员配送完成适配器索引
        private int childPosition = -1;

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
        @Bind(R.id.tv_month) //月份
        TextView tvMonth;
        public ViewHolderHead(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ViewHolderNormal extends RecyclerView.ViewHolder { //下级配送员配送明细
        @Bind(R.id.tv_day) //时间
        TextView tvDay;
        @Bind(R.id.tv_sum) //订单金额
        TextView tvSum;
        @Bind(R.id.tv_count) //数量
        TextView tvCount;
        @Bind(R.id.tv_income)//收入
        TextView tvIncome;
        public ViewHolderNormal(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
