package com.xiaodu.android.logaop;

public interface ILogger {

    /**
     * 打印信息
     * @param priority 优先级
     * @param tag 标签
     * @param msg 信息
     */
    void log(int priority, String tag, String msg);

}
