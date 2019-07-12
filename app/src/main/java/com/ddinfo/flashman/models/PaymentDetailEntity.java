package com.ddinfo.flashman.models;

import java.util.List;

/**
 * Created by fuh on 2017/5/12.
 * Email：unableApe@gmail.com
 */

public class PaymentDetailEntity {
    /**
     * info : {"batchId":"goods_pay1020001","id":1,"stateName":"代缴订单","sum":66,"time":"2017-05-09 15:55:33","tradeRecordNo":1}
     * orderDetail : [{"DeliveryOrderId":1111,"numberId":"DD111","orderAmount":11},{"DeliveryOrderId":2222,"numberId":"DD112","orderAmount":22},{"DeliveryOrderId":3333,"numberId":"DD113","orderAmount":33}]
     */

    private InfoBean info;
    private List<OrderDetailBean> orderDetail;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public List<OrderDetailBean> getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(List<OrderDetailBean> orderDetail) {
        this.orderDetail = orderDetail;
    }

    public static class InfoBean {
        /**
         * batchId : goods_pay1020001
         * id : 1
         * stateName : 代缴订单
         * sum : 66
         * time : 2017-05-09 15:55:33
         * tradeRecordNo : 1
         */

        private String batchId;
        private int id;
        private String state;
        private double sum;
        private String time;
        private String tradeRecordNo;
        private String modifyTime;

        public String getModifyTime() {
            return modifyTime;
        }

        public void setModifyTime(String modifyTime) {
            this.modifyTime = modifyTime;
        }

        public String getBatchId() {
            return batchId;
        }

        public void setBatchId(String batchId) {
            this.batchId = batchId;
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

        public String getTradeRecordNo() {
            return tradeRecordNo;
        }

        public void setTradeRecordNo(String tradeRecordNo) {
            this.tradeRecordNo = tradeRecordNo;
        }
    }

    public static class OrderDetailBean {
        /**
         * DeliveryOrderId : 1111
         * numberId : DD111
         * orderAmount : 11
         */

        private int DeliveryOrderId;
        private String numberId;
        private double orderAmount;
        private String invoiceNumberId;

        public String getInvoiceNumberId() {
            return invoiceNumberId;
        }

        public void setInvoiceNumberId(String invoiceNumberId) {
            this.invoiceNumberId = invoiceNumberId;
        }

        public int getDeliveryOrderId() {
            return DeliveryOrderId;
        }

        public void setDeliveryOrderId(int DeliveryOrderId) {
            this.DeliveryOrderId = DeliveryOrderId;
        }

        public String getNumberId() {
            return numberId;
        }

        public void setNumberId(String numberId) {
            this.numberId = numberId;
        }

        public double getOrderAmount() {
            return orderAmount;
        }

        public void setOrderAmount(double orderAmount) {
            this.orderAmount = orderAmount;
        }
    }
}
