package com.ddinfo.flashman.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Gavin on 2017/6/6.
 */

public class PartSendConfirmEntity implements Serializable {
  /**
   * deliveryOrderId : 11238
   * goodsDetail : {"arrive":[],"reject":[{"id":87785,"onSellGoodsCombs":[{"amount":87713,"name":"测试内容t7v5"}],"quantity":57044}]}
   * invoiceNumberId : 测试内容53b8
   * nowInfo : {}
   * orgInfo : {"dadouOff":82118,"goodsAmount":76008,"hadPaySum":33715,"needPaySum":25015,"needRefund":72676,"otherOff":10553,"sum":78784,"totalOff":56013}
   */

  private int deliveryOrderId;
  private GoodsDetailBean goodsDetail;
  private String invoiceNumberId;
  private NowInfoBean nowInfo;
  private OrgInfoBean orgInfo;

  public int getDeliveryOrderId() {
    return deliveryOrderId;
  }

  public void setDeliveryOrderId(int deliveryOrderId) {
    this.deliveryOrderId = deliveryOrderId;
  }

  public GoodsDetailBean getGoodsDetail() {
    return goodsDetail;
  }

  public void setGoodsDetail(GoodsDetailBean goodsDetail) {
    this.goodsDetail = goodsDetail;
  }

  public String getInvoiceNumberId() {
    return invoiceNumberId;
  }

  public void setInvoiceNumberId(String invoiceNumberId) {
    this.invoiceNumberId = invoiceNumberId;
  }

  public NowInfoBean getNowInfo() {
    return nowInfo;
  }

  public void setNowInfo(NowInfoBean nowInfo) {
    this.nowInfo = nowInfo;
  }

  public OrgInfoBean getOrgInfo() {
    return orgInfo;
  }

  public void setOrgInfo(OrgInfoBean orgInfo) {
    this.orgInfo = orgInfo;
  }

  public static class GoodsDetailBean implements Serializable{
    private List<RejectBean> arrive;
    private List<RejectBean> reject;
    private int arriveAmount;
    private int rejectAmount;

    public int getArriveAmount() {
      return arriveAmount;
    }

    public void setArriveAmount(int arriveAmount) {
      this.arriveAmount = arriveAmount;
    }

    public int getRejectAmount() {
      return rejectAmount;
    }

    public void setRejectAmount(int rejectAmount) {
      this.rejectAmount = rejectAmount;
    }

    public List<RejectBean> getArrive() {
      return arrive;
    }

    public void setArrive(List<RejectBean> arrive) {
      this.arrive = arrive;
    }

    public List<RejectBean> getReject() {
      return reject;
    }

    public void setReject(List<RejectBean> reject) {
      this.reject = reject;
    }

    public static class RejectBean implements Serializable{
      /**
       * id : 87785
       * onSellGoodsCombs : [{"amount":87713,"name":"测试内容t7v5"}]
       * quantity : 57044
       */

      private int id;
      private int quantity;
      private String name;
      private List<OnSellGoodsCombsBean> onSellGoodsCombs;
      private int giftFlag;

      public int getGiftFlag() {
        return giftFlag;
      }

      public void setGiftFlag(int giftFlag) {
        this.giftFlag = giftFlag;
      }

      public String getName() {
        return name;
      }

      public void setName(String name) {
        this.name = name;
      }

      public int getId() {
        return id;
      }

      public void setId(int id) {
        this.id = id;
      }

      public int getQuantity() {
        return quantity;
      }

      public void setQuantity(int quantity) {
        this.quantity = quantity;
      }

      public List<OnSellGoodsCombsBean> getOnSellGoodsCombs() {
        return onSellGoodsCombs;
      }

      public void setOnSellGoodsCombs(List<OnSellGoodsCombsBean> onSellGoodsCombs) {
        this.onSellGoodsCombs = onSellGoodsCombs;
      }

      public static class OnSellGoodsCombsBean implements Serializable{
        /**
         * amount : 87713
         * name : 测试内容t7v5
         */

        private int amount;
        private String name;

        public int getAmount() {
          return amount;
        }

        public void setAmount(int amount) {
          this.amount = amount;
        }

        public String getName() {
          return name;
        }

        public void setName(String name) {
          this.name = name;
        }
      }
    }
  }

  public static class NowInfoBean implements Serializable{
    /**
     * dadouOff : 82118
     * goodsAmount : 76008
     * hadPaySum : 33715
     * needPaySum : 25015
     * needRefund : 72676
     * otherOff : 10553
     * sum : 78784
     * totalOff : 56013
     */

    private double dadouOff;
    private int goodsAmount;
    private double hadPaySum;
    private double needPaySum;
    private double needRefund;
    private double otherOff;
    private double sum;
    private double totalOff;
    private int giftFlag;

    public int getGiftFlag() {
      return giftFlag;
    }

    public void setGiftFlag(int giftFlag) {
      this.giftFlag = giftFlag;
    }

    public int getGoodsAmount() {
      return goodsAmount;
    }

    public void setGoodsAmount(int goodsAmount) {
      this.goodsAmount = goodsAmount;
    }

    public double getDadouOff() {
      return dadouOff;
    }

    public void setDadouOff(double dadouOff) {
      this.dadouOff = dadouOff;
    }

    public double getHadPaySum() {
      return hadPaySum;
    }

    public void setHadPaySum(double hadPaySum) {
      this.hadPaySum = hadPaySum;
    }

    public double getNeedPaySum() {
      return needPaySum;
    }

    public void setNeedPaySum(double needPaySum) {
      this.needPaySum = needPaySum;
    }

    public double getNeedRefund() {
      return needRefund;
    }

    public void setNeedRefund(double needRefund) {
      this.needRefund = needRefund;
    }

    public double getOtherOff() {
      return otherOff;
    }

    public void setOtherOff(double otherOff) {
      this.otherOff = otherOff;
    }

    public double getSum() {
      return sum;
    }

    public void setSum(double sum) {
      this.sum = sum;
    }

    public double getTotalOff() {
      return totalOff;
    }

    public void setTotalOff(double totalOff) {
      this.totalOff = totalOff;
    }
  }

  public static class OrgInfoBean implements Serializable{
    /**
     * dadouOff : 82118
     * goodsAmount : 76008
     * hadPaySum : 33715
     * needPaySum : 25015
     * needRefund : 72676
     * otherOff : 10553
     * sum : 78784
     * totalOff : 56013
     */

    private double dadouOff;
    private int goodsAmount;
    private double hadPaySum;
    private double needPaySum;
    private double needRefund;
    private double otherOff;
    private double sum;
    private double totalOff;

    public double getDadouOff() {
      return dadouOff;
    }

    public void setDadouOff(double dadouOff) {
      this.dadouOff = dadouOff;
    }

    public int getGoodsAmount() {
      return goodsAmount;
    }

    public void setGoodsAmount(int goodsAmount) {
      this.goodsAmount = goodsAmount;
    }

    public double getHadPaySum() {
      return hadPaySum;
    }

    public void setHadPaySum(double hadPaySum) {
      this.hadPaySum = hadPaySum;
    }

    public double getNeedPaySum() {
      return needPaySum;
    }

    public void setNeedPaySum(double needPaySum) {
      this.needPaySum = needPaySum;
    }

    public double getNeedRefund() {
      return needRefund;
    }

    public void setNeedRefund(double needRefund) {
      this.needRefund = needRefund;
    }

    public double getOtherOff() {
      return otherOff;
    }

    public void setOtherOff(double otherOff) {
      this.otherOff = otherOff;
    }

    public double getSum() {
      return sum;
    }

    public void setSum(double sum) {
      this.sum = sum;
    }

    public double getTotalOff() {
      return totalOff;
    }

    public void setTotalOff(double totalOff) {
      this.totalOff = totalOff;
    }
  }
}
