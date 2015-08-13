package com.epam.amtt.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 @author Ivan_Bakach
 @version on 14.07.2015
 */

public class ScreenshotHelper {

    private static final String TAG = ScreenshotHelper.class.getSimpleName();
    public static final String REQUEST_TAKE_SCREENSHOT_ACTION = "REQUEST_TAKE_SCREENSHOT";
    private static final int QUALITY_COMPRESS_SCREENSHOT = 90;
    public static final String BR_TAG = "<br/>";
    private static final String SCREEN_KEY = "screen";

    public static void takeScreenshot(Context context, Activity activity, String listFragments) {
// create bitmap screen capture
        Bitmap bitmap;
        View rootView = activity.getWindow().getDecorView().getRootView();
        rootView.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, QUALITY_COMPRESS_SCREENSHOT, stream);
            Intent intent = new Intent();
            intent.setAction(REQUEST_TAKE_SCREENSHOT_ACTION);
            byte[] bytes = stream.toByteArray();
            intent.putExtra(SCREEN_KEY, bytes);
            intent.putExtra(TestBroadcastReceiver.LIST_FRAGMENTS_KEY, listFragments.substring(0, listFragments.lastIndexOf(BR_TAG) != -1 ? listFragments.lastIndexOf(BR_TAG) : 0));
            intent.putExtra(TestBroadcastReceiver.TITLE_KEY, FragmentInfoHelper.getActivityTitle(activity));
            intent.putExtra(TestBroadcastReceiver.ACTIVITY_CLASS_NAME_KEY, activity.getComponentName().getShortClassName());
            intent.putExtra(TestBroadcastReceiver.PACKAGE_NAME_KEY, activity.getPackageName());
            context.sendBroadcast(intent);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }
}
