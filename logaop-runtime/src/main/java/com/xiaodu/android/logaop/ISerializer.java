package com.xiaodu.android.logaop;

public interface ISerializer {
    /**
     * 将对象序列化为String
     *
     * @param obj 对象
     * @return
     */
    String toString(Object obj);
}
