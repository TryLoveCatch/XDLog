package com.xiaodu.android;

import android.util.Log;

import com.xiaodu.android.logaop.annotation.LogAop;


public class Test {
    @LogAop
    public void testA() {
        Log.e("Test", "testA");
    }

    public void testB() {
        Log.e("Test", "testB");
    }

    public void testC() {
        Log.e("Test", "testC");
    }
}
