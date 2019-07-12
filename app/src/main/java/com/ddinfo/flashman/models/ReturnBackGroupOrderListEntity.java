package com.ddinfo.flashman.models;

import java.util.List;

/**
 * 注释:
 * Created by weitf on 2017/12/26.
 * Email:weitengfei0212@gmail.com
 */

public class ReturnBackGroupOrderListEntity {


    /**
     * sum : 0
     * count : 0
     * details : [{"id":72,"goodId":137465,"packType":0,"produceTime":"2017-05-03T00:00:00.000Z","orgAmount":1,"realAmount":null,"realPrice":0.8,"specification":"100g","unit":"袋","name":"美美3"}]
     */

    private double sum;//实退退货金额
    private int count; //实退商品总数
    private List<ReturnBackGroupOrderEntity> details;

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ReturnBackGroupOrderEntity> getDetails() {
        return details;
    }

    public void setDetails(List<ReturnBackGroupOrderEntity> details) {
        this.details = details;
    }
}
