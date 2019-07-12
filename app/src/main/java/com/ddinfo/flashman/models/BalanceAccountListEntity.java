package com.ddinfo.flashman.models;

import java.io.Serializable;

/**
 * Created by weitf on 2017/2/9.
 */
public class BalanceAccountListEntity implements Serializable {

    /**
     * accountNo : 第三方账户编号
     * accountType : 账户类型
     * id : 账户ID
     * userName : 第三方用户名
     */

    private String accountNo;
    private String accountType;
    private String id;
    private String userName;

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
