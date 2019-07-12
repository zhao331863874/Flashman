package com.ddinfo.flashman.models.params;

/**
 * Created by 李占晓 on 2017/6/2.
 */

public class DeliveryOrderParams {
  private String deliveryOrderId;
  private String invoiceNumberId;
  private double lat;
  private double lon;
  private String orderId;

  public DeliveryOrderParams() {
  }

  public DeliveryOrderParams(String deliveryOrderId, String invoiceNumberId, double lat, double lon, String orderId) {
    this.deliveryOrderId = deliveryOrderId;
    this.invoiceNumberId = invoiceNumberId;
    this.lat = lat;
    this.lon = lon;
    this.orderId = orderId;
  }

  public String getDeliveryOrderId() {
    return deliveryOrderId;
  }

  public void setDeliveryOrderId(String deliveryOrderId) {
    this.deliveryOrderId = deliveryOrderId;
  }

  public String getInvoiceNumberId() {
    return invoiceNumberId;
  }

  public void setInvoiceNumberId(String invoiceNumberId) {
    this.invoiceNumberId = invoiceNumberId;
  }

  public double getLat() {
    return lat;
  }

  public void setLat(double lat) {
    this.lat = lat;
  }

  public double getLon() {
    return lon;
  }

  public void setLon(double lon) {
    this.lon = lon;
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }
}
