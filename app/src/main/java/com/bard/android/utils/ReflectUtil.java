package com.bard.android.utils;

import java.lang.reflect.Method;

/* loaded from: classes27.dex */
public class ReflectUtil {
    public static Object invokeStaticMethod(String clzName, String methodName, Class<?>[] methodParamTypes, Object... methodParamValues) {
        Method method;
        try {
            Class clz = Class.forName(clzName);
            if (clz != null && (method = clz.getDeclaredMethod(methodName, methodParamTypes)) != null) {
                method.setAccessible(true);
                return method.invoke(null, methodParamValues);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
