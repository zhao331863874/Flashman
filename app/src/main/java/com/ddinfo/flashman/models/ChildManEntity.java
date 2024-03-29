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

    private String creditAmount;    //授信额度
    private int deliveryManId;
    private String deliveryManName; //下级配送员名称
    private String frozenAmount;    //冻结金额
    private String phone;           //下级配送员电话
    private String usable;          //可接货额度

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
