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

import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;

/**
 * Created by Gavin on 2017/6/29.
 * 拒收弹框
 */

public class TaskRefuseDialog {
    private Context context;

    public TaskRefuseDialog(Context context) {
        this.context = context;
    }

    public void show(final OnRefuseOptionClickListener onRefuseOptionClickListener) {
        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_refuse, null);
        final EditText etGoodsCode = (EditText) view.findViewById(R.id.et_goods_code);   //店家收货码
        final LinearLayout llRefuse = (LinearLayout) view.findViewById(R.id.ll_refuse);
        final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.grout_reason); //拒收原因选项
        final EditText otherReason = (EditText) view.findViewById(R.id.edit_reason);     //其他原因输入框
        final RadioButton radioOther = (RadioButton) view.findViewById(R.id.radio_other);//其他选项

        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("是否确认拒收");
        builder.setView(view);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(etGoodsCode.getText().toString().trim())) {
                    ToastUtils.showShortToast("请输入收货码");
                    return;
                }
                String reason;
                if (radioOther.isChecked()) { //如果选中其他原因
                    reason = otherReason.getText().toString().trim();
                } else {
                    RadioButton button =
                            (RadioButton) view.findViewById(radioGroup.getCheckedRadioButtonId());
                    reason = button.getText().toString().trim(); //获得当前拒收原因
                }
                if (TextUtils.isEmpty(reason)) {
                    ToastUtils.showShortToast("请输入拒收原因");
                } else {
                    if(onRefuseOptionClickListener !=null){
                        onRefuseOptionClickListener.onRefuseEnterClick(reason,etGoodsCode.getText().toString().trim());
                    }
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

    public interface OnRefuseOptionClickListener {
        /**
         * @param reason 拒收原因
         * @param goodsCode 收货码
         */
        void onRefuseEnterClick(String reason, String goodsCode);
    }
}
