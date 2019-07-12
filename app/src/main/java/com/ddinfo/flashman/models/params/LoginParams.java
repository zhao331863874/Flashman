package com.ddinfo.flashman.models.params;

/**
 * Title: 登录
 * Created by fuh on 2017/1/19.
 * Email：unableApe@gmail.com
 */

public class LoginParams {
    private String phone; //手机号
    private String validateCode; //验证码

    public LoginParams(String phone, String validateCode) {
        this.phone = phone;
        this.validateCode = validateCode;
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
