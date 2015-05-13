package amtt.epam.com.amtt.api;

import java.util.ArrayList;

import amtt.epam.com.amtt.api.exception.AmttException;
import amtt.epam.com.amtt.api.rest.RestMethod;
import amtt.epam.com.amtt.api.rest.RestResponse;
import amtt.epam.com.amtt.bo.JMetaResponse;
import amtt.epam.com.amtt.bo.JPriorityResponse;
import amtt.epam.com.amtt.bo.JProjectExtVersionsResponse;
import amtt.epam.com.amtt.bo.JUserAssignableResponse;
import amtt.epam.com.amtt.bo.issue.CreateIssue;
import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;
import amtt.epam.com.amtt.processing.PriorityProcessor;
import amtt.epam.com.amtt.processing.ProjectsProcessor;
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
    private JUserAssignableResponse mUserAssignable;
    private ArrayList<String> mUserAssignableNames;
    private JPriorityResponse mProjectPriorities;
    private ArrayList<String> mProjectPrioritiesNames;
    private JProjects mExtendProject;

    private static class JiraContentHolder {
        public static final JiraContent INSTANCE = new JiraContent();
    }

    public static JiraContent getInstance() {
        return JiraContentHolder.INSTANCE;
    }

    public void getPrioritiesNames(IContentSuccess<ArrayList<String>> interfaceSuccess) {
        if (mProjectPrioritiesNames != null) {
            interfaceSuccess.success(mProjectPrioritiesNames, JiraContentTypesConst.PRIORITIES_NAMES);
        } else {
            getPriorityAsynchronously(interfaceSuccess);
        }
    }

    public String getPriorityIdByName(String priorityName) {
        return mProjectPriorities.getPriorityByName(priorityName).getId();
    }

    public void getProjectsNames(IContentSuccess<ArrayList<String>> interfaceSuccess) {
        if (mProjectsNames != null) {
            interfaceSuccess.success(mProjectsNames, JiraContentTypesConst.PROJECTS_NAMES);
        } else {
            getMetaAsynchronously(interfaceSuccess);
        }
    }

    public void getProjectKeyByName(String projectName, IContentSuccess<String> interfaceSuccess) {
        mExtendProject = mMetaResponse.getProjectByName(projectName);
        mProjectKey = mExtendProject.getKey();
        mIssueTypesNames = null;
        mProjectVersions = null;
        mProjectVersionsNames = null;
        interfaceSuccess.success(mProjectKey, JiraContentTypesConst.PROJECT_KEY_BY_NAME);
    }

    public void getIssueTypesNames(String projectName, IContentSuccess<ArrayList<String>> interfaceSuccess) {
        if (mIssueTypesNames != null) {
            interfaceSuccess.success(mIssueTypesNames, JiraContentTypesConst.ISSUE_TYPES_NAMES);
        } else {
            mIssueTypesNames = mExtendProject.getIssueTypesNames();
            interfaceSuccess.success(mIssueTypesNames, JiraContentTypesConst.ISSUE_TYPES_NAMES);
        }
    }

    public String getIssueTypeIdByName(String issueName) {
        return mExtendProject.getIssueTypeByName(issueName).getId();
    }

    public void getVersionsNames(String projectKey, IContentSuccess<ArrayList<String>> interfaceSuccess) {
        if (mIssueTypesNames != null) {
            interfaceSuccess.success(mIssueTypesNames, JiraContentTypesConst.VERSIONS_NAMES);
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
    private void getMetaAsynchronously(final IContentSuccess<ArrayList<String>> interfaceSuccess) {
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
                        interfaceSuccess.success(mProjectsNames, JiraContentTypesConst.PROJECTS_NAMES);
                    }

                    @Override
                    public void onRequestError(AmttException e) {
                    }
                })
                .createAndExecute();
    }


    @SuppressWarnings("unchecked")
    private void getVersionsAsynchronously(String projectsKey, final IContentSuccess<ArrayList<String>> interfaceSuccess) {
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
                        interfaceSuccess.success(mProjectVersionsNames, JiraContentTypesConst.VERSIONS_NAMES);
                    }

                    @Override
                    public void onRequestError(AmttException e) {
                    }

                })
                .createAndExecute();
    }

    /**
     * @SuppressWarnings("unchecked") private void getUsersAssignableAsynchronously() {
     * String path = JiraApiConst.USERS_ASSIGNABLE_PATH + getProjectKey();
     * RestMethod<JUserAssignableResponse> searchMethod = JiraApi.getInstance().buildDataSearch(path, new UsersAssignableProcessor());
     * new JiraTask.Builder<JUserAssignableResponse>()
     * .setRestMethod(searchMethod)
     * .setCallback(new JiraCallback() {
     * @Override public void onRequestStarted() {
     * }
     * @Override public void onRequestPerformed(RestResponse restResponse) {
     * mProjectVersions = (JProjectExtVersionsResponse) restResponse.getResultObject();
     * mProjectVersionsNames = new ArrayList();
     * mProjectVersionsNames = mProjectVersions.getVersionsNames();
     * interfaceSuccess.success(mProjectVersionsNames);
     * }
     * @Override public void onRequestError(AmttException e) {
     * }
     * <p/>
     * })
     * .createAndExecute();
     * }
     */

    @SuppressWarnings("unchecked")
    private void getPriorityAsynchronously(final IContentSuccess<ArrayList<String>> interfaceSuccess) {
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
                        interfaceSuccess.success(mProjectPrioritiesNames, JiraContentTypesConst.PRIORITIES_NAMES);
                    }

                    @Override
                    public void onRequestError(AmttException e) {
                    }

                })
                .createAndExecute();
    }

    @SuppressWarnings("unchecked")
    public void createIssueAsynchronously(String issueTypeName, String priorityName, String versionName, String summary, String description, String environment, final IContentSuccess<Boolean> interfaceSuccess) {
        CreateIssue issue = new CreateIssue();
        String mProjectKey, issueTypeId, priorityId, versionId;
        mProjectKey = mExtendProject.getKey();
        priorityId = getPriorityIdByName(priorityName);
        issueTypeId = getIssueTypeIdByName(issueTypeName);
        versionId = getVersionIdByName(versionName);

        RestMethod<JMetaResponse> createIssue = JiraApi.getInstance().buildIssueCeating(issue.createSimpleIssue(mProjectKey, issueTypeId, description, summary, priorityId, versionId, environment));
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
                        interfaceSuccess.success(true, JiraContentTypesConst.CREATE_ISSUE);

                    }

                    @Override
                    public void onRequestError(AmttException e) {
                        interfaceSuccess.success(false, JiraContentTypesConst.CREATE_ISSUE);
                    }

                })
                .createAndExecute();
    }

}
