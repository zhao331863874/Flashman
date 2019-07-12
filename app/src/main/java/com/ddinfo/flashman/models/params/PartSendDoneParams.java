package com.ddinfo.flashman.models.params;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;


/**
 * Created by Gavin on 2017/6/6.
 */

public class PartSendDoneParams implements Serializable {
    private String deliveryOrderId;
    private String receiveType;
    private String details;
    private String reason;
    private String receiptCode;

    public PartSendDoneParams() {
    }

    public PartSendDoneParams(String deliveryOrderId, String receiveType, String details, String reason, String receiptCode) {
        this.deliveryOrderId = deliveryOrderId;
        this.receiveType = receiveType;
        this.details = details;
        this.reason = reason;
        this.receiptCode = receiptCode;
    }

    public String getDeliveryOrderId() {
        return deliveryOrderId;
    }

    public void setDeliveryOrderId(String deliveryOrderId) {
        this.deliveryOrderId = deliveryOrderId;
    }

    public String getReceiveType() {
        return receiveType;
    }

    public void setReceiveType(String receiveType) {
        this.receiveType = receiveType;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getReceiptCode() {
        return receiptCode;
    }

    public void setReceiptCode(String receiptCode) {
        this.receiptCode = receiptCode;
    }
}