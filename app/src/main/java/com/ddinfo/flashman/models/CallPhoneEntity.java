package com.ddinfo.flashman.models;

/**
 * Created by fuh on 2017/2/9.
 * Email：unableApe@gmail.com
 */

public class CallPhoneEntity {
    private String telephone;
    private String email;

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
