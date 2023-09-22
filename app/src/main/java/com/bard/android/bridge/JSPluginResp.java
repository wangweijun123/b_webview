package com.bard.android.bridge;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/* loaded from: classes27.dex */
public class JSPluginResp<T> {
    private static Gson sGson = new GsonBuilder().disableHtmlEscaping().create();
    @SerializedName("data")
    @Expose
    public T data;
    @SerializedName("errMsg")
    @Expose
    public String errMsg;

    public JSPluginResp(String errMsg, T data) {
        this.errMsg = errMsg;
        this.data = data;
    }

    public static JSPluginResp success() {
        return new JSPluginResp("ok", null);
    }

    public static <T> JSPluginResp success(T data) {
        return new JSPluginResp("ok", data);
    }

    public static JSPluginResp error(String errMsg) {
        return new JSPluginResp(errMsg, null);
    }

    public static <T> JSPluginResp error(String errMsg, T data) {
        return new JSPluginResp(errMsg, data);
    }

    public static <T> JSPluginResp cancel(T data) {
        return new JSPluginResp("cancel", data);
    }

    public String toJSon() {
        return sGson.toJson(this);
    }
}
