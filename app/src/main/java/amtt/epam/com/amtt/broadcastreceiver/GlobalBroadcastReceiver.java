package amtt.epam.com.amtt.broadcastreceiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import amtt.epam.com.amtt.R;
import amtt.epam.com.amtt.database.util.StepUtil;
import amtt.epam.com.amtt.helper.NotificationIdConstant;
import amtt.epam.com.amtt.observer.AmttFileObserver;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.ui.activities.CreateIssueActivity;
import amtt.epam.com.amtt.ui.activities.HelpDialogActivity;
import amtt.epam.com.amtt.util.ActivityMetaUtil;
import amtt.epam.com.amtt.util.PreferenceUtils;

/**
 @author Ivan_Bakach
 @version on 02.06.2015
 */

public class GlobalBroadcastReceiver extends BroadcastReceiver {

    public static final String LOG_FILE = "LOG_FILE";
    public static final String REQUEST_TAKE_SCREENSHOT = "REQUEST_TAKE_SCREENSHOT";
    public static final String SCREEN_PATH_KEY = "screenPath";
    public static final String FILE_PATH_KEY = "filePath";
    public static final String EXCEPTION_ANSWER = "EXCEPTION_ANSWER";
    public static final String ANSWER_EXCEPTION_KEY = "answer";
    public static String logFilePath = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case LOG_FILE:
                Bundle extras = intent.getExtras();
                String filePath;
                if (extras!=null) {
                    filePath = extras.getString(FILE_PATH_KEY);
                    logFilePath = filePath;
                    if (!AmttFileObserver.getImageArray().contains(logFilePath)) {
                        AmttFileObserver.addToImageArray(logFilePath);
                    }
                    Toast.makeText(context, "Create log in file "+logFilePath, Toast.LENGTH_LONG).show();break;
                }break;
            case REQUEST_TAKE_SCREENSHOT:
                Bundle extrasScreenshot = intent.getExtras();
                if (extrasScreenshot!=null) {
                    String screenPath = extrasScreenshot.getString(SCREEN_PATH_KEY);
                    StepUtil.saveStep(ActivityMetaUtil.getTopActivityComponent(), screenPath);
                    TopButtonService.sendActionChangeTopButtonVisibility(true);
                    HelpDialogActivity.setIsCanTakeScreenshot(false);
                    Toast.makeText(context, "Create screenshot in "+screenPath, Toast.LENGTH_LONG).show();break;
                }break;
            case EXCEPTION_ANSWER:
                Bundle extraAnswear = intent.getExtras();
                if (extraAnswear!=null) {
                    String answer = extraAnswear.getString(ANSWER_EXCEPTION_KEY);
                    Toast.makeText(context, answer, Toast.LENGTH_LONG).show();
                    NotificationCompat.Builder builder =  new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.button_main)
                            .setGroup(context.getString(R.string.label_amtt_system_group_notification))
                            .setContentTitle(context.getString(R.string.title_error_caught_notification))
                            .setContentText(context.getString(R.string.text_caught_exception))
                            .setContentIntent(PendingIntent.getActivity(context, 1, new Intent(context, CreateIssueActivity.class), PendingIntent.FLAG_CANCEL_CURRENT));
                    NotificationManager managerCompat = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                    PreferenceUtils.putBoolean(context.getString(R.string.key_is_attach_logs), true);
                    managerCompat.notify(NotificationIdConstant.CAUGHT_EXCEPTION, builder.build());
                    break;
                }break;
        }
    }
}
