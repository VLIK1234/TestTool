package amtt.epam.com.amtt.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import amtt.epam.com.amtt.database.util.StepUtil;
import amtt.epam.com.amtt.observer.AmttFileObserver;
import amtt.epam.com.amtt.topbutton.service.TopButtonService;
import amtt.epam.com.amtt.ui.activities.HelpDialogActivity;
import amtt.epam.com.amtt.util.ActivityMetaUtil;

/**
 @author Ivan_Bakach
 @version on 02.06.2015
 */

public class GlobalBroadcastReceiver extends BroadcastReceiver {

    public static final String SHOW = "SHOW";
    public static final String LOG_FILE = "LOG_FILE";
    public static final String REQUEST_TAKE_SCREENSHOT = "REQUEST_TAKE_SCREENSHOT";
    public static final String SCREEN_PATH_KEY = "screenPath";
    public static final String FILE_PATH_KEY = "filePath";
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
        }
    }
}
