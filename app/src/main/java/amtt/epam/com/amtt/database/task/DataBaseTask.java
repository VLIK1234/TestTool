package amtt.epam.com.amtt.database.task;

import amtt.epam.com.amtt.bo.JMetaResponse;
import amtt.epam.com.amtt.bo.JPriorityResponse;
import amtt.epam.com.amtt.bo.issue.createmeta.JIssueTypes;
import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;
import amtt.epam.com.amtt.bo.project.JPriority;
import amtt.epam.com.amtt.bo.user.JiraUserInfo;
import amtt.epam.com.amtt.contentprovider.AmttContentProvider;
import amtt.epam.com.amtt.database.constant.ActivityInfoConstants;
import amtt.epam.com.amtt.database.table.*;
import amtt.epam.com.amtt.util.IOUtils;
import amtt.epam.com.amtt.util.Logger;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Artsiom_Kaliaha on 26.03.2015.
 */
public class DataBaseTask extends AsyncTask<Void, Void, DataBaseTaskResult> implements ActivityInfoConstants {


    String TAG = DataBaseTask.class.getSimpleName();
    public static class Builder {

        private DataBaseOperationType mOperationType;
        private Context mContext;
        private DataBaseCallback mCallback;
        private int mStepNumber;
        private String mUrl;
        private JiraUserInfo mJiraUserInfo;
        private JPriority mPriority;
        private JProjects mProjects;
        private String mEmail;
        private JIssueTypes mIssueTypes;
        private String mUserKey;
        private String mProjectKey;
        private JMetaResponse mMetaResponse;
        private JPriorityResponse mPriorityResponse;


        public Builder() {
        }

        public Builder setOperationType(DataBaseOperationType operationType) {
            mOperationType = operationType;
            return this;
        }

        public Builder setContext(@NonNull Context context) {
            Logger.d(DataBaseTask.class.getSimpleName(), "setContext()");
            mContext = context;

            return this;
        }

        public Builder setCallback(@NonNull DataBaseCallback callback) {
            Logger.d(DataBaseTask.class.getSimpleName(), "setCallback()");
            mCallback = callback;
            return this;
        }

        public Builder setStepNumber(int stepNumber) {
            mStepNumber = stepNumber;
            return this;
        }

        public Builder setUrl(String url) {
            Logger.d(DataBaseTask.class.getSimpleName(), "setUrl()");
            mUrl = url;
            return this;
        }

        public Builder setJiraUserInfo(JiraUserInfo jiraUserInfo) {
            Logger.d(DataBaseTask.class.getSimpleName(), "setJiraUserInfo()");
            mJiraUserInfo = jiraUserInfo;
            return this;
        }

        public Builder setPriority(JPriority priority) {
            mPriority = priority;
            return this;
        }

        public Builder setProjects(JProjects projects) {
            mProjects = projects;
            return this;
        }

        public Builder setEmail(String email) {
            mEmail = email;
            return this;
        }

        public Builder setIssueTypes(JIssueTypes issueTypes) {
            mIssueTypes = issueTypes;
            return this;
        }

        public Builder setUserKey(String userKey) {
            Logger.d(DataBaseTask.class.getSimpleName(), "setUserKey()");
            mUserKey = userKey;
            return this;
        }

        public Builder setProjectKey(String projectKey) {
            mProjectKey = projectKey;
            return this;
        }

        public Builder setMetaResponse(JMetaResponse metaResponse) {
            mMetaResponse = metaResponse;
            return this;
        }

        public Builder setPriorityResponse(JPriorityResponse priorityResponse) {
            mPriorityResponse = priorityResponse;
            return this;
        }

        public DataBaseTask create() {
            DataBaseTask dataBaseTask = new DataBaseTask();
            dataBaseTask.mOperationType = this.mOperationType;
            dataBaseTask.mContext = this.mContext;
            dataBaseTask.mCallback = this.mCallback;
            dataBaseTask.mStepNumber = this.mStepNumber;
            dataBaseTask.mPath = mContext.getFilesDir().getPath() + SCREENSHOT_FOLDER;
            dataBaseTask.mCurrentSdkVersion = android.os.Build.VERSION.SDK_INT;
            dataBaseTask.mUrl = this.mUrl;
            dataBaseTask.mJiraUserInfo = this.mJiraUserInfo;
            dataBaseTask.mPriority = this.mPriority;
            dataBaseTask.mProjects = this.mProjects;
            dataBaseTask.mEmail = this.mEmail;
            dataBaseTask.mIssueTypes = this.mIssueTypes;
            dataBaseTask.mUserKey = this.mUserKey;
            dataBaseTask.mProjectKey = this.mProjectKey;
            dataBaseTask.mMetaResponse = this.mMetaResponse;
            dataBaseTask.mPriorityResponse = this.mPriorityResponse;
            return dataBaseTask;
        }

    }

    private static final String SCREENSHOT_COMMAND = "/system/bin/screencap -p ";
    private static final String CHANGE_PERMISSION_COMMAND = "chmod 777 ";
    private static final String SCREENSHOT_FOLDER = "/screenshot";
    private static Map<Integer, String> sConfigChanges;
    private static Map<Integer, String> sFlags;
    private static Map<Integer, String> sLaunchMode;
    private static Map<Integer, String> sPersistableMode;
    private static Map<Integer, String> sScreenOrientation;
    private static Map<Integer, String> sSoftInputMode;
    private static Map<Integer, String> sUiOptions;
    private int mStepNumber;

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

    private DataBaseOperationType mOperationType;
    private Context mContext;
    private DataBaseCallback mCallback;
    private String mPath;
    private int mCurrentSdkVersion;
    private String mUrl;
    private JiraUserInfo mJiraUserInfo;
    private JPriority mPriority;
    private JProjects mProjects;
    private String mEmail;
    private JIssueTypes mIssueTypes;
    private String mUserKey;
    private String mProjectKey;
    private JMetaResponse mMetaResponse;
    private JPriorityResponse mPriorityResponse;

    @Override
    protected DataBaseTaskResult doInBackground(Void... params) {
        switch (mOperationType) {
            case SAVE_STEP:
                return performStepSaving();
            case CLEAR:
                return performCleaning();
            case SAVE_USER:
                return performUserSaving();
            case SAVE_PRIORITY:
                return performPrioritySaving();
            case SAVE_PROJECT:
                return performProjectSaving();
            case SAVE_ISSUETYPE:
                return performIssuetypeSaving();
            case SAVE_LIST_PROJECT:
                return performListProjectSaving();
            case SAVE_LIST_ISSUETYPE:
                return performListIssuetypesSaving();
            case SAVE_LIST_PRIORITY:
                return performListPrioritySaving();
            default:
                return null;
        }
    }

    @Override
    protected void onPostExecute(DataBaseTaskResult result) {
        if (mCallback != null) {
            mCallback.onDataBaseActionDone(result);
        }
    }


    private DataBaseTaskResult performStepSaving() {
        String screenPath;
        try {
            screenPath = saveScreen();
        } catch (Exception e) {
            return DataBaseTaskResult.ERROR;
        }

        ComponentName topActivity = getTopActivity();

        int existingActivityInfo = mContext.getContentResolver().query(
                AmttContentProvider.ACTIVITY_META_CONTENT_URI,
                new String[]{ActivityInfoTable._ACTIVITY_NAME},
                ActivityInfoTable._ACTIVITY_NAME,
                new String[]{topActivity.getClassName()},
                null).getCount();

        //if there is no records about current app in db
        if (existingActivityInfo == 0) {
            ActivityInfo activityInfo;
            try {
                activityInfo = mContext
                        .getPackageManager()
                        .getActivityInfo(topActivity, PackageManager.GET_META_DATA & PackageManager.GET_INTENT_FILTERS);
            } catch (PackageManager.NameNotFoundException e) {
                return DataBaseTaskResult.ERROR;
            }

            saveActivityInfo(activityInfo);
        }

        saveStep(screenPath, topActivity);

        return DataBaseTaskResult.DONE;
    }

    private DataBaseTaskResult performCleaning() {
        mContext.getContentResolver().delete(AmttContentProvider.ACTIVITY_META_CONTENT_URI, null, null);
        mContext.getContentResolver().delete(AmttContentProvider.STEP_CONTENT_URI, null, null);

        File screenshotDirectory = new File(mPath);
        if (!screenshotDirectory.exists()) {
            screenshotDirectory.mkdir();
        } else {
            for (File screenshot : screenshotDirectory.listFiles()) {
                screenshot.delete();
            }
        }
        return DataBaseTaskResult.CLEARED;
    }

    private DataBaseTaskResult performUserSaving() {
        Logger.d(TAG, "performUserSaving()");
        if (mUserKey != null) {
            Logger.d(TAG, "getCount()");
            /*int existingUserKey = mContext.getContentResolver().query(
                AmttContentProvider.USER_CONTENT_URI,
                new String[]{UsersTable._KEY},
                UsersTable._KEY,
                new String[]{mUserKey},
                null).getCount();
            Logger.d(TAG, String.valueOf(existingUserKey));*/
            if ((mJiraUserInfo != null) && (mUrl != null)) {
                Logger.d(TAG, "saveUser()");
                saveUser(mJiraUserInfo, mUrl);
            } else {
                Logger.d(TAG, "saveUser(not)");
                return DataBaseTaskResult.ERROR;
            }
            return DataBaseTaskResult.DONE;
        } else {
            Logger.d(TAG, "performUserSaving(not)");
            return DataBaseTaskResult.ERROR;
        }
    }

    //TODO test existing priorities in PriorityTable
    private DataBaseTaskResult performPrioritySaving() {
        if (mUrl != null) {
            int existingUserUrl = mContext.getContentResolver().query(
                AmttContentProvider.USER_CONTENT_URI,
                new String[]{UsersTable._URL},
                UsersTable._URL,
                new String[]{mUrl},
                null).getCount();
            if ((existingUserUrl != 0) && (mPriority != null)) {
                savePriority(mPriority, mUrl);
            } else {
                return DataBaseTaskResult.ERROR;
            }
            return DataBaseTaskResult.DONE;
        } else {
            return DataBaseTaskResult.ERROR;
        }
    }

    private DataBaseTaskResult performListPrioritySaving() {
        if (mUrl != null) {
            int existingUserUrl = mContext.getContentResolver().query(
                AmttContentProvider.USER_CONTENT_URI,
                new String[]{UsersTable._URL},
                UsersTable._URL,
                new String[]{mUrl},
                null).getCount();
            if ((existingUserUrl != 0) && (mPriority != null)) {
                saveListPriority(mPriorityResponse, mUrl);
            } else {
                return DataBaseTaskResult.ERROR;
            }
            return DataBaseTaskResult.DONE;
        } else {
            return DataBaseTaskResult.ERROR;
        }
    }

    //TODO test existing projects in ProjectTable
    private DataBaseTaskResult performProjectSaving() {
        if (mEmail != null) {
            int existingUserEmail = mContext.getContentResolver().query(
                AmttContentProvider.USER_CONTENT_URI,
                new String[]{UsersTable._EMAIL},
                UsersTable._EMAIL,
                new String[]{mEmail},
                null).getCount();
            if ((existingUserEmail == 1) && (mProjects != null)) {
                saveProject(mProjects, mEmail);
            } else {
                return DataBaseTaskResult.ERROR;
            }
            return DataBaseTaskResult.DONE;
        } else {
            return DataBaseTaskResult.ERROR;
        }
    }

    private DataBaseTaskResult performListProjectSaving() {
        if (mEmail != null) {
            int existingUserEmail = mContext.getContentResolver().query(
                AmttContentProvider.USER_CONTENT_URI,
                new String[]{UsersTable._EMAIL},
                UsersTable._EMAIL,
                new String[]{mEmail},
                null).getCount();
            if ((existingUserEmail == 1) && (mProjects != null)) {
                saveListProject(mMetaResponse, mEmail);
            } else {
                return DataBaseTaskResult.ERROR;
            }
            return DataBaseTaskResult.DONE;
        } else {
            return DataBaseTaskResult.ERROR;
        }
    }

    private DataBaseTaskResult performIssuetypeSaving() {
        if (mProjectKey != null) {
            int existingProjectKey = mContext.getContentResolver().query(
                AmttContentProvider.PROJECT_CONTENT_URI,
                new String[]{ProjectTable._KEY},
                ProjectTable._KEY,
                new String[]{mProjectKey},
                null).getCount();

            if ((existingProjectKey == 1) && (mIssueTypes != null)) {
                saveIssuetype(mIssueTypes, mProjectKey);
            } else {
                return DataBaseTaskResult.ERROR;
            }
            return DataBaseTaskResult.DONE;
        } else {
            return DataBaseTaskResult.ERROR;
        }
    }

    private DataBaseTaskResult performListIssuetypesSaving() {
        if (mProjects.getKey() != null) {
            int existingProjectKey = mContext.getContentResolver().query(
                AmttContentProvider.PROJECT_CONTENT_URI,
                new String[]{ProjectTable._KEY},
                ProjectTable._KEY,
                new String[]{mProjects.getKey()},
                null).getCount();
            if (existingProjectKey == 1) {
                saveListIssuetype(mProjects);
            } else {
                return DataBaseTaskResult.ERROR;
            }
            return DataBaseTaskResult.DONE;
        } else {
            return DataBaseTaskResult.ERROR;
        }
    }

    private String saveScreen() throws IOException {
        String screenPath = null;
        Process fileSavingProcess = null;
        Process changeModeProcess = null;
        OutputStream fileSavingStream = null;
        OutputStream changeModeStream = null;
        try {
            fileSavingProcess = Runtime.getRuntime().exec("su");
            fileSavingStream = fileSavingProcess.getOutputStream();
            fileSavingStream.write((SCREENSHOT_COMMAND + (screenPath = mPath + "/screen" + mStepNumber + ".png")).getBytes("ASCII"));
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
            destroyProcesses(fileSavingProcess, changeModeProcess);
        }
        return screenPath;
    }

    private ComponentName getTopActivity() {
        ActivityManager activityManager = (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);
        ActivityManager.RunningTaskInfo lastActivity = tasks.get(0);
        return lastActivity.topActivity;
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
        contentValues.put(ActivityInfoTable._UI_OPTIONS, getUiOptions(activityInfo));
        contentValues.put(ActivityInfoTable._PROCESS_NAME, activityInfo.processName);
        contentValues.put(ActivityInfoTable._PACKAGE_NAME, activityInfo.packageName);

        mContext.getContentResolver().insert(AmttContentProvider.ACTIVITY_META_CONTENT_URI, contentValues);
    }

    private void saveStep(String screenPath, ComponentName componentName) {
        ContentValues values = new ContentValues();
        values.put(StepsTable._ID, mStepNumber);
        values.put(StepsTable._SCREEN_PATH, screenPath);
        values.put(StepsTable._ASSOCIATED_ACTIVITY, componentName.getClassName());
        mContext.getContentResolver().insert(AmttContentProvider.STEP_CONTENT_URI, values);
    }

    public void saveUser(JiraUserInfo jiraUserInfo, String url) {
        ContentValues values = new ContentValues();
        values.put(UsersTable._AVATAR_MEDIUM_URL, jiraUserInfo.getAvatarUrls().getAvatarMediumUrl());
        values.put(UsersTable._AVATAR_SMALL_URL, jiraUserInfo.getAvatarUrls().getAvatarSmallUrl());
        values.put(UsersTable._AVATAR_URL, jiraUserInfo.getAvatarUrls().getAvatarUrl());
        values.put(UsersTable._AVATAR_X_SMALL_URL, jiraUserInfo.getAvatarUrls().getAvatarXSmallUrl());
        values.put(UsersTable._EMAIL, jiraUserInfo.getEmailAddress());
        values.put(UsersTable._KEY, jiraUserInfo.getKey());
        values.put(UsersTable._URL, url);
        values.put(UsersTable._USER_NAME, jiraUserInfo.getName());
        mContext.getContentResolver().insert(AmttContentProvider.USER_CONTENT_URI, values);
    }

    private void savePriority(JPriority jPriority, String url) {
        ContentValues values = new ContentValues();
        values.put(PriorityTable._JIRA_ID, jPriority.getId());
        values.put(PriorityTable._NAME, jPriority.getId());
        values.put(UsersTable._URL, url);
        mContext.getContentResolver().insert(AmttContentProvider.PRIORITY_CONTENT_URI, values);
    }

    private void saveListPriority(JPriorityResponse priorityResponce, String url) {
        ContentValues[] bulkToInsert;
        List<ContentValues> mValueList = new ArrayList<ContentValues>();
        for (int i = 0; i < priorityResponce.getPriorities().size(); i++) {
            ContentValues mNewValues = new ContentValues();
            mNewValues.put(PriorityTable._JIRA_ID, priorityResponce.getPriorities().get(i).getId());
            mNewValues.put(PriorityTable._NAME, priorityResponce.getPriorities().get(i).getId());
            mNewValues.put(UsersTable._URL, url);
            mValueList.add(mNewValues);
        }
        bulkToInsert = new ContentValues[mValueList.size()];
        mValueList.toArray(bulkToInsert);
        mContext.getContentResolver().bulkInsert(AmttContentProvider.PRIORITY_CONTENT_URI, bulkToInsert);
    }

    private void saveProject(JProjects projects, String email) {
        ContentValues values = new ContentValues();
        values.put(ProjectTable._AVATAR_MEDIUM_URL, projects.getAvatarUrls().getAvatarMediumUrl());
        values.put(ProjectTable._AVATAR_SMALL_URL, projects.getAvatarUrls().getAvatarSmallUrl());
        values.put(ProjectTable._AVATAR_URL, projects.getAvatarUrls().getAvatarUrl());
        values.put(ProjectTable._AVATAR_X_SMALL_URL, projects.getAvatarUrls().getAvatarXSmallUrl());
        values.put(UsersTable._EMAIL, email);
        values.put(ProjectTable._JIRA_ID, projects.getId());
        values.put(ProjectTable._KEY, projects.getKey());
        values.put(ProjectTable._NAME, projects.getName());
        mContext.getContentResolver().insert(AmttContentProvider.PROJECT_CONTENT_URI, values);
    }

    private void saveListProject(JMetaResponse metaResponse, String email) {
        ContentValues[] bulkToInsert;
        List<ContentValues> mValueList = new ArrayList<ContentValues>();
        for (int i = 0; i < metaResponse.getProjects().size(); i++) {
            ContentValues mNewValues = new ContentValues();
            mNewValues.put(ProjectTable._AVATAR_MEDIUM_URL, metaResponse.getProjects().get(i).getAvatarUrls().getAvatarMediumUrl());
            mNewValues.put(ProjectTable._AVATAR_SMALL_URL, metaResponse.getProjects().get(i).getAvatarUrls().getAvatarSmallUrl());
            mNewValues.put(ProjectTable._AVATAR_URL, metaResponse.getProjects().get(i).getAvatarUrls().getAvatarUrl());
            mNewValues.put(ProjectTable._AVATAR_X_SMALL_URL, metaResponse.getProjects().get(i).getAvatarUrls().getAvatarXSmallUrl());
            mNewValues.put(UsersTable._EMAIL, email);
            mNewValues.put(ProjectTable._JIRA_ID, metaResponse.getProjects().get(i).getId());
            mNewValues.put(ProjectTable._KEY, metaResponse.getProjects().get(i).getKey());
            mNewValues.put(ProjectTable._NAME, metaResponse.getProjects().get(i).getName());
            mValueList.add(mNewValues);
        }
        bulkToInsert = new ContentValues[mValueList.size()];
        mValueList.toArray(bulkToInsert);
        mContext.getContentResolver().bulkInsert(AmttContentProvider.PROJECT_CONTENT_URI, bulkToInsert);
    }

    private void saveIssuetype(JIssueTypes issueTypes, String key) {
        ContentValues values = new ContentValues();
        values.put(ProjectTable._KEY, key);
        values.put(IssuetypeTable._NAME, issueTypes.getName());
        mContext.getContentResolver().insert(AmttContentProvider.ISSUETYPE_CONTENT_URI, values);
    }

    private void saveListIssuetype(JProjects projects) {
        ContentValues[] bulkToInsert;
        List<ContentValues> mValueList = new ArrayList<ContentValues>();
        for (int i = 0; i < projects.getIssueTypes().size(); i++) {
            ContentValues mNewValues = new ContentValues();
            mNewValues.put(ProjectTable._KEY, projects.getKey());
            mNewValues.put(IssuetypeTable._NAME, projects.getIssueTypes().get(i).getName());
            mValueList.add(mNewValues);
        }
        bulkToInsert = new ContentValues[mValueList.size()];
        mValueList.toArray(bulkToInsert);
        mContext.getContentResolver().bulkInsert(AmttContentProvider.ISSUETYPE_CONTENT_URI, bulkToInsert);
    }

    //help methods
    private void destroyProcesses(@NonNull Process... processArray) {
        for (Process process : processArray) {
            if (process != null) {
                process.destroy();
            }
        }
    }


    //ActivityInfoTable methods
    private String getConfigChange(ActivityInfo activityInfo) {
        return sConfigChanges.get(activityInfo.configChanges) == null ? NOT_AVAILABLE : sConfigChanges.get(activityInfo.configChanges);
    }

    private String getFlags(ActivityInfo activityInfo) {
        return sFlags.get(activityInfo.flags) == null ? NOT_AVAILABLE : sFlags.get(activityInfo.flags);
    }

    private String getLaunchMode(ActivityInfo activityInfo) {
        return sLaunchMode.get(activityInfo.launchMode) == null ? NOT_AVAILABLE : sLaunchMode.get(activityInfo.launchMode);
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
        return activityInfo.parentActivityName == null ? NOT_AVAILABLE : activityInfo.parentActivityName;
    }

    private String getPermission(ActivityInfo activityInfo) {
        return activityInfo.permission == null ? NOT_AVAILABLE : activityInfo.permission;
    }

    private String getPersistableMode(ActivityInfo activityInfo) {
        if (mCurrentSdkVersion < Build.VERSION_CODES.LOLLIPOP) {
            return NOT_SUPPORTED;
        }
        return sPersistableMode.get(activityInfo.persistableMode) == null ? NOT_AVAILABLE : sPersistableMode.get(activityInfo.persistableMode);
    }

    private String getScreenOrientation(ActivityInfo activityInfo) {
        return sScreenOrientation.get(activityInfo.screenOrientation) == null ? NOT_AVAILABLE : sScreenOrientation.get(activityInfo.screenOrientation);
    }

    private String getSoftInputMode(ActivityInfo activityInfo) {
        return sSoftInputMode.get(activityInfo.softInputMode) == null ? NOT_AVAILABLE : sSoftInputMode.get(activityInfo.softInputMode);
    }

    private String getTargetActivity(ActivityInfo activityInfo) {
        return activityInfo.targetActivity == null ? NOT_AVAILABLE : activityInfo.targetActivity;
    }

    private String getUiOptions(ActivityInfo activityInfo) {
        return sUiOptions.get(activityInfo.uiOptions);
    }

}
