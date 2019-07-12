package com.ddinfo.flashman.models.params;

/**
 * 押金转入
 * 请求参数列表
 */
public class RechargeParams {
    private double sum;
    private String type;

    public RechargeParams(double sum, String type) {
        this.sum = sum;
        this.type = type;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
