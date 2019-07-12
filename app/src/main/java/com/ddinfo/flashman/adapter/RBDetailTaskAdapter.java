package com.ddinfo.flashman.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddinfo.flashman.R;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.impl.OnItemClickListenerRv;
import com.ddinfo.flashman.models.ReturnBackGroupOrderEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 退货取货商品展示适配器
 */
public class RBDetailTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private OnItemClickListenerRv itemListener;
    private List<ReturnBackGroupOrderEntity> mListData = new ArrayList<>();
    private OnEditTextInputClickListener onEditTextInputClickListener;
    private boolean isEmpty = false; //是否为空数据
    private boolean isLoadAll = false; //是否加载完成

    public void setmListData(List<ReturnBackGroupOrderEntity> mListData) {
        this.mListData = mListData;
        notifyDataSetChanged();
    }

    public void setEmpty(boolean empty) {
        isEmpty = empty;
    }

    public void setLoadAll(boolean loadAll) {
        isLoadAll = loadAll;
    }

    public RBDetailTaskAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case ExampleConfig.VIEWHOLDER_NORMAL:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_return_back_task, parent, false); //商品展示布局
                holder = new ViewHolderNormal(view);
                break;
            case ExampleConfig.VIEWHOLDER_FOOT:
                View viewFooter = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer_load_more, null); //正在加载布局
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
            final ViewHolderNormal viewHolderNormal = (ViewHolderNormal) holder;
            ReturnBackGroupOrderEntity backGroupOrder = mListData.get(position);
            viewHolderNormal.tvGoodsName.setText(backGroupOrder.getGoodName()); //货品名称
            viewHolderNormal.tvGoodsProDate.setText(com.ddinfo.flashman.utils.Utils.getWantDate(backGroupOrder.getProduceTime(), com.ddinfo.flashman.utils.Utils.PATTERN_STANDARD10H)); //生产日期
            viewHolderNormal.tvGoodsBackCount.setText(String.valueOf(backGroupOrder.getOrgAmount())); //退货数量
            viewHolderNormal.editGoodsRealBackCount.setText(String.valueOf(backGroupOrder.getRealAmount())); //实际退货数量（可更改）
            viewHolderNormal.itemView.setTag(position);
            viewHolderNormal.editGoodsRealBackCount.setTag(position);
            viewHolderNormal.editGoodsRealBackCount.addTextChangedListener(new TextWatcher() { //点击实际退货数量回调
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) { //输入完成回调
                    if ((int) viewHolderNormal.editGoodsRealBackCount.getTag() == position && onEditTextInputClickListener != null) {
                        onEditTextInputClickListener.onEditInputListener(position, s.toString());
                    }
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
        return position == mListData.size() ? ExampleConfig.VIEWHOLDER_FOOT : ExampleConfig.VIEWHOLDER_NORMAL;
    }

    class ViewHolderNormal extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_goods_name) //货品名称
        TextView tvGoodsName;
        @Bind(R.id.tv_goods_pro_date) //生产日期
        TextView tvGoodsProDate;
        @Bind(R.id.tv_goods_back_count) //退货数量
        TextView tvGoodsBackCount;
        @Bind(R.id.edit_goods_real_back_count) //真实退货数量（可更改）
        EditText editGoodsRealBackCount;
        @Bind(R.id.tv_goods_real_back_count)
        TextView tvGoodsRealBackCount;

        ViewHolderNormal(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


    class ViewHolderFoot extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_load_more)   //正在加载
        TextView tvLoadMore;
        @Bind(R.id.rl_load_more)
        RelativeLayout rlLoadMore;

        public ViewHolderFoot(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setOnEditTextInputListener(OnEditTextInputClickListener onEditTextInputListener) {
        this.onEditTextInputClickListener = onEditTextInputListener;
    }

    public interface OnEditTextInputClickListener {
        void onEditInputListener(int position, String count);
    }
}
