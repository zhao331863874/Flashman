package com.ddinfo.flashman.activity.webview;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.blankj.utilcode.utils.NetworkUtils;
import com.ddinfo.flashman.R;
import com.ddinfo.flashman.activity.base_frame.BaseActivity;
import com.ddinfo.flashman.constant.ExampleConfig;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

//import com.blankj.utilcode.utils.ToastUtils;

/**
 * 余额明细界面
 */
@SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
public class WebViewClientActivity extends BaseActivity {
    @Bind(R.id.webview)
    WebView webView;
    View navigationView;
    private ProgressDialog mDialogLoad = null;
    private String urlStr = ""; //余额明细Web url地址
    private String title = "";  //标题抬头

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        navigationView = findViewById(R.id.navigation_bar);
        urlStr = getIntent().getStringExtra(ExampleConfig.WB_URL_KEY);
        title = getIntent().getStringExtra(ExampleConfig.WB_NAV_TITLE_KEY);
        setTitle(title);
        initDialog();
        initWebViewAndSettings();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_webview_client_layout;
    }

    private void initWebViewAndSettings() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setJavaScriptEnabled(true);  //支持js
        webSettings.setSupportZoom(false);  //支持缩放
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); //支持内容重新布局
        webSettings.supportMultipleWindows();  //多窗口
        if (NetworkUtils.isConnected()) {
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。
        } else if (Build.VERSION.SDK_INT >= 19) {
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//没网，则从本地获取，即离线加载
        }
        webSettings.setAllowFileAccess(true);  //设置可以访问文件
        webSettings.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
        webSettings.setBuiltInZoomControls(true); //设置支持缩放
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setLoadsImagesAutomatically(true);  //支持自动加载图片
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", ExampleConfig.token);
        webView.loadUrl(urlStr, headerMap);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                onShowDialog("");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                onCancel();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                handler.proceed();
            }
        });
        webView.addJavascriptInterface(WebViewClientActivity.this, "WebViewJsObject");
    }

    @JavascriptInterface
    public void onFinish() {
        setResult(RESULT_OK);
        finish();
    }

    @JavascriptInterface
    public void onToast(String msg) {
        Toast.makeText(WebViewClientActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void onShowDialog(String msg) {
        if (mDialogLoad != null && !mDialogLoad.isShowing()) {
            if (!TextUtils.isEmpty(msg)) {
                mDialogLoad.setMessage(msg);
            }
            mDialogLoad.show();
        }
    }

    @JavascriptInterface
    public void onCancel() {
        if (mDialogLoad != null) {
            mDialogLoad.cancel();
        }
    }


    private void initDialog() {
        mDialogLoad = new ProgressDialog(WebViewClientActivity.this);
        mDialogLoad.setProgressStyle(ProgressDialog.STYLE_SPINNER); //设置进度条的形式为圆形转动的进度条
        mDialogLoad.setCancelable(true); //设置是否可以通过点击Back键取消
        mDialogLoad.setCanceledOnTouchOutside(false); //设置在点击Dialog外是否取消Dialog进度条
        mDialogLoad.setMessage("正在加载...");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && !(webView.canGoBack())) {
            finish();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack(); //返回上一页
            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {

        try {
            //清空所有Cookie
            CookieSyncManager.createInstance(WebViewClientActivity.this);  //Create a singleton CookieSyncManager within a context
            CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
            cookieManager.removeAllCookie();// Removes all cookies.
//            CookieSyncManager.getInstance().sync(); // forces sync manager to sync now

            if (Build.VERSION.SDK_INT >= 21) CookieManager.getInstance().flush();
            else CookieSyncManager.getInstance().sync();

//            webView.setWebChromeClient(null);

            webView.loadUrl("javascript:localStorage.clear()");
            webView.setWebViewClient(null);
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearCache(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }


    @OnClick({R.id.left_button, R.id.right_button, R.id.rightBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button: //点击返回
//                finish();
                if (!(webView.canGoBack())) {
                    finish();
                } else if (webView.canGoBack()) {
                    webView.goBack(); //返回上一页
                }
                break;
            case R.id.right_button:
                break;
            case R.id.rightBtn:
                break;
        }
    }

}

