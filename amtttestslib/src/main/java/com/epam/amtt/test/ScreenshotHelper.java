package com.epam.amtt.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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

    public static void takeScreenshot(final Context context, final Activity activity, final String listFragments) {
    // create bitmap screen capture
        Bitmap bitmap;
        View rootView;
        Log.d("TakeScreenshot",activity.hasWindowFocus()+"");
        if (!activity.hasWindowFocus()) {
            rootView = InjectionHelper.getTopWindowView();
        } else {
            rootView = activity.getWindow().getDecorView();
        }
        rootView.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
        rootView.setDrawingCacheEnabled(false);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, QUALITY_COMPRESS_SCREENSHOT, stream);
            byte[] bytes = stream.toByteArray();
            sendScreenshotAndInfo(context, activity, listFragments, bytes);
        } finally {
            try {
                stream.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    private static void sendScreenshotAndInfo(Context context, Activity activity, String listFragments, byte[] bytes) {
        Intent intent = new Intent();
        intent.setAction(REQUEST_TAKE_SCREENSHOT_ACTION);
        intent.putExtra(SCREEN_KEY, bytes);
        intent.putExtra(TestBroadcastReceiver.LIST_FRAGMENTS_KEY, listFragments.substring(0, listFragments.lastIndexOf(BR_TAG) != -1 ? listFragments.lastIndexOf(BR_TAG) : 0));
        intent.putExtra(TestBroadcastReceiver.TITLE_KEY, FragmentInfoHelper.getActivityTitle(activity));
        intent.putExtra(TestBroadcastReceiver.ACTIVITY_CLASS_NAME_KEY, activity.getComponentName().getShortClassName());
        intent.putExtra(TestBroadcastReceiver.PACKAGE_NAME_KEY, activity.getPackageName());
        context.sendBroadcast(intent);
    }
}
