package com.ddinfo.flashman.activity.menu.wallet;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.models.RecordsEntity;

import butterknife.Bind;

public class WithdrawsDetailActivity extends BaseActivity {
  @Bind(R.id.money)
  TextView money;
  @Bind(R.id.type_state)
  TextView typeState;
  @Bind(R.id.time)
  TextView time;
  @Bind(R.id.number_id)
  TextView numberId;
  @Bind(R.id.trade_number_id)
  TextView tradeNumberId;

  RecordsEntity entity;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setTitle("提现明细");
    entity = (RecordsEntity) getIntent().getSerializableExtra(ExampleConfig.DATA);
    if(entity==null){
      ToastUtils.showShortToast("没有详情数据");
    }
    initViews(entity);
  }

  private void initViews(RecordsEntity entity) {
    money.setText(String.format("%.2f",Math.abs(entity.getTransferAmount())));
    typeState.setText(entity.getTypeState());
    time.setText(entity.getCreatedAt());
    numberId.setText(entity.getNumberId());
    tradeNumberId.setText(entity.getTradeNumberId());
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_withdraws_detail;
  }

}
