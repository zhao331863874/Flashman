package com.ddinfo.flashman.utils;

import android.app.Activity;

import com.ddinfo.flashman.activity.menu.wallet.WalletActivity;
import com.ddinfo.flashman.activity.tab_frame.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author weitf
 * @version 创建时间：2015-3-4 上午11:32:23 通过记录栈完全退出程序
 * @E-mail:weitengfei58@gmail.com
 */
public class ExitUtil {
    // 定义一个activity列表
    private List<Activity> mList = new ArrayList<Activity>();

    // 顶一一个类的实例
    private static ExitUtil instance;

    // 私有构造方法 不允许创建类的实例
    private ExitUtil() {
    }

    /**
     * 单例模式
     *
     * @return
     */
    public static ExitUtil getInstance() {
        if (null == instance) {
            instance = new ExitUtil();
        }
        return instance;
    }

    /**
     * 如果activity已经 destory了 就移除
     *
     * @param activity
     */
    public void remove(Activity activity) {
        mList.remove(activity);
    }

    /**
     * 添加ativity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    /**
     * 遍历 结束activity 并且退出
     *
     * @throws Exception
     */
    public void exit() throws Exception {
        for (Activity activity : mList) {
            if (activity != null) {
                activity.finish();
            }
        }
    }

    /**
     * 遍历 充值成功 后跳转到 钱包页面
     *
     * @throws Exception
     */
    public void exitToWalletActivity() {
        try {
            for (Activity activity : mList) {
                if (activity != null && activity.getClass() != WalletActivity.class && activity.getClass() != MainActivity.class) {
                    activity.finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
