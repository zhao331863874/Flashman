package com.ddinfo.flashman.models.params;

/**
 * Created by fuh on 2017/2/8.
 * Email：unableApe@gmail.com
 */

public class GoodsRefuseParams {
    private String numberId;
    private String receiptCode;

    public GoodsRefuseParams(String numberId, String receiptCode) {
        this.numberId = numberId;
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

}
