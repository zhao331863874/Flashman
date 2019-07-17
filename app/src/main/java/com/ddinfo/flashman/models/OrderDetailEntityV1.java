package com.ddinfo.flashman.models;

import com.blankj.utilcode.utils.TimeUtils;
import java.io.Serializable;

/**
 * 订单详情
 * Created by fuh on 2017/2/10.
 * Email：unableApe@gmail.com
 */

public class OrderDetailEntityV1 implements Serializable {

  /**
   * id : 32
   * OrderId : 581
   * numberId : DD27
   * orderAmount : 89.52
   * receiveTime : 16:10:51
   * commission : 3.00
   * warehouseAddress : 无锡前置仓1
   * storeName : 家缘烟酒店
   * storeAddress : 江苏省无锡市锡山区东亭镇春潮东路61号
   * WarehouseId : 15
   * rejectCode : 15_Rejection_581
   */

  private int id;
  private int OrderId;             //订单编号
  private String numberId;         //运单编号
  private String orderAmount;      //订单金额
  private String receiveTime;      //取货时间
  private String commission;       //预期收入
  private String warehouseAddress; //取货地址
  private String storeName;        //店铺名称
  private String storeAddress;     //收货地址
  private int WarehouseId;
  private String rejectCode;
  private String invoiceNumberId;  //发货单号

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
      //String temp  = split[1];
      return temp;
    } catch (ArrayIndexOutOfBoundsException e) {
      return receiveTime;
    }catch (Exception e){
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

  public String getRejectCode() {
    return rejectCode;
  }

  public void setRejectCode(String rejectCode) {
    this.rejectCode = rejectCode;
  }

  public String getInvoiceNumberId() {
    return invoiceNumberId;
  }

  public void setInvoiceNumberId(String invoiceNumberId) {
    this.invoiceNumberId = invoiceNumberId;
  }
}
