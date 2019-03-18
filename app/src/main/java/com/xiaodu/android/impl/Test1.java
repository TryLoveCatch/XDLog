package com.xiaodu.android.impl;

import android.util.Log;

import com.xiaodu.android.interfaces.ITest1;

public class Test1 implements ITest1 {
    @Override
    public void test1() {
        Log.d("Test1", "test1");
    }
}
