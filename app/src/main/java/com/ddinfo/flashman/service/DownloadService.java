package com.ddinfo.flashman.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;
import android.util.Log;

import com.ddinfo.flashman.constant.ExampleConfig;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ailen on 2015/11/26.
 */
public class DownloadService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DownloadService(String name) {
        super(name);
    }

    public DownloadService() {
        super("DownloadService");
    }
    private int progress;
    private Context mContext = this;
    private static final String savePath = "/sdcard/updateApkDemo/";
    private static String saveFileName = savePath + "dianda_info.apk";
    private String apkUrl = null;
    public static final int UPDATE_PROGRESS = 8344;
    public static final int CANCLE_PROGRESS = 8345;
    public static final int FAIL_PROGRESS = 8346;
    /**
     * 安装apk
     *
     * @param
     */
    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);

    }

    private ResultReceiver receiver;

    @Override
    protected void onHandleIntent(Intent intent) {
        receiver = intent.getParcelableExtra("receiver");
        apkUrl = intent.getStringExtra("url");
        int code = intent.getIntExtra("versionCode", -1);
        saveFileName = savePath + "dianda_info.apk";
        if (code != -1) {
            saveFileName = savePath + "dianda_info_" + code + ".apk";
        }
        long total = 0;
        int fileLength = 0;
        try {
            URL url = new URL(apkUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty(
                    "Accept",
                    "image/gif,image/jpeg,image/pjpeg,application/x-shockwaveflash,application/x-ms-xbap,application/xaml+xml,application/vnd.ms-xpsdocument,application/x-ms-application,application/vnd.ms-excel,application/vnd.ms-powerpoint,application/msword,*/*");
            conn.setRequestProperty("Accept-Language", "zh-CN");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty(
                    "User-Agent",
                    "Mozilla/4.0(compatible;MSIE7.0;Windows NT 5.2;Trident/4.0;.NET CLR 1.1.4322;.NET CLR 2.0.50727;.NET CLR 3.0.04506.30;.NET CLR 3.0.4506.2152;.NET CLR 3.5.30729)");

            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.connect();
            fileLength = conn.getContentLength();

            InputStream input = new BufferedInputStream(conn.getInputStream());
            File file = new File(savePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            String apkFile = saveFileName;
            File ApkFile = new File(apkFile);
            OutputStream output = new FileOutputStream(ApkFile);
            byte data[] = new byte[1024];
           
            int count;
            progress = 0;
            while ((count = input.read(data)) != -1) {
                total += count;
                Bundle resultData = new Bundle();
                progress = (int) (total * 100 / fileLength);
                if (progress > 0) {
                    resultData.putInt("progress", progress);
                    receiver.send(UPDATE_PROGRESS, resultData);
                }
                output.write(data, 0, count);
                Log.d("下载进度", total + "");
            }

            output.flush();
            output.close();
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
            receiver.send(CANCLE_PROGRESS,null);
            return;
        }catch (Exception e){
            receiver.send(FAIL_PROGRESS,null);
            return;
        }
        if(fileLength==0||total<fileLength){
            Bundle resultData = new Bundle();
            resultData.putInt("progress", 0);
            receiver.send(FAIL_PROGRESS,resultData);
            return;
        }
        Bundle resultData = new Bundle();
        resultData.putInt("progress", 100);
        receiver.send(UPDATE_PROGRESS, resultData);
        installApk();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ExampleConfig.isDownload = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ExampleConfig.isDownload = false;
    }
}
