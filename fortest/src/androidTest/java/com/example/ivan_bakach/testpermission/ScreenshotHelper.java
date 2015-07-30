package com.example.ivan_bakach.testpermission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;

import java.io.ByteArrayOutputStream;

/**
 * Created by Ivan_Bakach on 14.07.2015.
 */
public class ScreenshotHelper {

    public static final String REQUEST_TAKE_SCREENSHOT_ACTION = "REQUEST_TAKE_SCREENSHOT";
    public static final int QUALITY_COMPRESS_SCREENSHOT = 90;
    public static final String BR_TAG = "<br/>";
    public static final String SCREEN_KEY = "screen";

    public static void takeScreenshot(Context context, Activity activity, String listFragments) {
// create bitmap screen capture
        Bitmap bitmap;
        View rootView = activity.getWindow().getDecorView().getRootView();
        rootView.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, QUALITY_COMPRESS_SCREENSHOT, stream);

        Intent intent = new Intent();
        intent.setAction(REQUEST_TAKE_SCREENSHOT_ACTION);
        byte[] bytes = stream.toByteArray();
        intent.putExtra(SCREEN_KEY, bytes);
        intent.putExtra(TestBroadcastReceiver.LIST_FRAGMENTS_KEY, listFragments.substring(0, listFragments.lastIndexOf(BR_TAG) != -1 ? listFragments.lastIndexOf(BR_TAG) : 0));
        intent.putExtra(TestBroadcastReceiver.TITLE_KEY, FragmentInfoHelper.getActivityTitle(activity));
        intent.putExtra(TestBroadcastReceiver.ACTIVITY_CLASS_NAME_KEY, activity.getClass().getName());
        intent.putExtra(TestBroadcastReceiver.PACKAGE_NAME_KEY, activity.getPackageName());
        context.sendBroadcast(intent);
    }
}
