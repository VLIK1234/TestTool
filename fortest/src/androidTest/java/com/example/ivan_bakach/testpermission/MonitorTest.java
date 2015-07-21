package com.example.ivan_bakach.testpermission;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.test.InstrumentationTestCase;
import android.util.Log;

import junit.framework.Assert;

/**
 * Created by Ivan_Bakach on 29.06.2015.
 */
public class MonitorTest extends InstrumentationTestCase implements Application.ActivityLifecycleCallbacks{

    public static final String EXCEPTION_ANSWER_ACTION = "EXCEPTION_ANSWER";
    public static final String EXCEPTION_ANSWER_KEY = "answer";
    public static final int RECEIVER_TIMEOUT = 1000;
    public Application mApplication;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    TestBroadcastReceiver receiver = new TestBroadcastReceiver();

    public void testMonitor() {
        final Context context = getInstrumentation().getTargetContext();
        LogManger.writeMultipleLogs();
        mApplication = (Application)getInstrumentation().getTargetContext().getApplicationContext();
        final Thread.UncaughtExceptionHandler exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                Log.e(thread.getName(), ex.toString());
                LogManger.writeArgumentsFromFragments(TestBroadcastReceiver.sCurrentArguments);
                Intent intent = new Intent();
                intent.setAction(EXCEPTION_ANSWER_ACTION);
                intent.putExtra(EXCEPTION_ANSWER_KEY, ex.getClass().getName());
                getInstrumentation().getContext().sendBroadcast(intent);
                exceptionHandler.uncaughtException(thread, ex);
            }
        });
        mApplication.registerActivityLifecycleCallbacks(this);
        IntentFilter filterReceiver = new IntentFilter();
        filterReceiver.addCategory(TestBroadcastReceiver.CATEGORY);
        filterReceiver.addAction(TestBroadcastReceiver.PING_ANSWER);
        filterReceiver.addAction(TestBroadcastReceiver.CLOSE_TEST);
        filterReceiver.addAction(TestBroadcastReceiver.TAKE_SCREENSHOT);
        filterReceiver.addAction(TestBroadcastReceiver.TAKE_ONLY_INFO);
        context.registerReceiver(receiver, filterReceiver);
        receiver.setCloseUnitTest(false);

        while (!receiver.needCloseUnitTest()) {
            try {
                Thread.sleep(RECEIVER_TIMEOUT);
                if (receiver.needCloseUnitTest()) {
                    context.unregisterReceiver(receiver);
                    mApplication.unregisterActivityLifecycleCallbacks(this);
                    Assert.assertTrue(true);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        receiver.setActivity(activity);
    }

    @Override
    public void onActivityPaused(Activity activity) {
        receiver.setActivity(null);
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
}
