package com.epam.itweek;

import android.test.InstrumentationTestRunner;

import com.epam.amtt.test.TestRunner;

public class Runner extends InstrumentationTestRunner {

    @Override
    public ClassLoader getLoader() {
        return TestRunner.class.getClassLoader();
    }

    @Override
    public void callApplicationOnCreate(android.app.Application app) {
        super.callApplicationOnCreate(app);
    }

}
