package com.ddinfo.flashman.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.lang.reflect.Method;

/**
 * Created by dianda on 2016/7/6.
 */
public class PermissionsUtils {
    private static final int STORAGE = 1111;//请求内存的权限
    private static final int READSTORAGE = 1121;//请求内存的权限
    private static final int CAMERA = 2222;//请求打开相机的权限
    private static final int READ_PHONE_STATE = 3333;//请求获取设备信息权限
    private static NotifyListener listener;
    public static boolean isDenied = true;
    private static boolean isCanBack;

    private static void askPermissions(Activity activity, String permission, int requestCode, boolean isCanBack) {
        PermissionsUtils.isCanBack = isCanBack;
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                if (isDenied || !isCanBack)
                    ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
                else {
                    if (listener != null) {
                        listener.permissionFinish();
                    }
                }
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
            }
        } else {
            if (listener != null) {
                listener.permissionFinish();
            }
        }
    }

    public static void askStoragePermissions(Activity activity, NotifyListener notifyListener) {
        listener = notifyListener;
        askPermissions(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE, false);
    }

    public static void askCameraPermissions(Activity activity, NotifyListener notifyListener) {
        listener = notifyListener;
        askPermissions(activity, Manifest.permission.CAMERA, CAMERA, false);
    }

    public static void askStorageReadPermissions(Activity activity, NotifyListener notifyListener) {
        listener = notifyListener;
        askPermissions(activity, Manifest.permission.READ_EXTERNAL_STORAGE, READSTORAGE, false);
    }

    public static void askPhoneState(Activity activity, NotifyListener notifyListener) {
        listener = notifyListener;
        askPermissions(activity, Manifest.permission.READ_PHONE_STATE, READ_PHONE_STATE, true);
    }
//    public static void askCallPhonePermissions(Activity activity, NotifyListener notifyListener) {
//        listener = notifyListener;
//        askPermissions(activity, Manifest.permission.CALL_PHONE, CLAAPHONE);
//    }

    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            isDenied = true;
            if (listener != null) {
                listener.permissionFinish();
            }
        } else {
            isDenied = false;
            if (PermissionsUtils.isCanBack) {
                listener.permissionFinish();
            }
        }

    }

    public interface NotifyListener {
        void permissionFinish();
    }


    /**
     * 判断 悬浮窗口权限是否打开
     *
     * @param context
     * @return true 允许  false禁止
     */
    public static boolean getAppOps(Context context) {
        try {
            Object object = context.getSystemService(context.APP_OPS_SERVICE);
            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = Integer.valueOf(24);
            arrayOfObject1[1] = Integer.valueOf(Binder.getCallingUid());
            arrayOfObject1[2] = context.getPackageName();
            int m = ((Integer) method.invoke(object, arrayOfObject1)).intValue();
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception ex) {

        }
        return false;
    }

    /**
     * 请求用户给予悬浮窗的权限
     */
    public static void askForPermission(final Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context).setCancelable(false)
                        .setMessage("店达商城申请推送消息悬浮窗权限")
                        .setNegativeButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                        Uri.parse("package:" + context.getPackageName()));
                                context.startActivity(intent);
                            }
                        });
                builder.create().show();


            } else {

            }
        }
    }





}
