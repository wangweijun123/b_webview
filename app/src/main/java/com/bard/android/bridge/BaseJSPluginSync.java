package com.bard.android.bridge;

/* loaded from: classes27.dex */
public abstract class BaseJSPluginSync extends BaseJSPlugin {
    public abstract String jsCallNative(String str);

    @Override // com.bard.android.bridge.BaseJSPlugin
    public void jsCallNative(String callbackId, String requestParams) {
    }
}
