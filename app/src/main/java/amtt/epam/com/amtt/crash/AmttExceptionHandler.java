package amtt.epam.com.amtt.crash;

import android.app.ActivityManager;
import android.content.Context;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Artsiom_Kaliaha on 27.03.2015.
 */
public class AmttExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final Context mContext;
    private final StringBuilder mStringBuilder;
    private String mFileName;
    private final Thread.UncaughtExceptionHandler mUncaughtExceptionHandler;

    private static final SimpleDateFormat sDateFormat;
    private static final String FORMAT_PATTERN = "yyyy-MM-dd-HH-mm-ss";

    static {
        sDateFormat = new SimpleDateFormat(FORMAT_PATTERN);
    }

    public AmttExceptionHandler(Context context) {
        mContext = context;
        mUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();

        mStringBuilder = new StringBuilder();
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = activityManager.getRunningAppProcesses();

        String processName = null;
        for (ActivityManager.RunningAppProcessInfo processInfo : runningApps) {
            if (processInfo.pid == android.os.Process.myPid()) {
                processName = processInfo.processName;
            }
        }

        Date date = new Date();
        mStringBuilder.append(mFileName = sDateFormat.format(date))
                .append("\n\nProcess: ")
                .append(processName)
                .append(", PID: ")
                .append(android.os.Process.myPid())
                .append("\n")
                .append(ex.toString());

        for (StackTraceElement element : ex.getStackTrace()) {
            mStringBuilder.append("\nat ")
                    .append(element.toString());
        }

        new CrashInfoSavingTask(mContext.getFilesDir().getPath() + "/crash.txt", mStringBuilder.toString()).execute();

        mUncaughtExceptionHandler.uncaughtException(thread, ex);
    }

}
