package amtt.epam.com.amtt.ticket;

import java.util.ArrayList;

import amtt.epam.com.amtt.bo.CreateIssue;
import amtt.epam.com.amtt.bo.JMetaResponse;
import amtt.epam.com.amtt.bo.JPriorityResponse;
import amtt.epam.com.amtt.bo.JProjectExtVersionsResponse;
import amtt.epam.com.amtt.bo.JUserAssignableResponse;
import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;

/**
 * Created by Iryna_Monchanka on 12.05.2015.
 */

public class JiraContent implements ContentLoadingCallback {

    private JMetaResponse mMetaResponse;
    private String mProjectKey;
    private ArrayList<String> mProjectsNames;
    private ArrayList<String> mIssueTypesNames;
    private JProjectExtVersionsResponse mProjectVersions;
    private ArrayList<String> mProjectVersionsNames;
    private JUserAssignableResponse mUsersAssignable;
    private ArrayList<String> mUsersAssignableNames;
    private JPriorityResponse mProjectPriorities;
    private ArrayList<String> mProjectPrioritiesNames;
    private JProjects mLastProject;
    private String mDeviceInfo = "***device info***";
    private String mVersionOS = "***version OS***";
    private String mActivityInfo = "***activity info***";
    private String mSteps = "***steps***";
    private String mLogs;
    private String mScreenshots;

    private static class JiraContentHolder {
        public static final JiraContent INSTANCE = new JiraContent();
    }

    public static JiraContent getInstance() {
        return JiraContentHolder.INSTANCE;
    }

    public void getPrioritiesNames(JiraGetContentCallback<ArrayList<String>> jiraGetContentCallback) {
        if (mProjectPrioritiesNames != null) {
            jiraGetContentCallback.resultOfDataLoading(mProjectPrioritiesNames, JiraContentConst.PRIORITIES_NAMES);
        } else {
            getPriorities(jiraGetContentCallback);
        }
    }

    public String getPriorityIdByName(String priorityName) {
        return mProjectPriorities.getPriorityByName(priorityName).getId();
    }

    public void getProjectsNames(JiraGetContentCallback<ArrayList<String>> jiraGetContentCallback) {
        if (mProjectsNames != null) {
            jiraGetContentCallback.resultOfDataLoading(mProjectsNames, JiraContentConst.PROJECTS_NAMES);
        } else {
            getMetaResponse(jiraGetContentCallback);
        }
    }

    public void getProjectKeyByName(String projectName, JiraGetContentCallback<String> jiraGetContentCallback) {
        mLastProject = mMetaResponse.getProjectByName(projectName);
        mProjectKey = mLastProject.getKey();
        mIssueTypesNames = null;
        mProjectVersions = null;
        mProjectVersionsNames = null;
        jiraGetContentCallback.resultOfDataLoading(mProjectKey, JiraContentConst.PROJECT_KEY_BY_NAME);
    }

    public void getIssueTypesNames(JiraGetContentCallback<ArrayList<String>> jiraGetContentCallback) {
        if (mIssueTypesNames != null) {
            jiraGetContentCallback.resultOfDataLoading(mIssueTypesNames, JiraContentConst.ISSUE_TYPES_NAMES);
        } else {
            mIssueTypesNames = mLastProject.getIssueTypesNames();
            jiraGetContentCallback.resultOfDataLoading(mIssueTypesNames, JiraContentConst.ISSUE_TYPES_NAMES);
        }
    }

    public String getIssueTypeIdByName(String issueName) {
        return mLastProject.getIssueTypeByName(issueName).getId();
    }

    public void getVersionsNames(String projectKey,
                                 JiraGetContentCallback<ArrayList<String>> jiraGetContentCallback) {
        if (mIssueTypesNames != null) {
            jiraGetContentCallback.resultOfDataLoading(mProjectVersionsNames, JiraContentConst.VERSIONS_NAMES);
        } else {
            getVersionsResponse(projectKey, jiraGetContentCallback);
        }
    }

    public String getVersionIdByName(String versionName) {
        if (versionName != null) {
            return mProjectVersions.getIssueVersionByName(versionName).getId();
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private void getMetaResponse(JiraGetContentCallback<ArrayList<String>> jiraGetContentCallback) {
        ContentFromBackend.getInstance().getMetaAsynchronously(this, jiraGetContentCallback);
    }

    @SuppressWarnings("unchecked")
    private void getVersionsResponse(String projectsKey,
                                     JiraGetContentCallback<ArrayList<String>> jiraGetContentCallback) {
        ContentFromBackend.getInstance().getVersionsAsynchronously(projectsKey, this, jiraGetContentCallback);
    }

    @SuppressWarnings("unchecked")
    public void getUsersAssignable(String userName,
                                   final JiraGetContentCallback<ArrayList<String>> jiraGetContentCallback) {
        ContentFromBackend.getInstance().getUsersAssignableAsynchronously(mLastProject.getKey(), userName, this, jiraGetContentCallback);
    }

    @SuppressWarnings("unchecked")
    private void getPriorities(final JiraGetContentCallback<ArrayList<String>> jiraGetContentCallback) {
        ContentFromBackend.getInstance().getPriorityAsynchronously(this, jiraGetContentCallback);
    }

    @SuppressWarnings("unchecked")
    public void createIssue(String issueTypeName, String priorityName, String versionName, String summary,
                            String description, String environment, String userAssigneId,
                            final JiraGetContentCallback<Boolean> jiraGetContentCallback) {
        String mProjectKey, issueTypeId, priorityId, versionId;
        mProjectKey = mLastProject.getKey();
        priorityId = getPriorityIdByName(priorityName);
        issueTypeId = getIssueTypeIdByName(issueTypeName);
        versionId = getVersionIdByName(versionName);
        String issueJson = new CreateIssue(mProjectKey, issueTypeId, description, summary, priorityId, versionId,
                environment, userAssigneId).getResultJson();
        ContentFromBackend.getInstance().createIssueAsynchronously(issueJson, this, jiraGetContentCallback);
    }

    public void getDescription(final JiraGetContentCallback<String> jiraGetContentCallback) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(mActivityInfo);
        stringBuilder.append("\n");
        stringBuilder.append(mSteps);
        String description = stringBuilder.toString();
        jiraGetContentCallback.resultOfDataLoading(description, JiraContentConst.DESCRIPTION);
    }

    public void getEnvironment(final JiraGetContentCallback<String> jiraGetContentCallback) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(mVersionOS);
        stringBuilder.append("\n");
        stringBuilder.append(mDeviceInfo);
        String environment = stringBuilder.toString();
        jiraGetContentCallback.resultOfDataLoading(environment, JiraContentConst.ENVIRONMENT);
    }

    public void getDeviceInfo(final JiraGetContentCallback<String> jiraGetContentCallback) {
        jiraGetContentCallback.resultOfDataLoading(mDeviceInfo, JiraContentConst.DEVICE_INFO);
    }

    public void getVersionOS(final JiraGetContentCallback<String> jiraGetContentCallback) {
        jiraGetContentCallback.resultOfDataLoading(mVersionOS, JiraContentConst.VERSION_OS);
    }

    public void getActivityInfo(final JiraGetContentCallback<String> jiraGetContentCallback) {
        jiraGetContentCallback.resultOfDataLoading(mActivityInfo, JiraContentConst.ACTIVITY_INFO);
    }

    public void getSteps(final JiraGetContentCallback<String> jiraGetContentCallback) {
        jiraGetContentCallback.resultOfDataLoading(mSteps, JiraContentConst.STEPS);
    }

    public void getLogs(final JiraGetContentCallback<String> jiraGetContentCallback) {
        jiraGetContentCallback.resultOfDataLoading(mLogs, JiraContentConst.LOGS);
    }

    public void getScreenshots(final JiraGetContentCallback<String> jiraGetContentCallback) {
        jiraGetContentCallback.resultOfDataLoading(mScreenshots, JiraContentConst.SCREENSHOTS);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void resultFromBackend(Object result, JiraContentConst tagResult, JiraGetContentCallback jiraGetContentCallback) {
        if (tagResult == JiraContentConst.META_RESPONSE) {
            if (result != null) {
                mMetaResponse = (JMetaResponse) result;
                mProjectsNames = new ArrayList();
                mProjectsNames = mMetaResponse.getProjectsNames();
                jiraGetContentCallback.resultOfDataLoading(mProjectsNames, JiraContentConst.PROJECTS_NAMES);
            } else {
                jiraGetContentCallback.resultOfDataLoading(null, JiraContentConst.PROJECTS_NAMES);
            }
        } else if (tagResult == JiraContentConst.VERSIONS_RESPONSE) {
            if (result != null) {
                mProjectVersions = (JProjectExtVersionsResponse) result;
                mProjectVersionsNames = new ArrayList();
                mProjectVersionsNames = mProjectVersions.getVersionsNames();
                jiraGetContentCallback.resultOfDataLoading(mProjectVersionsNames, JiraContentConst.VERSIONS_NAMES);
            } else {
                jiraGetContentCallback.resultOfDataLoading(null, JiraContentConst.VERSIONS_NAMES);
            }
        } else if (tagResult == JiraContentConst.CREATE_ISSUE_RESPONSE) {
            jiraGetContentCallback.resultOfDataLoading(result, JiraContentConst.CREATE_ISSUE);
        } else if (tagResult == JiraContentConst.PRIORITIES_RESPONSE) {
            if (result != null) {
                mProjectPriorities = (JPriorityResponse) result;
                mProjectPrioritiesNames = new ArrayList();
                mProjectPrioritiesNames = mProjectPriorities.getPriorityNames();
                jiraGetContentCallback.resultOfDataLoading(mProjectPrioritiesNames, JiraContentConst.PRIORITIES_NAMES);
            } else {
                jiraGetContentCallback.resultOfDataLoading(null, JiraContentConst.PRIORITIES_NAMES);
            }
        } else if (tagResult == JiraContentConst.USERS_ASSIGNABLE_RESPONSE) {
            if (result != null) {
                mUsersAssignable = (JUserAssignableResponse) result;
                mUsersAssignableNames = new ArrayList();
                mUsersAssignableNames = mUsersAssignable.getAssignableUsersNames();
                jiraGetContentCallback.resultOfDataLoading(mUsersAssignableNames, JiraContentConst.USERS_ASSIGNABLE_NAMES);
            } else {
                jiraGetContentCallback.resultOfDataLoading(null, JiraContentConst.USERS_ASSIGNABLE_NAMES);
            }
        }
    }

}
