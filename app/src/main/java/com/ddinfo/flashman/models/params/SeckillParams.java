package com.ddinfo.flashman.models.params;

/**
 * Created by fuh on 2017/2/7.
 * Email：unableApe@gmail.com
 */

public class SeckillParams {
    private int deliveryOrderId;

    public SeckillParams(int deliveryOrderId) {
        this.deliveryOrderId = deliveryOrderId;
    }

    public int getDeliveryOrderId() {
        return deliveryOrderId;
    }

    public void setDeliveryOrderId(int deliveryOrderId) {
        this.deliveryOrderId = deliveryOrderId;
    }
}
