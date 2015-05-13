package amtt.epam.com.amtt.util;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.pm.ActivityInfo;
import android.os.Build;

import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.database.constant.ActivityInfoConstants;
import amtt.epam.com.amtt.database.table.ActivityInfoTable;
import amtt.epam.com.amtt.database.table.StepsTable;

/**
 * Created by Artsiom_Kaliaha on 12.05.2015.
 */
public class ContentValuesUtil {

    private static Map<Integer, String> sConfigChanges;
    private static Map<Integer, String> sFlags;
    private static Map<Integer, String> sLaunchMode;
    private static Map<Integer, String> sPersistableMode;
    private static Map<Integer, String> sScreenOrientation;
    private static Map<Integer, String> sSoftInputMode;
    private static Map<Integer, String> sUiOptions;
    private static int sCurrentSdkVersion;

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

    public static ContentValues getValuesForActivityInfo(ActivityInfo activityInfo) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ActivityInfoTable._ACTIVITY_NAME, activityInfo.name);
        contentValues.put(ActivityInfoTable._CONFIG_CHANGES, getConfigChange(activityInfo));
        contentValues.put(ActivityInfoTable._FLAGS, getFlags(activityInfo));
        contentValues.put(ActivityInfoTable._LAUNCH_MODE, getLaunchMode(activityInfo));
        contentValues.put(ActivityInfoTable._MAX_RECENTS, getMaxRecents(activityInfo));
        contentValues.put(ActivityInfoTable._PARENT_ACTIVITY_NAME, getParentActivityName(activityInfo));
        contentValues.put(ActivityInfoTable._PERMISSION, getPermission(activityInfo));
        contentValues.put(ActivityInfoTable._PERSISTABLE_MODE, getPersistableMode(activityInfo));
        contentValues.put(ActivityInfoTable._SCREEN_ORIENTATION, getScreenOrientation(activityInfo));
        contentValues.put(ActivityInfoTable._SOFT_INPUT_MODE, getSoftInputMode(activityInfo));
        contentValues.put(ActivityInfoTable._TARGET_ACTIVITY_NAME, getTargetActivity(activityInfo));
        contentValues.put(ActivityInfoTable._TASK_AFFINITY, activityInfo.taskAffinity);
        contentValues.put(ActivityInfoTable._UI_OPTIONS, getUiOptions(activityInfo));
        contentValues.put(ActivityInfoTable._PROCESS_NAME, activityInfo.processName);
        contentValues.put(ActivityInfoTable._PACKAGE_NAME, activityInfo.packageName);
        return contentValues;
    }

    public static ContentValues getValuesForStep(int stepNumber, ComponentName componentName) {
        ContentValues contentValues = new ContentValues();
        ContentValues values = new ContentValues();
        values.put(StepsTable._ID, stepNumber);
        values.put(StepsTable._SCREEN_PATH, Step.SCREENSHOT_FOLDER + stepNumber);
        values.put(StepsTable._ASSOCIATED_ACTIVITY, componentName.getClassName());
        return contentValues;
    }


    private static String getConfigChange(ActivityInfo activityInfo) {
        return sConfigChanges.get(activityInfo.configChanges) == null ? ActivityInfoConstants.NOT_AVAILABLE : sConfigChanges.get(activityInfo.configChanges);
    }

    private static String getFlags(ActivityInfo activityInfo) {
        return sFlags.get(activityInfo.flags) == null ? ActivityInfoConstants.NOT_AVAILABLE : sFlags.get(activityInfo.flags);
    }

    private static String getLaunchMode(ActivityInfo activityInfo) {
        return sLaunchMode.get(activityInfo.launchMode) == null ? ActivityInfoConstants.NOT_AVAILABLE : sLaunchMode.get(activityInfo.launchMode);
    }

    private static String getMaxRecents(ActivityInfo activityInfo) {
        if (sCurrentSdkVersion < Build.VERSION_CODES.LOLLIPOP) {
            return ActivityInfoConstants.NOT_SUPPORTED;
        }
        return Integer.toString(activityInfo.maxRecents);
    }

    private static String getParentActivityName(ActivityInfo activityInfo) {
        if (sCurrentSdkVersion < Build.VERSION_CODES.JELLY_BEAN) {
            return ActivityInfoConstants.NOT_SUPPORTED;
        }
        return activityInfo.parentActivityName == null ? ActivityInfoConstants.NOT_AVAILABLE : activityInfo.parentActivityName;
    }

    private static String getPermission(ActivityInfo activityInfo) {
        return activityInfo.permission == null ? ActivityInfoConstants.NOT_AVAILABLE : activityInfo.permission;
    }

    private static String getPersistableMode(ActivityInfo activityInfo) {
        if (sCurrentSdkVersion < Build.VERSION_CODES.LOLLIPOP) {
            return ActivityInfoConstants.NOT_SUPPORTED;
        }
        return sPersistableMode.get(activityInfo.persistableMode) == null ? ActivityInfoConstants.NOT_AVAILABLE : sPersistableMode.get(activityInfo.persistableMode);
    }

    private static String getScreenOrientation(ActivityInfo activityInfo) {
        return sScreenOrientation.get(activityInfo.screenOrientation) == null ? ActivityInfoConstants.NOT_AVAILABLE : sScreenOrientation.get(activityInfo.screenOrientation);
    }

    private static String getSoftInputMode(ActivityInfo activityInfo) {
        return sSoftInputMode.get(activityInfo.softInputMode) == null ? ActivityInfoConstants.NOT_AVAILABLE : sSoftInputMode.get(activityInfo.softInputMode);
    }

    private static String getTargetActivity(ActivityInfo activityInfo) {
        return activityInfo.targetActivity == null ? ActivityInfoConstants.NOT_AVAILABLE : activityInfo.targetActivity;
    }

    private static String getUiOptions(ActivityInfo activityInfo) {
        return sUiOptions.get(activityInfo.uiOptions);
    }

}
