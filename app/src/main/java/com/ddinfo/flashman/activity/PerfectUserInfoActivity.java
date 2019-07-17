package com.ddinfo.flashman.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.application.MyApplication;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.models.AttachUserInfoEntity;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.network.SimpleCallBack;
import com.ddinfo.flashman.utils.GsonTools;
import com.ddinfo.flashman.utils.Utils;

import java.io.IOException;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 完善用户信息
 */
public class PerfectUserInfoActivity extends BaseActivity {
  @Bind(R.id.tint) //个人信息审核状态
  TextView tint;
  @Bind(R.id.real_name) //真实姓名
  EditText realName;
  @Bind(R.id.ID_card)   //身份证号
  EditText IDCard;
  @Bind(R.id.delivery_address) //家庭住址
  EditText deliveryAddress;
  @Bind(R.id.urgency_name)   //紧急联系人姓名
  EditText urgencyName;
  @Bind(R.id.urgency_phone)  //紧急联系人手机
  EditText urgencyPhone;
  @Bind(R.id.urgency_ship)   //紧急联系人关系
  EditText urgencyShip;
  @Bind(R.id.bank_name)      //开户银行

  EditText bankName;
  @Bind(R.id.bank_count_user)//户主姓名
  EditText bankCountUser;
  @Bind(R.id.bank_count_ID)  //银行卡号
  EditText bankCountID;
  @Bind(R.id.submit) //提交信息
  Button submit;
  private AttachUserInfoEntity userInfoEntity = new AttachUserInfoEntity();

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_perfect_user_info;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setTitle("完善个人信息");
    initUserInfo();
  }

  @OnClick(R.id.submit) //提交按钮监听
  void submitUpload(){
    try {
      AttachUserInfoEntity entity = getDataFromView();
      if(!verifyForm(entity)){ //判断信息格式是否验证通过
        return;
      }
      Call<BaseResponseEntity> uploadCall = webService.uploadAttachUserInfo(ExampleConfig.token, entity);
      uploadCall.enqueue(new SimpleCallBack<BaseResponseEntity>(this) {

                           @Override
                           public void onSuccess(Call<BaseResponseEntity> call, Response<BaseResponseEntity> response) {
                             ToastUtils.showShortToast("提交成功！");
                             setResult(RESULT_OK);
                             finish();
                           }

                           @Override
                           public void onProDialogDismiss() {

                           }
                         }
      );
    }catch (Exception e) {
      ToastUtils.showShortToast("填写内容异常！");
    }
  }

  private void initHint() { //初始化个人信息审核状态
    tint.setText(userInfoEntity.getConfirmStateDesc());
  }

  private void initUserInfo(){
    Call<BaseResponseEntity<AttachUserInfoEntity>> getUserInfoCall = webService.getAttachUserInfo(ExampleConfig.token);
    getUserInfoCall.enqueue(new SimpleCallBack<BaseResponseEntity<AttachUserInfoEntity>>(this) {
      @Override
      public void onSuccess(Call<BaseResponseEntity<AttachUserInfoEntity>> call, Response<BaseResponseEntity<AttachUserInfoEntity>> response) {
        userInfoEntity = response.body().getData();
        if(userInfoEntity==null) return;
        initHint();
        initViews();
        initOnlyShow(userInfoEntity.onlyShow());
      }

      @Override
      public void onProDialogDismiss() {

      }
    });
  }

  private void initViews() {
    realName.setText(userInfoEntity.realName);
    IDCard.setText(userInfoEntity.IDCard);
    deliveryAddress.setText(userInfoEntity.deliveryAddress);
    bankName.setText(userInfoEntity.bankName);
    bankCountID.setText(userInfoEntity.bankCountID);
    bankCountUser.setText(userInfoEntity.bankCountUser);
    urgencyName.setText(userInfoEntity.urgencyName);
    urgencyPhone.setText(userInfoEntity.urgencyPhone);
    urgencyShip.setText(userInfoEntity.urgencyShip);
  }

  private void initOnlyShow(boolean isOnlyShow) {
    if(!isOnlyShow) return;
    realName.setEnabled(false); //设置不可编辑状态
    if(!TextUtils.isEmpty(userInfoEntity.realName)){
      realName.setBackgroundColor(Color.TRANSPARENT);
    }

    IDCard.setEnabled(false);
    if(!TextUtils.isEmpty(userInfoEntity.IDCard)){
      IDCard.setBackgroundColor(Color.TRANSPARENT);
    }

    deliveryAddress.setEnabled(false);
    if(!TextUtils.isEmpty(userInfoEntity.deliveryAddress)){
      deliveryAddress.setBackgroundColor(Color.TRANSPARENT);
    }

    bankName.setEnabled(false);
    if(!TextUtils.isEmpty(userInfoEntity.bankName)){
      bankName.setBackgroundColor(Color.TRANSPARENT);
    }

    bankCountID.setEnabled(false);
    if(!TextUtils.isEmpty(userInfoEntity.bankCountID)){
      bankCountID.setBackgroundColor(Color.TRANSPARENT);
    }

    bankCountUser.setEnabled(false);
    if(!TextUtils.isEmpty(userInfoEntity.bankCountUser)){
      bankCountUser.setBackgroundColor(Color.TRANSPARENT);
    }

    urgencyName.setEnabled(false);
    if(!TextUtils.isEmpty(userInfoEntity.urgencyName)){
      urgencyName.setBackgroundColor(Color.TRANSPARENT);
    }

    urgencyPhone.setEnabled(false);
    if(!TextUtils.isEmpty(userInfoEntity.urgencyPhone)){
      urgencyPhone.setBackgroundColor(Color.TRANSPARENT);
    }

    urgencyShip.setEnabled(false);
    if(!TextUtils.isEmpty(userInfoEntity.urgencyShip)){
      urgencyShip.setBackgroundColor(Color.TRANSPARENT);
    }
    submit.setVisibility(View.GONE); //隐藏提交信息
  }

  private AttachUserInfoEntity getDataFromView() { //获取个人信息
    AttachUserInfoEntity entity = new AttachUserInfoEntity();
    entity.realName = realName.getText().toString().trim();
    entity.IDCard = IDCard.getText().toString().trim();
    entity.deliveryAddress = deliveryAddress.getText().toString().trim();
    entity.bankName = bankName.getText().toString().trim();
    entity.bankCountID = bankCountID.getText().toString().trim();
    entity.bankCountUser = bankCountUser.getText().toString().trim();
    entity.urgencyName = urgencyName.getText().toString().trim();
    entity.urgencyPhone = urgencyPhone.getText().toString().trim();
    entity.urgencyShip = urgencyShip.getText().toString().trim();
    return entity;
  }

  private boolean verifyForm(AttachUserInfoEntity entity) { //信息格式验证
    if(TextUtils.isEmpty(entity.realName)){
      ToastUtils.showShortToast("真实姓名不能为空！");
      return false;
    }
    if(TextUtils.isEmpty(entity.IDCard)){
      ToastUtils.showShortToast("身份证号不能为空！");
      return false;
    }
    if(!Utils.checkIDCard(entity.IDCard)) {
      ToastUtils.showShortToast("请正确填写身份证号码！");
      return false;
    }
    if(TextUtils.isEmpty(entity.deliveryAddress)){
      ToastUtils.showShortToast("家庭住址不能为空！");
      return false;
    }
    if(TextUtils.isEmpty(entity.urgencyName)){
      ToastUtils.showShortToast("紧急联系人不能为空！");
      return false;
    }
    if(TextUtils.isEmpty(entity.urgencyPhone)){
      ToastUtils.showShortToast("紧急联系人手机不能为空！");
      return false;
    }
    if(!Utils.checkPhone(entity.urgencyPhone)) {
      ToastUtils.showShortToast("请正确填写紧急联系人手机号！");
      return false;
    }
    if(TextUtils.isEmpty(entity.urgencyShip)){
      ToastUtils.showShortToast("紧急联系人关系不能为空！");
      return false;
    }
    if(!TextUtils.isEmpty(entity.bankCountID)
            &&(entity.bankCountID.length()<16||entity.bankCountID.length()>19)) {
      ToastUtils.showShortToast("请正确输入银行卡号！");
      return false;
    }
    return true;
  }
}
