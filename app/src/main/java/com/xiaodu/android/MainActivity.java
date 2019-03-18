package com.xiaodu.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.xiaodu.android.impl.Test1;
import com.xiaodu.android.interfaces.ITest1;
import com.xiaodu.android.interfaces.Test4;
import com.xiaodu.android.logaop.XDLog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        XDLog.enable();


        /**
         * 测试interfaces包下面
         */
        ITest1 tTest1 = new Test1();
        tTest1.test1();
        Test4 tTest4 = new Test4();
        tTest4.test4();



    }
}
