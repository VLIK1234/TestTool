package amtt.epam.com.amtt.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;

import java.util.List;
import java.util.Map;

import amtt.epam.com.amtt.AmttApplication;
import amtt.epam.com.amtt.database.constant.ActivityInfoConstants;

/**
 * Created by Artsiom_Kaliaha on 12.05.2015.
 */
public class ActivityMetaUtil {

    private static Map<Integer, String> sScreenOrientation;

    public static ComponentName getTopActivityComponent() {
        ActivityManager activityManager = (ActivityManager) AmttApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
        ActivityManager.RunningTaskInfo topTaskInfo = tasks.get(0);
        return topTaskInfo.topActivity;
    }

    public static String getScreenOrientation(int orientation) {
        return sScreenOrientation.get(orientation) == null ? ActivityInfoConstants.NOT_AVAILABLE : sScreenOrientation.get(orientation);
    }
}
