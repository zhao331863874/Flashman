package com.ddinfo.flashman.helps;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.constant.ExampleConfig;

import java.util.ArrayList;

import static com.ddinfo.flashman.activity.task.TaskDetailActivity.PAYTYPE_CASH;

/**
 * Created by Gavin on 2017/6/29.
 * 送达弹框
 */

public class TaskSendDialog {

    private String payType; //支付方式
    private Context context;

    public TaskSendDialog(Context context) {
        this.context = context;
    }

    /**
     * @param hasPay 是否支付
     * @param payMethods 支付方式
     * @param numberId numberId
     * @param onSendOptionClickListener 回调
     */
    public void show(final int hasPay, ArrayList<String> payMethods, final String numberId, final OnSendOptionClickListener onSendOptionClickListener) {


        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_send, null);
        final EditText etGoodsCode = (EditText) view.findViewById(R.id.et_goods_code);    //店家收货码
        final LinearLayout llPayType = (LinearLayout) view.findViewById(R.id.ll_pay_type);//请选择收款方式布局
        final TextView tvOrderPayed = (TextView) view.findViewById(R.id.tv_order_payed);  //订单已支付
        final RadioGroup rgPayType = (RadioGroup) view.findViewById(R.id.rg_pay_type);    //支付方式选项布局
        final RadioButton rbCash = (RadioButton) view.findViewById(R.id.rb_cash);         //现金支付
        final RadioButton rbPos = (RadioButton) view.findViewById(R.id.rb_pos);           //POS机支付
        final RadioButton rbAlipay = (RadioButton) view.findViewById(R.id.rb_alipay);     //支付宝支付
        final RadioButton rbWxpay = (RadioButton) view.findViewById(R.id.rb_wxpay);       //微信支付

        if (hasPay == 0) { //未支付
            llPayType.setVisibility(View.VISIBLE); //显示选择支付方式
            tvOrderPayed.setVisibility(View.GONE); //隐藏订单已支付布局
            for (int i = 0; i < payMethods.size(); i++) {
                switch (payMethods.get(i)) {
                    case ExampleConfig.PAYTYPE_CASH:
                        rbCash.setVisibility(View.VISIBLE); //显示现金支付方式
                        if (i == 0) {
                            rbCash.setChecked(true); //选中现金支付方式选项
                            payType = ExampleConfig.PAYTYPE_CASH;
                        }
                        break;
                    case ExampleConfig.PAYTYPE_POS:
                        rbPos.setVisibility(View.VISIBLE);
                        if (i == 0) {
                            rbPos.setChecked(true);
                            payType = ExampleConfig.PAYTYPE_POS;

                        }
                        break;
                    case ExampleConfig.PAYTYPE_ALIPAY:
                        rbAlipay.setVisibility(View.VISIBLE);
                        if (i == 0) {
                            rbAlipay.setChecked(true);
                            payType = ExampleConfig.PAYTYPE_ALIPAY;

                        }
                        break;
                    case ExampleConfig.PAYTYPE_WXPAY:
                        rbWxpay.setVisibility(View.VISIBLE);
                        if (i == 0) {
                            rbWxpay.setChecked(true);
                            payType = ExampleConfig.PAYTYPE_WXPAY;

                        }
                        break;
                }
            }

            rgPayType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton rbPayType = (RadioButton) group.findViewById(checkedId);
                    switch (checkedId) {
                        case R.id.rb_cash: //选择现金支付
                            payType = ExampleConfig.PAYTYPE_CASH;
                            break;
                        case R.id.rb_pos:  //选择POS机支付
                            payType = ExampleConfig.PAYTYPE_POS;
                            break;
                        case R.id.rb_alipay: //选择支付宝支付
                            payType = ExampleConfig.PAYTYPE_ALIPAY;
                            break;
                        case R.id.rb_wxpay: //选择微信支付
                            payType = ExampleConfig.PAYTYPE_WXPAY;
                            break;
                    }
                }
            });
        } else { //已支付
            llPayType.setVisibility(View.GONE); //隐藏选择收款方式布局
            tvOrderPayed.setVisibility(View.VISIBLE); //显示订单已支付
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("是否确认送达");
        builder.setView(view);

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(etGoodsCode.getText().toString().trim())) {
                    ToastUtils.showShortToast("请输入收货码");
                    return;
                }
                if (hasPay == 0) { //未支付
                    if (payType == "") {
                        ToastUtils.showShortToast("请选择支付方式");
                        return;
                    }
                    onSendOptionClickListener.onSendEnterClick(numberId + "",
                            etGoodsCode.getText().toString().trim(), payType);

                } else { //已支付 后台要求 payType==现金
                    onSendOptionClickListener.onSendEnterClick(numberId + "",
                            etGoodsCode.getText().toString().trim(), "");
                }
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public interface OnSendOptionClickListener {
        /**
         * @param numberId
         * @param goodsCode 店家收货码
         * @param payType 支付方式
         */
        void onSendEnterClick(String numberId, String goodsCode, String payType);
    }
}
