package amtt.epam.com.amtt.ticket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import amtt.epam.com.amtt.bo.JCreateIssue;
import amtt.epam.com.amtt.bo.JCreateIssueResponse;
import amtt.epam.com.amtt.bo.JPriorityResponse;
import amtt.epam.com.amtt.bo.JProjectsResponse;
import amtt.epam.com.amtt.bo.JUserAssignableResponse;
import amtt.epam.com.amtt.bo.JVersionsResponse;
import amtt.epam.com.amtt.bo.issue.createmeta.JIssueTypes;
import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;
import amtt.epam.com.amtt.bo.project.JPriority;
import amtt.epam.com.amtt.database.object.IResult;
import amtt.epam.com.amtt.util.ActiveUser;
import amtt.epam.com.amtt.util.Logger;

/**
 @author Iryna Monchanka
 @version on 12.05.2015
 */

public class JiraContent{

    private final String TAG = this.getClass().getSimpleName();
    private ArrayList<String> mIssueTypesNames;
    private ArrayList<String> mUsersAssignableNames;
    private HashMap<JProjects, String> mProjectsNames;
    private HashMap<String, String> mProjectPrioritiesNames;
    private HashMap<String, String> mProjectVersionsNames;
    private JProjects mLastProject;
    private String mActivityInfo = "***activity info***";
    private String mSteps = "***steps***";
    private String mEnvironment;
    private String mLogs;
    private String mScreenshots;
    private String mRecentIssueKey;

    private static class JiraContentHolder {
        public static final JiraContent INSTANCE = new JiraContent();
    }

    public static JiraContent getInstance() {
        return JiraContentHolder.INSTANCE;
    }

    public void getPrioritiesNames(final JiraGetContentCallback<HashMap<String, String>> jiraGetContentCallback) {
        if (mProjectPrioritiesNames != null) {
            jiraGetContentCallback.resultOfDataLoading(mProjectPrioritiesNames);
        } else {
            ContentFromDatabase.getPriorities(ActiveUser.getInstance().getUrl(), new IResult<List<JPriority>>() {
                @Override
                public void onResult(List<JPriority> result) {
                    if (result.isEmpty()) {
                        getPriorities(jiraGetContentCallback);
                    } else {
                        JPriorityResponse pro = new JPriorityResponse();
                        pro.setPriorities(new ArrayList<>(result));
                        mProjectPrioritiesNames = pro.getPriorityNames();
                        jiraGetContentCallback.resultOfDataLoading(mProjectPrioritiesNames);
                    }
                }

                @Override
                public void onError(Exception e) {
                    getPriorities(jiraGetContentCallback);
                }
            });
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

    public void getProjectsNames(final JiraGetContentCallback<HashMap<JProjects, String>> jiraGetContentCallback) {
        if (mProjectsNames != null) {
            jiraGetContentCallback.resultOfDataLoading(mProjectsNames);
        } else {
            ContentFromDatabase.getProjects(ActiveUser.getInstance().getId(), new IResult<List<JProjects>>() {
                @Override
                public void onResult(List<JProjects> result) {
                    if (result.isEmpty()) {
                        getProjectsResponse(jiraGetContentCallback);
                    } else {
                        JProjectsResponse pro = new JProjectsResponse();
                        pro.setProjects(new ArrayList<>(result));
                        mProjectsNames = pro.getProjectsNames();
                        jiraGetContentCallback.resultOfDataLoading(mProjectsNames);
                    }
                }

                @Override
                public void onError(Exception e) {
                    getProjectsResponse(jiraGetContentCallback);
                }
            });
        }
    }

    public void setProjectsNames(HashMap<JProjects, String> projectsNames) {
        this.mProjectsNames = projectsNames;
    }

    public void getProjectKeyByName(String projectName, JiraGetContentCallback<String> jiraGetContentCallback) {
        for (Map.Entry<JProjects, String> entry : mProjectsNames.entrySet()) {
            if (projectName.equals(entry.getValue())) {
                mLastProject = entry.getKey();
                ActiveUser.getInstance().setLastProjectKey(mLastProject.getKey());
            }
        }
        mIssueTypesNames = null;
        mProjectVersionsNames = null;
        jiraGetContentCallback.resultOfDataLoading(ActiveUser.getInstance().getLastProjectKey());
    }

    public void getProjectNameByKey(String projectKey, final JiraGetContentCallback<String> jiraGetContentCallback) {
        ContentFromDatabase.getLastProject(projectKey, new IResult<List<JProjects>>() {
            @Override
            public void onResult(List<JProjects> result) {
                if (result.isEmpty()) {
                    jiraGetContentCallback.resultOfDataLoading(null);
                } else {
                    mLastProject = result.get(1);
                    jiraGetContentCallback.resultOfDataLoading(mLastProject.getName());
                }
            }

            @Override
            public void onError(Exception e) {
                jiraGetContentCallback.resultOfDataLoading(null);
            }
        });
    }

    public void getIssueTypesNames(final JiraGetContentCallback<ArrayList<String>> jiraGetContentCallback) {
        if (mIssueTypesNames != null) {
            jiraGetContentCallback.resultOfDataLoading(mIssueTypesNames);
        } else {
            mIssueTypesNames = mLastProject.getIssueTypesNames();
            if (mIssueTypesNames != null) {
                jiraGetContentCallback.resultOfDataLoading(mIssueTypesNames);
            } else {
                ContentFromDatabase.getIssueTypes(ActiveUser.getInstance().getLastProjectKey(), new IResult<List<JIssueTypes>>() {
                    @Override
                    public void onResult(List<JIssueTypes> result) {
                        if (result.isEmpty()) {
                            jiraGetContentCallback.resultOfDataLoading(null);
                        } else {
                            mLastProject.setIssueTypes(new ArrayList<>(result));
                            mIssueTypesNames = mLastProject.getIssueTypesNames();
                            jiraGetContentCallback.resultOfDataLoading(mIssueTypesNames);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        jiraGetContentCallback.resultOfDataLoading(null);
                    }
                });
            }
        }
    }

    public String getIssueTypeIdByName(String issueName) {
        return mLastProject.getIssueTypeByName(issueName).getJiraId();
    }

    public void getVersionsNames(String projectKey,
                                 JiraGetContentCallback<HashMap<String, String>> jiraGetContentCallback) {
        if (mProjectVersionsNames != null) {
            jiraGetContentCallback.resultOfDataLoading(mProjectVersionsNames);
        } else {
            getVersionsResponse(projectKey, jiraGetContentCallback);
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

    public void setUsersAssignableNames(ArrayList<String> usersAssignableNames){
        this.mUsersAssignableNames = usersAssignableNames;
    }

    @SuppressWarnings("unchecked")
    private void getProjectsResponse(JiraGetContentCallback<HashMap<JProjects, String>> jiraGetContentCallback) {
        ContentFromBackend.getInstance().getProjectsAsynchronously(new ContentLoadingCallback<JProjectsResponse>() {
            @Override
            public void resultFromBackend(JProjectsResponse result, JiraContentConst tag, JiraGetContentCallback jiraGetContentCallback) {
                if (tag == JiraContentConst.PROJECTS_RESPONSE) {
                    if (jiraGetContentCallback != null) {
                        if (result != null) {
                            jiraGetContentCallback.resultOfDataLoading(mProjectsNames);
                        } else {
                            jiraGetContentCallback.resultOfDataLoading(null);
                        }
                    }
                }
            }
        }, jiraGetContentCallback);
    }

    @SuppressWarnings("unchecked")
    private void getVersionsResponse(String projectsKey,
                                     JiraGetContentCallback<HashMap<String, String>> jiraGetContentCallback) {
        ContentFromBackend.getInstance().getVersionsAsynchronously(projectsKey, new ContentLoadingCallback<JVersionsResponse>() {
            @Override
            public void resultFromBackend(JVersionsResponse result, JiraContentConst tag, JiraGetContentCallback jiraGetContentCallback) {
                if (result != null) {
                    jiraGetContentCallback.resultOfDataLoading(mProjectVersionsNames);
                } else {
                    jiraGetContentCallback.resultOfDataLoading(null);
                }
            }
        }, jiraGetContentCallback);
    }

    @SuppressWarnings("unchecked")
    public void getUsersAssignable(String userName,
                                   final JiraGetContentCallback<ArrayList<String>> jiraGetContentCallback) {
        ContentFromBackend.getInstance().getUsersAssignableAsynchronously(ActiveUser.getInstance().getLastProjectKey(), userName, new ContentLoadingCallback<JUserAssignableResponse>() {
            @Override
            public void resultFromBackend(JUserAssignableResponse result, JiraContentConst tag, JiraGetContentCallback jiraGetContentCallback) {
                if (result != null) {
                    jiraGetContentCallback.resultOfDataLoading(mUsersAssignableNames);
                } else {
                    jiraGetContentCallback.resultOfDataLoading(null);
                }
            }
        }, jiraGetContentCallback);
    }

    @SuppressWarnings("unchecked")
    private void getPriorities(final JiraGetContentCallback<HashMap<String, String>> jiraGetContentCallback) {
        ContentFromBackend.getInstance().getPriorityAsynchronously(new ContentLoadingCallback<JPriorityResponse>() {
            @Override
            public void resultFromBackend(JPriorityResponse result, JiraContentConst tag, JiraGetContentCallback jiraGetContentCallback) {
                if (tag == JiraContentConst.PRIORITIES_RESPONSE) {
                    if (jiraGetContentCallback != null) {
                        if (result != null) {
                            JiraContent.getInstance().setPrioritiesNames(result.getPriorityNames());
                            jiraGetContentCallback.resultOfDataLoading(mProjectPrioritiesNames);
                        } else {
                            jiraGetContentCallback.resultOfDataLoading(null);
                        }
                    }
                }
            }
        }, jiraGetContentCallback);
    }

    @SuppressWarnings("unchecked")
    public void createIssue(String issueTypeName, String priorityName, String versionName, String summary,
                            String description, String environment, String userAssigneName,
                            final JiraGetContentCallback<JCreateIssueResponse> jiraGetContentCallback) {
        String mProjectKey, issueTypeId, priorityId, versionId;
        mProjectKey = ActiveUser.getInstance().getLastProjectKey();
        priorityId = getPriorityIdByName(priorityName);
        issueTypeId = getIssueTypeIdByName(issueTypeName);
        versionId = getVersionIdByName(versionName);
        String issueJson = new JCreateIssue(mProjectKey, issueTypeId, description, summary, priorityId, versionId,
                environment, userAssigneName).getResultJson();
        ContentFromBackend.getInstance().createIssueAsynchronously(issueJson, new ContentLoadingCallback<JCreateIssueResponse>() {
            @Override
            public void resultFromBackend(JCreateIssueResponse result, JiraContentConst tag, JiraGetContentCallback jiraGetContentCallback) {
                if(result!=null){
                mRecentIssueKey = result.getKey();
                Logger.d(TAG, mRecentIssueKey);
                }
                jiraGetContentCallback.resultOfDataLoading(result);
            }
        }, jiraGetContentCallback);
    }

    @SuppressWarnings("unchecked")
    public void sendAttachment(String issueKey, ArrayList<String> fullFileName,
                            final JiraGetContentCallback<Boolean> jiraGetContentCallback) {
        ContentFromBackend.getInstance().sendAttachmentAsynchronously(issueKey, fullFileName, new ContentLoadingCallback<Boolean>() {
            @Override
            public void resultFromBackend(Boolean result, JiraContentConst tag, JiraGetContentCallback jiraGetContentCallback) {
                jiraGetContentCallback.resultOfDataLoading(result);
            }
        }, jiraGetContentCallback);
    }

    public void getDescription(final JiraGetContentCallback<String> jiraGetContentCallback) {
        String description = mActivityInfo + "\n" + mSteps;
        jiraGetContentCallback.resultOfDataLoading(description);
    }

    public void getEnvironment(final JiraGetContentCallback<String> jiraGetContentCallback) {
        jiraGetContentCallback.resultOfDataLoading(mEnvironment);
    }

    public void setEnvironment(String environment) {
        this.mEnvironment = environment;
    }

    public void getActivityInfo(final JiraGetContentCallback<String> jiraGetContentCallback) {
        jiraGetContentCallback.resultOfDataLoading(mActivityInfo);
    }

    public void getSteps(final JiraGetContentCallback<String> jiraGetContentCallback) {
        jiraGetContentCallback.resultOfDataLoading(mSteps);
    }

    public void getLogs(final JiraGetContentCallback<String> jiraGetContentCallback) {
        jiraGetContentCallback.resultOfDataLoading(mLogs);
    }

    public void getScreenshots(final JiraGetContentCallback<String> jiraGetContentCallback) {
        jiraGetContentCallback.resultOfDataLoading(mScreenshots);
    }

    public void getRecentIssueKey(final JiraGetContentCallback<String> jiraGetContentCallback) {
        jiraGetContentCallback.resultOfDataLoading(mRecentIssueKey);
    }

    public void clearData() {
        mIssueTypesNames = null;
        mProjectsNames = null;
        mProjectPrioritiesNames = null;
        mProjectVersionsNames = null;
        mLastProject = null;
    }

}
