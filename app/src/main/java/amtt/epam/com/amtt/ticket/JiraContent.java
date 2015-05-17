package amtt.epam.com.amtt.ticket;

import amtt.epam.com.amtt.bo.*;
import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;

import java.util.ArrayList;

/**
 * Created by Iryna_Monchanka on 12.05.2015.
 */

public class JiraContent implements ContentLoadingCallback{

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
    private JProjects mExtendProject;

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
        mExtendProject = mMetaResponse.getProjectByName(projectName);
        mProjectKey = mExtendProject.getKey();
        mIssueTypesNames = null;
        mProjectVersions = null;
        mProjectVersionsNames = null;
        jiraGetContentCallback.resultOfDataLoading(mProjectKey, JiraContentConst.PROJECT_KEY_BY_NAME);
    }

    public void getIssueTypesNames(JiraGetContentCallback<ArrayList<String>> jiraGetContentCallback) {
        if (mIssueTypesNames != null) {
            jiraGetContentCallback.resultOfDataLoading(mIssueTypesNames, JiraContentConst.ISSUE_TYPES_NAMES);
        } else {
            mIssueTypesNames = mExtendProject.getIssueTypesNames();
            jiraGetContentCallback.resultOfDataLoading(mIssueTypesNames, JiraContentConst.ISSUE_TYPES_NAMES);
        }
    }

    public String getIssueTypeIdByName(String issueName) {
        return mExtendProject.getIssueTypeByName(issueName).getId();
    }

    public void getVersionsNames(String projectKey,
                                 JiraGetContentCallback<ArrayList<String>> jiraGetContentCallback) {
        if (mIssueTypesNames != null) {
            jiraGetContentCallback.resultOfDataLoading(mIssueTypesNames, JiraContentConst.VERSIONS_NAMES);
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
    private void getMetaResponse(JiraGetContentCallback<ArrayList<String>> jiraGetContentCallback){
        ContentFromBackend.getInstance().getMetaAsynchronously(this, jiraGetContentCallback);
    }

    @SuppressWarnings("unchecked")
    private void getVersionsResponse(String projectsKey,
                                     JiraGetContentCallback<ArrayList<String>> jiraGetContentCallback){
        ContentFromBackend.getInstance().getVersionsAsynchronously(projectsKey, this, jiraGetContentCallback);
    }

    @SuppressWarnings("unchecked")
    public void getUsersAssignable(String projectKey,
                                   final JiraGetContentCallback<ArrayList<String>> jiraGetContentCallback) {
        ContentFromBackend.getInstance().getUsersAssignableAsynchronously(projectKey, this, jiraGetContentCallback);
    }

    @SuppressWarnings("unchecked")
    private void getPriorities(final JiraGetContentCallback<ArrayList<String>> jiraGetContentCallback) {
        ContentFromBackend.getInstance().getPriorityAsynchronously(this, jiraGetContentCallback);    }

    @SuppressWarnings("unchecked")
    public void createIssue(String issueTypeName, String priorityName, String versionName, String summary,
                            String description, String environment,
                            final JiraGetContentCallback<Boolean> jiraGetContentCallback) {
        String mProjectKey, issueTypeId, priorityId, versionId;
        mProjectKey = mExtendProject.getKey();
        priorityId = getPriorityIdByName(priorityName);
        issueTypeId = getIssueTypeIdByName(issueTypeName);
        versionId = getVersionIdByName(versionName);
        String issueJson = new CreateIssue(mProjectKey, issueTypeId, description, summary, priorityId, versionId,
            environment).getResultJson();
        ContentFromBackend.getInstance().createIssueAsynchronously(issueJson, this, jiraGetContentCallback);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void resultFromBackend(Object result, JiraContentConst tagResult, JiraGetContentCallback jiraGetContentCallback) {
        if (tagResult == JiraContentConst.META_RESPONSE) {
            if (result != null) {
                mMetaResponse = (JMetaResponse) result;
                mProjectsNames = new ArrayList();
                mProjectsNames = mMetaResponse.getProjectsNames();
            }
            jiraGetContentCallback.resultOfDataLoading(mProjectsNames, JiraContentConst.PROJECTS_NAMES);
        } else if (tagResult == JiraContentConst.VERSIONS_RESPONSE) {
            if (result != null) {
                mProjectVersions = (JProjectExtVersionsResponse) result;
                mProjectVersionsNames = new ArrayList();
                mProjectVersionsNames = mProjectVersions.getVersionsNames();
            }
            jiraGetContentCallback.resultOfDataLoading(mProjectVersionsNames, JiraContentConst.VERSIONS_NAMES);
        } else if (tagResult == JiraContentConst.CREATE_ISSUE_RESPONSE) {
            jiraGetContentCallback.resultOfDataLoading((Boolean) result, JiraContentConst.CREATE_ISSUE);
        } else if (tagResult == JiraContentConst.PRIORITIES_RESPONSE) {
            mProjectPriorities = (JPriorityResponse) result;
            mProjectPrioritiesNames = new ArrayList();
            mProjectPrioritiesNames = mProjectPriorities.getPriorityNames();
            jiraGetContentCallback.resultOfDataLoading(mProjectPrioritiesNames, JiraContentConst.PRIORITIES_NAMES);
        } else if (tagResult == JiraContentConst.USERS_ASSIGNABLE_RESPONSE) {
            if (result != null) {
                mUsersAssignable = (JUserAssignableResponse) result;
                mUsersAssignableNames = new ArrayList();
                mUsersAssignableNames = mUsersAssignable.getAssignableUsersNames();
            }
            jiraGetContentCallback.resultOfDataLoading(mUsersAssignableNames, JiraContentConst.USERS_ASSIGNABLE_NAMES);
        } else {

        }
    }

}
