package com.xiaodu.android.impl;

import android.util.Log;

import com.xiaodu.android.interfaces.ITest1;
import com.xiaodu.android.interfaces.ITest2;

public class Test2 implements ITest2 {
    @Override
    public void test2() {
        Log.d("Test1", "test1");
    }
}
