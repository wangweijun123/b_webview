package com.bard.android.bridge;

import com.bard.android.webview.BardWebView;

/* loaded from: classes27.dex */
public interface IPluginDispatcher {
    boolean canProcess(BardWebView bardWebView);

    void dispatchJSRequestAsync(BardWebView bardWebView, String str, String str2, String str3);

    String dispatchJSRequestSync(BardWebView bardWebView, String str, String str2);
}
