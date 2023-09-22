package com.bard.android.bridge;

/* loaded from: classes27.dex */
public class JSEvent {
    private String mEventType;
    private String mParams;

    public JSEvent(String eventType, String params) {
        this.mEventType = eventType;
        this.mParams = params;
    }

    public String getEventType() {
        return this.mEventType;
    }

    public void setEventType(String eventType) {
        this.mEventType = eventType;
    }

    public String getParams() {
        return this.mParams;
    }

    public void setParams(String params) {
        this.mParams = params;
    }
}
