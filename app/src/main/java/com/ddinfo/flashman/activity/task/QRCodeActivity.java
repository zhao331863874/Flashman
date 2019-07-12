package com.ddinfo.flashman.activity.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.NetworkUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.utils.zxing.encoding.EncodingHandler;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 订单二维码展示界面
 */
public class QRCodeActivity extends BaseActivity {

    @Bind(R.id.left_button) //返回按钮
    ImageButton leftButton;
    @Bind(R.id.header_name) //标题抬头
    TextView headerName;
    @Bind(R.id.right_button)
    ImageButton rightButton;
    @Bind(R.id.rightBtn)
    Button rightBtn;
    @Bind(R.id.tv_order_id) //订单号
    TextView tvOrderId;
    @Bind(R.id.iv_qr_code)  //二维码展示区域
    ImageView ivQrCode;

    private String orderId; //订单号
    private String QRCode;  //二维码代码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setTitle("订单二维码");
        orderId = getIntent().getExtras().getString(ExampleConfig.NUMBER_ID);
        QRCode = getIntent().getExtras().getString(ExampleConfig.QRCODE);
        tvOrderId.setText("订单号:" + orderId);
        try {
//                    Bitmap mBitmap = EncodingHandler.createQRCode("www.baidu.com", 300);
//                    qrcodeImg.setImageBitmap(mBitmap);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_logo);
            Bitmap www = EncodingHandler.createQRCode(QRCode, 600, 600, bitmap);
            ivQrCode.setImageBitmap(www);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_qrcode;
    }
}
