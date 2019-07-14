package com.ddinfo.flashman.activity.task;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.PayResultEntity;
import com.ddinfo.flashman.models.params.PartSendDoneParams;
import com.ddinfo.flashman.network.SimpleCallBack;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 拒收信息页面
 */
public class TaskPartSendCauseActivity extends BaseActivity {

    @Bind(R.id.left_button)   //返回按钮
    ImageButton leftButton;
    @Bind(R.id.header_name)   //标题抬头
    TextView headerName;
    @Bind(R.id.right_button)
    ImageButton rightButton;
    @Bind(R.id.rightBtn)
    Button rightBtn;
    @Bind(R.id.et_other_cause) //其它原因
    EditText etOtherCause;
    @Bind(R.id.et_take_code)   //店家收货码
    EditText etTakeCode;
    @Bind(R.id.rb_cash)   //现金
    RadioButton rbCash;
    @Bind(R.id.rb_pos)    //POS机
    RadioButton rbPos;
    @Bind(R.id.rb_alipay) //支付宝
    RadioButton rbAlipay;
    @Bind(R.id.rb_wxpay)  //微信
    RadioButton rbWxpay;
    @Bind(R.id.rg_pay_type)   //支付选项
    RadioGroup rgPayType;
    @Bind(R.id.ll_pay_way)//选择收款方式控件
    LinearLayout llPayWay;
    @Bind(R.id.tv_done)   //确认部分送达
    TextView tvDone;
    @Bind(R.id.rb_one)    //商品保质期不符合店家要求
    RadioButton rbOne;
    @Bind(R.id.rb_two)    //送达日期晚，已从其他渠道进货
    RadioButton rbTwo;
    @Bind(R.id.rb_three)  //破损/质量问题
    RadioButton rbThree;
    @Bind(R.id.rb_four)   //误解活动优惠
    RadioButton rbFour;
    @Bind(R.id.rg_refuse_item) //拒收原因选项
    RadioGroup rgRefuseItem;

    private String selRefuseString;//选中拒收原因
    private String selPayWay;//选中支付方式
    private String takeCode;//店家收获码
    private PartSendDoneParams confirmParams;//配送ID & 拒收详情
    private double hadPaySum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        setTitle("确认部分送达");
        confirmParams = (PartSendDoneParams) getIntent().getExtras().getSerializable("confirmParams");
        hadPaySum = getIntent().getExtras().getDouble("hadPaySum");
    }

    private void initData() {
        if (hadPaySum > 0) { //已支付订单
            llPayWay.setVisibility(View.GONE); //隐藏选择收款方式控件
        } else { //待支付订单
            proDialogHelps.showProDialog("获取城市核销模式...");
            Call<BaseResponseEntity<ArrayList<String>>> callPayMethod = webService.callPayMethod("orderPay");
            callPayMethod.enqueue(callbackPayMethod);
        }
    }

    Callback<BaseResponseEntity<ArrayList<String>>> callbackPayMethod = new SimpleCallBack<BaseResponseEntity<ArrayList<String>>>(context) {
        @Override
        public void onSuccess(Call<BaseResponseEntity<ArrayList<String>>> call, Response<BaseResponseEntity<ArrayList<String>>> response) {
            llPayWay.setVisibility(View.VISIBLE); //显示选择收款方式控件
            for (int i = 0; i < response.body().getData().size(); i++) {
                switch (response.body().getData().get(i)){
                    case ExampleConfig.PAYTYPE_CASH: //如果为现金支付方式
                        rbCash.setVisibility(View.VISIBLE); //显示现金支付
                        if(i ==0){
                            rbCash.setChecked(true);
                            selPayWay = ExampleConfig.PAYTYPE_CASH;
                        }
                        break;
                    case ExampleConfig.PAYTYPE_POS:
                        rbPos.setVisibility(View.VISIBLE);
                        if(i ==0){
                            rbPos.setChecked(true);
                            selPayWay = ExampleConfig.PAYTYPE_POS;
                        }
                        break;
                    case ExampleConfig.PAYTYPE_ALIPAY:
                        rbAlipay.setVisibility(View.VISIBLE);
                        if(i ==0){
                            rbAlipay.setChecked(true);
                            selPayWay = ExampleConfig.PAYTYPE_ALIPAY;
                        }
                        break;
                    case ExampleConfig.PAYTYPE_WXPAY:
                        rbWxpay.setVisibility(View.VISIBLE);
                        if(i ==0){
                            rbWxpay.setChecked(true);
                            selPayWay = ExampleConfig.PAYTYPE_WXPAY;
                        }
                        break;
                }
            }
        }

        @Override
        public void onProDialogDismiss() {
            proDialogHelps.removeProDialog();
        }
    };

    private void initListener() {

        etOtherCause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //点击其它原因回调
                for (int i = 0; i < rgRefuseItem.getChildCount(); i++) {
                    RadioButton rb = (RadioButton) rgRefuseItem.getChildAt(i);
                    rb.setChecked(false);
                }
            }
        });

        rgPayType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) { //点击支付方式回调
                RadioButton rbPayType = (RadioButton) group.findViewById(checkedId);
                switch (checkedId) {
                    case R.id.rb_cash: //现金
                        selPayWay = ExampleConfig.PAYTYPE_CASH;
                        break;
                    case R.id.rb_pos: //POS机
                        selPayWay = ExampleConfig.PAYTYPE_POS;
                        break;
                    case R.id.rb_alipay: //支付宝
                        selPayWay = ExampleConfig.PAYTYPE_ALIPAY;
                        break;
                    case R.id.rb_wxpay: //微信
                        selPayWay = ExampleConfig.PAYTYPE_WXPAY;
                        break;
                }
            }
        });

//        rgRefuseItem.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
//                RadioButton refuseRadio = (RadioButton) radioGroup.findViewById(i);
//                selRefuseString = refuseRadio.getText().toString();
//            }
//        });


    }


    @OnClick({R.id.tv_done})
    public void doClick(View view) {
        switch (view.getId()) {
            case R.id.tv_done: //点击确认部分送达
                takeCode = etTakeCode.getText().toString().trim(); //店家收货码

                if (!TextUtils.isEmpty(etOtherCause.getText().toString())) {
                    selRefuseString = etOtherCause.getText().toString().trim();
                } else {
                    RadioButton selRefuseRadio = (RadioButton) findViewById(rgRefuseItem.getCheckedRadioButtonId());
                    if (TextUtils.isEmpty(selRefuseRadio.getText().toString())) {
                        ToastUtils.showShortToast("请选择或填写拒收原因");
                        return;
                    } else {
                        selRefuseString = selRefuseRadio.getText().toString().trim();
                    }
                }
                if(hadPaySum <= 0){
                    if (TextUtils.isEmpty(selPayWay)) {
                        ToastUtils.showShortToast("请选择支付方式");
                        return;
                    }
                }else{
                    selPayWay =null;
                }
                if (TextUtils.isEmpty(takeCode)) {
                    ToastUtils.showShortToast("请填写收货码");
                    return;
                }

                proDialogHelps.showProDialog();
                PartSendDoneParams doneParams = new PartSendDoneParams(confirmParams.getDeliveryOrderId(), selPayWay, confirmParams.getDetails(), selRefuseString, takeCode);
                webService.getPartSendDone(doneParams).enqueue(new SimpleCallBack<BaseResponseEntity<PayResultEntity>>(context) {
                    @Override
                    public void onSuccess(Call<BaseResponseEntity<PayResultEntity>> call, Response<BaseResponseEntity<PayResultEntity>> response) {
                        if (response.body().getData() == null) {
                            ToastUtils.showShortToast("成功送达！");
                            startActivity(TaskAllListActivity.class);
                        } else {
                            Bundle bundle = new Bundle();
                            switch (selPayWay) {
                                case ExampleConfig.PAYTYPE_ALIPAY:
                                    bundle.putInt(ExampleConfig.INTENT_TYPE, TaskIncomeActivity.TYPE_ALIPAY);
                                    break;
                                case ExampleConfig.PAYTYPE_WXPAY:
                                    bundle.putInt(ExampleConfig.INTENT_TYPE, TaskIncomeActivity.TYPE_WXPAY);
                                    break;
                            }
                            bundle.putString(TaskIncomeActivity.PAY_ID, response.body().getData().getPayId()); //支付ID
                            bundle.putString(TaskIncomeActivity.URL_CODE, response.body().getData().getCodeUrl()); //生成二维码地址
                            bundle.putString("orderId",response.body().getData().getOrderId()+""); //订单号
                            bundle.putString("orderAmount",response.body().getData().getOrderAmount()+""); //订单金额
                            startActivity(TaskIncomeActivity.class,bundle);   //ZYZ标签
                        }
                    }
                    @Override
                    public void onProDialogDismiss() {
                        proDialogHelps.removeProDialog();
                    }
                });
                break;

        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_task_part_send_cause;
    }


}
