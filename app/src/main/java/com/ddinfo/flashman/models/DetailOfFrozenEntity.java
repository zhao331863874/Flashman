package com.ddinfo.flashman.models;

import java.io.Serializable;

/**
 * Created by weitf on 2017/2/6.
 */
public class DetailOfFrozenEntity implements Serializable {

    /**
     * finishSate : 已完成
     * id : 1
     * numberId : DD201701091236
     * state : 已拒收
     * updatedAt : 2017-01-16 14:24:01
     */

    private String finishSate;
    private int id;
    private String numberId;
    private String state;
    private String updatedAt;

    public String getFinishSate() {
        return finishSate;
    }

    public void setFinishSate(String finishSate) {
        this.finishSate = finishSate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumberId() {
        return numberId;
    }

    public void setNumberId(String numberId) {
        this.numberId = numberId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
