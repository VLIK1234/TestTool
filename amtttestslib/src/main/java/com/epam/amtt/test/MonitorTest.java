package com.epam.amtt.test;

import android.app.Application;
import android.test.InstrumentationTestCase;

/**
 @author Ivan_Bakach
 @version on 29.06.2015
 */

public class MonitorTest extends InstrumentationTestCase{

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testMonitor() {
        Application application = (Application) getInstrumentation().getTargetContext().getApplicationContext();
        LogManager.writeMultipleLogs(application);
        InjectionHelper.initInjection(application);
    }
}
