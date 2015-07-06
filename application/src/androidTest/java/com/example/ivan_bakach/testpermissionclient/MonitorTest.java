package com.example.ivan_bakach.testpermissionclient;

import android.content.Context;
import android.content.IntentFilter;
import android.test.InstrumentationTestCase;
import android.util.Log;

import junit.framework.Assert;

/**
 * Created by Ivan_Bakach on 29.06.2015.
 */
public class MonitorTest extends InstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    TestBroadcastReceiver receiver = new TestBroadcastReceiver();

    public void testMonitor() {
        final Context context = getInstrumentation().getTargetContext();
        TestBroadcastReceiver.writeMultipleLogs(context);

        IntentFilter filterReceiver = new IntentFilter();
        filterReceiver.addCategory(TestBroadcastReceiver.CATEGORY);
        filterReceiver.addAction(TestBroadcastReceiver.PING_ANSWER);
        filterReceiver.addAction(TestBroadcastReceiver.CLOSE_TEST);
        context.registerReceiver(receiver, filterReceiver);
        receiver.setCloseUnitTest(false);

        while (!receiver.getCloseUnitTest()) {
            if (receiver.getCloseUnitTest()) {
                context.unregisterReceiver(receiver);
                Assert.assertTrue(true);
            }
        }
    }

}
