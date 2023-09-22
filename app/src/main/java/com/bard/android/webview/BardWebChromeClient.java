package com.bard.android.webview;

import android.webkit.WebChromeClient;
import android.webkit.WebView;

/* loaded from: classes27.dex */
public class BardWebChromeClient extends WebChromeClient {
    private BardWebView mWebView;

    public BardWebChromeClient(BardWebView webView) {
        this.mWebView = webView;
    }

    @Override // android.webkit.WebChromeClient
    public void onProgressChanged(WebView view, int newProgress) {
        this.mWebView.updateProgress(newProgress);
        super.onProgressChanged(view, newProgress);
    }
}
