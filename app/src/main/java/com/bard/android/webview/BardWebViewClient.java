package com.bard.android.webview;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/* loaded from: classes27.dex */
public class BardWebViewClient extends WebViewClient {
    private BardWebView mWebView;

    public BardWebViewClient(BardWebView webView) {
        this.mWebView = webView;
    }

    @Override // android.webkit.WebViewClient
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        super.doUpdateVisitedHistory(view, url, isReload);
        if (this.mWebView.isNeedClearHistory()) {
            this.mWebView.setNeedClearHistory(false);
            this.mWebView.clearHistory();
        }
    }
}
