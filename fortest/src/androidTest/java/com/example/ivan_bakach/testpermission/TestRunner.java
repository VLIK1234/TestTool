package com.example.ivan_bakach.testpermission;

import android.content.Intent;
import android.os.Bundle;
import android.test.InstrumentationTestRunner;
import android.test.InstrumentationTestSuite;
import android.util.Log;

import junit.framework.TestSuite;

/**
 * Created by Ivan_Bakach on 07.07.2015.
 */
public class TestRunner extends InstrumentationTestRunner {
    @Override
    public TestSuite getAllTests(){
        InstrumentationTestSuite suite = new InstrumentationTestSuite(this);
        suite.addTestSuite(MonitorTest.class);
        return suite;
    }

    @Override
    public ClassLoader getLoader() {
        return TestRunner.class.getClassLoader();
    }

    @Override
    public void finish(int resultCode, Bundle results) {
        super.finish(resultCode, results);
    }
}
