package com.ddinfo.flashman.activity.base_frame;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.network.VariationInterface;
import com.ddinfo.flashman.network.WebConect;
import com.ddinfo.flashman.network.WebService;
import com.ddinfo.flashman.utils.ExitUtil;
import com.ddinfo.flashman.utils.ProDialogHelps;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by weitf on 16/4/6.
 */
public abstract class BaseActivity extends AppCompatActivity implements NavigationBarInterface, VariationInterface {

    public static final int REQUESTCODE = 145;
    public static final int REQUESTCODE1 = 144;
    public static final int REMOVEPRODIALOG = 11110;
    public static final int SHOWPRODIALOG = 11111;
    public static final int SHOWUPLOADDIALOG = 11112;
    public static final int SHOW_EDIT_CHANGE_DIALOG = 11113;
    public static final int SHOW_DEL_PHOTOS_DIALOG = 11114;
    public static final int SHOW_SEARCH_DIALOG = 11115;
    public static final int SHOW_LOGINOUT_DIALOG = 11116;
    public static final int SHOW_LOGIN_PRODIALOG = 11117;
    public static final int SHOW_REGISTER_PRODIALOG = 11119;
    public static final int SHOW_EXCHANGE_PRODIALOG = 11120;
    public static final int SHOW_BIND_PRODIALOG = 11121;
    public static final int SHOW_WITHDRAW_PRODIALOG = 11122;
    public static final int UNBIND_PRODIALOG = 11123;
    public static final int SHOW_SUBMIT_ORDER_PRODIALOG = 11124;
    public static final int SHOW_SING_IN_PRODIALOG = 11125;
    public static final int SHOW_SING_OUT_PRODIALOG = 11126;
    public static final int GETAUTHCODE = 11127;
    public static final int NETWORKERROR = 11118;
    public static final int NOT_CHECK_STORE_ERROR = 111199;
    public static final int ORDER_TIMER_TASK_START = 188888;
    public static final int ORDER_TIMER_TASK_CANCEL = 188889;
    public static final String TAG = "default_tag";
    public Context context;
    public Activity activity;
    public WebService webService = null;
    public ProgressDialog progressDialog = null;
    public ImageButton leftBtn; //上一步按钮
    public ImageButton rightBtn;//下一步按钮
    public Button rightButton;
    public TextView headerTitleTv; //标题抬头
    public ProDialogHelps proDialogHelps; //加载框

    //    @Nullable
//    @Bind(R.id.multiStateView)
//    public MultiStateView multiStateView;
    @Nullable
    @Bind(R.id.rel_setting)
    public RelativeLayout relSetting;
    public Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NETWORKERROR:
                    ToastUtils.showShortToastSafe("网络不可用");
                    break;
                case NOT_CHECK_STORE_ERROR:
                    ToastUtils.showShortToastSafe(" 暂无已签到店铺");
                    break;
                case REMOVEPRODIALOG:
                    try {
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SHOWPRODIALOG:
                    progressDialog = ProgressDialog.show(context, null, "加载中，请稍候...", false, true);
                    break;
                case SHOW_SING_IN_PRODIALOG:
                    progressDialog = ProgressDialog.show(context, null, "签到中，请稍候...", false, true);
                    break;
                case SHOW_SING_OUT_PRODIALOG:
                    progressDialog = ProgressDialog.show(context, null, "签退中，请稍候...", false, true);
                    break;
                case SHOW_SUBMIT_ORDER_PRODIALOG:
                    progressDialog = ProgressDialog.show(context, null, "订单提交中，请稍候...", false, true);
                    break;
                case SHOW_LOGIN_PRODIALOG:
                    progressDialog = ProgressDialog.show(context, null, "登录中，请稍候...", false, true);
                    break;
                case SHOW_REGISTER_PRODIALOG:
                    progressDialog = ProgressDialog.show(context, null, "注册中，请稍候...", false, true);
                    break;
                case SHOWUPLOADDIALOG:
                    progressDialog = ProgressDialog.show(context, null, "创建中，请稍候...", false, true);
                    break;
                case SHOW_EDIT_CHANGE_DIALOG:
                    progressDialog = ProgressDialog.show(context, null, "修改中，请稍候...", false, true);
                    break;
                case SHOW_DEL_PHOTOS_DIALOG:
                    progressDialog = ProgressDialog.show(context, null, "删除中，请稍候...", false, true);
                    break;
                case SHOW_SEARCH_DIALOG:
                    progressDialog = ProgressDialog.show(context, null, "搜索中，请稍候...", false, true);
                    break;
                case SHOW_LOGINOUT_DIALOG:
                    progressDialog = ProgressDialog.show(context, null, "退出中，请稍候...", false, true);
                    break;
                case SHOW_EXCHANGE_PRODIALOG:
                    progressDialog = ProgressDialog.show(context, null, "兑换中，请稍候...", false, true);
                    break;
                case SHOW_BIND_PRODIALOG:
                    progressDialog = ProgressDialog.show(context, null, "微信授权中，请稍候...", false, true);
                    break;
                case UNBIND_PRODIALOG:
                    progressDialog = ProgressDialog.show(context, null, "解除绑定中，请稍候...", false, true);
                    break;
                case SHOW_WITHDRAW_PRODIALOG:
                    progressDialog = ProgressDialog.show(context, null, "提现中，请稍候...", false, true);
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
//    private LoginParams loginParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());//把设置布局文件的操作交给继承的子类
        ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= 14) {
            parentView.setFitsSystemWindows(true);
        }
        leftBtn = (ImageButton) findViewById(R.id.left_button);
        rightButton = (Button) findViewById(R.id.rightBtn);
        headerTitleTv = (TextView) findViewById(R.id.header_name);
        rightBtn = (ImageButton) findViewById(R.id.right_button);
        if (leftBtn != null) {
            leftBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    onLeftBtnClicked();

                }
            });
        }
        if (rightBtn != null) {
            rightBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    onRightBtnClicked();
                }
            });
        }
        context = this;
        activity = this;
        ExitUtil.getInstance().addActivity(this);
        ButterKnife.bind(this); //注解框架,在Activity中绑定ButterKnife
//        String email = MyApplication.getSPUtilsInstance().getString(ExampleConfig.LOGIN_EMAIL);
//        String pwd =MyApplication.getSPUtilsInstance().getString(ExampleConfig.LOGIN_PASSWORD);
//        loginParams = new LoginParams(email, pwd);
        webService = WebConect.getInstance().getmWebService();
        proDialogHelps = new ProDialogHelps(this);
    }


    /**
     * 返回当前Activity布局文件的id
     *
     * @return
     */
    abstract protected int getLayoutResId();

    /**
     * 设置titlebar 内容
     *
     * @param title
     */
    protected void setTitle(String title) {
        if (title == null) {
            return;
        }
        headerTitleTv.setText(title);
    }

    /**
     * 设置右上方按钮内容
     *
     * @param text
     */
    protected void setRightBtnText(String text) {
        if (rightButton == null) {
            return;
        }
        rightButton.setVisibility(View.VISIBLE);
        rightButton.setText(text);
    }

    @Override
    public void onRightBtnClicked() {

    }

    @Override
    public void onLeftBtnClicked() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        ExitUtil.getInstance().remove(this);
    }


    @Override
    public void updateDevice() {

    }

    @Override
    public void refreshToken() {
//        Call<TokenEntity> callLogin=webService.login(loginParams);
//        callLogin.enqueue(new Callback<TokenEntity>() {
//            @Override
//            public void onResponse(Call<TokenEntity> call, Response<TokenEntity> response) {
//                if (response.code() == 200 && response.body()!=null&&response.body().getStatus()==1) {
//                    ExampleConfig.token = response.body().getToken();
//                    MyApplication.getSPUtilsInstance().putString(ExampleConfig.TOKEN, ExampleConfig.token);
//                    LogUtils.d(TAG, ExampleConfig.token);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<TokenEntity> call, Throwable t) {
//
//            }
//        });
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
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
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
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.push_left_in,
                R.anim.push_left_out);
    }

    @Override
    protected void onPause() {
        MobclickAgent.onPause(this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        MobclickAgent.onResume(this);
        super.onResume();
    }

//    protected void setEmptyOrError(int type) {
//        if (multiStateView != null) {
//            if (type == MultiStateView.VIEW_STATE_EMPTY)
//                multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
//            else if (type == MultiStateView.VIEW_STATE_ERROR) {
//                multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
//                if (relSetting != null) {
//                    relSetting.setVisibility(View.VISIBLE);
//                }
//            } else if (type == MultiStateView.VIEW_STATE_CONTENT) {
//                multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
//            }
//        }
//    }
}
