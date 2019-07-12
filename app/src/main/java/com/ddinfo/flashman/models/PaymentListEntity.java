package com.ddinfo.flashman.models;

/**
 * Created by fuh on 2017/5/11.
 * Email：unableApe@gmail.com
 */

public class PaymentListEntity {

    /**
     * tradeRecordNo : 1
     * fmt : 2017-05-11
     * id : 9
     * state : 代缴订单
     * sum : 66
     * time : 2017-05-11 15:41:56
     */

    private String tradeRecordNo;
    private String fmt;
    private int id;
    private String state;
    private double sum;
    private String time;
    private String batchId;

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        this.batchId = batchId;
    }

    public String getTradeRecordNo() {
        return tradeRecordNo;
    }

    public void setTradeRecordNo(String tradeRecordNo) {
        this.tradeRecordNo = tradeRecordNo;
    }

    public String getFmt() {
        return fmt;
    }

    public void setFmt(String fmt) {
        this.fmt = fmt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
