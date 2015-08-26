package amtt.epam.com.amtt.api.loadcontent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import amtt.epam.com.amtt.api.ContentLoadingCallback;
import amtt.epam.com.amtt.api.GetContentCallback;
import amtt.epam.com.amtt.bo.JComponentsResponse;
import amtt.epam.com.amtt.bo.JCreateIssue;
import amtt.epam.com.amtt.bo.JCreateIssueResponse;
import amtt.epam.com.amtt.bo.JPriorityResponse;
import amtt.epam.com.amtt.bo.JProjectsResponse;
import amtt.epam.com.amtt.bo.JUserAssignableResponse;
import amtt.epam.com.amtt.bo.JVersionsResponse;
import amtt.epam.com.amtt.bo.issue.createmeta.JIssueTypes;
import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;
import amtt.epam.com.amtt.bo.project.JPriority;
import amtt.epam.com.amtt.bo.user.JUserInfo;
import amtt.epam.com.amtt.database.object.IResult;
import amtt.epam.com.amtt.database.util.LocalContent;
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
    public void getPrioritiesNames(String userUrl, final GetContentCallback<HashMap<String, String>> getContentCallback) {
        if (mProjectPrioritiesNames != null) {
            getContentCallback.resultOfDataLoading(mProjectPrioritiesNames);
        } else {
            getPrioritiesFromDB(userUrl, getContentCallback);
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

    private void getPriorities(final String userUrl, final GetContentCallback<HashMap<String, String>> getContentCallback) {
        ContentFromBackend.getInstance().getPriority(new ContentLoadingCallback<JPriorityResponse, HashMap<String, String>>() {
            @Override
            public void resultFromBackend(JPriorityResponse result, GetContentCallback<HashMap<String, String>> contentCallback) {
                if (result != null) {
                    mPriorityResponse = result;
                    setPrioritiesNames(mPriorityResponse.getPriorityNames());
                    for (int i = 0; i < mPriorityResponse.getPriorities().size(); i++) {
                        mPriorityResponse.getPriorities().get(i).setUrl(userUrl);
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
                    contentCallback.resultOfDataLoading(mProjectPrioritiesNames);
                } else {
                    contentCallback.resultOfDataLoading(null);
                }
            }
        }, getContentCallback);
    }

    private void getPrioritiesFromDB(final String userUrl, final GetContentCallback<HashMap<String, String>> getContentCallback) {
        ContentFromDatabase.getPriorities(userUrl, new IResult<List<JPriority>>() {
            @Override
            public void onResult(List<JPriority> result) {
                if (result.isEmpty()) {
                    getPriorities(userUrl, getContentCallback);
                } else {
                    mPriorityResponse = new JPriorityResponse();
                    mPriorityResponse.setPriorities(new ArrayList<>(result));
                    setPrioritiesNames(mPriorityResponse.getPriorityNames());
                    getContentCallback.resultOfDataLoading(mProjectPrioritiesNames);
                }
            }

            @Override
            public void onError(Exception e) {
                getPriorities(userUrl, getContentCallback);
                Logger.e(TAG, e.getMessage(), e);
            }
        });
    }
    //endregion

    //region Projects
    public void getProjectsNames(int userId, final GetContentCallback<HashMap<JProjects, String>> getContentCallback) {
        if (mProjectsNames != null) {
            getContentCallback.resultOfDataLoading(mProjectsNames);
        } else {
            getProjectsFromDB(userId, getContentCallback);
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

    private void getProjectsFromBackend(final int userId, GetContentCallback<HashMap<JProjects, String>> getContentCallback) {
        ContentFromBackend.getInstance().getProjects(new ContentLoadingCallback<JProjectsResponse, HashMap<JProjects, String>>() {
            @Override
            public void resultFromBackend(JProjectsResponse result, GetContentCallback<HashMap<JProjects, String>> contentCallback) {
                if (result != null) {
                    for (int i = 0; i < result.getProjects().size(); i++) {
                        final int finalI = i;
                        result.getProjects().get(finalI).setIdUser(String.valueOf(userId));

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
                    contentCallback.resultOfDataLoading(mProjectsNames);
                } else {
                    contentCallback.resultOfDataLoading(null);
                }
            }

        }, getContentCallback);
    }

    private void getProjectsFromDB(final int userId, final GetContentCallback<HashMap<JProjects, String>> getContentCallback) {
        ContentFromDatabase.getProjects(String.valueOf(userId), new IResult<List<JProjects>>() {
            @Override
            public void onResult(List<JProjects> result) {
                if (result.isEmpty()) {
                    getProjectsFromBackend(userId, getContentCallback);
                } else {
                    JProjectsResponse projectsResponse = new JProjectsResponse();
                    projectsResponse.setProjects(new ArrayList<>(result));
                    mProjectsNames = projectsResponse.getProjectsNames();
                    getContentCallback.resultOfDataLoading(mProjectsNames);
                }
            }

            @Override
            public void onError(Exception e) {
                Logger.e(TAG, e.getMessage(), e);
                getProjectsFromBackend(userId, getContentCallback);
            }
        });
    }
    //endregion

    //region IssueTypes
    public void getIssueTypesNames(final GetContentCallback<List<String>> getContentCallback) {
        if (mIssueTypesNames != null) {
            getContentCallback.resultOfDataLoading(mIssueTypesNames);
        } else {
            mIssueTypesNames = mLastProject.getIssueTypesNames();
            if (mIssueTypesNames != null) {
                getContentCallback.resultOfDataLoading(mIssueTypesNames);
            } else {
                getIssueTypesFromDB(getContentCallback);
            }
        }
    }

    public String getIssueTypeIdByName(String issueName) {
        return mLastProject.getIssueTypeByName(issueName).getJiraId();
    }

    private void getIssueTypesFromDB(final GetContentCallback<List<String>> getContentCallback) {
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
            getVersionsFromBackend(projectKey, getContentCallback);
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

    private void getVersionsFromBackend(String projectsKey, GetContentCallback<HashMap<String, String>> getContentCallback) {
        ContentFromBackend.getInstance().getVersions(projectsKey, new ContentLoadingCallback<JVersionsResponse, HashMap<String, String>>() {
            @Override
            public void resultFromBackend(JVersionsResponse result, GetContentCallback<HashMap<String, String>> contentCallback) {
                if (result != null) {
                    contentCallback.resultOfDataLoading(mProjectVersionsNames);
                } else {
                    contentCallback.resultOfDataLoading(null);
                }
            }
        }, getContentCallback);
    }
    //endregion

    //region Components
    public void getComponentsNames(String projectKey, GetContentCallback<HashMap<String, String>> getContentCallback) {
        if (mProjectComponentsNames != null) {
            getContentCallback.resultOfDataLoading(mProjectComponentsNames);
        } else {
            getComponentsFromBackend(projectKey, getContentCallback);
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

    private void getComponentsFromBackend(String projectsKey, GetContentCallback<HashMap<String, String>> getContentCallback) {
        ContentFromBackend.getInstance().getComponents(projectsKey, new ContentLoadingCallback<JComponentsResponse, HashMap<String, String>>() {
            @Override
            public void resultFromBackend(JComponentsResponse result, GetContentCallback<HashMap<String, String>> contentCallback) {
                if (result != null) {
                    contentCallback.resultOfDataLoading(mProjectComponentsNames);
                } else {
                    contentCallback.resultOfDataLoading(null);
                }
            }
        }, getContentCallback);
    }
    //endregion

    //region UsersAssignable
    public void setUsersAssignableNames(List<String> usersAssignableNames){
        this.mUsersAssignableNames = usersAssignableNames;
    }

    public void getUsersAssignable(String userName, final GetContentCallback<List<String>> getContentCallback) {
        ContentFromBackend.getInstance().getUsersAssignable(mLastProject.getKey(), userName, new ContentLoadingCallback<JUserAssignableResponse, List<String>>() {
            @Override
            public void resultFromBackend(JUserAssignableResponse result, GetContentCallback<List<String>> contentCallback) {
                if (result != null) {
                    contentCallback.resultOfDataLoading(mUsersAssignableNames);
                } else {
                    contentCallback.resultOfDataLoading(null);
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
        ContentFromBackend.getInstance().createIssue(issueJson, new ContentLoadingCallback<JCreateIssueResponse, JCreateIssueResponse>() {
            @Override
            public void resultFromBackend(JCreateIssueResponse result, GetContentCallback<JCreateIssueResponse> contentCallback) {
                if (result != null) {
                    mRecentIssueKey = result.getKey();
                    Logger.d(TAG, mRecentIssueKey);
                }
                contentCallback.resultOfDataLoading(result);
            }
        }, getContentCallback);
    }

    @SuppressWarnings("unchecked")
    public void sendAttachment(String issueKey, List<String> fullFileName, final GetContentCallback<Boolean> getContentCallback) {
        ContentFromBackend.getInstance().sendAttachment(issueKey, fullFileName, new ContentLoadingCallback<Boolean, Boolean>() {
            @Override
            public void resultFromBackend(Boolean result, GetContentCallback<Boolean> contentCallback) {
                contentCallback.resultOfDataLoading(result);
            }
        }, getContentCallback);
    }

    public void getRecentIssueKey(final GetContentCallback<String> getContentCallback) {
        getContentCallback.resultOfDataLoading(mRecentIssueKey);
    }
    //endregion
    public void clearData() {
        mIssueTypesNames = null;
        mProjectsNames = null;
        mProjectPrioritiesNames = null;
        mProjectVersionsNames = null;
        mLastProject = null;
    }

    public void setDefaultConfig(final int userId, final String userName, final String userUrl, final String lastProjectKey, final String lastAssignee, final String lastComponentsIds) {
        if (lastProjectKey != null) {
            LocalContent.checkUser(userName, userUrl, new IResult<List<JUserInfo>>() {
                @Override
                public void onResult(List<JUserInfo> result) {
                    for (JUserInfo userInfo : result) {
                        if (userInfo.getName().equals(userName) && userInfo.getUrl().equals(userUrl)) {
                            JUserInfo user = result.get(0);
                            user.setLastProjectKey(lastProjectKey);
                            user.setLastAssigneeName(lastAssignee);
                            user.setLastComponentsIds(lastComponentsIds);
                            LocalContent.updateUser(userId, user, new IResult<Integer>() {
                                @Override
                                public void onResult(Integer res) {}

                                @Override
                                public void onError(Exception e) {
                                    Logger.e(TAG, e.getMessage(), e);
                                }
                            });
                        }
                    }
                }

                @Override
                public void onError(Exception e) {
                    Logger.e(TAG, e.getMessage(), e);
                }
            });
        }
    }

}
