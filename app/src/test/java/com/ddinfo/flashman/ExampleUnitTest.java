package com.ddinfo.flashman;

import android.os.SystemClock;

import com.blankj.utilcode.utils.LogUtils;
import com.ddinfo.flashman.models.AttachUserInfoEntity;
import com.ddinfo.flashman.utils.GsonTools;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        AttachUserInfoEntity userInfoEntity = new AttachUserInfoEntity();
        userInfoEntity.realName = "真实姓名";
        userInfoEntity.IDCard = "身份证号";
        userInfoEntity.deliveryAddress = "家庭住址";
        userInfoEntity.bankName = "开户银行";
        userInfoEntity.bankCountID = "1234567891234567";
        userInfoEntity.bankCountUser = "户主姓名";
        userInfoEntity.urgencyName = "";
        userInfoEntity.urgencyPhone = "157571012345";
        userInfoEntity.urgencyShip = "";
        userInfoEntity.confirmState = "1";
        userInfoEntity.confirmRefuseReason = "拒绝原因";
        String gsonStr = GsonTools.buildJsonStr(userInfoEntity);
        System.out.print("gsonStr:"+gsonStr);
        Map<String,String>  map = GsonTools.JsonToMapString(gsonStr);
        int a = 1;
    }
}