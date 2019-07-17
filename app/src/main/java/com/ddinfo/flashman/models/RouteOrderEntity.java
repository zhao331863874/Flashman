package com.ddinfo.flashman.models;

/**
 * Created by Gavin on 2017/8/18.
 */

public class RouteOrderEntity {
    /**
     * commission : 1
     * orderAmount : 5
     * orderTime : 2017-08-14 12:00:00
     * routeId : 1
     * routeName : 线路1
     * storeAddress : 店铺地址
     * storeName : 店铺名称
     * warehouseName : 南京中心仓
     */

    private int commission;       //收入金额
    private double orderAmount;   //冻结金额
    private String orderTime;     //下单时间
    private int routeId;
    private String routeName;     //线路名称
    private String storeAddress;  //收货地址
    private String storeName;     //店铺名称
    private String warehouseName; //取货地址

    public int getCommission() {
        return commission;
    }

    public void setCommission(int commission) {
        this.commission = commission;
    }

    public double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }
}
