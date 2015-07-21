package amtt.epam.com.amtt.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.res.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import amtt.epam.com.amtt.AmttApplication;
import amtt.epam.com.amtt.database.constant.ActivityInfoConstants;

/**
 @author Artsiom_Kaliaha
 @version on 12.05.2015
 */

public class ActivityMetaUtil {

    public static final String ORIENTATION_UNDEFINED = "ORIENTATION_UNDEFINED";
    public static final String ORIENTATION_LANDSCAPE = "ORIENTATION_LANDSCAPE";
    public static final String ORIENTATION_PORTRAIT = "ORIENTATION_PORTRAIT";
    private static Map<Integer, String> sScreenOrientation= new HashMap<>();

    static {
        sScreenOrientation.put(Configuration.ORIENTATION_UNDEFINED, ORIENTATION_UNDEFINED);
        sScreenOrientation.put(Configuration.ORIENTATION_LANDSCAPE, ORIENTATION_LANDSCAPE);
        sScreenOrientation.put(Configuration.ORIENTATION_PORTRAIT, ORIENTATION_PORTRAIT);
    }

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
