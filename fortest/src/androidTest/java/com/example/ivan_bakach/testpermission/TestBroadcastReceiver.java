package com.example.ivan_bakach.testpermission;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Ivan_Bakach on 29.06.2015.
 */
public class TestBroadcastReceiver extends BroadcastReceiver {

    public static final String PING_ANSWER = "PING_ANSWER";
    public static final String PING_REQUEST = "PING_REQUEST";
    public static final String CLOSE_TEST = "CLOSE_TEST";
    public static final String TAKE_SCREENSHOT = "TAKE_SCREENSHOT";
    public static final String PING_ANSWER_VALUE = "Success answer";
    public static final String TAKE_SCREEN_FAIL_KEY = "failScreen";
    public static final String ACTIVITY_CLASS_NAME_KEY = "activityClassName";
    public static final String PACKAGE_NAME_KEY = "packageName";
    public static final String TAKE_SCREEN_FAIL_VALUE = "Activity don't visible launch app and try again.";
    public static final String LIST_FRAGMENTS_KEY = "listFragments";
    public static final String TAKE_ONLY_INFO = "TAKE_ONLY_INFO";
    public static final String REQUEST_TAKE_ONLY_INFO = "REQUEST_TAKE_ONLY_INFO";
    public static final String TAKE_LOGS = "TAKE_LOGS";
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
                    intentTakeOnlyInfo.putExtra(ACTIVITY_CLASS_NAME_KEY, mActivity.getClass().getName());
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