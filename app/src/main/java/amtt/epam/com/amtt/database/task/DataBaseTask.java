package amtt.epam.com.amtt.database.task;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

import amtt.epam.com.amtt.bo.JProjectsResponse;
import amtt.epam.com.amtt.bo.JPriorityResponse;
import amtt.epam.com.amtt.bo.issue.createmeta.JIssueTypes;
import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;
import amtt.epam.com.amtt.bo.project.JPriority;
import amtt.epam.com.amtt.bo.user.JUserInfo;
import amtt.epam.com.amtt.contentprovider.AmttContentProvider;
import amtt.epam.com.amtt.database.constant.ActivityInfoConstants;
import amtt.epam.com.amtt.database.table.ActivityInfoTable;
import amtt.epam.com.amtt.database.table.IssuetypeTable;
import amtt.epam.com.amtt.database.table.PriorityTable;
import amtt.epam.com.amtt.database.table.ProjectTable;
import amtt.epam.com.amtt.database.table.StepsTable;
import amtt.epam.com.amtt.database.table.UsersTable;
import amtt.epam.com.amtt.util.IOUtils;
import amtt.epam.com.amtt.util.Logger;

/**
 * Created by Artsiom_Kaliaha on 26.03.2015.
 */
public class DataBaseTask extends AsyncTask<Void, Void, DataBaseTaskResult> implements ActivityInfoConstants {


    public static String TAG = DataBaseTask.class.getSimpleName();
    public static class Builder {

        private DataBaseOperationType mOperationType;
        private Context mContext;
        private DataBaseCallback mCallback;
        private int mStepNumber;
        private String mUrl;
        private JUserInfo mJiraUserInfo;
        private JPriority mPriority;
        private JProjects mProjects;
        private String mEmail;
        private JIssueTypes mIssueTypes;
        private String mUserKey;
        private String mProjectKey;
        private JProjectsResponse mMetaResponse;
        private JPriorityResponse mPriorityResponse;


        public Builder() {
        }

        public Builder setOperationType(DataBaseOperationType operationType) {
            mOperationType = operationType;
            return this;
        }

        public Builder setContext(@NonNull Context context) {
            Logger.d(TAG, "setContext()");
            mContext = context;

            return this;
        }

        public Builder setCallback(@NonNull DataBaseCallback callback) {
            Logger.d(TAG, "setCallback()");
            mCallback = callback;
            return this;
        }

        public Builder setStepNumber(int stepNumber) {
            mStepNumber = stepNumber;
            return this;
        }

        public Builder setUrl(String url) {
            Logger.d(TAG, "setUrl()" + url);
            mUrl = url;
            return this;
        }

        public Builder setJiraUserInfo(JUserInfo jiraUserInfo) {
            Logger.d(TAG, "setJiraUserInfo()");
            mJiraUserInfo = jiraUserInfo;
            return this;
        }

        public Builder setPriority(JPriority priority) {
            Logger.d(TAG, "setPriority()");
            mPriority = priority;
            return this;
        }

        public Builder setProjects(JProjects projects) {
            Logger.d(TAG, "setProjects()");
            mProjects = projects;
            return this;
        }

        public Builder setEmail(String email) {
            Logger.d(TAG, "setEmail()"+ email);
            mEmail = email;
            return this;
        }

        public Builder setIssueTypes(JIssueTypes issueTypes) {
            Logger.d(TAG, "setIssueTypes()");
            mIssueTypes = issueTypes;
            return this;
        }

        public Builder setUserKey(String userKey) {
            Logger.d(TAG, "setUserKey()"+ userKey);
            mUserKey = userKey;
            return this;
        }

        public Builder setProjectKey(String projectKey) {
            Logger.d(TAG, "setProjectKey()");
            mProjectKey = projectKey;
            return this;
        }

        public Builder setMetaResponse(JProjectsResponse metaResponse) {
            Logger.d(TAG, "setMetaResponse()"+ metaResponse.getProjectsKeys());
            mMetaResponse = metaResponse;
            return this;
        }

        public Builder setPriorityResponse(JPriorityResponse priorityResponse) {
            Logger.d(TAG, "setPriorityResponse()"+ priorityResponse.getPriorityNames());
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
    private JUserInfo mJiraUserInfo;
    private JPriority mPriority;
    private JProjects mProjects;
    private String mEmail;
    private JIssueTypes mIssueTypes;
    private String mUserKey;
    private String mProjectKey;
    private JProjectsResponse mMetaResponse;
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
            if (result == DataBaseTaskResult.ERROR) {
                mCallback.onDataBaseActionDone(String.valueOf(result));
            } else if (result == DataBaseTaskResult.CLEARED){
                mCallback.onDataBaseActionDone(String.valueOf(result));
            }
            else {

                mCallback.onDataBaseActionDone(String.valueOf(mOperationType));
            }
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
        Cursor cursor = mContext.getContentResolver().query(
                AmttContentProvider.ACTIVITY_META_CONTENT_URI,
                new String[]{ActivityInfoTable._ACTIVITY_NAME},
                ActivityInfoTable._ACTIVITY_NAME,
                new String[]{topActivity.getClassName()},
                null);
        int existingActivityInfo = cursor.getCount();
        cursor.close();

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
            Cursor cursor = mContext.getContentResolver().query(
                    AmttContentProvider.USER_CONTENT_URI,
                    new String[]{UsersTable._KEY},
                    UsersTable._KEY + " = ?",
                    new String[]{mUserKey},
                    null);
            int existingUserKey = cursor.getCount();
            cursor.close();
            Logger.d(TAG, String.valueOf(existingUserKey));
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
            Cursor cursor = mContext.getContentResolver().query(
                    AmttContentProvider.USER_CONTENT_URI,
                    new String[]{UsersTable._URL},
                    UsersTable._URL+" = ?",
                    new String[]{mUrl},
                    null);
            int existingUserUrl = cursor.getCount();
            cursor.close();
            if ((existingUserUrl != 0) && (mPriority != null)) {
                savePriority(mPriority, mUrl);
                Logger.d(TAG, "performPrioritySaving(ok)");
            } else {
                return DataBaseTaskResult.ERROR;
            }
            return DataBaseTaskResult.DONE;
        } else {
            return DataBaseTaskResult.ERROR;
        }
    }

    private DataBaseTaskResult performListPrioritySaving() {
        Logger.d(TAG, "performListPrioritySaving()");
        if (mUrl != null) {
            Cursor cursor = mContext.getContentResolver().query(
                    AmttContentProvider.USER_CONTENT_URI,
                    new String[]{UsersTable._URL},
                    UsersTable._URL+" = ?",
                    new String[]{mUrl},
                    null);
            int existingUserUrl = cursor.getCount();
            cursor.close();
            Logger.d(TAG, String.valueOf(existingUserUrl));
            if ((existingUserUrl > 0) && (mPriorityResponse != null)) {
                saveListPriority(mPriorityResponse, mUrl);
                Logger.d(TAG, "performListPrioritySaving(ok)");
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
        Logger.d(TAG, "performProjectSaving()");
        if (mEmail != null) {
            Cursor cursor = mContext.getContentResolver().query(
                    AmttContentProvider.USER_CONTENT_URI,
                    new String[]{UsersTable._EMAIL},
                    UsersTable._EMAIL+" = ?",
                    new String[]{mEmail},
                    null);
            int existingUserEmail = cursor.getCount();
            cursor.close();
            if ((existingUserEmail == 1) && (mProjects != null)) {
                saveProject(mProjects, mEmail);
                Logger.d(TAG, "performProjectSaving(ok)");
            } else {
                return DataBaseTaskResult.ERROR;
            }
            return DataBaseTaskResult.DONE;
        } else {
            return DataBaseTaskResult.ERROR;
        }
    }

    private DataBaseTaskResult performListProjectSaving() {
        Logger.d(TAG, "performListProjectSaving()");
        if (mEmail != null) {
            Cursor cursor = mContext.getContentResolver().query(
                    AmttContentProvider.USER_CONTENT_URI,
                    new String[]{UsersTable._EMAIL},
                    UsersTable._EMAIL+" = ?",
                    new String[]{mEmail},
                    null);
            int existingUserEmail = cursor.getCount();
            cursor.close();
            Logger.d(TAG, String.valueOf(existingUserEmail));
            if ((existingUserEmail > 0) && (mMetaResponse.getProjects() != null)) {
                saveListProject(mMetaResponse, mEmail);
                Logger.d(TAG, "performListProjectSaving(ok)");
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
            Cursor cursor = mContext.getContentResolver().query(
                    AmttContentProvider.PROJECT_CONTENT_URI,
                    new String[]{ProjectTable._KEY},
                    ProjectTable._KEY+" = ?",
                    new String[]{mProjectKey},
                    null);
            int existingProjectKey = cursor.getCount();
            cursor.close();
            if ((existingProjectKey >0) && (mIssueTypes != null)) {
                Logger.d(TAG, "performIssuetypeSaving(ok)");
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
       // if (mProjects.getKey() != null) {
            Logger.d(TAG, "performListIssuetypesSaving()");
            Cursor cursor = mContext.getContentResolver().query(
                    AmttContentProvider.PROJECT_CONTENT_URI,
                    new String[]{ProjectTable._KEY},
                    ProjectTable._KEY+" = ?",
                    new String[]{mProjects.getKey()},
                    null);
            int existingProjectKey = cursor.getCount();
            cursor.close();

            Logger.d(TAG, String.valueOf(existingProjectKey));
           // if (existingProjectKey >0) {
                saveListIssuetype(mProjects);
                Logger.d(TAG, "performListIssuetypesSaving(ok)");
            //} else {
            //    return DataBaseTaskResult.ERROR;
            //}
            return DataBaseTaskResult.DONE;
      //  } else {
      //      return DataBaseTaskResult.ERROR;
       // }
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

    public void saveUser(JUserInfo jiraUserInfo, String url) {
        Logger.d(TAG, "saveUser(JiraUserInfo jiraUserInfo, String url)");
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
        Logger.d(TAG, "saveUser(ok)");
    }

    private void savePriority(JPriority jPriority, String url) {
        Logger.d(TAG, "savePriority(JPriority jPriority, String url)");
        ContentValues values = new ContentValues();
        values.put(PriorityTable._JIRA_ID, jPriority.getId());
        values.put(PriorityTable._NAME, jPriority.getId());
        values.put(PriorityTable._URL, url);
        mContext.getContentResolver().insert(AmttContentProvider.PRIORITY_CONTENT_URI, values);
        Logger.d(TAG, "savePriority(ok)");
    }

    private void saveListPriority(JPriorityResponse priorityResponce, String url) {
        Logger.d(TAG, "saveListPriority(JPriorityResponse priorityResponce, String url)");
        ContentValues[] bulkToInsert;
        List<ContentValues> mValueList = new ArrayList<ContentValues>();
        for (int i = 0; i < priorityResponce.getPriorities().size(); i++) {
            ContentValues mNewValues = new ContentValues();
            mNewValues.put(PriorityTable._JIRA_ID, priorityResponce.getPriorities().get(i).getId());
            mNewValues.put(PriorityTable._NAME, priorityResponce.getPriorities().get(i).getId());
            mNewValues.put(PriorityTable._URL, url);
            mValueList.add(mNewValues);
        }
        bulkToInsert = new ContentValues[mValueList.size()];
        mValueList.toArray(bulkToInsert);
        mContext.getContentResolver().bulkInsert(AmttContentProvider.PRIORITY_CONTENT_URI, bulkToInsert);
        Logger.d(TAG, "saveListPriority(ok)");
    }

    private void saveProject(JProjects projects, String email) {
        Logger.d(TAG, "saveProject(JProjects projects, String email)");
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
        Logger.d(TAG, "saveProject(ok)");
    }

    private void saveListProject(JProjectsResponse metaResponse, String email) {
        Logger.d(TAG, "saveListProject(JMetaResponse metaResponse, String email)");
        ContentValues[] bulkToInsertProject;
        List<ContentValues> mValueListProject = new ArrayList<ContentValues>();
        for (int i = 0; i < metaResponse.getProjects().size(); i++) {


            Logger.d(TAG, "saveListIssuetype(JProjects projects)");
            ContentValues[] bulkToInsert;
            List<ContentValues> mValueList = new ArrayList<ContentValues>();
            for (int j = 0; j < metaResponse.getProjects().get(i).getIssueTypes().size(); j++) {
                ContentValues mNewValues = new ContentValues();
                mNewValues.put(ProjectTable._KEY, metaResponse.getProjects().get(i).getKey());
                mNewValues.put(IssuetypeTable._NAME, metaResponse.getProjects().get(i).getIssueTypes().get(j).getName());
                mValueList.add(mNewValues);
            }
            bulkToInsert = new ContentValues[mValueList.size()];
            mValueList.toArray(bulkToInsert);
            mContext.getContentResolver().bulkInsert(AmttContentProvider.ISSUETYPE_CONTENT_URI, bulkToInsert);
            Logger.d(TAG, "saveListIssuetype(ok)");

            ContentValues mNewValuesProject = new ContentValues();
            mNewValuesProject.put(ProjectTable._AVATAR_MEDIUM_URL, metaResponse.getProjects().get(i).getAvatarUrls().getAvatarMediumUrl());
            mNewValuesProject.put(ProjectTable._AVATAR_SMALL_URL, metaResponse.getProjects().get(i).getAvatarUrls().getAvatarSmallUrl());
            mNewValuesProject.put(ProjectTable._AVATAR_URL, metaResponse.getProjects().get(i).getAvatarUrls().getAvatarUrl());
            mNewValuesProject.put(ProjectTable._AVATAR_X_SMALL_URL, metaResponse.getProjects().get(i).getAvatarUrls().getAvatarXSmallUrl());
            mNewValuesProject.put(UsersTable._EMAIL, email);
            mNewValuesProject.put(ProjectTable._JIRA_ID, metaResponse.getProjects().get(i).getId());
            mNewValuesProject.put(ProjectTable._KEY, metaResponse.getProjects().get(i).getKey());
            mNewValuesProject.put(ProjectTable._NAME, metaResponse.getProjects().get(i).getName());
            mValueListProject.add(mNewValuesProject);
        }
        bulkToInsertProject = new ContentValues[mValueListProject.size()];
        mValueListProject.toArray(bulkToInsertProject);
        mContext.getContentResolver().bulkInsert(AmttContentProvider.PROJECT_CONTENT_URI, bulkToInsertProject);
        Logger.d(TAG, "saveListProject(ok)");
    }

    private void saveIssuetype(JIssueTypes issueTypes, String key) {
        Logger.d(TAG, "saveIssuetype(JIssueTypes issueTypes, String key)");
        ContentValues values = new ContentValues();
        values.put(ProjectTable._KEY, key);
        values.put(IssuetypeTable._NAME, issueTypes.getName());
        mContext.getContentResolver().insert(AmttContentProvider.ISSUETYPE_CONTENT_URI, values);
        Logger.d(TAG, "saveIssuetype(ok)");
    }

    private void saveListIssuetype(JProjects projects) {
        Logger.d(TAG, "saveListIssuetype(JProjects projects)");
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
        Logger.d(TAG, "saveListIssuetype(ok)");
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
