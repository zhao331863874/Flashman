package com.ddinfo.flashman.models.params;

/**
 * Created by fuh on 2017/2/6.
 * Email：unableApe@gmail.com
 */

public class SendCodeParams {
    private String phone;

    public SendCodeParams(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
