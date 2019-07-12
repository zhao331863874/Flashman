package com.ddinfo.flashman.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Title: 加载框
 * Created by fuh on 2017/1/19.
 * Email：unableApe@gmail.com
 */

public class ProDialogHelps {
    public static final int SHOWPRODIALOG = 11111;
    public static final int REMOVEPRODIALOG = 10001;

    public static final String PRODIALOG_CONTENT = "PRODIALOG_CONTENT";

    private Context context;

    private ProgressDialog progressDialog;

    Handler handler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case SHOWPRODIALOG:
                        progressDialog = ProgressDialog.show(context, null, msg.getData().getString(PRODIALOG_CONTENT, "加载中，请稍候..."), false, true);
                        break;
                    case REMOVEPRODIALOG:  //移除加载框
                        if(progressDialog!=null){
                            progressDialog.dismiss();
                        }
                        break;
                }
                super.handleMessage(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public ProDialogHelps(Context context) {
        this.context = context;
    }

    public void showProDialog() {
        Message msg = new Message();
        msg.what = SHOWPRODIALOG;
        handler.sendMessage(msg);
    }

    public void showProDialog(String proDialogContent) {
        Message msg = new Message();
        Bundle bundle = new Bundle();
        bundle.putString(PRODIALOG_CONTENT, proDialogContent);
        msg.what = SHOWPRODIALOG;
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
    //移除加载框
    public void removeProDialog() {
        Message msg = new Message();
        msg.what = REMOVEPRODIALOG;
        handler.sendMessage(msg);

    }
}

