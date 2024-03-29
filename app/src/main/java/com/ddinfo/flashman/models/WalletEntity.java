package com.ddinfo.flashman.models;

import java.io.Serializable;

/**
 * Created by weitf on 2017/2/6.
 */
public class WalletEntity implements Serializable {

    /**
     * balance : 0
     * deposit : 0
     */

    private double balance;   //余额
    private double deposit;   //押金
    private double frozenAmount; //冻结额度
    private double creditAmount; //授信额度
    private double usable;       //可接货额度
    private String deliveryManId;//送货员ID

    public String getDeliveryManId() {
        return deliveryManId;
    }

    public void setDeliveryManId(String deliveryManId) {
        this.deliveryManId = deliveryManId;
    }

    public double getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(double creditAmount) {
        this.creditAmount = creditAmount;
    }

    public double getUsable() {
        return usable;
    }

    public void setUsable(double usable) {
        this.usable = usable;
    }

    public double getBalance() {
        return balance;
    }

    public double getCanDeposit(){
        return Math.min(deposit,creditAmount+deposit-frozenAmount);
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getDeposit() {
        return deposit;
    }

    public void setDeposit(double deposit) {
        this.deposit = deposit;
    }

    public double getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(double frozenAmount) {
        this.frozenAmount = frozenAmount;
    }
}
