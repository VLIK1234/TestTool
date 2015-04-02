package amtt.epam.com.amtt.step;

import amtt.epam.com.amtt.contentprovider.AmttContentProvider;
import amtt.epam.com.amtt.database.ActivityInfoConstants;
import amtt.epam.com.amtt.database.ActivityInfoTable;
import amtt.epam.com.amtt.database.StepsTable;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Artsiom_Kaliaha on 26.03.2015.
 */
public class StepSavingTask extends AsyncTask<Void, Void, StepSavingResult> implements ActivityInfoConstants {

    private static Map<Integer, String> sConfigChanges;
    private static Map<Integer, String> sFlags;
    private static Map<Integer, String> sLaunchMode;
    private static Map<Integer, String> sPersistableMode;
    private static Map<Integer, String> sScreenOrientation;
    private static Map<Integer, String> sSoftInputMode;
    private static Map<Integer, String> sUiOptions;

    static {
        sConfigChanges = new HashMap<>();
        sConfigChanges.put(4096, CONFIG_FONT_SCALE);
        sConfigChanges.put(1, CONFIG_MCC);
        sConfigChanges.put(2, CONFIG_MNC);
        sConfigChanges.put(4, CONFIG_LOCALE);
        sConfigChanges.put(8, CONFIG_TOUCHSCREEN);
        sConfigChanges.put(16, CONFIG_KEYBOARD);
        sConfigChanges.put(64, CONFIG_NAVIGATION);
        sConfigChanges.put(128, CONFIG_ORIENTATION);
        sConfigChanges.put(256, CONFIG_SCREEN_LAYOUT);
        sConfigChanges.put(8192, CONFIG_LAYOUT_DIRECTION);

        sFlags = new HashMap<>();
        sFlags.put(1, FLAG_MULTIPROCESS);
        sFlags.put(2, FLAG_FINISH_ON_TASK_LAUNCH);
        sFlags.put(4, FLAG_CLEAR_TASK_ON_LAUNCH);
        sFlags.put(8, FLAG_ALWAYS_RETAIN_TASK_STATE);
        sFlags.put(16, FLAG_STATE_NOT_NEEDED);
        sFlags.put(32, FLAG_EXCLUDE_FROM_RECENTS);
        sFlags.put(64, FLAG_ALLOW_TASK_REPARENTING);
        sFlags.put(64, FLAG_ALLOW_TASK_REPARENTING);
        sFlags.put(128, FLAG_NO_HISTORY);
        sFlags.put(256, FLAG_FINISH_ON_CLOSE_SYSTEM_DIALOGS);
        sFlags.put(512, FLAG_HARDWARE_ACCELERATED);
        sFlags.put(1073741824, FLAG_SINGLE_USER);

        sLaunchMode = new HashMap<>();
        sLaunchMode.put(0, LAUNCH_MULTIPLE);
        sLaunchMode.put(1, LAUNCH_SINGLE_TOP);
        sLaunchMode.put(2, LAUNCH_SINGLE_TASK);
        sLaunchMode.put(3, LAUNCH_SINGLE_INSTANCE);

        sPersistableMode = new HashMap<>();
        sPersistableMode.put(0, PERSIST_ROOT_ONLY);
        sPersistableMode.put(1, PERSIST_NEVER);
        sPersistableMode.put(2, PERSIST_ACROSS_REBOOTS);

        sScreenOrientation = new HashMap<>();
        sScreenOrientation.put(-1, SCREEN_ORIENTATION_UNSPECIFIED);
        sScreenOrientation.put(0, SCREEN_ORIENTATION_LANDSCAPE);
        sScreenOrientation.put(1, SCREEN_ORIENTATION_PORTRAIT);
        sScreenOrientation.put(2, SCREEN_ORIENTATION_USER);
        sScreenOrientation.put(3, SCREEN_ORIENTATION_BEHIND);
        sScreenOrientation.put(4, SCREEN_ORIENTATION_SENSOR);
        sScreenOrientation.put(5, SCREEN_ORIENTATION_NOSENSOR);
        sScreenOrientation.put(6, SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        sScreenOrientation.put(7, SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        sScreenOrientation.put(8, SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        sScreenOrientation.put(9, SCREEN_ORIENTATION_REVERSE_PORTRAIT);
        sScreenOrientation.put(10, SCREEN_ORIENTATION_FULL_SENSOR);
        sScreenOrientation.put(11, SCREEN_ORIENTATION_USER_LANDSCAPE);
        sScreenOrientation.put(12, SCREEN_ORIENTATION_USER_PORTRAIT);
        sScreenOrientation.put(13, SCREEN_ORIENTATION_FULL_USER);
        sScreenOrientation.put(14, SCREEN_ORIENTATION_LOCKED);

        sSoftInputMode = new HashMap<>();
        sSoftInputMode.put(0, SOFT_INPUT_STATE_UNSPECIFIED); //conflict with SOFT_INPUT_ADJUST_UNSPECIFIED
        sSoftInputMode.put(1, SOFT_INPUT_STATE_UNCHANGED);
        sSoftInputMode.put(2, SOFT_INPUT_STATE_HIDDEN);
        sSoftInputMode.put(5, SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        sSoftInputMode.put(4, SOFT_INPUT_STATE_VISIBLE);
        sSoftInputMode.put(16, SOFT_INPUT_ADJUST_RESIZE);
        sSoftInputMode.put(32, SOFT_INPUT_ADJUST_PAN);

        sUiOptions = new HashMap<>();
        sUiOptions.put(0, UI_OPTIONS_NONE);
        sUiOptions.put(1, UI_OPTIONS_SPLIT_ACTIONBAR_WHEN_NARROW);
    }

    private final Context mContext;
    private final StepSavingCallback mCallback;
    private Bitmap mBitmap;
    private final Rect mRect;
    private final String mPath;
    private final ComponentName mComponentName;
    private final int mCurrentSdkVersion;

    public StepSavingTask(Context context, StepSavingCallback callback, Bitmap bitmap, Rect rect, ComponentName componentName) {
        mContext = context;
        mCallback = callback;
        mBitmap = bitmap;
        mRect = rect;
        mPath = context.getCacheDir().getPath();
        mComponentName = componentName;
        mCurrentSdkVersion = android.os.Build.VERSION.SDK_INT;
    }

    @Override
    protected StepSavingResult doInBackground(Void... params) {
        String screenPath;
        try {
            screenPath = saveScreen();
            mCallback.incrementScreenNumber();
        } catch (Exception e) {
            return StepSavingResult.ERROR;
        }

        int existingActivityInfo = mContext.getContentResolver().query(
            AmttContentProvider.ACTIVITY_META_CONTENT_URI,
            new String[]{ActivityInfoTable._ACTIVITY_NAME},
            ActivityInfoTable._ACTIVITY_NAME,
            new String[]{mComponentName.getClassName()},
            null).getCount();

        //if there is no records about current activity in db
        if (existingActivityInfo == 0) {
            ActivityInfo activityInfo;
            try {
                activityInfo = mContext
                    .getPackageManager()
                    .getActivityInfo(mComponentName, PackageManager.GET_META_DATA & PackageManager.GET_INTENT_FILTERS);
            } catch (PackageManager.NameNotFoundException e) {
                return StepSavingResult.ERROR;
            }

            saveActivityInfo(activityInfo);
        }


        saveStep(screenPath);

        return StepSavingResult.SAVED;
    }

    @Override
    protected void onPostExecute(StepSavingResult result) {
        mCallback.onStepSaved(result);
    }


    private String saveScreen() throws Exception {
        String screenPath = mPath + "/screen" + mCallback.getScreenNumber() + ".png";
        mBitmap = Bitmap.createBitmap(mBitmap, 0, mRect.top, mRect.width(), mRect.height());
        FileOutputStream bitmapPath = new FileOutputStream(screenPath);
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, bitmapPath);
        return screenPath;
    }

    private void saveActivityInfo(ActivityInfo activityInfo) {
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
        contentValues.put(ActivityInfoTable._THEME, getThemeName(activityInfo));
        contentValues.put(ActivityInfoTable._UI_OPTIONS, getUiOptions(activityInfo));
        contentValues.put(ActivityInfoTable._PROCESS_NAME, activityInfo.processName);
        contentValues.put(ActivityInfoTable._PACKAGE_NAME, activityInfo.packageName);

        mContext.getContentResolver().insert(AmttContentProvider.ACTIVITY_META_CONTENT_URI, contentValues);
    }

    private void saveStep(String screenPath) {
        ContentValues values = new ContentValues();
        values.put(StepsTable._SCREEN_PATH, screenPath);
        values.put(StepsTable._ASSOCIATED_ACTIVITY, mComponentName.getClassName());
        mContext.getContentResolver().insert(AmttContentProvider.STEP_CONTENT_URI, values);
    }


    //ActivityInfoTable methods
    private String getConfigChange(ActivityInfo activityInfo) {
        return sConfigChanges.get(activityInfo.configChanges) == null ? UNDEFINED_FIELD : sConfigChanges.get(activityInfo.configChanges);
    }

    private String getFlags(ActivityInfo activityInfo) {
        return sFlags.get(activityInfo.flags) == null ? UNDEFINED_FIELD : sFlags.get(activityInfo.flags);
    }

    private String getLaunchMode(ActivityInfo activityInfo) {
        return sLaunchMode.get(activityInfo.launchMode) == null ? UNDEFINED_FIELD : sLaunchMode.get(activityInfo.launchMode);
    }

    private String getMaxRecents(ActivityInfo activityInfo) {
        if (mCurrentSdkVersion < Build.VERSION_CODES.LOLLIPOP) {
            return NOT_SUPPORTED;
        }
        return Integer.toString(activityInfo.maxRecents);
    }

    private String getParentActivityName(ActivityInfo activityInfo) {
        if (mCurrentSdkVersion < Build.VERSION_CODES.JELLY_BEAN) {
            return NOT_SUPPORTED;
        }
        return activityInfo.parentActivityName == null ? UNDEFINED_FIELD : activityInfo.parentActivityName;
    }

    private String getPermission(ActivityInfo activityInfo) {
        return activityInfo.permission == null ? UNDEFINED_FIELD : activityInfo.permission;
    }

    private String getPersistableMode(ActivityInfo activityInfo) {
        if (mCurrentSdkVersion < Build.VERSION_CODES.LOLLIPOP) {
            return NOT_SUPPORTED;
        }
        return sPersistableMode.get(activityInfo.persistableMode) == null ? UNDEFINED_FIELD : sPersistableMode.get(activityInfo.persistableMode);
    }

    private String getScreenOrientation(ActivityInfo activityInfo) {
        return sScreenOrientation.get(activityInfo.screenOrientation) == null ? UNDEFINED_FIELD : sScreenOrientation.get(activityInfo.screenOrientation);
    }

    private String getSoftInputMode(ActivityInfo activityInfo) {
        return sSoftInputMode.get(activityInfo.softInputMode) == null ? UNDEFINED_FIELD : sSoftInputMode.get(activityInfo.softInputMode);
    }

    private String getTargetActivity(ActivityInfo activityInfo) {
        return activityInfo.targetActivity == null ? UNDEFINED_FIELD : activityInfo.targetActivity;
    }

    private String getThemeName(ActivityInfo activityInfo) {
        return activityInfo.theme == 0 ? UNDEFINED_FIELD : mContext.getResources().getResourceName(activityInfo.theme);
    }

    private String getUiOptions(ActivityInfo activityInfo) {
        return sUiOptions.get(activityInfo.uiOptions);
    }
}
