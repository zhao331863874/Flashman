package com.ddinfo.flashman.models;

/**
 * Created by fuh on 2017/3/29.
 * Email：unableApe@gmail.com
 */

public class PayResultEntity {
    private String codeUrl;
    private String payId;
    private String OrderId;
    private String orderAmount;

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String orderId) {
        OrderId = orderId;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }
}
