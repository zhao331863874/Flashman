package com.ddinfo.flashman.activity.task;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.params.GoodsGetParams;
import com.ddinfo.flashman.network.SimpleCallBack;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Response;

public class WriteQRCodeActivity extends BaseActivity {

  @Bind(R.id.et_write_code) EditText etWriteCode;
  @Bind(R.id.tv_enter) TextView tvEnter;

  private String QRcode;
  // 0 取货  1 扫一扫
  private int type;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    type = getIntent().getExtras().getInt("type", 0);
    initView();
  }

  private void initView() {
    setTitle("输入条形码");
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_write_qrcode;
  }

  @OnClick(R.id.tv_enter)
  public void onClick(View view) {
    QRcode = etWriteCode.getText().toString().trim();
    switch (view.getId()) {
      case R.id.tv_enter:
        if (type == 0) {
          proDialogHelps.showProDialog();
          Call<BaseResponseEntity> callGoodsGet = webService.goodsGet(ExampleConfig.token, new GoodsGetParams(QRcode));
          callGoodsGet.enqueue(new SimpleCallBack<BaseResponseEntity>(this) {
            @Override
            public void onSuccess(Call<BaseResponseEntity> call, Response<BaseResponseEntity> response) {
              ToastUtils.showShortToast("取货成功");
              ((TaskAllListActivity) context).changeTab(1);
            }

            @Override
            public void onProDialogDismiss() {
              proDialogHelps.removeProDialog();
            }
          });
        } else if (type == 1) {
          Bundle bundle = new Bundle();
          bundle.putInt(ExampleConfig.TASK_DETAIL_TYPE, 3);
          bundle.putString(ExampleConfig.ID, QRcode);
          startActivity(TaskDetailActivity.class, bundle);
        }
        break;
    }
  }
}
