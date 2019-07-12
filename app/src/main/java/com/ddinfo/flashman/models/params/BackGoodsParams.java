package com.ddinfo.flashman.models.params;

import java.util.List;

/**
 * 注释:
 * Created by weitf on 2017/12/25.
 * Email:weitengfei0212@gmail.com
 */

public class BackGoodsParams {
    int backOrderId;
    List<Detail> detail;

    public BackGoodsParams() {
    }

    public BackGoodsParams(int backOrderId, List<Detail> detail) {
        this.backOrderId = backOrderId;
        this.detail = detail;
    }

    public int getBackOrderId() {
        return backOrderId;
    }

    public void setBackOrderId(int backOrderId) {
        this.backOrderId = backOrderId;
    }

    public List<Detail> getDetail() {
        return detail;
    }

    public void setDetail(List<Detail> detail) {
        this.detail = detail;
    }

    public static class Detail {
        int goodId;
        int id;
        int quantity;
        int realSum;

        public Detail(int goodId, int id, int quantity) {
            this.goodId = goodId;
            this.id = id;
            this.quantity = quantity;
        }

        public int getGoodId() {
            return goodId;
        }

        public void setGoodId(int goodId) {
            this.goodId = goodId;
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

        public int getRealSum() {
            return realSum;
        }

        public void setRealSum(int realSum) {
            this.realSum = realSum;
        }
    }
}
