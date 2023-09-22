package com.bard.android.rules;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.bard.android.bridge.MagicalJourney;
import com.bard.android.webview.BardWebView;

public final class BardAuthenticator {
    private MagicalJourney mBridge;
    private BardWebView mWebView;

    public BardAuthenticator(BardWebView webView) {
        this.mWebView = webView;
    }

    public void defaultJSSettings() {
        this.mWebView.getSettings().setJavaScriptEnabled(false);
        customUA(false);
    }

    private void customUA(boolean enableBNC) {
        String productNameInUA = null;
        productNameInUA = (productNameInUA == null || TextUtils.isEmpty(productNameInUA)) ? "BNC" : "BNC";
        String uaStr = this.mWebView.getSettings().getUserAgentString();
        int index = uaStr.indexOf(productNameInUA);
        if (index > 0) {
            this.mWebView.getSettings().setUserAgentString(uaStr.substring(0, index - 1));
        }
        if (enableBNC) {
            StringBuilder ua = new StringBuilder(this.mWebView.getSettings().getUserAgentString());
            ua.append(" " + productNameInUA + "/").append("1.0.0");
            try {
                PackageInfo info = this.mWebView.getContext().getPackageManager().getPackageInfo(this.mWebView.getContext().getPackageName(), 0);
                ua.append(" (Android").append(" ").append(info.versionName).append(")");
            } catch (PackageManager.NameNotFoundException e) {
                ua.append(" (Android UNKNOWN)");
            }
            this.mWebView.getSettings().setUserAgentString(ua.toString());
        }
    }

    public void setBridge(MagicalJourney bridge) {
        this.mBridge = bridge;
    }

    private void enableBridge(boolean enable) {
        if (!enable || this.mBridge== null) {
            this.mWebView.removeJavascriptInterface("BardMagicalJourney");
        } else {
            this.mWebView.addJavascriptInterface(mBridge, "BardMagicalJourney");
        }
    }

    public void verify(String url) {
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        customUA(true);
        enableBridge(true);
    }

    public void cleanHistory() {
    }
}
