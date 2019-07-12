package com.ddinfo.flashman.network;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.ddinfo.flashman.activity.LoginActivity;
import com.ddinfo.flashman.models.BaseResponseEntity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by fuh on 2017/3/30.
 * Email：unableApe@gmail.com
 */

public abstract class SimpleCallBack<T extends BaseResponseEntity> implements Callback<T> {
    private Context context;

    public SimpleCallBack(Context context) {
        this.context = context;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) { //请求成功时回调
        try {
            onProDialogDismiss();
            if (response.isSuccessful() && response.body() != null) {
                if (response.body().getStatus() == 1) {
                    onSuccess(call, response);
                } else if (response.body().getStatus() == 401) {
                    onTokenLose(call, response);
                } else {
                    onError(call, response);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();//为处理弱网环境，页面关闭，请求回调渲染组件报错
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) { //请求失败时回调
        try {
            onFail(call, t);
        } catch (Exception e) {
            e.printStackTrace();//为处理弱网环境，页面关闭，请求回调渲染组件报错
        }
    }
    //请求成功
    public abstract void onSuccess(Call<T> call, Response<T> response);
    //Token过期
    public void onTokenLose(Call<T> call, Response<T> response) {
        if (!TextUtils.isEmpty(response.body().getMessage())) {
            ToastUtils.showShortToast(response.body().getMessage());
        }
        context.startActivity(new Intent(context, LoginActivity.class));
    }
    //请求Error
    public void onError(Call<T> call, Response<T> response) {
        if (!TextUtils.isEmpty(response.body().getMessage())) {
            ToastUtils.showShortToast(response.body().getMessage());
        }
    }
    //移除加载框
    public abstract void onProDialogDismiss();
    //请求失败
    public void onFail(Call<T> call, Throwable t){
        ToastUtils.showShortToast(t.toString());
        onProDialogDismiss();
    }
}
