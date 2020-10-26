package com.chance.mimorobot.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.chance.mimorobot.R;
import com.chance.mimorobot.utils.MyWebViewClient;
import com.github.lzyzsd.jsbridge.BridgeWebView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import butterknife.BindView;
import butterknife.OnClick;

public class WebActivity extends TitleBarActivity {
    private String TAG=WebActivity.class.getSimpleName();

    @BindView(R.id.webview)
    BridgeWebView webView;


    public static Intent getIntent(Context context, String json) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra("data", json);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String source1 = "http://www.qijie.mimm.co/showpage/index.html";
        if (!TextUtils.isEmpty(getIntent().getStringExtra("data"))){
            source1=getIntent().getStringExtra("data");
        }
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setWebChromeClient(new WebChromeClient());
//        webView.setWebViewClient(new MyWebViewClient(webView) );
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setGeolocationEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
//        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");
        webView.setWebViewClient(new MyWebViewClient(webView) {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                Log.e(TAG, "shouldInterceptRequest->" + request.getUrl().getScheme() + "  "
                        + request.getUrl().getHost() + "  " + request.getUrl().getPath() + "  " + request.getUrl().getQuery() + "  " + request.getUrl().getFragment());

                // 过滤中国政法服务网的数据
                return super.shouldInterceptRequest(view, request);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                view.stopLoading();
                view.clearView();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                view.loadUrl("javascript:window.java_obj.getSource('<html>'+" +
//                        "document.getElementsByTagName('html')[0].innerHTML+'</html>');");

            }
        });
        webView.loadUrl(source1);
    }

    @Override
    int getContentLayoutId() {
        return R.layout.activity_web;
    }

    @OnClick(R.id.webview)
    public void onViewClicked() {
    }
}
