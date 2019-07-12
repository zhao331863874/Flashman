package com.ddinfo.flashman.models;

import java.io.Serializable;

/**
 * Created by keyboard3 on 2018/5/17.
 */

public class PayOrderResult implements Serializable {
  public boolean finished;
  public String message;//支付成功 支付失败
  public boolean isPayOk(){
    return finished;
  }
}