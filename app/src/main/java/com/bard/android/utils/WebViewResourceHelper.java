package com.bard.android.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Build;
import android.text.TextUtils;

import java.lang.reflect.Method;

/* loaded from: classes27.dex */
public class WebViewResourceHelper {
    private static boolean sInitialed = false;

    public static boolean addChromeResourceIfNeeded(Context context) {
        boolean z = true;
        if (sInitialed) {
            return true;
        }
        String resourceDir = getWebViewResourceDir(context);
        if (TextUtils.isEmpty(resourceDir)) {
            return false;
        }
        try {
            Method m = getAddAssetPathMethod();
            if (m != null) {
                int ret = ((Integer) m.invoke(context.getAssets(), resourceDir)).intValue();
                if (ret <= 0) {
                    z = false;
                }
                sInitialed = z;
                return z;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Method getAddAssetPathMethod() {
        Method m = null;
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                m = AssetManager.class.getDeclaredMethod("addAssetPathAsSharedLibrary", String.class);
                m.setAccessible(true);
                return m;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return m;
            }
        }
        try {
            m = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
            m.setAccessible(true);
            return m;
        } catch (NoSuchMethodException e2) {
            e2.printStackTrace();
            return m;
        }
    }

    private static String getWebViewResourceDir(Context context) {
        String pkgName = getWebViewPackageName();
        if (TextUtils.isEmpty(pkgName)) {
            return null;
        }
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(getWebViewPackageName(), 1024);
            return pi.applicationInfo.sourceDir;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    private static String getWebViewPackageName() {
        int sdk2 = Build.VERSION.SDK_INT;
        switch (sdk2) {
            case 21:
            case 22:
                return getWebViewPackageName4Lollipop();
            case 23:
                return getWebViewPackageName4M();
            case 24:
                return getWebViewPackageName4N();
            default:
                return getWebViewPackageName4More();
        }
    }

    private static String getWebViewPackageName4Lollipop() {
        try {
            return (String) ReflectUtil.invokeStaticMethod("android.webkit.WebViewFactory", "getWebViewPackageName", null, new Object[0]);
        } catch (Throwable e) {
            e.printStackTrace();
            return "com.google.android.webview";
        }
    }

    private static String getWebViewPackageName4M() {
        return getWebViewPackageName4Lollipop();
    }

    private static String getWebViewPackageName4N() {
        try {
            Context c = (Context) ReflectUtil.invokeStaticMethod("android.webkit.WebViewFactory", "getWebViewContextAndSetProvider", null, new Object[0]);
            return c.getApplicationInfo().packageName;
        } catch (Throwable e) {
            e.printStackTrace();
            return "com.google.android.webview";
        }
    }

    private static String getWebViewPackageName4More() {
        return getWebViewPackageName4N();
    }
}
