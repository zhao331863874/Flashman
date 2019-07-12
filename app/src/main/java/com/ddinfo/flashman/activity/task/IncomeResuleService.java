package com.ddinfo.flashman.activity.task;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.utils.PollUtil;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * 支付结果查询服务
 */
public class IncomeResuleService extends Service {

    public static final String ACTION = "com.ddinfo.flashman.activity.task.IncomeResuleService";
    private static final int MES_CALLRESULT = 0x001;
    public static int count = 0;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case MES_CALLRESULT:
                    ToastUtils.showShortToast("new Message:"+msg.arg1);
                    if(msg.arg1 == 3){
                        PollUtil.stopPollingService(getApplicationContext(),IncomeResuleService.class,IncomeResuleService.ACTION);
                        Intent intent = new Intent(IncomeResuleService.this,TaskIncomeResultActivity.class);
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                    break;
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.i("IncomeAA","onBind:"+count);
        return null;
    }

    @Override
    public void onCreate() {
        LogUtils.i("IncomeAA","onCreate:"+count);
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.i("IncomeAA","onStartCommand:"+count);
        count++;
        if(count <= 3){
            Message message = new Message();
            message.what = MES_CALLRESULT;
            message.arg1 = count;
            handler.sendMessage(message);
        }
//        new PollingThread().start();
        return super.onStartCommand(intent, flags, startId);
    }

    class PollingThread extends Thread {

        @Override
        public void run() {
            count++;
            if(count <= 3){
                Message message = new Message();
                message.what = MES_CALLRESULT;
                message.arg1 = count;
                handler.sendMessage(message);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
