package com.ddinfo.flashman.models;

import java.io.Serializable;

/**
 * Created by weitf on 16/4/7.
 */
public class BaseResponseEntity<T> implements Serializable {
    private String tag;
    private int status;
    private String message;
    private T data;   //请求返回数据
    private int count;//商品总数量

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
