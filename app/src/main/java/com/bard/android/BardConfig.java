package com.bard.android;

import com.bard.android.bridge.BaseJSPlugin;

import java.util.HashMap;
import java.util.Map;

public class BardConfig {

    static final BardConfig instance = new BardConfig();

    // 需要往这个map添加plugin
    protected Map<String, BaseJSPlugin> mJSPluginMap = new HashMap<>();



    public static Map<String, BaseJSPlugin> getJSPluginMap() {
        return instance.mJSPluginMap;
    }


}
