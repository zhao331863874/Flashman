package com.ddinfo.flashman.models;

/**
 * Created by Gavin on 2017/8/19.
 * 冻结明细实体类
 */

public class FrozenDetailsEntity {
    /**
     * orderAmount : 1
     * finishSate : 未完成
     * id : 1
     * numberId : DD201701091234
     * state : 配送中
     * updatedAt : 2017-01-09 17:40:45
     */

    private String orderAmount; //冻结金额
    private String finishState; //订单完成状态
    private int id;
    private String numberId;    //订单ID
    private String state;       //冻结状态
    private String updatedAt;
    private String receiveTime; //接收时间

    public String getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getFinishState() {
        return finishState;
    }

    public void setFinishState(String finishSate) {
        this.finishState = finishSate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumberId() {
        return numberId;
    }

    public void setNumberId(String numberId) {
        this.numberId = numberId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
