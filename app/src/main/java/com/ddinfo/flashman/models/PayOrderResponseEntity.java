package com.ddinfo.flashman.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by weitf on 2017/2/13.
 */
public class PayOrderResponseEntity implements Serializable {
    String codeUrl;
    public String tradeRecordNo;
    /**
     * appid : wx0760d57c26778206
     * partnerid : 1439347102
     * timestamp : 1486799199
     * noncestr : Di2tSrzKOYL5bexd
     * prepayid : wx201702111547050bd63acd530746943759
     * package : Sign=WXPay
     * sign : B36C4DC3F107904C22B29BAE7E98EE4A
     */

    private AppParamsBean appParams;

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

    public AppParamsBean getAppParams() {
        return appParams;
    }

    public void setAppParams(AppParamsBean appParams) {
        this.appParams = appParams;
    }

    public static class AppParamsBean {
        private String appid;
        private String partnerid;
        private String timestamp;
        private String noncestr;
        private String prepayid;
        @SerializedName("package")
        private String packageX;
        private String sign;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
    }
}
