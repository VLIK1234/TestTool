package amtt.epam.com.amtt.broadcastreceiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.*;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.Process;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.Inflater;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.database.util.StepUtil;
import amtt.epam.com.amtt.helper.NotificationIdConstant;
import amtt.epam.com.amtt.helper.ScreenshotHelper;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.ui.activities.CreateIssueActivity;
import amtt.epam.com.amtt.util.FileUtil;
import amtt.epam.com.amtt.util.IOUtils;
import amtt.epam.com.amtt.util.PreferenceUtil;

/**
 * @author Ivan_Bakach
 * @version on 02.06.2015
 */

public class GlobalBroadcastReceiver extends BroadcastReceiver {

    public static final String SEND_LOG_FILE = "SEND_LOG_FILE";
    public static final String REQUEST_TAKE_SCREENSHOT = "REQUEST_TAKE_SCREENSHOT";
    public static final String EXCEPTION_ANSWER = "EXCEPTION_ANSWER";
    public static final String ANSWER_EXCEPTION_KEY = "answer";
    public static final String REQUEST_TAKE_ONLY_INFO = "REQUEST_TAKE_ONLY_INFO";
    public static final String FILE_NAME_KEY = "fileName";
    public static final String BYTE_ARRAY_DATA_KEY = "byteArrayData";
    public static final String CAT_ALL_FILE = "CAT_ALL_FILE";
    public static boolean isStepWithoutActivityInfo = false;

    public static final String LIST_FRAGMENTS_KEY = "listFragments";
    public static final String ACTIVITY_CLASS_NAME_KEY = "activityClassName";
    public static final String PACKAGE_NAME_KEY = "packageName";
    public static final String SCREEN_KEY = "screen";


    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case SEND_LOG_FILE:
                Bundle extras = intent.getExtras();
                byte[] bytesArray;
                String fileName = null;
                if (extras != null) {
                    fileName = extras.getString(FILE_NAME_KEY);
                    bytesArray = extras.getByteArray(BYTE_ARRAY_DATA_KEY);
                    File transferFile = new File(FileUtil.getCacheAmttDir(),fileName);
                    IOUtils.byteArrayToFile(bytesArray, transferFile);
//                    Toast.makeText(context, "Create log in file " + bytesArray.length, Toast.LENGTH_LONG).show();
                    break;
                }
                break;
            case CAT_ALL_FILE:
//                    Process processx = Runtime.getRuntime().exec("cat "+ FileUtil.getCacheAmttDir() + "log_common* > "+ FileUtil.getCacheAmttDir()+"log_common135.txt");
                Log.d("TAG", "/bin/sh cat "+ FileUtil.getCacheAmttDir() + "log_common* > "+FileUtil.getCacheAmttDir()+"log_common135.txt");
                Toast.makeText(context, "Cat all file", Toast.LENGTH_LONG).show();
                break;
            case REQUEST_TAKE_SCREENSHOT:
                Bundle extrasScreenshot = intent.getExtras();
                if (extrasScreenshot != null) {
                    final String listFragments = extrasScreenshot.getString(LIST_FRAGMENTS_KEY);
                    final String activtyClassName = extrasScreenshot.getString(ACTIVITY_CLASS_NAME_KEY);
                    final String packageName = extrasScreenshot.getString(PACKAGE_NAME_KEY);

                    byte[] bytes = extrasScreenshot.getByteArray(SCREEN_KEY);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    final String screenPath = ScreenshotHelper.writeBitmapInFile(bitmap);
                    if (screenPath != null && listFragments != null) {
                        if (isStepWithoutActivityInfo) {
                            isStepWithoutActivityInfo = false;
                            StepUtil.savePureScreenshot(screenPath);
                        } else {
                            StepUtil.saveStep(activtyClassName, packageName, screenPath, listFragments);
                        }
                        TopButtonService.sendActionChangeTopButtonVisibility(true);
                        Toast.makeText(context, "Create screenshot in " + screenPath, Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        final String failScreen = extrasScreenshot.getString("failScreen");
                        Toast.makeText(context, failScreen, Toast.LENGTH_SHORT).show();
                        TopButtonService.sendActionChangeTopButtonVisibility(true);
                    }
                }
                break;
            case EXCEPTION_ANSWER:
                Bundle extraAnswear = intent.getExtras();
                if (extraAnswear != null) {
                    String answer = extraAnswear.getString(ANSWER_EXCEPTION_KEY);
                    Toast.makeText(context, "Application crashed go to notification for make ticket", Toast.LENGTH_LONG).show();
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.button_main)
                            .setGroup(context.getString(R.string.label_amtt_system_group_notification))
                            .setOngoing(true)
                            .setAutoCancel(true)
                            .setContentTitle(context.getString(R.string.title_error_caught_notification))
                            .setContentText(context.getString(R.string.text_caught_exception))
                            .setContentIntent(PendingIntent.getActivity(context, 1, new Intent(context, CreateIssueActivity.class), PendingIntent.FLAG_CANCEL_CURRENT));
                    NotificationManager managerCompat = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                    PreferenceUtil.putBoolean(context.getString(R.string.key_is_attach_logs), true);
                    managerCompat.notify(NotificationIdConstant.CAUGHT_EXCEPTION, builder.build());
                    break;
                }
                break;
            case REQUEST_TAKE_ONLY_INFO:
                Bundle extraInfo = intent.getExtras();
                if (extraInfo != null) {
                    final String listFragments = extraInfo.getString(LIST_FRAGMENTS_KEY);
                    final String activtyClassName = extraInfo.getString(ACTIVITY_CLASS_NAME_KEY);
                    final String packageName = extraInfo.getString(PACKAGE_NAME_KEY);
                    StepUtil.saveStep(activtyClassName, packageName, null, listFragments);
                    Toast.makeText(context, context.getString(R.string.label_added_step_without_screen), Toast.LENGTH_LONG).show();
                    break;
                }
                break;
        }
    }

    public static void setStepWithoutActivityInfo(boolean stepWithoutActivityInfo) {
        isStepWithoutActivityInfo = stepWithoutActivityInfo;
    }
}
