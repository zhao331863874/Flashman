package com.ddinfo.flashman.activity.menu.wallet;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.application.MyApplication;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.CallPhoneEntity;
import com.ddinfo.flashman.network.SimpleCallBack;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * 联系客服界面
 */
public class CallServiceActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    @Bind(R.id.left_button) //左返回按钮
    ImageButton leftButton;
    @Bind(R.id.header_name) //标题抬头
    TextView headerName;
    @Bind(R.id.right_button)
    ImageButton rightButton;
    @Bind(R.id.rightBtn)
    Button rightBtn;
    @Bind(R.id.tv_call_phone) //客服电话
    TextView tvCallPhone;
    @Bind(R.id.tv_email)      //客服邮箱
    TextView tvEmail;
    @Bind(R.id.activity_call_service)
    LinearLayout activityCallService;
    private String phoneNumber;

    private static final int REQUEST_CODE_PHONE_PERMISSIONS = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();

    }

    private void initView() {
        setTitle("联系客服");
    }

    private void initData() {
        proDialogHelps.showProDialog("正在获取客服电话和邮箱...");
        Call<BaseResponseEntity<CallPhoneEntity>> callPhone = webService.callPhone(ExampleConfig.token);
        callPhone.enqueue(new SimpleCallBack<BaseResponseEntity<CallPhoneEntity>>(this) {
            @Override
            public void onSuccess(Call<BaseResponseEntity<CallPhoneEntity>> call, Response<BaseResponseEntity<CallPhoneEntity>> response) {
                ExampleConfig.telephone = response.body().getData().getTelephone();
                ExampleConfig.email =response.body().getData().getEmail();
                MyApplication.getSPUtilsInstance().putString(ExampleConfig.TELEPHONE, ExampleConfig.telephone); //存储客服电话
                MyApplication.getSPUtilsInstance().putString(ExampleConfig.EMAIL, ExampleConfig.email); //存储客服邮箱
                tvCallPhone.setText("电话："+ExampleConfig.telephone);
                tvEmail.setText("邮箱："+ExampleConfig.email);
            }
            @Override
            public void onProDialogDismiss() {
                proDialogHelps.removeProDialog();
            }
        });
    }

    private void initListener() {

    }

    @OnClick({R.id.tv_call_phone, R.id.tv_email})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_call_phone: //点击客服电话
                requestCodeQRCodePermissions();
                break;
            case R.id.tv_email:

                break;
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_call_service;
    }

    @AfterPermissionGranted(REQUEST_CODE_PHONE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CALL_PHONE};
        if (!EasyPermissions.hasPermissions(context, perms)) {
            EasyPermissions.requestPermissions(this, "联系客服需要通话权限", REQUEST_CODE_PHONE_PERMISSIONS, perms);
        } else {
            Intent intent = new Intent();

            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
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
}
