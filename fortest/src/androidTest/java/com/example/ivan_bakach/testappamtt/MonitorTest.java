package com.example.ivan_bakach.testappamtt;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.test.InstrumentationTestCase;
import android.util.Log;

import junit.framework.Assert;

/**
 @author Ivan_Bakach
 @version on 29.06.2015
 */

public class MonitorTest extends InstrumentationTestCase implements Application.ActivityLifecycleCallbacks{

    private static final String EXCEPTION_ANSWER_ACTION = "EXCEPTION_ANSWER";
    private static final String EXCEPTION_ANSWER_KEY = "answer";
    private static final int RECEIVER_TIMEOUT = 1000;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    private final TestBroadcastReceiver receiver = new TestBroadcastReceiver();

    public void testMonitor() {
        final Context context = getInstrumentation().getTargetContext();
        LogManger.writeMultipleLogs(getInstrumentation().getTargetContext());
        LogManger.transferLogsToAmtt(context);
        Application application = (Application) getInstrumentation().getTargetContext().getApplicationContext();
        final Thread.UncaughtExceptionHandler exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                Log.e(thread.getName(), ex.toString());
                FragmentInfoHelper.writeArgumentsFromFragments(FragmentInfoHelper.sCurrentArguments);
                Intent intent = new Intent();
                intent.setAction(EXCEPTION_ANSWER_ACTION);
                intent.putExtra(EXCEPTION_ANSWER_KEY, ex.getClass().getName());
                getInstrumentation().getContext().sendBroadcast(intent);
                exceptionHandler.uncaughtException(thread, ex);
                LogManger.closeLogsWriter();
            }
        });
        application.registerActivityLifecycleCallbacks(this);
        IntentFilter filterReceiver = new IntentFilter();
        filterReceiver.addCategory(Intent.CATEGORY_DEFAULT);
        filterReceiver.addAction(TestBroadcastReceiver.PING_ANSWER);
        filterReceiver.addAction(TestBroadcastReceiver.CLOSE_TEST);
        filterReceiver.addAction(TestBroadcastReceiver.TAKE_SCREENSHOT);
        filterReceiver.addAction(TestBroadcastReceiver.TAKE_LOGS);
        filterReceiver.addAction(TestBroadcastReceiver.TAKE_ONLY_INFO);
        context.registerReceiver(receiver, filterReceiver);
        receiver.setCloseUnitTest(false);

        while (!receiver.needCloseUnitTest()) {
            try {
                Thread.sleep(RECEIVER_TIMEOUT);
                if (receiver.needCloseUnitTest()) {
                    context.unregisterReceiver(receiver);
                    application.unregisterActivityLifecycleCallbacks(this);
                    LogManger.closeLogsWriter();
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
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (activity!=null&&receiver.mActivity!=null&&activity.getLocalClassName().equals(receiver.mActivity.getLocalClassName())) {
            receiver.setActivity(null);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
}
