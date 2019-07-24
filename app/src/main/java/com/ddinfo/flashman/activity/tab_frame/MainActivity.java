package com.ddinfo.flashman.activity.tab_frame;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.NetworkUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.blankj.utilcode.utils.Utils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.CaptureActivity;
import com.ddinfo.flashman.activity.LoginActivity;
import com.ddinfo.flashman.activity.PerfectUserInfoActivity;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.activity.menu.BoardActivity;
import com.ddinfo.flashman.activity.menu.PaymentActivity;
import com.ddinfo.flashman.activity.menu.PaymentListActivity;
import com.ddinfo.flashman.activity.menu.SettingActivity;
import com.ddinfo.flashman.activity.menu.SupervisingJuniorActivity;
import com.ddinfo.flashman.activity.menu.wallet.WalletActivity;
import com.ddinfo.flashman.application.MyApplication;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.fragment.ContentFragment;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.BindWareHouseEntity;
import com.ddinfo.flashman.models.WareHouseInfoEntity;
import com.ddinfo.flashman.models.params.BindWareHouseParams;
import com.ddinfo.flashman.models.params.DeliveryManIdParams;
import com.ddinfo.flashman.network.SimpleCallBack;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    @Bind(R.id.content_frame)
    FrameLayout contentFrame;
    @Bind(R.id.img_user)        //用户头像
    ImageView imgUser;
    @Bind(R.id.tv_user_name)    //用户名称
    TextView tvUserName;
    @Bind(R.id.ll_r)            //到仓确认布局
    LinearLayout llR;
    @Bind(R.id.activity_main)   //侧滑菜单效果的控件
    DrawerLayout activityMain;
    @Bind(R.id.tv_purse)        //钱包
    TextView tvPurse;
    @Bind(R.id.tv_setting)      //设置
    TextView tvSetting;
    @Bind(R.id.tv_arrive)       //到仓确认
    TextView tvArrive;
    @Bind(R.id.tv_cur_response) //到仓确认状态
    TextView tvCurResponse;
    @Bind(R.id.tv_money)        //待缴金额
    TextView tvMoney;
    @Bind(R.id.tv_payment)      //待交货款
    TextView tvPayment;
    @Bind(R.id.rl_top)
    RelativeLayout rlTop;
    @Bind(R.id.tv_payment_list) //交货款单
    TextView tvPaymentList;
    @Bind(R.id.tv_superior_name)//上级配送员名称
    TextView tvSuperiorName;

    @Bind(R.id.tv_payment_money)//待支付金额
    TextView tvPaymentMoney;

    private WareHouseInfoEntity wareHouseInfoEntity; //仓库信息实体类
    private boolean isLogin; //判断是否登录
    private ContentFragment fragment; //线路总览布局
    private boolean isFirstOpenLeft = true;


    private static final int CODE_BIND_WAREHOUSE = 0x001; //到仓确认/刷新接单仓
    private static final int CODE_BIND_SUPERIOR = 0x002;  //上级配送员
    private static final int CODE_PERFECT = 0x003;        //完善个人信息
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;

    Callback<BaseResponseEntity<WareHouseInfoEntity>> callbackWareHouseInfo = new SimpleCallBack<BaseResponseEntity<WareHouseInfoEntity>>(this) {
        @Override
        public void onSuccess(Call<BaseResponseEntity<WareHouseInfoEntity>> call, Response<BaseResponseEntity<WareHouseInfoEntity>> response) {
            wareHouseInfoEntity = response.body().getData();
            tvCurResponse.setText("当前接单仓：" + wareHouseInfoEntity.getWarehouseAddress());
            tvMoney.setText("待缴金额：" + wareHouseInfoEntity.getSum() + "元");
            tvPaymentMoney.setText("待支付金额：" + wareHouseInfoEntity.getBatchOrderSum() + "元");
            tvSuperiorName.setText(TextUtils.isEmpty(wareHouseInfoEntity.getParentManName())? "暂无上级配送员":wareHouseInfoEntity.getParentManName());
        }

        @Override
        public void onProDialogDismiss() {
            proDialogHelps.removeProDialog();
        }

        @Override
        public void onTokenLose(Call<BaseResponseEntity<WareHouseInfoEntity>> call, Response<BaseResponseEntity<WareHouseInfoEntity>> response) {
            tvCurResponse.setText("当前接单仓：登录后查看");
            tvMoney.setText("待缴金额：登录后查看");
            tvPaymentMoney.setText("待支付金额：登录后查看");
        }
    };
    private Dialog popUpDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.init(this); //初始化工具类
        initViews();
    }

    private void initViews() {
        initContent();
        initLeftMenu();
    }

    private void initData() {
        proDialogHelps.showProDialog("更新接单仓和货款信息...");
        Call<BaseResponseEntity<WareHouseInfoEntity>> callWareHouseInfo = webService.getWareHouseInfo(ExampleConfig.token);
        callWareHouseInfo.enqueue(callbackWareHouseInfo);

    }

    private boolean showPerfectDialog(int confirmState) {
        String message = "";
        if(0==confirmState) {
            message = "您还没有完善个人信息，请完善个人信息";
        } else if(1==confirmState) {
            message = "您提交的信息正在审核中，请联系仓库主管";
        } else if(3==confirmState) {
            message = "您提交的信息未通过审核，请重新完善个人信息";
        } else {
            return true;
        }

        final boolean canGo = 0==confirmState || 3==confirmState;
        String btnMessage = canGo ? "去完善":"知道了";

        AlertDialog.Builder perfectDialog = new AlertDialog.Builder(context);
        perfectDialog.setTitle("提示");
        perfectDialog.setMessage(message);
        perfectDialog.setPositiveButton(btnMessage, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(canGo) {
                    Intent intent = new Intent(MainActivity.this,PerfectUserInfoActivity.class);
                    startActivityForResult(intent, CODE_PERFECT);
                }
                dialog.dismiss();
            }
        });
        perfectDialog.show();
        return false;
    }

    private void initLeftMenu() {
        if (ExampleConfig.token == null || ExampleConfig.token.equals("")) {
            isLogin = false;
            tvUserName.setText("请登录");
            tvCurResponse.setText("当前接单仓：请先登录");
            tvMoney.setText("待缴金额：请先登录");
            tvPaymentMoney.setText("待支付金额：登录后查看");
        } else {
            isLogin = true;
            tvUserName.setText(MyApplication.getSPUtilsInstance().getString(ExampleConfig.LOGIN_PHONE));
            tvCurResponse.setText("当前接单仓：获取中...");
            tvMoney.setText("待缴金额：获取中...");
            tvPaymentMoney.setText("待支付金额：获取中...");
        }
    }

    private void initContent() {
        fragment = ContentFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @OnClick({R.id.tv_purse, R.id.tv_superior, R.id.tv_junior,
            R.id.tv_setting, R.id.tv_arrive, R.id.tv_cur_response,
            R.id.tv_payment, R.id.rl_top, R.id.tv_board, R.id.tv_payment_list})
    public void doClick(View view) {
        if (!isLogin) {
            startActivity(LoginActivity.class);
            return;
        }
        switch (view.getId()) {
            case R.id.tv_purse://我的钱包
                startActivity(WalletActivity.class);
                break;
            case R.id.tv_superior://上级配送员
                requestCodeQRCodePermissions(CODE_BIND_SUPERIOR);
                break;
            case R.id.tv_junior://下级配送员
                startActivity(SupervisingJuniorActivity.class);
                break;
            case R.id.tv_setting://设置
                startActivity(SettingActivity.class);
                break;
            case R.id.tv_arrive://到仓确认
                if(wareHouseInfoEntity==null) {
                    ToastUtils.showShortToast("仓库信息为空！");
                    return;
                }
                boolean isPass = showPerfectDialog(wareHouseInfoEntity.getConfirmState());
                if(isPass) {
                    requestCodeQRCodePermissions(CODE_BIND_WAREHOUSE);
                }
                break;
            case R.id.tv_cur_response://刷新接单仓
                requestCodeQRCodePermissions(CODE_BIND_WAREHOUSE);
                break;
            case R.id.tv_payment://交货款
                startActivity(PaymentActivity.class);
                break;
            case R.id.tv_payment_list: //交货款单
                startActivity(PaymentListActivity.class);
                break;
            case R.id.tv_board: //我的看板
                startActivity(BoardActivity.class);
                break;
            case R.id.rl_top:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_BIND_WAREHOUSE: //到仓确认
                if (resultCode == RESULT_OK) { //RESULT_OK = -1
                    Bundle bundle = data.getExtras();
                    String scanResult = bundle.getString("result");
                    proDialogHelps.showProDialog("正在绑定仓库...");
                    Call<BaseResponseEntity<BindWareHouseEntity>> callBindWareHouse = webService.bindWareHouse(ExampleConfig.token, new BindWareHouseParams(scanResult));
                    callBindWareHouse.enqueue(new SimpleCallBack<BaseResponseEntity<BindWareHouseEntity>>(this) {
                        @Override
                        public void onSuccess(Call<BaseResponseEntity<BindWareHouseEntity>> call, Response<BaseResponseEntity<BindWareHouseEntity>> response) {
                            ExampleConfig.token = response.body().getData().getToken();
                            MyApplication.getSPUtilsInstance().putString(ExampleConfig.TOKEN, ExampleConfig.token);
                            fragment.initDatas();
                            proDialogHelps.showProDialog();
                            Call<BaseResponseEntity<WareHouseInfoEntity>> callWareHouseInfo = webService.getWareHouseInfo(ExampleConfig.token);
                            callWareHouseInfo.enqueue(callbackWareHouseInfo);
                            ToastUtils.showShortToast("仓库绑定成功");
                        }

                        @Override
                        public void onProDialogDismiss() {
                            proDialogHelps.removeProDialog();
                        }
                    });
                }
                break;
            case CODE_BIND_SUPERIOR: //上级配送员
                if (resultCode == RESULT_OK) { //RESULT_OK = -1
                    Bundle bundle = data.getExtras();
                    String scanResult = bundle.getString("result");
                    proDialogHelps.showProDialog("正在绑定上级配送员...");
                    if(scanResult != null){
                        webService.bindParentMan(new DeliveryManIdParams(scanResult)).enqueue(new SimpleCallBack<BaseResponseEntity>(context) {
                            @Override
                            public void onSuccess(Call<BaseResponseEntity> call, Response<BaseResponseEntity> response) {
                                ToastUtils.showShortToast("绑定成功");
                                initData();
                            }
                            @Override
                            public void onProDialogDismiss() {
                                proDialogHelps.removeProDialog();
                            }
                        });
                    }else{
                        ToastUtils.showShortToast("扫码出错！");
                    }
                }
                break;
            case CODE_PERFECT: //完善个人信息
                if(resultCode == RESULT_OK) { //个人信息提交成功
                    initData();
                }
                break;
        }
    }


    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions(int resultCode) {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        } else {
            Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
            intent.putExtra("type", 2);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivityForResult(intent, resultCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
    }

    //显示左侧菜单
    public void showLeftDraw() {
        if (!activityMain.isDrawerOpen(GravityCompat.START)) { //判断左边菜单是否打开
            activityMain.openDrawer(GravityCompat.START); //打开菜单

            if(isFirstOpenLeft) {
                if(wareHouseInfoEntity==null) {
                    ToastUtils.showShortToast("仓库信息为空");
                    return ;
                }
                showPerfectDialog(wareHouseInfoEntity.getConfirmState());
            }
            isFirstOpenLeft = false;
        }
    }

    private long waitTime = 2000;
    private long touchTime = 0;

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - touchTime) >= waitTime) {
            ToastUtils.showShortToastSafe("再按一次退出");
            touchTime = currentTime;
        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        proDialogHelps.removeProDialog();
        initLeftMenu();
        if (NetworkUtils.isConnected()) {
            initData();
        } else {
            ToastUtils.showShortToast("网络不可用");
        }
    }
}
