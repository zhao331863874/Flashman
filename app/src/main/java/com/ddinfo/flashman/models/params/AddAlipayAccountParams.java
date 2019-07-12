package com.ddinfo.flashman.models.params;

/**
 * 押金转入
 * 请求参数列表
 */
public class AddAlipayAccountParams {
    /**
     * account	账号	string
     realName	真实姓名
     */
    private String account;
    private String realName;

    public AddAlipayAccountParams(String account, String realName) {
        this.account = account;
        this.realName = realName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
