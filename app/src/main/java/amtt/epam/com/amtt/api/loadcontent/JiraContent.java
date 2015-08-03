package amtt.epam.com.amtt.api.loadcontent;

import android.text.Spanned;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import amtt.epam.com.amtt.api.ContentConst;
import amtt.epam.com.amtt.api.ContentLoadingCallback;
import amtt.epam.com.amtt.api.GetContentCallback;
import amtt.epam.com.amtt.bo.JComponentsResponse;
import amtt.epam.com.amtt.bo.JCreateIssue;
import amtt.epam.com.amtt.bo.JCreateIssueResponse;
import amtt.epam.com.amtt.bo.JPriorityResponse;
import amtt.epam.com.amtt.bo.JProjectsResponse;
import amtt.epam.com.amtt.bo.JUserAssignableResponse;
import amtt.epam.com.amtt.bo.JVersionsResponse;
import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.bo.issue.createmeta.JIssueTypes;
import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;
import amtt.epam.com.amtt.bo.project.JPriority;
import amtt.epam.com.amtt.bo.user.JUserInfo;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.database.object.DbObjectManager;
import amtt.epam.com.amtt.database.object.IResult;
import amtt.epam.com.amtt.database.util.StepUtil;
import amtt.epam.com.amtt.googleapi.api.loadcontent.GSpreadsheetContent;
import amtt.epam.com.amtt.util.ActiveUser;
import amtt.epam.com.amtt.util.Logger;

/**
 @author Iryna Monchanka
 @version on 12.05.2015
 */

public class JiraContent{

    //region Variables
    private final String TAG = this.getClass().getSimpleName();
    private List<String> mIssueTypesNames;
    private List<String> mUsersAssignableNames;
    private HashMap<JProjects, String> mProjectsNames;
    private HashMap<String, String> mProjectPrioritiesNames;
    private HashMap<String, String> mProjectVersionsNames;
    private JProjects mLastProject;
    private String mRecentIssueKey;
    private JPriorityResponse mPriorityResponse;
    private HashMap<String, String> mProjectComponentsNames;
    //endregion

    private static class JiraContentHolder {
        public static final JiraContent INSTANCE = new JiraContent();
    }

    public static JiraContent getInstance() {
        return JiraContentHolder.INSTANCE;
    }

    //region Priorities
    public void getPrioritiesNames(final GetContentCallback<HashMap<String, String>> getContentCallback) {
        if (mProjectPrioritiesNames != null) {
            getContentCallback.resultOfDataLoading(mProjectPrioritiesNames);
        } else {
            getPrioritiesSynchronously(getContentCallback);
        }
    }

    public void setPrioritiesNames(HashMap<String, String> prioritiesNames) {
        this.mProjectPrioritiesNames = prioritiesNames;
    }

    public String getPriorityIdByName(String priorityName) {
        String priorityId = null;
        for (Map.Entry<String, String> entry : mProjectPrioritiesNames.entrySet()) {
            if (priorityName.equals(entry.getValue())) {
                priorityId = entry.getKey();
            }
        }
        return priorityId;
    }

    public String getPriorityNameById(String priorityId) {
        String priorityName = null;
        for (Map.Entry<String, String> entry : mProjectPrioritiesNames.entrySet()) {
            if (priorityId.equals(entry.getKey())) {
                priorityName = entry.getValue();
            }
        }
        return priorityName;
    }

    private void getPriorities(final GetContentCallback<HashMap<String, String>> getContentCallback) {
        ContentFromBackend.getInstance().getPriorityAsynchronously(new ContentLoadingCallback<JPriorityResponse>() {
            @Override
            public void resultFromBackend(JPriorityResponse result, ContentConst tag, GetContentCallback getContentCallback) {
                if (tag == ContentConst.PRIORITIES_RESPONSE) {
                    if (getContentCallback != null) {
                        if (result != null) {
                            mPriorityResponse = result;
                            JiraContent.getInstance().setPrioritiesNames(mPriorityResponse.getPriorityNames());
                            for (int i = 0; i < mPriorityResponse.getPriorities().size(); i++) {
                                mPriorityResponse.getPriorities().get(i).setUrl(ActiveUser.getInstance().getUrl());
                            }
                            ContentFromDatabase.setPriorities(mPriorityResponse, new IResult<Integer>() {
                                @Override
                                public void onResult(Integer result) {
                                    Logger.d(TAG, "Priority " + String.valueOf(result));
                                }

                                @Override
                                public void onError(Exception e) {
                                    Logger.e(TAG, "Priority " + e.getMessage(), e);
                                }
                            });
                            getContentCallback.resultOfDataLoading(mProjectPrioritiesNames);
                        } else {
                            getContentCallback.resultOfDataLoading(null);
                        }
                    }
                }
            }
        }, getContentCallback);
    }

    private void getPrioritiesSynchronously(final GetContentCallback<HashMap<String, String>> getContentCallback) {
        ContentFromDatabase.getPriorities(ActiveUser.getInstance().getUrl(), new IResult<List<JPriority>>() {
            @Override
            public void onResult(List<JPriority> result) {
                if (result.isEmpty()) {
                    getPriorities(getContentCallback);
                } else {
                    mPriorityResponse = new JPriorityResponse();
                    mPriorityResponse.setPriorities(new ArrayList<>(result));
                    mProjectPrioritiesNames = mPriorityResponse.getPriorityNames();
                    getContentCallback.resultOfDataLoading(mProjectPrioritiesNames);
                }
            }

            @Override
            public void onError(Exception e) {
                getPriorities(getContentCallback);
                Logger.e(TAG, e.getMessage(), e);
            }
        });
    }
    //endregion

    //region Projects
    public void getProjectsNames(final GetContentCallback<HashMap<JProjects, String>> getContentCallback) {
        if (mProjectsNames != null) {
            getContentCallback.resultOfDataLoading(mProjectsNames);
        } else {
            getProjectsSynchronously(getContentCallback);
        }
    }

    public void setProjectsNames(HashMap<JProjects, String> projectsNames) {
        this.mProjectsNames = projectsNames;
    }

    public void getProjectKeyByName(String projectName, GetContentCallback<String> getContentCallback) {
        for (Map.Entry<JProjects, String> entry : mProjectsNames.entrySet()) {
            if (projectName.equals(entry.getValue())) {
                mLastProject = entry.getKey();
            }
        }
        mIssueTypesNames = null;
        mProjectVersionsNames = null;
        mProjectComponentsNames = null;
        String mProjectKey = mLastProject.getKey();
        ActiveUser.getInstance().setLastProjectKey(mProjectKey);
        getContentCallback.resultOfDataLoading(mProjectKey);
    }

    public void getProjectNameByKey(String projectKey, final GetContentCallback<String> getContentCallback) {
        String lastProjectName = null;
        for (Map.Entry<JProjects, String> entry : mProjectsNames.entrySet()) {
            if (projectKey.equals(entry.getKey().getKey())) {
                mLastProject = entry.getKey();
                lastProjectName = mLastProject.getName();
            }
        }
        getContentCallback.resultOfDataLoading(lastProjectName);
    }

    private void getProjectsResponse(GetContentCallback<HashMap<JProjects, String>> getContentCallback) {
        ContentFromBackend.getInstance().getProjectsAsynchronously(new ContentLoadingCallback<JProjectsResponse>() {
            @Override
            public void resultFromBackend(JProjectsResponse result, ContentConst tag, GetContentCallback getContentCallback) {
                if (tag == ContentConst.PROJECTS_RESPONSE) {
                    if (getContentCallback != null) {
                        if (result != null) {
                            for (int i = 0; i < result.getProjects().size(); i++) {
                                final int finalI = i;
                                result.getProjects().get(finalI).setIdUser(String.valueOf(ActiveUser.getInstance().getId()));

                                for (int j = 0; j < result.getProjects().get(finalI).getIssueTypes().size(); j++) {
                                    result.getProjects().get(finalI).getIssueTypes().get(j).setKeyProject(result.getProjects().get(i).getKey());
                                }

                                ContentFromDatabase.setIssueTypes(result.getProjects().get(finalI), new IResult<Integer>() {
                                    @Override
                                    public void onResult(Integer result) {
                                        Logger.d(TAG, "Project " + finalI + ", IssueTypes " + String.valueOf(result));
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Logger.e(TAG, "Project " + finalI + ", IssueTypes " + e.getMessage(), e);
                                    }
                                });
                            }
                            ContentFromDatabase.setProjects(result, new IResult<Integer>() {
                                @Override
                                public void onResult(Integer result) {
                                    Logger.d(TAG, "Projects " + String.valueOf(result));
                                }

                                @Override
                                public void onError(Exception e) {
                                    Logger.e(TAG, "Projects " + e.getMessage(), e);
                                }
                            });
                            getContentCallback.resultOfDataLoading(mProjectsNames);
                        } else {
                            getContentCallback.resultOfDataLoading(null);
                        }
                    }
                }
            }
        }, getContentCallback);
    }

    private void getProjectsSynchronously(final GetContentCallback<HashMap<JProjects, String>> getContentCallback) {
        ContentFromDatabase.getProjects(String.valueOf(ActiveUser.getInstance().getId()), new IResult<List<JProjects>>() {
            @Override
            public void onResult(List<JProjects> result) {
                if (result.isEmpty()) {
                    getProjectsResponse(getContentCallback);
                } else {
                    JProjectsResponse pro = new JProjectsResponse();
                    pro.setProjects(new ArrayList<>(result));
                    mProjectsNames = pro.getProjectsNames();
                    getContentCallback.resultOfDataLoading(mProjectsNames);
                }
            }

            @Override
            public void onError(Exception e) {
                getProjectsResponse(getContentCallback);
                Logger.e(TAG, e.getMessage(), e);
            }
        });
    }
    //endregion

    //region IssueTypes
    public void getIssueTypesNames(final GetContentCallback<List<String>> getContentCallback) {
        if (mIssueTypesNames != null) {
            Logger.d(TAG, "mIssueTypesNames != null");
            getContentCallback.resultOfDataLoading(mIssueTypesNames);
        } else {
            mIssueTypesNames = mLastProject.getIssueTypesNames();
            if (mIssueTypesNames != null) {
                Logger.d(TAG, "mLastProject.getIssueTypesNames()");
                getContentCallback.resultOfDataLoading(mIssueTypesNames);
            } else {
                getIssueTypesSynchronously(getContentCallback);
            }
        }
    }

    public String getIssueTypeIdByName(String issueName) {
        return mLastProject.getIssueTypeByName(issueName).getJiraId();
    }

    private void getIssueTypesSynchronously(final GetContentCallback<List<String>> getContentCallback) {
        ContentFromDatabase.getIssueTypes(mLastProject.getKey(), new IResult<List<JIssueTypes>>() {
            @Override
            public void onResult(List<JIssueTypes> result) {
                if (!result.isEmpty()) {
                    Logger.d(TAG, "ContentFromDatabase.getIssueTypes !result.isEmpty()");
                    mLastProject.setIssueTypes(new ArrayList<>(result));
                    mIssueTypesNames = mLastProject.getIssueTypesNames();
                    getContentCallback.resultOfDataLoading(mIssueTypesNames);
                }
            }

            @Override
            public void onError(Exception e) {
                Logger.e(TAG, e.getMessage(), e);
                getContentCallback.resultOfDataLoading(mIssueTypesNames);
            }
        });
    }
    //endregion

    //region Versions
    public void getVersionsNames(String projectKey,
                                 GetContentCallback<HashMap<String, String>> getContentCallback) {
        if (mProjectVersionsNames != null) {
            getContentCallback.resultOfDataLoading(mProjectVersionsNames);
        } else {
            getVersionsResponse(projectKey, getContentCallback);
        }
    }

    public void setVersionsNames(HashMap<String, String> versionsNames) {
        this.mProjectVersionsNames = versionsNames;
    }

    public String getVersionIdByName(String versionName) {
        String versionId = null;
        for (Map.Entry<String, String> entry : mProjectVersionsNames.entrySet()) {
            if (versionName.equals(entry.getValue())) {
                versionId = entry.getKey();
            }
        }
        return versionId;
    }

    private void getVersionsResponse(String projectsKey,
                                     GetContentCallback<HashMap<String, String>> getContentCallback) {
        ContentFromBackend.getInstance().getVersionsAsynchronously(projectsKey, new ContentLoadingCallback<JVersionsResponse>() {
            @Override
            public void resultFromBackend(JVersionsResponse result, ContentConst tag, GetContentCallback getContentCallback) {
                if (result != null) {
                    getContentCallback.resultOfDataLoading(mProjectVersionsNames);
                } else {
                    getContentCallback.resultOfDataLoading(null);
                }
            }
        }, getContentCallback);
    }
    //endregion

    //region Components
    public void getComponentsNames(String projectKey,
                                 GetContentCallback<HashMap<String, String>> getContentCallback) {
        if (mProjectComponentsNames != null) {
            getContentCallback.resultOfDataLoading(mProjectComponentsNames);
        } else {
            getComponentsResponse(projectKey, getContentCallback);
        }
    }

    public void setComponentsNames(HashMap<String, String> versionsNames) {
        this.mProjectComponentsNames = versionsNames;
    }

    public String getComponentIdByName(String versionName) {
        String versionId = null;
        if(versionName!= null) {
            for (Map.Entry<String, String> entry : mProjectComponentsNames.entrySet()) {
                if (versionName.equals(entry.getValue())) {
                    versionId = entry.getKey();
                }
            }
        }
        return versionId;
    }

    public String getComponentNameById(String versionId) {
        String versionName = null;
        if (versionId != null) {
            for (Map.Entry<String, String> entry : mProjectComponentsNames.entrySet()) {
                if (versionId.equals(entry.getKey())) {
                    versionName = entry.getValue();
                }
            }
        }
        return versionName;
    }

    private void getComponentsResponse(String projectsKey,
                                       GetContentCallback<HashMap<String, String>> getContentCallback) {
        ContentFromBackend.getInstance().getComponentsAsynchronously(projectsKey, new ContentLoadingCallback<JComponentsResponse>() {
            @Override
            public void resultFromBackend(JComponentsResponse result, ContentConst tag, GetContentCallback getContentCallback) {
                if (result != null) {
                    getContentCallback.resultOfDataLoading(mProjectComponentsNames);
                } else {
                    getContentCallback.resultOfDataLoading(null);
                }
            }
        }, getContentCallback);
    }
    //endregion

    //region UsersAssignable
    public void setUsersAssignableNames(List<String> usersAssignableNames){
        this.mUsersAssignableNames = usersAssignableNames;
    }

    public void getUsersAssignable(String userName,
                                   final GetContentCallback<List<String>> getContentCallback) {
        ContentFromBackend.getInstance().getUsersAssignableAsynchronously(mLastProject.getKey(), userName, new ContentLoadingCallback<JUserAssignableResponse>() {
            @Override
            public void resultFromBackend(JUserAssignableResponse result, ContentConst tag, GetContentCallback getContentCallback) {
                if (result != null) {
                    getContentCallback.resultOfDataLoading(mUsersAssignableNames);
                } else {
                    getContentCallback.resultOfDataLoading(null);
                }
            }
        }, getContentCallback);
    }
    //endregion

    //region Issue
    public void createIssue(String issueTypeName, String priorityName, String versionName, String summary,
                            String description, String environment, String userAssigneName, String componentsIds,
                            final GetContentCallback<JCreateIssueResponse> getContentCallback) {
        final String mProjectKey;
        final String issueTypeId;
        final String priorityId;
        String versionId = null;
        mProjectKey = mLastProject.getKey();
        priorityId = getPriorityIdByName(priorityName);
        issueTypeId = getIssueTypeIdByName(issueTypeName);
        if (versionName != null) {
            versionId = getVersionIdByName(versionName);
        }
        String issueJson = new JCreateIssue(mProjectKey, issueTypeId, description, summary, priorityId, versionId,
                environment, userAssigneName, componentsIds).getResultJson();
        ContentFromBackend.getInstance().createIssueAsynchronously(issueJson, new ContentLoadingCallback<JCreateIssueResponse>() {
            @Override
            public void resultFromBackend(JCreateIssueResponse result, ContentConst tag, GetContentCallback getContentCallback) {
                if (result != null) {
                    mRecentIssueKey = result.getKey();
                    Logger.d(TAG, mRecentIssueKey);
                }
                getContentCallback.resultOfDataLoading(result);
            }
        }, getContentCallback);
    }

    @SuppressWarnings("unchecked")
    public void sendAttachment(String issueKey, List<String> fullFileName,
                            final GetContentCallback<Boolean> getContentCallback) {
        ContentFromBackend.getInstance().sendAttachmentAsynchronously(issueKey, fullFileName, new ContentLoadingCallback<Boolean>() {
            @Override
            public void resultFromBackend(Boolean result, ContentConst tag, GetContentCallback getContentCallback) {
                getContentCallback.resultOfDataLoading(result);
            }
        }, getContentCallback);
    }

    public void getRecentIssueKey(final GetContentCallback<String> getContentCallback) {
        getContentCallback.resultOfDataLoading(mRecentIssueKey);
    }
    //endregion

    public void getDescription(final GetContentCallback<Spanned> getContentCallback) {
        DbObjectManager.INSTANCE.getAll(new Step(), new IResult<List<DatabaseEntity>>() {
            @Override
            public void onResult(List<DatabaseEntity> result) {
                Spanned description = StepUtil.getStepInfo(result);
                getContentCallback.resultOfDataLoading(description);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public void clearData() {
        mIssueTypesNames = null;
        mProjectsNames = null;
        mProjectPrioritiesNames = null;
        mProjectVersionsNames = null;
        mLastProject = null;
    }

    public void setDefaultConfig(final String lastProjectKey, final String lastAssignee, final String lastComponentsIds) {
        if (lastProjectKey != null) {
            StepUtil.checkUser(ActiveUser.getInstance().getUserName(), ActiveUser.getInstance().getUrl(), new IResult<List<JUserInfo>>() {
                @Override
                public void onResult(List<JUserInfo> result) {
                    for (JUserInfo userInfo : result) {
                        if (userInfo.getName().equals(ActiveUser.getInstance().getUserName())&&userInfo.getUrl().equals(ActiveUser.getInstance().getUrl())) {
                            JUserInfo user = result.get(0);
                            user.setLastProjectKey(lastProjectKey);
                            user.setLastAssigneeName(lastAssignee);
                            user.setLastComponentsIds(lastComponentsIds);
                            ContentFromDatabase.updateUser(user, new IResult<Integer>() {
                                @Override
                                public void onResult(Integer res) {
                                }

                                @Override
                                public void onError(Exception e) {

                                }
                            });
                        }
                    }
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }
    }

}
