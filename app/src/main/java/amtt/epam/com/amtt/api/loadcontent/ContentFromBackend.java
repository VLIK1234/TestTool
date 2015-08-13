package amtt.epam.com.amtt.api.loadcontent;

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

    public void getProjects(final ContentLoadingCallback<JProjectsResponse> contentLoadingCallback,
                                          final GetContentCallback getContentCallback) {
        JiraApi.get().searchData(JiraApiConst.USER_PROJECTS_PATH,
                ProjectsProcessor.NAME,
                null,
                null,
                null,
                getCallback(ContentConst.PROJECTS_RESPONSE, contentLoadingCallback, getContentCallback));
    }

    public void getVersions(String projectsKey,
                                          final ContentLoadingCallback<JVersionsResponse> contentLoadingCallback,
                                          final GetContentCallback getContentCallback) {
        String path = JiraApiConst.PROJECT_VERSIONS_PATH + projectsKey + JiraApiConst.PROJECT_VERSIONS_PATH_V;
        JiraApi.get().searchData(path,
                VersionsProcessor.NAME,
                null,
                null,
                null,
                getCallback(ContentConst.VERSIONS_RESPONSE, contentLoadingCallback, getContentCallback));
    }

    public void getComponents(String projectsKey,
                                          final ContentLoadingCallback<JComponentsResponse> contentLoadingCallback,
                                          final GetContentCallback getContentCallback) {
        String path = JiraApiConst.PROJECT_COMPONENTS_PATH + projectsKey + JiraApiConst.PROJECT_COMPONENTS_PATH_C;
        JiraApi.get().searchData(path,
                ComponentsProcessor.NAME,
                null,
                null,
                null,
                getCallback(ContentConst.COMPONENTS_RESPONSE, contentLoadingCallback, getContentCallback));
    }

    public void getUsersAssignable(String projectKey,
                                                 String userName,
                                                 final ContentLoadingCallback<JUserAssignableResponse> contentLoadingCallback,
                                                 final GetContentCallback getContentCallback) {
        String path = JiraApiConst.USERS_ASSIGNABLE_PATH + projectKey + JiraApiConst.USERS_ASSIGNABLE_PATH_UN + userName + JiraApiConst.USERS_ASSIGNABLE_PATH_MR;
        JiraApi.get().searchData(path,
                UsersAssignableProcessor.NAME,
                null,
                null,
                null,
                getCallback(ContentConst.USERS_ASSIGNABLE_RESPONSE, contentLoadingCallback, getContentCallback));
    }

    public void getPriority(final ContentLoadingCallback<JPriorityResponse> contentLoadingCallback,
                                          final GetContentCallback getContentCallback) {
        String path = JiraApiConst.PROJECT_PRIORITY_PATH;
        JiraApi.get().searchData(path,
                PriorityProcessor.NAME,
                null,
                null,
                null,
                getCallback(ContentConst.PRIORITIES_RESPONSE, contentLoadingCallback, getContentCallback));


    }


    public void createIssue(String issueJson,
                                          final ContentLoadingCallback<JCreateIssueResponse> contentLoadingCallback,
                                          final GetContentCallback getContentCallback) {
        JiraApi.get().createIssue(issueJson,
                PostCreateIssueProcessor.NAME,
                getCallback(ContentConst.CREATE_ISSUE_RESPONSE, contentLoadingCallback, getContentCallback));
    }

    public void sendAttachment(String issueKey,
                                             List<String> fullFileName,
                                             final ContentLoadingCallback<Boolean> contentLoadingCallback,
                                             final GetContentCallback getContentCallback) {
        JiraApi.get().createAttachment(issueKey,
                fullFileName,
                getCallback(ContentConst.SEND_ATTACHMENT, true, false, contentLoadingCallback, getContentCallback));
    }

    private <Result> Callback getCallback(final ContentConst requestType,
                                          final Result successResult,
                                          final Result errorResult,
                                          final ContentLoadingCallback<Result> contentLoadingCallback,
                                          final GetContentCallback getContentCallback) {
        return new Callback<Result>() {
            @Override
            public void onLoadStart() {

            }

            @Override
            public void onLoadExecuted(Result result) {
                contentLoadingCallback.resultFromBackend(successResult, requestType, getContentCallback);
            }

            @Override
            public void onLoadError(Exception e) {
                contentLoadingCallback.resultFromBackend(errorResult, requestType, getContentCallback);
            }
        };
    }

    private <Result> Callback getCallback(final ContentConst requestType,
                                          final ContentLoadingCallback<Result> contentLoadingCallback,
                                          final GetContentCallback getContentCallback) {
        return new Callback<Result>() {
            @Override
            public void onLoadStart() {

            }

            @Override
            public void onLoadExecuted(Result result) {
                contentLoadingCallback.resultFromBackend(result, requestType, getContentCallback);
            }

            @Override
            public void onLoadError(Exception e) {
                contentLoadingCallback.resultFromBackend(null, requestType, getContentCallback);
            }
        };
    }

}
