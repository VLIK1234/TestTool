package com.example.ivan_bakach.testpermission;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by Ivan_Bakach on 29.06.2015.
 */
public class TestBroadcastReceiver extends BroadcastReceiver {

    public static final String PING_ANSWER = "PING_ANSWER";
    public static final String PING_REQUEST = "PING_REQUEST";
    public static final String CLOSE_TEST = "CLOSE_TEST";
    public static final String CATEGORY = "android.intent.category.DEFAULT";
    public static final String TAKE_SCREENSHOT = "TAKE_SCREENSHOT";
    private boolean closeUnitTest;
    private Activity mActivity;

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case PING_REQUEST:
                Intent in = new Intent();
                in.putExtra(PING_ANSWER, "Success answer");
                context.sendBroadcast(in);
                break;
            case CLOSE_TEST:
                LogManger.deleteFileIfExist(LogManger.sExceptionLog);
                LogManger.deleteFileIfExist(LogManger.sCommonLog);
                closeUnitTest = true;
                break;
            case TAKE_SCREENSHOT:
                if (mActivity!=null) {
                    ScreenshotHelper.takeScreenshot(mActivity, context);
                }else{
                    Intent failIntent = new Intent();
                    failIntent.setAction("REQUEST_TAKE_SCREENSHOT");
                    failIntent.putExtra("failScreen", "Activity don't visible launch app and try again.");
                    context.sendBroadcast(failIntent);
                }
                break;
        }
    }

    public void setActivity(Activity activity){
        mActivity = activity;
    }

    public boolean needCloseUnitTest() {
        return closeUnitTest;
    }

    public void setCloseUnitTest(boolean value) {
        closeUnitTest = value;
    }
}