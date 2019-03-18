package com.xiaodu.android.impl;

import android.util.Log;

import com.xiaodu.android.interfaces.ITest1;
import com.xiaodu.android.interfaces.ITest3;

public class Test3 implements ITest3 {
    @Override
    public void test3() {
        Log.d("Test1", "test1");
    }
}
