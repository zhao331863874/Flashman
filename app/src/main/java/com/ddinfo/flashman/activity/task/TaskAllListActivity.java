package com.ddinfo.flashman.activity.task;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.CaptureActivity;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.activity.base_frame.BaseFragment;
import com.ddinfo.flashman.constant.ExampleConfig;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 我的任务界面
 */
public class TaskAllListActivity extends BaseActivity
        implements EasyPermissions.PermissionCallbacks {

    @Bind(R.id.left_button)
    ImageButton leftButton;  //返回按钮
    @Bind(R.id.header_name)
    TextView headerName;     //标题抬头
    @Bind(R.id.right_button)
    ImageButton rightButton; //右侧按钮图片
    @Bind(R.id.rightBtn)
    Button rightBtn;         //扫一扫按钮
    @Bind(R.id.tab_top)
    TabLayout tabTop;         //选项卡切换的效果布局
    @Bind(R.id.vp_bottom)
    ViewPager vpBottom;       //视图翻页工具
    @Bind(R.id.activity_task_all_list)
    LinearLayout activityTaskAllList;

    private String[] tabTopData; //选项卡子选项名称
    private FragAdapter fragAdapter; //选项卡碎片适配器
    private List<String> tabTopDatas = new ArrayList<>();

    private static final int REQUEST_SCAN_CODE = 0x33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        initDatas();
        initListener();
    }

    private void initViews() {
        setTitle("我的任务");
        tabTop.setTabMode(TabLayout.MODE_FIXED);//TabLayout等比填充
        LinearLayout linearLayout = (LinearLayout) tabTop.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE); //设置分割线
        linearLayout.setDividerDrawable(ContextCompat.getDrawable(this, R.drawable.vp_line)); //设置分割线的样式
        tabTop.setTabTextColors(ContextCompat.getColor(this, R.color.light_black_color),
                ContextCompat.getColor(this, R.color.text_color_blue));//默认字体颜色黑色，选中字体颜色蓝色
        fragAdapter = new FragAdapter(getSupportFragmentManager());
        rightBtn.setText("扫一扫");
        rightBtn.setVisibility(View.VISIBLE);
    }

    private void initDatas() {
        tabTopData = getResources().getStringArray(R.array.tab_string_all_task); //构造选项卡子选项名称
        for (int i = 0; i < tabTopData.length; i++) {
            tabTopDatas.add(tabTopData[i]);
        }

        vpBottom.setAdapter(fragAdapter);
        //TabLayout 与 Viewpager 联动
        tabTop.setupWithViewPager(vpBottom);//设置位置
    }

    private void initListener() {
        rightBtn.setOnClickListener(new View.OnClickListener() { //点击扫一扫监听
            @Override
            public void onClick(View view) {
                requestCodeQRCodePermissions();
            }
        });
    }

    @AfterPermissionGranted(ExampleConfig.PERCAMERA)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}; //相机权限、存储权限
        if (!EasyPermissions.hasPermissions(context, perms)) { //检查perms权限是否开启
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", ExampleConfig.PERCAMERA,
                    perms);
        } else {
            Intent intent = new Intent(context, CaptureActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Bundle bundle = new Bundle();
            bundle.putInt(ExampleConfig.TYPE_CAMERA, 10);
            bundle.putInt("type", 1);
            intent.putExtras(bundle);
            startActivityForResult(intent, REQUEST_SCAN_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_SCAN_CODE:
                if (resultCode == RESULT_OK) { //RESULT_OK = -1
                    Bundle bundle = data.getExtras();
                    String scanResult = bundle.getString("result");
                    System.out.println("TaskAllListActivity.onActivityResult" + "    " + scanResult);
                    bundle.putInt(ExampleConfig.TASK_DETAIL_TYPE, 3);
                    bundle.putString(ExampleConfig.ID, scanResult);
                    startActivity(TaskDetailActivity.class, bundle);
                }
                break;
        }
    }
    //处理授权回调
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    public void changeTab(int position) {
        vpBottom.setCurrentItem(position);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_task_all_list;
    }

    public class FragAdapter extends FragmentPagerAdapter {
        private int position;

        public FragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            BaseFragment fragment = null;
            switch (position) {
                case 0:
                    setPosition(position);
                    fragment = AllocationTaskListFragment.getInstance();
                    break;
                case 1:
                case 2:
                case 3:
                    setPosition(position);
                    fragment = TaskAllListFragment.instance(tabTopDatas.get(position), position);
                    break;
                default:
                    break;
            }
            return fragment;
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

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}
