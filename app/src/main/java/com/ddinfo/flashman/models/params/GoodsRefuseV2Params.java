package com.ddinfo.flashman.models.params;

/**
 * Created by fuh on 2017/2/8.
 * Email：unableApe@gmail.com
 */

public class GoodsRefuseV2Params {
  private String numberId;
  private String reason;
  private String receiptCode;

  public GoodsRefuseV2Params(String numberId, String reason, String receiptCode) {
    this.numberId = numberId;
    this.reason = reason;
    this.receiptCode = receiptCode;
  }

  public String getNumberId() {
    return numberId;
  }

  public void setNumberId(String numberId) {
    this.numberId = numberId;
  }

  public String getReceiptCode() {
    return receiptCode;
  }

  public void setReceiptCode(String receiptCode) {
    this.receiptCode = receiptCode;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }
}
