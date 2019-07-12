package com.ddinfo.flashman.activity.task;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.models.CheckPayStatusEntity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 收款成功界面
 */
public class TaskIncomeResultActivity extends BaseActivity {

    public static final String TASK_RESULT_MODEL = "TASK_RESULT_MODEL"; //收款回调数据
    public static final String PAY_TYPE = "PAY_TYPE"; //支付类型
    public static final String ORDER_ID = "ORDER_ID"; //订单号
    @Bind(R.id.tv_order_money) //交易金额
    TextView tvOrderMoney;
    @Bind(R.id.tv_pay_state)   //收款成功
    TextView tvPayState;
    @Bind(R.id.tv_order_income_id) //交易单号
    TextView tvOrderIncomeId;
    @Bind(R.id.tv_order_time)  //交易时间
    TextView tvOrderTime;
    @Bind(R.id.tv_order_id)    //订单号
    TextView tvOrderId;

    private CheckPayStatusEntity checkPayStatusEntity;
    private String payTypeString; //支付类型
    private String orderId;       //订单号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        try {
            checkPayStatusEntity = (CheckPayStatusEntity) getIntent().getExtras().getSerializable(TASK_RESULT_MODEL);

            payTypeString = getIntent().getExtras().getString(PAY_TYPE);
            orderId = getIntent().getExtras().getString(ORDER_ID);
            if(checkPayStatusEntity!=null){
                tvOrderMoney.setText("￥"+checkPayStatusEntity.getOrderAmount());
                tvPayState.setText(payTypeString+"收款成功");
                tvOrderIncomeId.setText(checkPayStatusEntity.getTradeRecordNo()+"");
                tvOrderTime.setText(checkPayStatusEntity.getTime());
                tvOrderId.setText(orderId);
            }
        } catch (Exception e) {
            e.printStackTrace(); // 此处处理有可能传过来bundle空数据
        }
    }

    @OnClick({R.id.left_button})
    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.left_button: //点击返回按钮
                startActivity(TaskAllListActivity.class);
                break;
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_task_income_result;
    }
}
