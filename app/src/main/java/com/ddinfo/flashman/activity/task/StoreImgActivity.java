package com.ddinfo.flashman.activity.task;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.models.BaseResponseEntity;
import com.ddinfo.flashman.models.OrderDetailEntity;
import com.ddinfo.flashman.models.SeckillOrderList;
import com.ddinfo.flashman.models.params.DeliveryOrderParams;
import com.ddinfo.flashman.network.SimpleCallBack;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 李占晓 on 2017/6/1.
 */

public class StoreImgActivity extends BaseActivity {

  private int offset = 0;
  // 布尔: 李占晓  页面是否显示完
  private boolean isPrepared = false;

  // 布尔: 李占晓 正在加载数据
  private boolean isLoadMore = false;
  // 整数: 李占晓 当前分页数
  private int mPosition;

  private OrderDetailEntity mData;

  @Override
  protected int getLayoutResId() {
    return R.layout.activity_store_img;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initData();
  }

  private void initView() {
    ImageView imageView = (ImageView) findViewById(R.id.img_store_img);

    Glide.with(this).load(mData.getStoreImg())
        .placeholder(R.mipmap.icon_store_default_img)
        .error(R.mipmap.icon_store_default_img).into(imageView);

    imageView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          activity.finishAfterTransition();
        } else {
          finish();
        }
      }
    });
  }

  private void initData() {
    double lat = getIntent().getDoubleExtra("lat", 0);
    double lon = getIntent().getDoubleExtra("lon", 0);
    String invoiceNumberId = getIntent().getStringExtra(ExampleConfig.INVOICENUMBER_ID);
    DeliveryOrderParams params = new DeliveryOrderParams();
    params.setLon(lon);
    params.setLat(lat);
    params.setInvoiceNumberId(invoiceNumberId);
    Call<BaseResponseEntity<OrderDetailEntity>> callOderList =
        webService.getDeliveryOrder(ExampleConfig.token, params);
    callOderList.enqueue(callBackOrderList);
  }

  Callback<BaseResponseEntity<OrderDetailEntity>> callBackOrderList =
      new SimpleCallBack<BaseResponseEntity<OrderDetailEntity>>(context) {

        @Override
        public void onSuccess(Call<BaseResponseEntity<OrderDetailEntity>> call,
            Response<BaseResponseEntity<OrderDetailEntity>> response) {

          mData = response.body().getData();
          initView();
        }

        @Override
        public void onProDialogDismiss() {

        }
      };
}
