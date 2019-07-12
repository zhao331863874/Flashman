package com.ddinfo.flashman.activity.base_frame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ddinfo.flashman.R;
import com.ddinfo.flashman.network.VariationInterface;
import com.ddinfo.flashman.network.WebConect;
import com.ddinfo.flashman.network.WebService;
import com.ddinfo.flashman.utils.ProDialogHelps;
import com.kennyc.view.MultiStateView;

import butterknife.ButterKnife;

//import com.ddinfo.ddmall.view.UsersSpecialDialog;

/**
 * Created by weitf on 2016/7/22.
 */
public abstract class BaseFragment extends Fragment implements VariationInterface {
    protected boolean isVisible = false; //界面是否可见
    public WebService webService = null;
    public ImageButton leftBtn; //左点击按钮
    public ImageButton rightBtn;//右点击按钮
    public Button rightButton;
    public TextView headerTitleTv; //标题抬头
    public ProDialogHelps proDialogHelps;
    /**
     * Acitivity对象
     **/
    protected Activity activity;
    /**
     * 上下文
     **/
    protected Context context;
    /**
     * 当前显示的内容
     **/
    protected View rootView;
//    public LoginParams loginParams;
    public BaseFragment() {
        // Required empty public constructor
    }

    /**
     * 获得全局的，防止使用getActivity()为空
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { //创建的时候调用
        rootView = LayoutInflater.from(activity).inflate(getLayoutId(), container, false);
        webService = WebConect.getInstance().getmWebService();
        proDialogHelps = new ProDialogHelps(context);
        leftBtn = (ImageButton) rootView.findViewById(R.id.left_button);
        rightButton = (Button) rootView.findViewById(R.id.rightBtn);
        headerTitleTv = (TextView) rootView.findViewById(R.id.header_name);
        rightBtn = (ImageButton)rootView.findViewById(R.id.right_button);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) { //在onCreateView后被触发的事件
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initLoginParams();
        initView(view, savedInstanceState);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) { //当Fragment所在的Activity被启动完成后回调该方法
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) { //当fragment被用户可见时
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible() { //界面可见
        lazyResumeLoad();
    }

    protected abstract void lazyResumeLoad();

    protected void onInvisible() { //界面不可见
    }

    /**
     * 该抽象方法就是 onCreateView中需要的layoutID
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 该抽象方法就是 初始化view
     *  在onViewCreate时被调用
     * @param view
     * @param savedInstanceState
     */
    protected abstract void initView(View view, Bundle savedInstanceState);

    /**
     * 执行数据的加载
     */
    protected abstract void initDatas();

    /**
     * 监听
     */
    protected abstract void initListener();

    @Override
    public void updateDevice() {

    }
    /**
     * 登录参数
     */
    protected void initLoginParams() {
//        String email = MyApplication.getSPUtilsInstance().getString(ExampleConfig.LOGIN_EMAIL);
//        String pwd =MyApplication.getSPUtilsInstance().getString(ExampleConfig.LOGIN_PASSWORD);
//        loginParams = new LoginParams(email, pwd);
    }
    /**
     * 设置titlebar 内容
     * @param title
     */
    protected void setTitle(String title) {
        if (title == null) {
            return;
        }
        headerTitleTv.setText(title);
    }

    @Override
    public void refreshToken() {

    }

    /**
     * 通过Class跳转界面
     **/
    protected void startActivity(Class<?> cls) {
        startActivity(cls, null);
    }

    /**
     * 通过Action跳转界面
     **/
    protected void startActivity(String action) {
        startActivity(action, null);
    }


    /**
     * 含有Bundle通过Class跳转界面
     **/
    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
//        activity.overridePendingTransition(R.anim.push_left_in,
//                R.anim.push_left_out);
    }

    /**
     * 含有Bundle通过Action跳转界面
     **/
    protected void startActivity(String action, Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction(action);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        context.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }

    protected void setEmptyOrError(View parent,int type) {
        MultiStateView multiStateView = null; //多状态页面布局
        RelativeLayout relSetting =null;

        if(parent instanceof MultiStateView){
            multiStateView = (MultiStateView) parent;
        }
        relSetting = (RelativeLayout) parent.findViewById(R.id.rel_setting); //网络请求失败布局
        if (multiStateView != null) {
            if (type == MultiStateView.VIEW_STATE_EMPTY)
                multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            else if (type == MultiStateView.VIEW_STATE_ERROR) {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR); //切换MultiStateView布局
                if (relSetting != null) {
                    relSetting.setVisibility(View.VISIBLE);
                }
            } else if (type == MultiStateView.VIEW_STATE_CONTENT) {
                multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            }
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
