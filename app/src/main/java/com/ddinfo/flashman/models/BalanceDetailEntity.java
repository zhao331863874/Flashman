package com.ddinfo.flashman.models;

import java.io.Serializable;

/**
 * Created by weitf on 2017/2/6.
 */
public class BalanceDetailEntity implements Serializable {

    /**
     * DeliveryManId : 1
     * createdAt : 2017-01-19 18:13:51
     * id : 1
     * numberId : DD201701091234
     * payMethod : 1
     * state : 99
     * tradeNumberId : 2017010912341234
     * transferAmount : 111.11
     * type : 2
     * typeState : 押金转余额
     * updatedAt : 2017-01-19T10:13:53.000Z
     */

    private int DeliveryManId;
    private String createdAt;
    private int id;
    private String numberId;
    private int payMethod;
    private String state;
    private String tradeNumberId;
    private double transferAmount;
    private int type;
    private String typeState;
    private String updatedAt;

    public int getDeliveryManId() {
        return DeliveryManId;
    }

    public void setDeliveryManId(int DeliveryManId) {
        this.DeliveryManId = DeliveryManId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
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

    public int getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(int payMethod) {
        this.payMethod = payMethod;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTradeNumberId() {
        return tradeNumberId;
    }

    public void setTradeNumberId(String tradeNumberId) {
        this.tradeNumberId = tradeNumberId;
    }

    public double getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(double transferAmount) {
        this.transferAmount = transferAmount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeState() {
        return typeState;
    }

    public void setTypeState(String typeState) {
        this.typeState = typeState;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
