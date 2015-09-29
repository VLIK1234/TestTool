package com.epam.amtt.test;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import junit.framework.Assert;

/**
 * @author IvanBakach
 * @version on 29.09.2015
 */
public class InjectionHelper implements Application.ActivityLifecycleCallbacks{
    private static final String EXCEPTION_ANSWER_ACTION = "EXCEPTION_ANSWER";
    private static final String EXCEPTION_ANSWER_KEY = "answer";
    private static final int RECEIVER_TIMEOUT = 1000;

    private final TestBroadcastReceiver receiver = new TestBroadcastReceiver();
    private static InjectionHelper sInjectionHelper = new InjectionHelper();
    private static Application sApplication;

    private InjectionHelper(){

    }

    public static InjectionHelper getInstance(Application application){
        setApplication(application);
        return sInjectionHelper;
    }

    public static void setApplication(Application application) {
        sApplication = application;
    }

    public void setUncaughtExceptionHandler(){
        final Thread.UncaughtExceptionHandler exceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable ex) {
                Log.e(thread.getName(), ex.toString());
                FragmentInfoHelper.writeArgumentsFromFragments(FragmentInfoHelper.sCurrentArguments);
                Intent intent = new Intent();
                intent.setAction(EXCEPTION_ANSWER_ACTION);
                intent.putExtra(EXCEPTION_ANSWER_KEY, ex.getClass().getName());
                sApplication.sendBroadcast(intent);
                exceptionHandler.uncaughtException(thread, ex);
                LogManager.closeLogsWriter();
            }
        });
    }

    public void registerActivityLifecycleCallbacks() {
        sApplication.registerActivityLifecycleCallbacks(this);
    }

    private void unregisterActivityLifecycleCallbacks() {
        sApplication.unregisterActivityLifecycleCallbacks(this);
    }

    public void setBroadcastReceiver() {
        IntentFilter filterReceiver = new IntentFilter();
        filterReceiver.addCategory(Intent.CATEGORY_DEFAULT);
        filterReceiver.addAction(TestBroadcastReceiver.PING_ANSWER);
        filterReceiver.addAction(TestBroadcastReceiver.CLOSE_TEST);
        filterReceiver.addAction(TestBroadcastReceiver.TAKE_SCREENSHOT);
        filterReceiver.addAction(TestBroadcastReceiver.TAKE_LOGS);
        filterReceiver.addAction(TestBroadcastReceiver.TAKE_ONLY_INFO);
        sApplication.registerReceiver(receiver, filterReceiver);
        receiver.setCloseUnitTest(false);

        while (!receiver.needCloseUnitTest()) {
            try {
                Thread.sleep(RECEIVER_TIMEOUT);
                if (receiver.needCloseUnitTest()) {
                    sApplication.unregisterReceiver(receiver);
                    unregisterActivityLifecycleCallbacks();
                    LogManager.closeLogsWriter();
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
