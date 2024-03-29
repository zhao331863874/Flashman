package com.ddinfo.flashman.models;

import java.util.List;

/**
 * Title: 请输入标题
 * Created by fuh on 2017/2/14.
 * Email：unableApe@gmail.com
 * 历史任务订单实体类
 */

public class SeckillOrderEntity {


    /**
     * todayOrderCount : 0
     * allOrderCount : 0
     * count : 0
     * list : []
     */

    private int todayOrderCount; //今日完成订单数量
    private int allOrderCount;
    private int count;
    private String thisMonthOrderCount; //本月累计完成订单数量
    private String preMonthOrderCount;  //上月累计完成订单数量
    private List<SeckillOrderList> list;

    public String getThisMonthOrderCount() {
        return thisMonthOrderCount;
    }

    public void setThisMonthOrderCount(String thisMonthOrderCount) {
        this.thisMonthOrderCount = thisMonthOrderCount;
    }

    public String getPreMonthOrderCount() {
        return preMonthOrderCount;
    }

    public void setPreMonthOrderCount(String preMonthOrderCount) {
        this.preMonthOrderCount = preMonthOrderCount;
    }

    public int getTodayOrderCount() {
        return todayOrderCount;
    }

    public void setTodayOrderCount(int todayOrderCount) {
        this.todayOrderCount = todayOrderCount;
    }

    public int getAllOrderCount() {
        return allOrderCount;
    }

    public void setAllOrderCount(int allOrderCount) {
        this.allOrderCount = allOrderCount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<SeckillOrderList> getList() {
        return list;
    }

    public void setList(List<SeckillOrderList> list) {
        this.list = list;
    }
}
