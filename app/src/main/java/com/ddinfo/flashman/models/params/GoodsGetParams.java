package com.ddinfo.flashman.models.params;

/**
 * Created by fuh on 2017/2/8.
 * Email：unableApe@gmail.com
 */

public class GoodsGetParams {
    private String QRCode;

    public GoodsGetParams(String QRCode) {
        this.QRCode = QRCode;
    }

    public String getQRCode() {
        return QRCode;
    }

    public void setQRCode(String QRCode) {
        this.QRCode = QRCode;
    }
}
