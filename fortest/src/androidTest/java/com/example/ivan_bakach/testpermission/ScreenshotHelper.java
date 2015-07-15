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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

/**
 * Created by Ivan_Bakach on 14.07.2015.
 */
public class ScreenshotHelper {
    private static final String SCREENSHOT_FILE_NAME_TEMPLATE = "Screenshot_%s.png";
    public static void takeScreenshot(Activity activity, Context context){
        long imageTime = System.currentTimeMillis();
        String imageDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date(imageTime));
        String imageFileName = String.format(SCREENSHOT_FILE_NAME_TEMPLATE, imageDate);
        String path = Environment.getExternalStorageDirectory().toString() + "/Amtt_cache/" + imageFileName;

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
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fout);
            fout.flush();
            fout.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent();
        intent.setAction("REQUEST_TAKE_SCREENSHOT");
        intent.putExtra("screenPath", path);
        context.sendBroadcast(intent);
    }
}
