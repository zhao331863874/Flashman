package com.ddinfo.flashman.models;

/**
 * Created by Gavin on 2017/8/18.
 */

public class RouteEntity {


    /**
     * routeName : 1
     * capacity : 5
     * orderTotal : 3
     * routeId : 1
     * sum : 10
     */

    private String routeName;
    private String capacity;
    private String orderTotal;
    private int routeId;
    private double sum;

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
