package amtt.epam.com.amtt.broadcastreceiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.database.util.StepUtil;
import amtt.epam.com.amtt.helper.NotificationIdConstant;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.ui.activities.CreateIssueActivity;
import amtt.epam.com.amtt.util.ActivityMetaUtil;
import amtt.epam.com.amtt.util.PreferenceUtil;

/**
 @author Ivan_Bakach
 @version on 02.06.2015
 */

public class GlobalBroadcastReceiver extends BroadcastReceiver {

    public static final String LOG_FILE = "LOG_FILE";
    public static final String REQUEST_TAKE_SCREENSHOT = "REQUEST_TAKE_SCREENSHOT";
    public static final String SCREEN_PATH_KEY = "screenPath";
    public static final String LIST_FRAGMENTS_KEY = "listFragments";
    public static final String FILE_PATH_KEY = "filePath";
    public static final String EXCEPTION_ANSWER = "EXCEPTION_ANSWER";
    public static final String ANSWER_EXCEPTION_KEY = "answer";
    public static String logFilePath = "";
    public static boolean isStepWithoutActivityInfo = false;


    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case LOG_FILE:
                Bundle extras = intent.getExtras();
                String filePath;
                if (extras!=null) {
                    filePath = extras.getString(FILE_PATH_KEY);
                    logFilePath = filePath;
                    Toast.makeText(context, "Create log in file "+logFilePath, Toast.LENGTH_LONG).show();break;
                }break;
            case REQUEST_TAKE_SCREENSHOT:
                Bundle extrasScreenshot = intent.getExtras();
                if (extrasScreenshot!=null) {
                    final String screenPath = extrasScreenshot.getString(SCREEN_PATH_KEY);
                    final String listFragments = extrasScreenshot.getString(LIST_FRAGMENTS_KEY);
                    if (screenPath!=null&&listFragments!=null) {
                        if (isStepWithoutActivityInfo) {
                            isStepWithoutActivityInfo = false;
                            StepUtil.savePureScreenshot(screenPath);
                        } else {
                            StepUtil.saveStep(ActivityMetaUtil.getTopActivityComponent(), screenPath, listFragments);
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
        }
    }

    public static void setStepWithoutActivityInfo(boolean stepWithoutActivityInfo) {
        isStepWithoutActivityInfo = stepWithoutActivityInfo;
    }
}
