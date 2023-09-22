package com.bard.android.bridge;

import android.content.Context;

import com.bard.android.webview.BardWebView;

/* loaded from: classes27.dex */
public abstract class BaseJSPlugin {
    private MagicalJourney mJSBridge;

    public abstract void jsCallNative(String str, String str2);

    public void setJSBridge(MagicalJourney jsBridge) {
        this.mJSBridge = jsBridge;
    }

    public MagicalJourney getJSBridge() {
        return this.mJSBridge;
    }

    public BardWebView getWebView() {
        MagicalJourney magicalJourney = this.mJSBridge;
        if (magicalJourney != null) {
            return magicalJourney.getWebView();
        }
        return null;
    }

    public Context getContext() {
        BardWebView webView = getWebView();
        if (webView != null) {
            return webView.getContext();
        }
        return null;
    }

    public void reportSuccess(String callbackId) {
        reportSuccess(callbackId, (String) null);
    }

    public void reportSuccess(String callbackId, String params) {
        MagicalJourney magicalJourney = this.mJSBridge;
        if (magicalJourney != null) {
            magicalJourney.callbackJS(callbackId, "success", params);
        }
    }

    public void reportSuccess(String callbackId, JSPluginResp resp) {
        MagicalJourney magicalJourney = this.mJSBridge;
        if (magicalJourney != null) {
            magicalJourney.callbackJS(callbackId, "success", resp.toJSon());
        }
    }

    public void reportFail(String callbackId) {
        reportFail(callbackId, (String) null);
    }

    public void reportFail(String callbackId, String params) {
        MagicalJourney magicalJourney = this.mJSBridge;
        if (magicalJourney != null) {
            magicalJourney.callbackJS(callbackId, "fail", params);
        }
    }

    public void reportFail(String callbackId, JSPluginResp resp) {
        MagicalJourney magicalJourney = this.mJSBridge;
        if (magicalJourney != null) {
            magicalJourney.callbackJS(callbackId, "fail", resp.toJSon());
        }
    }

    public void reportCancel(String callbackId) {
        reportCancel(callbackId, (String) null);
    }

    public void reportCancel(String callbackId, String params) {
        MagicalJourney magicalJourney = this.mJSBridge;
        if (magicalJourney != null) {
            magicalJourney.callbackJS(callbackId, "cancel", params);
        }
    }

    public void reportCancel(String callbackId, JSPluginResp resp) {
        MagicalJourney magicalJourney = this.mJSBridge;
        if (magicalJourney != null) {
            magicalJourney.callbackJS(callbackId, "cancel", resp.toJSon());
        }
    }
}
