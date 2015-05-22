package amtt.epam.com.amtt.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import amtt.epam.com.amtt.bo.database.ActivityMeta;
import amtt.epam.com.amtt.database.constant.ActivityInfoConstants;

/**
 * Created by Artsiom_Kaliaha on 12.05.2015.
 */
public class ActivityMetaUtil {

    private static Map<Integer, String> sConfigChanges;
    private static Map<Integer, String> sFlags;
    private static Map<Integer, String> sLaunchMode;
    private static Map<Integer, String> sPersistableMode;
    private static Map<Integer, String> sScreenOrientation;
    private static Map<Integer, String> sSoftInputMode;
    private static Map<Integer, String> sUiOptions;
    private static int sCurrentSdkVersion;

    private static ActivityInfo sActivityInfo;

    static {
        sConfigChanges = new HashMap<>();
        sConfigChanges.put(4096, ActivityInfoConstants.CONFIG_FONT_SCALE);
        sConfigChanges.put(1, ActivityInfoConstants.CONFIG_MCC);
        sConfigChanges.put(2, ActivityInfoConstants.CONFIG_MNC);
        sConfigChanges.put(4, ActivityInfoConstants.CONFIG_LOCALE);
        sConfigChanges.put(8, ActivityInfoConstants.CONFIG_TOUCHSCREEN);
        sConfigChanges.put(16, ActivityInfoConstants.CONFIG_KEYBOARD);
        sConfigChanges.put(64, ActivityInfoConstants.CONFIG_NAVIGATION);
        sConfigChanges.put(128, ActivityInfoConstants.CONFIG_ORIENTATION);
        sConfigChanges.put(256, ActivityInfoConstants.CONFIG_SCREEN_LAYOUT);
        sConfigChanges.put(8192, ActivityInfoConstants.CONFIG_LAYOUT_DIRECTION);

        sFlags = new HashMap<>();
        sFlags.put(1, ActivityInfoConstants.FLAG_MULTIPROCESS);
        sFlags.put(2, ActivityInfoConstants.FLAG_FINISH_ON_TASK_LAUNCH);
        sFlags.put(4, ActivityInfoConstants.FLAG_CLEAR_TASK_ON_LAUNCH);
        sFlags.put(8, ActivityInfoConstants.FLAG_ALWAYS_RETAIN_TASK_STATE);
        sFlags.put(16, ActivityInfoConstants.FLAG_STATE_NOT_NEEDED);
        sFlags.put(32, ActivityInfoConstants.FLAG_EXCLUDE_FROM_RECENTS);
        sFlags.put(64, ActivityInfoConstants.FLAG_ALLOW_TASK_REPARENTING);
        sFlags.put(64, ActivityInfoConstants.FLAG_ALLOW_TASK_REPARENTING);
        sFlags.put(128, ActivityInfoConstants.FLAG_NO_HISTORY);
        sFlags.put(256, ActivityInfoConstants.FLAG_FINISH_ON_CLOSE_SYSTEM_DIALOGS);
        sFlags.put(512, ActivityInfoConstants.FLAG_HARDWARE_ACCELERATED);
        sFlags.put(1073741824, ActivityInfoConstants.FLAG_SINGLE_USER);

        sLaunchMode = new HashMap<>();
        sLaunchMode.put(0, ActivityInfoConstants.LAUNCH_MULTIPLE);
        sLaunchMode.put(1, ActivityInfoConstants.LAUNCH_SINGLE_TOP);
        sLaunchMode.put(2, ActivityInfoConstants.LAUNCH_SINGLE_TASK);
        sLaunchMode.put(3, ActivityInfoConstants.LAUNCH_SINGLE_INSTANCE);

        sPersistableMode = new HashMap<>();
        sPersistableMode.put(0, ActivityInfoConstants.PERSIST_ROOT_ONLY);
        sPersistableMode.put(1, ActivityInfoConstants.PERSIST_NEVER);
        sPersistableMode.put(2, ActivityInfoConstants.PERSIST_ACROSS_REBOOTS);

        sScreenOrientation = new HashMap<>();
        sScreenOrientation.put(-1, ActivityInfoConstants.SCREEN_ORIENTATION_UNSPECIFIED);
        sScreenOrientation.put(0, ActivityInfoConstants.SCREEN_ORIENTATION_LANDSCAPE);
        sScreenOrientation.put(1, ActivityInfoConstants.SCREEN_ORIENTATION_PORTRAIT);
        sScreenOrientation.put(2, ActivityInfoConstants.SCREEN_ORIENTATION_USER);
        sScreenOrientation.put(3, ActivityInfoConstants.SCREEN_ORIENTATION_BEHIND);
        sScreenOrientation.put(4, ActivityInfoConstants.SCREEN_ORIENTATION_SENSOR);
        sScreenOrientation.put(5, ActivityInfoConstants.SCREEN_ORIENTATION_NOSENSOR);
        sScreenOrientation.put(6, ActivityInfoConstants.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        sScreenOrientation.put(7, ActivityInfoConstants.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        sScreenOrientation.put(8, ActivityInfoConstants.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        sScreenOrientation.put(9, ActivityInfoConstants.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
        sScreenOrientation.put(10, ActivityInfoConstants.SCREEN_ORIENTATION_FULL_SENSOR);
        sScreenOrientation.put(11, ActivityInfoConstants.SCREEN_ORIENTATION_USER_LANDSCAPE);
        sScreenOrientation.put(12, ActivityInfoConstants.SCREEN_ORIENTATION_USER_PORTRAIT);
        sScreenOrientation.put(13, ActivityInfoConstants.SCREEN_ORIENTATION_FULL_USER);
        sScreenOrientation.put(14, ActivityInfoConstants.SCREEN_ORIENTATION_LOCKED);

        sSoftInputMode = new HashMap<>();
        sSoftInputMode.put(0, ActivityInfoConstants.SOFT_INPUT_STATE_UNSPECIFIED); //conflict with SOFT_INPUT_ADJUST_UNSPECIFIED
        sSoftInputMode.put(1, ActivityInfoConstants.SOFT_INPUT_STATE_UNCHANGED);
        sSoftInputMode.put(2, ActivityInfoConstants.SOFT_INPUT_STATE_HIDDEN);
        sSoftInputMode.put(5, ActivityInfoConstants.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        sSoftInputMode.put(4, ActivityInfoConstants.SOFT_INPUT_STATE_VISIBLE);
        sSoftInputMode.put(16, ActivityInfoConstants.SOFT_INPUT_ADJUST_RESIZE);
        sSoftInputMode.put(32, ActivityInfoConstants.SOFT_INPUT_ADJUST_PAN);

        sUiOptions = new HashMap<>();
        sUiOptions.put(0, ActivityInfoConstants.UI_OPTIONS_NONE);
        sUiOptions.put(1, ActivityInfoConstants.UI_OPTIONS_SPLIT_ACTIONBAR_WHEN_NARROW);

        sCurrentSdkVersion = android.os.Build.VERSION.SDK_INT;
    }

    public static ActivityMeta createMeta() throws NameNotFoundException {
        sActivityInfo = getTopActivityInfo();
        String activityName = sActivityInfo.name;
        String taskAffinity = sActivityInfo.taskAffinity;
        String processName = sActivityInfo.processName;
        String packageName = sActivityInfo.packageName;
        String configChanges = getConfigChange();
        String flags = getFlags();
        String launchMode = getLaunchMode();
        String maxRecents = getMaxRecents();
        String parentActivityName = getParentActivityName();
        String permission = getPermission();
        String persistableMode = getPersistableMode();
        String screenOrientation = getScreenOrientation();
        String softInputMode = getSoftInputMode();
        String targetActivity = getTargetActivity();
        String uiOptions = getUiOptions();
        sActivityInfo = null;
        return new ActivityMeta(activityName, taskAffinity, processName, packageName, configChanges, flags, launchMode, maxRecents,
                parentActivityName, permission, persistableMode, screenOrientation, softInputMode, targetActivity, uiOptions);
    }

    public static ComponentName getTopActivityComponent() {
        ActivityManager activityManager = (ActivityManager) ContextHolder.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
        ActivityManager.RunningTaskInfo topTaskInfo = tasks.get(0);
        return topTaskInfo.topActivity;
    }

    public static ActivityInfo getTopActivityInfo() throws NameNotFoundException {
        return ContextHolder.getContext()
                    .getPackageManager()
                    .getActivityInfo(getTopActivityComponent(), PackageManager.GET_META_DATA & PackageManager.GET_INTENT_FILTERS);
    }

    private static String getConfigChange() {
        return sConfigChanges.get(sActivityInfo.configChanges) == null ? ActivityInfoConstants.NOT_AVAILABLE : sConfigChanges.get(sActivityInfo.configChanges);
    }

    private static String getFlags() {
        return sFlags.get(sActivityInfo.flags) == null ? ActivityInfoConstants.NOT_AVAILABLE : sFlags.get(sActivityInfo.flags);
    }

    private static String getLaunchMode() {
        return sLaunchMode.get(sActivityInfo.launchMode) == null ? ActivityInfoConstants.NOT_AVAILABLE : sLaunchMode.get(sActivityInfo.launchMode);
    }

    private static String getMaxRecents() {
        if (sCurrentSdkVersion < Build.VERSION_CODES.LOLLIPOP) {
            return ActivityInfoConstants.NOT_SUPPORTED;
        }
        return Integer.toString(sActivityInfo.maxRecents);
    }

    private static String getParentActivityName() {
        if (sCurrentSdkVersion < Build.VERSION_CODES.JELLY_BEAN) {
            return ActivityInfoConstants.NOT_SUPPORTED;
        }
        return sActivityInfo.parentActivityName == null ? ActivityInfoConstants.NOT_AVAILABLE : sActivityInfo.parentActivityName;
    }

    private static String getPermission() {
        return sActivityInfo.permission == null ? ActivityInfoConstants.NOT_AVAILABLE : sActivityInfo.permission;
    }

    private static String getPersistableMode() {
        if (sCurrentSdkVersion < Build.VERSION_CODES.LOLLIPOP) {
            return ActivityInfoConstants.NOT_SUPPORTED;
        }
        return sPersistableMode.get(sActivityInfo.persistableMode) == null ? ActivityInfoConstants.NOT_AVAILABLE : sPersistableMode.get(sActivityInfo.persistableMode);
    }

    private static String getScreenOrientation() {
        return sScreenOrientation.get(sActivityInfo.screenOrientation) == null ? ActivityInfoConstants.NOT_AVAILABLE : sScreenOrientation.get(sActivityInfo.screenOrientation);
    }

    private static String getSoftInputMode() {
        return sSoftInputMode.get(sActivityInfo.softInputMode) == null ? ActivityInfoConstants.NOT_AVAILABLE : sSoftInputMode.get(sActivityInfo.softInputMode);
    }

    private static String getTargetActivity() {
        return sActivityInfo.targetActivity == null ? ActivityInfoConstants.NOT_AVAILABLE : sActivityInfo.targetActivity;
    }

    private static String getUiOptions() {
        return sUiOptions.get(sActivityInfo.uiOptions);
    }

}
