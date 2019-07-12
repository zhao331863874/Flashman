package com.ddinfo.flashman.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.provider.Settings;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shouliang on 2016/10/12.
 */
public class Utils {
    public static final String PATTERN_STANDARD08W = "yyyyMMdd";
    public static final String PATTERN_STANDARD12W = "yyyyMMddHHmm";
    public static final String PATTERN_STANDARD14W = "yyyyMMddHHmmss";
    public static final String PATTERN_STANDARD17W = "yyyyMMddHHmmssSSS";

    public static final String PATTERN_STANDARD10H = "yyyy-MM-dd";
    public static final String PATTERN_STANDARD16H = "yyyy-MM-dd HH:mm";
    public static final String PATTERN_STANDARD19H = "yyyy-MM-dd HH:mm:ss";

    public static final String PATTERN_STANDARD10X = "yyyy/MM/dd";
    public static final String PATTERN_STANDARD16X = "yyyy/MM/dd HH:mm";
    public static final String PATTERN_STANDARD19X = "yyyy/MM/dd HH:mm:ss";

    public static float limitValue(float a, float b) {
        float valve = 0;
        final float min = Math.min(a, b);
        final float max = Math.max(a, b);
        valve = valve > min ? valve : min;
        valve = valve < max ? valve : max;
        return valve;
    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent;
        if (android.os.Build.VERSION.SDK_INT > 10) {
            intent = new Intent(Settings.ACTION_SETTINGS);
        } else {
            intent = new Intent();
            ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        activity.startActivity(intent);
    }

    public static String jointNumFromat(double d) {
        return subZeroAndDot(numFormat(d)) + " 元";
    }

    /**
     * 使用java正则表达式去掉多余的.0
     * subZeroAndDot("1");  // 转换后为1
     * subZeroAndDot("10");  // 转换后为10
     * subZeroAndDot("1.0");  // 转换后为1
     * subZeroAndDot("1.010");  // 转换后为1.01
     * subZeroAndDot("1.01");  // 转换后为1.01
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }


    /**
     * 保留小数点后两位
     *
     * @param d
     * @return
     */
    public static String numFormat(double d) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d);
    }

    public static String numFromats(double d) {
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bd.toString();
    }

    public static String numFormatInt(double d) {
        DecimalFormat df = new DecimalFormat("0.");
        return df.format(d);
    }

    public static String num2Percen(double number) {
        NumberFormat nf = NumberFormat.getPercentInstance();
        nf.setMinimumFractionDigits(2);
        return nf.format(number);

    }

    /**
     * 判断是否是手机号
     *
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone) {
        Pattern pattern = Pattern
                .compile("^(1)\\d{10}$");
        Matcher matcher = pattern.matcher(phone);

        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是身份证
     * @param IdCard
     * @return
     */
    public static boolean checkIDCard(String IdCard) {
        return IdCard.length() == 18;
//        Pattern pattern = Pattern
//                .compile("^(\\d{14}|\\d{17})(\\d|[xX])$");
//        Matcher matcher = pattern.matcher(IdCard);
//
//        if (matcher.matches()) {
//            return true;
//        }
//        return false;
    }

    //转换日期格式
    public static String getWantDate(String dateStr, String wantFormat) {
        if (!"".equals(dateStr) && dateStr != null) {
            String pattern = PATTERN_STANDARD14W;
            int len = dateStr.length();
            switch (len) {
                case 8:
                    pattern = PATTERN_STANDARD08W;
                    break;
                case 12:
                    pattern = PATTERN_STANDARD12W;
                    break;
                case 14:
                    pattern = PATTERN_STANDARD14W;
                    break;
                case 17:
                    pattern = PATTERN_STANDARD17W;
                    break;
                case 10:
                    pattern = (dateStr.contains("-")) ? PATTERN_STANDARD10H : PATTERN_STANDARD10X;
                    break;
                case 16:
                    pattern = (dateStr.contains("-")) ? PATTERN_STANDARD16H : PATTERN_STANDARD16X;
                    break;
                case 19:
                    pattern = (dateStr.contains("-")) ? PATTERN_STANDARD19H : PATTERN_STANDARD19X;
                    break;
                default:
                    pattern = PATTERN_STANDARD14W;
                    break;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(wantFormat); //日期格式化显示
            try {
                SimpleDateFormat sdfStr = new SimpleDateFormat(pattern);
                Date date = sdfStr.parse(dateStr);
                dateStr = sdf.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dateStr;
    }

}
