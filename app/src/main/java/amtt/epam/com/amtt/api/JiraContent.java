package amtt.epam.com.amtt.api;

import java.util.ArrayList;

import amtt.epam.com.amtt.api.exception.AmttException;
import amtt.epam.com.amtt.api.rest.RestMethod;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.bo.JMetaResponse;
import amtt.epam.com.amtt.bo.JPriorityResponse;
import amtt.epam.com.amtt.bo.JProjectExtVersionsResponse;
import amtt.epam.com.amtt.bo.JUserAssignableResponse;
import amtt.epam.com.amtt.bo.CreateIssue;
import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;
import amtt.epam.com.amtt.processing.PriorityProcessor;
import amtt.epam.com.amtt.processing.ProjectsProcessor;
import amtt.epam.com.amtt.processing.UsersAssignableProcessor;
import amtt.epam.com.amtt.processing.VersionsProcessor;

/**
 * Created by Iryna_Monchanka on 12.05.2015.
 */

public class JiraContent {

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

    public void getPrioritiesNames(JiraGetContentCallback<ArrayList<String>> interfaceSuccess) {
        if (mProjectPrioritiesNames != null) {
            interfaceSuccess.loadData(mProjectPrioritiesNames, JiraContentConst.PRIORITIES_NAMES);
        } else {
            getPriorityAsynchronously(interfaceSuccess);
        }
    }

    public String getPriorityIdByName(String priorityName) {
        return mProjectPriorities.getPriorityByName(priorityName).getId();
    }

    public void getProjectsNames(JiraGetContentCallback<ArrayList<String>> interfaceSuccess) {
        if (mProjectsNames != null) {
            interfaceSuccess.loadData(mProjectsNames, JiraContentConst.PROJECTS_NAMES);
        } else {
            getMetaAsynchronously(interfaceSuccess);
        }
    }

    public void getProjectKeyByName(String projectName, JiraGetContentCallback<String> interfaceSuccess) {
        mExtendProject = mMetaResponse.getProjectByName(projectName);
        mProjectKey = mExtendProject.getKey();
        mIssueTypesNames = null;
        mProjectVersions = null;
        mProjectVersionsNames = null;
        interfaceSuccess.loadData(mProjectKey, JiraContentConst.PROJECT_KEY_BY_NAME);
    }

    public void getIssueTypesNames(JiraGetContentCallback<ArrayList<String>> interfaceSuccess) {
        if (mIssueTypesNames != null) {
            interfaceSuccess.loadData(mIssueTypesNames, JiraContentConst.ISSUE_TYPES_NAMES);
        } else {
            mIssueTypesNames = mExtendProject.getIssueTypesNames();
            interfaceSuccess.loadData(mIssueTypesNames, JiraContentConst.ISSUE_TYPES_NAMES);
        }
    }

    public String getIssueTypeIdByName(String issueName) {
        return mExtendProject.getIssueTypeByName(issueName).getId();
    }

    public void getVersionsNames(String projectKey, JiraGetContentCallback<ArrayList<String>> interfaceSuccess) {
        if (mIssueTypesNames != null) {
            interfaceSuccess.loadData(mIssueTypesNames, JiraContentConst.VERSIONS_NAMES);
        } else {
            getVersionsAsynchronously(projectKey, interfaceSuccess);
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
    private void getMetaAsynchronously(final JiraGetContentCallback<ArrayList<String>> interfaceSuccess) {
        RestMethod<JMetaResponse> searchMethod = JiraApi.getInstance().buildDataSearch(JiraApiConst.USER_PROJECTS_PATH, new ProjectsProcessor());
        new JiraTask.Builder<JMetaResponse>()
                .setRestMethod(searchMethod)
                .setCallback(new JiraCallback() {
                    @Override
                    public void onRequestStarted() {
                    }
                    @Override
                    public void onRequestPerformed(RestResponse restResponse) {
                        mMetaResponse = (JMetaResponse) restResponse.getResultObject();
                        mProjectsNames = new ArrayList();
                        mProjectsNames = mMetaResponse.getProjectsNames();
                        interfaceSuccess.loadData(mProjectsNames, JiraContentConst.PROJECTS_NAMES);
                    }
                    @Override
                    public void onRequestError(AmttException e) {
                        JiraContentConst.error = e;
                        interfaceSuccess.loadData(mProjectsNames, JiraContentConst.PROJECTS_NAMES);
                    }
                })
                .createAndExecute();
    }


    @SuppressWarnings("unchecked")
    private void getVersionsAsynchronously(String projectsKey, final JiraGetContentCallback<ArrayList<String>> interfaceSuccess) {
        String path = JiraApiConst.PROJECT_VERSIONS_PATH + projectsKey + JiraApiConst.PROJECT_VERSIONS_PATH_V;
        RestMethod<JProjectExtVersionsResponse> searchMethod = JiraApi.getInstance().buildDataSearch(path, new VersionsProcessor());
        new JiraTask.Builder<JProjectExtVersionsResponse>()
                .setRestMethod(searchMethod)
                .setCallback(new JiraCallback() {
                    @Override
                    public void onRequestStarted() {
                    }
                    @Override
                    public void onRequestPerformed(RestResponse restResponse) {
                        mProjectVersions = (JProjectExtVersionsResponse) restResponse.getResultObject();
                        mProjectVersionsNames = new ArrayList();
                        mProjectVersionsNames = mProjectVersions.getVersionsNames();
                        interfaceSuccess.loadData(mProjectVersionsNames, JiraContentConst.VERSIONS_NAMES);
                    }

                    @Override
                    public void onRequestError(AmttException e) {
                    }

                })
                .createAndExecute();
    }

    @SuppressWarnings("unchecked")
    public void getUsersAssignableAsynchronously(String projectKey, final JiraGetContentCallback<ArrayList<String>> interfaceSuccess) {
        String path = JiraApiConst.USERS_ASSIGNABLE_PATH + projectKey;
        RestMethod<JUserAssignableResponse> searchMethod = JiraApi.getInstance().buildDataSearch(path, new UsersAssignableProcessor());
        new JiraTask.Builder<JUserAssignableResponse>()
                .setRestMethod(searchMethod)
                .setCallback(new JiraCallback() {
                    @Override
                    public void onRequestStarted() {
                    }
                    @Override
                    public void onRequestPerformed(RestResponse restResponse) {
                        mUsersAssignable = (JUserAssignableResponse) restResponse.getResultObject();
                        mUsersAssignableNames = new ArrayList();
                        mUsersAssignableNames = mUsersAssignable.getAssignableUsersNames();
                        interfaceSuccess.loadData(mUsersAssignableNames, JiraContentConst.USERS_ASSIGNABLE_NAMES);
                    }
                    @Override
                    public void onRequestError(AmttException e) {
                        interfaceSuccess.loadData(null, JiraContentConst.USERS_ASSIGNABLE_NAMES);
                    }
                })
                .createAndExecute();
    }

    @SuppressWarnings("unchecked")
    private void getPriorityAsynchronously(final JiraGetContentCallback<ArrayList<String>> interfaceSuccess) {
        String path = JiraApiConst.PROJECT_PRIORITY_PATH;
        RestMethod<JPriorityResponse> searchMethod = JiraApi.getInstance().buildDataSearch(path, new PriorityProcessor());
        new JiraTask.Builder<JPriorityResponse>()
                .setRestMethod(searchMethod)
                .setCallback(new JiraCallback() {
                    @Override
                    public void onRequestStarted() {
                    }

                    @Override
                    public void onRequestPerformed(RestResponse restResponse) {
                        mProjectPriorities = (JPriorityResponse) restResponse.getResultObject();
                        mProjectPrioritiesNames = new ArrayList();
                        mProjectPrioritiesNames = mProjectPriorities.getPriorityNames();
                        interfaceSuccess.loadData(mProjectPrioritiesNames, JiraContentConst.PRIORITIES_NAMES);
                    }

                    @Override
                    public void onRequestError(AmttException e) {
                    }

                })
                .createAndExecute();
    }

    @SuppressWarnings("unchecked")
    public void createIssueAsynchronously(String issueTypeName, String priorityName, String versionName, String summary, String description, String environment, final JiraGetContentCallback<Boolean> interfaceSuccess) {
        String mProjectKey, issueTypeId, priorityId, versionId;
        mProjectKey = mExtendProject.getKey();
        priorityId = getPriorityIdByName(priorityName);
        issueTypeId = getIssueTypeIdByName(issueTypeName);
        versionId = getVersionIdByName(versionName);
        RestMethod<JMetaResponse> createIssue = JiraApi.getInstance().buildIssueCeating(new CreateIssue(mProjectKey, issueTypeId, description, summary, priorityId, versionId, environment).getResultJson());
        new JiraTask.Builder<JMetaResponse>()
                .setRestMethod(createIssue)
                .setCallback(new JiraCallback() {
                    @Override
                    public void onRequestStarted() {
                    }
                    @Override
                    public void onRequestPerformed(RestResponse restResponse) {
                        mProjectVersions = (JProjectExtVersionsResponse) restResponse.getResultObject();
                        if (mProjectVersions != null) {
                            mProjectVersionsNames = new ArrayList();
                            mProjectVersionsNames = mProjectVersions.getVersionsNames();
                        }
                        interfaceSuccess.loadData(true, JiraContentConst.CREATE_ISSUE);

                    }
                    @Override
                    public void onRequestError(AmttException e) {
                        interfaceSuccess.loadData(false, JiraContentConst.CREATE_ISSUE);
                    }

                })
                .createAndExecute();
    }

}
