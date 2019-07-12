package com.ddinfo.flashman.models.params;

/**
 * 提交付款
 * ids	id数组	array<number>
 * payMethod	支付方式	string	wechat、alipay
 * paySum	金额
 */
public class GoodsPayParams {
    private int batchOrderId;
    private String payMethod;

    public GoodsPayParams(int batchOrderId, String payMethod) {
        this.batchOrderId = batchOrderId;
        this.payMethod = payMethod;
    }

    public int getBatchOrderId() {
        return batchOrderId;
    }

    public void setBatchOrderId(int batchOrderId) {
        this.batchOrderId = batchOrderId;
    }


    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }
}
