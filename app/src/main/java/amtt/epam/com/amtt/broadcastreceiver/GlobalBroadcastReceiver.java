package amtt.epam.com.amtt.broadcastreceiver;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.database.util.StepUtil;
import amtt.epam.com.amtt.helper.NotificationIdConstant;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.ui.activities.CreateIssueActivity;
import amtt.epam.com.amtt.util.ActivityMetaUtil;
import amtt.epam.com.amtt.util.IOUtils;
import amtt.epam.com.amtt.util.PreferenceUtil;

/**
 @author Ivan_Bakach
 @version on 02.06.2015
 */

public class GlobalBroadcastReceiver extends BroadcastReceiver {

    public static final String LOG_FILE = "LOG_FILE";
    public static final String REQUEST_TAKE_SCREENSHOT = "REQUEST_TAKE_SCREENSHOT";
    public static final String FILE_PATH_KEY = "filePath";
    public static final String EXCEPTION_ANSWER = "EXCEPTION_ANSWER";
    public static final String ANSWER_EXCEPTION_KEY = "answer";
    public static final String REQUEST_TAKE_ONLY_INFO = "REQUEST_TAKE_ONLY_INFO";
    public static String sLogFilePath = "";
    public static boolean isStepWithoutActivityInfo = false;

    public static final String LIST_FRAGMENTS_KEY = "listFragments";
    private static final String SCREENSHOT_FILE_NAME_TEMPLATE = "Screenshot13_%s.jpeg";
    public static final String SCREENSHOT_DATETIME_FORMAT = "yyyy-MM-dd-HH-mm-ss";
    public static final String SCREEN_PATH_KEY = "screenPath";
    public static final int QUALITY_COMPRESS_SCREENSHOT = 90;
    public static final String ACTIVITY_CLASS_NAME_KEY = "activityClassName";
    public static final String PACKAGE_NAME_KEY = "packageName";
    public static final String AMTT_CACHE_DIRECTORY = "Amtt_cache";


    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case LOG_FILE:
                Bundle extras = intent.getExtras();
                String filePath;
                if (extras!=null) {
                    filePath = extras.getString(FILE_PATH_KEY);
                    sLogFilePath = filePath;
                    Toast.makeText(context, "Create log in file "+ sLogFilePath, Toast.LENGTH_LONG).show();break;
                }break;
            case REQUEST_TAKE_SCREENSHOT:
                Bundle extrasScreenshot = intent.getExtras();
                if (extrasScreenshot!=null) {
                    final String listFragments = extrasScreenshot.getString(LIST_FRAGMENTS_KEY);
                    final String activtyClassName = extrasScreenshot.getString(ACTIVITY_CLASS_NAME_KEY);
                    final String packageName = extrasScreenshot.getString(PACKAGE_NAME_KEY);

                    byte[] bytes = extrasScreenshot.getByteArray("image");
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    final String screenPath = writeBitmapInFile(bitmap);
                    if (screenPath!=null&&listFragments!=null) {
                        if (isStepWithoutActivityInfo) {
                            isStepWithoutActivityInfo = false;
                            StepUtil.savePureScreenshot(screenPath);
                        } else {
                            StepUtil.saveStep(activtyClassName, packageName, screenPath, listFragments);
                        }
                        TopButtonService.sendActionChangeTopButtonVisibility(true);
                        Toast.makeText(context, "Create screenshot in "+screenPath, Toast.LENGTH_SHORT).show();break;
                    }
                    else {
                        final String failScreen = extrasScreenshot.getString("failScreen");
                        Toast.makeText(context, failScreen, Toast.LENGTH_SHORT).show();
                        TopButtonService.sendActionChangeTopButtonVisibility(true);
                    }
                }break;
            case EXCEPTION_ANSWER:
                Bundle extraAnswear = intent.getExtras();
                if (extraAnswear!=null) {
                    String answer = extraAnswear.getString(ANSWER_EXCEPTION_KEY);
                    Toast.makeText(context, "Application crashed go to notification for make ticket", Toast.LENGTH_LONG).show();
                    NotificationCompat.Builder builder =  new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.button_main)
                            .setGroup(context.getString(R.string.label_amtt_system_group_notification))
                            .setOngoing(true)
                            .setAutoCancel(true)
                            .setContentTitle(context.getString(R.string.title_error_caught_notification))
                            .setContentText(context.getString(R.string.text_caught_exception))
                            .setContentIntent(PendingIntent.getActivity(context, 1, new Intent(context, CreateIssueActivity.class), PendingIntent.FLAG_CANCEL_CURRENT));
                    NotificationManager managerCompat = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                    PreferenceUtil.putBoolean(context.getString(R.string.key_is_attach_logs), true);
                    managerCompat.notify(NotificationIdConstant.CAUGHT_EXCEPTION, builder.build());
                    break;
                }break;
            case REQUEST_TAKE_ONLY_INFO:
                Bundle extraInfo = intent.getExtras();
                if (extraInfo!=null) {
                    final String listFragments = extraInfo.getString(LIST_FRAGMENTS_KEY);
                    final String activtyClassName = extraInfo.getString(ACTIVITY_CLASS_NAME_KEY);
                    final String packageName = extraInfo.getString(PACKAGE_NAME_KEY);
                    StepUtil.saveStep(activtyClassName, packageName, null, listFragments);
                    Toast.makeText(context, context.getString(R.string.label_added_step_without_screen), Toast.LENGTH_LONG).show();
                    break;
                }break;
        }
    }

    public static String writeBitmapInFile(Bitmap bitmap){
        long imageTime = System.currentTimeMillis();
        String imageDate = new SimpleDateFormat(SCREENSHOT_DATETIME_FORMAT).format(new Date(imageTime));
        String imageFileName = String.format(SCREENSHOT_FILE_NAME_TEMPLATE, imageDate);
        String path = Environment.getExternalStorageDirectory().toString() + File.separatorChar + AMTT_CACHE_DIRECTORY + File.separatorChar + imageFileName;

// create bitmap screen capture
        OutputStream fout = null;
        File imageFile = new File(path);
        try {
            fout = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, QUALITY_COMPRESS_SCREENSHOT, fout);
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.close(fout);
        }
        return path;

    }

    public static void setStepWithoutActivityInfo(boolean stepWithoutActivityInfo) {
        isStepWithoutActivityInfo = stepWithoutActivityInfo;
    }
}
