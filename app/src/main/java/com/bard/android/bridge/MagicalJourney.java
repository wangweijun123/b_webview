package com.bard.android.bridge;

import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;

import com.bard.android.BardConfig;
import com.bard.android.webview.BardWebView;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/* loaded from: classes27.dex */
public final class MagicalJourney {
    private LinkedList<JSEvent> mEventCacheList;
    private IPluginDispatcher mInjectPerfDispatcher;
    private Map<String, BaseJSPlugin> mJSPluginMap = BardConfig.getJSPluginMap();
    private boolean mJsReady;
    private IPluginDispatcher mPluginDispatcher;
    private BardWebView mWebView;

    public MagicalJourney(BardWebView webView) {
        this.mWebView = webView;
    }

    public void setPluginDispatcher(IPluginDispatcher pluginDispatcher) {
        this.mPluginDispatcher = pluginDispatcher;
    }

    public void setInjectPerfDispatcher(IPluginDispatcher perfDispatcher) {
        this.mInjectPerfDispatcher = perfDispatcher;
    }

    public BardWebView getWebView() {
        return this.mWebView;
    }

    public BaseJSPlugin getJSPlugin(String jsFunction) {
        Map<String, BaseJSPlugin> map = this.mJSPluginMap;
        if (map == null) {
            return null;
        }
        return map.get(jsFunction);
    }

    @JavascriptInterface
    public void messageSend(String functionName, String callbackId, String params) {
        dispatchJSRequest(functionName, callbackId, params);
    }

    @JavascriptInterface
    public String messageSendSync(String functionName, String params) {
        try {
            IPluginDispatcher iPluginDispatcher = this.mPluginDispatcher;
            if (iPluginDispatcher != null && iPluginDispatcher.canProcess(this.mWebView)) {
                return this.mPluginDispatcher.dispatchJSRequestSync(this.mWebView, functionName, params);
            }
            return dispatchJSRequest(functionName, params);
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    @JavascriptInterface
    public void bridgeReady() {
        this.mJsReady = true;
        LinkedList<JSEvent> linkedList = this.mEventCacheList;
        if (linkedList == null || linkedList.size() == 0) {
            return;
        }
        Iterator<JSEvent> it = this.mEventCacheList.iterator();
        while (it.hasNext()) {
            JSEvent jsEvent = it.next();
            callEventJs(jsEvent);
        }
        this.mEventCacheList.clear();
    }

    @JavascriptInterface
    public void jsError(String callbackId) {
    }

    @JavascriptInterface
    public void noticeContentLoadedEnd(String message) {
        Log.d("MagicalJourney", "Monitor end = " + message);
        IPluginDispatcher iPluginDispatcher = this.mInjectPerfDispatcher;
        if (iPluginDispatcher != null) {
            iPluginDispatcher.dispatchJSRequestSync(this.mWebView, "", message);
        }
    }

    private void dispatchJSRequest(final String functionName, final String callbackId, final String params) {
        BardWebView bardWebView = this.mWebView;
        if (bardWebView == null) {
            return;
        }
        bardWebView.post(new Runnable() { // from class: com.bard.android.bridge.MagicalJourney.1
            @Override // java.lang.Runnable
            public void run() {
                if (MagicalJourney.this.mPluginDispatcher == null || !MagicalJourney.this.mPluginDispatcher.canProcess(MagicalJourney.this.mWebView)) {
                    BaseJSPlugin jsPlugin = (BaseJSPlugin) MagicalJourney.this.mJSPluginMap.get(functionName);
                    try {
                        if (jsPlugin == null) {
                            MagicalJourney.this.callbackJS(callbackId, "fail", null);
                        } else {
                            jsPlugin.setJSBridge(MagicalJourney.this);
                            jsPlugin.jsCallNative(callbackId, params);
                        }
                        return;
                    } catch (Exception e) {
                        MagicalJourney.this.callbackJS(callbackId, "fail", null);
                        return;
                    }
                }
                MagicalJourney.this.mPluginDispatcher.dispatchJSRequestAsync(MagicalJourney.this.mWebView, functionName, callbackId, params);
            }
        });
    }

    private String dispatchJSRequest(String functionName, String params) {
        BaseJSPlugin jsPlugin = this.mJSPluginMap.get(functionName);
        if (jsPlugin != null && (jsPlugin instanceof BaseJSPluginSync)) {
            BaseJSPluginSync jsPluginSync = (BaseJSPluginSync) jsPlugin;
            jsPluginSync.setJSBridge(this);
            return jsPluginSync.jsCallNative(params);
        }
        return null;
    }

    public void callbackJS(String callbackId, String callbackFunctionName, String params) {
        if (this.mWebView == null) {
            return;
        }
        final StringBuilder jsSb = new StringBuilder();
        jsSb.append("BardApp.callbackFromNative('");
        jsSb.append(callbackId);
        jsSb.append("','");
        jsSb.append(callbackFunctionName);
        jsSb.append(TextUtils.isEmpty(params) ? "')" : "','" + params + "')");
        this.mWebView.post(new Runnable() { // from class: com.bard.android.bridge.MagicalJourney.2
            @Override // java.lang.Runnable
            public void run() {
                MagicalJourney.this.mWebView.evaluateJavascript(jsSb.toString(), new ValueCallback<String>() { // from class: com.bard.android.bridge.MagicalJourney.2.1
                    @Override // android.webkit.ValueCallback
                    public void onReceiveValue(String s) {
                    }
                });
            }
        });
    }

    public void callEventJs(JSEvent jsEvent) {
        if (this.mJsReady) {
            if (this.mWebView == null) {
                return;
            }
            final StringBuilder jsSb = new StringBuilder();
            jsSb.append("BardApp.eventFromNative('");
            jsSb.append(jsEvent.getEventType());
            jsSb.append(TextUtils.isEmpty(jsEvent.getParams()) ? "')" : "','" + jsEvent.getParams() + "')");
            this.mWebView.post(new Runnable() { // from class: com.bard.android.bridge.MagicalJourney.3
                @Override // java.lang.Runnable
                public void run() {
                    MagicalJourney.this.mWebView.evaluateJavascript(jsSb.toString(), new ValueCallback<String>() { // from class: com.bard.android.bridge.MagicalJourney.3.1
                        @Override // android.webkit.ValueCallback
                        public void onReceiveValue(String s) {
                        }
                    });
                }
            });
            return;
        }
        if (this.mEventCacheList == null) {
            this.mEventCacheList = new LinkedList<>();
        }
        this.mEventCacheList.add(jsEvent);
    }
}
