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

/**
 * 提现明细布局
 */
public class WithdrawsDetailActivity extends BaseActivity {
  @Bind(R.id.money) //出账金额
  TextView money;
  @Bind(R.id.type_state) //交易类型
  TextView typeState;
  @Bind(R.id.time)       //提现时间
  TextView time;
  @Bind(R.id.number_id)  //运单号
  TextView numberId;
  @Bind(R.id.trade_number_id) //交易单号
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
    money.setText(String.format("%.2f",Math.abs(entity.getTransferAmount()))); //出账金额
    typeState.setText(entity.getTypeState()); //交易类型
    time.setText(entity.getCreatedAt());      //提现时间
    numberId.setText(entity.getNumberId());   //运单号
    tradeNumberId.setText(entity.getTradeNumberId()); //交易单号
  }

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_withdraws_detail;
  }

}
