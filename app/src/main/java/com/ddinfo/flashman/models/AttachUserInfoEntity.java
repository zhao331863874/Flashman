package com.ddinfo.flashman.models;

import java.io.Serializable;

/**
 * Created by keyboard3 on 2018/7/10.
 */

public class AttachUserInfoEntity extends AttachUserInfo {
  public int confirmState;          //信息审核状态（0:未提交、1:审核中、2:已通过、3:未通过）
  public String confirmRefuseReason;  //拒绝的原因

  /**
   * 页面信息仅只读
   * @return
   */
  public boolean onlyShow(){
    return 1==confirmState || 2==confirmState;
  }
  public String getConfirmStateDesc() {
    if(0==confirmState) {
      return "请完善个人信息";
    } else if(1==confirmState) {
      return "您提交的信息正在审核中，请耐心等待！";
    } else if(2==confirmState) {
      return  "您提交的信息已通过审核！";
    } else if(3==confirmState) {
      return "审核未通过："+confirmRefuseReason;
    } else {
      return "不能识别该审核状态："+confirmState;
    }
  }
}
