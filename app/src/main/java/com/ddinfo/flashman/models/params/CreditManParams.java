package com.ddinfo.flashman.models.params;

/**
 * Created by Gavin on 2017/8/19.
 */

public class CreditManParams {
    private String creditAmount;
    private int deliveryManId;

    public CreditManParams(String creditAmount, int deliveryManId) {
        this.creditAmount = creditAmount;
        this.deliveryManId = deliveryManId;
    }

    public String getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(String creditAmount) {
        this.creditAmount = creditAmount;
    }

    public int getDeliveryManId() {
        return deliveryManId;
    }

    public void setDeliveryManId(int deliveryManId) {
        this.deliveryManId = deliveryManId;
    }
}
