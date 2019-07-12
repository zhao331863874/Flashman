package com.ddinfo.flashman.models.params;

/**
 * Created by fuh on 2017/2/8.
 * Email：unableApe@gmail.com
 */

public class GoodsSendParams {
    private String numberId;
    private String receiptCode;
    private String receiveType;

    public GoodsSendParams(String numberId, String receiptCode, String receiveType) {
        this.numberId = numberId;
        this.receiptCode = receiptCode;
        this.receiveType = receiveType;
    }

    public GoodsSendParams(String numberId, String receiptCode) {
        this.numberId = numberId;
        this.receiptCode = receiptCode;
    }

    public String getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(String receiveType) {
        this.receiveType = receiveType;
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
