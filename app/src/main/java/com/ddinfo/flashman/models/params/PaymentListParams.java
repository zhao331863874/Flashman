package com.ddinfo.flashman.models.params;

/**
 * Created by fuh on 2017/5/15.
 * Email：unableApe@gmail.com
 */

public class PaymentListParams {
    private int limit;
    private int page;
    private long stamp;
    private int type;

    public PaymentListParams(int limit, int offset, long stamp, int type) {
        this.limit = limit;
        this.page = offset;
        this.stamp = stamp;
        this.type = type;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getOffset() {
        return page;
    }

    public void setOffset(int offset) {
        this.page = offset;
    }

    public long getStamp() {
        return stamp;
    }

    public void setStamp(long stamp) {
        this.stamp = stamp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
