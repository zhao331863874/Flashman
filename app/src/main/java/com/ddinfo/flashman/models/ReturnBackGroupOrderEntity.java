package com.ddinfo.flashman.models;

/**
 * 注释:
 * Created by weitf on 2017/12/26.
 * Email:weitengfei0212@gmail.com
 */

public class ReturnBackGroupOrderEntity {

    /**
     * id : 72
     * goodId : 137465
     * packType : 0
     * produceTime : 2017-05-03T00:00:00.000Z
     * orgAmount : 1
     * realAmount : null
     * realPrice : 0.8
     * specification : 100g
     * unit : 袋
     * name : 美美3
     */

    private int id;
    private int goodId;
    private int packType;
    private String produceTime; //生产日期
    private int orgAmount;   //退货数量
    private int realAmount;  //实际退货数量
    private double realPrice;
    private String specification;
    private String unit;
    private String goodName; //货品名称

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGoodId() {
        return goodId;
    }

    public void setGoodId(int goodId) {
        this.goodId = goodId;
    }

    public int getPackType() {
        return packType;
    }

    public void setPackType(int packType) {
        this.packType = packType;
    }

    public String getProduceTime() {
        return produceTime;
    }

    public void setProduceTime(String produceTime) {
        this.produceTime = produceTime;
    }

    public int getOrgAmount() {
        return orgAmount;
    }

    public void setOrgAmount(int orgAmount) {
        this.orgAmount = orgAmount;
    }

    public int getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(int realAmount) {
        this.realAmount = realAmount;
    }

    public double getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(double realPrice) {
        this.realPrice = realPrice;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }
}
