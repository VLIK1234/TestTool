package amtt.epam.com.amtt.api.loadcontent;

import java.util.HashMap;
import java.util.List;

import amtt.epam.com.amtt.api.ContentLoadingCallback;
import amtt.epam.com.amtt.api.JiraApi;
import amtt.epam.com.amtt.api.JiraApiConst;
import amtt.epam.com.amtt.api.ContentConst;
import amtt.epam.com.amtt.api.GetContentCallback;
import amtt.epam.com.amtt.bo.JComponentsResponse;
import amtt.epam.com.amtt.bo.JCreateIssueResponse;
import amtt.epam.com.amtt.bo.JPriorityResponse;
import amtt.epam.com.amtt.bo.JProjectsResponse;
import amtt.epam.com.amtt.bo.JUserAssignableResponse;
import amtt.epam.com.amtt.bo.JVersionsResponse;
import amtt.epam.com.amtt.bo.issue.createmeta.JProjects;
import amtt.epam.com.amtt.common.Callback;
import amtt.epam.com.amtt.processing.ComponentsProcessor;
import amtt.epam.com.amtt.processing.PostCreateIssueProcessor;
import amtt.epam.com.amtt.processing.PriorityProcessor;
import amtt.epam.com.amtt.processing.ProjectsProcessor;
import amtt.epam.com.amtt.processing.UsersAssignableProcessor;
import amtt.epam.com.amtt.processing.VersionsProcessor;

/**
 * @author Iryna Monchanka
 * @version on 15.05.2015
 */

public class ContentFromBackend {

    private static class ContentFromBackendHolder {
        public static final ContentFromBackend INSTANCE = new ContentFromBackend();
    }

    public static ContentFromBackend getInstance() {
        return ContentFromBackendHolder.INSTANCE;
    }

    public void getProjects(final ContentLoadingCallback<JProjectsResponse, HashMap<JProjects, String>> loadingCallback,
                            final GetContentCallback<HashMap<JProjects, String>> contentCallback) {
        JiraApi.get().searchData(JiraApiConst.USER_PROJECTS_PATH, ProjectsProcessor.NAME,
                getCallback(ContentConst.PROJECTS_RESPONSE, loadingCallback, contentCallback));
    }

    public void getVersions(String projectsKey, final ContentLoadingCallback<JVersionsResponse, HashMap<String, String>> loadingCallback,
                            final GetContentCallback<HashMap<String, String>> contentCallback) {
        String path = JiraApiConst.PROJECT_VERSIONS_PATH + projectsKey + JiraApiConst.PROJECT_VERSIONS_PATH_V;
        JiraApi.get().searchData(path, VersionsProcessor.NAME,
                getCallback(ContentConst.VERSIONS_RESPONSE, loadingCallback, contentCallback));
    }

    public void getComponents(String projectsKey, final ContentLoadingCallback<JComponentsResponse, HashMap<String, String>> loadingCallback,
                              final GetContentCallback<HashMap<String, String>> contentCallback) {
        String path = JiraApiConst.PROJECT_COMPONENTS_PATH + projectsKey + JiraApiConst.PROJECT_COMPONENTS_PATH_C;
        JiraApi.get().searchData(path, ComponentsProcessor.NAME,
                getCallback(ContentConst.COMPONENTS_RESPONSE, loadingCallback, contentCallback));
    }

    public void getUsersAssignable(String projectKey, String userName, final ContentLoadingCallback<JUserAssignableResponse, List<String>> loadingCallback,
                                   final GetContentCallback<List<String>> contentCallback) {
        String path = JiraApiConst.USERS_ASSIGNABLE_PATH + projectKey + JiraApiConst.USERS_ASSIGNABLE_PATH_UN + userName + JiraApiConst.USERS_ASSIGNABLE_PATH_MR;
        JiraApi.get().searchData(path, UsersAssignableProcessor.NAME,
                getCallback(ContentConst.USERS_ASSIGNABLE_RESPONSE, loadingCallback, contentCallback));
    }

    public void getPriority(final ContentLoadingCallback<JPriorityResponse, HashMap<String, String>> loadingCallback,
                            final GetContentCallback contentCallback) {
        String path = JiraApiConst.PROJECT_PRIORITY_PATH;
        JiraApi.get().searchData(path, PriorityProcessor.NAME,
                getCallback(ContentConst.PRIORITIES_RESPONSE, loadingCallback, contentCallback));
    }


    public void createIssue(String issueJson, final ContentLoadingCallback<JCreateIssueResponse, JCreateIssueResponse> loadingCallback,
                            final GetContentCallback<JCreateIssueResponse> contentCallback) {
        JiraApi.get().createIssue(issueJson, PostCreateIssueProcessor.NAME,
                getCallback(ContentConst.CREATE_ISSUE_RESPONSE, loadingCallback, contentCallback));
    }

    public void sendAttachment(String issueKey, List<String> fullFileName, final ContentLoadingCallback<Boolean, Boolean> loadingCallback,
                               final GetContentCallback<Boolean> contentCallback) {
        JiraApi.get().createAttachment(issueKey, fullFileName,
                getCallback(ContentConst.SEND_ATTACHMENT, true, false, loadingCallback, contentCallback));
    }

    private <Result> Callback getCallback(final ContentConst requestType, final Result successResult, final Result errorResult,
                                          final ContentLoadingCallback loadingCallback, final GetContentCallback contentCallback) {
        return new Callback<Result>() {
            @Override
            public void onLoadStart() {

            }

            @Override
            public void onLoadExecuted(Result result) {
                loadingCallback.resultFromBackend(successResult, requestType, contentCallback);
            }

            @Override
            public void onLoadError(Exception e) {
                loadingCallback.resultFromBackend(errorResult, requestType, contentCallback);
            }
        };
    }

    private <Result> Callback getCallback(final ContentConst requestType, final ContentLoadingCallback loadingCallback,
                                          final GetContentCallback contentCallback) {
        return new Callback<Result>() {
            @Override
            public void onLoadStart() {

            }

            @Override
            public void onLoadExecuted(Result result) {
                loadingCallback.resultFromBackend(result, requestType, contentCallback);
            }

            @Override
            public void onLoadError(Exception e) {
                loadingCallback.resultFromBackend(null, requestType, contentCallback);
            }
        };
    }

}
