package com.ddinfo.flashman.activity.menu.wallet;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.models.WalletEntity;

import butterknife.Bind;
import butterknife.OnClick;

public class TopUpInputActivity extends BaseActivity {

    @Bind(R.id.left_button)
    ImageButton leftButton;
    @Bind(R.id.header_name)
    TextView headerName;
    @Bind(R.id.right_button)
    ImageButton rightButton;
    @Bind(R.id.rightBtn)
    Button rightBtn;
    @Bind(R.id.et_top_up)
    EditText etTopUp;
    @Bind(R.id.tv_next)
    TextView tvNext;
    WalletEntity wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setTitle("押金充值");
        Bundle bundle = getIntent().getExtras();
        wallet = (WalletEntity) bundle.getSerializable("wallet");
    }

    @OnClick({R.id.tv_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_next:
                String topUpMoney = etTopUp.getText().toString().trim();
                if (TextUtils.isEmpty(topUpMoney)) {
                    ToastUtils.showShortToast("请输入正确的充值金额");
                    return;
                } else if (Integer.parseInt(topUpMoney) < 1 || Integer.parseInt(topUpMoney)>999999) {
                    ToastUtils.showShortToast("允许充值金额范围[1-999999]");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putDouble(ExampleConfig.INTENT_TOPUPMONEY, Double.parseDouble(topUpMoney));
                bundle.putSerializable("wallet", wallet);
                startActivity(ModeOfPaymentActivity.class, bundle);
                break;
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_top_up_input;
    }
}
