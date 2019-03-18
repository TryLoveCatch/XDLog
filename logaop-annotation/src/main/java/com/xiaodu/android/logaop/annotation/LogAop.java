package com.xiaodu.android.logaop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Inherited
public @interface LogAop {
    /**
     * 日志的优先级(默认是0)
     */
    int priority() default 0;
}
