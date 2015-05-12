package amtt.epam.com.amtt.bo.database;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import amtt.epam.com.amtt.util.ContextHolder;
import amtt.epam.com.amtt.util.IOUtils;

/**
 * Created by Artsiom_Kaliaha on 12.05.2015.
 */
public class Step {

    private static final String SCREENSHOT_COMMAND = "/system/bin/screencap -p ";
    private static final String CHANGE_PERMISSION_COMMAND = "chmod 777 ";
    public static final String SCREENSHOT_FOLDER = "/screenshot";

    private static final String sScreenBasePath;
    private final int mStepNumber;
    private final ComponentName mActivityComponent;

    static {
        sScreenBasePath = ContextHolder.getContext().getFilesDir().getPath() + SCREENSHOT_FOLDER;
    }

    public Step(int stepNumber) {
        mStepNumber = stepNumber;
        mActivityComponent = getTopActivity();
    }

    public ComponentName getActivityComponent() {
        return mActivityComponent;
    }

    public int getStepNumber() {
        return mStepNumber;
    }

    //help methods
    private ComponentName getTopActivity() {
        ActivityManager activityManager = (ActivityManager) ContextHolder.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
        ActivityManager.RunningTaskInfo topTaskInfo = tasks.get(0);
        return topTaskInfo.topActivity;
    }

    public String saveScreen() throws IOException {
        String screenPath = null;
        Process fileSavingProcess = null;
        Process changeModeProcess = null;
        OutputStream fileSavingStream = null;
        OutputStream changeModeStream = null;
        try {
            fileSavingProcess = Runtime.getRuntime().exec("su");
            fileSavingStream = fileSavingProcess.getOutputStream();
            fileSavingStream.write((SCREENSHOT_COMMAND + (screenPath = sScreenBasePath + "/screen" + mStepNumber + ".png")).getBytes("ASCII"));
            fileSavingStream.flush();
            fileSavingStream.close();

            changeModeProcess = Runtime.getRuntime().exec("su");
            changeModeStream = changeModeProcess.getOutputStream();
            changeModeStream.write((CHANGE_PERMISSION_COMMAND + screenPath + "\n").getBytes("ASCII"));
            changeModeStream.flush();
            changeModeStream.close();
            changeModeProcess.destroy();
        } finally {
            IOUtils.close(fileSavingStream, changeModeStream);
            IOUtils.destroyProcesses(fileSavingProcess, changeModeProcess);
        }
        return screenPath;
    }

}
