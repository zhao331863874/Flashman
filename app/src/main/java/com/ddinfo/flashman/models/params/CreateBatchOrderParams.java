package com.ddinfo.flashman.models.params;

import java.util.List;

/**
 * Created by fuh on 2017/5/15.
 * Email：unableApe@gmail.com
 */

public class CreateBatchOrderParams {
    private List<Integer> ids;
    private String paySum;

    public CreateBatchOrderParams(List<Integer> ids, String paySum) {
        this.ids = ids;
        this.paySum = paySum;
    }

    public List<Integer> getIds() {
        return ids;
    }

    public void setIds(List<Integer> ids) {
        this.ids = ids;
    }

    public String getPaySum() {
        return paySum;
    }

    public void setPaySum(String paySum) {
        this.paySum = paySum;
    }
}
