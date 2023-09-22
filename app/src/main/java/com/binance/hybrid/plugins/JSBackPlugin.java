package com.binance.hybrid.plugins;

import com.bard.android.bridge.BaseJSPlugin;


/*
   JSBackPlugin 需要添加进
 */
public class JSBackPlugin  extends BaseJSPlugin  {
    @Override
    public void jsCallNative(String callbackId, String requestParams) {
        reportSuccess(callbackId);
//        reportFail(callbackId);
    }
}
