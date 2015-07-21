package com.example.ivan_bakach.testpermission;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

/**
 * Created by Ivan_Bakach on 29.06.2015.
 */
public class TestBroadcastReceiver extends BroadcastReceiver {

    public static final String PING_ANSWER = "PING_ANSWER";
    public static final String PING_REQUEST = "PING_REQUEST";
    public static final String CLOSE_TEST = "CLOSE_TEST";
    public static final String CATEGORY = "android.intent.category.DEFAULT";
    public static final String TAKE_SCREENSHOT = "TAKE_SCREENSHOT";
    public static final String PING_ANSWER_VALUE = "Success answer";
    public static final String TAKE_SCREEN_FAIL_KEY = "failScreen";
    public static final String ACTIVITY_CLASS_NAME_KEY = "activityClassName";
    public static final String PACKAGE_NAME_KEY = "packageName";
    public static final String TAKE_SCREEN_FAIL_VALUE = "Activity don't visible launch app and try again.";
    public static final String LIST_FRAGMENTS_KEY = "listFragments";
    public static final String TAKE_ONLY_INFO = "TAKE_ONLY_INFO";
    public static final String REQUEST_TAKE_ONLY_INFO = "REQUEST_TAKE_ONLY_INFO";
    private boolean closeUnitTest;
    private Activity mActivity;
    private static String sListFragments;
    public static String sCurrentArguments;

    TestBroadcastReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case PING_REQUEST:
                Intent in = new Intent();
                in.putExtra(PING_ANSWER, PING_ANSWER_VALUE);
                context.sendBroadcast(in);
                break;
            case CLOSE_TEST:
                LogManger.deleteFileIfExist(LogManger.sArgumentsFragments);
                LogManger.deleteFileIfExist(LogManger.sExceptionLog);
                LogManger.deleteFileIfExist(LogManger.sCommonLog);
                closeUnitTest = true;
                break;
            case TAKE_SCREENSHOT:
                if (mActivity != null) {
                    ScreenshotHelper.takeScreenshot(context, mActivity, sListFragments);
                    LogManger.writeArgumentsFromFragments(sCurrentArguments);
                } else {
                    Intent failIntent = new Intent();
                    failIntent.setAction(ScreenshotHelper.REQUEST_TAKE_SCREENSHOT_ACTION);
                    failIntent.putExtra(TAKE_SCREEN_FAIL_KEY, TAKE_SCREEN_FAIL_VALUE);
                    context.sendBroadcast(failIntent);
                }
                break;
            case TAKE_ONLY_INFO:
                if (mActivity != null) {
                    Intent intentTakeOnlyInfo = new Intent();
                    intentTakeOnlyInfo.setAction(REQUEST_TAKE_ONLY_INFO);
                    intentTakeOnlyInfo.putExtra(LIST_FRAGMENTS_KEY, sListFragments.substring(0, sListFragments.lastIndexOf("<br/>") != -1 ? sListFragments.lastIndexOf("<br/>") : 0));
                    intentTakeOnlyInfo.putExtra(ACTIVITY_CLASS_NAME_KEY, mActivity.getClass().getName());
                    intentTakeOnlyInfo.putExtra(PACKAGE_NAME_KEY, mActivity.getPackageName());
                    context.sendBroadcast(intentTakeOnlyInfo);
                    LogManger.writeArgumentsFromFragments(sCurrentArguments);
                } else {
                    Intent failIntent = new Intent();
                    failIntent.setAction(ScreenshotHelper.REQUEST_TAKE_SCREENSHOT_ACTION);
                    failIntent.putExtra(TAKE_SCREEN_FAIL_KEY, TAKE_SCREEN_FAIL_VALUE);
                    context.sendBroadcast(failIntent);
                }
                break;
        }
    }

    public void setActivity(final Activity activity) {
        mActivity = activity;
        sListFragments = "";
        sCurrentArguments = "";
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (activity != null && activity instanceof FragmentActivity && ((FragmentActivity) activity).getSupportFragmentManager().getFragments() != null) {
                    for (Fragment fragment : ((FragmentActivity) activity).getSupportFragmentManager().getFragments()) {
                        if (fragment.isVisible() && fragment.getUserVisibleHint() && fragment.getView() != null && fragment.getView().getParent() != null && !fragment.getView().getParent().isLayoutRequested()) {
                            Bundle bundleArguments = fragment.getArguments();
                            if (bundleArguments != null) {
                                sCurrentArguments = fragment.getClass().getSimpleName() + "\n" + LogManger.getArgumentsFromFragments(bundleArguments);
                            }
                            sListFragments += (fragment.getClass().getSimpleName() + "<br/>");
                        }
                    }
                }
            }
        }, 1000);
    }

    public boolean needCloseUnitTest() {
        return closeUnitTest;
    }

    public void setCloseUnitTest(boolean value) {
        closeUnitTest = value;
    }
}