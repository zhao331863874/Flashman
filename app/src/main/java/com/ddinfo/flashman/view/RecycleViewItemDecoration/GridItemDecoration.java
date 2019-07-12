package com.ddinfo.flashman.view.RecycleViewItemDecoration;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.ddinfo.flashman.R;


/**
 * 块状间隔
 * Created by Fh on 2016/5/24.
 */
public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable spacingLine;
    private int mHeadViewCount = 0;
    private boolean isAddForSide;
    private int spanCount;


    private static final int[] ATTRS = new int[]{
            android.R.attr.listDivider
    };

    /**
     * 自定义间隔填充内容
     *
     * @param spacingLine 填充图形
     */
    public GridItemDecoration(Drawable spacingLine) {
        this.spacingLine = spacingLine;
    }

    /**
     * 默认构造方式，间距为Activity的theme中的Style - listDivider
     *
     * @param context
     */
    public GridItemDecoration(Context context) {
        TypedArray a = context.obtainStyledAttributes(ATTRS);
        spacingLine = (a!=null?a.getDrawable(0): ContextCompat.getDrawable(context, R.drawable.item_decoration_common));
        a.recycle();
    }

    /**
     * 设置头部View的个数，通过layoutManager.setSpanSizeLookup来设置步进
     *
     * @param headViewCount 头部单行Item的个数
     */
    public void setHeadViewCount(int headViewCount) {
        mHeadViewCount = headViewCount;
    }

    /**
     * 是否添加外间隔
     *
     * @param isAddForSide true：添加外间隔 false：不添加外间隔
     */
    public void setIsAddForSide(boolean isAddForSide,int spanCount) {
        this.isAddForSide = isAddForSide;
        this.spanCount = spanCount;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    /**
     * 此处画出水平方向的分割线（非orientation为 Horizontal 时）
     *
     * @param c
     * @param parent
     */
    private void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams childParms = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + childParms.bottomMargin;
            int bottom = top + spacingLine.getIntrinsicHeight();
            int left = child.getLeft() - childParms.leftMargin;
            int right = child.getRight() + childParms.rightMargin;
            spacingLine.setBounds(left, top, right, bottom);
            spacingLine.draw(c);
        }
    }

    /**
     * 此处画出水平方向的分割线（非 orientation为 Vertical 时）
     *
     * @param c
     * @param parent
     */
    private void drawVertical(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams childParms = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getTop() - childParms.topMargin;
            //+spacingLine.getIntrinsicHeight() 是为了填充右下角的空白，如果不加右下角有一个空白四方形
            int bottom = child.getBottom() + childParms.bottomMargin + spacingLine.getIntrinsicHeight();
            int left = child.getRight() + childParms.rightMargin;
            int right = left + spacingLine.getIntrinsicWidth();
            spacingLine.setBounds(left, top, right, bottom);
            spacingLine.draw(c);
        }
    }

    //通过改变Item的 右方 和 下方 的偏移量来填充间隔
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();
        int position = parent.getChildLayoutPosition(view);
        int column = (position-mHeadViewCount) % spanCount; // item column
        //过滤特殊情况
        int left = 0,top = 0,right = 0,bottom = 0;

        if (isHeader(parent, position)) { //头部
            // 仅绘制 下
            outRect.set(0, 0, 0, spacingLine.getIntrinsicHeight());

        } else {
            if (isAddForSide) {

                left = spacingLine.getIntrinsicWidth() - column * spacingLine.getIntrinsicWidth() / spanCount;
                top = 0;
                right = (column + 1) * spacingLine.getIntrinsicWidth() / spanCount;
                bottom = spacingLine.getIntrinsicHeight();
                outRect.set(left, 0 ,right,bottom);

            } else {
//                right = spacingLine.getIntrinsicWidth() - (column + 1) * spacingLine.getIntrinsicWidth() / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
//                if (position >= spanCount) {
//                    top = spacingLine.getIntrinsicHeight(); // item top
//                }
//                left = column * spacingLine.getIntrinsicWidth() / spanCount;
//                outRect.set(left,top,right,bottom);



                //无外间距
                if (isLastRaw(parent, position, spanCount, childCount)) { //最后一行
                    // 仅绘制 右
                    outRect.set(0, 0, spacingLine.getIntrinsicWidth(), 0);
                } else if (isLastColum(parent, position, spanCount, childCount)) { //最后一列
                    // 仅绘制 下
                    outRect.set(0, 0, 0, spacingLine.getIntrinsicHeight());
                }else {
                    //绘制 右 下
                    outRect.set(0, 0, spacingLine.getIntrinsicWidth(), spacingLine.getIntrinsicHeight());
                }
//                outRect.set(0, 0, spacingLine.getIntrinsicWidth(), spacingLine.getIntrinsicHeight());
            }
        }
    }

    private boolean isHeader(RecyclerView parent, int pos) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if (pos < mHeadViewCount)// 是头部
            {
                return true;
            }
        }
        return false;
    }

    private boolean isFirstColum(RecyclerView parent, int pos, int spanCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos - mHeadViewCount) % spanCount == 0)// 如果第一列
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 通过LayoutManager获取到列数
     *
     * @return 每一行的child数量
     */
    private int getSpanCount(RecyclerView parent) {
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {

            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();

        } else if (layoutManager instanceof StaggeredGridLayoutManager) {

            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();

        }
        return spanCount;
    }

    private boolean isLastColum(RecyclerView parent, int pos, int spanCount,
                                int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1 - mHeadViewCount) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
            {
                return true;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1 - mHeadViewCount) % spanCount == 0)// 如果是最后一列，则不需要绘制右边
                {
                    return true;
                }
            } else {
                childCount = childCount - childCount % spanCount;
                if (pos - mHeadViewCount >= childCount)// 如果是最后一列，则不需要绘制右边
                    return true;
            }
        }
        return false;
    }

    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount,
                              int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            childCount = childCount - childCount % spanCount;
            if (pos - mHeadViewCount >= childCount)// 如果是最后一行，则不需要绘制底部
                return true;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行，则不需要绘制底部
                if (pos - mHeadViewCount >= childCount)
                    return true;
            } else
            // StaggeredGridLayoutManager 且横向滚动
            {
                // 如果是最后一行，则不需要绘制底部
                if ((pos - mHeadViewCount + 1) % spanCount == 0) {
                    return true;
                }
            }
        }
        return false;
    }

}
