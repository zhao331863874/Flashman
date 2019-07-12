package com.ddinfo.flashman.models.params;

/**
 * Created by fuh on 2017/2/6.
 * Email：unableApe@gmail.com
 */

public class RegisterParams {
    private String phone;
    private String validateCode;
    private String realName;
    private String IDCard;

    public RegisterParams(String phone, String validateCode, String realName, String IDCard) {
        this.phone = phone;
        this.validateCode = validateCode;
        this.realName = realName;
        this.IDCard = IDCard;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }
}
