package com.example.ivan_bakach.testpermission;

import android.content.Context;
import android.content.IntentFilter;
import android.test.InstrumentationTestCase;

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
        LogManger.writeMultipleLogs();

        IntentFilter filterReceiver = new IntentFilter();
        filterReceiver.addCategory(TestBroadcastReceiver.CATEGORY);
        filterReceiver.addAction(TestBroadcastReceiver.PING_ANSWER);
        filterReceiver.addAction(TestBroadcastReceiver.CLOSE_TEST);
        context.registerReceiver(receiver, filterReceiver);
        receiver.setCloseUnitTest(false);

        while (!receiver.needCloseUnitTest()) {
            try {
                Thread.sleep(1000);
                if (receiver.needCloseUnitTest()) {
                    context.unregisterReceiver(receiver);
                    Assert.assertTrue(true);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
