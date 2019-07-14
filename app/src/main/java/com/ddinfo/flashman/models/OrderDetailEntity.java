package com.ddinfo.flashman.models;

import com.blankj.utilcode.utils.TimeUtils;
import com.ddinfo.flashman.utils.Utils;
import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * 订单详情
 * Created by fuh on 2017/2/10.
 * Email：unableApe@gmail.com
 */

public class OrderDetailEntity implements Serializable {

  /**
   * id : 410
   * OrderId : 1052
   * numberId : DD449
   * orderAmount : 0.01
   * receiveTime : 2017-02-15 07:22:18   接单时间
   * pickupTime: 测试内容g0a7             取货时间
   * commission : 3.00
   * warehouseAddress : 无锡
   * storeName : 家缘烟酒店
   * storeAddress : 江苏省无锡市锡山区东亭镇春潮东路61号
   * WarehouseId : 15
   * StoreId : 2506
   * mainOrderId : 0
   * storeImg : http://192.168.1.251:3911
   * storePhone : 88213414
   * storeAcceptName : 戴晓峰
   * remark :
   * state : 0
   * invoiceNumberId : FH-15-0
   * rejectCode : 15_Rejection_1052
   * hasPay : 1
   * distance : 122592
   * lon : 120.3709852049921
   * lat : 31.58290713260139
   */

  private int id;
  private int OrderId;             //订单编号
  private String numberId;         //运单编号
  private String orderAmount;      //订单金额
  private String receiveTime;      //接单时间
  private String pickupTime;       //取货时间
  private String commission;       //预期收入
  private String warehouseAddress; //取货地址
  private String storeName;        //店铺名称
  private String storeAddress;     //收货地址
  private int WarehouseId;
  private int StoreId;
  private int mainOrderId;
  private String storeImg;         //店铺图片地址
  private String storePhone;       //店铺老板电话
  private String storeAcceptName;  //店铺老板名称
  private String remark;           //订单备注
  private int state;
  private String invoiceNumberId;  //发货单号
  private String rejectCode;
  private int hasPay;              //是否支付
  private int distance;            //距离
  private double lon;
  private double lat;

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

  //BUG: 禅道bugId=5657，解决日期:2017/6/6，解决人:李占晓，解释:所有面页的订单详情，取货时间没有年月日
  public String getReceiveTime() {
    try {
      if (receiveTime.equals("Invalid date")) {
        return TimeUtils.getNowTimeString();
      }
      String[] split = receiveTime.split(" ");
      String temp = split[0] + "\n" + split[1];
      //String temp  = split[1];
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

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
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

  @Override
  public String toString() {
    return "OrderDetailEntity{"
        + "id="
        + id
        + ", OrderId="
        + OrderId
        + ", numberId='"
        + numberId
        + '\''
        + ", orderAmount='"
        + orderAmount
        + '\''
        + ", receiveTime='"
        + receiveTime
        + '\''
        + ", commission='"
        + commission
        + '\''
        + ", warehouseAddress='"
        + warehouseAddress
        + '\''
        + ", storeName='"
        + storeName
        + '\''
        + ", storeAddress='"
        + storeAddress
        + '\''
        + ", WarehouseId="
        + WarehouseId
        + ", StoreId="
        + StoreId
        + ", mainOrderId="
        + mainOrderId
        + ", storeImg='"
        + storeImg
        + '\''
        + ", storePhone='"
        + storePhone
        + '\''
        + ", storeAcceptName='"
        + storeAcceptName
        + '\''
        + ", remark='"
        + remark
        + '\''
        + ", state="
        + state
        + ", invoiceNumberId='"
        + invoiceNumberId
        + '\''
        + ", rejectCode='"
        + rejectCode
        + '\''
        + ", hasPay="
        + hasPay
        + ", distance="
        + distance
        + ", lon="
        + lon
        + ", lat="
        + lat
        + '}';
  }

  public String getPickupTime() {
    try {
      if (pickupTime.equals("Invalid date")) {
        return TimeUtils.getNowTimeString();
      }
      String[] split = pickupTime.split(" ");
      String temp = split[0] + "\n" + split[1];
      //String temp  = split[1];
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
}
