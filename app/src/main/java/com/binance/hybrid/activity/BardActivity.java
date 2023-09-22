package com.binance.hybrid.activity;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;

import com.bard.android.bridge.MagicalJourney;
import com.bard.android.webview.BardWebView;
import com.bard.android.webview.BardWebViewPool;
import com.binance.hybrid.R;


public class BardActivity extends ComponentActivity {
    FrameLayout frameLayout;
    BardWebView webView;
    String url = "https://www.baidu.com/";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        frameLayout = findViewById(R.id.webView_container);

        BardWebViewPool.getInstance().initWebViewPool(getApplicationContext());

        webView = BardWebViewPool.getInstance().getWebView(false);
        webView.loadUrl(url);

        MagicalJourney brige = new MagicalJourney(webView);
        webView.setBridge(brige);

        frameLayout.addView(webView);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        frameLayout.removeAllViews();
        BardWebViewPool bardWebViewPool = BardWebViewPool.getInstance();
        bardWebViewPool.resetWebView(webView);
    }
}
