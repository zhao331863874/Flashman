package com.ddinfo.flashman.models;

import java.io.Serializable;

/**
 * Created by keyboard3 on 2018/7/10.
 */

public class AttachUserInfo implements Serializable {
  public String realName;             //真实姓名
  public String IDCard;               //身份证号
  public String deliveryAddress;      //家庭住址
  public String bankName;             //开户银行
  public String bankCountID;          //银行卡号
  public String bankCountUser;        //户主姓名
  public String urgencyName;          //紧急联系人
  public String urgencyPhone;         //紧急联系人手机
  public String urgencyShip;          //紧急联系人关系
}
