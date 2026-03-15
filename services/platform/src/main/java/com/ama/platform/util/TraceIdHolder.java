package com.ama.platform.util;

import com.ama.platform.constants.TraceConstants;
import org.slf4j.MDC;

public final class TraceIdHolder {
    private TraceIdHolder() {
    }

    public static String getTraceId() {
        return MDC.get(TraceConstants.TRACE_ID);
    }
}
