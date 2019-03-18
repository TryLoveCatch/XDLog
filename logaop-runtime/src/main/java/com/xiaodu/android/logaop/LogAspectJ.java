package com.xiaodu.android.logaop;

import android.os.Build;
import android.os.Looper;
import android.os.Trace;
import android.support.annotation.NonNull;

import com.xiaodu.android.logaop.annotation.LogAop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.concurrent.TimeUnit;

@Aspect
public class LogAspectJ {
    @Pointcut("within(@com.xiaodu.android.logaop.annotation.LogAop *)")
    public void withinAnnotatedClass() {
    }

    @Pointcut("execution(!synthetic * *(..)) && withinAnnotatedClass()")
    public void methodInsideAnnotatedType() {
    }

    @Pointcut("execution(!synthetic *.new(..)) && withinAnnotatedClass()")
    public void constructorInsideAnnotatedType() {
    }

    @Pointcut("execution(@com.xiaodu.android.logaop.annotation.LogAop * *(..)) || methodInsideAnnotatedType()")
    public void method() {
    } //方法切入点

    @Pointcut("execution(@com.xiaodu.android.logaop.annotation.LogAop *.new(..)) || constructorInsideAnnotatedType()")
    public void constructor() {
    } //构造器切入点


    /**
     *
     * 1、不传参数LogAop，可以对注解的类下的所有方法都起作用
     * 2、传参数只能对方法和构造函数起作用
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */

//    @Around("(method() || constructor()) && @annotation(log)")// 主要这里是参数，不是参数的类型，我就是卡在这里，卡了好久！！！！
//    @Around(value = "(method() || constructor()) && @annotation(log)", argNames = "joinPoint, log")
    @Around("method() || constructor()")
    public Object logAndExecute(ProceedingJoinPoint joinPoint/*, LogAop log*/) throws Throwable {
        enterMethod(joinPoint, null);

        long startNanos = System.nanoTime();
        Object result = joinPoint.proceed();
        long stopNanos = System.nanoTime();
        long lengthMillis = TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos);

        exitMethod(joinPoint, null, result, lengthMillis);

        return result;
    }


    /**
     * interfaces包下面所有类接口和子类的所有方法
     */
//    @Pointcut("execution(* com.xiaodu.android.interfaces..*(..))")
//    public void test() {
//    } //构造器切入点
//
//    @Before("test()")
//    public void logAndExecute(JoinPoint joinPoint) throws Throwable {
//        long startNanos = System.nanoTime();
//        long stopNanos = System.nanoTime();
//        long lengthMillis = TimeUnit.NANOSECONDS.toMillis(stopNanos - startNanos);
//
//        exitMethod(joinPoint, null, null, lengthMillis);
//    }


    /**
     * 将所有的log打印都替换了
     */
//    @Pointcut("call(static * android.util.Log.*(String,String))")
//    public void log() {}
//
//    @Around("log()")
//    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
//        String methodName = joinPoint.getSignature().getName();
//        String tag = (String) joinPoint.getArgs()[0];
//        String msg = (String) joinPoint.getArgs()[1];
//        Log.e("22222", tag + " ---> " + msg);
//        return 0;
//    }








    /**
     * 方法执行前切入
     *
     * @param joinPoint
     */
    private void enterMethod(JoinPoint joinPoint, LogAop logAop) {
//        if (!XDLog.isDebug()) return;

        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();

        Class<?> cls = codeSignature.getDeclaringType(); //方法所在类
        String methodName = codeSignature.getName();    //方法名
        String[] parameterNames = codeSignature.getParameterNames(); //方法参数名集合
        Object[] parameterValues = joinPoint.getArgs(); //方法参数集合

        //记录并打印方法的信息
        StringBuilder builder = getMethodLogInfo(methodName, parameterNames, parameterValues);

        XDLog.log(logAop == null ? 0 : logAop.priority(), asTag(cls), builder.toString());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            final String section = builder.toString().substring(2);
            Trace.beginSection(section);
        }
    }

    /**
     * 获取方法的日志信息
     *
     * @param methodName      方法名
     * @param parameterNames  方法参数名集合
     * @param parameterValues 方法参数值集合
     * @return
     */
    @NonNull
    private StringBuilder getMethodLogInfo(String methodName, String[] parameterNames, Object[] parameterValues) {
        StringBuilder builder = new StringBuilder("\u21E2 ");
        builder.append(methodName).append('(');
        for (int i = 0; i < parameterValues.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(parameterNames[i]).append('=');
            builder.append(Strings.toString(parameterValues[i]));
        }
        builder.append(')');

        if (Looper.myLooper() != Looper.getMainLooper()) {
            builder.append(" [Thread:\"").append(Thread.currentThread().getName()).append("\"]");
        }
        return builder;
    }


    /**
     * 方法执行完毕，切出
     *
     * @param joinPoint
     * @param result       方法执行后的结果
     * @param lengthMillis 执行方法所需要的时间
     */
    private void exitMethod(JoinPoint joinPoint, LogAop logAop, Object result, long lengthMillis) {
//        if (!XMark.isDebug()) return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            Trace.endSection();
        }

        Signature signature = joinPoint.getSignature();

        Class<?> cls = signature.getDeclaringType();
        String methodName = signature.getName();

        boolean hasReturnType = isHasReturnType(signature);

        StringBuilder builder = new StringBuilder("\u21E0 ")
                .append(methodName)
                .append(" [")
                .append(lengthMillis)
                .append("ms]");

        if (hasReturnType) {
            builder.append(" = ");
            builder.append(Strings.toString(result));
        }

        XDLog.log(logAop == null ? 0 : logAop.priority(), asTag(cls), builder.toString());
    }

    /**
     * 方法是否有返回值
     *
     * @param signature
     * @return
     */
    private boolean isHasReturnType(Signature signature) {
        return signature instanceof MethodSignature
                && ((MethodSignature) signature).getReturnType() != void.class;
    }


    private static String asTag(Class<?> cls) {
//        if (cls.isAnonymousClass()) {
//            return asTag(cls.getEnclosingClass());
//        }
//        return cls.getSimpleName();
        return "1111";
    }
}
