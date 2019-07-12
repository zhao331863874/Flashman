package com.ddinfo.flashman.models;

import java.io.Serializable;

/**
 * Created by fuh on 2017/3/30.
 * Email：unableApe@gmail.com
 */

public class CheckPayStatusEntity implements Serializable{
    private int orderId;
    private String tradeRecordNo; //交易单号
    private double orderAmount;   //交易金额
    private String time;          //交易时间
    private int hasPay;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getTradeRecordNo() {
        return tradeRecordNo;
    }

    public void setTradeRecordNo(String tradeRecordNo) {
        this.tradeRecordNo = tradeRecordNo;
    }

    public double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getHasPay() {
        return hasPay;
    }

    public void setHasPay(int hasPay) {
        this.hasPay = hasPay;
    }
}
