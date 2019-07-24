package com.ddinfo.flashman.models;

import java.util.List;

/**
 * Created by fuh on 2017/5/5.
 * Email：unableApe@gmail.com
 * 看板实体类
 */

public class BoardEntity {
    /**
     * months : 201703
     * num : 2
     * orderSum : 0.4
     * commissionSum : 0.2
     * dayList : [{"days":"20170329","num":2,"orderSum":0.4,"commissionSum":0.2}]
     */

    private String months; //月份
    private int num;       //配送单数
    private double orderSum; //月份累计总金额
    private double commissionSum; //收益
    private List<DayListBean> dayList;

    public String getMonths() {
        return months;
    }

    public void setMonths(String months) {
        this.months = months;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getOrderSum() {
        return orderSum;
    }

    public void setOrderSum(double orderSum) {
        this.orderSum = orderSum;
    }

    public double getCommissionSum() {
        return commissionSum;
    }

    public void setCommissionSum(double commissionSum) {
        this.commissionSum = commissionSum;
    }

    public List<DayListBean> getDayList() {
        return dayList;
    }

    public void setDayList(List<DayListBean> dayList) {
        this.dayList = dayList;
    }

    public static class DayListBean {
        /**
         * days : 20170329
         * num : 2
         * orderSum : 0.4
         * commissionSum : 0.2
         */

        private String days; //时间
        private int num;     //数量
        private double orderSum; //订单金额
        private double commissionSum; //收入

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public double getOrderSum() {
            return orderSum;
        }

        public void setOrderSum(double orderSum) {
            this.orderSum = orderSum;
        }

        public double getCommissionSum() {
            return commissionSum;
        }

        public void setCommissionSum(double commissionSum) {
            this.commissionSum = commissionSum;
        }
    }
}
