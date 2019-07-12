package com.ddinfo.flashman.models.params;

/**
 * Created by keyboard3 on 2018/6/25.
 */

public class WithdrawCashParams {
  private int offset; //0
  private int limit; //10
  private int type; //-1  非必须字段 -3: 违约金 -2: 充值押金 -1: 提现 1: 配送收入 2: 押金转余额
  private int target; //1 非必须字段 0:余额(默认值) 1:押金

  public WithdrawCashParams(int offset, int limit, int type, int target) {
    this.offset = offset;
    this.limit = limit;
    this.type = type;
    this.target = target;
  }

  public int getOffset() {
    return offset;
  }

  public void setOffset(int offset) {
    this.offset = offset;
  }

  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    this.limit = limit;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public int getTarget() {
    return target;
  }

  public void setTarget(int target) {
    this.target = target;
  }
}
