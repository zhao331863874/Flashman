package com.ddinfo.flashman.models;

/**
 * Created by fuh on 2017/3/29.
 * Email：unableApe@gmail.com
 */

public class PayResultEntity {
    private String codeUrl; //生成二维码地址
    private String payId;   //支付ID
    private String OrderId; //订单号
    private String orderAmount; //订单金额

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
