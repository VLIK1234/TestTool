package com.example.ivan_bakach.testappamtt;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 @author Ivan_Bakach
 @version on 29.06.2015
 */

public class TestBroadcastReceiver extends BroadcastReceiver {

    public static final String PING_ANSWER = "PING_ANSWER";
    private static final String PING_REQUEST = "PING_REQUEST";
    public static final String CLOSE_TEST = "CLOSE_TEST";
    public static final String TAKE_SCREENSHOT = "TAKE_SCREENSHOT";
    private static final String PING_ANSWER_VALUE = "Success answer";
    private static final String TAKE_SCREEN_FAIL_KEY = "failScreen";
    public static final String ACTIVITY_CLASS_NAME_KEY = "activityClassName";
    public static final String PACKAGE_NAME_KEY = "packageName";
    private static final String TAKE_SCREEN_FAIL_VALUE = "Activity don't visible launch app and try again.";
    public static final String LIST_FRAGMENTS_KEY = "listFragments";
    public static final String TAKE_ONLY_INFO = "TAKE_ONLY_INFO";
    private static final String REQUEST_TAKE_ONLY_INFO = "REQUEST_TAKE_ONLY_INFO";
    public static final String TAKE_LOGS = "TAKE_LOGS";
    public static final String TITLE_KEY = "title";
    private boolean mCloseUnitTest;
    public Activity mActivity;

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
                IOUtils.deleteFileIfExist(LogManger.sArgumentsFragments);
                IOUtils.deleteFileIfExist(LogManger.sExceptionLog);
                IOUtils.deleteFileIfExist(LogManger.sCommonLog);
                mCloseUnitTest = true;
                break;
            case TAKE_LOGS:
                LogManger.transferLogsToAmtt(context);
                break;
            case TAKE_SCREENSHOT:
                if (mActivity != null) {
                    ScreenshotHelper.takeScreenshot(context, mActivity, FragmentInfoHelper.getListFragments());
                    FragmentInfoHelper.writeArgumentsFromFragments(FragmentInfoHelper.sCurrentArguments);
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
                    intentTakeOnlyInfo.putExtra(LIST_FRAGMENTS_KEY,
                            FragmentInfoHelper.getListFragments().substring(0, FragmentInfoHelper.getListFragments().lastIndexOf(ScreenshotHelper.BR_TAG) != -1
                                    ? FragmentInfoHelper.getListFragments().lastIndexOf(ScreenshotHelper.BR_TAG) : 0));
                    intentTakeOnlyInfo.putExtra(TITLE_KEY, FragmentInfoHelper.getActivityTitle(mActivity));
                    intentTakeOnlyInfo.putExtra(ACTIVITY_CLASS_NAME_KEY, mActivity.getComponentName().getShortClassName());
                    intentTakeOnlyInfo.putExtra(PACKAGE_NAME_KEY, mActivity.getPackageName());
                    context.sendBroadcast(intentTakeOnlyInfo);
                    FragmentInfoHelper.writeArgumentsFromFragments(FragmentInfoHelper.sCurrentArguments);
                } else {
                    Intent failIntent = new Intent();
                    failIntent.setAction(ScreenshotHelper.REQUEST_TAKE_SCREENSHOT_ACTION);
                    failIntent.putExtra(TAKE_SCREEN_FAIL_KEY, TAKE_SCREEN_FAIL_VALUE);
                    context.sendBroadcast(failIntent);
                }
                break;
        }
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
        FragmentInfoHelper.initFragmentsInfo(activity);
    }

    public boolean needCloseUnitTest() {
        return mCloseUnitTest;
    }

    public void setCloseUnitTest(boolean value) {
        mCloseUnitTest = value;
    }
}