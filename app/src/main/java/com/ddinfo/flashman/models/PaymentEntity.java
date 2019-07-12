package com.ddinfo.flashman.models;

/**
 * Created by fuh on 2017/2/8.
 * Email：unableApe@gmail.com
 */

public class PaymentEntity {

    /**
     * DeliveryOrderId : 1
     * numberId : DD201701091236
     * orderAmount : 111.00
     * state : 0
     */

    private int DeliveryOrderId;
    private int id;
    private String numberId;
    private String orderAmount;
    private String state;
    private boolean isChecked = false;

    public boolean getChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getDeliveryOrderId() {
        return DeliveryOrderId;
    }

    public void setDeliveryOrderId(int DeliveryOrderId) {
        this.DeliveryOrderId = DeliveryOrderId;
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

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
