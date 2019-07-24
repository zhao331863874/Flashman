package com.ddinfo.flashman.models;

/**
 * Created by fuh on 2017/2/9.
 * Email：unableApe@gmail.com
 */

public class CallPhoneEntity {
    private String telephone; //客服电话
    private String email;     //客服邮箱

    public CallPhoneEntity(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
