package com.ddinfo.flashman.models;

import com.blankj.utilcode.utils.TimeUtils;
import com.ddinfo.flashman.utils.Utils;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Created by fuh on 2017/2/6.
 * Email：unableApe@gmail.com
 */

public class SeckillOrderList implements Serializable {

    /**
     * id : 965
     * OrderId : 4920
     * numberId : DD16872
     * orderAmount : 23.60
     * commission : 23.60
     * warehouseAddress : 无锡
     * storeName : 苏佳超市
     * storeAddress : 江苏省无锡市崇安区金科观庭商业b栋6-8单元一层
     * WarehouseId : 4
     * StoreId : 2581
     * mainOrderId : 1684371
     * storeImg : http://211.152.32.59:3911null
     * storePhone : 82030108
     * storeAcceptName : 钱艳
     * invoiceNumberId : FH-4-1684371
     * pickupTime: 测试内容g0a7             取货时间
     * receiveTime : 2017-05-27 16:55:27  接单时间
     * rejectCode : 4_Rejection_4920
     * hasPay : 0
     * distance : 126567
     * lon : 120.33797083831742
     * lat : 31.589072241691987
     */

    private int id;
    private int OrderId;             //订单ID
    private String numberId;
    private String orderAmount;      //冻结金额
    private String receiveTime;      //取货时间
    private String commission;       //收入金额
    private String warehouseAddress; //取货地址
    private String storeName;        //收货店铺名称
    private String storeAddress;     //店铺地址
    private String createdTime;      //创建时间
    private int backOrderId;         //退货单号
    private int WarehouseId;
    private int StoreId;
    private int mainOrderId;
    private String storeImg;         //店铺图片
    private String storePhone;       //店铺老板电话
    private String storeAcceptName;  //店铺老板名称
    private String invoiceNumberId;
    private String pickupTime;
    private String rejectCode;       //二维码代码
    private int hasPay;
    private int distance;            //距离
    private double lon;
    private double lat;

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public int getBackOrderId() {
        return backOrderId;
    }

    public void setBackOrderId(int backOrderId) {
        this.backOrderId = backOrderId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int OrderId) {
        this.OrderId = OrderId;
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

    public String getReceiveTime() {
        try {
            if (receiveTime.equals("Invalid date")) {
                return TimeUtils.getNowTimeString();
            }
            String[] split = receiveTime.split(" ");
            String temp = split[0] + "\n" + split[1];
            return temp;
        } catch (ArrayIndexOutOfBoundsException e) {
            return receiveTime;
        } catch (Exception e) {
            return TimeUtils.getNowTimeString();
        }
    }

    public void setReceiveTime(String receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getCommission() {
        return commission;
    }

    public void setCommission(String commission) {
        this.commission = commission;
    }

    public String getWarehouseAddress() {
        return warehouseAddress;
    }

    public void setWarehouseAddress(String warehouseAddress) {
        this.warehouseAddress = warehouseAddress;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public int getWarehouseId() {
        return WarehouseId;
    }

    public void setWarehouseId(int WarehouseId) {
        this.WarehouseId = WarehouseId;
    }

    public int getStoreId() {
        return StoreId;
    }

    public void setStoreId(int StoreId) {
        this.StoreId = StoreId;
    }

    public int getMainOrderId() {
        return mainOrderId;
    }

    public void setMainOrderId(int mainOrderId) {
        this.mainOrderId = mainOrderId;
    }

    public String getStoreImg() {
        return storeImg;
    }

    public void setStoreImg(String storeImg) {
        this.storeImg = storeImg;
    }

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    public String getStoreAcceptName() {
        return storeAcceptName;
    }

    public void setStoreAcceptName(String storeAcceptName) {
        this.storeAcceptName = storeAcceptName;
    }

    public String getInvoiceNumberId() {
        return invoiceNumberId;
    }

    public void setInvoiceNumberId(String invoiceNumberId) {
        this.invoiceNumberId = invoiceNumberId;
    }

    public String getRejectCode() {
        return rejectCode;
    }

    public void setRejectCode(String rejectCode) {
        this.rejectCode = rejectCode;
    }

    public int getHasPay() {
        return hasPay;
    }

    public void setHasPay(int hasPay) {
        this.hasPay = hasPay;
    }

    public String getDistance() {
        if (distance < 1000) {
            DecimalFormat df = new DecimalFormat("0.0");
            return Utils.subZeroAndDot(df.format(distance)) + "m";
        } else if (distance < 50 * 1000) {
            float num = (float) distance / 1000;
            DecimalFormat df = new DecimalFormat("0.0");
            return Utils.subZeroAndDot(df.format(num)) + "km";
        } else {
            return "50km之外";
        }
    }

    public String getPickupTime() {
        try {
            if (pickupTime.equals("Invalid date")) {
                return TimeUtils.getNowTimeString();
            }
            String[] split = pickupTime.split(" ");
            String temp = split[0] + "\n" + split[1];
            return temp;
        } catch (ArrayIndexOutOfBoundsException e) {
            return pickupTime;
        } catch (Exception e) {
            return TimeUtils.getNowTimeString();
        }
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
