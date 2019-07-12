package com.ddinfo.flashman.network;


import android.content.Intent;

import com.blankj.utilcode.utils.AppUtils;
import com.blankj.utilcode.utils.ToastUtils;
import com.blankj.utilcode.utils.Utils;
import com.ddinfo.flashman.BuildConfig;
import com.ddinfo.flashman.activity.LoginActivity;
import com.ddinfo.flashman.application.MyApplication;
import com.ddinfo.flashman.constant.ExampleConfig;
import com.ddinfo.flashman.constant.UrlConstant;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 网络接口请求
 */
public class WebConect {
    private static WebConect webConectInstance = new WebConect();

    public void setmWebService(WebService mWebService) {
        this.mWebService = mWebService;
    }

    private WebService mWebService;

    public Retrofit getRetrofit() {
        return retrofit;
    }

    private Retrofit retrofit = null; //Retrofit 网络请求框架

    private OkHttpClient client =null;

    public WebConect() {
    }

    public static WebConect getInstance() {
        return webConectInstance;
    }

    public WebService getmWebService() {
        if (mWebService == null) {
            initOkHttpInterceptor();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.API_SERVER_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create()) //对服务器的数据进行解析
                    .build();
            mWebService = retrofit.create(WebService.class);
        }
        return mWebService;
    }

    private void initOkHttpInterceptor(){
        Interceptor mTokenInterceptor = new Interceptor() { //网络拦截器
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                if ( MyApplication.getSPUtilsInstance().getString(ExampleConfig.TOKEN) == null) {
                    return chain.proceed(originalRequest);
                }
                Request authorised = originalRequest.newBuilder()
                        .header("Authorization",  MyApplication.getSPUtilsInstance().getString(ExampleConfig.TOKEN)) //token
                        .addHeader("Platform", "android") //平台
                        .addHeader("Version",AppUtils.getAppVersionName(Utils.getContext())) //App版本号
                        .build();
                Response response = chain.proceed(authorised);
                if (response.code() == 401) {
                    ToastUtils.showShortToast("您的账号信息已失效或在其他设备登录，请重新登录");
                    Utils.getContext().startActivity(new Intent(Utils.getContext(),LoginActivity.class));
                }
                return response;
            }
        };

        client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)                //是否自动重连
                .connectTimeout(15, TimeUnit.SECONDS) //设置连接超时
                .addNetworkInterceptor(mTokenInterceptor)      //添加网络拦截器
                .build();
    }

//    private void initOkHttpInterceptor(){
//        client = new OkHttpClient.Builder()
//                .retryOnConnectionFailure(true)
//                .connectTimeout(15, TimeUnit.SECONDS)
//                .addNetworkInterceptor(mTokenInterceptor)
//                .authenticator(mAuthenticator)
//                .build();
//        Interceptor mTokenInterceptor = new Interceptor() {
//            @Override public Response intercept(Chain chain) throws IOException {
//                Request originalRequest = chain.request();
//                if (Your.sToken == null || alreadyHasAuthorizationHeader(originalRequest)) {
//                    return chain.proceed(originalRequest);
//                }
//                Request authorised = originalRequest.newBuilder()
//                        .header("Authorization", Your.sToken)
//                        .build();
//                return chain.proceed(authorised);
//            }
//        };
//        Authenticator mAuthenticator = new Authenticator() {
//            @Override public Request authenticate(Route route, Response response)
//                    throws IOException {
//                Your.sToken = mWebService.refreshToken();
//                return response.request().newBuilder()
//                        .addHeader("Authorization", Your.sToken)
//                        .build();
//            }
//        };
//    }
}
