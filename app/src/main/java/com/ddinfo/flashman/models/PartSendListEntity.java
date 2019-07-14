package com.ddinfo.flashman.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Gavin on 2017/6/5.
 */

public class PartSendListEntity implements Serializable {

    /**
     * id : 513688
     * details : [{"id":2116005,"name":"美汁源果粒橙  1.25L*12瓶/箱","quantity":1,"snapshot":{"id":125375,"name":"美汁源果粒橙  1.25L*12瓶/箱","comboType":0,"onSellGoodsCombs":[{"goodId":110,"type":"0","amount":1,"isFree":false,"name":"美汁源果粒橙  1.25L*12瓶/箱","specification":"1.25L*12瓶","unit":"箱","bulkSpecification":"1.25L","bulkUnit":"瓶","barCode":"6956416200814","transProportion":12,"originalPrice":0,"price":0,"WarehouseId":17}]}}]
     * name : 无锡测试--店达
     * invoiceNumberId : FH-17-513688
     * deliveryOrderId : 10045
     */

    private int id;
    private String name;
    private String invoiceNumberId;
    private String deliveryOrderId; //送货订单ID
    private List<DetailsBean> details;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInvoiceNumberId() {
        return invoiceNumberId;
    }

    public void setInvoiceNumberId(String invoiceNumberId) {
        this.invoiceNumberId = invoiceNumberId;
    }

    public String getDeliveryOrderId() {
        return deliveryOrderId;
    }

    public void setDeliveryOrderId(String deliveryOrderId) {
        this.deliveryOrderId = deliveryOrderId;
    }

    public List<DetailsBean> getDetails() {
        return details;
    }

    public void setDetails(List<DetailsBean> details) {
        this.details = details;
    }

    public static class DetailsBean {
        /**
         * id : 2116005
         * name : 美汁源果粒橙  1.25L*12瓶/箱
         * quantity : 1
         * snapshot : {"id":125375,"name":"美汁源果粒橙  1.25L*12瓶/箱","comboType":0,"onSellGoodsCombs":[{"goodId":110,"type":"0","amount":1,"isFree":false,"name":"美汁源果粒橙  1.25L*12瓶/箱","specification":"1.25L*12瓶","unit":"箱","bulkSpecification":"1.25L","bulkUnit":"瓶","barCode":"6956416200814","transProportion":12,"originalPrice":0,"price":0,"WarehouseId":17}]}
         */

        private int id;            //商品ID
        private String name;       //商品名称
        private int quantity;      //商品所有的数量
        private int refuseQuantity;//添加字段 拒收数量
        private int giftFlag;

        public int getGiftFlag() {
            return giftFlag;
        }

        public void setGiftFlag(int giftFlag) {
            this.giftFlag = giftFlag;
        }

        public int getRefuseQuantity() {
            return refuseQuantity;
        }

        public void setRefuseQuantity(int refuseQuantity) {
            this.refuseQuantity = refuseQuantity;
        }

        private SnapshotBean snapshot;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public SnapshotBean getSnapshot() {
            return snapshot;
        }

        public void setSnapshot(SnapshotBean snapshot) {
            this.snapshot = snapshot;
        }

        public static class SnapshotBean {
            /**
             * id : 125375
             * name : 美汁源果粒橙  1.25L*12瓶/箱
             * comboType : 0
             * onSellGoodsCombs : [{"goodId":110,"type":"0","amount":1,"isFree":false,"name":"美汁源果粒橙  1.25L*12瓶/箱","specification":"1.25L*12瓶","unit":"箱","bulkSpecification":"1.25L","bulkUnit":"瓶","barCode":"6956416200814","transProportion":12,"originalPrice":0,"price":0,"WarehouseId":17}]
             */

            private int id;
            private String name;
            private int comboType;
            private List<OnSellGoodsCombsBean> onSellGoodsCombs;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getComboType() {
                return comboType;
            }

            public void setComboType(int comboType) {
                this.comboType = comboType;
            }

            public List<OnSellGoodsCombsBean> getOnSellGoodsCombs() {
                return onSellGoodsCombs;
            }

            public void setOnSellGoodsCombs(List<OnSellGoodsCombsBean> onSellGoodsCombs) {
                this.onSellGoodsCombs = onSellGoodsCombs;
            }

            public static class OnSellGoodsCombsBean {
                /**
                 * goodId : 110
                 * type : 0
                 * amount : 1
                 * isFree : false
                 * name : 美汁源果粒橙  1.25L*12瓶/箱
                 * specification : 1.25L*12瓶
                 * unit : 箱
                 * bulkSpecification : 1.25L
                 * bulkUnit : 瓶
                 * barCode : 6956416200814
                 * transProportion : 12
                 * originalPrice : 0
                 * price : 0
                 * WarehouseId : 17
                 */

                private int goodId;
                private String type;
                private int amount;
                private boolean isFree;
                private String name;
                private String specification;
                private String unit;
                private String bulkSpecification;
                private String bulkUnit;
                private String barCode;
                private int transProportion;
                private double originalPrice;
                private double price;
                private int WarehouseId;


                public int getGoodId() {
                    return goodId;
                }

                public void setGoodId(int goodId) {
                    this.goodId = goodId;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public int getAmount() {
                    return amount;
                }

                public void setAmount(int amount) {
                    this.amount = amount;
                }

                public boolean isIsFree() {
                    return isFree;
                }

                public void setIsFree(boolean isFree) {
                    this.isFree = isFree;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
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

                public String getBulkSpecification() {
                    return bulkSpecification;
                }

                public void setBulkSpecification(String bulkSpecification) {
                    this.bulkSpecification = bulkSpecification;
                }

                public String getBulkUnit() {
                    return bulkUnit;
                }

                public void setBulkUnit(String bulkUnit) {
                    this.bulkUnit = bulkUnit;
                }

                public String getBarCode() {
                    return barCode;
                }

                public void setBarCode(String barCode) {
                    this.barCode = barCode;
                }

                public int getTransProportion() {
                    return transProportion;
                }

                public void setTransProportion(int transProportion) {
                    this.transProportion = transProportion;
                }

                public double getOriginalPrice() {
                    return originalPrice;
                }

                public void setOriginalPrice(double originalPrice) {
                    this.originalPrice = originalPrice;
                }

                public double getPrice() {
                    return price;
                }

                public void setPrice(double price) {
                    this.price = price;
                }

                public int getWarehouseId() {
                    return WarehouseId;
                }

                public void setWarehouseId(int WarehouseId) {
                    this.WarehouseId = WarehouseId;
                }
            }
        }
    }
}
