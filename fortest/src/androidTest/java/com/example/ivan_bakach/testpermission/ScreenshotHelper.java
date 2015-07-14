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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

/**
 * Created by Ivan_Bakach on 14.07.2015.
 */
public class ScreenshotHelper {
    public static void takeScreenshot(Activity activity, Context context){
        String mPath = Environment.getExternalStorageDirectory().toString() + "/Amtt_cache/" + "Screenshot_"+getCurrentDateTime()+".jpg";

// create bitmap screen capture
        Bitmap bitmap;
        View v1 = activity.getWindow().getDecorView().getRootView();
        v1.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(v1.getDrawingCache());
        v1.setDrawingCacheEnabled(false);

        OutputStream fout = null;
        File imageFile = new File(mPath);

        try {
            fout = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout);
            fout.flush();
            fout.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent();
        intent.setAction("REQUEST_TAKE_SCREENSHOT");
        intent.putExtra("screenPath", mPath);
        context.sendBroadcast(intent);
    }

    private static String getCurrentDateTime() {
        Calendar c = new GregorianCalendar(SimpleTimeZone.getDefault());
        String dateTimeTemplate = "%s-%s-%s-%s-%s-%s";
        return String.format(dateTimeTemplate, c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1,
                c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE), c.get(Calendar.SECOND));
    }
}
