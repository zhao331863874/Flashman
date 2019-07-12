package com.ddinfo.flashman.models.params;

/**
 * 押金转入
 * 请求参数列表
 */
public class DepositInParams {
    private double sum;
    private String validateCode;
    private String accountNo;
    private String userName;
    private int target; // 提现类型 0:余额提现， 1:押金提现(必须提供， 原来的余额提现需添加target:0)

    public DepositInParams(double sum, String validateCode, String accountNo, String userName, int target) {
        this.sum = sum;
        this.validateCode = validateCode;
        this.accountNo = accountNo;
        this.userName = userName;
        this.target = target;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public DepositInParams(double sum) {
        this.sum = sum;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public String getValidateCode() {
        return validateCode;
    }

    public void setValidateCode(String validateCode) {
        this.validateCode = validateCode;
    }
}
