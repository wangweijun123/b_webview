package com.bard.android.webview;

import static android.webkit.WebSettings.LOAD_DEFAULT;

import android.content.Context;
import android.content.MutableContextWrapper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.bard.android.bridge.MagicalJourney;
import com.bard.android.rules.BardAuthenticator;
import com.bard.android.utils.WebViewResourceHelper;

import java.util.Map;

/* loaded from: classes27.dex */
public class BardWebView extends WebView {
    private BardAuthenticator mAuthenticator;
    private boolean mEnableProgressBar;
    private boolean mNeedClearHistory;
    private ProgressBar mProgressBar;
    private boolean mTouchByUser;
    private boolean needVerify;

    public BardWebView(Context context) {
        super(context);
        this.mEnableProgressBar = true;
        this.needVerify = true;
        init(context);
    }

    public BardWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mEnableProgressBar = true;
        this.needVerify = true;
        init(context);
    }

    public BardWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mEnableProgressBar = true;
        this.needVerify = true;
        init(context);
    }

    public BardWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mEnableProgressBar = true;
        this.needVerify = true;
        init(context);
    }

    private void init(Context context) {
        WebViewResourceHelper.addChromeResourceIfNeeded(context);
        initProgressBar(context);
        WebView.setWebContentsDebuggingEnabled(false);
        this.mAuthenticator = new BardAuthenticator(this);
        defaultSettings();
    }

    protected void defaultNormalSettings() {
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        getSettings().setDisplayZoomControls(false);
        setLayerType(LAYER_TYPE_HARDWARE, null);
        defaultSecuritySettings();
    }

    private void defaultSecuritySettings() {
        this.needVerify = true;
        getSettings().setSavePassword(false);
        supportFileAccess(false);
    }

    public void supportFileAccess(boolean support) {
        getSettings().setAllowFileAccess(support);
        getSettings().setAllowFileAccessFromFileURLs(support);
        getSettings().setAllowUniversalAccessFromFileURLs(support);
    }

    public void supportViewPort(boolean support) {
        getSettings().setUseWideViewPort(support);
        getSettings().setLoadWithOverviewMode(support);
    }

    public void supportZoom(boolean support) {
        getSettings().setSupportZoom(support);
        getSettings().setBuiltInZoomControls(support);
    }

    protected void enableWebCache() {
        getSettings().setCacheMode(LOAD_DEFAULT);
//        getSettings().setAppCachePath(getContext().getCacheDir().getAbsolutePath() + "webcache/");
//        getSettings().setAppCacheEnabled(true);
        getSettings().setDomStorageEnabled(true);
    }

    public void setEnableProgressBar(boolean enableProgressBar) {
        this.mEnableProgressBar = enableProgressBar;
        ProgressBar progressBar = this.mProgressBar;
        if (progressBar != null) {
            if (enableProgressBar) {
                progressBar.setVisibility(VISIBLE);
            } else {
                progressBar.setVisibility(GONE);
            }
        }
    }

    public void defaultSettings() {
        defaultNormalSettings();
        this.mAuthenticator.defaultJSSettings();
        enableWebCache();
        supportViewPort(true);
        supportZoom(false);
        setWebViewClient(generateWebViewClient());
        setWebChromeClient(generateWebChromeClient());
        setEnableProgressBar(true);
    }

    protected void initProgressBar(Context context) {
        ProgressBar progressBar = new ProgressBar(context, null, 16842872);
        this.mProgressBar = progressBar;
        progressBar.setLayoutParams(new LayoutParams(-1, 4, 0, 0));
        addView(this.mProgressBar);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void updateProgress(int newProgress) {
        ProgressBar progressBar = this.mProgressBar;
        if (progressBar == null || !this.mEnableProgressBar || newProgress < 0) {
            return;
        }
        if (newProgress == 100) {
            progressBar.setVisibility(GONE);
            return;
        }
        if (progressBar.getVisibility() == GONE) {
            this.mProgressBar.setVisibility(VISIBLE);
        }
        this.mProgressBar.setProgress(newProgress);
    }

    protected BardWebViewClient generateWebViewClient() {
        return new BardWebViewClient(this);
    }

    protected BardWebChromeClient generateWebChromeClient() {
        return new BardWebChromeClient(this);
    }

    public void bindNewContext(Context context) {
        if (getContext() instanceof MutableContextWrapper) {
            ((MutableContextWrapper) getContext()).setBaseContext(context);
        }
    }

    public void setBridge(MagicalJourney bridge) {
        this.mAuthenticator.setBridge(bridge);
    }

    @Override // android.webkit.WebView
    public void loadUrl(String url) {
        this.mAuthenticator.verify(url);
        super.loadUrl(url);
        resetAllStateInternal(url);
    }

    @Override // android.webkit.WebView
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        this.mAuthenticator.verify(url);
        super.loadUrl(url, additionalHttpHeaders);
        resetAllStateInternal(url);
    }

    @Override // android.webkit.WebView
    public void postUrl(String url, byte[] postData) {
        this.mAuthenticator.verify(url);
        super.postUrl(url, postData);
        resetAllStateInternal(url);
    }

    @Override // android.webkit.WebView
    public void loadData(String data, String mimeType, String encoding) {
        this.mAuthenticator.verify(getUrl());
        super.loadData(data, mimeType, encoding);
        resetAllStateInternal(getUrl());
    }

    @Override // android.webkit.WebView
    public void loadDataWithBaseURL(String baseUrl, String data, String mimeType, String encoding, String historyUrl) {
        this.mAuthenticator.verify(getUrl());
        super.loadDataWithBaseURL(baseUrl, data, mimeType, encoding, historyUrl);
        resetAllStateInternal(getUrl());
    }

    @Override // android.webkit.WebView
    public void reload() {
        this.mAuthenticator.verify(getUrl());
        super.reload();
        resetAllStateInternal(getUrl());
    }

    @Override // android.webkit.WebView
    public boolean canGoBack() {
        if (copyBackForwardList().getCurrentIndex() == 1 && copyBackForwardList().getItemAtIndex(0).getUrl().equals("about:blank")) {
            return false;
        }
        return super.canGoBack();
    }

    @Override // android.webkit.WebView
    public void goBack() {
        int backIndex = copyBackForwardList().getCurrentIndex() - 1;
        String backUrl = copyBackForwardList().getItemAtIndex(backIndex).getUrl();
        this.mAuthenticator.verify(backUrl);
        super.goBack();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.webkit.WebView, android.view.View
    public void onScrollChanged(int l2, int t, int oldl, int oldt) {
        if (this.mEnableProgressBar) {
            LayoutParams lp = (LayoutParams) this.mProgressBar.getLayoutParams();
            lp.x = l2;
            lp.y = t;
            this.mProgressBar.setLayoutParams(lp);
        }
        super.onScrollChanged(l2, t, oldl, oldt);
    }

    @Override // android.webkit.WebView, android.view.View
    public void setOverScrollMode(int mode) {
        try {
            super.setOverScrollMode(mode);
        } catch (Throwable e) {
            String trace = Log.getStackTraceString(e);
            if (trace.contains("android.content.pm.PackageManager$NameNotFoundException") || trace.contains("java.lang.RuntimeException: Cannot load WebView") || trace.contains("android.webkit.WebViewFactory$MissingWebViewPackageException: Failed to load WebView provider")) {
                e.printStackTrace();
                return;
            }
            throw e;
        }
    }

    @Override // android.view.View
    public ActionMode startActionMode(ActionMode.Callback callback) {
        try {
            return super.startActionMode(callback);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override // android.view.View
    public ActionMode startActionMode(ActionMode.Callback callback, int type) {
        try {
            return super.startActionMode(callback, type);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isTouchByUser() {
        return this.mTouchByUser;
    }

    public void setNeedClearHistory(boolean needClearHistory) {
        this.mNeedClearHistory = needClearHistory;
    }

    public boolean isNeedClearHistory() {
        return this.mNeedClearHistory;
    }

    @Override // android.webkit.WebView
    public void clearHistory() {
        super.clearHistory();
        this.mAuthenticator.cleanHistory();
    }

    private void resetAllStateInternal(String url) {
        if (!TextUtils.isEmpty(url) && url.startsWith("javascript:")) {
            return;
        }
        resetAllState();
    }

    protected void resetAllState() {
        this.mTouchByUser = false;
    }

    @Override // android.webkit.WebView, android.view.View
    public boolean onTouchEvent(MotionEvent event2) {
        switch (event2.getAction()) {
            case 0:
                this.mTouchByUser = true;
                break;
        }
        return super.onTouchEvent(event2);
    }

    public void setNeedVerify(boolean needVerify) {
        this.needVerify = needVerify;
    }

    public boolean isNeedVerify() {
        return this.needVerify;
    }
}
