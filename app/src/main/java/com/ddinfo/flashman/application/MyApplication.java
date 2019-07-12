package com.ddinfo.flashman.application;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

import com.baidu.mapapi.SDKInitializer;
import com.blankj.utilcode.utils.SPUtils;
import com.blankj.utilcode.utils.Utils;
import com.ddinfo.flashman.BuildConfig;
import com.ddinfo.flashman.activity.base_frame.camera.ThumbBean;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.location.LocationService;
import com.ddinfo.flashman.network.WebConect;
import com.ddinfo.flashman.network.WebService;
import com.tencent.bugly.Bugly;

/**
 * Created by weitf
 */
public class MyApplication extends Application {
    public static Context context;
    public static WebService mWebservice = null;
    public LocationService locationService;
    public Vibrator mVibrator; //Android手机自带的振动器
    public static SPUtils spUtils;
    private static MyApplication app;
    public static ThumbBean storeThumBean = null;

    public static int StoreMessageId;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        context = getApplicationContext();
        mWebservice = WebConect.getInstance().getmWebService();
        initBugly();
        Utils.init(context);
        initLocalMapSdk();
        getSPUtilsInstance();
        ExampleConfig.token = MyApplication.getSPUtilsInstance().getString(ExampleConfig.TOKEN);
    }


    private void initBugly() { //初始化腾讯异常上报和运营统计
        Bugly.init(getApplicationContext(), ExampleConfig.BUGLY_APP_ID, BuildConfig.DEBUG);

    }

    public static MyApplication getMyApplication() {
        return app;
    }

    public static SPUtils getSPUtilsInstance() {
        if (spUtils == null) {
            spUtils = new SPUtils(ExampleConfig.SALES_SHARE);
        }
        return spUtils;
    }

    /***
     * 初始化定位sdk，建议在Application中创建
     */
    private void initLocalMapSdk() {
        locationService = new LocationService(getApplicationContext());
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE); //获得Vibrator实例
        SDKInitializer.initialize(getApplicationContext());
    }


}
