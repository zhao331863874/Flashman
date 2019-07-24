package com.ddinfo.flashman.models;

/**
 * Created by fuh on 2017/2/8.
 * Email：unableApe@gmail.com
 * 仓库信息实体类
 */

public class WareHouseInfoEntity {

    /**
     * sum : 0
     * warehouseAddress : 达尔文路
     */

    private String sum;              //待缴金额
    private String warehouseAddress; //当前接单仓
    private String batchOrderSum;    //待支付金额
    private String parentManName;    //上级配送员名称
    private int confirmState;//信息审核状态（0:未提交、1:审核中、2:已通过、3:未通过

    public int getConfirmState() {
        return confirmState;
    }

    public void setConfirmState(int confirmState) {
        this.confirmState = confirmState;
    }

    public String getParentManName() {
        return parentManName;
    }

    public void setParentManName(String parentManName) {
        this.parentManName = parentManName;
    }

    public String getSum() {
        return sum;
    }

    public void setSum(String sum) {
        this.sum = sum;
    }

    public String getWarehouseAddress() {
        return warehouseAddress;
    }

    public void setWarehouseAddress(String warehouseAddress) {
        this.warehouseAddress = warehouseAddress;
    }

    public String getBatchOrderSum() {
        return batchOrderSum;
    }

    public void setBatchOrderSum(String batchOrderSum) {
        this.batchOrderSum = batchOrderSum;
    }
}
