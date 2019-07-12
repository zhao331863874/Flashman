package com.ddinfo.flashman.models;

/**
 * Created by Gavin on 2017/8/18.
 */

public class ChildManEntity {
    /**
     * creditAmount : 1
     * deliveryManId : 1
     * deliveryManName : 测试内容yn70
     * frozenAmount : 1
     * phone : 1
     * usable : 1
     */

    private String creditAmount;
    private int deliveryManId;
    private String deliveryManName;
    private String frozenAmount;
    private String phone;
    private String usable;

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

    public String getDeliveryManName() {
        return deliveryManName;
    }

    public void setDeliveryManName(String deliveryManName) {
        this.deliveryManName = deliveryManName;
    }

    public String getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(String frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsable() {
        return usable;
    }

    public void setUsable(String usable) {
        this.usable = usable;
    }
}
