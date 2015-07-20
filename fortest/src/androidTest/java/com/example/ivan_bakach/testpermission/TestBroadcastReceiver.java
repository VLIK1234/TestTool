package com.example.ivan_bakach.testpermission;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

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
    public static final String PING_ANSWER_VALUE = "Success answer";
    public static final String TAKE_SCREEN_FAIL_KEY = "failScreen";
    public static final String TAKE_SCREEN_FAIL_VALUE = "Activity don't visible launch app and try again.";
    private boolean closeUnitTest;
    private Activity mActivity;
    private String listFragments;

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case PING_REQUEST:
                Intent in = new Intent();
                in.putExtra(PING_ANSWER, PING_ANSWER_VALUE);
                context.sendBroadcast(in);
                break;
            case CLOSE_TEST:
                LogManger.deleteFileIfExist(LogManger.sExceptionLog);
                LogManger.deleteFileIfExist(LogManger.sCommonLog);
                closeUnitTest = true;
                break;
            case TAKE_SCREENSHOT:
                if (mActivity!=null) {
                    ScreenshotHelper.takeScreenshot(context, mActivity, listFragments);
                }else{
                    Intent failIntent = new Intent();
                    failIntent.setAction(ScreenshotHelper.REQUEST_TAKE_SCREENSHOT_ACTION);
                    failIntent.putExtra(TAKE_SCREEN_FAIL_KEY, TAKE_SCREEN_FAIL_VALUE);
                    context.sendBroadcast(failIntent);
                }
                break;
        }
    }

    public Runnable setActivity(Activity activity){
        mActivity = activity;
        listFragments = "";
        Log.d("TAG2", (activity!=null) +" activity!=null "+ (activity instanceof FragmentActivity) +" instanceof "+ (((FragmentActivity) activity).getSupportFragmentManager().getFragments()!=null)
                +" fragments!=null ");
        if (activity!=null && activity instanceof FragmentActivity&&((FragmentActivity) activity).getSupportFragmentManager().getFragments()!=null) {
            for(Fragment fragment : ((FragmentActivity) activity).getSupportFragmentManager().getFragments()) {
                Log.d("TAG2", fragment.isAdded() +" isAdded "+ !fragment.isHidden() +" isHidden "+ (fragment.getView() != null)
                        +" view!=null "+ (fragment.getView().getWindowToken() != null) +" WindowToken!=null "+ (fragment.getView().getVisibility() == View.VISIBLE)+" visibility==VISIBLE ");
                if (fragment.isAdded() && !fragment.isHidden() && fragment.getView() != null
                        && fragment.getView().getVisibility() == View.VISIBLE) {
                    listFragments += (fragment.getClass().getSimpleName()+"<br/>");
                }
            }
        }
    }

    public boolean needCloseUnitTest() {
        return closeUnitTest;
    }

    public void setCloseUnitTest(boolean value) {
        closeUnitTest = value;
    }
}