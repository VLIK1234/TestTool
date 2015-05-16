package amtt.epam.com.amtt.bo.database;

import android.content.ContentValues;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;

import amtt.epam.com.amtt.contentprovider.AmttUri;
import amtt.epam.com.amtt.database.dao.DatabaseEntity;
import amtt.epam.com.amtt.database.table.ActivityInfoTable;
import amtt.epam.com.amtt.database.table.StepsTable;
import amtt.epam.com.amtt.util.ActivityMetaUtil;

/**
 * Created by Artsiom_Kaliaha on 15.05.2015.
 */
public class ActivityMeta extends DatabaseEntity {

    private int mId;
    private String mActivityName;
    private String mConfigChanges;
    private String mFlags;
    private String mLaunchMode;
    private String mMaxRecents;
    private String mParentActivityName;
    private String mPermission;
    private String mPersistableMode;
    private String mScreenOrientation;
    private String mSoftInputMode;
    private String mTargetActivityName;
    private String mTaskAffinity;
    private String mUiOptions;
    private String mProcessName;
    private String mPackageName;

    public ActivityMeta() { }

    public ActivityMeta(String activityName, String taskAffinity, String processName, String packageName, String configChanges,
                        String flags, String launchMode, String maxRecents, String parentActivityName, String permission,
                        String persistableMode, String screenOrientation, String softInputMode, String targetActivity, String uiOptions) {
        mActivityName = activityName;
        mTaskAffinity = taskAffinity;
        mProcessName = processName;
        mPackageName = packageName;
        mConfigChanges = configChanges;
        mFlags = flags;
        mLaunchMode = launchMode;
        mMaxRecents = maxRecents;
        mParentActivityName = parentActivityName;
        mPermission = permission;
        mPersistableMode = persistableMode;
        mScreenOrientation = screenOrientation;
        mSoftInputMode = softInputMode;
        mTargetActivityName = targetActivity;
        mUiOptions = uiOptions;
    }

    public ActivityMeta(Cursor cursor) {
        super(cursor);
        mId = cursor.getInt(cursor.getColumnIndex(StepsTable._ID));
        mActivityName = cursor.getString(cursor.getColumnIndex(StepsTable._ASSOCIATED_ACTIVITY));
        mConfigChanges = cursor.getString(cursor.getColumnIndex(ActivityInfoTable._CONFIG_CHANGES));
        mFlags =  cursor.getString(cursor.getColumnIndex(ActivityInfoTable._FLAGS));
        mLaunchMode = cursor.getString(cursor.getColumnIndex(ActivityInfoTable._LAUNCH_MODE));
        mMaxRecents = cursor.getString(cursor.getColumnIndex(ActivityInfoTable._MAX_RECENTS));
        mParentActivityName = cursor.getString(cursor.getColumnIndex(ActivityInfoTable._PARENT_ACTIVITY_NAME));
        mPermission = cursor.getString(cursor.getColumnIndex(ActivityInfoTable._PERMISSION));
        mPersistableMode = cursor.getString(cursor.getColumnIndex(ActivityInfoTable._PERSISTABLE_MODE));
        mScreenOrientation = cursor.getString(cursor.getColumnIndex(ActivityInfoTable._SCREEN_ORIENTATION));
        mSoftInputMode =  cursor.getString(cursor.getColumnIndex(ActivityInfoTable._SOFT_INPUT_MODE));
        mTargetActivityName = cursor.getString(cursor.getColumnIndex(ActivityInfoTable._TARGET_ACTIVITY_NAME));
        mTaskAffinity = cursor.getString(cursor.getColumnIndex(ActivityInfoTable._TASK_AFFINITY));
        mUiOptions =  cursor.getString(cursor.getColumnIndex(ActivityInfoTable._UI_OPTIONS));
        mProcessName = cursor.getString(cursor.getColumnIndex(ActivityInfoTable._PROCESS_NAME));
        mPackageName = cursor.getString(cursor.getColumnIndex(ActivityInfoTable._PACKAGE_NAME));
    }

    @Override
    public int getId() {
        return mId;
    }

    @Override
    public Uri getUri() {
        return AmttUri.ACTIVITY_META.get();
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(ActivityInfoTable._ACTIVITY_NAME, mActivityName);
        values.put(ActivityInfoTable._CONFIG_CHANGES, mConfigChanges);
        values.put(ActivityInfoTable._FLAGS, mFlags);
        values.put(ActivityInfoTable._LAUNCH_MODE, mLaunchMode);
        values.put(ActivityInfoTable._MAX_RECENTS, mMaxRecents);
        values.put(ActivityInfoTable._PARENT_ACTIVITY_NAME, mParentActivityName);
        values.put(ActivityInfoTable._PERMISSION, mPermission);
        values.put(ActivityInfoTable._PERSISTABLE_MODE, mPersistableMode);
        values.put(ActivityInfoTable._SCREEN_ORIENTATION, mScreenOrientation);
        values.put(ActivityInfoTable._SOFT_INPUT_MODE, mSoftInputMode);
        values.put(ActivityInfoTable._TARGET_ACTIVITY_NAME, mTargetActivityName);
        values.put(ActivityInfoTable._TASK_AFFINITY, mTaskAffinity);
        values.put(ActivityInfoTable._UI_OPTIONS, mUiOptions);
        values.put(ActivityInfoTable._PROCESS_NAME, mProcessName);
        values.put(ActivityInfoTable._PACKAGE_NAME, mPackageName);
        return values;
    }

    public String getAssociatedActivity() {
        return mActivityName;
    }

    public String getConfigChanges() {
        return mConfigChanges;
    }

    public String getFlags() {
        return mFlags;
    }

    public String getLaunchMode() {
        return mLaunchMode;
    }

    public String getMaxRecents() {
        return mMaxRecents;
    }

    public String getParentActivityName() {
        return mParentActivityName;
    }

    public String getPermission() {
        return mPermission;
    }

    public String getPersistableMode() {
        return mPersistableMode;
    }

    public String getScreenOrientation() {
        return mScreenOrientation;
    }

    public String getSoftInputMode() {
        return mSoftInputMode;
    }

    public String getTargetActivityName() {
        return mTargetActivityName;
    }

    public String getTaskAffinity() {
        return mTaskAffinity;
    }

    public String getUiOptions() {
        return mUiOptions;
    }

    public String getProcessName() {
        return mProcessName;
    }

    public String getPackageName() {
        return mPackageName;
    }

}
