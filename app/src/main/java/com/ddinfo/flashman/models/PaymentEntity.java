package com.ddinfo.flashman.models;

/**
 * Created by fuh on 2017/2/8.
 * Email：unableApe@gmail.com
 * 交货款项实体类
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
    private String numberId;    //交货款ID
    private String orderAmount; //货款价格
    private String state;
    private boolean isChecked = false; //是否选中当前交货款项

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
