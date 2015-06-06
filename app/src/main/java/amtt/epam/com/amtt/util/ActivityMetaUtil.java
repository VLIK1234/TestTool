package amtt.epam.com.amtt.util;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.view.WindowManager;

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
        sConfigChanges.put(ActivityInfo.CONFIG_FONT_SCALE, ActivityInfoConstants.CONFIG_FONT_SCALE);
        sConfigChanges.put(ActivityInfo.CONFIG_MCC, ActivityInfoConstants.CONFIG_MCC);
        sConfigChanges.put(ActivityInfo.CONFIG_MNC, ActivityInfoConstants.CONFIG_MNC);
        sConfigChanges.put(ActivityInfo.CONFIG_LOCALE, ActivityInfoConstants.CONFIG_LOCALE);
        sConfigChanges.put(ActivityInfo.CONFIG_TOUCHSCREEN, ActivityInfoConstants.CONFIG_TOUCHSCREEN);
        sConfigChanges.put(ActivityInfo.CONFIG_KEYBOARD, ActivityInfoConstants.CONFIG_KEYBOARD);
        sConfigChanges.put(ActivityInfo.CONFIG_NAVIGATION, ActivityInfoConstants.CONFIG_NAVIGATION);
        sConfigChanges.put(ActivityInfo.CONFIG_ORIENTATION, ActivityInfoConstants.CONFIG_ORIENTATION);
        sConfigChanges.put(ActivityInfo.CONFIG_SCREEN_LAYOUT, ActivityInfoConstants.CONFIG_SCREEN_LAYOUT);
        sConfigChanges.put(ActivityInfo.CONFIG_LAYOUT_DIRECTION, ActivityInfoConstants.CONFIG_LAYOUT_DIRECTION);

        sFlags = new HashMap<>();
        sFlags.put(ActivityInfo.FLAG_MULTIPROCESS, ActivityInfoConstants.FLAG_MULTIPROCESS);
        sFlags.put(ActivityInfo.FLAG_FINISH_ON_TASK_LAUNCH, ActivityInfoConstants.FLAG_FINISH_ON_TASK_LAUNCH);
        sFlags.put(ActivityInfo.FLAG_CLEAR_TASK_ON_LAUNCH, ActivityInfoConstants.FLAG_CLEAR_TASK_ON_LAUNCH);
        sFlags.put(ActivityInfo.FLAG_ALWAYS_RETAIN_TASK_STATE, ActivityInfoConstants.FLAG_ALWAYS_RETAIN_TASK_STATE);
        sFlags.put(ActivityInfo.FLAG_STATE_NOT_NEEDED, ActivityInfoConstants.FLAG_STATE_NOT_NEEDED);
        sFlags.put(ActivityInfo.FLAG_EXCLUDE_FROM_RECENTS, ActivityInfoConstants.FLAG_EXCLUDE_FROM_RECENTS);
        sFlags.put(ActivityInfo.FLAG_ALLOW_TASK_REPARENTING, ActivityInfoConstants.FLAG_ALLOW_TASK_REPARENTING);
        sFlags.put(ActivityInfo.FLAG_ALLOW_TASK_REPARENTING, ActivityInfoConstants.FLAG_ALLOW_TASK_REPARENTING);
        sFlags.put(ActivityInfo.FLAG_NO_HISTORY, ActivityInfoConstants.FLAG_NO_HISTORY);
        sFlags.put(ActivityInfo.FLAG_FINISH_ON_CLOSE_SYSTEM_DIALOGS, ActivityInfoConstants.FLAG_FINISH_ON_CLOSE_SYSTEM_DIALOGS);
        sFlags.put(ActivityInfo.FLAG_HARDWARE_ACCELERATED, ActivityInfoConstants.FLAG_HARDWARE_ACCELERATED);
        sFlags.put(ActivityInfo.FLAG_SINGLE_USER, ActivityInfoConstants.FLAG_SINGLE_USER);

        sLaunchMode = new HashMap<>();
        sLaunchMode.put(ActivityInfo.LAUNCH_MULTIPLE, ActivityInfoConstants.LAUNCH_MULTIPLE);
        sLaunchMode.put(ActivityInfo.LAUNCH_SINGLE_TOP, ActivityInfoConstants.LAUNCH_SINGLE_TOP);
        sLaunchMode.put(ActivityInfo.LAUNCH_SINGLE_TASK, ActivityInfoConstants.LAUNCH_SINGLE_TASK);
        sLaunchMode.put(ActivityInfo.LAUNCH_SINGLE_INSTANCE, ActivityInfoConstants.LAUNCH_SINGLE_INSTANCE);

        sPersistableMode = new HashMap<>();
        sPersistableMode.put(ActivityInfo.PERSIST_ROOT_ONLY, ActivityInfoConstants.PERSIST_ROOT_ONLY);
        sPersistableMode.put(ActivityInfo.PERSIST_NEVER, ActivityInfoConstants.PERSIST_NEVER);
        sPersistableMode.put(ActivityInfo.PERSIST_ACROSS_REBOOTS, ActivityInfoConstants.PERSIST_ACROSS_REBOOTS);

        sScreenOrientation = new HashMap<>();
        sScreenOrientation.put(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED, ActivityInfoConstants.SCREEN_ORIENTATION_UNSPECIFIED);
        sScreenOrientation.put(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE, ActivityInfoConstants.SCREEN_ORIENTATION_LANDSCAPE);
        sScreenOrientation.put(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT, ActivityInfoConstants.SCREEN_ORIENTATION_PORTRAIT);
        sScreenOrientation.put(ActivityInfo.SCREEN_ORIENTATION_USER, ActivityInfoConstants.SCREEN_ORIENTATION_USER);
        sScreenOrientation.put(ActivityInfo.SCREEN_ORIENTATION_BEHIND, ActivityInfoConstants.SCREEN_ORIENTATION_BEHIND);
        sScreenOrientation.put(ActivityInfo.SCREEN_ORIENTATION_SENSOR, ActivityInfoConstants.SCREEN_ORIENTATION_SENSOR);
        sScreenOrientation.put(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR, ActivityInfoConstants.SCREEN_ORIENTATION_NOSENSOR);
        sScreenOrientation.put(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE, ActivityInfoConstants.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        sScreenOrientation.put(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT, ActivityInfoConstants.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        sScreenOrientation.put(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE, ActivityInfoConstants.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        sScreenOrientation.put(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT, ActivityInfoConstants.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
        sScreenOrientation.put(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR, ActivityInfoConstants.SCREEN_ORIENTATION_FULL_SENSOR);
        sScreenOrientation.put(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE, ActivityInfoConstants.SCREEN_ORIENTATION_USER_LANDSCAPE);
        sScreenOrientation.put(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT, ActivityInfoConstants.SCREEN_ORIENTATION_USER_PORTRAIT);
        sScreenOrientation.put(ActivityInfo.SCREEN_ORIENTATION_FULL_USER, ActivityInfoConstants.SCREEN_ORIENTATION_FULL_USER);
        sScreenOrientation.put(ActivityInfo.SCREEN_ORIENTATION_LOCKED, ActivityInfoConstants.SCREEN_ORIENTATION_LOCKED);

        sSoftInputMode = new HashMap<>();
        sSoftInputMode.put(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED, ActivityInfoConstants.SOFT_INPUT_STATE_UNSPECIFIED); //conflict with SOFT_INPUT_ADJUST_UNSPECIFIED
        sSoftInputMode.put(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED, ActivityInfoConstants.SOFT_INPUT_STATE_UNCHANGED);
        sSoftInputMode.put(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN, ActivityInfoConstants.SOFT_INPUT_STATE_HIDDEN);
        sSoftInputMode.put(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE, ActivityInfoConstants.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        sSoftInputMode.put(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE, ActivityInfoConstants.SOFT_INPUT_STATE_VISIBLE);
        sSoftInputMode.put(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE, ActivityInfoConstants.SOFT_INPUT_ADJUST_RESIZE);
        sSoftInputMode.put(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN, ActivityInfoConstants.SOFT_INPUT_ADJUST_PAN);

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
