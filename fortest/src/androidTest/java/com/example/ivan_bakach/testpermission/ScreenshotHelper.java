package com.example.ivan_bakach.testpermission;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ivan_Bakach on 14.07.2015.
 */
public class ScreenshotHelper {

    public static final String REQUEST_TAKE_SCREENSHOT_ACTION = "REQUEST_TAKE_SCREENSHOT";
    public static final String LIST_FRAGMENTS_KEY = "listFragments";
    private static final String SCREENSHOT_FILE_NAME_TEMPLATE = "Screenshot_%s.png";
    public static final String SCREENSHOT_DATETIME_FORMAT = "yyyy-MM-dd-HH-mm-ss";
    public static final String SCREEN_PATH_KEY = "screenPath";
    public static final int QUALITY_COMPRESS_SCREENSHOT = 90;

    public static void takeScreenshot(Context context, Activity activity, String listFragments) {
        long imageTime = System.currentTimeMillis();
        String imageDate = new SimpleDateFormat(SCREENSHOT_DATETIME_FORMAT).format(new Date(imageTime));
        String imageFileName = String.format(SCREENSHOT_FILE_NAME_TEMPLATE, imageDate);
        String path = Environment.getExternalStorageDirectory().toString() + File.separatorChar + LogManger.AMTT_CACHE_DIRECTORY + File.separatorChar + imageFileName;

// create bitmap screen capture
        Bitmap bitmap;
        View v1 = activity.getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);

        OutputStream fout = null;
        File imageFile = new File(path);

        try {
            fout = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, QUALITY_COMPRESS_SCREENSHOT, fout);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(fout);
        }

        Intent intent = new Intent();
        intent.setAction(REQUEST_TAKE_SCREENSHOT_ACTION);
        intent.putExtra(SCREEN_PATH_KEY, path);
        intent.putExtra(LIST_FRAGMENTS_KEY, listFragments);
        context.sendBroadcast(intent);
    }
}
