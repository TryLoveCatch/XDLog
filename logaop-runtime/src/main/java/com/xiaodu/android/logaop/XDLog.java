package com.xiaodu.android.logaop;

public class XDLog {
    /**
     * 默认的日志记录为Logcat
     */
    private static ILogger sLogger = new LogcatLogger();

    private static ISerializer sSerializer = null;

    private static boolean sIsEnable = false;

    public static void setLogger(ILogger pLogger) {
        sLogger = pLogger;
    }

    public static void setSerializer(ISerializer pSerializer) {
        sSerializer = pSerializer;
    }

    /**
     * 如果需要这个功能，需要在合适的地方调用这个方法
     */
    public static void enable() {
        sIsEnable = true;
    }

    public static void log(int priority, String tag, String msg) {
        if (!sIsEnable) {
            return;
        }
        sLogger.log(priority, tag, msg);
    }

    public static String toString(Object obj) {
        if (!sIsEnable || sSerializer == null) {
            return null;
        }

        return sSerializer.toString(obj);
    }
}
