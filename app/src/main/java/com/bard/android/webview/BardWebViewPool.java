package com.bard.android.webview;

import android.content.Context;
import android.content.MutableContextWrapper;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes27.dex */
public final class BardWebViewPool {
    private List<BardWebView> mAvailable;
    private Context mContext;
    private List<BardWebView> mInUse;
    private int mPoolSize;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes27.dex */
    public static class WebViewPoolHolder {
        private static final BardWebViewPool INSTANCE = new BardWebViewPool();

        private WebViewPoolHolder() {
        }
    }

    private BardWebViewPool() {
        this.mPoolSize = 0;
        this.mAvailable = Collections.synchronizedList(new ArrayList());
        this.mInUse = Collections.synchronizedList(new ArrayList());
    }

    public static BardWebViewPool getInstance() {
        return WebViewPoolHolder.INSTANCE;
    }

    public void initWebViewPool(Context context) {
        this.mContext = context;
        this.mPoolSize = 1;
    }

    public void filledPool() {
        if (this.mPoolSize == 0) {
            return;
        }
        for (int i = 0; i < this.mPoolSize && this.mAvailable.size() < this.mPoolSize; i++) {
            BardWebView webView = new BardWebView(new MutableContextWrapper(getContext()));
            this.mAvailable.add(webView);
        }
    }

    public BardWebView getWebView(boolean needVerity) {
        BardWebView webView;
        if (this.mAvailable.size() > 0) {
            webView = this.mAvailable.get(0);
            this.mAvailable.remove(0);
        } else {
            webView = new BardWebView(new MutableContextWrapper(getContext()));
        }
        webView.setNeedVerify(needVerity);
        this.mInUse.add(webView);
        return webView;
    }

    public BardWebView getWebView() {
        BardWebView webView;
        if (this.mAvailable.size() > 0) {
            webView = this.mAvailable.get(0);
            this.mAvailable.remove(0);
        } else {
            webView = new BardWebView(new MutableContextWrapper(getContext()));
        }
        this.mInUse.add(webView);
        return webView;
    }

    public void resetWebView(BardWebView webView) {
        ((MutableContextWrapper) webView.getContext()).setBaseContext(getContext());
        reset(webView); // 清除历史记录
        this.mInUse.remove(webView);
        if (this.mAvailable.size() < this.mPoolSize) {
            BardWebView newWebView = new BardWebView(getContext());
            this.mAvailable.add(newWebView); //
            return;
        }
        destroy(webView);
    }

    private void reset(BardWebView webView) {
        if (webView != null) {
            webView.stopLoading();
            webView.loadUrl("about:blank");
            webView.setNeedClearHistory(true);
            webView.defaultSettings();
        }
    }

    private void destroy(BardWebView webView) {
        if (webView != null) {
            try {
                ViewParent parent = webView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(webView);
                }
                webView.removeAllViews();
                webView.destroy();
            } catch (Exception e) {
            }
        }
    }

    public Context getContext() {
        return this.mContext;
    }
}
