package com.ddinfo.flashman.activity.menu;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 交货款单界面
 */
public class PaymentListActivity extends BaseActivity {

    @Bind(R.id.left_button) //左返回按钮
    ImageButton leftButton;
    @Bind(R.id.header_name) //标题名称
    TextView headerName;
    @Bind(R.id.right_button)
    ImageButton rightButton;
    @Bind(R.id.rightBtn)
    Button rightBtn;
    @Bind(R.id.tab_top)     //选项卡切换的效果布局
    TabLayout tabTop;
    @Bind(R.id.vp_bottom)   //视图翻页工具
    ViewPager vpBottom;
    @Bind(R.id.activity_task_all_list)
    LinearLayout activityTaskAllList;

    private String[] tabTopData;
    private FragAdapter fragAdapter;
    private List<String> tabTopDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        setTitle("交货款单");
        tabTop.setTabMode(TabLayout.MODE_FIXED);//TabLayout等比填充
        LinearLayout linearLayout = (LinearLayout) tabTop.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE); //在子视图之间显示分割线
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this,
                R.drawable.vp_line)); //设置分割线Drawable
        tabTop.setTabTextColors(ContextCompat.getColor(this, R.color.light_black_color), ContextCompat.getColor(this, R.color.text_color_blue));//默认字体颜色，选中字体颜色
        fragAdapter = new FragAdapter(getSupportFragmentManager());
    }

    private void initData() {
        tabTopData = getResources().getStringArray(R.array.tab_string_payment_list);
        for (int i = 0; i <tabTopData.length; i++) {
            tabTopDatas.add(tabTopData[i]);
        }

        vpBottom.setAdapter(fragAdapter);
        //TabLayout 与 Viewpager 联动
        tabTop.setupWithViewPager(vpBottom);//设置位置
    }

    private void initListener() {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_payment_list;
    }

    public void changeTab(int position){
        vpBottom.setCurrentItem(position);
    }

    public class FragAdapter extends FragmentPagerAdapter {

        public FragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PaymentListFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return tabTopDatas.size();
        }

        //TabLayout内部集成了监听Fragment的此方法
        //这里返回的字符也是tab的字符
        @Override
        public CharSequence getPageTitle(int position) {
            return tabTopDatas.get(position);
        }
    }
}
